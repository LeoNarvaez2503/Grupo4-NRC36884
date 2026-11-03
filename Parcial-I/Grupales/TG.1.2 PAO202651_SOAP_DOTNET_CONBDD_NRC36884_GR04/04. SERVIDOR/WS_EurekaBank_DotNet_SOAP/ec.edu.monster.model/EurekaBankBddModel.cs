using System.Data;
using Microsoft.Data.SqlClient;

namespace ec.edu.monster.model
{
    /// <summary>
    /// Capa Modelo (MVC Tradicional) con persistencia ACID en Microsoft SQL Server 2022 (Docker/Podman),
    /// bloqueo pesimista SELECT ... WITH (UPDLOCK, ROWLOCK) y semáforo cromático MashUp (VERDE / ROJO).
    /// </summary>
    public class EurekaBankBddModel
    {
        private static long _correlativo = DateTimeOffset.UtcNow.ToUnixTimeSeconds() % 100000;
        public static string ConnectionString { get; set; } =
            "Server=127.0.0.1,1433;Database=eurekabank_db;User Id=sa;Password=Monster9#Admin;TrustServerCertificate=True;";
        public static int LockDelayMs { get; set; } = 4500;

        private static string GenerarNumeroOperacion(string prefijo)
        {
            long seq = Interlocked.Increment(ref _correlativo);
            return $"{prefijo}-{DateTimeOffset.UtcNow.ToUnixTimeSeconds()}-{seq}";
        }

        public UsuarioModel AutenticarUsuario(string usuario, string clave)
        {
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
            {
                return new UsuarioModel
                {
                    Autenticado = false,
                    Mensaje = "Debe ingresar usuario y contraseña."
                };
            }

            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    SELECT TOP 1 id_empleado, usuario, password, nombre_completo, rol, id_sucursal, COALESCE(ventanilla, 1)
                    FROM empleado
                    WHERE LOWER(usuario) = LOWER(@usr);";
                using var cmd = new SqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@usr", usuario.Trim());
                using var rd = cmd.ExecuteReader();
                if (!rd.Read())
                {
                    return new UsuarioModel
                    {
                        Autenticado = false,
                        Mensaje = "Credenciales inválidas. Verifique usuario y contraseña."
                    };
                }

                string hash = rd.GetString(2);
                bool ok = false;
                if (hash.StartsWith("$2"))
                {
                    ok = BCrypt.Net.BCrypt.Verify(clave, hash);
                }
                else
                {
                    ok = string.Equals(clave, hash, StringComparison.Ordinal);
                }

                if (!ok)
                {
                    return new UsuarioModel
                    {
                        Autenticado = false,
                        Mensaje = "Contraseña incorrecta para el operador indicado."
                    };
                }

                return new UsuarioModel
                {
                    Autenticado = true,
                    IdEmpleado = rd.GetInt32(0),
                    Usuario = rd.GetString(1),
                    NombreCompleto = rd.GetString(3),
                    Rol = rd.GetString(4),
                    IdSucursal = rd.GetInt32(5),
                    Ventanilla = rd.GetInt32(6),
                    Token = $"MONSTER-DOTNET-{Guid.NewGuid():N}",
                    Mensaje = "Autenticación exitosa en EurekaBank (.NET - SQL Server)."
                };
            }
            catch (Exception ex)
            {
                return new UsuarioModel
                {
                    Autenticado = false,
                    Mensaje = "Error de conexión con la base de datos: " + ex.Message
                };
            }
        }

