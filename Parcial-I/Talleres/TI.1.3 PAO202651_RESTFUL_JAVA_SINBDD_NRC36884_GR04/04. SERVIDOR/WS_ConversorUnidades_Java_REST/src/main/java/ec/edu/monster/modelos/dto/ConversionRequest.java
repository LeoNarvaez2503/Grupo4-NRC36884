package ec.edu.monster.modelos.dto;

import java.io.Serializable;

public class ConversionRequest implements Serializable {

    private double valor;
    private String unidadInicial;
    private String unidadFinal;

    public ConversionRequest() {
    }

    public ConversionRequest(double valor, String unidadInicial, String unidadFinal) {
        this.valor = valor;
        this.unidadInicial = unidadInicial;
        this.unidadFinal = unidadFinal;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUnidadInicial() {
        return unidadInicial;
    }

    public void setUnidadInicial(String unidadInicial) {
        this.unidadInicial = unidadInicial;
    }

    public String getUnidadFinal() {
        return unidadFinal;
    }

    public void setUnidadFinal(String unidadFinal) {
        this.unidadFinal = unidadFinal;
    }
}
