package ec.edu.monster.wsconversorcon.model;

public enum UnidadTemperatura {
    CELSIUS("Celsius (°C)"),
    FAHRENHEIT("Fahrenheit (°F)"),
    KELVIN("Kelvin (K)"),
    RANKINE("Rankine (°R)");

    private final String etiqueta;

    UnidadTemperatura(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
