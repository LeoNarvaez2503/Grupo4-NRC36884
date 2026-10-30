package ec.edu.monster.modelo.peticion;

import java.io.Serializable;

public class TransferenciaRequest implements Serializable {
    private String cuentaOrigen;
    private String cuentaDestino;
    private double monto;
    private int empleadoId;
    private int ventanilla;

    public TransferenciaRequest() {}

    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public int getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(int empleadoId) { this.empleadoId = empleadoId; }

    public int getVentanilla() { return ventanilla; }
    public void setVentanilla(int ventanilla) { this.ventanilla = ventanilla; }
}
