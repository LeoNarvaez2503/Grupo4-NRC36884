using System.Collections.Concurrent;
using MySqlConnector;

namespace ec.edu.monster.model
{
    public class ConcurrenciaMashupModel
    {
        public class InfoBloqueo
        {
            public string NumeroCuenta { get; set; } = string.Empty;
            public int Ventanilla { get; set; }
            public string Usuario { get; set; } = string.Empty;
            public DateTime Inicio { get; set; } = DateTime.Now;
        }

        private static readonly ConcurrentDictionary<string, InfoBloqueo> BloqueosActivos = new(StringComparer.OrdinalIgnoreCase);
        private static readonly object SyncLock = new();
        public static string ConnectionString { get; set; } =
            "Server=127.0.0.1;Port=3306;Database=eurekabank_db;User ID=root;Password=admin;SslMode=None;AllowPublicKeyRetrieval=True;";

        public static bool BloquearCuenta(string numeroCuenta, int ventanilla, string usuario)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return false;

            lock (SyncLock)
            {
                // Verificar tanto memoria local como tabla control_mashup compartida
                if (BloqueosActivos.ContainsKey(cta) || EstaRojoEnBdd(cta))
                {
                    return false;
                }

                var info = new InfoBloqueo
                {
                    NumeroCuenta = cta,
                    Ventanilla = ventanilla <= 0 ? 1 : ventanilla,
                    Usuario = string.IsNullOrWhiteSpace(usuario) ? "Monster" : usuario.Trim(),
                    Inicio = DateTime.Now
                };
                BloqueosActivos[cta] = info;
                PersistirEstadoMashup(cta, "ROJO", info.Ventanilla, info.Usuario);
                return true;
            }
        }

        public static void LiberarCuenta(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return;

            lock (SyncLock)
            {
                BloqueosActivos.TryRemove(cta, out _);
                PersistirEstadoMashup(cta, "VERDE", null, null);
            }
        }

        public static void DesbloquearAdministrativo(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return;

            lock (SyncLock)
            {
                BloqueosActivos.TryRemove(cta, out _);
                PersistirEstadoMashup(cta, "VERDE", null, null);
                try
                {
                    using var con = new MySqlConnection(ConnectionString);
                    con.Open();
                    using var cmd = new MySqlCommand("UPDATE cuenta SET estado = 'Activa' WHERE numero_cuenta = @cta", con);
                    cmd.Parameters.AddWithValue("@cta", cta);
                    cmd.ExecuteNonQuery();
                }
                catch
                {
                    // Ignorar fallo secundario
                }
            }
        }

        public static InfoBloqueo ObtenerInfoBloqueo(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (BloqueosActivos.TryGetValue(cta, out var local))
            {
                return local;
            }

            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var cmd = new MySqlCommand(
                    "SELECT estado_mashup, COALESCE(ventanilla, 1), COALESCE(bloqueado_por, 'Operador') FROM control_mashup WHERE numero_cuenta = @cta",
                    con);
                cmd.Parameters.AddWithValue("@cta", cta);
                using var rd = cmd.ExecuteReader();
                if (rd.Read() && string.Equals(rd.GetString(0), "ROJO", StringComparison.OrdinalIgnoreCase))
                {
                    return new InfoBloqueo
                    {
                        NumeroCuenta = cta,
                        Ventanilla = rd.GetInt32(1),
                        Usuario = rd.GetString(2)
                    };
                }
            }
            catch
            {
            }

            return new InfoBloqueo { NumeroCuenta = cta, Ventanilla = 1, Usuario = "Ventanilla Activa" };
        }

        private static bool EstaRojoEnBdd(string cta)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var cmd = new MySqlCommand("SELECT estado_mashup FROM control_mashup WHERE numero_cuenta = @cta", con);
                cmd.Parameters.AddWithValue("@cta", cta);
                var val = cmd.ExecuteScalar()?.ToString();
                return string.Equals(val, "ROJO", StringComparison.OrdinalIgnoreCase);
            }
            catch
            {
                return false;
            }
        }

        private static void PersistirEstadoMashup(string cta, string estado, int? ventanilla, string? usuario)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por)
                    VALUES (@cta, @est, @ven, @usr)
                    ON DUPLICATE KEY UPDATE
                        estado_mashup = VALUES(estado_mashup),
                        ventanilla = VALUES(ventanilla),
                        bloqueado_por = VALUES(bloqueado_por),
                        actualizado_en = CURRENT_TIMESTAMP;";
                using var cmd = new MySqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@cta", cta);
                cmd.Parameters.AddWithValue("@est", estado);
                cmd.Parameters.AddWithValue("@ven", ventanilla.HasValue ? ventanilla.Value : DBNull.Value);
                cmd.Parameters.AddWithValue("@usr", !string.IsNullOrEmpty(usuario) ? usuario : DBNull.Value);
                cmd.ExecuteNonQuery();
            }
            catch
            {
            }
        }
    }
}
