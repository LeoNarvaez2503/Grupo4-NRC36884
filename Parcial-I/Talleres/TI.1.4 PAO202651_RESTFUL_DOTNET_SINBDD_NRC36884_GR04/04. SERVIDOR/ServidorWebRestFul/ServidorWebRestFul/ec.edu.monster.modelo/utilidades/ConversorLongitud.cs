using System;
using System.Collections.Generic;
using ec.edu.monster.modelo.enums;

namespace ec.edu.monster.modelo.utilidades
{
    /// <summary>
    /// Implementación de conversor para unidades de longitud con base en el metro.
    /// </summary>
    public class ConversorLongitud : IConversor<UnidadLongitud>
    {
        private readonly Dictionary<UnidadLongitud, double> factores = new Dictionary<UnidadLongitud, double>
        {
            { UnidadLongitud.MILIMETRO, 0.001 },
            { UnidadLongitud.CENTIMETRO, 0.01 },
            { UnidadLongitud.METRO, 1.0 },
            { UnidadLongitud.KILOMETRO, 1000.0 },
            { UnidadLongitud.YARDA, 0.9144 }
        };

        public double Convertir(double valor, UnidadLongitud unidadOrigen, UnidadLongitud unidadFinal)
        {
            if (!factores.ContainsKey(unidadOrigen) || !factores.ContainsKey(unidadFinal))
            {
                throw new ArgumentException("Unidad de longitud no soportada");
            }

            double valorEnBase = valor * factores[unidadOrigen];
            return valorEnBase / factores[unidadFinal];
        }
    }
}
