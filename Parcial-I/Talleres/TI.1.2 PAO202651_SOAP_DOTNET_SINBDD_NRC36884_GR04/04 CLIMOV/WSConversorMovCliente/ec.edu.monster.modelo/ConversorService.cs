using System;
using System.Threading.Tasks;
using Microsoft.Maui.Devices;
using ServiceReference1;

namespace ec.edu.monster.modelo
{
    public class ConversorService
    {
        public static string EndpointUrl { get; set; } = DeviceInfo.Platform == DevicePlatform.Android
            ? "http://10.0.2.2:55981/ServicioWeb.svc"
            : "http://localhost:55981/ServicioWeb.svc";

        private WSConversorUnidadesClient CrearCliente()
        {
            return new WSConversorUnidadesClient(
                WSConversorUnidadesClient.EndpointConfiguration.BasicHttpBinding_IWSConversorUnidades,
                EndpointUrl);
        }

        public async Task<string> LoginAsync(string usuario, string contrasenia)
        {
            using var cliente = CrearCliente();
            return await cliente.loginAsync(usuario, contrasenia);
        }

        public string Login(string usuario, string contrasenia)
        {
            return LoginAsync(usuario, contrasenia).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirLongitudAsync(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return await cliente.ConvertirLongitudAsync(valor, unidadInicial, unidadFinal);
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirLongitudAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirMasaAsync(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return await cliente.ConvertirMasaAsync(valor, unidadInicial, unidadFinal);
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirMasaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public async Task<double> ConvertirTemperaturaAsync(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return await cliente.ConvertirTemperaturaAsync(valor, unidadInicial, unidadFinal);
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirTemperaturaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }
    }
}
