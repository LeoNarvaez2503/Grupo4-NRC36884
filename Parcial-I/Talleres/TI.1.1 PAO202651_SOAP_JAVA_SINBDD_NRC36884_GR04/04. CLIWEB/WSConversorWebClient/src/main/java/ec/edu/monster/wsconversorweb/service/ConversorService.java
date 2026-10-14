package ec.edu.monster.wsconversorweb.service;

import ec.edu.monster.ws.WSConversorUnidades;
import ec.edu.monster.ws.WSConversorUnidades_Service;

public class ConversorService {

    private WSConversorUnidades getPort() {
        WSConversorUnidades_Service service = new WSConversorUnidades_Service();
        return service.getWSConversorUnidadesPort();
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

    public String cambiarContrasenia(String actual, String nueva) {
        return getPort().cambiarContrasenia(actual, nueva);
    }
}
