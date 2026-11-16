package ec.edu.monster.modelo;

import java.io.Serializable;

/** Renglon de una factura: un telefono del carrito con su cantidad. */
public class FacturaDetalle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codDetalle;
    private int numFactura;
    private int codTelefono;
    private String modeloTelefono;
    private double precioUnitario;
    private int cantidad = 1;
    private double subtotal;

    public FacturaDetalle() {}

    public FacturaDetalle(int codTelefono, String modeloTelefono, double precioUnitario, int cantidad) {
        this.codTelefono = codTelefono;
        this.modeloTelefono = modeloTelefono;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = Math.round(precioUnitario * cantidad * 100.0) / 100.0;
    }

    public int getCodDetalle() { return codDetalle; }
    public void setCodDetalle(int codDetalle) { this.codDetalle = codDetalle; }

    public int getNumFactura() { return numFactura; }
    public void setNumFactura(int numFactura) { this.numFactura = numFactura; }

    public int getCodTelefono() { return codTelefono; }
    public void setCodTelefono(int codTelefono) { this.codTelefono = codTelefono; }

    public String getModeloTelefono() { return modeloTelefono; }
    public void setModeloTelefono(String modeloTelefono) { this.modeloTelefono = modeloTelefono; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}
