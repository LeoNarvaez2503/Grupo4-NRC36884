package ec.edu.monster.climov.model;

import java.io.Serializable;
import java.util.List;

public class FacturaModel {
    public static class FacturaRequest implements Serializable {
        public String cedula;
        public String nombre;
        public int codTelefono;
        public String formaPago; // "EFECTIVO" o "CREDITO_DIRECTO"
        public int cuotas;       // Para crédito directo (3-18)
    }

    public static class FacturaResponse implements Serializable {
        public boolean exito;
        public String mensaje;
        public int facturaId;
        public List<CuotaAmortizacion> tabla;
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
}
