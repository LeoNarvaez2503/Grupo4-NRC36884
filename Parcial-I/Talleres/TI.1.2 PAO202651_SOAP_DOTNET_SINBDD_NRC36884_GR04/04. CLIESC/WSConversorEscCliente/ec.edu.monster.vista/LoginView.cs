using System;
using System.Drawing;
using System.IO;
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
            txtPassword.PasswordChar = chkMostrar.Checked ? '\0' : '•';
        }

        private void btnIngresar_Click(object? sender, EventArgs e)
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

            try
            {
                AppControlador control = new AppControlador();
                string token = control.Login(user, pass);

                if (!string.IsNullOrWhiteSpace(token))
                {
                    MessageBox.Show(
                        $"¡Bienvenido, {user}!\nInicio de sesión exitoso.",
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
                        "Usuario o contraseña incorrectos.\n\nVerifica tus credenciales e intenta nuevamente.",
                        "Error de Autenticación",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
            }
            catch (System.ServiceModel.FaultException)
            {
                MessageBox.Show(
                    "Usuario o contraseña incorrectos.\n\nVerifica tus credenciales e intenta nuevamente.",
                    "Credenciales Inválidas",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                MessageBox.Show(
                    "No se pudo conectar con el servidor web SOAP.\n\n" +
                    "Por favor, asegúrate de que el servidor esté iniciado.",
                    "Servidor no Disponible",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Error);
            }
            catch (TimeoutException)
            {
                MessageBox.Show(
                    "El servidor tardó demasiado tiempo en responder.\n\nPor favor, verifica tu conexión e intenta de nuevo.",
                    "Tiempo de Espera Agotado",
                    MessageBoxButtons.OK,
                    MessageBoxIcon.Warning);
            }
            catch (Exception ex)
            {
                if (ex.ToString().Contains("denegó expresamente") ||
                    ex.ToString().Contains("SocketException") ||
                    ex.ToString().Contains("ConnectFailure") ||
                    ex.ToString().Contains("No connection could be made"))
                {
                    MessageBox.Show(
                        "No se pudo conectar con el servidor web SOAP.\n\n" +
                        "Por favor, asegúrate de que el servidor esté iniciado.",
                        "Servidor no Disponible",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
                else
                {
                    MessageBox.Show(
                        "Ocurrió un error al intentar iniciar sesión:\n\n" + ex.Message,
                        "Error de Autenticación",
                        MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
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
