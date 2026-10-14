package ec.edu.monster.wsconversorweb.model;

public enum UnidadLongitud {
    MILIMETRO("Milímetro (mm)"),
    CENTIMETRO("Centímetro (cm)"),
    METRO("Metro (m)"),
    KILOMETRO("Kilómetro (km)"),
    YARDA("Yarda (yd)");

    private final String etiqueta;

    UnidadLongitud(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
