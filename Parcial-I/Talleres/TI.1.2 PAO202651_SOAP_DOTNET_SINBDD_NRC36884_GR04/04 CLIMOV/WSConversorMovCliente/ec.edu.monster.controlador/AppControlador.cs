using System.Threading.Tasks;
using ec.edu.monster.modelo;

namespace ec.edu.monster.controlador
{
    public class AppControlador
    {
        private readonly ConversorService service = new ConversorService();

        public async Task<string> LoginAsync(string usuario, string contrasenia)
        {
            return await service.LoginAsync(usuario, contrasenia);
        }

        public string Login(string usuario, string contrasenia)
        {
            return service.Login(usuario, contrasenia);
        }

        public async Task<double> ConvertirLongitudAsync(double n1, string unidadInicial, string unidadFinal)
        {
            return await service.ConvertirLongitudAsync(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirLongitud(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirLongitud(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirLogintud(double n1, string unidadInicial, string unidadFinal)
        {
            return ConvertirLongitud(n1, unidadInicial, unidadFinal);
        }

        public async Task<double> ConvertirMasaAsync(double n1, string unidadInicial, string unidadFinal)
        {
            return await service.ConvertirMasaAsync(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirMasa(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirMasa(n1, unidadInicial, unidadFinal);
        }

        public async Task<double> ConvertirTemperaturaAsync(double n1, string unidadInicial, string unidadFinal)
        {
            return await service.ConvertirTemperaturaAsync(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirTemperatura(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirTemperatura(n1, unidadInicial, unidadFinal);
        }
    }
}
