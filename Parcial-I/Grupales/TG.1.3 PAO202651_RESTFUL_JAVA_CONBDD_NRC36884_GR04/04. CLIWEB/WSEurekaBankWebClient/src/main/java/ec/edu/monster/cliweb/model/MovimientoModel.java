package ec.edu.monster.cliweb.model;

public class MovimientoModel implements java.io.Serializable {
    private int idMovimiento;
    private String numeroOperacion;
    private String numeroCuenta;
    private int idEmpleado;
    private String nombreEmpleado;
    private int ventanilla;
    private String tipo;
    private double monto;
    private double saldoResultante;
    private String cuentaReferencia;
    private String descripcion;
    private String fechaHora;

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }
    public int getVentanilla() { return ventanilla; }
    public void setVentanilla(int ventanilla) { this.ventanilla = ventanilla; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public double getSaldoResultante() { return saldoResultante; }
    public void setSaldoResultante(double saldoResultante) { this.saldoResultante = saldoResultante; }
    public String getCuentaReferencia() { return cuentaReferencia; }
    public void setCuentaReferencia(String cuentaReferencia) { this.cuentaReferencia = cuentaReferencia; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
