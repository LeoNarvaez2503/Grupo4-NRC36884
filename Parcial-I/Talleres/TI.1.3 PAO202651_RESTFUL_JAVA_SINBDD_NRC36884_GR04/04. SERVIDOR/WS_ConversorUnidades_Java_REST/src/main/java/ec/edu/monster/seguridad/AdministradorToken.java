package ec.edu.monster.seguridad;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Administrador de tokens de seguridad en memoria.
 * Genera y valida tokens con tiempo de expiración configurable.
 * 
 * @author MeatPuppets
 */
public class AdministradorToken {

    // Duración de validez del token en milisegundos (por defecto 30 minutos)
    private static final long TIEMPO_EXPIRACION_MS = 30 * 60 * 1000L;

    // Mapa de token -> timestamp de creación
    private static final Map<String, Long> TOKENS_ACTIVOS = new ConcurrentHashMap<>();

    private AdministradorToken() {
        // Constructor privado para clase utilitaria
    }

    /**
     * Genera un nuevo token de seguridad único y lo almacena con su marca de tiempo.
     * 
     * @return El identificador de token generado.
     */
    public static String generarToken() {
        String token = UUID.randomUUID().toString();
        TOKENS_ACTIVOS.put(token, System.currentTimeMillis());
        return token;
    }

    /**
     * Valida si un token existe y no ha expirado.
     * 
     * @param token El token a validar.
     * @return true si el token es válido y está vigente, false en caso contrario.
     */
    public static boolean validarToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        Long timestampCreacion = TOKENS_ACTIVOS.get(token.trim());
        if (timestampCreacion == null) {
            return false;
        }

        // Verificar si expiró
        if (System.currentTimeMillis() - timestampCreacion > TIEMPO_EXPIRACION_MS) {
            TOKENS_ACTIVOS.remove(token.trim());
            return false;
        }

        return true;
    }

    /**
     * Invalida (elimina) un token de la sesión activa.
     * 
     * @param token El token a invalidar.
     */
    public static void invalidarToken(String token) {
        if (token != null) {
            TOKENS_ACTIVOS.remove(token.trim());
        }
    }

    /**
     * Limpia todos los tokens almacenados.
     */
    public static void limpiar() {
        TOKENS_ACTIVOS.clear();
    }
}
