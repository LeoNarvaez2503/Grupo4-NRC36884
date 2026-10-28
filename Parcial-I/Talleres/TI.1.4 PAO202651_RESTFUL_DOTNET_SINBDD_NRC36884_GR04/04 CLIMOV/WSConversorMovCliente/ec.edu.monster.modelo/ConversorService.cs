using System;
using System.Globalization;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.Maui.Devices;

namespace ec.edu.monster.modelo
{
    public class ConversorService
    {
        // La IP del servidor se define en ConfiguracionServidor (y se puede cambiar desde la app).
        public static string EndpointUrl { get; set; } = ConfiguracionServidor.UrlServicio;

        public static string? TokenSesion { get; set; }

        private static readonly HttpClient httpClient = new HttpClient(new HostRewriteHandler(new HttpClientHandler()));

        public string? Login(string usuario, string contrasenia)
        {
            return LoginAsync(usuario, contrasenia).GetAwaiter().GetResult();
        }

        public async Task<string?> LoginAsync(string usuario, string contrasenia)
        {
            try
            {
                var payload = new { usuario, contrasenia };
                var json = JsonSerializer.Serialize(payload);
                using var request = new HttpRequestMessage(HttpMethod.Post, $"{EndpointUrl}/auth/login");
                request.Content = new StringContent(json, Encoding.UTF8, "application/json");

                AplicarHostHeader(request);

                var response = await httpClient.SendAsync(request);
                var responseBody = await response.Content.ReadAsStringAsync();

                using var doc = JsonDocument.Parse(responseBody);
                var root = doc.RootElement;

                if (root.TryGetProperty("exito", out var exitoProp) && exitoProp.GetBoolean())
                {
                    if (root.TryGetProperty("token", out var tokenProp))
                    {
                        TokenSesion = tokenProp.GetString();
                        return TokenSesion;
                    }
                }

                return null;
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException("Error en Login REST: " + ex.Message, ex);
            }
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirLongitudAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirLongitudAsync(double valor, string unidadInicial, string unidadFinal)
        {
            return await EjecutarConversionAsync("longitud", valor, unidadInicial, unidadFinal);
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirMasaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirMasaAsync(double valor, string unidadInicial, string unidadFinal)
        {
            return await EjecutarConversionAsync("masa", valor, unidadInicial, unidadFinal);
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirTemperaturaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirTemperaturaAsync(double valor, string unidadInicial, string unidadFinal)
        {
            return await EjecutarConversionAsync("temperatura", valor, unidadInicial, unidadFinal);
        }

        private async Task<double> EjecutarConversionAsync(string tipo, double valor, string unidadInicial, string unidadFinal)
        {
            try
            {
                string vStr = valor.ToString(CultureInfo.InvariantCulture);
                string url = $"{EndpointUrl}/conversor/{tipo}?valor={vStr}&unidadInicial={Uri.EscapeDataString(unidadInicial)}&unidadFinal={Uri.EscapeDataString(unidadFinal)}";

                using var request = new HttpRequestMessage(HttpMethod.Get, url);
                AplicarHostHeader(request);

                if (!string.IsNullOrEmpty(TokenSesion))
                {
                    request.Headers.Add("Authorization", "Bearer " + TokenSesion);
                }

                var response = await httpClient.SendAsync(request);
                var responseBody = await response.Content.ReadAsStringAsync();

                using var doc = JsonDocument.Parse(responseBody);
                var root = doc.RootElement;

                if (root.TryGetProperty("exito", out var exitoProp) && exitoProp.GetBoolean())
                {
                    if (root.TryGetProperty("valorFinal", out var valProp))
                    {
                        return valProp.GetDouble();
                    }
                }

                string msg = root.TryGetProperty("mensaje", out var msgProp) ? msgProp.GetString() ?? "Error en conversión" : "Error en conversión";
                throw new InvalidOperationException(msg);
            }
            catch (Exception ex)
            {
                throw new InvalidOperationException($"Error en conversión de {tipo} REST: " + ex.Message, ex);
            }
        }

        private static void AplicarHostHeader(HttpRequestMessage request)
        {
            if (request.RequestUri != null && !request.RequestUri.Host.Equals("localhost", StringComparison.OrdinalIgnoreCase))
            {
                request.Headers.Host = $"localhost:{request.RequestUri.Port}";
            }
        }
    }

    internal class HostRewriteHandler : DelegatingHandler
    {
        public HostRewriteHandler(HttpMessageHandler innerHandler) : base(innerHandler)
        {
        }

        protected override Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, System.Threading.CancellationToken cancellationToken)
        {
            if (request.RequestUri != null && !request.RequestUri.Host.Equals("localhost", StringComparison.OrdinalIgnoreCase))
            {
                request.Headers.Host = $"localhost:{request.RequestUri.Port}";
            }
            return base.SendAsync(request, cancellationToken);
        }
    }
}
