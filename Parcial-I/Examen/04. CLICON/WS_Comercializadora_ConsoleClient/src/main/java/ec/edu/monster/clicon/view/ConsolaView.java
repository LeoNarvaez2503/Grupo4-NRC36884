package ec.edu.monster.clicon.view;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.model.FacturaModel.*;
import ec.edu.monster.clicon.model.TelefonoModel;
import java.text.Normalizer;
import java.util.ArrayList;
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
    private final List<ItemCarrito> carrito = new ArrayList<>();
    private static final int MAX_POR_ITEM = 10;

    public ConsolaView(AppControlador controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Sanitiza el texto eliminando diacriticos (tildes) y simbolos que se corrompen en Windows CMD.
     */
    public static String clean(String text) {
        if (text == null) return "";
        String s = text.replace("✔", "[OK]")
                       .replace("❌", "[X]")
                       .replace("¡", "")
                       .replace("¿", "");
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public void iniciar() {
        mostrarEncabezado();
        if (!autenticar()) {
            System.out.println(ANSI_RED + "Acceso denegado. Numero de intentos superado." + ANSI_RESET);
            return;
        }
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            System.out.print("Seleccione una opcion: ");
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
                    menuCarrito();
                    break;
                case "5":
                    facturarCarrito();
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
                case "9":
                    cambiarHostServidor();
                    break;
                case "0":
                    salir = true;
                    System.out.println("\n" + ANSI_CYAN + "Gracias por usar el Sistema Comercializadora. Saliendo..." + ANSI_RESET);
                    break;
                default:
                    System.out.println(ANSI_RED + "Opcion invalida. Intente de nuevo." + ANSI_RESET);
            }
        }
    }

    private boolean autenticar() {
        System.out.println(ANSI_YELLOW + "\n--- AUTENTICACION REQUERIDA (GRUPO 04 - MONSTER) ---" + ANSI_RESET);
        int intentos = 0;
        while (intentos < 3) {
            System.out.print("Usuario: ");
            String user = scanner.nextLine().trim();
            System.out.print("Contrasena: ");
            String pass = scanner.nextLine().trim();
            if (controlador.autenticar(user, pass)) {
                System.out.println(ANSI_GREEN + "[OK] Autenticacion exitosa desde BDD. Bienvenido, " + user + "!" + ANSI_RESET);
                return true;
            } else {
                intentos++;
                System.out.println(ANSI_RED + "[X] Credenciales incorrectas. (Requerido: Monster / Monster9) Intentos restantes: " + (3 - intentos) + ANSI_RESET);
            }
        }
        return false;
    }

    private void mostrarEncabezado() {
        System.out.println(ANSI_CYAN + "================================================================================");
        System.out.println("  COMERCIALIZADORA DE TELEFONOS & BANQUITO CREDITO - CLIENTE CONSOLA (GRUPO 04)");
        System.out.println("  Asignatura: Servicios Web / Distribuidos | NRC: 36884 | Java 21 RESTful       ");
        System.out.println("================================================================================" + ANSI_RESET);
    }

    private void mostrarMenu() {
        System.out.println("\n" + ANSI_BOLD + "--- MENU PRINCIPAL ---" + ANSI_RESET);
        System.out.println(" 1. Ver Catalogo de Celulares Disponibles");
        System.out.println(" 2. Verificar Sujeto de Credito (BanQuito - por Cedula)");
        System.out.println(" 3. Consultar Monto Maximo de Credito Aprobado (por Codigo Cliente)");
        System.out.println(" 4. Carrito de Compras: agregar / quitar / ver articulos (" + totalArticulos() + " en el carrito)");
        System.out.println(" 5. Facturar el Carrito (Efectivo con 42% o Credito Directo BanQuito)");
        System.out.println(" 6. Consultar Tabla de Amortizacion de un Credito Otorgado");
        System.out.println(ANSI_YELLOW + " 7. [DEMO TRIBUNAL] Ejecutar Pruebas Automatizadas de los Clientes Oficiales" + ANSI_RESET);
        System.out.println(" 8. Agregar Nuevo Telefono al Catalogo");
        System.out.println(" 9. Cambiar IP / host del servidor (actual: " + controlador.getHost() + ")");
        System.out.println(" 0. Salir");
    }

    private void cambiarHostServidor() {
        System.out.println("\n" + ANSI_BOLD + "=== CAMBIAR IP / HOST DEL SERVIDOR ===" + ANSI_RESET);
        System.out.println("Host actual: " + controlador.getHost());
        System.out.print("Nuevo host o IP (Enter para dejarlo igual): ");
        String nuevo = scanner.nextLine().trim();
        if (nuevo.isEmpty()) {
            return;
        }
        controlador.cambiarHost(nuevo);
        System.out.println(ANSI_GREEN + "[OK] Servidor configurado en: " + controlador.getHost() + ANSI_RESET);
    }

    private void verCatalogoTelefonos() {
        System.out.println("\n" + ANSI_CYAN + "=== CATALOGO DE TELEFONOS CELULARES DISPONIBLES ===" + ANSI_RESET);
        List<TelefonoModel> lista = controlador.listarTelefonos();
        if (lista.isEmpty()) {
            System.out.println(ANSI_RED + "No hay telefonos disponibles o el servicio esta offline." + ANSI_RESET);
            return;
        }
        System.out.printf("%-5s | %-12s | %-22s | %-10s | %-12s%n", "ID", "MARCA", "MODELO", "PRECIO", "ESTADO");
        System.out.println("-------------------------------------------------------------------------");
        for (TelefonoModel t : lista) {
            System.out.printf("%-5d | %-12s | %-22s | $%-9.2f | %-12s%n",
                    t.getCodTelefono(), clean(t.getMarca()), clean(t.getModelo()), t.getPrecio(), clean(t.getEstado()));
        }
    }

    private void verificarSujetoCredito() {
        System.out.println("\n" + ANSI_CYAN + "=== VERIFICAR SUJETO DE CREDITO EN BANQUITO ===" + ANSI_RESET);
        System.out.print("Ingrese numero de cedula: ");
        String cedula = scanner.nextLine().trim();
        SujetoCreditoResponse resp = controlador.verificarSujeto(cedula);
        if (resp.esSujeto) {
            System.out.println(ANSI_GREEN + "[APROBADO] " + clean(resp.motivo) + ANSI_RESET);
            System.out.println(" - Cliente: " + clean(resp.nombre) + " (Cod: " + resp.codCliente + ") | Edad: " + resp.edad + " | Genero: " + resp.genero);
        } else {
            System.out.println(ANSI_RED + "[RECHAZADO] " + clean(resp.motivo) + ANSI_RESET);
        }
    }

    private void consultarMontoMaximo() {
        System.out.println("\n" + ANSI_CYAN + "=== MONTO MAXIMO DE CREDITO AUTORIZADO ===" + ANSI_RESET);
        System.out.print("Ingrese el Codigo de Cliente BanQuito: ");
        try {
            int cod = Integer.parseInt(scanner.nextLine().trim());
            MontoMaximoResponse resp = controlador.consultarMontoMaximo(cod);
            if (resp != null) {
                System.out.println(ANSI_GREEN + "Calculo Trimestral Exitoso:" + ANSI_RESET);
                System.out.printf(" - Promedio Depositos (ultimos 3 meses): $%.2f%n", resp.promedioDepositos);
                System.out.printf(" - Promedio Retiros (ultimos 3 meses):   $%.2f%n", resp.promedioRetiros);
                System.out.printf(" - Capacidad Neta:                      $%.2f%n", resp.diferenciaPromedios);
                System.out.printf(ANSI_BOLD + " - MONTO MAXIMO AUTORIZADO (30%% * 6):   $%.2f%n" + ANSI_RESET, resp.montoMaximo);
            } else {
                System.out.println(ANSI_RED + "Cliente no encontrado o sin datos financieros." + ANSI_RESET);
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Codigo invalido." + ANSI_RESET);
        }
    }

    // =====================================================================
    // CARRITO DE COMPRAS
    // =====================================================================

    private int totalArticulos() {
        int n = 0;
        for (ItemCarrito i : carrito) n += i.cantidad;
        return n;
    }

    private TelefonoModel buscarTelefono(List<TelefonoModel> lista, int cod) {
        for (TelefonoModel t : lista) {
            if (t.getCodTelefono() == cod) return t;
        }
        return null;
    }

    private double subtotalCarrito(List<TelefonoModel> catalogo) {
        double sum = 0;
        for (ItemCarrito i : carrito) {
            TelefonoModel t = buscarTelefono(catalogo, i.codTelefono);
            if (t != null) sum += t.getPrecio() * i.cantidad;
        }
        return sum;
    }

    private void menuCarrito() {
        boolean volver = false;
        while (!volver) {
            verCarrito();
            System.out.println(ANSI_BOLD + "\n--- CARRITO DE COMPRAS ---" + ANSI_RESET);
            System.out.println(" A. Agregar telefono al carrito");
            System.out.println(" Q. Quitar telefono del carrito");
            System.out.println(" V. Vaciar carrito");
            System.out.println(" F. Facturar el carrito ahora");
            System.out.println(" 0. Volver al menu principal");
            System.out.print("Opcion: ");
            String op = scanner.nextLine().trim().toUpperCase();
            switch (op) {
                case "A": agregarAlCarrito(); break;
                case "Q": quitarDelCarrito(); break;
                case "V":
                    carrito.clear();
                    System.out.println(ANSI_YELLOW + "Carrito vaciado." + ANSI_RESET);
                    break;
                case "F": facturarCarrito(); break;
                case "0": volver = true; break;
                default: System.out.println(ANSI_RED + "Opcion invalida." + ANSI_RESET);
            }
        }
    }

    private void agregarAlCarrito() {
        List<TelefonoModel> catalogo = controlador.listarTelefonos();
        if (catalogo.isEmpty()) {
            System.out.println(ANSI_RED + "No hay telefonos disponibles o el servicio esta offline." + ANSI_RESET);
            return;
        }
        verCatalogoTelefonos();
        try {
            System.out.print("ID del telefono a agregar: ");
            int cod = Integer.parseInt(scanner.nextLine().trim());
            TelefonoModel t = buscarTelefono(catalogo, cod);
            if (t == null) {
                System.out.println(ANSI_RED + "El telefono #" + cod + " no existe en el catalogo." + ANSI_RESET);
                return;
            }
            System.out.print("Cantidad (1 a " + MAX_POR_ITEM + ") [1]: ");
            String q = scanner.nextLine().trim();
            int cant = q.isEmpty() ? 1 : Integer.parseInt(q);
            if (cant < 1) {
                System.out.println(ANSI_RED + "La cantidad debe ser al menos 1." + ANSI_RESET);
                return;
            }
            ItemCarrito existente = null;
            for (ItemCarrito i : carrito) {
                if (i.codTelefono == cod) { existente = i; break; }
            }
            int nueva = (existente != null ? existente.cantidad : 0) + cant;
            if (nueva > MAX_POR_ITEM) {
                System.out.println(ANSI_RED + "Maximo " + MAX_POR_ITEM + " unidades por articulo." + ANSI_RESET);
                return;
            }
            if (existente != null) existente.cantidad = nueva; else carrito.add(new ItemCarrito(cod, cant));
            System.out.println(ANSI_GREEN + "[OK] " + cant + " x " + clean(t.getMarca()) + " " + clean(t.getModelo()) + " agregado al carrito." + ANSI_RESET);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Numero invalido." + ANSI_RESET);
        }
    }

    private void quitarDelCarrito() {
        if (carrito.isEmpty()) {
            System.out.println(ANSI_YELLOW + "El carrito ya esta vacio." + ANSI_RESET);
            return;
        }
        try {
            System.out.print("ID del telefono a quitar: ");
            int cod = Integer.parseInt(scanner.nextLine().trim());
            ItemCarrito encontrado = null;
            for (ItemCarrito i : carrito) {
                if (i.codTelefono == cod) { encontrado = i; break; }
            }
            if (encontrado == null) {
                System.out.println(ANSI_RED + "Ese telefono no esta en el carrito." + ANSI_RESET);
                return;
            }
            System.out.print("Unidades a quitar (Enter = todas) [" + encontrado.cantidad + "]: ");
            String q = scanner.nextLine().trim();
            int quitar = q.isEmpty() ? encontrado.cantidad : Integer.parseInt(q);
            if (quitar >= encontrado.cantidad) {
                carrito.remove(encontrado);
            } else if (quitar > 0) {
                encontrado.cantidad -= quitar;
            }
            System.out.println(ANSI_YELLOW + "Carrito actualizado." + ANSI_RESET);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Numero invalido." + ANSI_RESET);
        }
    }

    private void verCarrito() {
        System.out.println("\n" + ANSI_CYAN + "=== CARRITO DE COMPRAS ===" + ANSI_RESET);
        if (carrito.isEmpty()) {
            System.out.println("  (vacio)");
            return;
        }
        List<TelefonoModel> catalogo = controlador.listarTelefonos();
        System.out.printf("%-5s | %-34s | %-10s | %-5s | %-11s%n", "ID", "DISPOSITIVO", "P. UNIT.", "CANT.", "SUBTOTAL");
        System.out.println("---------------------------------------------------------------------------");
        for (ItemCarrito i : carrito) {
            TelefonoModel t = buscarTelefono(catalogo, i.codTelefono);
            String nombre = t != null ? clean(t.getMarca() + " " + t.getModelo()) : "(no disponible)";
            double pu = t != null ? t.getPrecio() : 0;
            System.out.printf("%-5d | %-34s | $%-9.2f | %-5d | $%-10.2f%n", i.codTelefono, nombre, pu, i.cantidad, pu * i.cantidad);
        }
        System.out.println("---------------------------------------------------------------------------");
        double sub = subtotalCarrito(catalogo);
        System.out.printf(ANSI_BOLD + "  Articulos: %d | TOTAL CARRITO: $%.2f%n" + ANSI_RESET, totalArticulos(), sub);
        System.out.printf("  (En efectivo con 42%% de descuento pagaria: $%.2f)%n", sub * 0.58);
    }

    /** Factura TODO el carrito en una sola factura (Efectivo o Credito Directo). */
    private void facturarCarrito() {
        System.out.println("\n" + ANSI_CYAN + "=== FACTURAR CARRITO ===" + ANSI_RESET);
        if (carrito.isEmpty()) {
            System.out.println(ANSI_RED + "El carrito esta vacio. Use la opcion 4 para agregar telefonos." + ANSI_RESET);
            return;
        }
        verCarrito();
        try {
            System.out.print("\nForma de pago: 1) Efectivo (42% desc.)  2) Credito Directo BanQuito: ");
            String fp = scanner.nextLine().trim();
            if (!fp.equals("1") && !fp.equals("2")) {
                System.out.println(ANSI_RED + "Forma de pago invalida." + ANSI_RESET);
                return;
            }
            System.out.print("Ingrese Cedula del cliente: ");
            String cedula = scanner.nextLine().trim();
            System.out.print("Ingrese Nombre del cliente: ");
            String nombre = scanner.nextLine().trim();

            FacturaRequest req = new FacturaRequest();
            req.cedula = cedula;
            req.nombre = nombre;
            for (ItemCarrito i : carrito) req.items.add(new ItemCarrito(i.codTelefono, i.cantidad));

            if (fp.equals("1")) {
                req.formaPago = "EFECTIVO";
            } else {
                req.formaPago = "CREDITO_DIRECTO";
                System.out.print("Ingrese Plazo en meses (3 a 18): ");
                req.cuotas = Integer.parseInt(scanner.nextLine().trim());
            }

            FacturaResponse resp = controlador.facturar(req);
            imprimirFactura(resp);
            if (resp != null && resp.exito) {
                carrito.clear(); // compra finalizada
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en los datos ingresados: " + clean(e.getMessage()) + ANSI_RESET);
        }
    }

    private void consultarTablaAmortizacion() {
        System.out.println("\n" + ANSI_CYAN + "=== CONSULTA DE TABLA DE AMORTIZACION ===" + ANSI_RESET);
        System.out.print("Ingrese el Codigo de Credito: ");
        try {
            int codCredito = Integer.parseInt(scanner.nextLine().trim());
            List<CuotaAmortizacion> cuotas = controlador.consultarTablaAmortizacion(codCredito);
            if (cuotas.isEmpty()) {
                System.out.println(ANSI_RED + "No se encontraron cuotas para el credito #" + codCredito + ANSI_RESET);
                return;
            }
            imprimirTablaAmortizacion(cuotas);
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "Numero invalido." + ANSI_RESET);
        }
    }

    public void ejecutarBateriaCuatroClientes() {
        System.out.println("\n" + ANSI_YELLOW + "================================================================================");
        System.out.println("  EJECUTANDO BATERIA OFICIAL DE EVALUACION ANTE EL TRIBUNAL                     ");
        System.out.println("================================================================================" + ANSI_RESET);

        // Caso 1: Carlos Andrade (Aprobado Credito Directo $450 Motorola)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 1: CLIENTE 1 (Carlos Andrade - C.I. 1710000001) - Credito Directo ($450.00)" + ANSI_RESET);
        FacturaRequest r1 = new FacturaRequest();
        r1.cedula = "1710000001";
        r1.nombre = "Carlos Andrade";
        r1.items.add(new ItemCarrito(1, 1)); // Motorola $450
        r1.formaPago = "CREDITO_DIRECTO";
        r1.cuotas = 6;
        FacturaResponse f1 = controlador.facturar(r1);
        imprimirFactura(f1);

        // Caso 2: Andrea Morales (Excede cupo si pide celular caro como Samsung $850)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 2: CLIENTE 2 (Andrea Morales - C.I. 1710000002) - Excede Cupo ($850.00)" + ANSI_RESET);
        FacturaRequest r2 = new FacturaRequest();
        r2.cedula = "1710000002";
        r2.nombre = "Andrea Morales";
        r2.items.add(new ItemCarrito(2, 1)); // Samsung $850 (cupo maximo es $579.38 -> denegado)
        r2.formaPago = "CREDITO_DIRECTO";
        r2.cuotas = 12;
        FacturaResponse f2 = controlador.facturar(r2);
        imprimirFactura(f2);

        // Caso 3: Mateo Salazar (Rechazado edad < 25)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 3: CLIENTE 3 (Mateo Salazar - C.I. 1710000003) - Varon de 22 anos" + ANSI_RESET);
        FacturaRequest r3 = new FacturaRequest();
        r3.cedula = "1710000003";
        r3.nombre = "Mateo Salazar";
        r3.items.add(new ItemCarrito(1, 1));
        r3.formaPago = "CREDITO_DIRECTO";
        r3.cuotas = 6;
        FacturaResponse f3 = controlador.facturar(r3);
        imprimirFactura(f3);

        // Caso 4: Elena Castro (Pago en Efectivo con 42% desc.)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 4: CLIENTE 4 (Elena Castro - C.I. 1710000004) - Efectivo (42% Desc.)" + ANSI_RESET);
        FacturaRequest r4 = new FacturaRequest();
        r4.cedula = "1710000004";
        r4.nombre = "Elena Castro";
        r4.items.add(new ItemCarrito(1, 1)); // Motorola $450 -> Total $261
        r4.formaPago = "EFECTIVO";
        FacturaResponse f4 = controlador.facturar(r4);
        imprimirFactura(f4);

        // Caso 5: David Paredes (Cliente con Cupo Alto $2,640)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 5: CLIENTE 6 (David Paredes - C.I. 1710000006) - Credito Aprobado iPhone ($950.00)" + ANSI_RESET);
        FacturaRequest r5 = new FacturaRequest();
        r5.cedula = "1710000006";
        r5.nombre = "David Paredes";
        r5.items.add(new ItemCarrito(3, 1)); // iPhone 15 $950
        r5.formaPago = "CREDITO_DIRECTO";
        r5.cuotas = 12;
        FacturaResponse f5 = controlador.facturar(r5);
        imprimirFactura(f5);

        // Caso 6: carrito con VARIOS articulos en una sola factura (efectivo)
        System.out.println("\n" + ANSI_BOLD + ">>> CASO 6: CARRITO MULTI-ARTICULO (Elena Castro - Efectivo 42%) - 2 x ID 1 + 1 x ID 3 en UNA factura" + ANSI_RESET);
        FacturaRequest r6 = new FacturaRequest();
        r6.cedula = "1710000004";
        r6.nombre = "Elena Castro";
        r6.items.add(new ItemCarrito(1, 2));
        r6.items.add(new ItemCarrito(3, 1));
        r6.formaPago = "EFECTIVO";
        FacturaResponse f6 = controlador.facturar(r6);
        imprimirFactura(f6);

        System.out.println("\n" + ANSI_GREEN + "Bateria de pruebas finalizada." + ANSI_RESET);
    }

    private void agregarTelefono() {
        System.out.println("\n" + ANSI_CYAN + "=== AGREGAR TELEFONO AL CATALOGO ===" + ANSI_RESET);
        try {
            System.out.print("Marca: ");
            String marca = scanner.nextLine().trim();
            System.out.print("Modelo: ");
            String modelo = scanner.nextLine().trim();
            System.out.print("Precio (USD): ");
            double precio = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Caracteristicas: ");
            String carac = scanner.nextLine().trim();

            TelefonoModel t = new TelefonoModel(0, marca, modelo, precio, carac, "https://via.placeholder.com/300", "DISPONIBLE");
            boolean ok = controlador.agregarTelefono(t);
            if (ok) {
                System.out.println(ANSI_GREEN + "Telefono agregado exitosamente al catalogo." + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "No se pudo agregar el telefono." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en los datos: " + clean(e.getMessage()) + ANSI_RESET);
        }
    }

    private void imprimirFactura(FacturaResponse f) {
        if (f == null) {
            System.out.println(ANSI_RED + "Respuesta nula del servidor." + ANSI_RESET);
            return;
        }
        if (!f.exito) {
            System.out.println(ANSI_RED + ">> TRANSACCION RECHAZADA: " + clean(f.mensaje) + ANSI_RESET);
            return;
        }

        System.out.println("\n" + ANSI_GREEN + "================================================================================");
        System.out.println("  TRANSACCION EXITOSA: Factura #" + f.facturaId);
        System.out.println("================================================================================" + ANSI_RESET);
        System.out.println("  Detalle: " + clean(f.mensaje));

        FacturaInfo fi = f.factura;
        if (fi != null && fi.items != null && !fi.items.isEmpty()) {
            System.out.println("\n  Comprobante: " + clean(fi.numeroComprobante) + " | Cliente: " + clean(fi.nombreCliente) + " (C.I. " + clean(fi.cedulaCliente) + ")");
            System.out.println("  Forma de pago: " + clean(fi.formaPago));
            System.out.printf("  %-34s | %-10s | %-5s | %-11s%n", "DISPOSITIVO", "P. UNIT.", "CANT.", "SUBTOTAL");
            System.out.println("  ---------------------------------------------------------------------");
            for (FacturaDetalle d : fi.items) {
                System.out.printf("  %-34s | $%-9.2f | %-5d | $%-10.2f%n", clean(d.modeloTelefono), d.precioUnitario, d.cantidad, d.subtotal);
            }
            System.out.println("  ---------------------------------------------------------------------");
            System.out.printf("  Subtotal: $%.2f%n", fi.subtotal);
            if (fi.valorDescuento > 0) {
                System.out.printf("  Descuento (%.0f%%): -$%.2f%n", fi.porcentajeDescuento, fi.valorDescuento);
            }
            System.out.printf(ANSI_BOLD + "  TOTAL A PAGAR: $%.2f%n" + ANSI_RESET, fi.totalPagar);
            if (fi.codCreditoBanco != null && fi.numCuotas != null && fi.valorCuota != null) {
                System.out.printf("  Credito BanQuito #%d: %d cuotas de $%.2f%n", fi.codCreditoBanco, fi.numCuotas, fi.valorCuota);
            }
        }

        if (f.tabla != null && !f.tabla.isEmpty()) {
            imprimirTablaAmortizacion(f.tabla);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    private void imprimirTablaAmortizacion(List<CuotaAmortizacion> cuotas) {
        System.out.println("\n" + ANSI_CYAN + "--- TABLA DE AMORTIZACION OFICIAL BANQUITO (SISTEMA FRANCES 16.5% ANUAL) ---" + ANSI_RESET);
        System.out.printf("%-8s | %-12s | %-15s | %-15s | %-12s%n",
                "# Cuota", "Valor Cuota", "Interes Pagado", "Capital Pagado", "Saldo");
        System.out.println("-------------------------------------------------------------------------");
        for (CuotaAmortizacion c : cuotas) {
            System.out.printf("%-8d | $%-11.2f | $%-14.2f | $%-14.2f | $%-11.2f%n",
                    c.numCuota, c.valorCuota, c.interesPagado, c.capitalPagado, c.saldo);
        }
    }
}
