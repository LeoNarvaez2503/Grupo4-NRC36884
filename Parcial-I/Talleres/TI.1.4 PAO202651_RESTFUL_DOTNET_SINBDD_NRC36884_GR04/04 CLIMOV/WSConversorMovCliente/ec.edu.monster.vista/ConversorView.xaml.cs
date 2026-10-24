using System;
using System.Globalization;
using System.Net.Http;
using System.Threading.Tasks;
using Microsoft.Maui.Controls;
using ec.edu.monster.controlador;
using ec.edu.monster.modelo;

namespace ec.edu.monster.vista
{
    public partial class ConversorView : ContentPage
    {
        private readonly AppControlador controlador = new AppControlador();

        public ConversorView()
        {
            InitializeComponent();
            cmbMagnitud.SelectedIndex = 0; // LONGITUD por defecto
            ActualizarUnidades();
        }

        private void ActualizarUnidades()
        {
            string? seleccion = cmbMagnitud.SelectedItem as string ?? "LONGITUD";

            string[] unidades;
            switch (seleccion.ToUpperInvariant())
            {
                case "MASA":
                    unidades = Enum.GetNames(typeof(UnidadMasa));
                    lblTitulo.Text = "Conversor de Masa";
                    Title = "Conversor de Masa";
                    break;
                case "TEMPERATURA":
                    unidades = Enum.GetNames(typeof(UnidadTemperatura));
                    lblTitulo.Text = "Conversor de Temperatura";
                    Title = "Conversor de Temperatura";
                    break;
                case "LONGITUD":
                default:
                    unidades = Enum.GetNames(typeof(UnidadLongitud));
                    lblTitulo.Text = "Conversor de Longitudes";
                    Title = "Conversor de Longitudes";
                    break;
            }

            cmbUndInit.ItemsSource = unidades;
            cmbUndFinal.ItemsSource = unidades;

            if (unidades.Length > 0)
            {
                cmbUndInit.SelectedIndex = 0;
                cmbUndFinal.SelectedIndex = unidades.Length > 1 ? 1 : 0;
            }

            lblResultado.Text = "--";
            cardResultado.IsVisible = false;
            lblError.IsVisible = false;
        }

        private void cmbMagnitud_SelectedIndexChanged(object sender, EventArgs e)
        {
            ActualizarUnidades();
        }

        private void btnIntercambiar_Clicked(object sender, EventArgs e)
        {
            int origen = cmbUndInit.SelectedIndex;
            int destino = cmbUndFinal.SelectedIndex;
            cmbUndInit.SelectedIndex = destino;
            cmbUndFinal.SelectedIndex = origen;
        }

        private async void btnConvertir_Clicked(object sender, EventArgs e)
        {
            lblError.IsVisible = false;

            string txtVal = txtValor.Text?.Trim() ?? "";
            if (string.IsNullOrEmpty(txtVal))
            {
                await DisplayAlert("Valor Requerido", "Por favor ingresa un número para realizar la conversión.", "Aceptar");
                txtValor.Focus();
                return;
            }

            string normalizado = txtVal.Replace(',', '.');
            if (!double.TryParse(normalizado, NumberStyles.Float, CultureInfo.InvariantCulture, out double num))
            {
                await DisplayAlert("Formato Inválido", "El valor ingresado no es un número válido.\nPor favor escribe un número (ejemplo: 25 o 12.5).", "Aceptar");
                txtValor.Focus();
                return;
            }

            string unidadInicial = cmbUndInit.SelectedItem as string ?? "";
            string unidadFinal = cmbUndFinal.SelectedItem as string ?? "";
            string tipo = cmbMagnitud.SelectedItem as string ?? "LONGITUD";

            if (string.IsNullOrEmpty(unidadInicial) || string.IsNullOrEmpty(unidadFinal))
            {
                await DisplayAlert("Unidades Requeridas", "Selecciona las unidades de origen y destino.", "Aceptar");
                return;
            }

            SetCargando(true);

            try
            {
                double conversion;
                if ("MASA".Equals(tipo, StringComparison.OrdinalIgnoreCase))
                {
                    conversion = await controlador.ConvertirMasaAsync(num, unidadInicial, unidadFinal);
                }
                else if ("TEMPERATURA".Equals(tipo, StringComparison.OrdinalIgnoreCase))
                {
                    conversion = await controlador.ConvertirTemperaturaAsync(num, unidadInicial, unidadFinal);
                }
                else
                {
                    conversion = await controlador.ConvertirLongitudAsync(num, unidadInicial, unidadFinal);
                }

                lblResultado.Text = conversion.ToString(CultureInfo.InvariantCulture);
                lblDetalleConversion.Text = $"{num} {unidadInicial} = {conversion.ToString(CultureInfo.InvariantCulture)} {unidadFinal}";
                cardResultado.IsVisible = true;
            }
            catch (HttpRequestException)
            {
                string msg = $"Se perdió la conexión con el servidor web RESTful en:\n{ConversorService.EndpointUrl}\n\nVerifica que el servicio siga activo.";
                lblError.Text = msg;
                lblError.IsVisible = true;
                await DisplayAlert("Servidor Desconectado", msg, "Aceptar");
            }
            catch (TimeoutException)
            {
                string msg = "El servidor tardó demasiado tiempo en responder. Verifica la red e intenta de nuevo.";
                lblError.Text = msg;
                lblError.IsVisible = true;
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
                    lblError.Text = msg;
                    lblError.IsVisible = true;
                    await DisplayAlert("Servidor no Disponible", msg, "Aceptar");
                }
                else
                {
                    string msg = $"Ocurrió un error al convertir: {ex.Message}";
                    lblError.Text = msg;
                    lblError.IsVisible = true;
                    await DisplayAlert("Error", msg, "Aceptar");
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
            btnConvertir.IsEnabled = !cargando;
            txtValor.IsEnabled = !cargando;
            cmbMagnitud.IsEnabled = !cargando;
            cmbUndInit.IsEnabled = !cargando;
            cmbUndFinal.IsEnabled = !cargando;
        }

        private async void btnCerrarSesion_Clicked(object sender, EventArgs e)
        {
            bool confirmar = await DisplayAlert("Cerrar Sesión", "¿Estás seguro de que deseas salir?", "Sí, salir", "Cancelar");
            if (confirmar)
            {
                await Navigation.PopToRootAsync();
            }
        }
    }
}
