package ec.edu.monster.wsconversorcon.view;

import ec.edu.monster.wsconversorcon.model.UnidadLongitud;
import ec.edu.monster.wsconversorcon.model.UnidadMasa;
import ec.edu.monster.wsconversorcon.model.UnidadTemperatura;

import java.util.Scanner;

public class ConsolaView {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarBanner() {
        System.out.println("   /\\_______/\\");
        System.out.println("  /   o   o   \\     ╔═══════════════════════════════════════╗");
        System.out.println(" (   == ^ ==   )    ║   MONSTERS INC. - CONVERSOR REST      ║");
        System.out.println("  )   \\___/   (     ║   (Arquitectura MVC - Java Puro)      ║");
        System.out.println(" (  (_______)  )    ║   Personaje: James P. Sullivan        ║");
        System.out.println("  \\___________/     ╚═══════════════════════════════════════╝");
        System.out.println();
    }

    public String leerTexto(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return scanner.nextLine().trim();
    }

    public String leerContrasenia(String etiqueta) {
        if (System.console() != null) {
            char[] pass = System.console().readPassword(etiqueta + ": ");
            return new String(pass);
        } else {
            return leerTexto(etiqueta);
        }
    }

    public double leerDouble(String etiqueta) {
        while (true) {
            System.out.print(etiqueta + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println(" [!] Por favor ingrese un número válido.");
            }
        }
    }

    public int leerEntero(String etiqueta, int min, int max) {
        while (true) {
            System.out.print(etiqueta + " [" + min + "-" + max + "]: ");
            String input = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(input);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println(" [!] La opción debe estar entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println(" [!] Ingrese un número entero válido.");
            }
        }
    }

    public void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("--- MENÚ PRINCIPAL ---");
        System.out.println("1. Conversor de Longitudes");
        System.out.println("2. Conversor de Masa");
        System.out.println("3. Conversor de Temperatura");
        System.out.println("4. Salir");
    }

    public String seleccionarUnidadLongitud(String momento) {
        System.out.println("\nSeleccione Unidad " + momento + ":");
        UnidadLongitud[] valores = UnidadLongitud.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i].name() + " (" + valores[i].getEtiqueta() + ")");
        }
        int seleccion = leerEntero("Opción", 1, valores.length);
        return valores[seleccion - 1].name();
    }

    public String seleccionarUnidadMasa(String momento) {
        System.out.println("\nSeleccione Unidad " + momento + ":");
        UnidadMasa[] valores = UnidadMasa.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i].name() + " (" + valores[i].getEtiqueta() + ")");
        }
        int seleccion = leerEntero("Opción", 1, valores.length);
        return valores[seleccion - 1].name();
    }

    public String seleccionarUnidadTemperatura(String momento) {
        System.out.println("\nSeleccione Unidad " + momento + ":");
        UnidadTemperatura[] valores = UnidadTemperatura.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println((i + 1) + ". " + valores[i].name() + " (" + valores[i].getEtiqueta() + ")");
        }
        int seleccion = leerEntero("Opción", 1, valores.length);
        return valores[seleccion - 1].name();
    }

    public void mostrarResultado(double resultado) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("Resultado: " + resultado);
        System.out.println("==================================================");
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(" > " + mensaje);
    }

    public void mostrarError(String error) {
        System.err.println(" [X] Error: " + error);
    }
}
