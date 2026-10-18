package ec.edu.monster.controladores.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuración base de Jakarta RESTful Web Services.
 * Define el prefijo base de todas las rutas de la API (/api/*).
 * 
 * @author MeatPuppets
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
}
