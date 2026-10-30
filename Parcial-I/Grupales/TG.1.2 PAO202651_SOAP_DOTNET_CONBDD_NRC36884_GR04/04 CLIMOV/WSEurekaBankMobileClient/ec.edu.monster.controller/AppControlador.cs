using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    /// <summary>
    /// Capa Controlador (MVC Tradicional puro) para el Cliente Móvil .NET MAUI (SOAP).
    /// </summary>
    public class AppControlador
    {
        private readonly EurekaBankSoapModel _modelo = new();
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

        public List<SucursalModel> ObtenerSucursales() => _modelo.ListarSucursales();
        public List<CuentaModel> ObtenerCuentasPorSucursal(int sucursalId) => _modelo.ListarCuentasPorSucursal(sucursalId);
        public List<CuentaModel> BuscarCuentasPorCliente(string dni) => _modelo.ConsultarCuentasPorCliente(dni);
        public List<MovimientoModel> ObtenerExtracto(string numeroCuenta) => _modelo.ConsultarExtracto(numeroCuenta);

        public OperacionModel Depositar(string cta, double monto) =>
            _modelo.Depositar(cta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel DepositarEnVentanilla(string cta, double monto, int ventanilla) =>
            _modelo.Depositar(cta, monto, SesionActual?.IdEmpleado ?? 1, ventanilla);

        public OperacionModel Retirar(string cta, double monto) =>
            _modelo.Retirar(cta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel Transferir(string origen, string destino, double monto) =>
            _modelo.Transferir(origen, destino, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel BloquearManual(string cta) =>
            _modelo.BloquearCuentaManual(cta, VentanillaActiva, SesionActual?.Usuario ?? "Monster");

        public OperacionModel Desbloquear(string cta) =>
            _modelo.DesbloquearCuenta(cta, SesionActual?.Usuario ?? "Monster");
    }
}
