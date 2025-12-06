package com.arqui.crud.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.arqui.crud.entity.User;
import com.arqui.crud.repository.UserRepository;
import com.arqui.crud.util.UserPDFExporter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controlador encargado de gestionar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@link User}.
 * <p>
 * Este controlador maneja las peticiones web, interactúa con la base de datos
 * a través del repositorio y decide qué vista (HTML) mostrar al usuario.
 * </p>
 * 
 * @author Braulio Cuevas
 * @author Mauricio Dzay
 * @author Jareth Moo
 * @version 1.0
 */

@Controller
public class UserController {

    /**
     * Inyección de dependencia del repositorio de usuarios.
     * Permite realizar operaciones SQL (SELECT, INSERT, DELETE) sin escribir código SQL.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Maneja la petición GET a la raíz del sitio ("/") y gestiona el listado de usuarios.
     * <p>
     * Este método actúa como el punto de entrada principal y tiene una lógica condicional:
     * </p>
     * <ul>
     * <li><b>Modo Búsqueda:</b> Si el parámetro {@code keyword} no es nulo, filtra los resultados buscando coincidencias parciales en el nombre o el email.</li>
     * <li><b>Modo Listado Completo:</b> Si no hay keyword, recupera todos los registros de la base de datos.</li>
     * </ul>
     * <p>
     * También se encarga de inyectar un objeto {@code User} vacío en el modelo, el cual es requerido
     * por el formulario del modal "Agregar Usuario" para el binding de datos.
     * </p>
     *
     * @param model Contenedor de datos que viaja desde el controlador hacia la vista HTML (Thymeleaf).
     * @param keyword (Opcional) Término de búsqueda recibido como parámetro de la URL (ej: {@code /?keyword=juan}).
     * Puede ser null si el usuario entra directamente a la página.
     * @return La cadena "index", que indica a Spring Boot que debe renderizar la plantilla {@code index.html}.
     */
    @GetMapping("/")
    public String listUsers(Model model, @RequestParam(required = false) String keyword) {

        List<User> userList;
        if (keyword != null && !keyword.isEmpty()) {
            userList = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
        } else {
            userList = userRepository.findAll();
        }

        model.addAttribute("users", userList);

        model.addAttribute("user", new User());

        return "index";
    }

    /**
     * Maneja la petición POST para guardar o actualizar un usuario.
     * <p>
     * Si el objeto User tiene un ID nulo, se crea un nuevo registro.
     * Si el objeto User tiene un ID existente, se actualiza el registro correspondiente.
     * </p>
     *
     * @param user El objeto usuario poblado automáticamente con los datos del formulario HTML via @ModelAttribute.
     * @return Una instrucción de redirección a la raíz ("/") para refrescar la lista de usuarios.
     */
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/";
    }
    
    /**
     * Procesa la solicitud para eliminar un único usuario de la base de datos.
     * <p>
     * Este método recibe el ID como un parámetro de la petición (Query Param o Form Data),
     * no como parte de la URL. Es útil para eliminaciones individuales disparadas desde
     * un formulario o botón simple.
     * </p>
     *
     * @param id El identificador único (Primary Key) del usuario que se desea borrar.
     * Spring extrae este valor del parámetro "id" del formulario enviado.
     * @return Redirección a la vista principal ("/") para actualizar la tabla.
     */
    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {        
        userRepository.deleteById(id);
        return "redirect:/";
    }

    /**
     * Ejecuta la eliminación masiva (batch delete) de múltiples usuarios seleccionados.
     * <p>
     * Spring Boot convierte automáticamente una cadena de IDs separados por comas
     * (ej: "1,5,10") proveniente del input oculto del formulario en una {@link List} de Java.
     * Utiliza {@code deleteAllById} de JPA para optimizar la operación en la base de datos.
     * </p>
     *
     * @param ids Lista de identificadores (Long) de todos los usuarios seleccionados para eliminar.
     * @return Redirección a la vista principal ("/") mostrando la lista actualizada sin los usuarios borrados.
     */
    @PostMapping("/delete-batch")
    public String deleteBatch(@RequestParam List<Long> ids) {
        userRepository.deleteAllById(ids);
        return "redirect:/";
    }
    
    /**
     * Genera y descarga un reporte en formato PDF con el listado completo de usuarios.
     * <p>
     * Este método intercepta la petición GET en "/export/pdf". En lugar de devolver una vista HTML,
     * configura la respuesta HTTP para entregar un archivo binario. Realiza tres acciones clave:
     * </p>
     * <ol>
     * <li>Establece el tipo MIME a {@code application/pdf}.</li>
     * <li>Configura el encabezado {@code Content-Disposition} como "attachment" para forzar la descarga inmediata con el nombre "usuarios_reporte.pdf".</li>
     * <li>Obtiene todos los usuarios de la BD y utiliza la clase utilitaria {@link UserPDFExporter} para escribir el documento en el flujo de salida.</li>
     * </ol>
     *
     * @param response Objeto {@link HttpServletResponse} inyectado por Spring.
     * Es fundamental aquí porque permite escribir directamente en el {@code OutputStream}
     * del navegador, saltándose el motor de plantillas Thymeleaf.
     */
    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) {
        response.setContentType("application/pdf");
        
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=usuarios_reporte.pdf";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = userRepository.findAll();

        UserPDFExporter exporter = new UserPDFExporter(listUsers);
        try {
            exporter.export(response);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
