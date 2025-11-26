package com.arqui.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arqui.crud.entity.User;

/**
 * Componente de Acceso a Datos (DAO) para la entidad {@link User}.
 * <p>
 * Esta interfaz extiende de {@link JpaRepository}, lo que le otorga superpoderes inmediatos
 * para interactuar con la base de datos sin escribir SQL manual.
 * </p>
 * * <h3>Funcionalidades heredadas:</h3>
 * <ul>
 * <li>Operaciones CRUD estándar (save, findById, delete, etc).</li>
 * <li>Paginación y Ordenamiento de registros.</li>
 * <li>Capacidad de limpiar datos en lotes (batching).</li>
 * </ul>
 * * <p>
 * Spring Boot detectará esta interfaz al arrancar y creará una implementación
 * en memoria (Proxy) automáticamente.
 * </p>
 *
 * @see JpaRepository
 * @author Jaret
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
