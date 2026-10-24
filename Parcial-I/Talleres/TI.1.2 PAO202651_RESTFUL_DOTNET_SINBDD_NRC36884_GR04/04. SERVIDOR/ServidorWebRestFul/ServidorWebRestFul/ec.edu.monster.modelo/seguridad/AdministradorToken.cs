using System;
using System.Collections.Concurrent;

namespace ec.edu.monster.modelo.seguridad
{
    /// <summary>
    /// Administrador de tokens de sesión en memoria para clientes RESTful.
    /// Valida vigencia (30 minutos) e invalidación por cierre de sesión.
    /// </summary>
    public static class AdministradorToken
    {
        private static readonly TimeSpan TIEMPO_EXPIRACION = TimeSpan.FromMinutes(30);
        private static readonly ConcurrentDictionary<string, DateTime> TOKENS_ACTIVOS = new ConcurrentDictionary<string, DateTime>();

        public static string GenerarToken()
        {
            string token = Guid.NewGuid().ToString("N");
            TOKENS_ACTIVOS[token] = DateTime.UtcNow;
            return token;
        }

        public static bool ValidarToken(string token)
        {
            if (string.IsNullOrWhiteSpace(token))
            {
                return false;
            }

            string tokenLimpio = token.Trim();
            if (!TOKENS_ACTIVOS.TryGetValue(tokenLimpio, out DateTime fechaCreacion))
            {
                return false;
            }

            if (DateTime.UtcNow - fechaCreacion > TIEMPO_EXPIRACION)
            {
                TOKENS_ACTIVOS.TryRemove(tokenLimpio, out _);
                return false;
            }

            return true;
        }

        public static void InvalidarToken(string token)
        {
            if (!string.IsNullOrWhiteSpace(token))
            {
                TOKENS_ACTIVOS.TryRemove(token.Trim(), out _);
            }
        }

        public static void Limpiar()
        {
            TOKENS_ACTIVOS.Clear();
        }
    }
}
