using ec.edu.monster.view;
using Microsoft.Maui.Controls;
using Microsoft.Maui.Graphics;

namespace ec.edu.monster
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
                BarBackgroundColor = Color.FromArgb("#0F172A"),
                BarTextColor = Colors.White
            });
        }
    }
}
