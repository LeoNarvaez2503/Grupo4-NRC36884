namespace ApiPaisesMVC_SQLServer.Models;

public class RegionEstadisticaItem
{
    public string Region { get; set; } = string.Empty;
    public int CantidadPaises { get; set; }
    public double PromedioPoblacion { get; set; }
    public long PoblacionTotal { get; set; }
}

public class ReportesViewModel
{
    public int TotalPaises { get; set; }
    public Pais? PaisMasPoblado { get; set; }
    public RegionEstadisticaItem? RegionConMasPaises { get; set; }
    public List<RegionEstadisticaItem> EstadisticasPorRegion { get; set; } = [];
}
