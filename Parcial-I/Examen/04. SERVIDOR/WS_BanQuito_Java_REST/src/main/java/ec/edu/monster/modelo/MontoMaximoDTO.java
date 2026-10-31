package ec.edu.monster.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MontoMaximoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codCliente;
    private String cedula;
    private String nombre;
    private double promedioDepositos;
    private double promedioRetiros;
    private double diferencia;
    private double montoMaximo;
    private String detalleCalculo;

    public MontoMaximoDTO() {}

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
