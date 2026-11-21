package modelo;

/**
 * Entidad que representa un país obtenido de la API REST y persistido en MySQL.
 */
public class Pais {

    private int id;
    private String nombre;
    private String capital;
    private String region;
    private long poblacion;
    private String moneda;

    public Pais() {
    }

    public Pais(String nombre, String capital, String region, long poblacion, String moneda) {
        this.nombre = nombre;
        this.capital = capital;
        this.region = region;
        this.poblacion = poblacion;
        this.moneda = moneda;
    }

    public Pais(int id, String nombre, String capital, String region, long poblacion, String moneda) {
        this.id = id;
        this.nombre = nombre;
        this.capital = capital;
        this.region = region;
        this.poblacion = poblacion;
        this.moneda = moneda;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public long getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(long poblacion) {
        this.poblacion = poblacion;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    @Override
    public String toString() {
        return nombre + " (" + region + ") - Capital: " + capital
                + ", Población: " + poblacion + ", Moneda: " + moneda;
    }
}
