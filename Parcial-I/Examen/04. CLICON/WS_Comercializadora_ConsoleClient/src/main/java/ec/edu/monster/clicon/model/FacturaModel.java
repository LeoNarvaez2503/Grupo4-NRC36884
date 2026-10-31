package ec.edu.monster.clicon.model;

import java.util.List;

public class FacturaModel {
    public static class FacturaRequest {
        public String cedula;
        public String nombre;
        public int codTelefono;
        public String formaPago; // "EFECTIVO" o "CREDITO_DIRECTO"
        public int cuotas;       // Para crédito directo (3-18)
    }

    public static class FacturaResponse {
        public boolean exito;
        public String mensaje;
        public int facturaId;
        public List<CuotaAmortizacion> tabla;
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
}
