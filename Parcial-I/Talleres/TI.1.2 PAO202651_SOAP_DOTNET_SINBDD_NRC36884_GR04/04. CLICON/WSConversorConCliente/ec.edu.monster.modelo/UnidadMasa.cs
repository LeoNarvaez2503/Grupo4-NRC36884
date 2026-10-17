namespace ec.edu.monster.modelo
{
    public enum UnidadMasa
    {
        MILIGRAMO,
        GRAMO,
        KILOGRAMO,
        TONELADA,
        ONZA
    }

    public static class UnidadMasaExtensions
    {
        public static string GetEtiqueta(this UnidadMasa unidad) => unidad switch
        {
            UnidadMasa.MILIGRAMO => "Miligramo (mg)",
            UnidadMasa.GRAMO => "Gramo (g)",
            UnidadMasa.KILOGRAMO => "Kilogramo (kg)",
            UnidadMasa.TONELADA => "Tonelada (t)",
            UnidadMasa.ONZA => "Onza (oz)",
            _ => unidad.ToString()
        };
    }
}
