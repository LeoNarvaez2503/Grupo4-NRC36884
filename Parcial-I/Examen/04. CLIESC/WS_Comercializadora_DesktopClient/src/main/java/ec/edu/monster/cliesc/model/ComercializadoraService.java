package ec.edu.monster.cliesc.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.cliesc.model.FacturaModel.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ComercializadoraService {

    private final ConfiguracionServidor config = new ConfiguracionServidor();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final Gson gson = new Gson();

    public List<TelefonoModel> listarTelefonos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getComEndpoint() + "/telefonos"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<TelefonoModel>>() {}.getType());
            }
        } catch (Exception e) {
            System.err.println("[Error listarTelefonos]: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public boolean crearTelefono(TelefonoModel telefono) {
        try {
            String json = gson.toJson(telefono);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getComEndpoint() + "/telefonos"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("[Error crearTelefono]: " + e.getMessage());
            return false;
        }
    }

    public SujetoCreditoResponse verificarSujetoCredito(String cedula) {
        try {
            String url = config.getBanquitoEndpoint() + "/es-sujeto/" + URLEncoder.encode(cedula, StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return gson.fromJson(response.body(), SujetoCreditoResponse.class);
        } catch (Exception e) {
            SujetoCreditoResponse err = new SujetoCreditoResponse();
            err.esSujeto = false;
            err.motivo = "Error de conexión con BanQuito: " + e.getMessage();
            return err;
        }
    }

    public MontoMaximoResponse obtenerMontoMaximo(int codCliente) {
        try {
            String url = config.getBanquitoEndpoint() + "/monto-maximo/" + codCliente;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return gson.fromJson(response.body(), MontoMaximoResponse.class);
        } catch (Exception e) {
            System.err.println("[Error obtenerMontoMaximo]: " + e.getMessage());
            return null;
        }
    }

    public FacturaResponse emitirFactura(FacturaRequest requestData) {
        try {
            String json = gson.toJson(requestData);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getComEndpoint() + "/facturar"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return gson.fromJson(response.body(), FacturaResponse.class);
        } catch (Exception e) {
            FacturaResponse err = new FacturaResponse();
            err.exito = false;
            err.mensaje = "Error de comunicación: " + e.getMessage();
            return err;
        }
    }

    public List<CuotaAmortizacion> consultarTablaAmortizacion(int codCredito) {
        try {
            String url = config.getBanquitoEndpoint() + "/tabla-amortizacion/" + codCredito;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<CuotaAmortizacion>>() {}.getType());
            }
        } catch (Exception e) {
            System.err.println("[Error consultarTablaAmortizacion]: " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
