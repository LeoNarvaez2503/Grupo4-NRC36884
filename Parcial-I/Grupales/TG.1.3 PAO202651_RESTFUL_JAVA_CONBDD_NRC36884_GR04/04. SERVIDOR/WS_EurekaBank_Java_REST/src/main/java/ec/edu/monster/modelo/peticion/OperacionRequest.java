package ec.edu.monster.modelo.peticion;

import java.io.Serializable;

public class OperacionRequest implements Serializable {
    private String numeroCuenta;
    private double monto;
    private int empleadoId;
    private int ventanilla;
    private String usuarioAdmin;

    public OperacionRequest() {}

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public int getVentanilla() { return ventanilla; }
    public void setVentanilla(int ventanilla) { this.ventanilla = ventanilla; }

    public String getUsuarioAdmin() { return usuarioAdmin; }
    public void setUsuarioAdmin(String usuarioAdmin) { this.usuarioAdmin = usuarioAdmin; }
}
