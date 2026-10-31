package ec.edu.monster.modelo;

import java.io.Serializable;

public class Credito implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codCredito;
    private int codCliente;
    private double monto;
    private int numCuotas;
    private double tasaInteresAnual;
    private double valorCuota;
    private String fechaCreacion;
    private String estado; // 'ACTIVO', 'PAGADO', 'CANCELADO'

    public Credito() {}

    public int getCodCredito() { return codCredito; }
    public void setCodCredito(int codCredito) { this.codCredito = codCredito; }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public int getNumCuotas() { return numCuotas; }
    public void setNumCuotas(int numCuotas) { this.numCuotas = numCuotas; }

    public double getTasaInteresAnual() { return tasaInteresAnual; }
    public void setTasaInteresAnual(double tasaInteresAnual) { this.tasaInteresAnual = tasaInteresAnual; }

    public double getValorCuota() { return valorCuota; }
    public void setValorCuota(double valorCuota) { this.valorCuota = valorCuota; }

    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
