namespace ec.edu.monster.model
{
    public class LoginRestRequest
    {
        public string Usuario { get; set; } = string.Empty;
        public string Clave { get; set; } = string.Empty;
    }

    public class TransaccionRestRequest
    {
        public string NumeroCuenta { get; set; } = string.Empty;
        public double Monto { get; set; }
        public int IdEmpleado { get; set; } = 1;
        public int Ventanilla { get; set; } = 1;
    }

    public class TransferenciaRestRequest
    {
        public string CuentaOrigen { get; set; } = string.Empty;
        public string CuentaDestino { get; set; } = string.Empty;
        public double Monto { get; set; }
        public int IdEmpleado { get; set; } = 1;
        public int Ventanilla { get; set; } = 1;
    }

    public class BloqueoRestRequest
    {
        public int Ventanilla { get; set; } = 1;
        public string Usuario { get; set; } = "Monster";
    }
}
