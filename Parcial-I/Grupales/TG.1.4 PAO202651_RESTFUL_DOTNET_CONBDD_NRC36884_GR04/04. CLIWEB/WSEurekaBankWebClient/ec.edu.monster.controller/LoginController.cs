using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class LoginController : Controller
    {
        private readonly AppControlador _controlador = new();

        [HttpGet]
        public IActionResult Index()
        {
            if (AppControlador.LeerSesionCookie(Request) != null)
                return RedirectToAction("Index", "Dashboard");

            return View("~/ec.edu.monster.view/Login/Index.cshtml", new LoginWebModel());
        }

        [HttpPost]
        public IActionResult Index(LoginWebModel model)
        {
            var res = _controlador.IniciarSesion(model.Usuario, model.Clave);
            if (!res.Autenticado)
            {
                model.MensajeError = res.Mensaje;
                return View("~/ec.edu.monster.view/Login/Index.cshtml", model);
            }

            int vent = model.Ventanilla is >= 1 and <= 4 ? model.Ventanilla : 1;
            AppControlador.GuardarSesionCookie(Response, res, vent);
            return RedirectToAction("Index", "Dashboard");
        }

        [HttpGet]
        public IActionResult Logout()
        {
            AppControlador.CerrarSesionCookie(Response);
            return RedirectToAction("Index", "Login");
        }
    }
}
