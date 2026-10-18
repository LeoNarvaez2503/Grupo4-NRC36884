package ec.edu.monster.wsconversorcon.controller;

import ec.edu.monster.wsconversorcon.view.ConsolaView;

public class ConsolaControlador {

    private final ConsolaView vista = new ConsolaView();
    private final AppControlador appControlador = new AppControlador();

    public void iniciar() {
        vista.mostrarBanner();

        if (!realizarLogin()) {
            vista.mostrarMensaje("Acceso cancelado. Finalizando programa.");
            return;
        }

        bucleMenuPrincipal();
    }

    private boolean realizarLogin() {
        System.out.println("\n--- INICIO DE SESIÓN ---");
        System.out.println("(Credenciales oficiales: MONSTER / MONSTER9)");

        while (true) {
            String usuario = vista.leerTexto("Usuario");
            String contrasenia = vista.leerContrasenia("Contraseña");

            if (usuario.isEmpty() || contrasenia.isEmpty()) {
                vista.mostrarMensaje("Por favor complete todos los campos.");
                continue;
            }

            try {
                String token = appControlador.login(usuario, contrasenia);
                if (token != null && !token.isBlank()) {
                    vista.mostrarMensaje("¡Bienvenido " + usuario + "! Sesión iniciada correctamente.");
                    return true;
                } else {
                    vista.mostrarError("Usuario o contraseña incorrectos.");
                }
            } catch (Exception e) {
                vista.mostrarError("No se pudo conectar con el servidor REST o credenciales incorrectas: " + e.getMessage());
            }

            String reintentar = vista.leerTexto("¿Desea reintentar? (s/n)");
            if (!reintentar.equalsIgnoreCase("s")) {
                return false;
            }
        }
    }

    private void bucleMenuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            vista.mostrarMenuPrincipal();
            int opcion = vista.leerEntero("Seleccione una opción", 1, 4);

            switch (opcion) {
                case 1:
                    procesarLongitud();
                    break;
                case 2:
                    procesarMasa();
                    break;
                case 3:
                    procesarTemperatura();
                    break;
                case 4:
                    continuar = false;
                    vista.mostrarMensaje("¡Gracias por utilizar el WS Conversor! Hasta luego.");
                    break;
            }
        }
    }

    private void procesarLongitud() {
        try {
            System.out.println("\n>>> CONVERSOR DE LONGITUDES <<<");
            String unidadInicial = vista.seleccionarUnidadLongitud("Inicial");
            double valor = vista.leerDouble("Ingrese el valor a convertir");
            String unidadFinal = vista.seleccionarUnidadLongitud("Final");

            double resultado = appControlador.convertirLongitud(valor, unidadInicial, unidadFinal);
            vista.mostrarResultado(resultado);
        } catch (Exception e) {
            vista.mostrarError("Error en la conversión: " + e.getMessage());
        }
    }

    private void procesarMasa() {
        try {
            System.out.println("\n>>> CONVERSOR DE MASA <<<");
            String unidadInicial = vista.seleccionarUnidadMasa("Inicial");
            double valor = vista.leerDouble("Ingrese el valor a convertir");
            String unidadFinal = vista.seleccionarUnidadMasa("Final");

            double resultado = appControlador.convertirMasa(valor, unidadInicial, unidadFinal);
            vista.mostrarResultado(resultado);
        } catch (Exception e) {
            vista.mostrarError("Error en la conversión: " + e.getMessage());
        }
    }

    private void procesarTemperatura() {
        try {
            System.out.println("\n>>> CONVERSOR DE TEMPERATURA <<<");
            String unidadInicial = vista.seleccionarUnidadTemperatura("Inicial");
            double valor = vista.leerDouble("Ingrese el valor a convertir");
            String unidadFinal = vista.seleccionarUnidadTemperatura("Final");

            double resultado = appControlador.convertirTemperatura(valor, unidadInicial, unidadFinal);
            vista.mostrarResultado(resultado);
        } catch (Exception e) {
            vista.mostrarError("Error en la conversión: " + e.getMessage());
        }
    }
}
