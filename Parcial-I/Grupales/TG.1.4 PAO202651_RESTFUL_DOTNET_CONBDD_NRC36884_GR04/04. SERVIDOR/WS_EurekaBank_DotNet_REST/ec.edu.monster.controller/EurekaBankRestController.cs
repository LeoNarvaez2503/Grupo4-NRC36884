using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    /// <summary>
    /// Capa Controlador RESTful (MVC Tradicional) de EurekaBank en ASP.NET Core Web API.
    /// Retorna códigos HTTP semánticos (200 OK, 400 Bad Request, 401 Unauthorized, 404 Not Found, 409 Conflict).
    /// </summary>
    [ApiController]
    [Route("api")]
    public class EurekaBankRestController : ControllerBase
    {
        private readonly EurekaBankBddModel _modelo = new();

        [HttpPost("auth/login")]
        public IActionResult Login([FromBody] LoginRestRequest req)
        {
            var res = _modelo.AutenticarUsuario(req.Usuario, req.Clave);
            if (!res.Autenticado) return Unauthorized(res);
            return Ok(res);
        }

        [HttpGet("sucursales")]
        public IActionResult ListarSucursales() =>
            Ok(_modelo.ListarSucursales());

        [HttpGet("cuentas")]
        public IActionResult ListarCuentas([FromQuery] int sucursal = 0) =>
            Ok(_modelo.ListarCuentasPorSucursal(sucursal));

        [HttpGet("cuentas/{numeroCuenta}")]
        public IActionResult ObtenerCuenta(string numeroCuenta)
        {
            var cta = _modelo.ObtenerCuentaPorNumero(numeroCuenta);
            if (cta == null) return NotFound(OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {numeroCuenta} no existe."));
            return Ok(cta);
        }

        [HttpGet("clientes/{dni}/cuentas")]
        public IActionResult ConsultarPorCliente(string dni) =>
            Ok(_modelo.ConsultarCuentasPorCliente(dni));

        [HttpGet("cuentas/{numeroCuenta}/movimientos")]
        public IActionResult ConsultarExtracto(string numeroCuenta) =>
            Ok(_modelo.ConsultarExtracto(numeroCuenta));

        [HttpGet("cuentas/{numeroCuenta}/estado")]
        public IActionResult ConsultarEstadoMashup(string numeroCuenta)
        {
            var res = _modelo.ConsultarEstadoMashUp(numeroCuenta);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/deposito")]
        public IActionResult Depositar([FromBody] TransaccionRestRequest req)
        {
            var res = _modelo.Depositar(req.NumeroCuenta, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/retiro")]
        public IActionResult Retirar([FromBody] TransaccionRestRequest req)
        {
            var res = _modelo.Retirar(req.NumeroCuenta, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/transferencia")]
        public IActionResult Transferir([FromBody] TransferenciaRestRequest req)
        {
            var res = _modelo.Transferir(req.CuentaOrigen, req.CuentaDestino, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("cuentas/{numeroCuenta}/bloquear")]
        public IActionResult BloquearManual(string numeroCuenta, [FromBody] BloqueoRestRequest req)
        {
            var res = _modelo.BloquearCuentaManual(numeroCuenta, req.Ventanilla, req.Usuario);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("cuentas/{numeroCuenta}/desbloquear")]
        public IActionResult Desbloquear(string numeroCuenta, [FromBody] BloqueoRestRequest req)
        {
            var res = _modelo.DesbloquearCuenta(numeroCuenta, req.Usuario);
            return ResolverCodigoHttp(res);
        }

        private IActionResult ResolverCodigoHttp(OperacionModel res)
        {
            if (res.Exito) return Ok(res);
            return res.Codigo switch
            {
                "CONCURRENCIA_BLOQUEADA" => Conflict(res),
                "CUENTA_NO_EXISTE" => NotFound(res),
                "NO_AUTORIZADO" => StatusCode(403, res),
                _ => BadRequest(res)
            };
        }
    }
}
