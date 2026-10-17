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
            var cliente = new WSConversorUnidadesClient(
                WSConversorUnidadesClient.EndpointConfiguration.BasicHttpBinding_IWSConversorUnidades,
                EndpointUrl);

            var uri = new Uri(EndpointUrl);
            if (!uri.Host.Equals("localhost", StringComparison.OrdinalIgnoreCase))
            {
                cliente.Endpoint.EndpointBehaviors.Add(new IisExpressHostHeaderBehavior($"localhost:{uri.Port}"));
            }

            return cliente;
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

    internal class IisExpressHostHeaderBehavior : System.ServiceModel.Description.IEndpointBehavior, System.ServiceModel.Dispatcher.IClientMessageInspector
    {
        private readonly string hostHeader;

        public IisExpressHostHeaderBehavior(string hostHeader)
        {
            this.hostHeader = hostHeader;
        }

        public void AddBindingParameters(System.ServiceModel.Description.ServiceEndpoint endpoint, System.ServiceModel.Channels.BindingParameterCollection bindingParameters) { }
        
        public void ApplyClientBehavior(System.ServiceModel.Description.ServiceEndpoint endpoint, System.ServiceModel.Dispatcher.ClientRuntime clientRuntime)
        {
            clientRuntime.ClientMessageInspectors.Add(this);
        }

        public void ApplyDispatchBehavior(System.ServiceModel.Description.ServiceEndpoint endpoint, System.ServiceModel.Dispatcher.EndpointDispatcher endpointDispatcher) { }

        public void Validate(System.ServiceModel.Description.ServiceEndpoint endpoint) { }

        public void AfterReceiveReply(ref System.ServiceModel.Channels.Message reply, object correlationState) { }

        public object BeforeSendRequest(ref System.ServiceModel.Channels.Message request, System.ServiceModel.IClientChannel channel)
        {
            System.ServiceModel.Channels.HttpRequestMessageProperty httpRequestProperty;
            if (request.Properties.TryGetValue(System.ServiceModel.Channels.HttpRequestMessageProperty.Name, out object? obj) && obj is System.ServiceModel.Channels.HttpRequestMessageProperty prop)
            {
                httpRequestProperty = prop;
            }
            else
            {
                httpRequestProperty = new System.ServiceModel.Channels.HttpRequestMessageProperty();
                request.Properties.Add(System.ServiceModel.Channels.HttpRequestMessageProperty.Name, httpRequestProperty);
            }

            httpRequestProperty.Headers["Host"] = hostHeader;
            return null!;
        }
    }
}
