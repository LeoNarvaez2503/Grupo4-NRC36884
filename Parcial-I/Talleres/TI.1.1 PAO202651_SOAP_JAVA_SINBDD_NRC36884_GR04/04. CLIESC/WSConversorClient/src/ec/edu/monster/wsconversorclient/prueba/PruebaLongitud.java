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
public class PruebaLongitud {
    public static void main(String[] args) {
        // datos
        int n1 = 80;
        
        // proceso
        ConversorService service = new ConversorService();
        double res = service.convertirLongitud(n1, "MILIMETRO", "YARDA");
        
        // impresion
        
        System.out.println("Conversion de Milimetro a Yarda: " + res);
    }
}
