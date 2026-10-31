package ec.edu.monster.modelo;

import java.io.Serializable;

public class SujetoCreditoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean esSujeto;
    private int codCliente;
    private String cedula;
    private String nombre;
    private int edad;
    private String genero;
    private String motivo;

    // Reglas de negocio detalladas
    private boolean cumpleCliente;
    private boolean cumpleDepositoUltimoMes;
    private boolean cumpleEdadGenero;
    private boolean cumpleSinCreditoActivo;

    public SujetoCreditoDTO() {}

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
