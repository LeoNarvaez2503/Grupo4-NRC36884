using System.Collections.Concurrent;
using Microsoft.Data.SqlClient;

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
            "Server=127.0.0.1,1433;Database=eurekabank_db;User Id=sa;Password=Monster9#Admin;TrustServerCertificate=True;";

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
                    using var con = new SqlConnection(ConnectionString);
                    con.Open();
                    using var cmd = new SqlCommand("UPDATE cuenta SET estado = 'Activa' WHERE numero_cuenta = @cta", con);
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
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand(
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
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                using var cmd = new SqlCommand("SELECT estado_mashup FROM control_mashup WHERE numero_cuenta = @cta", con);
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
                using var con = new SqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    IF EXISTS (SELECT 1 FROM control_mashup WHERE numero_cuenta = @cta)
                        UPDATE control_mashup
                        SET estado_mashup = @est,
                            ventanilla = @ven,
                            bloqueado_por = @usr,
                            actualizado_en = GETDATE()
                        WHERE numero_cuenta = @cta;
                    ELSE
                        INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por, actualizado_en)
                        VALUES (@cta, @est, @ven, @usr, GETDATE());";
                using var cmd = new SqlCommand(sql, con);
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
