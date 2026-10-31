package ec.edu.monster.modelo;

import java.io.Serializable;

public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;

    private int numFactura;
    private String numeroComprobante;
    private String fecha;
    private Integer codClienteBanco;
    private String cedulaCliente;
    private String nombreCliente;
    private int codTelefono;
    private String modeloTelefono;
    private double precioUnitario;
    private String formaPago; // 'EFECTIVO' o 'CREDITO_DIRECTO'
    private double porcentajeDescuento; // 42.0 para efectivo
    private double valorDescuento;
    private double totalPagar;
    private Integer codCreditoBanco;
    private Integer numCuotas;
    private Double valorCuota;
    private String observacion;

    public Factura() {}

    public int getNumFactura() { return numFactura; }
    public void setNumFactura(int numFactura) { this.numFactura = numFactura; }

    public String getNumeroComprobante() { return numeroComprobante; }
    public void setNumeroComprobante(String numeroComprobante) { this.numeroComprobante = numeroComprobante; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public Integer getCodClienteBanco() { return codClienteBanco; }
    public void setCodClienteBanco(Integer codClienteBanco) { this.codClienteBanco = codClienteBanco; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public int getCodTelefono() { return codTelefono; }
    public void setCodTelefono(int codTelefono) { this.codTelefono = codTelefono; }

    public String getModeloTelefono() { return modeloTelefono; }
    public void setModeloTelefono(String modeloTelefono) { this.modeloTelefono = modeloTelefono; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public double getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }

    public Integer getCodCreditoBanco() { return codCreditoBanco; }
    public void setCodCreditoBanco(Integer codCreditoBanco) { this.codCreditoBanco = codCreditoBanco; }

    public Integer getNumCuotas() { return numCuotas; }
    public void setNumCuotas(Integer numCuotas) { this.numCuotas = numCuotas; }

    public Double getValorCuota() { return valorCuota; }
    public void setValorCuota(Double valorCuota) { this.valorCuota = valorCuota; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
