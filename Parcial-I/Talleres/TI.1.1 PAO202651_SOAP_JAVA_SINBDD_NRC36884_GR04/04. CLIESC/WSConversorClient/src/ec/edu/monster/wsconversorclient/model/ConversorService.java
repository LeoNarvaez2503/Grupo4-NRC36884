/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.wsconversorclient.model;

import ec.edu.monster.ws.WSConversorUnidades;
import ec.edu.monster.ws.WSConversorUnidades_Service;
import jakarta.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author meatpuppets
 */
public class ConversorService {

    private static String tokenSesion;

    public static String getTokenSesion() {
        return tokenSesion;
    }

    public static void setTokenSesion(String token) {
        tokenSesion = token;
    }

    private WSConversorUnidades crearPort() {
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
        WSConversorUnidades port = crearPort();
        String token = port.login(usuario, contrasenia);
        tokenSesion = token;
        return token;
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades port = crearPort();
        return port.convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades port = crearPort();
        return port.convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades port = crearPort();
        return port.convertirTemperatura(valor, unidadInicial, unidadFinal);
    }

}
