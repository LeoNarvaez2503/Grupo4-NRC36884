using System;
using System.Collections.Generic;
using ServidorDotNet_Grupo4.modelos.enums;

namespace ServidorDotNet_Grupo4.modelos.utilidades
{
    /// <summary>
    /// Conversor para unidades de masa.
    /// </summary>
    public class ConversorMasa : IConversor<UnidadMasa>
    {
        private readonly Dictionary<UnidadMasa, double> factores = new Dictionary<UnidadMasa, double>
        {
            { UnidadMasa.MILIGRAMO, 0.001 },
            { UnidadMasa.GRAMO, 1.0 },
            { UnidadMasa.KILOGRAMO, 1000.0 },
            { UnidadMasa.TONELADA, 1000000.0 },
            { UnidadMasa.ONZA, 28.3495 }
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
