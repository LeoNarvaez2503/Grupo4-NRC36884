package ec.edu.monster.cliweb.view;

import ec.edu.monster.dao.UsuarioDAO;
import ec.edu.monster.dao.UsuarioDAO.UsuarioDTO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 * Managed Bean JSF para la Autenticación de Usuarios.
 * Valida credenciales contra la Base de Datos (comercializadora_db.USUARIO).
 * Credenciales por defecto: Monster / Monster9
 * NRC: 36884 | Grupo 04
 */
@Named("loginView")
@SessionScoped
public class LoginView implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    private String hostServidor = ec.edu.monster.servicio.BanQuitoRestClient.getHost();
    private String usuario = "Monster";
    private String clave = "";
    private boolean autenticado = false;
    private String mensajeError = "";
    private String nombreCompleto = "Administrador Monster Core";
    private String rol = "ADMIN";

    public String login() {
        ec.edu.monster.servicio.BanQuitoRestClient.configurarHost(hostServidor);
        this.hostServidor = ec.edu.monster.servicio.BanQuitoRestClient.getHost();
        UsuarioDTO res = usuarioDAO.autenticar(usuario, clave);
        if (res.isAutenticado()) {
            this.autenticado = true;
            this.usuario = res.getUsuario();
            this.nombreCompleto = res.getNombre();
            this.rol = res.getRol();
            this.mensajeError = "";
            return "index?faces-redirect=true";
        } else {
            this.autenticado = false;
            this.mensajeError = res.getMensaje();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, this.mensajeError, null));
            return null;
        }
    }

    public String logout() {
        this.autenticado = false;
        this.clave = "";
        this.mensajeError = "";
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    public void verificarSesion() {
        try {
            if (!this.autenticado) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            }
        } catch (Exception ignored) {}
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public boolean isAutenticado() { return autenticado; }
    public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }

    public String getMensajeError() { return mensajeError; }
    public void setMensajeError(String mensajeError) { this.mensajeError = mensajeError; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getHostServidor() {
        return hostServidor;
    }

    public void setHostServidor(String hostServidor) {
        this.hostServidor = hostServidor;
    }
}
