using System;
using ServiceReference1;

namespace ec.edu.monster.modelo
{
    public class ConversorService
    {
        public string Login(string usuario, string contrasenia)
        {
            using var cliente = new WSConversorUnidadesClient();
            return cliente.loginAsync(usuario, contrasenia).GetAwaiter().GetResult();
        }

        public double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = new WSConversorUnidadesClient();
            return cliente.ConvertirLongitudAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public double ConvertirMasa(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = new WSConversorUnidadesClient();
            return cliente.ConvertirMasaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }

        public double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal)
        {
            using var cliente = new WSConversorUnidadesClient();
            return cliente.ConvertirTemperaturaAsync(valor, unidadInicial, unidadFinal).GetAwaiter().GetResult();
        }
    }
}
