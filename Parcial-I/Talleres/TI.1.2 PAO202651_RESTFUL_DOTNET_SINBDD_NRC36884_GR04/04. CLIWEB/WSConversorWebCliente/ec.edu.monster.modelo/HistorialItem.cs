namespace ec.edu.monster.modelo
{
    public class HistorialItem
    {
        public string Magnitud { get; set; } = string.Empty;
        public double ValorOrigen { get; set; }
        public string UnidadOrigen { get; set; } = string.Empty;
        public double ValorDestino { get; set; }
        public string UnidadDestino { get; set; } = string.Empty;
        public DateTime Fecha { get; set; } = DateTime.Now;
    }
}
