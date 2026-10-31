package ec.edu.monster.clicon.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConfiguracionServidor {
    private String comEndpoint = "http://localhost:8090/api";
    private String banquitoEndpoint = "http://localhost:8080/api/credito";
    private String resolvedBanquito = null;

    public String getComEndpoint() {
        return comEndpoint;
    }

    public void setComEndpoint(String comEndpoint) {
        this.comEndpoint = comEndpoint;
    }

    public String getBanquitoEndpoint() {
        if (resolvedBanquito != null) {
            return resolvedBanquito;
        }
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(banquitoEndpoint)).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBanquito = banquitoEndpoint;
                return resolvedBanquito;
            }
        } catch (Exception ignored) {}

        String payaraUrl = "http://localhost:8080/WS_BanQuito_Java_REST/api/credito";
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(800)).build();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(payaraUrl)).timeout(Duration.ofMillis(800)).GET().build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                resolvedBanquito = payaraUrl;
                return resolvedBanquito;
            }
        } catch (Exception ignored) {}

        resolvedBanquito = banquitoEndpoint;
        return resolvedBanquito;
    }

    public void setBanquitoEndpoint(String banquitoEndpoint) {
        this.banquitoEndpoint = banquitoEndpoint;
        this.resolvedBanquito = banquitoEndpoint;
    }
}
