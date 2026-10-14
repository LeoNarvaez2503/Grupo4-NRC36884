/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.wsconversorclient.prueba;

import ec.edu.monster.wsconversorclient.service.ConversorService;

/**
 *
 * @author meatpuppets
 */
public class PruebaMasa {
    public static void main(String[] args) {
        // datos
        double n1 = 5.0;

        // proceso
        ConversorService service = new ConversorService();
        double res = service.convertirMasa(n1, "KILOGRAMO", "GRAMO");

        // impresion
        System.out.println("Conversion de Kilogramo a Gramo: " + res);
    }
}
