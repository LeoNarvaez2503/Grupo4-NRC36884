package ec.edu.monster.cliesc.model;

import java.util.List;

public class FacturaModel {
    /** Linea del carrito: telefono + cantidad. */
    public static class ItemCarrito {
        public int codTelefono;
        public int cantidad;

        public ItemCarrito() {}

        public ItemCarrito(int codTelefono, int cantidad) {
            this.codTelefono = codTelefono;
            this.cantidad = cantidad;
        }
    }

    public static class FacturaRequest {
        public String cedula;
        public String nombre;
        public List<ItemCarrito> items = new java.util.ArrayList<>(); // carrito: varios telefonos
        public String formaPago; // "EFECTIVO" o "CREDITO_DIRECTO"
        public int cuotas;       // Para crédito directo (3-18)
    }

    /** Renglon de una factura emitida. */
    public static class FacturaDetalle {
        public int codTelefono;
        public String modeloTelefono;
        public double precioUnitario;
        public int cantidad;
        public double subtotal;
    }

    /** Cabecera de la factura emitida + todos sus renglones. */
    public static class FacturaInfo {
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

    public static class FacturaResponse {
        public boolean exito;
        public String mensaje;
        public int facturaId;
        public List<CuotaAmortizacion> tabla;
        public FacturaInfo factura;
    }

    public static class SujetoCreditoResponse {
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

    public static class MontoMaximoResponse {
        public int codCliente;
        public double promedioDepositos;
        public double promedioRetiros;
        public double diferenciaPromedios;
        public double factorCapacidad;
        public int factorMeses;
        public double montoMaximo;
    }

    public static class CuotaAmortizacion {
        public int codAmortizacion;
        public int codCredito;
        public int numCuota;
        public double valorCuota;
        public double interesPagado;
        public double capitalPagado;
        public double saldo;
        public String estado;
    }

    public static class FacturaItem {
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

        /** Texto de los articulos: "2 x Modelo A; 1 x Modelo B" (o el resumen del servidor). */
        public String resumenArticulos() {
            if (items == null || items.isEmpty()) return modeloTelefono != null ? modeloTelefono : "";
            StringBuilder sb = new StringBuilder();
            for (FacturaDetalle d : items) {
                if (sb.length() > 0) sb.append("; ");
                sb.append(d.cantidad).append(" x ").append(d.modeloTelefono);
            }
            return sb.toString();
        }
    }
}
