namespace ec.edu.monster.modelo
{
    public class LoginViewModel
    {
        public string Usuario { get; set; } = string.Empty;
        public string Contrasenia { get; set; } = string.Empty;
        public string? MensajeError { get; set; }
    }
}
