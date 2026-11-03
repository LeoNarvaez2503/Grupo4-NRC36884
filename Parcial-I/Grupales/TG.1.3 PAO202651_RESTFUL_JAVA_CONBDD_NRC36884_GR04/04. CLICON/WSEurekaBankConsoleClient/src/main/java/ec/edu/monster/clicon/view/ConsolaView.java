package ec.edu.monster.clicon.view;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.model.CuentaModel;
import ec.edu.monster.clicon.model.MovimientoModel;
import ec.edu.monster.clicon.model.ResultadoOperacion;
import ec.edu.monster.clicon.model.SucursalModel;
import ec.edu.monster.clicon.model.UsuarioSesion;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Capa Vista (Patr\u00f3n MVC Tradicional) para el Cliente de Consola de EurekaBank.
 */
public class ConsolaView {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private final AppControlador controlador;
    private final Scanner scanner;

    public ConsolaView(AppControlador controlador) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception ignored) {
        }
        this.controlador = controlador;
        this.scanner = new Scanner(System.in, StandardCharsets.UTF_8);
    }

    public void iniciar() {
        mostrarEncabezado();
        while (true) {
            if (controlador.getSesionActual() == null || !controlador.getSesionActual().isAutenticado()) {
                if (!mostrarPantallaLogin()) {
                    System.out.println("\nSesi\u00f3n finalizada. Hasta pronto.");
                    break;
                }
            } else {
                mostrarMenuPrincipal();
            }
        }
    }

    public void ejecutarDiagnosticoAutomatico() {
        mostrarEncabezado();
        System.out.println("[DEMO] Autenticando con credenciales maestras Monster / Monster9...");
        UsuarioSesion sesion = controlador.iniciarSesion("Monster", "Monster9");
        if (!sesion.isAutenticado()) {
            throw new IllegalStateException("Fallo login Monster/Monster9: " + sesion.getMensaje());
        }
        System.out.println(ANSI_GREEN + "[OK] " + sesion.getMensaje() + " | Token: " + sesion.getToken() + ANSI_RESET);

        List<SucursalModel> sucursales = controlador.obtenerSucursales();
        System.out.println("[OK] Sucursales cargadas desde el servidor: " + sucursales.size());

        List<CuentaModel> cuentas = controlador.obtenerCuentasPorSucursal(0);
        imprimirTablaCuentas(cuentas);

        System.out.println("\n[DEMO] Probando escenario concurrente de 3 Ventanillas simult\u00e1neas sobre cuenta 001-0001...");
        simularConcurrenciaTresVentanillas("001-0001", 10.00);

        List<MovimientoModel> movs = controlador.obtenerExtracto("001-0001");
        System.out.println("[OK] \u00daltimos movimientos de 001-0001: " + movs.size());
        if (!movs.isEmpty()) {
            MovimientoModel u = movs.get(0);
            System.out.printf("     \u00daltima Op: %s | %s | Monto: $%.2f | Saldo: $%.2f | Fecha: %s%n",
                    u.getNumeroOperacion(), u.getTipo(), u.getMonto(), u.getSaldoResultante(), u.getFechaHora());
        }
    }

    private void mostrarEncabezado() {
        System.out.println(ANSI_CYAN + "==============================================================================");
        System.out.println("          EUREKABANK - CLIENTE CONSOLA ADMINISTRATIVO (GRUPO 04)              ");
        System.out.println("          Dominio: ec.edu.monster | Arquitectura MVC Tradicional              ");
        System.out.println("          Servidor: " + controlador.getEndpointActual());
        System.out.println("==============================================================================" + ANSI_RESET);
    }

    private boolean mostrarPantallaLogin() {
        System.out.println("\n--- AUTENTICACI\u00d3N CENTRALIZADA ---");
        System.out.print("Usuario (ej. Monster / cajero1..4 / gerente1) [0 para salir]: ");
        String usr = scanner.nextLine().trim();
        if ("0".equals(usr)) {
            return false;
        }
        System.out.print("Contrase\u00f1a: ");
        String pwd = scanner.nextLine().trim();

        try {
            UsuarioSesion sesion = controlador.iniciarSesion(usr, pwd);
            if (sesion.isAutenticado()) {
                System.out.println(ANSI_GREEN + "\n[OK] " + sesion.getMensaje() + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "\n[ERROR] " + sesion.getMensaje() + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "\n[ERROR] Error al conectar con el servidor: " + e.getMessage() + ANSI_RESET);
        }
        return true;
    }

    private void mostrarMenuPrincipal() {
        UsuarioSesion s = controlador.getSesionActual();
        System.out.println("\n------------------------------------------------------------------------------");
        System.out.printf(" Usuario: %s (%s) | Ventanilla Activa: [%d]%n",
                s.getNombreCompleto(), s.getRol(), controlador.getVentanillaActiva());
        System.out.println("------------------------------------------------------------------------------");
        System.out.println(" 1. Listar Sucursales y Cuentas (Monitor Sem\u00e1foro MashUp VERDE/ROJO)");
        System.out.println(" 2. Consultar Cuenta por N\u00famero o DNI de Titular");
        System.out.println(" 3. Consultar Extracto Cronol\u00f3gico de Movimientos");
        System.out.println(" 4. Realizar Dep\u00f3sito Bancario (ACID + Bloqueo)");
        System.out.println(" 5. Realizar Retiro Bancario (Validaci\u00f3n Saldo + Bloqueo)");
        System.out.println(" 6. Realizar Transferencia entre Cuentas");
        System.out.println(" 7. Cambiar Ventanilla Operativa (1 a 4)");
        System.out.println(" 8. Simular Escenario Cr\u00edtico: 3 Ventanillas Simult\u00e1neas (MashUp)");
        System.out.println(" 9. Desbloqueo Administrativo Superadmin Monster");
        System.out.println(" 0. Cerrar Sesi\u00f3n");
        System.out.print("Seleccione una opci\u00f3n: ");

        String op = scanner.nextLine().trim();
        try {
            switch (op) {
                case "1" -> opcionListarCuentas();
                case "2" -> opcionBuscarCuenta();
                case "3" -> opcionExtracto();
                case "4" -> opcionDeposito();
                case "5" -> opcionRetiro();
                case "6" -> opcionTransferencia();
                case "7" -> opcionCambiarVentanilla();
                case "8" -> {
                    System.out.print("Cuenta objetivo (ej. 001-0001): ");
                    String cta = scanner.nextLine().trim();
                    simularConcurrenciaTresVentanillas(cta.isEmpty() ? "001-0001" : cta, 25.00);
                }
                case "9" -> opcionDesbloquear();
                case "0" -> controlador.cerrarSesion();
                default -> System.out.println(ANSI_YELLOW + "Opci\u00f3n no v\u00e1lida." + ANSI_RESET);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error de comunicaci\u00f3n con servidor: " + e.getMessage() + ANSI_RESET);
        }
    }

    private void opcionListarCuentas() {
        List<SucursalModel> sucursales = controlador.obtenerSucursales();
        System.out.println("\nSUCURSALES DISPONIBLES:");
        System.out.println("  [0] TODAS LAS SUCURSALES (Consolidado MashUp)");
        for (SucursalModel suc : sucursales) {
            System.out.printf("  [%d] %s - %s (%d cuentas)%n",
                    suc.getIdSucursal(), suc.getNombre(), suc.getCiudad(), suc.getTotalCuentas());
        }
        System.out.print("Ingrese ID de sucursal [0 = Todas]: ");
        String in = scanner.nextLine().trim();
        int idSuc = in.isEmpty() ? 0 : Integer.parseInt(in);
        imprimirTablaCuentas(controlador.obtenerCuentasPorSucursal(idSuc));
    }

    private void imprimirTablaCuentas(List<CuentaModel> cuentas) {
        System.out.println("\n+------------+------------------------+--------------+---------------+--------------------------+");
        System.out.println("| CUENTA     | TITULAR                | SALDO (USD)  | SEM\u00c1FORO      | DETALLE BLOQUEO          |");
        System.out.println("+------------+------------------------+--------------+---------------+--------------------------+");
        for (CuentaModel c : cuentas) {
            boolean verde = "VERDE".equalsIgnoreCase(c.getDisponibilidadMashup());
            String badge = verde
                    ? ANSI_GREEN + "[LIBRE] VERDE " + ANSI_RESET
                    : ANSI_RED + "[OCUP] ROJO  " + ANSI_RESET;
            String detalle = verde ? "Disponible"
                    : ("Ventanilla " + c.getVentanillaBloqueo() + " (" + (c.getBloqueadoPor() != null ? c.getBloqueadoPor() : "Activa") + ")");
            System.out.printf("| %-10s | %-22s | $%11.2f | %s | %-24s |%n",
                    c.getNumeroCuenta(),
                    recortar(c.getTitularCompleto(), 22),
                    c.getSaldo(),
                    badge,
                    recortar(detalle, 24));
        }
        System.out.println("+------------+------------------------+--------------+---------------+--------------------------+");
    }

    private void opcionBuscarCuenta() {
        System.out.print("Ingrese N\u00famero de Cuenta (ej. 001-0001) o DNI del Titular: ");
        String dato = scanner.nextLine().trim();
        if (dato.contains("-")) {
            CuentaModel c = controlador.buscarCuentaPorNumero(dato);
            if (c == null) {
                System.out.println(ANSI_RED + "Cuenta no encontrada." + ANSI_RESET);
            } else {
                imprimirTablaCuentas(List.of(c));
            }
        } else {
            List<CuentaModel> lista = controlador.buscarCuentasPorDni(dato);
            if (lista.isEmpty()) {
                System.out.println(ANSI_RED + "No se encontraron cuentas para el DNI indicado." + ANSI_RESET);
            } else {
                imprimirTablaCuentas(lista);
            }
        }
    }

    private void opcionExtracto() {
        System.out.print("N\u00famero de cuenta para extracto: ");
        String cta = scanner.nextLine().trim();
        List<MovimientoModel> movs = controlador.obtenerExtracto(cta);
        if (movs.isEmpty()) {
            System.out.println(ANSI_YELLOW + "Sin movimientos registrados para la cuenta " + cta + "." + ANSI_RESET);
            return;
        }
        System.out.println("\n+----------------------+----------------------+------------+------------+-------------------+---------------------+");
        System.out.println("| NRO OPERACI\u00d3N        | TIPO                 | MONTO      | SALDO RES. | CAJERO / VENT.    | FECHA / HORA        |");
        System.out.println("+----------------------+----------------------+------------+------------+-------------------+---------------------+");
        for (MovimientoModel m : movs) {
            System.out.printf("| %-20s | %-20s | $%9.2f | $%9.2f | V%d %-14s | %-19s |%n",
                    recortar(m.getNumeroOperacion(), 20),
                    recortar(m.getTipo(), 20),
                    m.getMonto(),
                    m.getSaldoResultante(),
                    m.getVentanilla(),
                    recortar(m.getNombreEmpleado(), 14),
                    recortar(m.getFechaHora(), 19));
        }
        System.out.println("+----------------------+----------------------+------------+------------+-------------------+---------------------+");
    }

    private void opcionDeposito() {
        System.out.print("Cuenta destino del dep\u00f3sito: ");
        String cta = scanner.nextLine().trim();
        System.out.print("Monto a depositar (USD): ");
        double monto = Double.parseDouble(scanner.nextLine().trim());
        System.out.println("Procesando en servidor (Bloqueo MashUp ROJO activo)...");
        mostrarResultado(controlador.depositar(cta, monto));
    }

    private void opcionRetiro() {
        System.out.print("Cuenta a debitar: ");
        String cta = scanner.nextLine().trim();
        System.out.print("Monto a retirar (USD): ");
        double monto = Double.parseDouble(scanner.nextLine().trim());
        System.out.println("Procesando en servidor (Validando fondos + Bloqueo MashUp ROJO)...");
        mostrarResultado(controlador.retirar(cta, monto));
    }

    private void opcionTransferencia() {
        System.out.print("Cuenta origen: ");
        String orig = scanner.nextLine().trim();
        System.out.print("Cuenta destino: ");
        String dest = scanner.nextLine().trim();
        System.out.print("Monto a transferir (USD): ");
        double monto = Double.parseDouble(scanner.nextLine().trim());
        System.out.println("Procesando transferencia at\u00f3mica en servidor...");
        mostrarResultado(controlador.transferir(orig, dest, monto));
    }

    private void opcionCambiarVentanilla() {
        System.out.print("Seleccione n\u00famero de ventanilla (1, 2, 3 o 4): ");
        int v = Integer.parseInt(scanner.nextLine().trim());
        controlador.setVentanillaActiva(v);
        System.out.println(ANSI_GREEN + "Ventanilla activa cambiada a: Ventanilla " + controlador.getVentanillaActiva() + ANSI_RESET);
    }

    private void opcionDesbloquear() {
        System.out.print("Cuenta a liberar manualmente (Superadmin Monster): ");
        String cta = scanner.nextLine().trim();
        mostrarResultado(controlador.desbloquearCuenta(cta));
    }

    private void simularConcurrenciaTresVentanillas(String cuenta, double monto) {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Callable<String>> tareas = new ArrayList<>();
            for (int v = 1; v <= 3; v++) {
                final int ventanilla = v;
                tareas.add(() -> {
                    if (ventanilla > 1) {
                        Thread.sleep(250L * ventanilla);
                    }
                    ResultadoOperacion r = controlador.depositarDesdeVentanilla(cuenta, monto, ventanilla);
                    return "[Ventanilla " + ventanilla + "] " + (r.isExito() ? "\u00c9XITO -> " : "BLOQUEADO -> ") + r.getMensaje();
                });
            }
            Future<String> monitorMashup = pool.submit(() -> {
                Thread.sleep(600L);
                CuentaModel estadoDurante = controlador.buscarCuentaPorNumero(cuenta);
                return "[Monitor MashUp en Vivo] Cuenta " + cuenta + " => Estado Sem\u00e1foro: "
                        + (estadoDurante != null ? estadoDurante.getDisponibilidadMashup() : "N/A")
                        + " (Ventanilla poseedora: " + (estadoDurante != null ? estadoDurante.getVentanillaBloqueo() : 0) + ")";
            });

            List<Future<String>> resultados = new ArrayList<>();
            for (Callable<String> t : tareas) {
                resultados.add(pool.submit(t));
            }
            System.out.println(ANSI_YELLOW + monitorMashup.get() + ANSI_RESET);
            for (Future<String> f : resultados) {
                String linea = f.get();
                if (linea.contains("\u00c9XITO")) {
                    System.out.println(ANSI_GREEN + linea + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + linea + ANSI_RESET);
                }
            }
            CuentaModel estadoFinal = controlador.buscarCuentaPorNumero(cuenta);
            if (estadoFinal != null) {
                System.out.printf(ANSI_GREEN + "[Post-Transacci\u00f3n] Cuenta %s restablecida a %s | Saldo consistente: $%.2f%n" + ANSI_RESET,
                        cuenta, estadoFinal.getDisponibilidadMashup(), estadoFinal.getSaldo());
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "Error en simulaci\u00f3n concurrente: " + e.getMessage() + ANSI_RESET);
        } finally {
            pool.shutdownNow();
        }
    }

    private void mostrarResultado(ResultadoOperacion r) {
        if (r.isExito()) {
            System.out.println(ANSI_GREEN + "\n[OK] " + r.getMensaje() + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "\n[ERROR] [" + r.getCodigo() + "] " + r.getMensaje() + ANSI_RESET);
        }
    }

    private String recortar(String txt, int max) {
        if (txt == null) return "";
        return txt.length() <= max ? txt : txt.substring(0, max - 2) + "..";
    }
}