using System;
using System.Globalization;
using System.Text;
using ec.edu.monster.modelo.enums;

namespace ec.edu.monster.modelo.mapeadores
{
    public static class UnidadMapper
    {
        private static string Normalizar(string texto)
        {
            if (string.IsNullOrWhiteSpace(texto))
            {
                throw new ArgumentException("La unidad no puede estar vacía.");
            }

            string textoNormalizado = texto.Trim().Normalize(NormalizationForm.FormD);
            StringBuilder sb = new StringBuilder();

            foreach (char c in textoNormalizado)
            {
                UnicodeCategory uc = CharUnicodeInfo.GetUnicodeCategory(c);
                if (uc != UnicodeCategory.NonSpacingMark)
                {
                    sb.Append(c);
                }
            }

            return sb.ToString().Normalize(NormalizationForm.FormC).ToUpperInvariant();
        }

        public static UnidadLongitud ToLongitud(string valor)
        {
            string u = Normalizar(valor);
            switch (u)
            {
                case "MILIMETRO":
                case "MILIMETROS":
                case "MM":
                    return UnidadLongitud.MILIMETRO;
                case "CENTIMETRO":
                case "CENTIMETROS":
                case "CM":
                    return UnidadLongitud.CENTIMETRO;
                case "METRO":
                case "METROS":
                case "M":
                    return UnidadLongitud.METRO;
                case "KILOMETRO":
                case "KILOMETROS":
                case "KM":
                    return UnidadLongitud.KILOMETRO;
                case "YARDA":
                case "YARDAS":
                case "YD":
                    return UnidadLongitud.YARDA;
                default:
                    if (Enum.TryParse(u, true, out UnidadLongitud resultado))
                    {
                        return resultado;
                    }
                    throw new ArgumentException("Unidad de longitud no soportada: " + valor);
            }
        }

        public static UnidadMasa ToMasa(string valor)
        {
            string u = Normalizar(valor);
            switch (u)
            {
                case "MILIGRAMO":
                case "MILIGRAMOS":
                case "MG":
                    return UnidadMasa.MILIGRAMO;
                case "GRAMO":
                case "GRAMOS":
                case "G":
                    return UnidadMasa.GRAMO;
                case "KILOGRAMO":
                case "KILOGRAMOS":
                case "KG":
                    return UnidadMasa.KILOGRAMO;
                case "TONELADA":
                case "TONELADAS":
                case "T":
                    return UnidadMasa.TONELADA;
                case "ONZA":
                case "ONZAS":
                case "OZ":
                    return UnidadMasa.ONZA;
                default:
                    if (Enum.TryParse(u, true, out UnidadMasa resultado))
                    {
                        return resultado;
                    }
                    throw new ArgumentException("Unidad de masa no soportada: " + valor);
            }
        }

        public static UnidadTemperatura ToTemperatura(string valor)
        {
            string u = Normalizar(valor);
            switch (u)
            {
                case "CELSIUS":
                case "CENTIGRADO":
                case "CENTIGRADOS":
                case "C":
                case "GRADOS CELSIUS":
                case "°C":
                    return UnidadTemperatura.CELSIUS;
                case "FAHRENHEIT":
                case "F":
                case "GRADOS FAHRENHEIT":
                case "°F":
                    return UnidadTemperatura.FAHRENHEIT;
                case "KELVIN":
                case "K":
                case "GRADOS KELVIN":
                case "°K":
                    return UnidadTemperatura.KELVIN;
                case "RANKINE":
                case "R":
                case "GRADOS RANKINE":
                case "°R":
                    return UnidadTemperatura.RANKINE;
                default:
                    if (Enum.TryParse(u, true, out UnidadTemperatura resultado))
                    {
                        return resultado;
                    }
                    throw new ArgumentException("Unidad de temperatura no soportada: " + valor);
            }
        }
    }
}