        public List<SucursalModel> ListarSucursales()
        {
            var lista = new List<SucursalModel>();
            using var con = new SqlConnection(ConnectionString);
            con.Open();
            const string sql = "SELECT id_sucursal, codigo, nombre, ciudad, direccion FROM sucursal ORDER BY id_sucursal ASC;";
            using var cmd = new SqlCommand(sql, con);
            using var rd = cmd.ExecuteReader();
            while (rd.Read())
            {
                lista.Add(new SucursalModel
                {
                    IdSucursal = rd.GetInt32(0),
                    Codigo = rd.GetString(1),
                    Nombre = rd.GetString(2),
                    Ciudad = rd.GetString(3),
                    Direccion = rd.GetString(4)
                });
            }
            return lista;
        }

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId)
        {
            var lista = new List<CuentaModel>();
            using var con = new SqlConnection(ConnectionString);
            con.Open();
            string sql = @"
                SELECT c.numero_cuenta, c.moneda, c.saldo, c.estado,
                       cl.id_cliente, cl.dni, cl.nombre, cl.apellido, COALESCE(cl.telefono,''), COALESCE(cl.email,''),
                       s.id_sucursal, s.nombre,
                       COALESCE(cm.estado_mashup, 'VERDE'), COALESCE(cm.ventanilla, 0), COALESCE(cm.bloqueado_por, '')
                FROM cuenta c
                INNER JOIN cliente cl ON cl.id_cliente = c.id_cliente
                INNER JOIN sucursal s ON s.id_sucursal = c.id_sucursal
                LEFT JOIN control_mashup cm ON cm.numero_cuenta = c.numero_cuenta "
                + (sucursalId > 0 ? "WHERE c.id_sucursal = @suc " : "")
                + "ORDER BY c.numero_cuenta ASC;";

            using var cmd = new SqlCommand(sql, con);
            if (sucursalId > 0)
            {
                cmd.Parameters.AddWithValue("@suc", sucursalId);
            }
            using var rd = cmd.ExecuteReader();
            while (rd.Read())
            {
                lista.Add(MapearCuenta(rd));
            }
            return lista;
        }

        public CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta)) return null;
            using var con = new SqlConnection(ConnectionString);
            con.Open();
            const string sql = @"
                SELECT TOP 1 c.numero_cuenta, c.moneda, c.saldo, c.estado,
                       cl.id_cliente, cl.dni, cl.nombre, cl.apellido, COALESCE(cl.telefono,''), COALESCE(cl.email,''),
                       s.id_sucursal, s.nombre,
                       COALESCE(cm.estado_mashup, 'VERDE'), COALESCE(cm.ventanilla, 0), COALESCE(cm.bloqueado_por, '')
                FROM cuenta c
                INNER JOIN cliente cl ON cl.id_cliente = c.id_cliente
                INNER JOIN sucursal s ON s.id_sucursal = c.id_sucursal
                LEFT JOIN control_mashup cm ON cm.numero_cuenta = c.numero_cuenta
                WHERE c.numero_cuenta = @cta;";
            using var cmd = new SqlCommand(sql, con);
            cmd.Parameters.AddWithValue("@cta", numeroCuenta.Trim());
            using var rd = cmd.ExecuteReader();
            return rd.Read() ? MapearCuenta(rd) : null;
        }

        public List<CuentaModel> ConsultarCuentasPorCliente(string dni)
        {
            var lista = new List<CuentaModel>();
            if (string.IsNullOrWhiteSpace(dni)) return lista;
            using var con = new SqlConnection(ConnectionString);
            con.Open();
            const string sql = @"
                SELECT c.numero_cuenta, c.moneda, c.saldo, c.estado,
                       cl.id_cliente, cl.dni, cl.nombre, cl.apellido, COALESCE(cl.telefono,''), COALESCE(cl.email,''),
                       s.id_sucursal, s.nombre,
                       COALESCE(cm.estado_mashup, 'VERDE'), COALESCE(cm.ventanilla, 0), COALESCE(cm.bloqueado_por, '')
                FROM cuenta c
                INNER JOIN cliente cl ON cl.id_cliente = c.id_cliente
                INNER JOIN sucursal s ON s.id_sucursal = c.id_sucursal
                LEFT JOIN control_mashup cm ON cm.numero_cuenta = c.numero_cuenta
                WHERE cl.dni = @dni OR c.numero_cuenta = @dni
                ORDER BY c.numero_cuenta ASC;";
            using var cmd = new SqlCommand(sql, con);
            cmd.Parameters.AddWithValue("@dni", dni.Trim());
            using var rd = cmd.ExecuteReader();
            while (rd.Read())
            {
                lista.Add(MapearCuenta(rd));
            }
            return lista;
        }

        public List<MovimientoModel> ConsultarExtracto(string numeroCuenta)
        {
            var lista = new List<MovimientoModel>();
            if (string.IsNullOrWhiteSpace(numeroCuenta)) return lista;
            using var con = new SqlConnection(ConnectionString);
            con.Open();
            const string sql = @"
                SELECT TOP 50 m.id_movimiento, m.numero_operacion, m.numero_cuenta, m.id_empleado,
                       e.nombre_completo, COALESCE(m.ventanilla, 1), m.tipo, m.monto, m.saldo_resultante,
                       COALESCE(m.cuenta_referencia, '-'), COALESCE(m.descripcion, ''),
                       CONVERT(VARCHAR(19), m.fecha_hora, 120)
                FROM movimiento m
                INNER JOIN empleado e ON e.id_empleado = m.id_empleado
                WHERE m.numero_cuenta = @cta
                ORDER BY m.fecha_hora DESC, m.id_movimiento DESC;";
            using var cmd = new SqlCommand(sql, con);
            cmd.Parameters.AddWithValue("@cta", numeroCuenta.Trim());
            using var rd = cmd.ExecuteReader();
            while (rd.Read())
            {
                lista.Add(new MovimientoModel
                {
                    IdMovimiento = rd.GetInt32(0),
                    NumeroOperacion = rd.GetString(1),
                    NumeroCuenta = rd.GetString(2),
                    IdEmpleado = rd.GetInt32(3),
                    NombreEmpleado = rd.GetString(4),
                    Ventanilla = rd.GetInt32(5),
                    Tipo = rd.GetString(6),
                    Monto = Convert.ToDouble(rd.GetDecimal(7)),
                    SaldoResultante = Convert.ToDouble(rd.GetDecimal(8)),
                    CuentaReferencia = rd.GetString(9),
                    Descripcion = rd.GetString(10),
                    FechaHora = rd.GetString(11)
                });
            }
            return lista;
        }

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Debe especificar el número de cuenta.");
            if (monto <= 0)
                return OperacionModel.Error("ERROR_VALIDACION", "El monto del depósito debe ser mayor a $0.00.");

            string cta = numeroCuenta.Trim();
            int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
            int empId = idEmpleado <= 0 ? 1 : idEmpleado;
            string nombreUsuario = ObtenerUsuarioEmpleado(empId);

            if (!ConcurrenciaMashupModel.BloquearCuenta(cta, nroVentanilla, nombreUsuario))
            {
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(cta);
                return OperacionModel.Error(
                    "CONCURRENCIA_BLOQUEADA",
                    $"CONCURRENCIA [ROJO]: La cuenta {cta} está siendo operada actualmente en Ventanilla {info.Ventanilla} ({info.Usuario}). Intente en unos segundos.",
                    "ROJO",
                    info.Ventanilla);
            }

            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var tx = con.BeginTransaction(IsolationLevel.ReadCommitted);

                var infoCuenta = LeerCuentaForUpdate(con, tx, cta);
                if (infoCuenta == null)
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {cta} no existe.");
                }
                if (string.Equals(infoCuenta.Value.Estado, "Bloqueada", StringComparison.OrdinalIgnoreCase))
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_BLOQUEADA", $"La cuenta {cta} tiene bloqueo administrativo. Solicite desbloqueo a Monster.");
                }

                if (LockDelayMs > 0)
                {
                    Thread.Sleep(LockDelayMs);
                }

                decimal saldoAnterior = infoCuenta.Value.Saldo;
                decimal montoDec = Math.Round((decimal)monto, 2);
                decimal nuevoSaldo = Math.Round(saldoAnterior + montoDec, 2);

                ActualizarSaldoCuenta(con, tx, cta, nuevoSaldo);
                string nroOp = GenerarNumeroOperacion("DEP");
                InsertarMovimiento(con, tx, nroOp, cta, empId, nroVentanilla, "Deposito", montoDec, nuevoSaldo, null,
                    $"Depósito en efectivo Ventanilla {nroVentanilla}");

                tx.Commit();

                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = $"Depósito de ${montoDec:F2} acreditado exitosamente en cuenta {cta} (Ventanilla {nroVentanilla}).",
                    NumeroOperacion = nroOp,
                    NumeroCuenta = cta,
                    SaldoAnterior = (double)saldoAnterior,
                    MontoOperacion = (double)montoDec,
                    SaldoActual = (double)nuevoSaldo,
                    EstadoMashup = "VERDE",
                    Ventanilla = nroVentanilla,
                    FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                };
            }
            catch (Exception ex)
            {
                return OperacionModel.Error("ERROR_TRANSACCION", "Transacción revertida (ROLLBACK): " + ex.Message);
            }
            finally
            {
                ConcurrenciaMashupModel.LiberarCuenta(cta);
            }
        }

        public OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Debe especificar el número de cuenta.");
            if (monto <= 0)
                return OperacionModel.Error("ERROR_VALIDACION", "El monto del retiro debe ser mayor a $0.00.");

            string cta = numeroCuenta.Trim();
            int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
            int empId = idEmpleado <= 0 ? 1 : idEmpleado;
            string nombreUsuario = ObtenerUsuarioEmpleado(empId);

            if (!ConcurrenciaMashupModel.BloquearCuenta(cta, nroVentanilla, nombreUsuario))
            {
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(cta);
                return OperacionModel.Error(
                    "CONCURRENCIA_BLOQUEADA",
                    $"CONCURRENCIA [ROJO]: La cuenta {cta} está siendo operada actualmente en Ventanilla {info.Ventanilla} ({info.Usuario}). Intente en unos segundos.",
                    "ROJO",
                    info.Ventanilla);
            }

            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var tx = con.BeginTransaction(IsolationLevel.ReadCommitted);

                var infoCuenta = LeerCuentaForUpdate(con, tx, cta);
                if (infoCuenta == null)
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {cta} no existe.");
                }
                if (string.Equals(infoCuenta.Value.Estado, "Bloqueada", StringComparison.OrdinalIgnoreCase))
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_BLOQUEADA", $"La cuenta {cta} tiene bloqueo administrativo.");
                }

                if (LockDelayMs > 0)
                {
                    Thread.Sleep(LockDelayMs);
                }

                decimal saldoAnterior = infoCuenta.Value.Saldo;
                decimal montoDec = Math.Round((decimal)monto, 2);
                if (saldoAnterior < montoDec)
                {
                    tx.Rollback();
                    return OperacionModel.Error("SALDO_INSUFICIENTE",
                        $"Saldo insuficiente en cuenta {cta}. Saldo disponible: ${saldoAnterior:F2}, Monto solicitado: ${montoDec:F2}.");
                }

                decimal nuevoSaldo = Math.Round(saldoAnterior - montoDec, 2);
                ActualizarSaldoCuenta(con, tx, cta, nuevoSaldo);
                string nroOp = GenerarNumeroOperacion("RET");
                InsertarMovimiento(con, tx, nroOp, cta, empId, nroVentanilla, "Retiro", montoDec, nuevoSaldo, null,
                    $"Retiro en ventanilla {nroVentanilla}");

                tx.Commit();

                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = $"Retiro de ${montoDec:F2} debitado exitosamente de cuenta {cta} (Ventanilla {nroVentanilla}).",
                    NumeroOperacion = nroOp,
                    NumeroCuenta = cta,
                    SaldoAnterior = (double)saldoAnterior,
                    MontoOperacion = (double)montoDec,
                    SaldoActual = (double)nuevoSaldo,
                    EstadoMashup = "VERDE",
                    Ventanilla = nroVentanilla,
                    FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                };
            }
            catch (Exception ex)
            {
                return OperacionModel.Error("ERROR_TRANSACCION", "Transacción revertida (ROLLBACK): " + ex.Message);
            }
            finally
            {
                ConcurrenciaMashupModel.LiberarCuenta(cta);
            }
        }

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla)
        {
            if (string.IsNullOrWhiteSpace(cuentaOrigen) || string.IsNullOrWhiteSpace(cuentaDestino))
                return OperacionModel.Error("ERROR_VALIDACION", "Debe especificar cuentas de origen y destino.");

            string origen = cuentaOrigen.Trim();
            string destino = cuentaDestino.Trim();

            if (string.Equals(origen, destino, StringComparison.OrdinalIgnoreCase))
                return OperacionModel.Error("ERROR_VALIDACION", "La cuenta origen y destino no pueden ser la misma.");
            if (monto <= 0)
                return OperacionModel.Error("ERROR_VALIDACION", "El monto a transferir debe ser mayor a $0.00.");

            int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
            int empId = idEmpleado <= 0 ? 1 : idEmpleado;
            string nombreUsuario = ObtenerUsuarioEmpleado(empId);

            // Ordenamiento lexicográfico para prevenir Deadlocks
            string primera = string.Compare(origen, destino, StringComparison.Ordinal) < 0 ? origen : destino;
            string segunda = string.Compare(origen, destino, StringComparison.Ordinal) < 0 ? destino : origen;

            if (!ConcurrenciaMashupModel.BloquearCuenta(primera, nroVentanilla, nombreUsuario))
            {
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(primera);
                return OperacionModel.Error(
                    "CONCURRENCIA_BLOQUEADA",
                    $"CONCURRENCIA [ROJO]: La cuenta {primera} está ocupada en Ventanilla {info.Ventanilla} ({info.Usuario}).",
                    "ROJO", info.Ventanilla);
            }

            if (!ConcurrenciaMashupModel.BloquearCuenta(segunda, nroVentanilla, nombreUsuario))
            {
                ConcurrenciaMashupModel.LiberarCuenta(primera);
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(segunda);
                return OperacionModel.Error(
                    "CONCURRENCIA_BLOQUEADA",
                    $"CONCURRENCIA [ROJO]: La cuenta {segunda} está ocupada en Ventanilla {info.Ventanilla} ({info.Usuario}).",
                    "ROJO", info.Ventanilla);
            }

            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var tx = con.BeginTransaction(IsolationLevel.ReadCommitted);

                // Bloqueo SELECT ... WITH (UPDLOCK, ROWLOCK) en orden lexicográfico
                _ = LeerCuentaForUpdate(con, tx, primera);
                _ = LeerCuentaForUpdate(con, tx, segunda);

                var ctaOrigen = LeerCuentaForUpdate(con, tx, origen);
                var ctaDestino = LeerCuentaForUpdate(con, tx, destino);

                if (ctaOrigen == null)
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta origen {origen} no existe.");
                }
                if (ctaDestino == null)
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta destino {destino} no existe.");
                }

                if (LockDelayMs > 0)
                {
                    Thread.Sleep(LockDelayMs);
                }

                decimal montoDec = Math.Round((decimal)monto, 2);
                if (ctaOrigen.Value.Saldo < montoDec)
                {
                    tx.Rollback();
                    return OperacionModel.Error("SALDO_INSUFICIENTE",
                        $"Saldo insuficiente en {origen}. Disponible: ${ctaOrigen.Value.Saldo:F2}.");
                }

                decimal nuevoSaldoOrigen = Math.Round(ctaOrigen.Value.Saldo - montoDec, 2);
                decimal nuevoSaldoDestino = Math.Round(ctaDestino.Value.Saldo + montoDec, 2);

                ActualizarSaldoCuenta(con, tx, origen, nuevoSaldoOrigen);
                ActualizarSaldoCuenta(con, tx, destino, nuevoSaldoDestino);

                string nroOpOut = GenerarNumeroOperacion("TRF-OUT");
                string nroOpIn = GenerarNumeroOperacion("TRF-IN");

                InsertarMovimiento(con, tx, nroOpOut, origen, empId, nroVentanilla, "Transferencia Salida",
                    montoDec, nuevoSaldoOrigen, destino, $"Transferencia enviada a cuenta {destino}");
                InsertarMovimiento(con, tx, nroOpIn, destino, empId, nroVentanilla, "Transferencia Ingreso",
                    montoDec, nuevoSaldoDestino, origen, $"Transferencia recibida de cuenta {origen}");

                tx.Commit();

                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = $"Transferencia de ${montoDec:F2} desde {origen} hacia {destino} completada con éxito.",
                    NumeroOperacion = nroOpOut,
                    NumeroCuenta = origen,
                    SaldoAnterior = (double)ctaOrigen.Value.Saldo,
                    MontoOperacion = (double)montoDec,
                    SaldoActual = (double)nuevoSaldoOrigen,
                    EstadoMashup = "VERDE",
                    Ventanilla = nroVentanilla,
                    FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                };
            }
            catch (Exception ex)
            {
                return OperacionModel.Error("ERROR_TRANSACCION", "Transferencia revertida (ROLLBACK): " + ex.Message);
            }
            finally
            {
                ConcurrenciaMashupModel.LiberarCuenta(segunda);
                ConcurrenciaMashupModel.LiberarCuenta(primera);
            }
        }

        public OperacionModel CambiarEstadoCuenta(string numeroCuenta, string nuevoEstado, string usuarioAdmin)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Debe especificar la cuenta.");

            string est = (nuevoEstado ?? "").Trim();
            if (!est.Equals("Activa", StringComparison.OrdinalIgnoreCase) &&
                !est.Equals("Bloqueada", StringComparison.OrdinalIgnoreCase))
            {
                return OperacionModel.Error("ERROR_VALIDACION", "El estado solo puede ser 'Activa' o 'Bloqueada'.");
            }

            string rol = ObtenerRolUsuario(usuarioAdmin);
            if (!rol.Equals("Superadmin Monster", StringComparison.OrdinalIgnoreCase) &&
                !rol.Equals("Gerente", StringComparison.OrdinalIgnoreCase))
            {
                return OperacionModel.Error("ACCESO_DENEGADO",
                    $"El usuario '{usuarioAdmin}' (rol '{rol}') no tiene permisos para cambiar estados de cuenta. Solo Superadmin Monster o Gerente.");
            }

            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand("UPDATE cuenta SET estado = @est WHERE numero_cuenta = @cta;", con);
                cmd.Parameters.AddWithValue("@est", est);
                cmd.Parameters.AddWithValue("@cta", numeroCuenta.Trim());
                int n = cmd.ExecuteNonQuery();
                if (n == 0) return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {numeroCuenta} no existe.");

                if (est.Equals("Activa", StringComparison.OrdinalIgnoreCase))
                {
                    ConcurrenciaMashupModel.DesbloquearAdministrativo(numeroCuenta.Trim());
                }

                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = $"Estado de cuenta {numeroCuenta} actualizado a '{est}' por {usuarioAdmin}.",
                    NumeroCuenta = numeroCuenta.Trim(),
                    EstadoMashup = est.Equals("Activa", StringComparison.OrdinalIgnoreCase) ? "VERDE" : "ROJO"
                };
            }
            catch (Exception ex)
            {
                return OperacionModel.Error("ERROR_BDD", "Error al actualizar estado: " + ex.Message);
            }
        }

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta)
        {
            var cta = ObtenerCuentaPorNumero(numeroCuenta);
            if (cta == null)
                return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {numeroCuenta} no existe.");

            return new OperacionModel
            {
                Exito = true,
                Codigo = "OK",
                Mensaje = cta.EstadoMashup == "ROJO"
                    ? $"OCUPADO [ROJO] en Ventanilla {cta.VentanillaOcupada} ({cta.BloqueadoPor})"
                    : "LIBRE [VERDE] disponible para operar.",
                NumeroCuenta = cta.NumeroCuenta,
                SaldoActual = cta.Saldo,
                EstadoMashup = cta.EstadoMashup,
                Ventanilla = cta.VentanillaOcupada,
                FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
            };
        }

        public OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Indique el número de cuenta a bloquear.");

            string cta = numeroCuenta.Trim();
            if (ObtenerCuentaPorNumero(cta) == null)
                return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {cta} no existe.");

            if (!ConcurrenciaMashupModel.BloquearCuenta(cta, ventanilla <= 0 ? 1 : ventanilla, usuario))
            {
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(cta);
                return OperacionModel.Error("CONCURRENCIA_BLOQUEADA",
                    $"La cuenta {cta} ya se encuentra en estado ROJO en Ventanilla {info.Ventanilla}.", "ROJO", info.Ventanilla);
            }

            return new OperacionModel
            {
                Exito = true,
                Codigo = "OK",
                Mensaje = $"Cuenta {cta} retenida en estado ROJO (Ocupada en Ventanilla {ventanilla}).",
                NumeroCuenta = cta,
                EstadoMashup = "ROJO",
                Ventanilla = ventanilla,
                FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
            };
        }

        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Indique la cuenta a desbloquear.");

            string usr = (usuarioAdmin ?? string.Empty).Trim();
            if (!EsAdministradorAutorizado(usr))
            {
                return OperacionModel.Error("NO_AUTORIZADO",
                    "Solo el Superadmin Monster o Gerencia pueden desbloquear cuentas forzadamente.");
            }

            string cta = numeroCuenta.Trim();
            ConcurrenciaMashupModel.DesbloquearAdministrativo(cta);
            return new OperacionModel
            {
                Exito = true,
                Codigo = "OK",
                Mensaje = $"Cuenta {cta} liberada a estado VERDE por {usr}.",
                NumeroCuenta = cta,
                EstadoMashup = "VERDE",
                FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
            };
        }

        private bool EsAdministradorAutorizado(string usuario)
        {
            if (string.Equals(usuario, "Monster", StringComparison.OrdinalIgnoreCase)) return true;
            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand("SELECT TOP 1 rol FROM empleado WHERE LOWER(usuario) = LOWER(@u);", con);
                cmd.Parameters.AddWithValue("@u", usuario);
                var rol = cmd.ExecuteScalar()?.ToString() ?? string.Empty;
                return rol.Contains("Monster", StringComparison.OrdinalIgnoreCase)
                    || rol.Contains("Gerente", StringComparison.OrdinalIgnoreCase);
            }
            catch
            {
                return false;
            }
        }

        public string ObtenerRolUsuario(string usuario)
        {
            if (string.IsNullOrWhiteSpace(usuario)) return "Cajero";
            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand("SELECT TOP 1 rol FROM empleado WHERE LOWER(usuario) = LOWER(@u);", con);
                cmd.Parameters.AddWithValue("@u", usuario.Trim());
                var val = cmd.ExecuteScalar();
                return val?.ToString() ?? "Cajero";
            }
            catch
            {
                return "Cajero";
            }
        }

        public string ObtenerUsuarioEmpleado(int idEmpleado)
        {
            if (idEmpleado <= 0) return "Monster";
            try
            {
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand("SELECT TOP 1 usuario FROM empleado WHERE id_empleado = @id;", con);
                cmd.Parameters.AddWithValue("@id", idEmpleado);
                var val = cmd.ExecuteScalar();
                return val?.ToString() ?? "Monster";
            }
            catch
            {
                return "Monster";
            }
        }

        private static (decimal Saldo, string Estado)? LeerCuentaForUpdate(SqlConnection con, SqlTransaction tx, string cta)
        {
            const string sql = "SELECT saldo, estado FROM cuenta WITH (UPDLOCK, ROWLOCK) WHERE numero_cuenta = @cta;";
            using var cmd = new SqlCommand(sql, con, tx);
            cmd.Parameters.AddWithValue("@cta", cta);
            using var rd = cmd.ExecuteReader();
            if (!rd.Read()) return null;
            return (rd.GetDecimal(0), rd.GetString(1));
        }

        private static void ActualizarSaldoCuenta(SqlConnection con, SqlTransaction tx, string cta, decimal nuevoSaldo)
        {
            using var cmd = new SqlCommand("UPDATE cuenta SET saldo = @s WHERE numero_cuenta = @cta;", con, tx);
            cmd.Parameters.AddWithValue("@s", nuevoSaldo);
            cmd.Parameters.AddWithValue("@cta", cta);
            cmd.ExecuteNonQuery();
        }

        private static void InsertarMovimiento(SqlConnection con, SqlTransaction tx, string nroOp, string cta,
            int idEmp, int ventanilla, string tipo, decimal monto, decimal saldoResultante, string? ctaRef, string desc)
        {
            const string sql = @"
                INSERT INTO movimiento (numero_operacion, numero_cuenta, id_empleado, ventanilla, tipo, monto, saldo_resultante, cuenta_referencia, descripcion)
                VALUES (@op, @cta, @emp, @ven, @tip, @mon, @sal, @ref, @des);";
            using var cmd = new SqlCommand(sql, con, tx);
            cmd.Parameters.AddWithValue("@op", nroOp);
            cmd.Parameters.AddWithValue("@cta", cta);
            cmd.Parameters.AddWithValue("@emp", idEmp);
            cmd.Parameters.AddWithValue("@ven", ventanilla);
            cmd.Parameters.AddWithValue("@tip", tipo);
            cmd.Parameters.AddWithValue("@mon", monto);
            cmd.Parameters.AddWithValue("@sal", saldoResultante);
            cmd.Parameters.AddWithValue("@ref", ctaRef != null ? ctaRef : DBNull.Value);
            cmd.Parameters.AddWithValue("@des", desc);
            cmd.ExecuteNonQuery();
        }

        private static CuentaModel MapearCuenta(SqlDataReader rd)
        {
            return new CuentaModel
            {
                NumeroCuenta = rd.GetString(0),
                Moneda = rd.GetString(1),
                Saldo = Convert.ToDouble(rd.GetDecimal(2)),
                EstadoCuenta = rd.GetString(3),
                IdCliente = rd.GetInt32(4),
                DniCliente = rd.GetString(5),
                NombreCliente = rd.GetString(6),
                ApellidoCliente = rd.GetString(7),
                TelefonoCliente = rd.GetString(8),
                EmailCliente = rd.GetString(9),
                IdSucursal = rd.GetInt32(10),
                NombreSucursal = rd.GetString(11),
                EstadoMashup = rd.GetString(12),
                VentanillaOcupada = rd.GetInt32(13),
                BloqueadoPor = rd.GetString(14)
            };
        }
    }
}
