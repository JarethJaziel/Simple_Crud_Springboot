package com.arqui.crud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal para la aplicación Spring Boot CRUD.
 * <p>
 * Esta clase contiene el método {@code main} que arranca el framework.
 * La anotación {@link SpringBootApplication} es una "meta-anotación" que activa tres funcionalidades esenciales:
 * </p>
 * <ul>
 * <li><b>@Configuration:</b> Indica que esta clase puede definir beans de configuración.</li>
 * <li><b>@EnableAutoConfiguration:</b> Le dice a Spring Boot que configure cosas automáticamente (como la base de datos) basado en las librerías del {@code pom.xml}.</li>
 * <li><b>@ComponentScan:</b> Busca automáticamente otros componentes (Controladores, Servicios, Repositorios) en el paquete actual y sus subpaquetes.</li>
 * </ul>
 *
 * @author Braulio Cuevas
 * @author Mauricio Dzay
 * @author Jareth Moo
 * @version 1.0
 */
@SpringBootApplication
public class CrudApplication {

	/**
     * Método principal estándar de Java que inicia la ejecución del programa.
     * <p>
     * Delega el control a {@link SpringApplication#run}, el cual se encarga de:
     * 1. Iniciar el contenedor de Inyección de Dependencias (IoC).
     * 2. Levantar el servidor web embebido (Tomcat) en el puerto 8080.
     * 3. Desplegar la aplicación.
     * </p>
     *
     * @param args Argumentos de línea de comandos que pueden ser pasados al iniciar la aplicación
     * (útiles para cambiar configuraciones al ejecutar el JAR).
     */
	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}

}
