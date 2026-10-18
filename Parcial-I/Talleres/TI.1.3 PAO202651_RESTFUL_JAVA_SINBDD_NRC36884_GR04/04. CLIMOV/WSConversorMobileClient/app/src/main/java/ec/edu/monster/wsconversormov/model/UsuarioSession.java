package ec.edu.monster.wsconversormov.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        this.horaIngreso = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
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
