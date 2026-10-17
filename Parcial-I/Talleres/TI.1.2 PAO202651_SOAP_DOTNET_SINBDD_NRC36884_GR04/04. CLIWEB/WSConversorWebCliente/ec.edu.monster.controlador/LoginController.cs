using System;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.modelo;

namespace ec.edu.monster.controlador
{
    public class LoginController : Controller
    {
        private readonly AppControlador controlador = new AppControlador();

        [HttpGet]
        [Route("")]
        [Route("Login")]
        [Route("Login/Index")]
        public IActionResult Index()
        {
            if (!string.IsNullOrEmpty(HttpContext.Session.GetString("Token")))
            {
                return RedirectToAction("Index", "Conversor");
            }
            return View("~/Views/Login/Index.cshtml", new LoginViewModel());
        }

        [HttpPost]
        [Route("Login")]
        [Route("Login/Index")]
        [ValidateAntiForgeryToken]
        public IActionResult Index(LoginViewModel model)
        {
            string usuario = model.Usuario?.Trim() ?? "";
            string contrasenia = model.Contrasenia ?? "";

            if (string.IsNullOrEmpty(usuario) || string.IsNullOrEmpty(contrasenia))
            {
                model.MensajeError = "Por favor escribe el usuario y la contraseña para continuar.";
                return View("~/Views/Login/Index.cshtml", model);
            }

            try
            {
                string token = controlador.Login(usuario, contrasenia);

                if (!string.IsNullOrWhiteSpace(token))
                {
                    HttpContext.Session.SetString("Usuario", usuario);
                    HttpContext.Session.SetString("Token", token);
                    return RedirectToAction("Index", "Conversor");
                }
                else
                {
                    model.MensajeError = "Usuario o contraseña incorrectos. Verifica tus credenciales e intenta nuevamente.";
                    return View("~/Views/Login/Index.cshtml", model);
                }
            }
            catch (System.ServiceModel.FaultException)
            {
                model.MensajeError = "Usuario o contraseña incorrectos. Verifica tus credenciales e intenta nuevamente.";
                return View("~/Views/Login/Index.cshtml", model);
            }
            catch (System.ServiceModel.EndpointNotFoundException)
            {
                model.MensajeError = "No se pudo conectar con el servidor web SOAP. Por favor, asegúrate de que el servidor esté iniciado.";
                return View("~/Views/Login/Index.cshtml", model);
            }
            catch (TimeoutException)
            {
                model.MensajeError = "El servidor tardó demasiado tiempo en responder. Por favor, verifica tu conexión e intenta de nuevo.";
                return View("~/Views/Login/Index.cshtml", model);
            }
            catch (Exception ex)
            {
                if (ex.ToString().Contains("denegó expresamente") ||
                    ex.ToString().Contains("SocketException") ||
                    ex.ToString().Contains("ConnectFailure") ||
                    ex.ToString().Contains("No connection could be made"))
                {
                    model.MensajeError = "No se pudo conectar con el servidor web SOAP. Por favor, asegúrate de que el servidor esté iniciado.";
                }
                else
                {
                    model.MensajeError = "Ocurrió un error al intentar iniciar sesión: " + ex.Message;
                }
                return View("~/Views/Login/Index.cshtml", model);
            }
        }

        [HttpGet]
        [Route("Logout")]
        [Route("Login/Logout")]
        public IActionResult Logout()
        {
            HttpContext.Session.Clear();
            return RedirectToAction("Index");
        }
    }
}
