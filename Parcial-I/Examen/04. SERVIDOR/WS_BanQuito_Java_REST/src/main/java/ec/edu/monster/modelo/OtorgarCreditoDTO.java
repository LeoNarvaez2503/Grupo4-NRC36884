package ec.edu.monster.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OtorgarCreditoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    // Campos de Solicitud (Request)
    private int codCliente;
    private double precioTelefono;
    private int numeroCuotas;

    // Campos de Respuesta (Response)
    private boolean exito;
    private String mensaje;
    private Credito credito;
    private List<CuotaAmortizacion> tablaAmortizacion = new ArrayList<>();

    public OtorgarCreditoDTO() {}

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public double getPrecioTelefono() { return precioTelefono; }
    public void setPrecioTelefono(double precioTelefono) { this.precioTelefono = precioTelefono; }

    public int getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Credito getCredito() { return credito; }
    public void setCredito(Credito credito) { this.credito = credito; }

    public List<CuotaAmortizacion> getTablaAmortizacion() { return tablaAmortizacion; }
    public void setTablaAmortizacion(List<CuotaAmortizacion> tablaAmortizacion) { this.tablaAmortizacion = tablaAmortizacion; }
}
