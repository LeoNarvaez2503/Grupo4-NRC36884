using System.Windows.Forms;
using ec.edu.monster.view;

namespace ec.edu.monster
{
    internal static class Program
    {
        [STAThread]
        static void Main()
        {
            ApplicationConfiguration.Initialize();
            Application.Run(new LoginView());
        }
    }
}
