using System;
using System.Collections.Generic;
using ec.edu.monster.modelo.enums;

namespace ec.edu.monster.modelo.utilidades
{
    /// <summary>
    /// Implementación de conversor para unidades de masa con base en el gramo.
    /// </summary>
    public class ConversorMasa : IConversor<UnidadMasa>
    {
        private readonly Dictionary<UnidadMasa, double> factores = new Dictionary<UnidadMasa, double>
        {
            { UnidadMasa.MILIGRAMO, 0.001 },
            { UnidadMasa.GRAMO, 1.0 },
            { UnidadMasa.KILOGRAMO, 1000.0 },
            { UnidadMasa.TONELADA, 1000000.0 },
            { UnidadMasa.ONZA, 28.349523125 }
        };

        public double Convertir(double valor, UnidadMasa unidadOrigen, UnidadMasa unidadFinal)
        {
            if (!factores.ContainsKey(unidadOrigen) || !factores.ContainsKey(unidadFinal))
            {
                throw new ArgumentException("Unidad de masa no soportada");
            }

            double valorEnBase = valor * factores[unidadOrigen];
            return valorEnBase / factores[unidadFinal];
        }
    }
}
