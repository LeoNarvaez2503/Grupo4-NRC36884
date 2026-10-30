using CoreWCF;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    [ServiceContract(Namespace = "http://ws.monster.edu.ec/", Name = "EurekaBankWS")]
    public interface IEurekaBankSoapContrato
    {
        [OperationContract]
        UsuarioModel AutenticarUsuario(string usuario, string clave);

        [OperationContract]
        List<SucursalModel> ListarSucursales();

        [OperationContract]
        List<CuentaModel> ListarCuentasPorSucursal(int sucursalId);

        [OperationContract]
        CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta);

        [OperationContract]
        List<CuentaModel> ConsultarCuentasPorCliente(string dni);

        [OperationContract]
        List<MovimientoModel> ConsultarExtracto(string numeroCuenta);

        [OperationContract]
        OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);

        [OperationContract]
        OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);

        [OperationContract]
        OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla);

        [OperationContract]
        OperacionModel ConsultarEstadoMashUp(string numeroCuenta);

        [OperationContract]
        OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin);

        [OperationContract]
        OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario);
    }

    [ServiceBehavior(
        IncludeExceptionDetailInFaults = true,
        InstanceContextMode = InstanceContextMode.Single,
        ConcurrencyMode = ConcurrencyMode.Multiple)]
    public class EurekaBankSoapController : IEurekaBankSoapContrato
    {
        private readonly EurekaBankBddModel _modelo = new();

        public UsuarioModel AutenticarUsuario(string usuario, string clave) =>
            _modelo.AutenticarUsuario(usuario, clave);

        public List<SucursalModel> ListarSucursales() =>
            _modelo.ListarSucursales();

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId) =>
            _modelo.ListarCuentasPorSucursal(sucursalId);

        public CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta) =>
            _modelo.ObtenerCuentaPorNumero(numeroCuenta);

        public List<CuentaModel> ConsultarCuentasPorCliente(string dni) =>
            _modelo.ConsultarCuentasPorCliente(dni);

        public List<MovimientoModel> ConsultarExtracto(string numeroCuenta) =>
            _modelo.ConsultarExtracto(numeroCuenta);

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Depositar(numeroCuenta, monto, idEmpleado, ventanilla);

        public OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Retirar(numeroCuenta, monto, idEmpleado, ventanilla);

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Transferir(cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla);

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta) =>
            _modelo.ConsultarEstadoMashUp(numeroCuenta);

        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin) =>
            _modelo.DesbloquearCuenta(numeroCuenta, usuarioAdmin);

        public OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario) =>
            _modelo.BloquearCuentaManual(numeroCuenta, ventanilla, usuario);
    }
}
