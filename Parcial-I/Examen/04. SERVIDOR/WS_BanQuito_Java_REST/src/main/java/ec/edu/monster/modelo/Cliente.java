package ec.edu.monster.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codCliente;
    private String cedula;
    private String nombre;
    private String genero; // 'M' o 'F'
    private String fechaNacimiento;

    public Cliente() {}

    public Cliente(int codCliente, String cedula, String nombre, String genero, String fechaNacimiento) {
        this.codCliente = codCliente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int calcularEdad() {
        if (fechaNacimiento == null || fechaNacimiento.isBlank()) return 0;
        try {
            LocalDate nac = LocalDate.parse(fechaNacimiento.substring(0, 10));
            return Period.between(nac, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getCodCliente() { return codCliente; }
    public void setCodCliente(int codCliente) { this.codCliente = codCliente; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
