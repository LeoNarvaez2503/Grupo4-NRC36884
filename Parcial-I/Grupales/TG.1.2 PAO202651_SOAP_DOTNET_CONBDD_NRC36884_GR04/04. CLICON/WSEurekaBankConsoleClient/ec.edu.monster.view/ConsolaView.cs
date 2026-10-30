using System.Globalization;
using ec.edu.monster.controller;
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    /// <summary>
    /// Capa Vista (MVC Tradicional) para el Cliente de Consola SOAP .NET.
    /// </summary>
    public class ConsolaView
    {
        private readonly AppControlador _controlador = new();

        public void Iniciar(string[] args)
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            if (args.Any(a => string.Equals(a, "--demo", StringComparison.OrdinalIgnoreCase)))
            {
                EjecutarDemostracionAutomatica();
                return;
            }

            MostrarBanner();
            while (_controlador.SesionActual == null)
            {
                Console.Write("\nUsuario (ej. Monster): ");
                string usr = Console.ReadLine()?.Trim() ?? "";
                Console.Write("Contraseña: ");
                string pwd = Console.ReadLine()?.Trim() ?? "";

                var login = _controlador.IniciarSesion(usr, pwd);
                if (login.Autenticado)
                {
                    Console.ForegroundColor = ConsoleColor.Green;
                    Console.WriteLine($"[OK] Bienvenido {login.NombreCompleto} | Rol: {login.Rol} | Ventanilla: {_controlador.VentanillaActiva}");
                    Console.ResetColor();
                }
                else
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine($"[ERROR] {login.Mensaje}");
                    Console.ResetColor();
                }
            }

            bool salir = false;
            while (!salir)
            {
                Console.WriteLine("\n=====================================================================");
                Console.WriteLine($" EUREKABANK CONSOLA SOAP .NET | Ventanilla {_controlador.VentanillaActiva} | Operador: {_controlador.SesionActual.Usuario}");
                Console.WriteLine("=====================================================================");
                Console.WriteLine(" 1. Monitorear Semáforo MashUp de Cuentas (VERDE / ROJO)");
                Console.WriteLine(" 2. Consultar Cuenta por Número o DNI");
                Console.WriteLine(" 3. Consultar Extracto Bancario");
                Console.WriteLine(" 4. Realizar Depósito");
                Console.WriteLine(" 5. Realizar Retiro");
                Console.WriteLine(" 6. Realizar Transferencia entre Cuentas");
                Console.WriteLine(" 7. Simular Concurrencia Crítica (3 Ventanillas Simultáneas)");
                Console.WriteLine(" 8. Cambiar Ventanilla Activa (1..4)");
                Console.WriteLine(" 9. Desbloquear Cuenta (Superadmin Monster / Gerencia)");
                Console.WriteLine(" 0. Salir");
                Console.Write("Seleccione una opción: ");

                switch (Console.ReadLine()?.Trim())
                {
                    case "1": ImprimirCuentas(_controlador.ObtenerCuentasPorSucursal(0)); break;
                    case "2":
                        Console.Write("Ingrese Número de Cuenta o DNI: ");
                        ImprimirCuentas(_controlador.BuscarCuentasPorCliente(Console.ReadLine() ?? ""));
                        break;
                    case "3":
                        Console.Write("Número de Cuenta (ej. 001-0001): ");
                        ImprimirExtracto(_controlador.ObtenerExtracto(Console.ReadLine() ?? ""));
                        break;
                    case "4":
                        Console.Write("Cuenta destino: ");
                        string ctaDep = Console.ReadLine() ?? "";
                        Console.Write("Monto a depositar ($): ");
                        if (double.TryParse(Console.ReadLine()?.Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out double mDep))
                            ImprimirResultado(_controlador.Depositar(ctaDep, mDep));
                        break;
                    case "5":
                        Console.Write("Cuenta a debitar: ");
                        string ctaRet = Console.ReadLine() ?? "";
                        Console.Write("Monto a retirar ($): ");
                        if (double.TryParse(Console.ReadLine()?.Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out double mRet))
                            ImprimirResultado(_controlador.Retirar(ctaRet, mRet));
                        break;
                    case "6":
                        Console.Write("Cuenta origen: ");
                        string orig = Console.ReadLine() ?? "";
                        Console.Write("Cuenta destino: ");
                        string dest = Console.ReadLine() ?? "";
                        Console.Write("Monto a transferir ($): ");
                        if (double.TryParse(Console.ReadLine()?.Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out double mTrf))
                            ImprimirResultado(_controlador.Transferir(orig, dest, mTrf));
                        break;
                    case "7": SimularTresVentanillas("001-0001"); break;
                    case "8":
                        Console.Write("Nueva Ventanilla (1-4): ");
                        if (int.TryParse(Console.ReadLine(), out int v) && v >= 1 && v <= 4)
                            _controlador.VentanillaActiva = v;
                        break;
                    case "9":
                        Console.Write("Cuenta a desbloquear: ");
                        ImprimirResultado(_controlador.DesbloquearCuenta(Console.ReadLine() ?? ""));
                        break;
                    case "0": salir = true; break;
                }
            }
        }

        private void EjecutarDemostracionAutomatica()
        {
            MostrarBanner();
            Console.WriteLine("[DEMO] 1. Autenticando con credenciales maestras Monster / Monster9...");
            var login = _controlador.IniciarSesion("Monster", "Monster9");
            Console.WriteLine($"[DEMO] Resultado Login: Autenticado={login.Autenticado} | Usuario={login.NombreCompleto} | Rol={login.Rol}");
            if (!login.Autenticado) Environment.Exit(1);

            Console.WriteLine("\n[DEMO] 2. Estado inicial de cuentas y Semáforo MashUp:");
            ImprimirCuentas(_controlador.ObtenerCuentasPorSucursal(0));

            Console.WriteLine("\n[DEMO] 3. Prueba Crítica de Concurrencia (3 Ventanillas Simultáneas sobre 001-0001):");
            SimularTresVentanillas("001-0001");

            Console.WriteLine("\n[DEMO] 4. Estado final de cuentas tras liberar semáforo (VERDE):");
            ImprimirCuentas(_controlador.ObtenerCuentasPorSucursal(0));
        }

        private void SimularTresVentanillas(string cuenta)
        {
            var t1 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 50.00, 1));
            Thread.Sleep(350);
            var estadoDurante = _controlador.ConsultarEstadoMashUp(cuenta);
            Console.WriteLine($"  -> [MONITOR MASHUP EN VIVO] Cuenta {cuenta}: Estado={estadoDurante.EstadoMashup} | {estadoDurante.Mensaje}");

            var t2 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 30.00, 2));
            var t3 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 20.00, 3));

            Task.WaitAll(t1, t2, t3);
            Console.WriteLine("  -> [Ventanilla 1]: "); ImprimirResultado(t1.Result);
            Console.WriteLine("  -> [Ventanilla 2]: "); ImprimirResultado(t2.Result);
            Console.WriteLine("  -> [Ventanilla 3]: "); ImprimirResultado(t3.Result);
        }

        private static void MostrarBanner()
        {
            Console.WriteLine("=====================================================================");
            Console.WriteLine("  EUREKABANK - CLIENTE CONSOLA SOAP .NET (MVC: ec.edu.monster) GR04  ");
            Console.WriteLine("=====================================================================");
        }

        private static void ImprimirCuentas(List<CuentaModel> cuentas)
        {
            Console.WriteLine($"{"CUENTA",-10} | {"TITULAR",-24} | {"SALDO",-11} | {"SEMÁFORO MASHUP",-28}");
            Console.WriteLine(new string('-', 82));
            foreach (var c in cuentas)
            {
                Console.WriteLine($"{c.NumeroCuenta,-10} | {c.TitularCompleto,-24} | ${c.Saldo,9:F2} | {c.EtiquetaSemaforo,-28}");
            }
        }

        private static void ImprimirExtracto(List<MovimientoModel> movs)
        {
            foreach (var m in movs.Take(10))
            {
                Console.WriteLine($"{m.FechaHora} | {m.NumeroOperacion,-18} | {m.Tipo,-20} | ${m.Monto,8:F2} | Saldo: ${m.SaldoResultante,9:F2}");
            }
        }

        private static void ImprimirResultado(OperacionModel r)
        {
            Console.ForegroundColor = r.Exito ? ConsoleColor.Green : ConsoleColor.Red;
            Console.WriteLine($"     [{(r.Exito ? "EXITO" : r.Codigo)}] {r.Mensaje}");
            Console.ResetColor();
        }
    }
}
