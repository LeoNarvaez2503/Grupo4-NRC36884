package ec.edu.monster.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BanQuitoDTOs {

    public static class SujetoCreditoResponse implements Serializable {
        private static final long serialVersionUID = 1L;

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

        public boolean isEsSujeto() { return esSujeto; }
        public void setEsSujeto(boolean esSujeto) { this.esSujeto = esSujeto; }

        public int getCodCliente() { return codCliente; }
        public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

        public String getCedula() { return cedula; }
        public void setCedula(String cedula) { this.cedula = cedula; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public int getEdad() { return edad; }
        public void setEdad(int edad) { this.edad = edad; }

        public String getGenero() { return genero; }
        public void setGenero(String genero) { this.genero = genero; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }

        public boolean isCumpleCliente() { return cumpleCliente; }
        public void setCumpleCliente(boolean cumpleCliente) { this.cumpleCliente = cumpleCliente; }

        public boolean isCumpleDepositoUltimoMes() { return cumpleDepositoUltimoMes; }
        public void setCumpleDepositoUltimoMes(boolean cumpleDepositoUltimoMes) { this.cumpleDepositoUltimoMes = cumpleDepositoUltimoMes; }

        public boolean isCumpleEdadGenero() { return cumpleEdadGenero; }
        public void setCumpleEdadGenero(boolean cumpleEdadGenero) { this.cumpleEdadGenero = cumpleEdadGenero; }

        public boolean isCumpleSinCreditoActivo() { return cumpleSinCreditoActivo; }
        public void setCumpleSinCreditoActivo(boolean cumpleSinCreditoActivo) { this.cumpleSinCreditoActivo = cumpleSinCreditoActivo; }
    }

    public static class MontoMaximoResponse implements Serializable {
        private static final long serialVersionUID = 1L;

        public int codCliente;
        public String cedula;
        public String nombre;
        public double promedioDepositos;
        public double promedioRetiros;
        public double diferencia;
        public double montoMaximo;
        public String detalleCalculo;

        public int getCodCliente() { return codCliente; }
        public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

        public String getCedula() { return cedula; }
        public void setCedula(String cedula) { this.cedula = cedula; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public double getPromedioDepositos() { return promedioDepositos; }
        public void setPromedioDepositos(double promedioDepositos) { this.promedioDepositos = promedioDepositos; }

        public double getPromedioRetiros() { return promedioRetiros; }
        public void setPromedioRetiros(double promedioRetiros) { this.promedioRetiros = promedioRetiros; }

        public double getDiferencia() { return diferencia; }
        public void setDiferencia(double diferencia) { this.diferencia = diferencia; }

        public double getMontoMaximo() { return montoMaximo; }
        public void setMontoMaximo(double montoMaximo) { this.montoMaximo = montoMaximo; }

        public String getDetalleCalculo() { return detalleCalculo; }
        public void setDetalleCalculo(String detalleCalculo) { this.detalleCalculo = detalleCalculo; }
    }

    public static class CuotaAmortizacion implements Serializable {
        private static final long serialVersionUID = 1L;

        public int codAmortizacion;
        public int codCredito;
        public int numCuota;
        public double valorCuota;
        public double interesPagado;
        public double capitalPagado;
        public double saldo;

        public int getCodAmortizacion() { return codAmortizacion; }
        public void setCodAmortizacion(int codAmortizacion) { this.codAmortizacion = codAmortizacion; }

        public int getCodCredito() { return codCredito; }
        public void setCodCredito(int codCredito) { this.codCredito = codCredito; }

        public int getNumCuota() { return numCuota; }
        public void setNumCuota(int numCuota) { this.numCuota = numCuota; }

        public double getValorCuota() { return valorCuota; }
        public void setValorCuota(double valorCuota) { this.valorCuota = valorCuota; }

        public double getInteresPagado() { return interesPagado; }
        public void setInteresPagado(double interesPagado) { this.interesPagado = interesPagado; }

        public double getCapitalPagado() { return capitalPagado; }
        public void setCapitalPagado(double capitalPagado) { this.capitalPagado = capitalPagado; }

        public double getSaldo() { return saldo; }
        public void setSaldo(double saldo) { this.saldo = saldo; }
    }

    public static class OtorgarCreditoResponse implements Serializable {
        private static final long serialVersionUID = 1L;

        public int codCliente;
        public double precioTelefono;
        public int numeroCuotas;
        public boolean exito;
        public String mensaje;
        public CreditoInfo credito;
        public List<CuotaAmortizacion> tablaAmortizacion = new ArrayList<>();

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

        public CreditoInfo getCredito() { return credito; }
        public void setCredito(CreditoInfo credito) { this.credito = credito; }

        public List<CuotaAmortizacion> getTablaAmortizacion() { return tablaAmortizacion; }
        public void setTablaAmortizacion(List<CuotaAmortizacion> tablaAmortizacion) { this.tablaAmortizacion = tablaAmortizacion; }
    }

    public static class CreditoInfo implements Serializable {
        private static final long serialVersionUID = 1L;

        public int codCredito;
        public int codCliente;
        public double monto;
        public int numCuotas;
        public double tasaInteresAnual;
        public double valorCuota;
        public String fechaCreacion;
        public String estado;

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
}
