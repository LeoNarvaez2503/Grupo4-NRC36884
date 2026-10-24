using System;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using ec.edu.monster.controlador;
using ec.edu.monster.modelo.dto;
using ec.edu.monster.servicio;

namespace ServidorWebRestFul
{
    /// <summary>
    /// Fachada de Servicio Web RESTful (WCF).
    /// En el patrón arquitectónico MVC tradicional, este componente actúa como punto de entrada
    /// y despacho de peticiones HTTP, delegando toda la lógica a los controladores especializados
    /// (AutenticacionControlador y ConversorControlador) y retornando representaciones DTO/JSON.
    /// </summary>
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    public class ServicioWebRest : IWSConversorRest
    {
        private readonly AutenticacionControlador authControlador;
        private readonly ConversorControlador conversorControlador;

        public ServicioWebRest()
        {
            this.authControlador = new AutenticacionControlador();
            this.conversorControlador = new ConversorControlador();
        }

        private void AplicarCabecerasCors()
        {
            var ctx = WebOperationContext.Current;
            if (ctx != null)
            {
                var headers = ctx.OutgoingResponse.Headers;
                headers["Access-Control-Allow-Origin"] = "*";
                headers["Access-Control-Allow-Methods"] = "POST, GET, OPTIONS";
                headers["Access-Control-Allow-Headers"] = "Content-Type, Accept, Authorization, X-Requested-With";
            }
        }

        // =====================================================================
        // CORS OPTIONS PREFLIGHT
        // =====================================================================
        public void HandleOptions()
        {
            AplicarCabecerasCors();
        }

        // =====================================================================
        // AUTENTICACIÓN: RUTAS RESTful (/auth/...)
        // =====================================================================
        public LoginResponse LoginRestPost(LoginRequest request)
        {
            AplicarCabecerasCors();
            if (request == null)
            {
                return new LoginResponse(false, null, null, "Cuerpo de solicitud requerido");
            }
            return authControlador.Login(request.Usuario, request.Contrasenia);
        }

        public LoginResponse LoginRestGet(string usuario, string contrasenia)
        {
            AplicarCabecerasCors();
            return authControlador.Login(usuario, contrasenia);
        }

        public RespuestaGeneral CambiarContraseniaRestPost(CambioContraseniaRequest request)
        {
            AplicarCabecerasCors();
            if (request == null)
            {
                return RespuestaGeneral.Error("Cuerpo de solicitud requerido");
            }
            return authControlador.CambiarContrasenia(request.ContraseniaActual, request.ContraseniaNueva, request.Usuario);
        }

        public RespuestaGeneral ValidarTokenRestGet(string token)
        {
            AplicarCabecerasCors();
            return authControlador.ValidarToken(token);
        }

        public RespuestaGeneral LogoutRestPost(string token)
        {
            AplicarCabecerasCors();
            return authControlador.Logout(token);
        }

        // =====================================================================
        // CONVERSIÓN: RUTAS RESTful (/conversor/...)
        // =====================================================================
        public ConversionResponse ConvertirLongitudRestGet(string valor, string unidadInicial, string unidadFinal)
        {
            AplicarCabecerasCors();
            try
            {
                double v = ConversorControlador.ParsearNumero(valor);
                return conversorControlador.ConvertirLongitud(v, unidadInicial, unidadFinal);
            }
            catch (Exception ex)
            {
                return new ConversionResponse(false, "LONGITUD", 0, unidadInicial, 0, unidadFinal, ex.Message);
            }
        }

        public ConversionResponse ConvertirLongitudRestPost(ConversionRequest request)
        {
            AplicarCabecerasCors();
            if (request == null)
            {
                return new ConversionResponse(false, "LONGITUD", 0, null, 0, null, "Cuerpo de solicitud requerido");
            }
            return conversorControlador.ConvertirLongitud(request.Valor, request.UnidadInicial, request.UnidadFinal);
        }

        public ConversionResponse ConvertirMasaRestGet(string valor, string unidadInicial, string unidadFinal)
        {
            AplicarCabecerasCors();
            try
            {
                double v = ConversorControlador.ParsearNumero(valor);
                return conversorControlador.ConvertirMasa(v, unidadInicial, unidadFinal);
            }
            catch (Exception ex)
            {
                return new ConversionResponse(false, "MASA", 0, unidadInicial, 0, unidadFinal, ex.Message);
            }
        }

        public ConversionResponse ConvertirMasaRestPost(ConversionRequest request)
        {
            AplicarCabecerasCors();
            if (request == null)
            {
                return new ConversionResponse(false, "MASA", 0, null, 0, null, "Cuerpo de solicitud requerido");
            }
            return conversorControlador.ConvertirMasa(request.Valor, request.UnidadInicial, request.UnidadFinal);
        }

        public ConversionResponse ConvertirTemperaturaRestGet(string valor, string unidadInicial, string unidadFinal)
        {
            AplicarCabecerasCors();
            try
            {
                double v = ConversorControlador.ParsearNumero(valor);
                return conversorControlador.ConvertirTemperatura(v, unidadInicial, unidadFinal);
            }
            catch (Exception ex)
            {
                return new ConversionResponse(false, "TEMPERATURA", 0, unidadInicial, 0, unidadFinal, ex.Message);
            }
        }

        public ConversionResponse ConvertirTemperaturaRestPost(ConversionRequest request)
        {
            AplicarCabecerasCors();
            if (request == null)
            {
                return new ConversionResponse(false, "TEMPERATURA", 0, null, 0, null, "Cuerpo de solicitud requerido");
            }
            return conversorControlador.ConvertirTemperatura(request.Valor, request.UnidadInicial, request.UnidadFinal);
        }

        public CatalogoUnidadesResponse ObtenerCatalogoUnidades()
        {
            AplicarCabecerasCors();
            return conversorControlador.ObtenerCatalogoUnidades();
        }

        // =====================================================================
        // COMPATIBILIDAD DIRECTA
        // =====================================================================
        public LoginResponse LoginDirectoPost(LoginRequest request)
        {
            return LoginRestPost(request);
        }

        public LoginResponse LoginDirectoGet(string usuario, string contrasenia)
        {
            return LoginRestGet(usuario, contrasenia);
        }

        public RespuestaGeneral CambiarContraseniaDirectoPost(CambioContraseniaRequest request)
        {
            return CambiarContraseniaRestPost(request);
        }

        public ConversionResponse ConvertirLongitudDirectoGet(string valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirLongitudRestGet(valor, unidadInicial, unidadFinal);
        }

        public ConversionResponse ConvertirLongitudDirectoPost(ConversionRequest request)
        {
            return ConvertirLongitudRestPost(request);
        }

        public ConversionResponse ConvertirMasaDirectoGet(string valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirMasaRestGet(valor, unidadInicial, unidadFinal);
        }

        public ConversionResponse ConvertirMasaDirectoPost(ConversionRequest request)
        {
            return ConvertirMasaRestPost(request);
        }

        public ConversionResponse ConvertirTemperaturaDirectoGet(string valor, string unidadInicial, string unidadFinal)
        {
            return ConvertirTemperaturaRestGet(valor, unidadInicial, unidadFinal);
        }

        public ConversionResponse ConvertirTemperaturaDirectoPost(ConversionRequest request)
        {
            return ConvertirTemperaturaRestPost(request);
        }
    }
}
