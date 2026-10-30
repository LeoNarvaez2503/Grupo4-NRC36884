using System.Globalization;
using ec.edu.monster.controller;
using ec.edu.monster.model;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Dispatching;

namespace ec.edu.monster.view
{
    public partial class DashboardView : ContentPage
    {
        private readonly AppControlador _controlador;
        private IDispatcherTimer? _timer;
        private bool _refrescando;

        public DashboardView(AppControlador controlador)
        {
            InitializeComponent();
            _controlador = controlador;
            LblBienvenida.Text = $"Operador: {_controlador.SesionActual?.NombreCompleto} | Ventanilla {_controlador.VentanillaActiva}";
        }

        protected override async void OnAppearing()
        {
            base.OnAppearing();
            await RefrescarTodoAsync();

            _timer = Dispatcher.CreateTimer();
            _timer.Interval = TimeSpan.FromMilliseconds(1200);
            _timer.Tick += async (_, _) => await RefrescarMashupSilenciosoAsync();
            _timer.Start();
        }

        protected override void OnDisappearing()
        {
            _timer?.Stop();
            base.OnDisappearing();
        }

        private async Task RefrescarTodoAsync()
        {
            await RefrescarMashupSilenciosoAsync(forzar: true);
            await CargarExtractoAsync(TxtCuenta.Text?.Trim() ?? "001-0001");
        }

        private async Task RefrescarMashupSilenciosoAsync(bool forzar = false)
        {
            if (_refrescando) return;
            string filtro = TxtFiltro.Text?.Trim() ?? "";
            if (!forzar && !string.IsNullOrEmpty(filtro)) return;

            _refrescando = true;
            try
            {
                var cuentas = await Task.Run(() => string.IsNullOrEmpty(filtro)
                    ? _controlador.ObtenerCuentasPorSucursal(0)
                    : _controlador.BuscarCuentasPorCliente(filtro));

                int verdes = cuentas.Count(c => c.EstadoMashup != "ROJO");
                int rojas = cuentas.Count(c => c.EstadoMashup == "ROJO");

                LblKpiVerdes.Text = $"VERDE (Libres): {verdes}";
                LblKpiRojas.Text = $"ROJO (Ocupadas): {rojas}";
                ListaCuentas.ItemsSource = cuentas;
            }
            finally
            {
                _refrescando = false;
            }
        }

        private async Task CargarExtractoAsync(string cta)
        {
            LblTituloExtracto.Text = $"Extracto Reciente — {cta}";
            var movs = await Task.Run(() => _controlador.ObtenerExtracto(cta));
            ListaExtracto.ItemsSource = movs.Take(12).ToList();
        }

        private async void OnCuentaSeleccionada(object? sender, SelectionChangedEventArgs e)
        {
            if (e.CurrentSelection.FirstOrDefault() is CuentaModel c)
            {
                TxtCuenta.Text = c.NumeroCuenta;
                await CargarExtractoAsync(c.NumeroCuenta);
            }
        }

        private async void OnBuscarClicked(object? sender, EventArgs e) =>
            await RefrescarMashupSilenciosoAsync(forzar: true);

        private async void OnLimpiarFiltroClicked(object? sender, EventArgs e)
        {
            TxtFiltro.Text = "";
            await RefrescarMashupSilenciosoAsync(forzar: true);
        }

        private async void OnDepositarClicked(object? sender, EventArgs e) =>
            await EjecutarTransaccionAsync("DEPOSITO");

        private async void OnRetirarClicked(object? sender, EventArgs e) =>
            await EjecutarTransaccionAsync("RETIRO");

        private async void OnTransferirClicked(object? sender, EventArgs e) =>
            await EjecutarTransaccionAsync("TRANSFERENCIA");

        private async Task EjecutarTransaccionAsync(string tipo)
        {
            string cta = TxtCuenta.Text?.Trim() ?? "";
            string dest = TxtDestino.Text?.Trim() ?? "";
            if (!double.TryParse((TxtMonto.Text ?? "").Trim().Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out double monto))
            {
                MostrarMensaje(false, "Ingrese un monto numérico válido.");
                return;
            }

            MostrarMensaje(true, $"Procesando {tipo} en Ventanilla {_controlador.VentanillaActiva}... Semáforo en ROJO.");

            var tarea = Task.Run(() => tipo switch
            {
                "DEPOSITO" => _controlador.Depositar(cta, monto),
                "RETIRO" => _controlador.Retirar(cta, monto),
                _ => _controlador.Transferir(cta, dest, monto)
            });

            await Task.Delay(300);
            await RefrescarMashupSilenciosoAsync(forzar: true);

            var res = await tarea;
            MostrarMensaje(res.Exito, res.Mensaje);
            await RefrescarTodoAsync();
        }

        private async void OnSimular3Clicked(object? sender, EventArgs e)
        {
            string cta = TxtCuenta.Text?.Trim() ?? "001-0001";
            MostrarMensaje(true, $"Simulando 3 ventanillas simultáneas sobre {cta}...");

            var t1 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 25.00, 1));
            await Task.Delay(300);
            await RefrescarMashupSilenciosoAsync(forzar: true);
            var t2 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 15.00, 2));
            var t3 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 10.00, 3));

            await Task.WhenAll(t1, t2, t3);
            MostrarMensaje(true, $"V1: {(t1.Result.Exito ? "OK" : t1.Result.Codigo)} | V2: {t2.Result.Codigo} | V3: {t3.Result.Codigo}");
            await RefrescarTodoAsync();
        }

        private async void OnBloquearClicked(object? sender, EventArgs e)
        {
            string cta = TxtCuenta.Text?.Trim() ?? "";
            var res = await Task.Run(() => _controlador.BloquearManual(cta));
            MostrarMensaje(res.Exito, res.Mensaje);
            await RefrescarMashupSilenciosoAsync(forzar: true);
        }

        private async void OnDesbloquearClicked(object? sender, EventArgs e)
        {
            string cta = TxtCuenta.Text?.Trim() ?? "";
            var res = await Task.Run(() => _controlador.Desbloquear(cta));
            MostrarMensaje(res.Exito, res.Mensaje);
            await RefrescarMashupSilenciosoAsync(forzar: true);
        }

        private void MostrarMensaje(bool ok, string msg)
        {
            LblMensajeOp.IsVisible = true;
            LblMensajeOp.BackgroundColor = ok ? Color.FromArgb("#DCFCE7") : Color.FromArgb("#FEE2E2");
            LblMensajeOp.TextColor = ok ? Color.FromArgb("#166534") : Color.FromArgb("#991B1B");
            LblMensajeOp.Text = msg;
        }
    }
}
