/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.wsconversorclient.controller;

import ec.edu.monster.wsconversorclient.model.ConversorService;

/**
 *
 * @author meatpuppets
 */
public class AppControlador {

    private final ConversorService service = new ConversorService();

    public String login(String usuario, String contrasenia) {
        return service.login(usuario, contrasenia);
    }

    public double convertirLogintud(double n1, String unidadInicial, String unidadFinal){
        return service.convertirLongitud(n1, unidadInicial, unidadFinal);
    }

    public double convertirLongitud(double n1, String unidadInicial, String unidadFinal){
        return service.convertirLongitud(n1, unidadInicial, unidadFinal);
    }

    public double convertirMasa(double n1, String unidadInicial, String unidadFinal){
        return service.convertirMasa(n1, unidadInicial, unidadFinal);
    }

    public double convertirTemperatura(double n1, String unidadInicial, String unidadFinal){
        return service.convertirTemperatura(n1, unidadInicial, unidadFinal);
    }
}
