using System.ComponentModel.DataAnnotations;

namespace ApiPaisesMVC_SQLServer.Models;

public class LoginViewModel
{
    [Required(ErrorMessage = "Ingrese el usuario.")]
    [Display(Name = "Usuario")]
    public string Usuario { get; set; } = string.Empty;

    [Required(ErrorMessage = "Ingrese la contraseña.")]
    [DataType(DataType.Password)]
    [Display(Name = "Contraseña")]
    public string Password { get; set; } = string.Empty;

    public string? ReturnUrl { get; set; }
}
