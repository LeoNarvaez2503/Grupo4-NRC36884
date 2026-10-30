namespace ec.edu.monster.model
{
    public class LoginWebModel
    {
        public string Usuario { get; set; } = "Monster";
        public string Clave { get; set; } = "Monster9";
        public int Ventanilla { get; set; } = 1;
        public string? MensajeError { get; set; }
    }

    public class DashboardWebModel
    {
        public UsuarioModel Sesion { get; set; } = new();
        public int VentanillaActiva { get; set; } = 1;
        public int SucursalSeleccionada { get; set; }
        public string Filtro { get; set; } = string.Empty;
        public string CuentaSeleccionada { get; set; } = "001-0001";
        public List<SucursalModel> Sucursales { get; set; } = new();
        public List<CuentaModel> Cuentas { get; set; } = new();
        public List<MovimientoModel> Extracto { get; set; } = new();
        public int TotalVerdes => Cuentas.Count(c => c.EstadoMashup != "ROJO");
        public int TotalRojas => Cuentas.Count(c => c.EstadoMashup == "ROJO");
    }
}
