using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    /// <summary>
    /// Capa Controlador (MVC Tradicional) para el Cliente de Consola SOAP .NET.
    /// </summary>
    public class AppControlador
    {
        private readonly EurekaBankRestModel _modelo = new();
        public UsuarioModel? SesionActual { get; private set; }
        public int VentanillaActiva { get; set; } = 1;

        public string EndpointActual
        {
            get => _modelo.EndpointUrl;
            set => _modelo.EndpointUrl = value;
        }

        public UsuarioModel IniciarSesion(string usuario, string clave)
        {
            var res = _modelo.Autenticar(usuario, clave);
            if (res.Autenticado)
            {
                SesionActual = res;
                if (res.Ventanilla >= 1 && res.Ventanilla <= 4)
                    VentanillaActiva = res.Ventanilla;
            }
            return res;
        }

        public void CerrarSesion() => SesionActual = null;

        public List<SucursalModel> ObtenerSucursales() => _modelo.ListarSucursales();
        public List<CuentaModel> ObtenerCuentasPorSucursal(int sucursalId) => _modelo.ListarCuentasPorSucursal(sucursalId);
        public CuentaModel? BuscarCuentaPorNumero(string numeroCuenta) => _modelo.ObtenerCuentaPorNumero(numeroCuenta);
        public List<CuentaModel> BuscarCuentasPorCliente(string dni) => _modelo.ConsultarCuentasPorCliente(dni);
        public List<MovimientoModel> ObtenerExtracto(string numeroCuenta) => _modelo.ConsultarExtracto(numeroCuenta);

        public OperacionModel Depositar(string numeroCuenta, double monto) =>
            _modelo.Depositar(numeroCuenta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel DepositarEnVentanilla(string numeroCuenta, double monto, int ventanilla) =>
            _modelo.Depositar(numeroCuenta, monto, SesionActual?.IdEmpleado ?? 1, ventanilla);

        public OperacionModel Retirar(string numeroCuenta, double monto) =>
            _modelo.Retirar(numeroCuenta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel Transferir(string origen, string destino, double monto) =>
            _modelo.Transferir(origen, destino, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta) =>
            _modelo.ConsultarEstadoMashUp(numeroCuenta);

        public OperacionModel DesbloquearCuenta(string numeroCuenta) =>
            _modelo.DesbloquearCuenta(numeroCuenta, SesionActual?.Usuario ?? "Monster");

        public OperacionModel BloquearCuentaManual(string numeroCuenta) =>
            _modelo.BloquearCuentaManual(numeroCuenta, VentanillaActiva, SesionActual?.Usuario ?? "Monster");
    }
}

