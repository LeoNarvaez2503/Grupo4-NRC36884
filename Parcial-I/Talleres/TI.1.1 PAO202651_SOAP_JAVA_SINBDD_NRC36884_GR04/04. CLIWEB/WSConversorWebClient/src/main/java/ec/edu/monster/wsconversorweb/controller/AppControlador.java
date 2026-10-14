package ec.edu.monster.wsconversorweb.controller;

import ec.edu.monster.wsconversorweb.service.ConversorService;

public class AppControlador {

    private final ConversorService service = new ConversorService();

    public String login(String usuario, String contrasenia) {
        return service.login(usuario, contrasenia);
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) {
        return service.convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) {
        return service.convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) {
        return service.convertirTemperatura(valor, unidadInicial, unidadFinal);
    }
}
