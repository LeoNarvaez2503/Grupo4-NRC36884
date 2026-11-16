using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ApiPaisesMVC_SQLServer.Models;

[Table("Paises")]
public class Pais
{
    public int Id { get; set; }

    [Required(ErrorMessage = "El nombre del país es obligatorio.")]
    [StringLength(100)]
    [Display(Name = "Nombre")]
    public string Nombre { get; set; } = string.Empty;

    [StringLength(100)]
    [Display(Name = "Capital")]
    public string? Capital { get; set; }

    [Required(ErrorMessage = "La región es obligatoria.")]
    [StringLength(100)]
    [Display(Name = "Región")]
    public string Region { get; set; } = string.Empty;

    [StringLength(100)]
    [Display(Name = "Subregión")]
    public string? SubRegion { get; set; }

    [Range(0, long.MaxValue, ErrorMessage = "La población no puede ser negativa.")]
    [Display(Name = "Población")]
    public long Poblacion { get; set; }

    [Range(0, double.MaxValue, ErrorMessage = "El área no puede ser negativa.")]
    [Display(Name = "Área (km²)")]
    public double Area { get; set; }

    [StringLength(100)]
    [Display(Name = "Moneda")]
    public string? Moneda { get; set; }
}
