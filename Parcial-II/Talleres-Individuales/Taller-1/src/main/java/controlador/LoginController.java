package controlador;

import modelo.Usuario;

/**
 * Controlador MVC para la autenticación de usuarios.
 */
public class LoginController {

    private static final String USUARIO_VALIDO = "Monster";
    private static final String PASSWORD_VALIDA = "Monster9";

    /**
     * Valida las credenciales ingresadas contra las configuradas para el sistema.
     */
    public boolean autenticar(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return USUARIO_VALIDO.equals(username.trim()) && PASSWORD_VALIDA.equals(password);
    }

    public boolean autenticar(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        return autenticar(usuario.getUsername(), usuario.getPassword());
    }
}
