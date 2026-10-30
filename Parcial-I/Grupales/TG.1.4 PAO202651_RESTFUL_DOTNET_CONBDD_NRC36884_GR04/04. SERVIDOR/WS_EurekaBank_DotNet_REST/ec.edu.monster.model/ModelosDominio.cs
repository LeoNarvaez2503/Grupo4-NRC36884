using System.Runtime.Serialization;

namespace ec.edu.monster.model
{
    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class UsuarioModel
    {
        [DataMember(Order = 1)] public bool Autenticado { get; set; }
        [DataMember(Order = 2)] public int IdEmpleado { get; set; }
        [DataMember(Order = 3)] public string Usuario { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string NombreCompleto { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string Rol { get; set; } = string.Empty;
        [DataMember(Order = 6)] public int IdSucursal { get; set; }
        [DataMember(Order = 7)] public int Ventanilla { get; set; }
        [DataMember(Order = 8)] public string Token { get; set; } = string.Empty;
        [DataMember(Order = 9)] public string Mensaje { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class SucursalModel
    {
        [DataMember(Order = 1)] public int IdSucursal { get; set; }
        [DataMember(Order = 2)] public string Codigo { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string Nombre { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string Ciudad { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string Direccion { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class CuentaModel
    {
        [DataMember(Order = 1)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 2)] public string Moneda { get; set; } = "USD";
        [DataMember(Order = 3)] public double Saldo { get; set; }
        [DataMember(Order = 4)] public string EstadoCuenta { get; set; } = "Activa";
        [DataMember(Order = 5)] public int IdCliente { get; set; }
        [DataMember(Order = 6)] public string DniCliente { get; set; } = string.Empty;
        [DataMember(Order = 7)] public string NombreCliente { get; set; } = string.Empty;
        [DataMember(Order = 8)] public string ApellidoCliente { get; set; } = string.Empty;
        [DataMember(Order = 9)] public string TelefonoCliente { get; set; } = string.Empty;
        [DataMember(Order = 10)] public string EmailCliente { get; set; } = string.Empty;
        [DataMember(Order = 11)] public int IdSucursal { get; set; }
        [DataMember(Order = 12)] public string NombreSucursal { get; set; } = string.Empty;
        [DataMember(Order = 13)] public string EstadoMashup { get; set; } = "VERDE";
        [DataMember(Order = 14)] public int VentanillaOcupada { get; set; }
        [DataMember(Order = 15)] public string BloqueadoPor { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class MovimientoModel
    {
        [DataMember(Order = 1)] public int IdMovimiento { get; set; }
        [DataMember(Order = 2)] public string NumeroOperacion { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 4)] public int IdEmpleado { get; set; }
        [DataMember(Order = 5)] public string NombreEmpleado { get; set; } = string.Empty;
        [DataMember(Order = 6)] public int Ventanilla { get; set; }
        [DataMember(Order = 7)] public string Tipo { get; set; } = string.Empty;
        [DataMember(Order = 8)] public double Monto { get; set; }
        [DataMember(Order = 9)] public double SaldoResultante { get; set; }
        [DataMember(Order = 10)] public string CuentaReferencia { get; set; } = string.Empty;
        [DataMember(Order = 11)] public string Descripcion { get; set; } = string.Empty;
        [DataMember(Order = 12)] public string FechaHora { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class OperacionModel
    {
        [DataMember(Order = 1)] public bool Exito { get; set; }
        [DataMember(Order = 2)] public string Codigo { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string Mensaje { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string NumeroOperacion { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 6)] public double SaldoAnterior { get; set; }
        [DataMember(Order = 7)] public double MontoOperacion { get; set; }
        [DataMember(Order = 8)] public double SaldoActual { get; set; }
        [DataMember(Order = 9)] public string EstadoMashup { get; set; } = "VERDE";
        [DataMember(Order = 10)] public int Ventanilla { get; set; }
        [DataMember(Order = 11)] public string FechaHora { get; set; } = string.Empty;

        public static OperacionModel Error(string codigo, string mensaje, string estadoMashup = "VERDE", int ventanilla = 0)
        {
            return new OperacionModel
            {
                Exito = false,
                Codigo = codigo,
                Mensaje = mensaje,
                EstadoMashup = estadoMashup,
                Ventanilla = ventanilla,
                FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
            };
        }
    }
}
