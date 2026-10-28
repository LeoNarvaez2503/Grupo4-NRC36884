using System;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Maui.Devices;
using ServiceReference1;

namespace ec.edu.monster.modelo
{
    public class ConversorService
    {
        // La IP del servidor se define en ConfiguracionServidor (y se puede cambiar desde la app).
        public static string EndpointUrl { get; set; } = ConfiguracionServidor.UrlServicio;

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
        // Pide el WSDL con y sin el Host reescrito para saber quien responde y con que codigo.
        public static async Task<string> DiagnosticarAsync()
        {
            var uri = new Uri(EndpointUrl);
            var sb = new System.Text.StringBuilder();
            foreach (var reescribir in new[] { true, false })
            {
                try
                {
                    HttpMessageHandler handler = new HttpClientHandler();
                    if (reescribir)
                    {
                        handler = new HostRewriteHandler(handler, $"localhost:{uri.Port}");
                    }
                    using var http = new System.Net.Http.HttpClient(handler) { Timeout = TimeSpan.FromSeconds(8) };
                    using var resp = await http.GetAsync(EndpointUrl + "?wsdl");
                    var cuerpo = await resp.Content.ReadAsStringAsync();
                    var servidor = string.Join(",", System.Linq.Enumerable.Select(resp.Headers.Server, x => x.ToString()));
                    cuerpo = cuerpo.Length > 100 ? cuerpo.Substring(0, 100) : cuerpo;
                    sb.AppendLine($"Host {(reescribir ? "reescrito" : "original")}: HTTP {(int)resp.StatusCode}, Server={servidor}, cuerpo={cuerpo.Replace('\n', ' ')}");
                }
                catch (Exception ex)
                {
                    sb.AppendLine($"Host {(reescribir ? "reescrito" : "original")}: fallo {ex.GetType().Name}: {ex.Message}");
                }
            }
            return sb.ToString();
        }

    }

    internal class IisExpressHostHeaderBehavior : System.ServiceModel.Description.IEndpointBehavior, System.ServiceModel.Dispatcher.IClientMessageInspector
    {
        private readonly string hostHeader;

        public IisExpressHostHeaderBehavior(string hostHeader)
        {
            this.hostHeader = hostHeader;
        }

        public void AddBindingParameters(System.ServiceModel.Description.ServiceEndpoint endpoint, System.ServiceModel.Channels.BindingParameterCollection bindingParameters)
        {
            bindingParameters.Add(new Func<System.Net.Http.HttpClientHandler, System.Net.Http.HttpMessageHandler>(
                handler => new HostRewriteHandler(handler, hostHeader)));
        }
        
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

    internal class HostRewriteHandler : System.Net.Http.DelegatingHandler
    {
        private readonly string hostHeader;

        public HostRewriteHandler(System.Net.Http.HttpMessageHandler innerHandler, string hostHeader)
            : base(innerHandler)
        {
            this.hostHeader = hostHeader;
        }

        protected override System.Threading.Tasks.Task<System.Net.Http.HttpResponseMessage> SendAsync(
            System.Net.Http.HttpRequestMessage request,
            System.Threading.CancellationToken cancellationToken)
        {
            request.Headers.Host = hostHeader;
            return base.SendAsync(request, cancellationToken);
        }
    }
}
