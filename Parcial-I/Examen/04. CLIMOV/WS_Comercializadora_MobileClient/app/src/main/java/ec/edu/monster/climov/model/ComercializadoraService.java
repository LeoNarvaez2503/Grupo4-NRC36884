package ec.edu.monster.climov.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ec.edu.monster.climov.model.FacturaModel.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ComercializadoraService {

    private final ConfiguracionConexion config = new ConfiguracionConexion();
    private final Gson gson = new Gson();

    public List<TelefonoModel> listarTelefonos() {
        try {
            String json = realizarPeticion(config.getComEndpoint() + "/telefonos", "GET", null);
            if (json != null) {
                return gson.fromJson(json, new TypeToken<List<TelefonoModel>>() {}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public SujetoCreditoResponse verificarSujetoCredito(String cedula) {
        try {
            String url = config.getBanquitoEndpoint() + "/es-sujeto/" + URLEncoder.encode(cedula, "UTF-8");
            String json = realizarPeticion(url, "GET", null);
            if (json != null) {
                return gson.fromJson(json, SujetoCreditoResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        SujetoCreditoResponse err = new SujetoCreditoResponse();
        err.esSujeto = false;
        err.motivo = "Error de conexión con el servicio BanQuito";
        return err;
    }

    public MontoMaximoResponse obtenerMontoMaximo(int codCliente) {
        try {
            String url = config.getBanquitoEndpoint() + "/monto-maximo/" + codCliente;
            String json = realizarPeticion(url, "GET", null);
            if (json != null) {
                return gson.fromJson(json, MontoMaximoResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public FacturaResponse emitirFactura(FacturaRequest requestData) {
        try {
            String jsonBody = gson.toJson(requestData);
            String jsonResp = realizarPeticion(config.getComEndpoint() + "/facturar", "POST", jsonBody);
            if (jsonResp != null) {
                return gson.fromJson(jsonResp, FacturaResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FacturaResponse err = new FacturaResponse();
        err.exito = false;
        err.mensaje = "Error al emitir factura en la Comercializadora";
        return err;
    }

    public List<CuotaAmortizacion> consultarTablaAmortizacion(int codCredito) {
        try {
            String url = config.getBanquitoEndpoint() + "/tabla-amortizacion/" + codCredito;
            String json = realizarPeticion(url, "GET", null);
            if (json != null) {
                return gson.fromJson(json, new TypeToken<List<CuotaAmortizacion>>() {}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private String realizarPeticion(String endpoint, String metodo, String body) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);

        if ("POST".equalsIgnoreCase(metodo) && body != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
        if (is == null) return null;

        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) {
            sb.append(linea);
        }
        br.close();
        return sb.toString();
    }
}
