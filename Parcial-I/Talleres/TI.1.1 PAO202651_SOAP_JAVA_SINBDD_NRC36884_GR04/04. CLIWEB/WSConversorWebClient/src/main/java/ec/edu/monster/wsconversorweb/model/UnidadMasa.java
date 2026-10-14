package ec.edu.monster.wsconversorweb.model;

public enum UnidadMasa {
    MILIGRAMO("Miligramo (mg)"),
    GRAMO("Gramo (g)"),
    KILOGRAMO("Kilogramo (kg)"),
    TONELADA("Tonelada (t)"),
    ONZA("Onza (oz)");

    private final String etiqueta;

    UnidadMasa(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
