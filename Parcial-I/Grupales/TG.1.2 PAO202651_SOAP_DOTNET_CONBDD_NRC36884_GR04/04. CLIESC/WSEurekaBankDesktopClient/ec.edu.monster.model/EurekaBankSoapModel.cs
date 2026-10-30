using System.Runtime.Serialization;
using System.ServiceModel;

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

        public string TitularCompleto => $"{NombreCliente} {ApellidoCliente}".Trim();
        public string EtiquetaSemaforo => EstadoMashup == "ROJO"
            ? $"ROJO (Vent. {VentanillaOcupada} - {BloqueadoPor})"
            : "VERDE (Libre)";
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
    }

    [ServiceContract(Namespace = "http://ws.monster.edu.ec/", Name = "EurekaBankWS")]
    public interface IEurekaBankSoapContrato
    {
        [OperationContract] UsuarioModel AutenticarUsuario(string usuario, string clave);
        [OperationContract] List<SucursalModel> ListarSucursales();
        [OperationContract] List<CuentaModel> ListarCuentasPorSucursal(int sucursalId);
        [OperationContract] CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta);
        [OperationContract] List<CuentaModel> ConsultarCuentasPorCliente(string dni);
        [OperationContract] List<MovimientoModel> ConsultarExtracto(string numeroCuenta);
        [OperationContract] OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel ConsultarEstadoMashUp(string numeroCuenta);
        [OperationContract] OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin);
        [OperationContract] OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario);
    }

    /// <summary>
    /// Capa Modelo (MVC Tradicional) de comunicación SOAP con el Servidor WCF.
    /// Cliente delgado: cero lógica de negocio ni acceso a BDD en el cliente.
    /// </summary>
    public class EurekaBankSoapModel
    {
        public string EndpointUrl { get; set; } = "http://localhost:5120/EurekaBankWS.svc";

        private T Ejecutar<T>(Func<IEurekaBankSoapContrato, T> accion)
        {
            var binding = new BasicHttpBinding(BasicHttpSecurityMode.None)
            {
                MaxReceivedMessageSize = 10 * 1024 * 1024,
                SendTimeout = TimeSpan.FromSeconds(30),
                ReceiveTimeout = TimeSpan.FromSeconds(30)
            };
            using var factory = new ChannelFactory<IEurekaBankSoapContrato>(binding, new EndpointAddress(EndpointUrl));
            var canal = factory.CreateChannel();
            try
            {
                var res = accion(canal);
                ((IClientChannel)canal).Close();
                return res;
            }
            catch
            {
                ((IClientChannel)canal).Abort();
                throw;
            }
        }

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                return Ejecutar(c => c.AutenticarUsuario(usuario, clave));
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error conectando al servidor SOAP: " + ex.Message };
            }
        }

        public List<SucursalModel> ListarSucursales() =>
            EjecutarSegura(c => c.ListarSucursales(), new List<SucursalModel>());

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId) =>
            EjecutarSegura(c => c.ListarCuentasPorSucursal(sucursalId), new List<CuentaModel>());

        public CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta) =>
            EjecutarSegura(c => c.ObtenerCuentaPorNumero(numeroCuenta), null);

        public List<CuentaModel> ConsultarCuentasPorCliente(string dni) =>
            EjecutarSegura(c => c.ConsultarCuentasPorCliente(dni), new List<CuentaModel>());

        public List<MovimientoModel> ConsultarExtracto(string numeroCuenta) =>
            EjecutarSegura(c => c.ConsultarExtracto(numeroCuenta), new List<MovimientoModel>());

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            EjecutarOperacion(c => c.Depositar(numeroCuenta, monto, idEmpleado, ventanilla));

        public OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            EjecutarOperacion(c => c.Retirar(numeroCuenta, monto, idEmpleado, ventanilla));

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla) =>
            EjecutarOperacion(c => c.Transferir(cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla));

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta) =>
            EjecutarOperacion(c => c.ConsultarEstadoMashUp(numeroCuenta));

        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin) =>
            EjecutarOperacion(c => c.DesbloquearCuenta(numeroCuenta, usuarioAdmin));

        public OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario) =>
            EjecutarOperacion(c => c.BloquearCuentaManual(numeroCuenta, ventanilla, usuario));

        private T EjecutarSegura<T>(Func<IEurekaBankSoapContrato, T> fn, T fallback)
        {
            try { return Ejecutar(fn); }
            catch { return fallback; }
        }

        private OperacionModel EjecutarOperacion(Func<IEurekaBankSoapContrato, OperacionModel> fn)
        {
            try
            {
                return Ejecutar(fn);
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Codigo = "ERROR_RED", Mensaje = "Fallo SOAP: " + ex.Message };
            }
        }
    }
}
