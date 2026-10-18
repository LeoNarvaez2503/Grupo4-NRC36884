package ec.edu.monster.wsconversorweb.view;

import ec.edu.monster.wsconversorweb.controller.AppControlador;
import ec.edu.monster.wsconversorweb.model.UsuarioSession;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Vista / Bean de Inicio de Sesión para Cliente Web (Patrón MVC).
 */
@Named("loginView")
@SessionScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String contrasenia;
    private UsuarioSession usuarioSession;
    private final AppControlador controlador = new AppControlador();

    public String login() {
        if (usuario == null || usuario.trim().isEmpty() ||
            contrasenia == null || contrasenia.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Por favor complete todos los campos.", null));
            return null;
        }

        try {
            String token = controlador.login(usuario.trim(), contrasenia.trim());
            if (token != null && !token.isBlank()) {
                usuarioSession = new UsuarioSession(usuario.trim().toUpperCase(), token);
                return "conversor?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas (Usuario o contraseña no válidos).", null));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error de conexión con el servicio SOAP en Payara: " + e.getMessage(), null));
            return null;
        }
    }

    public String logout() {
        usuarioSession = null;
        usuario = null;
        contrasenia = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    public void limpiar() {
        this.usuario = "";
        this.contrasenia = "";
    }

    public boolean isAutenticado() {
        return usuarioSession != null && usuarioSession.getToken() != null && !usuarioSession.getToken().isBlank();
    }

    public String getNombreUsuario() {
        return usuarioSession != null ? usuarioSession.getUsuario() : "";
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public UsuarioSession getUsuarioSession() {
        return usuarioSession;
    }
}
