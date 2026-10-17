using System;
using System.Drawing;
using System.Globalization;
using System.IO;
using System.Windows.Forms;
using ec.edu.monster.controlador;
using ec.edu.monster.modelo;

namespace ec.edu.monster.vista
{
    public partial class ConversorView : Form
    {
        public ConversorView()
        {
            InitializeComponent();
            CargarImagenSulley();
            cmbMagnitud.SelectedIndex = 0; // LONGITUD por defecto
            ActualizarUnidades();
        }

        private void CargarImagenSulley()
        {
            try
            {
                string baseDir = AppDomain.CurrentDomain.BaseDirectory;
                string[] posiblesRutas = new[]
                {
                    Path.Combine(baseDir, "images", "sullivan.png"),
                    Path.Combine(baseDir, "images", "monster_sulley.png"),
                    Path.Combine(baseDir, "..", "..", "..", "images", "sullivan.png"),
                    Path.Combine(baseDir, "..", "..", "..", "images", "monster_sulley.png")
                };

                foreach (string ruta in posiblesRutas)
                {
                    if (File.Exists(ruta))
                    {
                        Image img = Image.FromFile(ruta);
                        picSulley.Image = img;
                        break;
                    }
                }
            }
            catch
            {
                // Si la imagen no está disponible, la interfaz continúa normalmente
            }
        }

        /// <summary>
        /// Actualiza las unidades de los ComboBox según la magnitud seleccionada.
        /// </summary>
        private void ActualizarUnidades()
        {
            string? seleccion = cmbMagnitud.SelectedItem as string ?? "LONGITUD";

            string[] unidades;
            switch (seleccion)
            {
                case "MASA":
                    unidades = Enum.GetNames(typeof(UnidadMasa));
                    lblTitulo.Text = "Conversor de Masa";
                    this.Text = "Conversor de Masa";
                    break;
                case "TEMPERATURA":
                    unidades = Enum.GetNames(typeof(UnidadTemperatura));
                    lblTitulo.Text = "Conversor de Temperatura";
                    this.Text = "Conversor de Temperatura";
                    break;
                case "LONGITUD":
                default:
                    unidades = Enum.GetNames(typeof(UnidadLongitud));
                    lblTitulo.Text = "Conversor de Longitudes";
                    this.Text = "Conversor de Longitudes";
                    break;
            }

            cmbUndInit.Items.Clear();
            cmbUndInit.Items.AddRange(unidades);
            if (cmbUndInit.Items.Count > 0)
            {
                cmbUndInit.SelectedIndex = 0;
            }

            cmbUndFinal.Items.Clear();
            cmbUndFinal.Items.AddRange(unidades);
            if (cmbUndFinal.Items.Count > 0)
            {
                cmbUndFinal.SelectedIndex = cmbUndFinal.Items.Count > 1 ? 1 : 0;
            }

            conversionResultado.Text = "";
        }

        private void cmbMagnitud_SelectedIndexChanged(object? sender, EventArgs e)
        {
            ActualizarUnidades();
        }

        private void btnConvertir_Click(object? sender, EventArgs e)
        {
            string txtVal = txtValor.Text != null ? txtValor.Text.Trim() : "";
            if (string.IsNullOrEmpty(txtVal))
            {
                MessageBox.Show(
                    "Por favor ingresa un número para realizar la conversión.",
                    "Valor Requerido",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
                txtValor.Focus();
                return;
            }

            // Permite punto o coma decimal
            string normalizado = txtVal.Replace(',', '.');
            if (!double.TryParse(normalizado, NumberStyles.Float, CultureInfo.InvariantCulture, out double num))
            {
                MessageBox.Show(
                    "El valor ingresado no es un número válido.\nPor favor escribe un número (ejemplo: 25 o 12.5).",
                    "Formato Inválido",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
                txtValor.SelectAll();
                txtValor.Focus();
                return;
            }

            string unidadInicial = cmbUndInit.SelectedItem as string ?? "";
            string unidadFinal = cmbUndFinal.SelectedItem as string ?? "";
            string tipo = cmbMagnitud.SelectedItem as string ?? "LONGITUD";

            try
            {
                AppControlador control = new AppControlador();
                double conversion;

                if ("MASA".Equals(tipo, StringComparison.OrdinalIgnoreCase))
                {
                    conversion = control.ConvertirMasa(num, unidadInicial, unidadFinal);
                }
                else if ("TEMPERATURA".Equals(tipo, StringComparison.OrdinalIgnoreCase))
                {
                    conversion = control.ConvertirTemperatura(num, unidadInicial, unidadFinal);
                }
                else
                {
                    conversion = control.ConvertirLogintud(num, unidadInicial, unidadFinal);
                }

                conversionResultado.Text = conversion.ToString(CultureInfo.InvariantCulture);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                MessageBox.Show(
                    "Se perdió la conexión con el servidor web SOAP.\n\nVerifica que el servicio siga activo.",
                    "Servidor Desconectado",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            catch (Exception ex)
            {
                if (ex.ToString().Contains("denegó expresamente") ||
                    ex.ToString().Contains("SocketException") ||
                    ex.ToString().Contains("ConnectFailure") ||
                    ex.ToString().Contains("No connection could be made"))
                {
                    MessageBox.Show(
                        "No se pudo conectar con el servidor web SOAP.\n\nVerifica que el servicio esté activo.",
                        "Servidor no Disponible",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
                else
                {
                    MessageBox.Show(
                        "No se pudo realizar la conversión:\n" + ex.Message,
                        "Error en la Conversión",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
            }
        }

        private void txtValor_KeyDown(object? sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                btnConvertir.PerformClick();
                e.Handled = true;
                e.SuppressKeyPress = true;
            }
        }

        private void ConversorView_FormClosed(object? sender, FormClosedEventArgs e)
        {
            Application.Exit();
        }
    }
}
