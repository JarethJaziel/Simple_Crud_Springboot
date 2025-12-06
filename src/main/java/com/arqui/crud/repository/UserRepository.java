package com.arqui.crud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arqui.crud.entity.User;

/**
 * Componente de Acceso a Datos (Repository) para la entidad {@link User}.
 * <p>
 * Esta interfaz actúa como un puente entre la aplicación Java y la base de datos MySQL.
 * Al extender de {@link JpaRepository}, hereda automáticamente una amplia gama de funcionalidades
 * sin necesidad de escribir sentencias SQL manualmente.
 * </p>
 * * <b>Características principales:</b>
 * <ul>
 * <li><b>CRUD Estándar:</b> Métodos listos para usar como save(), findById(), findAll(), deleteById().</li>
 * <li><b>Paginación y Ordenamiento:</b> Soporte nativo para devolver datos paginados (Page/Pageable).</li>
 * <li><b>Manejo de Transacciones:</b> Spring gestiona la apertura y cierre de conexiones automáticamente.</li>
 * </ul>
 * * <p>
 * La anotación {@code @Repository} marca esta interfaz como un Bean de Spring, permitiendo
 * la inyección de dependencias y la traducción automática de excepciones de base de datos.
 * </p>
 *
 * @see JpaRepository
 * @see User
 * @author Braulio Cuevas
 * @author Mauricio Dzay
 * @author Jareth Moo
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca usuarios que coincidan parcialmente por nombre O por email, sin distinguir mayúsculas de minúsculas.
     * <p>
     * Este método utiliza la estrategia de "Derivación de Consultas" (Query Derivation) de Spring Data JPA.
     * Al analizar el nombre del método, Spring genera automáticamente una consulta SQL similar a:
     * </p>
     * <pre>
     * SELECT * FROM users 
     * WHERE UPPER(name) LIKE UPPER('%param1%') 
     * OR UPPER(email) LIKE UPPER('%param2%')
     * </pre>
     *
     * @param name El texto a buscar dentro de la columna del nombre.
     * @param email El texto a buscar dentro de la columna del email.
     * (Nota: En una búsqueda general, se suele enviar la misma palabra clave a ambos parámetros).
     * @return Una lista de objetos {@link User} que cumplen con cualquiera de las dos condiciones.
     */
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}
