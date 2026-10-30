package ec.edu.monster.modelo;

import java.io.Serializable;

/**
 * DTO de Sucursal Bancaria.
 */
public class SucursalDTO implements Serializable {

    private int idSucursal;
    private String codigo;
    private String nombre;
    private String ciudad;
    private String direccion;
    private int totalCuentas;

    public SucursalDTO() {
    }

    public SucursalDTO(int idSucursal, String codigo, String nombre, String ciudad, String direccion, int totalCuentas) {
        this.idSucursal = idSucursal;
        this.codigo = codigo;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.totalCuentas = totalCuentas;
    }

    public int getIdSucursal() { return idSucursal; }
    public void setIdSucursal(int idSucursal) { this.idSucursal = idSucursal; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getTotalCuentas() { return totalCuentas; }
    public void setTotalCuentas(int totalCuentas) { this.totalCuentas = totalCuentas; }
}
