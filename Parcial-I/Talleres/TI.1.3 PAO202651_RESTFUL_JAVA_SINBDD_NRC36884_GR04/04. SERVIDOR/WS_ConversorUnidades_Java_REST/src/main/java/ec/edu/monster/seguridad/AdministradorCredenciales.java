package ec.edu.monster.seguridad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Administrador de credenciales en memoria (sin base de datos).
 * Gestiona la autenticación y cambio de contraseñas de los usuarios.
 * 
 * @author MeatPuppets
 */
public class AdministradorCredenciales {

    private static final Map<String, String> USUARIOS = new ConcurrentHashMap<>();

    // Credencial única permitida en el sistema
    public static final String USUARIO_DEFAULT = "MONSTER";
    public static final String CONTRASENIA_DEFAULT = "MONSTER9";

    static {
        reiniciar();
    }

    private AdministradorCredenciales() {
        // Constructor privado para clase utilitaria
    }

    /**
     * Restablece las credenciales a la única credencial permitida:
     * USUARIO: MONSTER
     * PASSWORD: MONSTER9
     */
    public static void reiniciar() {
        USUARIOS.clear();
        USUARIOS.put(USUARIO_DEFAULT.toLowerCase(), CONTRASENIA_DEFAULT);
    }

    /**
     * Valida si el usuario y la contraseña coinciden.
     * 
     * @param usuario     Nombre de usuario.
     * @param contrasenia Contraseña ingresada.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public static boolean validarUsuario(String usuario, String contrasenia) {
        if (usuario == null || contrasenia == null) {
            return false;
        }
        String claveAlmacenada = USUARIOS.get(usuario.trim().toLowerCase());
        return claveAlmacenada != null && claveAlmacenada.equals(contrasenia);
    }

    /**
     * Cambia la contraseña buscando el usuario cuya contraseña actual coincide con
     * la provista.
     * Si no se encuentra coincidencia, lanza RuntimeException.
     * 
     * @param contraseniaActual Contraseña actual del usuario.
     * @param contraseniaNueva  Nueva contraseña a establecer.
     */
    public static void cambiarContrasenia(String contraseniaActual, String contraseniaNueva) {
        if (contraseniaActual == null || contraseniaNueva == null || contraseniaNueva.trim().isEmpty()) {
            throw new RuntimeException("Las contraseñas no pueden ser nulas ni vacías.");
        }

        boolean actualizada = false;
        for (Map.Entry<String, String> entry : USUARIOS.entrySet()) {
            if (entry.getValue().equals(contraseniaActual)) {
                USUARIOS.put(entry.getKey(), contraseniaNueva);
                actualizada = true;
                break;
            }
        }

        if (!actualizada) {
            throw new RuntimeException("La contraseña actual es incorrecta.");
        }
    }

    /**
     * Cambia la contraseña para un usuario específico.
     * 
     * @param usuario           Nombre de usuario.
     * @param contraseniaActual Contraseña actual.
     * @param contraseniaNueva  Nueva contraseña.
     */
    public static void cambiarContrasenia(String usuario, String contraseniaActual, String contraseniaNueva) {
        if (usuario == null || contraseniaActual == null || contraseniaNueva == null
                || contraseniaNueva.trim().isEmpty()) {
            throw new RuntimeException("Datos inválidos para cambio de contraseña.");
        }
        String userKey = usuario.trim().toLowerCase();
        String claveActual = USUARIOS.get(userKey);
        if (claveActual == null || !claveActual.equals(contraseniaActual)) {
            throw new RuntimeException("Credenciales incorrectas para el usuario: " + usuario);
        }
        USUARIOS.put(userKey, contraseniaNueva);
    }

    /**
     * Registra o actualiza un usuario en memoria.
     * 
     * @param usuario     Nombre de usuario.
     * @param contrasenia Contraseña del usuario.
     */
    public static void registrarUsuario(String usuario, String contrasenia) {
        if (usuario != null && contrasenia != null && !usuario.isBlank() && !contrasenia.isBlank()) {
            USUARIOS.put(usuario.trim().toLowerCase(), contrasenia);
        }
    }
}
