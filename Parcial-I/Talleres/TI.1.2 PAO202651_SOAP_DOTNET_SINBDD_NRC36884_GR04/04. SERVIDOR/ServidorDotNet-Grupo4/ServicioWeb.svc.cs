using ServidorDotNet_Grupo4.controladores;
using ServidorDotNet_Grupo4.servicios;

namespace ServidorDotNet_Grupo4
{
    /// <summary>
    /// Servicio Web SOAP (WCF) que expone las operaciones de autenticación y conversión de unidades.
    /// Delega la ejecución al controlador MVC (ConversorControlador).
    /// </summary>
    public class ServicioWeb : IWSConversorUnidades
    {
        private readonly ConversorControlador controlador = new ConversorControlador();

        public string Login(string usuario, string password)
        {
            return controlador.Login(usuario, password);
        }

        public string CambiarPassword(string usuario, string passwordActual, string passwordNueva)
        {
            return controlador.CambiarPassword(usuario, passwordActual, passwordNueva);
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            return controlador.ConvertirLongitud(valor, unidadInicial, unidadFinal);
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            return controlador.ConvertirTemperatura(valor, unidadInicial, unidadFinal);
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            return controlador.ConvertirMasa(valor, unidadInicial, unidadFinal);
        }
    }
}
