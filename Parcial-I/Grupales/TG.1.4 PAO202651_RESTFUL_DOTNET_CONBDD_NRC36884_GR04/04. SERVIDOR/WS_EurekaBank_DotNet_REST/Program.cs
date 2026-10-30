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

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddCors(opt => opt.AddDefaultPolicy(p => p.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod()));

var app = builder.Build();

app.UseCors();
app.UseSwagger();
app.UseSwaggerUI();
app.MapControllers();

app.MapGet("/", () =>
{
    var bdd = new EurekaBankBddModel();
    var cuentas = bdd.ListarCuentasPorSucursal(0);
    return Results.Content(EstadoServidorView.RenderizarHtml(cuentas), "text/html; charset=utf-8");
});

app.Run();
