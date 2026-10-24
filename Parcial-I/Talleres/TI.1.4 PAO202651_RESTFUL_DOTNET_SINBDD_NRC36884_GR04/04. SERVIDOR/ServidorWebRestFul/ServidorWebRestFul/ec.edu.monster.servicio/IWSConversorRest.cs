using System.ServiceModel;
using System.ServiceModel.Web;
using ec.edu.monster.modelo.dto;

namespace ec.edu.monster.servicio
{
    /// <summary>
    /// Contrato de servicio RESTful para el servidor de conversión de unidades y autenticación.
    /// Soporta métodos HTTP GET y POST con formato JSON, compatible con clientes Web, Escritorio, Consola y Móvil.
    /// </summary>
    [ServiceContract(Name = "IWSConversorRest")]
    public interface IWSConversorRest
    {
        // =========================================================================
        // PREFLIGHT CORS (Para clientes Web en navegador)
        // =========================================================================
        [OperationContract]
        [WebInvoke(Method = "OPTIONS", UriTemplate = "*")]
        void HandleOptions();

        // =========================================================================
        // AUTENTICACIÓN (Rutas RESTful estándar /auth/...)
        // =========================================================================

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "auth/login",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        LoginResponse LoginRestPost(LoginRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "auth/login?usuario={usuario}&contrasenia={contrasenia}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        LoginResponse LoginRestGet(string usuario, string contrasenia);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "auth/cambiar-contrasenia",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        RespuestaGeneral CambiarContraseniaRestPost(CambioContraseniaRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "auth/validar-token?token={token}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        RespuestaGeneral ValidarTokenRestGet(string token);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "auth/logout?token={token}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        RespuestaGeneral LogoutRestPost(string token);

        // =========================================================================
        // CONVERSIÓN DE UNIDADES (Rutas RESTful estándar /conversor/...)
        // =========================================================================

        [OperationContract]
        [WebGet(UriTemplate = "conversor/longitud?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirLongitudRestGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "conversor/longitud",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirLongitudRestPost(ConversionRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "conversor/masa?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirMasaRestGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "conversor/masa",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirMasaRestPost(ConversionRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "conversor/temperatura?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirTemperaturaRestGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "conversor/temperatura",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirTemperaturaRestPost(ConversionRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "conversor/unidades",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        CatalogoUnidadesResponse ObtenerCatalogoUnidades();

        // =========================================================================
        // COMPATIBILIDAD DIRECTA (Nomenclatura directa previa)
        // =========================================================================

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "login",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        LoginResponse LoginDirectoPost(LoginRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "login?usuario={usuario}&contrasenia={contrasenia}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        LoginResponse LoginDirectoGet(string usuario, string contrasenia);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "cambiarContrasenia",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        RespuestaGeneral CambiarContraseniaDirectoPost(CambioContraseniaRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "convertirLongitud?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirLongitudDirectoGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "convertirLongitud",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirLongitudDirectoPost(ConversionRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "convertirMasa?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirMasaDirectoGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "convertirMasa",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirMasaDirectoPost(ConversionRequest request);

        [OperationContract]
        [WebGet(UriTemplate = "convertirTemperatura?valor={valor}&unidadInicial={unidadInicial}&unidadFinal={unidadFinal}",
            ResponseFormat = WebMessageFormat.Json, BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirTemperaturaDirectoGet(string valor, string unidadInicial, string unidadFinal);

        [OperationContract]
        [WebInvoke(Method = "POST", UriTemplate = "convertirTemperatura",
            RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json,
            BodyStyle = WebMessageBodyStyle.Bare)]
        ConversionResponse ConvertirTemperaturaDirectoPost(ConversionRequest request);
    }
}
