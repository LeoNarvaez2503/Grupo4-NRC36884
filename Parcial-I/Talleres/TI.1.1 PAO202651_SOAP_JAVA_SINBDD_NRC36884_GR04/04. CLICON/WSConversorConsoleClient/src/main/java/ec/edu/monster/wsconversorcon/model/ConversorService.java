package ec.edu.monster.wsconversorcon.model;

import ec.edu.monster.ws.WSConversorUnidades;
import ec.edu.monster.ws.WSConversorUnidades_Service;
import jakarta.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class ConversorService {

    private WSConversorUnidades getPort() {
        try {
            WSConversorUnidades_Service service
                    = new WSConversorUnidades_Service(new URL(ConfiguracionServidor.getUrlWsdl()));
            WSConversorUnidades port = service.getWSConversorUnidadesPort();
            ((BindingProvider) port).getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY, ConfiguracionServidor.getUrlServicio());
            return port;
        } catch (MalformedURLException ex) {
            throw new IllegalStateException("URL del servidor invalida: " + ConfiguracionServidor.getUrlWsdl(), ex);
        }
    }

    public String login(String usuario, String contrasenia) {
        return getPort().login(usuario, contrasenia);
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        return getPort().convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        return getPort().convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        return getPort().convertirTemperatura(valor, unidadInicial, unidadFinal);
    }
}
