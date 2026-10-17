namespace ec.edu.monster.modelo
{
    public enum UnidadTemperatura
    {
        CELSIUS,
        FAHRENHEIT,
        KELVIN,
        RANKINE
    }

    public static class UnidadTemperaturaExtensions
    {
        public static string GetEtiqueta(this UnidadTemperatura unidad) => unidad switch
        {
            UnidadTemperatura.CELSIUS => "Celsius (°C)",
            UnidadTemperatura.FAHRENHEIT => "Fahrenheit (°F)",
            UnidadTemperatura.KELVIN => "Kelvin (K)",
            UnidadTemperatura.RANKINE => "Rankine (°R)",
            _ => unidad.ToString()
        };
    }
}
