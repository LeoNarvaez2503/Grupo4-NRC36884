using System;
using ServiceReference1;

namespace ec.edu.monster.modelo
{
    public class ConversorService
    {
        private static WSConversorUnidadesClient CrearCliente()
        {
            var cliente = new WSConversorUnidadesClient(
                WSConversorUnidadesClient.EndpointConfiguration.BasicHttpBinding_IWSConversorUnidades,
                ConfiguracionServidor.UrlServicio);

            if (!ConfiguracionServidor.HostActual.Equals("localhost", StringComparison.OrdinalIgnoreCase))
            {
                cliente.Endpoint.EndpointBehaviors.Add(new IisExpressHostHeaderBehavior($"localhost:{ConfiguracionServidor.Puerto}"));
            }

            return cliente;
        }

        public string Login(string usuario, string contrasenia)
        {
            using var cliente = CrearCliente();
            return cliente.loginAsync(usuario, contrasenia).GetAwaiter().GetResult();
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return cliente.ConvertirLongitudAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return cliente.ConvertirMasaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = CrearCliente();
            return cliente.ConvertirTemperaturaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }
    }
}
