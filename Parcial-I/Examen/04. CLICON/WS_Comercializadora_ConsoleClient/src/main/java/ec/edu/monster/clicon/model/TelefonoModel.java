package ec.edu.monster.clicon.model;

public class TelefonoModel {
    private int codTelefono;
    private String marca;
    private String modelo;
    private double precio;
    private String caracteristicas;
    private String imagenUrl;
    private String estado;

    public TelefonoModel() {}

    public TelefonoModel(int codTelefono, String marca, String modelo, double precio, String caracteristicas, String imagenUrl, String estado) {
        this.codTelefono = codTelefono;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.caracteristicas = caracteristicas;
        this.imagenUrl = imagenUrl;
        this.estado = estado;
    }

    public int getCodTelefono() { return codTelefono; }
    public void setCodTelefono(int codTelefono) { this.codTelefono = codTelefono; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
