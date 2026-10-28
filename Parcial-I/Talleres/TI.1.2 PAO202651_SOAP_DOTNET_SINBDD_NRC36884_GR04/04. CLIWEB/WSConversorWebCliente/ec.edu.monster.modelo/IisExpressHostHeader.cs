using System;

namespace ec.edu.monster.modelo
{
    // IIS Express solo acepta el Host 'localhost:puerto'; este behavior lo reescribe cuando se usa una IP.
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
