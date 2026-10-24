using System;
using ec.edu.monster.modelo.utilidades;

namespace ec.edu.monster.servicio
{
    /// <summary>
    /// Servicio genérico del modelo para la lógica de conversión de unidades.
    /// </summary>
    /// <typeparam name="T">Tipo de unidad (enum).</typeparam>
    public class ServicioConversor<T>
    {
        private readonly IConversor<T> conversor;

        public ServicioConversor(IConversor<T> conversor)
        {
            this.conversor = conversor ?? throw new ArgumentNullException(nameof(conversor), "Conversor no especificado");
        }

        public double Convertir(double valor, T origen, T destino)
        {
            return conversor.Convertir(valor, origen, destino);
        }
    }
}
