using ec.edu.monster.vista;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Graphics;

namespace WSConversorMovCliente
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();
        }

        protected override Window CreateWindow(IActivationState? activationState)
        {
            return new Window(new NavigationPage(new LoginView())
            {
                BarBackgroundColor = Color.FromArgb("#0284C7"),
                BarTextColor = Colors.White
            });
        }
    }
}