using System;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace ec.edu.monster.modelo.seguridad
{
    /// <summary>
    /// Gestión en memoria de credenciales de usuario.
    /// Credenciales iniciales por defecto: Usuario: Monster, Contraseña: Monster9
    /// </summary>
    public static class AdministradorCredenciales
    {
        private static readonly ConcurrentDictionary<string, string> USUARIOS = new ConcurrentDictionary<string, string>();

        public const string USUARIO_DEFAULT = "Monster";
        public const string CONTRASENIA_DEFAULT = "Monster9";

        static AdministradorCredenciales()
        {
            Reiniciar();
        }

        public static void Reiniciar()
        {
            USUARIOS.Clear();
            USUARIOS[USUARIO_DEFAULT.ToLowerInvariant()] = CONTRASENIA_DEFAULT;
        }

        public static bool ValidarUsuario(string usuario, string contrasenia)
        {
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrEmpty(contrasenia))
            {
                return false;
            }

            string userKey = usuario.Trim().ToLowerInvariant();
            if (!USUARIOS.TryGetValue(userKey, out string claveAlmacenada))
            {
                return false;
            }

            if (string.Equals(claveAlmacenada, contrasenia, StringComparison.Ordinal))
            {
                return true;
            }

            if (string.Equals(claveAlmacenada, CONTRASENIA_DEFAULT, StringComparison.OrdinalIgnoreCase) &&
                (string.Equals(contrasenia, "Monster9", StringComparison.Ordinal) ||
                 string.Equals(contrasenia, "MONSTER9", StringComparison.Ordinal)))
            {
                return true;
            }

            return false;
        }

        public static void CambiarContrasenia(string contraseniaActual, string contraseniaNueva)
        {
            if (string.IsNullOrEmpty(contraseniaActual) || string.IsNullOrWhiteSpace(contraseniaNueva))
            {
                throw new ArgumentException("Las contraseñas no pueden ser nulas ni vacías.");
            }

            bool actualizada = false;
            foreach (KeyValuePair<string, string> entry in USUARIOS)
            {
                if (ValidarUsuario(entry.Key, contraseniaActual))
                {
                    USUARIOS[entry.Key] = contraseniaNueva;
                    actualizada = true;
                    break;
                }
            }

            if (!actualizada)
            {
                throw new InvalidOperationException("La contraseña actual es incorrecta.");
            }
        }

        public static void CambiarContrasenia(string usuario, string contraseniaActual, string contraseniaNueva)
        {
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrEmpty(contraseniaActual) || string.IsNullOrWhiteSpace(contraseniaNueva))
            {
                throw new ArgumentException("Datos inválidos para cambio de contraseña.");
            }

            if (!ValidarUsuario(usuario, contraseniaActual))
            {
                throw new InvalidOperationException("Credenciales incorrectas para el usuario: " + usuario);
            }

            string userKey = usuario.Trim().ToLowerInvariant();
            USUARIOS[userKey] = contraseniaNueva;
        }
    }
}
