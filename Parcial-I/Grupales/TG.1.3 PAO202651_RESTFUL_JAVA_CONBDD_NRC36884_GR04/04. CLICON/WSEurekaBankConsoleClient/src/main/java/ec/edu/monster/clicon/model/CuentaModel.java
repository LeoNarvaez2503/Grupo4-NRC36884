package ec.edu.monster.clicon.model;

public class CuentaModel {
    private String numeroCuenta;
    private String moneda;
    private double saldo;
    private String estadoCuenta;
    private String disponibilidadMashup;
    private int ventanillaBloqueo;
    private String bloqueadoPor;
    private int idCliente;
    private String dniCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private int idSucursal;
    private String nombreSucursal;
    private String ciudadSucursal;

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
    public String getDisponibilidadMashup() { return disponibilidadMashup; }
    public void setDisponibilidadMashup(String disponibilidadMashup) { this.disponibilidadMashup = disponibilidadMashup; }
    public int getVentanillaBloqueo() { return ventanillaBloqueo; }
    public void setVentanillaBloqueo(int ventanillaBloqueo) { this.ventanillaBloqueo = ventanillaBloqueo; }
    public String getBloqueadoPor() { return bloqueadoPor; }
    public void setBloqueadoPor(String bloqueadoPor) { this.bloqueadoPor = bloqueadoPor; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }
    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }
    public String getCiudadSucursal() { return ciudadSucursal; }
    public void setCiudadSucursal(String ciudadSucursal) { this.ciudadSucursal = ciudadSucursal; }

    public String getTitularCompleto() {
        return ((nombreCliente != null ? nombreCliente : "") + " " + (apellidoCliente != null ? apellidoCliente : "")).trim();
    }
}
