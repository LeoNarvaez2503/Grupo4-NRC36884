using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using ec.edu.monster.controller;

var builder = WebApplication.CreateBuilder(args);

string? ep = builder.Configuration["EurekaBank:EndpointUrl"];
if (!string.IsNullOrWhiteSpace(ep))
{
    AppControlador.EndpointGlobal = ep;
}

// Configuración MVC Tradicional ubicando las vistas en ec.edu.monster.view
builder.Services.AddControllersWithViews()
    .AddRazorOptions(options =>
    {
        options.ViewLocationFormats.Clear();
        options.ViewLocationFormats.Add("/ec.edu.monster.view/{1}/{0}.cshtml");
        options.ViewLocationFormats.Add("/ec.edu.monster.view/{0}.cshtml");
    });

var app = builder.Build();

app.UseStaticFiles();
app.UseRouting();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Login}/{action=Index}/{id?}");

app.Run();
