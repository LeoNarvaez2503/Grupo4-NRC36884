using System.Text.Json;
using Microsoft.AspNetCore.Http;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class AppControlador
    {
        public static string EndpointGlobal { get; set; } = "http://localhost:5140/api";
        private readonly EurekaBankRestModel _modelo = new() { EndpointUrl = EndpointGlobal };
        private const string CookieSesion = "EurekaBank_Sesion_REST";

        public UsuarioModel IniciarSesion(string usuario, string clave) =>
            _modelo.Autenticar(usuario, clave);

        public List<SucursalModel> ObtenerSucursales() => _modelo.ListarSucursales();
        public List<CuentaModel> ObtenerCuentasPorSucursal(int sucursalId) => _modelo.ListarCuentasPorSucursal(sucursalId);
        public List<CuentaModel> BuscarCuentasPorCliente(string dni) => _modelo.ConsultarCuentasPorCliente(dni);
        public List<MovimientoModel> ObtenerExtracto(string numeroCuenta) => _modelo.ConsultarExtracto(numeroCuenta);

        public OperacionModel Depositar(string cta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Depositar(cta, monto, idEmpleado, ventanilla);

        public OperacionModel Retirar(string cta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Retirar(cta, monto, idEmpleado, ventanilla);

        public OperacionModel Transferir(string origen, string destino, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Transferir(origen, destino, monto, idEmpleado, ventanilla);

        public OperacionModel BloquearManual(string cta, int ventanilla, string usuario) =>
            _modelo.BloquearCuentaManual(cta, ventanilla, usuario);

        public OperacionModel Desbloquear(string cta, string usuario) =>
            _modelo.DesbloquearCuenta(cta, usuario);

        public static void GuardarSesionCookie(HttpResponse response, UsuarioModel sesion, int ventanilla)
        {
            sesion.Ventanilla = ventanilla;
            string json = JsonSerializer.Serialize(sesion);
            response.Cookies.Append(CookieSesion, json, new CookieOptions
            {
                HttpOnly = true,
                IsEssential = true,
                SameSite = SameSiteMode.Lax,
                Expires = DateTimeOffset.UtcNow.AddHours(8)
            });
        }

        public static UsuarioModel? LeerSesionCookie(HttpRequest request)
        {
            if (!request.Cookies.TryGetValue(CookieSesion, out string? json) || string.IsNullOrEmpty(json))
                return null;
            try
            {
                var u = JsonSerializer.Deserialize<UsuarioModel>(json);
                return u != null && u.Autenticado ? u : null;
            }
            catch
            {
                return null;
            }
        }

        public static void CerrarSesionCookie(HttpResponse response) =>
            response.Cookies.Delete(CookieSesion);
    }
}

