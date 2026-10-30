package ec.edu.monster.modelo.peticion;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private String usuario;
    private String clave;

    public LoginRequest() {}

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}
