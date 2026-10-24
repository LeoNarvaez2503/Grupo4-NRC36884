using System;
using ec.edu.monster.modelo.dto;
using ec.edu.monster.modelo.seguridad;

namespace ec.edu.monster.controlador
{
    /// <summary>
    /// Controlador MVC para la gestión de autenticación, validación de tokens y contraseñas.
    /// Coordina las peticiones de los clientes con el modelo de seguridad.
    /// </summary>
    public class AutenticacionControlador
    {
        public LoginResponse Login(string usuario, string contrasenia)
        {
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrEmpty(contrasenia))
            {
                return new LoginResponse(false, null, usuario, "Debe proporcionar usuario y contraseña");
            }

            if (AdministradorCredenciales.ValidarUsuario(usuario, contrasenia))
            {
                string token = AdministradorToken.GenerarToken();
                return new LoginResponse(true, token, usuario.Trim().ToUpperInvariant(), "Autenticación exitosa");
            }

            return new LoginResponse(false, null, usuario, "Usuario o contraseña incorrectos");
        }

        public RespuestaGeneral CambiarContrasenia(string actual, string nueva, string usuario = null)
        {
            try
            {
                if (string.IsNullOrWhiteSpace(usuario))
                {
                    AdministradorCredenciales.CambiarContrasenia(actual, nueva);
                }
                else
                {
                    AdministradorCredenciales.CambiarContrasenia(usuario, actual, nueva);
                }

                return RespuestaGeneral.Ok("Contraseña actualizada correctamente");
            }
            catch (Exception ex)
            {
                return RespuestaGeneral.Error("Error al cambiar contraseña: " + ex.Message);
            }
        }

        public RespuestaGeneral ValidarToken(string token)
        {
            if (AdministradorToken.ValidarToken(token))
            {
                return RespuestaGeneral.Ok("Token válido y activo");
            }

            return RespuestaGeneral.Error("Token inválido o expirado");
        }

        public RespuestaGeneral Logout(string token)
        {
            if (!string.IsNullOrWhiteSpace(token))
            {
                AdministradorToken.InvalidarToken(token);
            }
            return RespuestaGeneral.Ok("Sesión cerrada exitosamente");
        }
    }
}
