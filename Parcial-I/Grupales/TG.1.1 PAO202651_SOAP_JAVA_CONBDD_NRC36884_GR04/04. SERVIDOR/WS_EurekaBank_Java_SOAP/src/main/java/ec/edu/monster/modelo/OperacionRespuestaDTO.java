package ec.edu.monster.modelo;

import java.io.Serializable;

/**
 * DTO de Respuesta de Operación Transaccional (Depósito, Retiro, Transferencia, Desbloqueo) - RQF-004.
 */
public class OperacionRespuestaDTO implements Serializable {

    private boolean exito;
    private String codigo; // OK, SALDO_INSUFICIENTE, CONCURRENCIA_BLOQUEADA, CUENTA_NO_EXISTE, ERROR_VALIDACION
    private String mensaje;
    private String numeroOperacion;
    private String numeroCuenta;
    private double saldoAnterior;
    private double monto;
    private double saldoActual;
    private String fechaHora;

    public OperacionRespuestaDTO() {
    }

    public static OperacionRespuestaDTO error(String codigo, String mensaje) {
        OperacionRespuestaDTO r = new OperacionRespuestaDTO();
        r.setExito(false);
        r.setCodigo(codigo);
        r.setMensaje(mensaje);
        return r;
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public double getSaldoAnterior() { return saldoAnterior; }
    public void setSaldoAnterior(double saldoAnterior) { this.saldoAnterior = saldoAnterior; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
