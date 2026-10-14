/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.wsconversorclient.service;
import ec.edu.monster.ws.WSConversorUnidades;
import ec.edu.monster.ws.WSConversorUnidades_Service;

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

    public String login(String usuario, String contrasenia) {
        WSConversorUnidades_Service service = new WSConversorUnidades_Service();
        WSConversorUnidades port = service.getWSConversorUnidadesPort();
        String token = port.login(usuario, contrasenia);
        tokenSesion = token;
        return token;
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades_Service service = new WSConversorUnidades_Service();
        WSConversorUnidades port = service.getWSConversorUnidadesPort();
        return port.convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades_Service service = new WSConversorUnidades_Service();
        WSConversorUnidades port = service.getWSConversorUnidadesPort();
        return port.convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        WSConversorUnidades_Service service = new WSConversorUnidades_Service();
        WSConversorUnidades port = service.getWSConversorUnidadesPort();
        return port.convertirTemperatura(valor, unidadInicial, unidadFinal);
    }
    
}
