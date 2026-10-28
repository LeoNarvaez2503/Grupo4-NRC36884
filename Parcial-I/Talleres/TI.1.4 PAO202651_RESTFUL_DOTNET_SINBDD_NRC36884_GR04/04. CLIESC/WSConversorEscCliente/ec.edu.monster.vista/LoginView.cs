using System;
using System.Drawing;
using System.IO;
using System.Net.Http;
using System.Threading.Tasks;
using System.Windows.Forms;
using ec.edu.monster.controlador;

namespace ec.edu.monster.vista
{
    public partial class LoginView : Form
    {
        public LoginView()
        {
            InitializeComponent();
            CargarImagenSulley();
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

        private void chkMostrar_CheckedChanged(object? sender, EventArgs e)
        {
            txtPassword.PasswordChar = chkMostrar.Checked ? '\0' : '●';
        }

        private async void btnIngresar_Click(object? sender, EventArgs e)
        {
            string user = txtUsuario.Text != null ? txtUsuario.Text.Trim() : "";
            string pass = txtPassword.Text != null ? txtPassword.Text : "";

            if (string.IsNullOrEmpty(user) || string.IsNullOrEmpty(pass))
            {
                MessageBox.Show(
                    "Por favor escribe el usuario y la contraseña para continuar.",
                    "Campos Requeridos",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
                return;
            }

            btnIngresar.Enabled = false;
            btnLimpiar.Enabled = false;
            Cursor = Cursors.WaitCursor;

            try
            {
                AppControlador control = new AppControlador();
                string? token = await Task.Run(() => control.Login(user, pass));

                if (!string.IsNullOrWhiteSpace(token))
                {
                    MessageBox.Show(
                        $"¡Bienvenido, {user}!\nInicio de sesión RESTful exitoso.",
                        "Acceso Concedido",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Information);

                    ConversorView vistaPrincipal = new ConversorView();
                    vistaPrincipal.Show();
                    this.Hide();
                }
                else
                {
                    MessageBox.Show(
                        "Usuario o contraseña incorrectos.\n\nVerifica tus credenciales e intenta nuevamente.\n(Credenciales por defecto: Monster / Monster9)",
                        "Error de Autenticación",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
            }
            catch (HttpRequestException)
            {
                MessageBox.Show(
                    "No se pudo conectar con el servidor web RESTful.\n\n" +
                    "Asegúrate de que el servidor esté iniciado ejecutando 'IniciarServidor.bat' en la carpeta '04. SERVIDOR'.\n" +
                    "URL requerida: " + ec.edu.monster.modelo.ConfiguracionServidor.UrlServicio,
                    "Servidor no Disponible",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            catch (TimeoutException)
            {
                MessageBox.Show(
                    "El servidor tardó demasiado tiempo en responder.\n\nPor favor, verifica que el servicio esté activo e intenta de nuevo.",
                    "Tiempo de Espera Agotado",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
            }
            catch (Exception ex)
            {
                string msg = ex.ToString();
                if (msg.Contains("denegó expresamente") ||
                    msg.Contains("SocketException") ||
                    msg.Contains("ConnectFailure") ||
                    msg.Contains("No connection could be made") ||
                    msg.Contains("refused") ||
                    msg.Contains("denied"))
                {
                    MessageBox.Show(
                        "No se pudo conectar con el servidor web RESTful.\n\n" +
                        "El servidor no está iniciado en " + ec.edu.monster.modelo.ConfiguracionServidor.UrlServicio + ".\n\n" +
                        "Por favor, ejecuta el archivo 'IniciarServidor.bat' en la carpeta 04. SERVIDOR para levantarlo.",
                        "Servidor Desconectado",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
                else
                {
                    MessageBox.Show(
                        "Ocurrió un error al intentar iniciar sesión:\n\n" + ex.Message,
                        "Error de Conexión",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
            }
            finally
            {
                btnIngresar.Enabled = true;
                btnLimpiar.Enabled = true;
                Cursor = Cursors.Default;
            }
        }

        private void btnLimpiar_Click(object? sender, EventArgs e)
        {
            txtUsuario.Text = "";
            txtPassword.Text = "";
            txtUsuario.Focus();
        }

        private void txtUsuario_KeyDown(object? sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                txtPassword.Focus();
                e.Handled = true;
                e.SuppressKeyPress = true;
            }
        }

        private void txtPassword_KeyDown(object? sender, KeyEventArgs e)
        {
            if (e.KeyCode == Keys.Enter)
            {
                btnIngresar.PerformClick();
                e.Handled = true;
                e.SuppressKeyPress = true;
            }
        }
    }
}
