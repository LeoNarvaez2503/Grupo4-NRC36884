package ec.edu.monster.modelos.dto;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private boolean exito;
    private String token;
    private String usuario;
    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(boolean exito, String token, String usuario, String mensaje) {
        this.exito = exito;
        this.token = token;
        this.usuario = usuario;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
