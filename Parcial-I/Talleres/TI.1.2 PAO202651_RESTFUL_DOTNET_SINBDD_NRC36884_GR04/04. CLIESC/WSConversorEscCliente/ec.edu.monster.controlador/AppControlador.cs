using ec.edu.monster.modelo;

namespace ec.edu.monster.controlador
{
    public class AppControlador
    {
        private readonly ConversorService service = new ConversorService();

        public string Login(string usuario, string contrasenia)
        {
            return service.Login(usuario, contrasenia);
        }

        public double ConvertirLongitud(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirLongitud(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirLogintud(double n1, string unidadInicial, string unidadFinal)
        {
            return ConvertirLongitud(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirMasa(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirMasa(n1, unidadInicial, unidadFinal);
        }

        public double ConvertirTemperatura(double n1, string unidadInicial, string unidadFinal)
        {
            return service.ConvertirTemperatura(n1, unidadInicial, unidadFinal);
        }
    }
}
