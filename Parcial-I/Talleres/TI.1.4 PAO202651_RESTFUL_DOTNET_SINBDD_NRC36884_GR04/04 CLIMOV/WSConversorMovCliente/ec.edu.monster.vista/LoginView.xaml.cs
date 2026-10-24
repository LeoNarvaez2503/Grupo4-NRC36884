using System;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Maui.Controls;
using ec.edu.monster.controlador;
using ec.edu.monster.modelo;

namespace ec.edu.monster.vista
{
    public partial class LoginView : ContentPage
    {
        private readonly AppControlador controlador = new AppControlador();

        public LoginView()
        {
            InitializeComponent();
            txtEndpointUrl.Text = ConversorService.EndpointUrl;
        }

        private void btnTogglePass_Clicked(object sender, EventArgs e)
        {
            txtPassword.IsPassword = !txtPassword.IsPassword;
            btnTogglePass.Text = txtPassword.IsPassword ? "👁" : "🔒";
        }

        private async void btnIngresar_Clicked(object sender, EventArgs e)
        {
            string user = txtUsuario.Text?.Trim() ?? "";
            string pass = txtPassword.Text ?? "";
            string endpoint = txtEndpointUrl.Text?.Trim() ?? "";

            lblMensaje.IsVisible = false;

            if (string.IsNullOrEmpty(user) || string.IsNullOrEmpty(pass))
            {
                await DisplayAlert("Campos Requeridos", "Por favor escribe el usuario y la contraseña para continuar.", "Aceptar");
                return;
            }

            if (!string.IsNullOrWhiteSpace(endpoint))
            {
                ConversorService.EndpointUrl = endpoint;
            }

            SetCargando(true);

            try
            {
                string? token = await controlador.LoginAsync(user, pass);

                if (!string.IsNullOrWhiteSpace(token))
                {
                    await DisplayAlert("Acceso Concedido", $"¡Bienvenido, {user}!\nInicio de sesión RESTful exitoso.", "Continuar");

                    // Navegar a la vista del conversor
                    await Navigation.PushAsync(new ConversorView());
                }
                else
                {
                    lblMensaje.Text = "Usuario o contraseña incorrectos.\nVerifica tus credenciales e intenta nuevamente.";
                    lblMensaje.IsVisible = true;
                    await DisplayAlert("Error de Autenticación", "Usuario o contraseña incorrectos.\nVerifica tus credenciales e intenta nuevamente.", "Aceptar");
                }
            }
            catch (HttpRequestException)
            {
                string msg = $"No se pudo conectar con el servidor web RESTful en:\n{ConversorService.EndpointUrl}\n\nAsegúrate de que el servidor esté iniciado y accesible.";
                lblMensaje.Text = msg;
                lblMensaje.IsVisible = true;
                await DisplayAlert("Servidor no Disponible", msg, "Aceptar");
            }
            catch (TimeoutException)
            {
                string msg = "El servidor tardó demasiado tiempo en responder.\nPor favor verifica la conexión e intenta de nuevo.";
                lblMensaje.Text = msg;
                lblMensaje.IsVisible = true;
                await DisplayAlert("Tiempo de Espera Agotado", msg, "Aceptar");
            }
            catch (Exception ex)
            {
                string exStr = ex.ToString();
                if (exStr.Contains("denegó expresamente") ||
                    exStr.Contains("SocketException") ||
                    exStr.Contains("ConnectFailure") ||
                    exStr.Contains("No connection could be made"))
                {
                    string msg = $"No se pudo conectar con el servidor RESTful en:\n{ConversorService.EndpointUrl}\n\nVerifica que el servicio esté ejecutándose.";
                    lblMensaje.Text = msg;
                    lblMensaje.IsVisible = true;
                    await DisplayAlert("Servidor no Disponible", msg, "Aceptar");
                }
                else
                {
                    lblMensaje.Text = $"Error: {ex.Message}";
                    lblMensaje.IsVisible = true;
                    await DisplayAlert("Error", $"Ocurrió un error inesperado:\n{ex.Message}", "Aceptar");
                }
            }
            finally
            {
                SetCargando(false);
            }
        }

        private void SetCargando(bool cargando)
        {
            actCargando.IsRunning = cargando;
            actCargando.IsVisible = cargando;
            btnIngresar.IsEnabled = !cargando;
            txtUsuario.IsEnabled = !cargando;
            txtPassword.IsEnabled = !cargando;
            txtEndpointUrl.IsEnabled = !cargando;
        }

        private void btnLimpiar_Clicked(object sender, EventArgs e)
        {
            txtUsuario.Text = "";
            txtPassword.Text = "";
            lblMensaje.IsVisible = false;
            txtUsuario.Focus();
        }
    }
}
