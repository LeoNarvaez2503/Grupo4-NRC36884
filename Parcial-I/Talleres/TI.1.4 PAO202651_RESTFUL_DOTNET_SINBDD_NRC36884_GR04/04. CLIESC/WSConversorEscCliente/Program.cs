using System;
using System.Windows.Forms;
using ec.edu.monster.vista;

namespace WSConversorEscCliente
{
    internal static class Program
    {
        /// <summary>
        /// Punto de entrada principal para la aplicación.
        /// </summary>
        [STAThread]
        static void Main()
        {
            ApplicationConfiguration.Initialize();
            Application.Run(new LoginView());
        }
    }
}