package ec.edu.monster.modelo;

import java.io.Serializable;

public class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int codMovimiento;
    private String numCuenta;
    private String tipo; // 'DEP' o 'RET'
    private double valor;
    private String fecha;

    public Movimiento() {}

    public int getCodMovimiento() { return codMovimiento; }
    public void setCodMovimiento(int codMovimiento) { this.codMovimiento = codMovimiento; }

    public String getNumCuenta() { return numCuenta; }
    public void setNumCuenta(String numCuenta) { this.numCuenta = numCuenta; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}
