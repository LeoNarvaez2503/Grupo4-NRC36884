package ec.edu.monster.clicon.model;

import java.io.Serializable;

/**
 * Modelo de sesión activa del usuario autenticado (Patrón MVC).
 */
public class UsuarioSesion implements Serializable {

    private boolean autenticado;
    private String mensaje;
    private String token;
    private int idEmpleado;
    private String usuario;
    private String nombreCompleto;
    private String rol;
    private int idSucursal;
    private String nombreSucursal;
    private int ventanilla;

    public boolean isAutenticado() { return autenticado; }
    public void setAutenticado(boolean autenticado) { this.autenticado = autenticado; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }

    public int getVentanilla() { return ventanilla; }
    public void setVentanilla(int ventanilla) { this.ventanilla = ventanilla; }
}
