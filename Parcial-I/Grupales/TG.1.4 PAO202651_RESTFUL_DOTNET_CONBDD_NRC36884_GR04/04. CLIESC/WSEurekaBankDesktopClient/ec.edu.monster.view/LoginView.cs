using System.Drawing;
using System.IO;
using System.Windows.Forms;
using ec.edu.monster.controller;

namespace ec.edu.monster.view
{
    public class LoginView : Form
    {
        private readonly AppControlador _controlador = new();
        private readonly TextBox _txtUsuario = new() { Text = "Monster", Width = 280, Font = new Font("Segoe UI", 10.5f) };
        private readonly TextBox _txtClave = new() { Text = "Monster9", Width = 280, UseSystemPasswordChar = true, Font = new Font("Segoe UI", 10.5f) };
        private readonly ComboBox _cmbVentanilla = new() { DropDownStyle = ComboBoxStyle.DropDownList, Width = 280, Font = new Font("Segoe UI", 10f) };
        private readonly TextBox _txtEndpoint = new() { Width = 280, Font = new Font("Segoe UI", 9f) };
        private readonly Label _lblEstado = new() { AutoSize = false, Width = 320, Height = 44, ForeColor = Color.FromArgb(185, 28, 28), Font = new Font("Segoe UI", 9f, FontStyle.Bold), TextAlign = ContentAlignment.MiddleCenter };
        private readonly Button _btnIngresar = new() { Text = "INGRESAR A ESTACIÃ“N BANCARIA", Width = 280, Height = 40, BackColor = Color.FromArgb(37, 99, 235), ForeColor = Color.White, FlatStyle = FlatStyle.Flat, Font = new Font("Segoe UI", 10f, FontStyle.Bold), Cursor = Cursors.Hand };

        public LoginView()
        {
            Text = "EurekaBank RESTful .NET - AutenticaciÃ³n de Ventanilla (GR04)";
            Size = new Size(440, 560);
            StartPosition = FormStartPosition.CenterScreen;
            FormBorderStyle = FormBorderStyle.FixedSingle;
            MaximizeBox = false;
            BackColor = Color.FromArgb(15, 23, 42);

            _txtEndpoint.Text = _controlador.EndpointActual;
            _cmbVentanilla.Items.AddRange(new object[] { "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4" });
            _cmbVentanilla.SelectedIndex = 0;

            var card = new Panel
            {
                Size = new Size(360, 470),
                Location = new Point(32, 26),
                BackColor = Color.White
            };

            var pic = new PictureBox
            {
                Size = new Size(88, 88),
                Location = new Point(136, 16),
                SizeMode = PictureBoxSizeMode.Zoom
            };
            string imgPath = Path.Combine(AppContext.BaseDirectory, "images", "sullivan.png");
            if (File.Exists(imgPath)) pic.Image = Image.FromFile(imgPath);

            var lblTitulo = new Label
            {
                Text = "EUREKABANK â€” RESTful .NET",
                Font = new Font("Segoe UI", 13f, FontStyle.Bold),
                ForeColor = Color.FromArgb(15, 23, 42),
                Location = new Point(20, 110),
                Size = new Size(320, 26),
                TextAlign = ContentAlignment.MiddleCenter
            };
            var lblSub = new Label
            {
                Text = "PatrÃ³n MVC Tradicional | ec.edu.monster",
                Font = new Font("Segoe UI", 8.5f),
                ForeColor = Color.FromArgb(100, 116, 139),
                Location = new Point(20, 136),
                Size = new Size(320, 18),
                TextAlign = ContentAlignment.MiddleCenter
            };

            int y = 166;
            card.Controls.Add(CrearEtiqueta("Usuario Operador:", 40, y));
            _txtUsuario.Location = new Point(40, y + 20);
            card.Controls.Add(_txtUsuario);

            y += 58;
            card.Controls.Add(CrearEtiqueta("ContraseÃ±a:", 40, y));
            _txtClave.Location = new Point(40, y + 20);
            card.Controls.Add(_txtClave);

            y += 58;
            card.Controls.Add(CrearEtiqueta("EstaciÃ³n / Ventanilla FÃ­sica:", 40, y));
            _cmbVentanilla.Location = new Point(40, y + 20);
            card.Controls.Add(_cmbVentanilla);

            y += 58;
            card.Controls.Add(CrearEtiqueta("Endpoint Servidor RESTful (.NET):", 40, y));
            _txtEndpoint.Location = new Point(40, y + 20);
            card.Controls.Add(_txtEndpoint);

            _btnIngresar.Location = new Point(40, y + 58);
            _btnIngresar.FlatAppearance.BorderSize = 0;
            _btnIngresar.Click += async (_, _) => await ProcesarLoginAsync();

            _lblEstado.Location = new Point(20, y + 102);

            card.Controls.AddRange(new Control[] { pic, lblTitulo, lblSub, _btnIngresar, _lblEstado });
            Controls.Add(card);
            AcceptButton = _btnIngresar;
        }

        private static Label CrearEtiqueta(string texto, int x, int y) => new()
        {
            Text = texto,
            Location = new Point(x, y),
            Size = new Size(280, 18),
            Font = new Font("Segoe UI", 9f, FontStyle.Bold),
            ForeColor = Color.FromArgb(51, 65, 85)
        };

        private async Task ProcesarLoginAsync()
        {
            _btnIngresar.Enabled = false;
            _lblEstado.ForeColor = Color.FromArgb(37, 99, 235);
            _lblEstado.Text = "Verificando credenciales con el Servidor RESTful .NET...";
            _controlador.EndpointActual = _txtEndpoint.Text.Trim();

            string usr = _txtUsuario.Text.Trim();
            string pwd = _txtClave.Text;
            int vent = _cmbVentanilla.SelectedIndex + 1;

            var res = await Task.Run(() => _controlador.IniciarSesion(usr, pwd));
            _btnIngresar.Enabled = true;

            if (res.Autenticado)
            {
                _controlador.VentanillaActiva = vent;
                var dash = new DashboardView(_controlador);
                dash.FormClosed += (_, _) => Show();
                Hide();
                dash.Show();
            }
            else
            {
                _lblEstado.ForeColor = Color.FromArgb(185, 28, 28);
                _lblEstado.Text = res.Mensaje;
            }
        }
    }
}

