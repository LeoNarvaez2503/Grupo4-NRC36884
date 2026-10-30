package ec.edu.monster.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidad criptográfica para hashing y verificación BCrypt de contraseñas.
 */
public class SeguridadUtil {

    private SeguridadUtil() {
    }

    public static String generarHash(String passwordPlano) {
        return BCrypt.hashpw(passwordPlano, BCrypt.gensalt(10));
    }

    public static boolean verificarPassword(String passwordPlano, String hashAlmacenado) {
        if (passwordPlano == null || hashAlmacenado == null || hashAlmacenado.isBlank()) {
            return false;
        }
        if (hashAlmacenado.startsWith("$2a$") || hashAlmacenado.startsWith("$2b$") || hashAlmacenado.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(passwordPlano, hashAlmacenado);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return passwordPlano.equals(hashAlmacenado);
    }
}
