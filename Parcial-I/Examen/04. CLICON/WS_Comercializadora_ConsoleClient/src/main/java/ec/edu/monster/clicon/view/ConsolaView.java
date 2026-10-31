package ec.edu.monster.clicon.view;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.model.FacturaModel.*;
import ec.edu.monster.clicon.model.TelefonoModel;
import java.util.List;
import java.util.Scanner;

public class ConsolaView {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BOLD = "\u001B[1m";

    private final AppControlador controlador;
    private final Scanner scanner;

    public ConsolaView(AppControlador controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        mostrarEncabezado();
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            String opt = scanner.nextLine().trim();
            switch (opt) {
                case "1":
                    verCatalogoTelefonos();
                    break;
                case "2":
                    verificarSujetoCredito();
                    break;
                case "3":
                    consultarMontoMaximo();
                    break;
                case "4":
                    facturarEfectivo();
                    break;
                case "5":
                    facturarCreditoDirecto();
                    break;
                case "6":
                    consultarTablaAmortizacion();
                    break;
                case "7":
                    ejecutarBateriaCuatroClientes();
                    break;
                case "8":
                    agregarTelefono();
                    break;
                case "0":
                    salir = true;
                    System.out.println("\n" + ANSI_CYAN + "Gracias por usar el Sistema Comercializadora. Saliendo..." + ANSI_RESET);
                    break;
                default:
                    System.out.println(ANSI_RED + "Opción inválida. Intente de nuevo." + ANSI_RESET);
            }
        }
    }

    private void mostrarEncabezado() {
        System.out.println(ANSI_CYAN + "================================================================================");
        System.out.println("  COMERCIALIZADORA DE TELÉFONOS & BANQUITO CRÉDITO - CLIENTE CONSOLA (GRUPO 04)");
        System.out.println("  Asignatura: Servicios Web / Distribuidos | NRC: 36884 | Java 21 RESTful       ");
        System.out.println("================================================================================" + ANSI_RESET);
    }

    private void mostrarMenu() {
        System.out.println("\n" + ANSI_BOLD + "--- MENÚ PRINCIPAL ---" + ANSI_RESET);
        System.out.println(" 1. Ver Catálogo de Celulares Disponibles");
        System.out.println(" 2. Verificar Sujeto de Crédito (BanQuito - por Cédula)");
        System.out.println(" 3. Consultar Monto Máximo de Crédito Aprobado (por Código Cliente)");
        System.out.println(" 4. Facturar Venta en Efectivo (Aplica Descuento Oficial 42%)");
        System.out.println(" 5. Facturar Venta con Crédito Directo BanQuito");
        System.out.println(" 6. Consultar Tabla de Amortización de un Crédito Otorgado");
        System.out.println(ANSI_YELLOW + " 7. [DEMO TRIBUNAL] Ejecutar Pruebas Automatizadas de los 4 Clientes Oficiales" + ANSI_RESET);
        System.out.println(" 8. Agregar Nuevo Teléfono al Catálogo");
        System.out.println(" 0. Salir");
    }

    private void verCatalogoTelefonos() {
        System.out.println("\n" + ANSI_CYAN + "=== CATÁLOGO DE TELÉFONOS CELULARES DISPONIBLES ===" + ANSI_RESET);
        List<TelefonoModel> lista = controlador.listarTelefonos();
        if (lista.isEmpty()) {
            System.out.println(ANSI_RED + "No hay teléfonos disponibles o el servicio está offline." + ANSI_RESET);
            return;
        }
        System.out.printf("%-5s | %-12s | %-22s | %-10s | %-12s%n", "ID", "MARCA", "MODELO", "PRECIO", "ESTADO");
        System.out.println("-------------------------------------------------------------------------");
        for (TelefonoModel t : lista) {
            System.out.printf("%-5d | %-12s | %-22s | $%-9.2f | %-12s%n",
                    t.getCodTelefono(), t.getMarca(), t.getModelo(), t.getPrecio(), t.getEstado());
        }
    }

    private void verificarSujetoCredito() {
        System.out.println("\n" + ANSI_CYAN + "=== VERIFICAR SUJETO DE CRÉDITO EN BANQUITO ===" + ANSI_RESET);
        System.out.print("Ingrese número de cédula: ");
        String cedula = scanner.nextLine().trim();
        SujetoCreditoResponse resp = controlador.verificarSujeto(cedula);
        if (resp.esSujeto) {
            System.out.println(ANSI_GREEN + "[APROBADO] " + resp.motivo + ANSI_RESET);
            System.out.println(" - Cliente: " + resp.nombre + " (Cód: " + resp.codCliente + ") | Edad: " + resp.edad + " | Género: " + resp.genero);
        } else {
            System.out.println(ANSI_RED + "[RECHAZADO] " + resp.motivo + ANSI_RESET);
        }
    }

    private void consultarMontoMaximo() {
        System.out.println("\n" + ANSI_CYAN + "=== MONTO MÁXIMO DE CRÉDITO AUTORIZADO ===" + ANSI_RESET);
        System.out.print("Ingrese el Código de Cliente BanQuito: ");
        try {
            int cod = Integer.parseInt(scanner.nextLine().trim());
            MontoMaximoResponse resp = controlador.consultarMontoMaximo(cod);
            if (resp != null) {
                System.out.println(ANSI_GREEN + "Cálculo Trimestral Exitoso:" + ANSI_RESET);
                System.out.printf(" - Promedio Depósitos (últimos 3 meses): $%.2f%n", resp.promedioDepositos);
                System.out.printf(" - Promedio Retiros (últimos 3 meses):   $%.2f%n", resp.promedioRetiros);
                System.out.printf(" - Capacidad Neta:                      $%.2f%n", resp.diferenciaPromedios);
                System.out.printf(ANSI_BOLD + " - MONTO MÁXIMO AUTORIZADO (30%% * 6):   $%.2f%n" + ANSI_RESET, resp.montoMaximo);
            } else {
                System.out.println(ANSI_RED + "Cliente no encontrado o sin datos financieros." + ANSI_RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Código inválido." + ANSI_RESET);
        }
    }

    private void facturarEfectivo() {
        System.out.println("\n" + ANSI_CYAN + "=== FACTURACIÓN CON PAGO EN EFECTIVO (DESCUENTO 42%) ===" + ANSI_RESET);
        verCatalogoTelefonos();
        try {
            System.out.print("Ingrese Cédula del comprador: ");
            String cedula = scanner.nextLine().trim();
            System.out.print("Ingrese Nombre del comprador: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Ingrese ID del Teléfono a comprar: ");
            int codTel = Integer.parseInt(scanner.nextLine().trim());

            FacturaRequest req = new FacturaRequest();
            req.cedula = cedula;
            req.nombre = nombre;
            req.codTelefono = codTel;
            req.formaPago = "EFECTIVO";

            FacturaResponse resp = controlador.facturar(req);
            imprimirFactura(resp);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en los datos ingresados: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void facturarCreditoDirecto() {
        System.out.println("\n" + ANSI_CYAN + "=== FACTURACIÓN CON CRÉDITO DIRECTO BANQUITO ===" + ANSI_RESET);
        verCatalogoTelefonos();
        try {
            System.out.print("Ingrese Cédula del cliente (debe existir en BanQuito): ");
            String cedula = scanner.nextLine().trim();
            System.out.print("Ingrese Nombre del cliente: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Ingrese ID del Teléfono a financiar: ");
            int codTel = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Ingrese Plazo en meses (3 a 18): ");
            int cuotas = Integer.parseInt(scanner.nextLine().trim());

            FacturaRequest req = new FacturaRequest();
            req.cedula = cedula;
            req.nombre = nombre;
            req.codTelefono = codTel;
            req.formaPago = "CREDITO_DIRECTO";
            req.cuotas = cuotas;

            FacturaResponse resp = controlador.facturar(req);
            imprimirFactura(resp);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en los datos ingresados: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void consultarTablaAmortizacion() {
        System.out.println("\n" + ANSI_CYAN + "=== CONSULTA DE TABLA DE AMORTIZACIÓN ===" + ANSI_RESET);
        System.out.print("Ingrese el Código de Crédito: ");
        try {
            int codCredito = Integer.parseInt(scanner.nextLine().trim());
            List<CuotaAmortizacion> cuotas = controlador.consultarTablaAmortizacion(codCredito);
            if (cuotas.isEmpty()) {
                System.out.println(ANSI_RED + "No se encontraron cuotas para el crédito #" + codCredito + ANSI_RESET);
                return;
            }
            imprimirTablaAmortizacion(cuotas);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Número inválido." + ANSI_RESET);
        }
    }

    public void ejecutarBateriaCuatroClientes() {
        System.out.println("\n" + ANSI_YELLOW + "================================================================================");
        System.out.println("  EJECUTANDO BATERÍA OFICIAL DE EVALUACIÓN ANTE EL TRIBUNAL (4 CLIENTES)        ");
        System.out.println("================================================================================" + ANSI_RESET);

        // Caso 1: Carlos Andrade (Aprobado Credito Directo)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 1: CLIENTE 1 (Carlos Andrade - C.I. 1710000001) - Crédito Directo" + ANSI_RESET);
        FacturaRequest r1 = new FacturaRequest();
        r1.cedula = "1710000001";
        r1.nombre = "Carlos Andrade";
        r1.codTelefono = 1; // Motorola $450
        r1.formaPago = "CREDITO_DIRECTO";
        r1.cuotas = 6;
        FacturaResponse f1 = controlador.facturar(r1);
        imprimirFactura(f1);

        // Caso 2: Andrea Morales (Excede cupo maximo)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 2: CLIENTE 2 (Andrea Morales - C.I. 1710000002) - Crédito Directo ($850.00)" + ANSI_RESET);
        FacturaRequest r2 = new FacturaRequest();
        r2.cedula = "1710000002";
        r2.nombre = "Andrea Morales";
        r2.codTelefono = 2; // Samsung $850
        r2.formaPago = "CREDITO_DIRECTO";
        r2.cuotas = 12;
        FacturaResponse f2 = controlador.facturar(r2);
        imprimirFactura(f2);

        // Caso 3: Mateo Salazar (Rechazado edad < 25)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 3: CLIENTE 3 (Mateo Salazar - C.I. 1710000003) - Varón de 22 años" + ANSI_RESET);
        FacturaRequest r3 = new FacturaRequest();
        r3.cedula = "1710000003";
        r3.nombre = "Mateo Salazar";
        r3.codTelefono = 1;
        r3.formaPago = "CREDITO_DIRECTO";
        r3.cuotas = 6;
        FacturaResponse f3 = controlador.facturar(r3);
        imprimirFactura(f3);

        // Caso 4: Elena Castro (Pago en Efectivo con 42% desc.)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 4: CLIENTE 4 (Elena Castro - C.I. 1710000004) - Efectivo (42% Desc.)" + ANSI_RESET);
        FacturaRequest r4 = new FacturaRequest();
        r4.cedula = "1710000004";
        r4.nombre = "Elena Castro";
        r4.codTelefono = 1; // Motorola $450 -> Total $261
        r4.formaPago = "EFECTIVO";
        FacturaResponse f4 = controlador.facturar(r4);
        imprimirFactura(f4);

        System.out.println("\n" + ANSI_GREEN + "Batería de pruebas de los 4 clientes finalizada." + ANSI_RESET);
    }

    private void agregarTelefono() {
        System.out.println("\n" + ANSI_CYAN + "=== AGREGAR TELÉFONO AL CATÁLOGO ===" + ANSI_RESET);
        try {
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine().trim();
            System.out.print("Precio (USD): ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Características: ");
            String carac = scanner.nextLine().trim();

            TelefonoModel t = new TelefonoModel(0, marca, modelo, precio, carac, "https://via.placeholder.com/300", "DISPONIBLE");
            boolean ok = controlador.agregarTelefono(t);
            if (ok) {
                System.out.println(ANSI_GREEN + "Teléfono agregado exitosamente al catálogo." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "No se pudo agregar el teléfono." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en los datos: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void imprimirFactura(FacturaResponse f) {
        if (f == null) {
            System.out.println(ANSI_RED + "Respuesta nula del servidor." + ANSI_RESET);
            return;
        }
        if (!f.exito) {
            System.out.println(ANSI_RED + ">> TRANSACCIÓN RECHAZADA: " + f.mensaje + ANSI_RESET);
            return;
        }

        System.out.println("\n" + ANSI_GREEN + "================================================================================");
        System.out.println("  TRANSACCIÓN EXITOSA: Factura #" + f.facturaId);
        System.out.println("================================================================================" + ANSI_RESET);
        System.out.println("  Detalle: " + f.mensaje);

        if (f.tabla != null && !f.tabla.isEmpty()) {
            imprimirTablaAmortizacion(f.tabla);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void imprimirTablaAmortizacion(List<CuotaAmortizacion> cuotas) {
        System.out.println("\n" + ANSI_CYAN + "--- TABLA DE AMORTIZACIÓN OFICIAL BANQUITO (SISTEMA FRANCÉS 16.5% ANUAL) ---" + ANSI_RESET);
        System.out.printf("%-8s | %-12s | %-15s | %-15s | %-12s%n",
                "# Cuota", "Valor Cuota", "Interés Pagado", "Capital Pagado", "Saldo");
        System.out.println("-------------------------------------------------------------------------");
        for (CuotaAmortizacion c : cuotas) {
            System.out.printf("%-8d | $%-11.2f | $%-14.2f | $%-14.2f | $%-11.2f%n",
                    c.numCuota, c.valorCuota, c.interesPagado, c.capitalPagado, c.saldo);
        }
    }
}
