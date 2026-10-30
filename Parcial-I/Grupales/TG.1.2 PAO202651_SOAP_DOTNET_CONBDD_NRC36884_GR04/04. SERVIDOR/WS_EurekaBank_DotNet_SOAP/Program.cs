using CoreWCF;
using CoreWCF.Channels;
using CoreWCF.Configuration;
using CoreWCF.Description;
using ec.edu.monster.controller;
using ec.edu.monster.model;
using ec.edu.monster.view;

var builder = WebApplication.CreateBuilder(args);

string? connStr = builder.Configuration["EurekaBank:ConnectionString"];
if (!string.IsNullOrWhiteSpace(connStr))
{
    EurekaBankBddModel.ConnectionString = connStr;
    ConcurrenciaMashupModel.ConnectionString = connStr;
}
if (int.TryParse(builder.Configuration["EurekaBank:LockDelayMs"], out int delayMs))
{
    EurekaBankBddModel.LockDelayMs = delayMs;
}

builder.Services.AddServiceModelServices();
builder.Services.AddServiceModelMetadata();
builder.Services.AddSingleton<EurekaBankSoapController>();

var app = builder.Build();

app.UseServiceModel(serviceBuilder =>
{
    serviceBuilder.AddService<EurekaBankSoapController>(options =>
    {
        options.DebugBehavior.IncludeExceptionDetailInFaults = true;
    });

    var binding = new BasicHttpBinding(BasicHttpSecurityMode.None)
    {
        MaxReceivedMessageSize = 10 * 1024 * 1024
    };

    serviceBuilder.AddServiceEndpoint<EurekaBankSoapController, IEurekaBankSoapContrato>(
        binding, "/EurekaBankWS.svc");

    var metadataBehavior = app.Services.GetRequiredService<ServiceMetadataBehavior>();
    metadataBehavior.HttpGetEnabled = true;
});

app.MapGet("/", () =>
{
    var bdd = new EurekaBankBddModel();
    var cuentas = bdd.ListarCuentasPorSucursal(0);
    return Results.Content(EstadoServidorView.RenderizarHtml(cuentas), "text/html; charset=utf-8");
});

app.Run();
