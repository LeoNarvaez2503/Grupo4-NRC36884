using System.Net.Http.Json;
using System.Text;
using System.Text.Json;

namespace ec.edu.monster.model
{
    public class UsuarioModel
    {
        public bool Autenticado { get; set; }
        public int IdEmpleado { get; set; }
        public string Usuario { get; set; } = string.Empty;
        public string NombreCompleto { get; set; } = string.Empty;
        public string Rol { get; set; } = string.Empty;
        public int IdSucursal { get; set; }
        public int Ventanilla { get; set; }
        public string Token { get; set; } = string.Empty;
        public string Mensaje { get; set; } = string.Empty;
    }

    public class SucursalModel
    {
        public int IdSucursal { get; set; }
        public string Codigo { get; set; } = string.Empty;
        public string Nombre { get; set; } = string.Empty;
        public string Ciudad { get; set; } = string.Empty;
        public string Direccion { get; set; } = string.Empty;
    }

    public class CuentaModel
    {
        public string NumeroCuenta { get; set; } = string.Empty;
        public string Moneda { get; set; } = "USD";
        public double Saldo { get; set; }
        public string EstadoCuenta { get; set; } = "Activa";
        public int IdCliente { get; set; }
        public string DniCliente { get; set; } = string.Empty;
        public string NombreCliente { get; set; } = string.Empty;
        public string ApellidoCliente { get; set; } = string.Empty;
        public string TelefonoCliente { get; set; } = string.Empty;
        public string EmailCliente { get; set; } = string.Empty;
        public int IdSucursal { get; set; }
        public string NombreSucursal { get; set; } = string.Empty;
        public string EstadoMashup { get; set; } = "VERDE";
        public int VentanillaOcupada { get; set; }
        public string BloqueadoPor { get; set; } = string.Empty;

        public string TitularCompleto => $"{NombreCliente} {ApellidoCliente}".Trim();
        public string EtiquetaSemaforo => EstadoMashup == "ROJO"
            ? $"ROJO (Vent. {VentanillaOcupada} - {BloqueadoPor})"
            : "VERDE (Libre)";
    }

    public class MovimientoModel
    {
        public int IdMovimiento { get; set; }
        public string NumeroOperacion { get; set; } = string.Empty;
        public string NumeroCuenta { get; set; } = string.Empty;
        public int IdEmpleado { get; set; }
        public string NombreEmpleado { get; set; } = string.Empty;
        public int Ventanilla { get; set; }
        public string Tipo { get; set; } = string.Empty;
        public double Monto { get; set; }
        public double SaldoResultante { get; set; }
        public string CuentaReferencia { get; set; } = string.Empty;
        public string Descripcion { get; set; } = string.Empty;
        public string FechaHora { get; set; } = string.Empty;
    }

    public class OperacionModel
    {
        public bool Exito { get; set; }
        public string Codigo { get; set; } = string.Empty;
        public string Mensaje { get; set; } = string.Empty;
        public string NumeroOperacion { get; set; } = string.Empty;
        public string NumeroCuenta { get; set; } = string.Empty;
        public double SaldoAnterior { get; set; }
        public double MontoOperacion { get; set; }
        public double SaldoActual { get; set; }
        public string EstadoMashup { get; set; } = "VERDE";
        public int Ventanilla { get; set; }
        public string FechaHora { get; set; } = string.Empty;
    }

    /// <summary>
    /// Capa Modelo (MVC Tradicional) de comunicación HTTP/JSON con el Servidor RESTful .NET.
    /// Cliente delgado: cero lógica de negocio ni acceso a BDD en el cliente.
    /// </summary>
    public class EurekaBankRestModel
    {
        private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(30) };
        private static readonly JsonSerializerOptions JsonOpts = new() { PropertyNameCaseInsensitive = true };

        public string EndpointUrl { get; set; } = "http://localhost:5140/api";

        private string BaseUrl => EndpointUrl.TrimEnd('/');

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                using var content = CrearJson(new { usuario, clave });
                using var resp = Http.PostAsync($"{BaseUrl}/auth/login", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<UsuarioModel>(raw, JsonOpts)
                    ?? new UsuarioModel { Autenticado = false, Mensaje = "Respuesta inválida del servidor REST." };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error conectando al servidor REST: " + ex.Message };
            }
        }

        public List<SucursalModel> ListarSucursales() =>
            GetJson($"{BaseUrl}/sucursales", new List<SucursalModel>());

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId) =>
            GetJson($"{BaseUrl}/cuentas?sucursal={sucursalId}", new List<CuentaModel>());

        public CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta) =>
            GetJson<CuentaModel?>($"{BaseUrl}/cuentas/{Uri.EscapeDataString(numeroCuenta)}", null);

        public List<CuentaModel> ConsultarCuentasPorCliente(string dni) =>
            GetJson($"{BaseUrl}/clientes/{Uri.EscapeDataString(dni)}/cuentas", new List<CuentaModel>());

        public List<MovimientoModel> ConsultarExtracto(string numeroCuenta) =>
            GetJson($"{BaseUrl}/cuentas/{Uri.EscapeDataString(numeroCuenta)}/movimientos", new List<MovimientoModel>());

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta) =>
            GetJson($"{BaseUrl}/cuentas/{Uri.EscapeDataString(numeroCuenta)}/estado",
                new OperacionModel { Exito = false, Codigo = "ERROR_RED", Mensaje = "Sin respuesta" });

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/deposito", new { numeroCuenta, monto, idEmpleado, ventanilla });

        public OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/retiro", new { numeroCuenta, monto, idEmpleado, ventanilla });

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/transferencia", new { cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla });

        public OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario) =>
            PostOperacion($"{BaseUrl}/cuentas/{Uri.EscapeDataString(numeroCuenta)}/bloquear", new { ventanilla, usuario });

        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin) =>
            PostOperacion($"{BaseUrl}/cuentas/{Uri.EscapeDataString(numeroCuenta)}/desbloquear", new { ventanilla = 1, usuario = usuarioAdmin });

        private static StringContent CrearJson(object payload) =>
            new(JsonSerializer.Serialize(payload), Encoding.UTF8, "application/json");

        private static T GetJson<T>(string url, T fallback)
        {
            try
            {
                using var resp = Http.GetAsync(url).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<T>(raw, JsonOpts) ?? fallback;
            }
            catch
            {
                return fallback;
            }
        }

        private static OperacionModel PostOperacion(string url, object payload)
        {
            try
            {
                using var content = CrearJson(payload);
                using var resp = Http.PostAsync(url, content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<OperacionModel>(raw, JsonOpts)
                    ?? new OperacionModel { Exito = false, Codigo = $"HTTP_{(int)resp.StatusCode}", Mensaje = raw };
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Codigo = "ERROR_RED", Mensaje = "Fallo REST: " + ex.Message };
            }
        }
    }
}
