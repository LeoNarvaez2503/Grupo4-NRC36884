package ec.edu.monster.modelos.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversionResponse implements Serializable {

    private boolean exito;
    private String tipo;
    private double valorInicial;
    private String unidadInicial;
    private double valorFinal;
    private String unidadFinal;
    private String mensaje;
    private String timestamp;

    public ConversionResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public ConversionResponse(boolean exito, String tipo, double valorInicial, String unidadInicial,
                              double valorFinal, String unidadFinal, String mensaje) {
        this.exito = exito;
        this.tipo = tipo;
        this.valorInicial = valorInicial;
        this.unidadInicial = unidadInicial;
        this.valorFinal = valorFinal;
        this.unidadFinal = unidadFinal;
        this.mensaje = mensaje;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(double valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getUnidadInicial() {
        return unidadInicial;
    }

    public void setUnidadInicial(String unidadInicial) {
        this.unidadInicial = unidadInicial;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getUnidadFinal() {
        return unidadFinal;
    }

    public void setUnidadFinal(String unidadFinal) {
        this.unidadFinal = unidadFinal;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
