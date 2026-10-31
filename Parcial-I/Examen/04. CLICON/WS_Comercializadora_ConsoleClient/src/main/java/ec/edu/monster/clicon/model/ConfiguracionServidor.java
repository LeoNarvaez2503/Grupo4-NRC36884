package ec.edu.monster.clicon.model;

public class ConfiguracionServidor {
    private String comEndpoint = "http://localhost:8090/api";
    private String banquitoEndpoint = "http://localhost:8080/api/credito";

    public String getComEndpoint() {
        return comEndpoint;
    }

    public void setComEndpoint(String comEndpoint) {
        this.comEndpoint = comEndpoint;
    }

    public String getBanquitoEndpoint() {
        return banquitoEndpoint;
    }

    public void setBanquitoEndpoint(String banquitoEndpoint) {
        this.banquitoEndpoint = banquitoEndpoint;
    }
}
