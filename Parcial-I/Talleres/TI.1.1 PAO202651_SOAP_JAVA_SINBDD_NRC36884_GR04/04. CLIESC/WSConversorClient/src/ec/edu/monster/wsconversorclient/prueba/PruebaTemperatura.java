/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.wsconversorclient.prueba;

import ec.edu.monster.wsconversorclient.model.ConversorService;

/**
 *
 * @author meatpuppets
 */
public class PruebaTemperatura {
    public static void main(String[] args) {
        // datos
        double n1 = 100.0;

        // proceso
        ConversorService service = new ConversorService();
        double res = service.convertirTemperatura(n1, "CELSIUS", "FAHRENHEIT");

        // impresion
        System.out.println("Conversion de 100 Celsius a Fahrenheit: " + res);
    }
}
