package com.arqui.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.arqui.crud.entity.User;
import com.arqui.crud.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador encargado de gestionar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad {@link User}.
 * <p>
 * Este controlador maneja las peticiones web, interactúa con la base de datos
 * a través del repositorio y decide qué vista (HTML) mostrar al usuario.
 * </p>
 *
 * @author Jaret
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
     * Maneja la petición GET a la raíz del sitio ("/").
     * <p>
     * Este método prepara dos cosas para la vista:
     * 1. Una lista completa de usuarios para llenar la tabla.
     * 2. Un objeto User vacío ("newUser") para que el formulario del modal pueda usarse.
     * </p>
     *
     * @param model Objeto de Spring que permite pasar datos del controlador (Java) a la vista (Thymeleaf).
     * @return El nombre lógico de la vista "index" (busca index.html en templates).
     */
    @GetMapping("/")
    public String listUsers(Model model) {

        model.addAttribute("users", userRepository.findAll());

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
    public String saveUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/";
    }
    
    /**
     * Maneja la petición para eliminar un usuario específico.
     *
     * @param id El identificador único (Primary Key) del usuario a eliminar, extraído de la URL.
     * @return Una instrucción de redirección a la raíz ("/") después de borrar el registro.
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {        
        userRepository.deleteById(id);
        return "redirect:/";
    }

}
