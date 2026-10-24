namespace ec.edu.monster.modelo
{
    public enum UnidadLongitud
    {
        MILIMETRO,
        CENTIMETRO,
        METRO,
        KILOMETRO,
        YARDA
    }

    public static class UnidadLongitudExtensions
    {
        public static string GetEtiqueta(this UnidadLongitud unidad) => unidad switch
        {
            UnidadLongitud.MILIMETRO => "Milímetro (mm)",
            UnidadLongitud.CENTIMETRO => "Centímetro (cm)",
            UnidadLongitud.METRO => "Metro (m)",
            UnidadLongitud.KILOMETRO => "Kilómetro (km)",
            UnidadLongitud.YARDA => "Yarda (yd)",
            _ => unidad.ToString()
        };
    }
}
