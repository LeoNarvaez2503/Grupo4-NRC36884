using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ApiPaisesMVC_SQLServer.Models;

[Table("Universidades")]
public class Universidad
{
    public int Id { get; set; }

    [Required]
    [StringLength(200)]
    [Display(Name = "Nombre")]
    public string Nombre { get; set; } = string.Empty;

    [StringLength(150)]
    [Display(Name = "Dominio")]
    public string? Dominio { get; set; }

    [StringLength(300)]
    [Display(Name = "Sitio Web")]
    public string? SitioWeb { get; set; }

    [StringLength(100)]
    [Display(Name = "País")]
    public string? Pais { get; set; }
}
