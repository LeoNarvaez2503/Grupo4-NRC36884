package ec.edu.monster.climov.model;

public class ConfiguracionConexion {
    // 10.0.2.2 es el alias del host en el emulador oficial de Android; localhost para pruebas directas
    private String comEndpoint = "http://10.0.2.2:8090/api";
    private String banquitoEndpoint = "http://10.0.2.2:8080/api/credito";

    public String getComEndpoint() { return comEndpoint; }
    public void setComEndpoint(String comEndpoint) { this.comEndpoint = comEndpoint; }

    public String getBanquitoEndpoint() { return banquitoEndpoint; }
    public void setBanquitoEndpoint(String banquitoEndpoint) { this.banquitoEndpoint = banquitoEndpoint; }
}
