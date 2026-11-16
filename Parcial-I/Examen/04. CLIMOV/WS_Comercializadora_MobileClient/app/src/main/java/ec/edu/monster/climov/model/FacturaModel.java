package ec.edu.monster.climov.model;

import java.io.Serializable;
import java.util.List;

public class FacturaModel {
    /** Linea del carrito: telefono + cantidad. */
    public static class ItemCarrito implements Serializable {
        public int codTelefono;
        public int cantidad;

        public ItemCarrito() {}

        public ItemCarrito(int codTelefono, int cantidad) {
            this.codTelefono = codTelefono;
            this.cantidad = cantidad;
        }
    }

    public static class FacturaRequest implements Serializable {
        public String cedula;
        public String nombre;
        public List<ItemCarrito> items = new java.util.ArrayList<>(); // carrito: varios telefonos
        public String formaPago; // "EFECTIVO" o "CREDITO_DIRECTO"
        public int cuotas;       // Para crédito directo (3-18)
    }

    /** Renglon de una factura emitida. */
    public static class FacturaDetalle implements Serializable {
        public int codTelefono;
        public String modeloTelefono;
        public double precioUnitario;
        public int cantidad;
        public double subtotal;
    }

    /** Cabecera de la factura emitida + todos sus renglones. */
    public static class FacturaInfo implements Serializable {
        public int numFactura;
        public String numeroComprobante;
        public String cedulaCliente;
        public String nombreCliente;
        public String formaPago;
        public double subtotal;
        public double porcentajeDescuento;
        public double valorDescuento;
        public double totalPagar;
        public Integer codCreditoBanco;
        public Integer numCuotas;
        public Double valorCuota;
        public List<FacturaDetalle> items;
    }

    public static class FacturaResponse implements Serializable {
        public boolean exito;
        public String mensaje;
        public int facturaId;
        public List<CuotaAmortizacion> tabla;
        public FacturaInfo factura;
    }

    public static class SujetoCreditoResponse implements Serializable {
        public boolean esSujeto;
        public String motivo;
        public Integer codCliente;
        public String nombre;
        public String cedula;
        public int edad;
        public String genero;
        public boolean cumpleCliente;
        public boolean cumpleDepositoUltimoMes;
        public boolean cumpleEdadGenero;
        public boolean cumpleSinCreditoActivo;
    }

    public static class MontoMaximoResponse implements Serializable {
        public int codCliente;
        public double promedioDepositos;
        public double promedioRetiros;
        public double diferenciaPromedios;
        public double factorCapacidad;
        public int factorMeses;
        public double montoMaximo;
    }

    public static class CuotaAmortizacion implements Serializable {
        public int codAmortizacion;
        public int codCredito;
        public int numCuota;
        public double valorCuota;
        public double interesPagado;
        public double capitalPagado;
        public double saldo;
        public String estado;
    }

    public static class FacturaItem implements Serializable {
        public int numFactura;
        public String numeroComprobante;
        public String fecha;
        public String cedulaCliente;
        public String nombreCliente;
        public String modeloTelefono;
        public String formaPago;
        public double precioUnitario;
        public double subtotal;
        public double valorDescuento;
        public double totalPagar;
        public Integer codCreditoBanco;
        public List<FacturaDetalle> items;
    }
}
