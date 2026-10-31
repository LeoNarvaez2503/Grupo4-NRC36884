package ec.edu.monster.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BanQuitoDTOs {

    public static class SujetoCreditoResponse implements Serializable {
        public boolean esSujeto;
        public int codCliente;
        public String cedula;
        public String nombre;
        public int edad;
        public String genero;
        public String motivo;
        public boolean cumpleCliente;
        public boolean cumpleDepositoUltimoMes;
        public boolean cumpleEdadGenero;
        public boolean cumpleSinCreditoActivo;
    }

    public static class MontoMaximoResponse implements Serializable {
        public int codCliente;
        public String cedula;
        public String nombre;
        public double promedioDepositos;
        public double promedioRetiros;
        public double diferencia;
        public double montoMaximo;
        public String detalleCalculo;
    }

    public static class CuotaAmortizacion implements Serializable {
        public int codAmortizacion;
        public int codCredito;
        public int numCuota;
        public double valorCuota;
        public double interesPagado;
        public double capitalPagado;
        public double saldo;
    }

    public static class OtorgarCreditoResponse implements Serializable {
        public int codCliente;
        public double precioTelefono;
        public int numeroCuotas;
        public boolean exito;
        public String mensaje;
        public CreditoInfo credito;
        public List<CuotaAmortizacion> tablaAmortizacion = new ArrayList<>();
    }

    public static class CreditoInfo implements Serializable {
        public int codCredito;
        public int codCliente;
        public double monto;
        public int numCuotas;
        public double tasaInteresAnual;
        public double valorCuota;
        public String fechaCreacion;
        public String estado;
    }
}
