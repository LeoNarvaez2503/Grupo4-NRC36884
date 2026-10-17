using ServidorDotNet_Grupo4.modelos.utilidades;
using System;

namespace ServidorDotNet_Grupo4.servicios
{
    /// <summary>
    /// Servicio genérico de conversión de unidades en el modelo de negocio.
    /// </summary>
    public class ServicioConversor<T>
    {
        private readonly IConversor<T> conversor;

        public ServicioConversor(IConversor<T> conversor)
        {
            this.conversor = conversor ?? throw new ArgumentNullException(nameof(conversor), "No se ha inyectado un conversor");
        }

        public double Convertir(double valor, T origen, T destino)
        {
            return conversor.Convertir(valor, origen, destino);
        }
    }
}
