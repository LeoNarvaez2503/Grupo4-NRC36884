package ec.edu.monster.cliweb.view;

import ec.edu.monster.cliweb.controller.AppControlador;
import ec.edu.monster.cliweb.model.UsuarioSesion;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Capa Vista (Patrón MVC Tradicional): Backing Bean JSF para login.xhtml.
 */
@Named("loginView")
@SessionScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario = "Monster";
    private String clave = "";
    private int ventanilla = 1;
    private final AppControlador controlador = new AppControlador();

    public String login() {
        if (usuario == null || usuario.trim().isEmpty() || clave == null || clave.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Complete usuario y contraseña.", null));
            return null;
        }
        try {
            UsuarioSesion s = controlador.iniciarSesion(usuario.trim(), clave.trim());
            controlador.setVentanillaActiva(ventanilla);
            if (s != null && s.isAutenticado()) {
                return "dashboard?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                s != null ? s.getMensaje() : "Credenciales inválidas.", null));
                return null;
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                            "Error de comunicación con el servidor: " + e.getMessage(), null));
            return null;
        }
    }

    public String logout() {
        controlador.cerrarSesion();
        clave = "";
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    public void limpiar() {
        this.usuario = "";
        this.clave = "";
    }

    public boolean isAutenticado() {
        UsuarioSesion s = controlador.getSesionActual();
        return s != null && s.isAutenticado();
    }

    public AppControlador getControlador() {
        return controlador;
    }

    public UsuarioSesion getSesion() {
        return controlador.getSesionActual();
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public int getVentanilla() { return ventanilla; }
    public void setVentanilla(int ventanilla) {
        this.ventanilla = ventanilla;
        controlador.setVentanillaActiva(ventanilla);
    }
}
