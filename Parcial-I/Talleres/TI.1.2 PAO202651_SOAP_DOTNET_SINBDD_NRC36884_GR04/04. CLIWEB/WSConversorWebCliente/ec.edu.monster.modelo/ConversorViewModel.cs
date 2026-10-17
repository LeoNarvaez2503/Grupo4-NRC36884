using System.Collections.Generic;

namespace ec.edu.monster.modelo
{
    public class ConversorViewModel
    {
        public string Titulo { get; set; } = "Conversor de Unidades";
        public string NombreUsuario { get; set; } = string.Empty;
        public string TipoMagnitud { get; set; } = "LONGITUD";
        public string? Valor { get; set; }
        public string UnidadOrigen { get; set; } = string.Empty;
        public string UnidadDestino { get; set; } = string.Empty;
        public string? Resultado { get; set; }
        public List<string> UnidadesDisponibles { get; set; } = new List<string>();
        public List<HistorialItem> Historial { get; set; } = new List<HistorialItem>();
        public string? MensajeError { get; set; }
        public string? MensajeExito { get; set; }
    }
}
