package com.arqui.crud.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Representa la entidad de Usuario en la base de datos.
 * <p>
 * Esta clase mapea los objetos Java a la tabla "users".
 * Utiliza la librería <b>Lombok</b> (@Data) para generar automáticamente
 * los métodos getters, setters, toString, equals y hashCode durante la compilación.
 * </p>
 *
 * @author Braulio Cuevas
 * @author Mauricio Dzay
 * @author Jareth Moo
 * @version 1.0
 */
@Data
@Entity
@Table(name="users")
public class User {

    /**
     * Identificador único del usuario (Llave Primaria).
     * <p>
     * Se genera automáticamente por la base de datos gracias a la estrategia IDENTITY
     * (equivalente a AUTO_INCREMENT en MySQL).
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre completo del usuario.
     * <p>
     * Este campo es obligatorio (nullable = false).
     * </p>
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Correo electrónico del usuario.
     * <p>
     * Este campo actúa como identificador lógico, por lo que debe ser único en toda la base de datos
     * (unique = true) y es obligatorio.
     * </p>
     */
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Número de teléfono del usuario.
     * <p>
     * Se almacena como String para permitir formatos internacionales (+52) o guiones.
     * Es un campo obligatorio.
     * </p>
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * Edad del usuario.
     * <p>
     * Se utiliza la clase Wrapper {@link Integer} en lugar del primitivo int.
     * Aunque está marcado como obligatorio (nullable = false), usar Integer permite
     * manejar mejor la lógica de negocio antes de guardar.
     * </p>
     */
    @Column(name = "age", nullable = false)
    private Integer age;
    
    /**
     * Constructor vacío requerido por JPA/Hibernate.
     * <p>
     * Hibernate necesita este constructor sin argumentos para poder instanciar
     * la entidad mediante reflexión cuando recupera datos de la base de datos.
     * </p>
     */
    public User() {
    }

    /**
     * Constructor conveniente para crear nuevas instancias de usuario manualmente.
     * <p>
     * No incluye el ID porque este es generado automáticamente por la base de datos al guardar.
     * </p>
     *
     * @param name Nombre completo del usuario.
     * @param email Correo electrónico (debe ser único).
     * @param phone Número de teléfono.
     * @param age Edad del usuario.
     */
    public User(String name, String email, String phone, Integer age) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
    } 

}
