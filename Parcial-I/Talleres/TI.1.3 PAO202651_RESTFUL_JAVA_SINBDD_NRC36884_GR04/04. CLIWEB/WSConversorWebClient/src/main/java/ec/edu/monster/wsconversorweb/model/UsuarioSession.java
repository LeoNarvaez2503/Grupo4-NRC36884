package ec.edu.monster.wsconversorweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuarioSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String token;
    private String horaIngreso;

    public UsuarioSession() {
    }

    public UsuarioSession(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
        this.horaIngreso = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }
}
