using System.ServiceModel;

namespace ServidorDotNet_Grupo4.servicios
{
    [ServiceContract(Name = "IWSConversorUnidades")]
    public interface IWSConversorUnidades
    {
        [OperationContract(Name = "login")]
        string Login(string usuario, string password);

        [OperationContract(Name = "cambiarPassword")]
        string CambiarPassword(string usuario, string passwordActual, string passwordNueva);

        [OperationContract(Name = "ConvertirLongitud")]
        double ConvertirLongitud(double valor, string unidadInicial, string unidadFinal);

        [OperationContract(Name = "ConvertirTemperatura")]
        double ConvertirTemperatura(double valor, string unidadInicial, string unidadFinal);

        [OperationContract(Name = "ConvertirMasa")]
        double ConvertirMasa(double valor, string unidadInicial, string unidadFinal);
    }
}
