using System.Drawing;
using System.Globalization;
using System.IO;
using System.Windows.Forms;
using ec.edu.monster.controller;
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public class DashboardView : Form
    {
        private readonly AppControlador _controlador;
        private readonly System.Windows.Forms.Timer _timerMashup;
        private bool _refrescando;

        private readonly Label _lblKpiVerde = new() { Text = "● Cuentas VERDE (Libres): 0", AutoSize = false, Size = new Size(340, 34), BackColor = Color.FromArgb(220, 252, 231), ForeColor = Color.FromArgb(22, 101, 52), Font = new Font("Segoe UI", 10f, FontStyle.Bold), TextAlign = ContentAlignment.MiddleLeft, Padding = new Padding(10, 0, 0, 0) };
        private readonly Label _lblKpiRojo = new() { Text = "● Cuentas ROJO (Ocupadas en Ventanilla): 0", AutoSize = false, Size = new Size(380, 34), BackColor = Color.FromArgb(254, 226, 226), ForeColor = Color.FromArgb(153, 27, 27), Font = new Font("Segoe UI", 10f, FontStyle.Bold), TextAlign = ContentAlignment.MiddleLeft, Padding = new Padding(10, 0, 0, 0) };
        private readonly ComboBox _cmbSucursal = new() { DropDownStyle = ComboBoxStyle.DropDownList, Width = 240, Font = new Font("Segoe UI", 9.5f) };
        private readonly TextBox _txtFiltro = new() { Width = 170, PlaceholderText = "Cuenta o DNI...", Font = new Font("Segoe UI", 9.5f) };
        private readonly ComboBox _cmbVentanilla = new() { DropDownStyle = ComboBoxStyle.DropDownList, Width = 135, Font = new Font("Segoe UI", 9.5f) };

        private readonly DataGridView _gridCuentas = new()
        {
            Dock = DockStyle.Fill,
            ReadOnly = true,
            AllowUserToAddRows = false,
            AutoGenerateColumns = false,
            SelectionMode = DataGridViewSelectionMode.FullRowSelect,
            MultiSelect = false,
            AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill,
            BackgroundColor = Color.White,
            RowHeadersVisible = false
        };

        private readonly DataGridView _gridExtracto = new()
        {
            Dock = DockStyle.Fill,
            ReadOnly = true,
            AllowUserToAddRows = false,
            AutoGenerateColumns = false,
            SelectionMode = DataGridViewSelectionMode.FullRowSelect,
            AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill,
            BackgroundColor = Color.White,
            RowHeadersVisible = false
        };

        private readonly TextBox _txtCuentaPrincipal = new() { Text = "001-0001", Width = 320, Font = new Font("Segoe UI", 10.5f, FontStyle.Bold) };
        private readonly TextBox _txtCuentaDestino = new() { Text = "001-0002", Width = 320, Font = new Font("Segoe UI", 10f) };
        private readonly TextBox _txtMonto = new() { Text = "50.00", Width = 320, Font = new Font("Segoe UI", 10.5f, FontStyle.Bold) };
        private readonly Label _lblResultado = new() { AutoSize = false, Size = new Size(320, 88), BackColor = Color.FromArgb(241, 245, 249), ForeColor = Color.FromArgb(15, 23, 42), Font = new Font("Segoe UI", 9f, FontStyle.Bold), Padding = new Padding(8) };

        private List<SucursalModel> _sucursales = new();

        public DashboardView(AppControlador controlador)
        {
            _controlador = controlador;
            Text = $"EurekaBank Estación de Ventanilla (SOAP .NET) — Operador: {_controlador.SesionActual?.NombreCompleto} | ec.edu.monster";
            Size = new Size(1320, 780);
            StartPosition = FormStartPosition.CenterScreen;
            BackColor = Color.FromArgb(241, 245, 249);

            ConstruirInterfaz();

            _timerMashup = new System.Windows.Forms.Timer { Interval = 1200 };
            _timerMashup.Tick += async (_, _) => await RefrescarCuentasAsync(silencioso: true);
            Load += async (_, _) =>
            {
                ConfigurarColumnasCuentas();
                ConfigurarColumnasExtracto();
                await CargarCatalogosInicialesAsync();
                _timerMashup.Start();
            };
            FormClosing += (_, _) => _timerMashup.Stop();
        }

        private void ConstruirInterfaz()
        {
            var topBar = new Panel { Dock = DockStyle.Top, Height = 64, BackColor = Color.FromArgb(15, 23, 42) };
            var pic = new PictureBox { Size = new Size(44, 44), Location = new Point(18, 10), SizeMode = PictureBoxSizeMode.Zoom };
            string imgPath = Path.Combine(AppContext.BaseDirectory, "images", "sullivan.png");
            if (File.Exists(imgPath)) pic.Image = Image.FromFile(imgPath);

            var lblHeader = new Label
            {
                Text = "EUREKABANK — TERMINAL DE CAJERO & MONITOR MASHUP EN VIVO (SOAP .NET)",
                ForeColor = Color.FromArgb(56, 189, 248),
                Font = new Font("Segoe UI", 12f, FontStyle.Bold),
                Location = new Point(74, 10),
                AutoSize = true
            };
            var lblOperador = new Label
            {
                Text = $"Operador: {_controlador.SesionActual?.NombreCompleto} ({_controlador.SesionActual?.Rol}) | Sincronización automática: 1.2s",
                ForeColor = Color.FromArgb(203, 213, 225),
                Font = new Font("Segoe UI", 9f),
                Location = new Point(74, 35),
                AutoSize = true
            };

            _cmbVentanilla.Items.AddRange(new object[] { "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4" });
            _cmbVentanilla.SelectedIndex = Math.Clamp(_controlador.VentanillaActiva - 1, 0, 3);
            _cmbVentanilla.Location = new Point(1010, 18);
            _cmbVentanilla.SelectedIndexChanged += (_, _) => _controlador.VentanillaActiva = _cmbVentanilla.SelectedIndex + 1;

            var btnSalir = CrearBoton("Cerrar Sesión", Color.FromArgb(71, 85, 105), 115, 32);
            btnSalir.Location = new Point(1160, 16);
            btnSalir.Click += (_, _) => Close();

            topBar.Controls.AddRange(new Control[] { pic, lblHeader, lblOperador, _cmbVentanilla, btnSalir });

            // Panel izquierdo: Semáforo MashUp + Extracto
            var panelIzquierdo = new Panel { Location = new Point(16, 78), Size = new Size(900, 648), BackColor = Color.Transparent };

            var cardMashup = new Panel { Location = new Point(0, 0), Size = new Size(900, 390), BackColor = Color.White, BorderStyle = BorderStyle.FixedSingle };
            _lblKpiVerde.Location = new Point(14, 12);
            _lblKpiRojo.Location = new Point(366, 12);

            _cmbSucursal.Location = new Point(14, 56);
            _cmbSucursal.SelectedIndexChanged += async (_, _) => await RefrescarCuentasAsync(silencioso: false);
            _txtFiltro.Location = new Point(264, 56);

            var btnBuscar = CrearBoton("Buscar", Color.FromArgb(37, 99, 235), 85, 28);
            btnBuscar.Location = new Point(442, 55);
            btnBuscar.Click += async (_, _) => await BuscarCuentasFiltroAsync();

            var btnLimpiar = CrearBoton("Ver Todas", Color.FromArgb(100, 116, 139), 95, 28);
            btnLimpiar.Location = new Point(534, 55);
            btnLimpiar.Click += async (_, _) => { _txtFiltro.Text = ""; _cmbSucursal.SelectedIndex = 0; await RefrescarCuentasAsync(false); };

            var panelGridCuentas = new Panel { Location = new Point(14, 92), Size = new Size(870, 284) };
            panelGridCuentas.Controls.Add(_gridCuentas);
            ConfigurarColumnasCuentas();

            _gridCuentas.CellClick += async (_, e) =>
            {
                if (e.RowIndex >= 0 && _gridCuentas.Rows[e.RowIndex].Cells["NumeroCuenta"].Value is string nro)
                {
                    _txtCuentaPrincipal.Text = nro;
                    await CargarExtractoAsync(nro);
                }
            };
            cardMashup.Controls.AddRange(new Control[] { _lblKpiVerde, _lblKpiRojo, _cmbSucursal, _txtFiltro, btnBuscar, btnLimpiar, panelGridCuentas });

            var cardExtracto = new Panel { Location = new Point(0, 402), Size = new Size(900, 244), BackColor = Color.White, BorderStyle = BorderStyle.FixedSingle };
            var lblExt = new Label { Text = "Extracto Cronológico de Movimientos (Click en una cuenta arriba para inspeccionar)", Font = new Font("Segoe UI", 10f, FontStyle.Bold), Location = new Point(14, 10), AutoSize = true };
            var panelGridExt = new Panel { Location = new Point(14, 36), Size = new Size(870, 196) };
            panelGridExt.Controls.Add(_gridExtracto);
            ConfigurarColumnasExtracto();
            cardExtracto.Controls.AddRange(new Control[] { lblExt, panelGridExt });

            panelIzquierdo.Controls.AddRange(new Control[] { cardMashup, cardExtracto });

            // Panel derecho: Operaciones Transaccionales
            var cardOps = new Panel { Location = new Point(930, 78), Size = new Size(358, 648), BackColor = Color.White, BorderStyle = BorderStyle.FixedSingle };
            int y = 14;
            cardOps.Controls.Add(new Label { Text = "Operaciones Transaccionales ACID", Font = new Font("Segoe UI", 11f, FontStyle.Bold), Location = new Point(18, y), AutoSize = true });

            y += 34;
            cardOps.Controls.Add(new Label { Text = "Cuenta Principal / Origen:", Font = new Font("Segoe UI", 9f, FontStyle.Bold), Location = new Point(18, y), AutoSize = true });
            _txtCuentaPrincipal.Location = new Point(18, y + 20);
            cardOps.Controls.Add(_txtCuentaPrincipal);

            y += 56;
            cardOps.Controls.Add(new Label { Text = "Cuenta Destino (solo Transferencia):", Font = new Font("Segoe UI", 9f, FontStyle.Bold), Location = new Point(18, y), AutoSize = true });
            _txtCuentaDestino.Location = new Point(18, y + 20);
            cardOps.Controls.Add(_txtCuentaDestino);

            y += 56;
            cardOps.Controls.Add(new Label { Text = "Importe USD ($):", Font = new Font("Segoe UI", 9f, FontStyle.Bold), Location = new Point(18, y), AutoSize = true });
            _txtMonto.Location = new Point(18, y + 20);
            cardOps.Controls.Add(_txtMonto);

            y += 60;
            var btnDep = CrearBoton("ACREDITAR DEPÓSITO (+)", Color.FromArgb(22, 163, 74), 320, 38);
            btnDep.Location = new Point(18, y);
            btnDep.Click += async (_, _) => await EjecutarTransaccionAsync("DEPOSITO");

            y += 46;
            var btnRet = CrearBoton("PROCESAR RETIRO (-)", Color.FromArgb(220, 38, 38), 320, 38);
            btnRet.Location = new Point(18, y);
            btnRet.Click += async (_, _) => await EjecutarTransaccionAsync("RETIRO");

            y += 46;
            var btnTrf = CrearBoton("EJECUTAR TRANSFERENCIA (⇄)", Color.FromArgb(79, 70, 229), 320, 38);
            btnTrf.Location = new Point(18, y);
            btnTrf.Click += async (_, _) => await EjecutarTransaccionAsync("TRANSFERENCIA");

            y += 52;
            var btnSim = CrearBoton("⚡ SIMULAR 3 VENTANILLAS SIMULTÁNEAS", Color.FromArgb(217, 119, 6), 320, 38);
            btnSim.Location = new Point(18, y);
            btnSim.Click += async (_, _) => await SimularTresVentanillasAsync();

            y += 46;
            var btnLock = CrearBoton("🔒 Retener ROJO", Color.FromArgb(185, 28, 28), 155, 34);
            btnLock.Location = new Point(18, y);
            btnLock.Click += async (_, _) => await EjecutarBloqueoManualAsync(true);

            var btnUnlock = CrearBoton("🔓 Liberar VERDE", Color.FromArgb(13, 148, 136), 155, 34);
            btnUnlock.Location = new Point(183, y);
            btnUnlock.Click += async (_, _) => await EjecutarBloqueoManualAsync(false);

            y += 46;
            _lblResultado.Location = new Point(18, y);
            _lblResultado.Text = "Listo para operar. Al iniciar una transacción verá el semáforo cambiar a ROJO en tiempo real.";

            cardOps.Controls.AddRange(new Control[] { btnDep, btnRet, btnTrf, btnSim, btnLock, btnUnlock, _lblResultado });

            Controls.AddRange(new Control[] { topBar, panelIzquierdo, cardOps });
        }

        private void ConfigurarColumnasCuentas()
        {
            if (_gridCuentas.Columns.Count >= 6) return;
            _gridCuentas.AutoGenerateColumns = false;
            _gridCuentas.Columns.Clear();
            _gridCuentas.Columns.Add("NumeroCuenta", "Cuenta");
            _gridCuentas.Columns.Add("Titular", "Titular");
            _gridCuentas.Columns.Add("Dni", "DNI");
            _gridCuentas.Columns.Add("Sucursal", "Sucursal");
            _gridCuentas.Columns.Add("Saldo", "Saldo USD");
            _gridCuentas.Columns.Add("Semaforo", "Semáforo MashUp");
        }

        private void ConfigurarColumnasExtracto()
        {
            if (_gridExtracto.Columns.Count >= 6) return;
            _gridExtracto.AutoGenerateColumns = false;
            _gridExtracto.Columns.Clear();
            _gridExtracto.Columns.Add("Fecha", "Fecha / Hora");
            _gridExtracto.Columns.Add("Operacion", "N° Operación");
            _gridExtracto.Columns.Add("Tipo", "Tipo");
            _gridExtracto.Columns.Add("Ventanilla", "Vent.");
            _gridExtracto.Columns.Add("Monto", "Monto");
            _gridExtracto.Columns.Add("Saldo", "Saldo Final");
        }

        private static Button CrearBoton(string texto, Color color, int w, int h)
        {
            var b = new Button
            {
                Text = texto,
                Size = new Size(w, h),
                BackColor = color,
                ForeColor = Color.White,
                FlatStyle = FlatStyle.Flat,
                Font = new Font("Segoe UI", 9f, FontStyle.Bold),
                Cursor = Cursors.Hand
            };
            b.FlatAppearance.BorderSize = 0;
            return b;
        }

        private async Task CargarCatalogosInicialesAsync()
        {
            _sucursales = await Task.Run(() => _controlador.ObtenerSucursales());
            _cmbSucursal.Items.Clear();
            _cmbSucursal.Items.Add("0 - Todas las Sucursales");
            foreach (var s in _sucursales)
                _cmbSucursal.Items.Add($"{s.IdSucursal} - {s.Nombre} ({s.Ciudad})");
            _cmbSucursal.SelectedIndex = 0;

            await RefrescarCuentasAsync(false);
            await CargarExtractoAsync(_txtCuentaPrincipal.Text.Trim());
        }

        private int ObtenerSucursalFiltro()
        {
            if (_cmbSucursal.SelectedIndex <= 0) return 0;
            int idx = _cmbSucursal.SelectedIndex - 1;
            return idx < _sucursales.Count ? _sucursales[idx].IdSucursal : 0;
        }

        private async Task RefrescarCuentasAsync(bool silencioso)
        {
            if (_refrescando) return;
            if (!string.IsNullOrWhiteSpace(_txtFiltro.Text) && silencioso) return;

            _refrescando = true;
            try
            {
                int sucId = ObtenerSucursalFiltro();
                var cuentas = await Task.Run(() => _controlador.ObtenerCuentasPorSucursal(sucId));
                PintarCuentas(cuentas);
            }
            finally
            {
                _refrescando = false;
            }
        }

        private async Task BuscarCuentasFiltroAsync()
        {
            string q = _txtFiltro.Text.Trim();
            if (string.IsNullOrEmpty(q))
            {
                await RefrescarCuentasAsync(false);
                return;
            }
            var cuentas = await Task.Run(() => _controlador.BuscarCuentasPorCliente(q));
            PintarCuentas(cuentas);
        }

        private void PintarCuentas(List<CuentaModel> cuentas)
        {
            if (InvokeRequired)
            {
                Invoke(new Action(() => PintarCuentas(cuentas)));
                return;
            }

            if (_gridCuentas.Columns.Count == 0)
            {
                ConfigurarColumnasCuentas();
            }

            string? sel = null;
            try
            {
                if (_gridCuentas.CurrentRow != null && _gridCuentas.Columns.Contains("NumeroCuenta"))
                {
                    sel = _gridCuentas.CurrentRow.Cells["NumeroCuenta"].Value?.ToString();
                }
            }
            catch { }

            _gridCuentas.Rows.Clear();

            int verdes = 0, rojas = 0;
            foreach (var c in cuentas)
            {
                bool esRojo = string.Equals(c.EstadoMashup, "ROJO", StringComparison.OrdinalIgnoreCase);
                if (esRojo) rojas++; else verdes++;

                int idx = _gridCuentas.Rows.Add(
                    c.NumeroCuenta,
                    c.TitularCompleto,
                    c.DniCliente,
                    c.NombreSucursal,
                    $"${c.Saldo:F2}",
                    esRojo ? $"🔴 ROJO (Vent. {c.VentanillaOcupada} - {c.BloqueadoPor})" : "🟢 VERDE (Libre)");

                if (idx >= 0 && idx < _gridCuentas.Rows.Count)
                {
                    var fila = _gridCuentas.Rows[idx];
                    fila.DefaultCellStyle.BackColor = esRojo ? Color.FromArgb(254, 226, 226) : Color.FromArgb(240, 253, 244);
                    fila.DefaultCellStyle.ForeColor = esRojo ? Color.FromArgb(153, 27, 27) : Color.FromArgb(20, 83, 45);
                    if (sel != null && sel == c.NumeroCuenta) fila.Selected = true;
                }
            }

            _lblKpiVerde.Text = $"● Cuentas VERDE (Libres): {verdes}";
            _lblKpiRojo.Text = $"● Cuentas ROJO (Ocupadas en Ventanilla): {rojas}";
        }

        private async Task CargarExtractoAsync(string numeroCuenta)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta)) return;
            var movs = await Task.Run(() => _controlador.ObtenerExtracto(numeroCuenta));
            PintarExtracto(movs);
        }

        private void PintarExtracto(List<MovimientoModel> movs)
        {
            if (InvokeRequired)
            {
                Invoke(new Action(() => PintarExtracto(movs)));
                return;
            }

            if (_gridExtracto.Columns.Count == 0)
            {
                ConfigurarColumnasExtracto();
            }

            _gridExtracto.Rows.Clear();
            foreach (var m in movs)
            {
                _gridExtracto.Rows.Add(m.FechaHora, m.NumeroOperacion, m.Tipo, $"V{m.Ventanilla}", $"${m.Monto:F2}", $"${m.SaldoResultante:F2}");
            }
        }

        private async Task EjecutarTransaccionAsync(string tipo)
        {
            string cta = _txtCuentaPrincipal.Text.Trim();
            string dest = _txtCuentaDestino.Text.Trim();
            if (!double.TryParse(_txtMonto.Text.Trim().Replace(',', '.'), NumberStyles.Float, CultureInfo.InvariantCulture, out double monto))
            {
                MostrarMensaje(false, "Ingrese un monto numérico válido.");
                return;
            }

            MostrarMensaje(true, $"Procesando {tipo} en Ventanilla {_controlador.VentanillaActiva}... Observe el semáforo cambiar a ROJO.");

            var tareaTx = Task.Run(() => tipo switch
            {
                "DEPOSITO" => _controlador.Depositar(cta, monto),
                "RETIRO" => _controlador.Retirar(cta, monto),
                _ => _controlador.Transferir(cta, dest, monto)
            });

            // Refresco inmediato tras 300ms para evidenciar estado ROJO en la misma pantalla
            await Task.Delay(300);
            await RefrescarCuentasAsync(false);

            var res = await tareaTx;
            MostrarMensaje(res.Exito, res.Mensaje);
            await RefrescarCuentasAsync(false);
            await CargarExtractoAsync(cta);
        }

        private async Task SimularTresVentanillasAsync()
        {
            string cta = _txtCuentaPrincipal.Text.Trim();
            MostrarMensaje(true, $"Lanzando 3 cajeros simultáneos sobre {cta}...");

            var t1 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 25.00, 1));
            await Task.Delay(300);
            await RefrescarCuentasAsync(false);
            var t2 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 15.00, 2));
            var t3 = Task.Run(() => _controlador.DepositarEnVentanilla(cta, 10.00, 3));

            await Task.WhenAll(t1, t2, t3);
            MostrarMensaje(true,
                $"V1: {(t1.Result.Exito ? "OK" : t1.Result.Codigo)} | V2: {(t2.Result.Exito ? "OK" : t2.Result.Codigo)} | V3: {(t3.Result.Exito ? "OK" : t3.Result.Codigo)}");
            await RefrescarCuentasAsync(false);
            await CargarExtractoAsync(cta);
        }

        private async Task EjecutarBloqueoManualAsync(bool bloquear)
        {
            string cta = _txtCuentaPrincipal.Text.Trim();
            var res = await Task.Run(() => bloquear ? _controlador.BloquearCuentaManual(cta) : _controlador.DesbloquearCuenta(cta));
            MostrarMensaje(res.Exito, res.Mensaje);
            await RefrescarCuentasAsync(false);
        }

        private void MostrarMensaje(bool exito, string msg)
        {
            if (InvokeRequired)
            {
                Invoke(new Action(() => MostrarMensaje(exito, msg)));
                return;
            }
            _lblResultado.BackColor = exito ? Color.FromArgb(220, 252, 231) : Color.FromArgb(254, 226, 226);
            _lblResultado.ForeColor = exito ? Color.FromArgb(22, 101, 52) : Color.FromArgb(153, 27, 27);
            _lblResultado.Text = msg;
        }
    }
}
