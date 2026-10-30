using ec.edu.monster.controller;
using Microsoft.Maui.Controls;

namespace ec.edu.monster.view
{
    public partial class LoginView : ContentPage
    {
        private readonly AppControlador _controlador = new();

        public LoginView()
        {
            InitializeComponent();
            PckVentanilla.SelectedIndex = 0;
            TxtEndpoint.Text = _controlador.EndpointActual;
        }

        private async void OnIngresarClicked(object? sender, EventArgs e)
        {
            BtnIngresar.IsEnabled = false;
            LblEstado.TextColor = Color.FromArgb("#2563EB");
            LblEstado.Text = "Autenticando con Servidor RESTful .NET...";
            _controlador.EndpointActual = TxtEndpoint.Text?.Trim() ?? _controlador.EndpointActual;

            string usr = TxtUsuario.Text?.Trim() ?? "";
            string pwd = TxtClave.Text ?? "";
            int vent = PckVentanilla.SelectedIndex >= 0 ? PckVentanilla.SelectedIndex + 1 : 1;

            var res = await Task.Run(() => _controlador.IniciarSesion(usr, pwd));
            BtnIngresar.IsEnabled = true;

            if (res.Autenticado)
            {
                _controlador.VentanillaActiva = vent;
                LblEstado.Text = "";
                await Navigation.PushAsync(new DashboardView(_controlador));
            }
            else
            {
                LblEstado.TextColor = Color.FromArgb("#DC2626");
                LblEstado.Text = res.Mensaje;
            }
        }
    }
}

