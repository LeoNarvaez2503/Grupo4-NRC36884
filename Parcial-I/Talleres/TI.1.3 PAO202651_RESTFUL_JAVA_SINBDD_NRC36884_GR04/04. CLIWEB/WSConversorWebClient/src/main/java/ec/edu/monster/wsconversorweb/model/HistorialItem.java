package ec.edu.monster.wsconversorweb.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String magnitud;
    private double valorOrigen;
    private String unidadOrigen;
    private double valorDestino;
    private String unidadDestino;
    private String hora;

    public HistorialItem() {
    }

    public HistorialItem(String magnitud, double valorOrigen, String unidadOrigen, double valorDestino, String unidadDestino) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.magnitud = magnitud;
        this.valorOrigen = valorOrigen;
        this.unidadOrigen = unidadOrigen;
        this.valorDestino = valorDestino;
        this.unidadDestino = unidadDestino;
        this.hora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getId() {
        return id;
    }

    public String getMagnitud() {
        return magnitud;
    }

    public double getValorOrigen() {
        return valorOrigen;
    }

    public String getUnidadOrigen() {
        return unidadOrigen;
    }

    public double getValorDestino() {
        return valorDestino;
    }

    public String getUnidadDestino() {
        return unidadDestino;
    }

    public String getHora() {
        return hora;
    }
}
