package ec.edu.monster.wsconversormov.controller;

import ec.edu.monster.wsconversormov.model.ConversorService;

public class AppControlador {

    private final ConversorService service = new ConversorService();

    public String login(String usuario, String contrasenia) throws Exception {
        return service.login(usuario, contrasenia);
    }

    public double convertirLongitud(double valor, String unidadInicial, String unidadFinal) throws Exception {
        return service.convertirLongitud(valor, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double valor, String unidadInicial, String unidadFinal) throws Exception {
        return service.convertirMasa(valor, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double valor, String unidadInicial, String unidadFinal) throws Exception {
        return service.convertirTemperatura(valor, unidadInicial, unidadFinal);
    }
}
