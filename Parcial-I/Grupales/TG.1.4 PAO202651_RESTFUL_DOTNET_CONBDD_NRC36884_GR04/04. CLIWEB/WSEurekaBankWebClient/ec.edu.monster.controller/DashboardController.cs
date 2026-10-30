using System.Globalization;
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class DashboardController : Controller
    {
        private readonly AppControlador _controlador = new();

        [HttpGet]
        public IActionResult Index(int sucursalId = 0, string filtro = "", string cuenta = "001-0001")
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return RedirectToAction("Index", "Login");

            var cuentas = string.IsNullOrWhiteSpace(filtro)
                ? _controlador.ObtenerCuentasPorSucursal(sucursalId)
                : _controlador.BuscarCuentasPorCliente(filtro);

            string ctaSel = !string.IsNullOrWhiteSpace(cuenta)
                ? cuenta
                : (cuentas.FirstOrDefault()?.NumeroCuenta ?? "001-0001");

            var vm = new DashboardWebModel
            {
                Sesion = sesion,
                VentanillaActiva = sesion.Ventanilla is >= 1 and <= 4 ? sesion.Ventanilla : 1,
                SucursalSeleccionada = sucursalId,
                Filtro = filtro,
                CuentaSeleccionada = ctaSel,
                Sucursales = _controlador.ObtenerSucursales(),
                Cuentas = cuentas,
                Extracto = _controlador.ObtenerExtracto(ctaSel)
            };

            return View("~/ec.edu.monster.view/Dashboard/Index.cshtml", vm);
        }

        /// <summary>
        /// Endpoint MVC sin bloqueo de sesión para actualización MashUp en vivo (VERDE / ROJO).
        /// </summary>
        [HttpGet]
        public IActionResult EstadoMashup(int sucursalId = 0, string filtro = "", string cuentaExtracto = "001-0001")
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return Unauthorized();

            var cuentas = string.IsNullOrWhiteSpace(filtro)
                ? _controlador.ObtenerCuentasPorSucursal(sucursalId)
                : _controlador.BuscarCuentasPorCliente(filtro);

            var extracto = string.IsNullOrWhiteSpace(cuentaExtracto)
                ? new List<MovimientoModel>()
                : _controlador.ObtenerExtracto(cuentaExtracto);

            return Json(new
            {
                verdes = cuentas.Count(c => c.EstadoMashup != "ROJO"),
                rojas = cuentas.Count(c => c.EstadoMashup == "ROJO"),
                cuentas = cuentas.Select(c => new
                {
                    numeroCuenta = c.NumeroCuenta,
                    titular = c.TitularCompleto,
                    dni = c.DniCliente,
                    sucursal = c.NombreSucursal,
                    saldo = c.Saldo.ToString("F2", CultureInfo.InvariantCulture),
                    estadoMashup = c.EstadoMashup,
                    etiqueta = c.EstadoMashup == "ROJO"
                        ? $"ROJO (Vent. {c.VentanillaOcupada} - {c.BloqueadoPor})"
                        : "VERDE (Libre)"
                }),
                extracto = extracto.Take(15).Select(m => new
                {
                    fechaHora = m.FechaHora,
                    numeroOperacion = m.NumeroOperacion,
                    tipo = m.Tipo,
                    ventanilla = m.Ventanilla,
                    monto = m.Monto.ToString("F2", CultureInfo.InvariantCulture),
                    saldoResultante = m.SaldoResultante.ToString("F2", CultureInfo.InvariantCulture),
                    descripcion = m.Descripcion
                })
            });
        }

        [HttpPost]
        public async Task<IActionResult> Operar(
            [FromForm] string tipo,
            [FromForm] string cuenta,
            [FromForm] string? destino,
            [FromForm] string? monto,
            [FromForm] int ventanilla = 1)
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return Unauthorized();

            int vent = ventanilla is >= 1 and <= 4 ? ventanilla : sesion.Ventanilla;
            int idEmp = sesion.IdEmpleado > 0 ? sesion.IdEmpleado : 1;
            string cta = (cuenta ?? string.Empty).Trim();

            double montoVal = 0;
            if (!string.IsNullOrWhiteSpace(monto))
            {
                double.TryParse(monto.Trim().Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out montoVal);
            }

            string op = (tipo ?? string.Empty).Trim().ToUpperInvariant();
            if (op == "SIMULAR3")
            {
                var t1 = Task.Run(() => _controlador.Depositar(cta, 25.00, idEmp, 1));
                await Task.Delay(250);
                var t2 = Task.Run(() => _controlador.Depositar(cta, 15.00, idEmp, 2));
                var t3 = Task.Run(() => _controlador.Depositar(cta, 10.00, idEmp, 3));
                await Task.WhenAll(t1, t2, t3);

                return Json(new
                {
                    exito = true,
                    codigo = "SIMULACION_COMPLETADA",
                    mensaje = $"[V1]: {t1.Result.Mensaje} | [V2]: {t2.Result.Mensaje} | [V3]: {t3.Result.Mensaje}"
                });
            }

            OperacionModel res = op switch
            {
                "DEPOSITO" => await Task.Run(() => _controlador.Depositar(cta, montoVal, idEmp, vent)),
                "RETIRO" => await Task.Run(() => _controlador.Retirar(cta, montoVal, idEmp, vent)),
                "TRANSFERENCIA" => await Task.Run(() => _controlador.Transferir(cta, destino ?? "", montoVal, idEmp, vent)),
                "BLOQUEAR" => await Task.Run(() => _controlador.BloquearManual(cta, vent, sesion.Usuario)),
                "DESBLOQUEAR" => await Task.Run(() => _controlador.Desbloquear(cta, sesion.Usuario)),
                _ => new OperacionModel { Exito = false, Codigo = "OP_INVALIDA", Mensaje = "Operación no reconocida." }
            };

            return Json(new
            {
                exito = res.Exito,
                codigo = res.Codigo,
                mensaje = res.Mensaje,
                estadoMashup = res.EstadoMashup
            });
        }
    }
}
