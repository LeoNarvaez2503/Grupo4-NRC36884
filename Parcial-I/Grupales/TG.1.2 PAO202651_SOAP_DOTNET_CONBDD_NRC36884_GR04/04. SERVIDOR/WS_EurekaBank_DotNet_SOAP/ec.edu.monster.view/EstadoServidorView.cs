using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public static class EstadoServidorView
    {
        public static string RenderizarHtml(List<CuentaModel> cuentas)
        {
            int verdes = cuentas.Count(c => c.EstadoMashup != "ROJO");
            int rojas = cuentas.Count(c => c.EstadoMashup == "ROJO");

            var filas = string.Join("", cuentas.Select(c =>
            {
                string badge = c.EstadoMashup == "ROJO"
                    ? $"<span style='background:#fee2e2;color:#991b1b;padding:4px 10px;border-radius:999px;font-weight:700;'>ROJO (Ventanilla {c.VentanillaOcupada} - {c.BloqueadoPor})</span>"
                    : "<span style='background:#dcfce7;color:#166534;padding:4px 10px;border-radius:999px;font-weight:700;'>VERDE (Libre)</span>";
                return $"<tr><td><b>{c.NumeroCuenta}</b></td><td>{c.NombreCliente} {c.ApellidoCliente}</td><td>${c.Saldo:F2}</td><td>{c.NombreSucursal}</td><td>{badge}</td></tr>";
            }));

            return $@"<!DOCTYPE html>
<html lang='es'>
<head>
  <meta charset='utf-8'/>
  <meta http-equiv='refresh' content='2'/>
  <title>WS_EurekaBank_DotNet_SOAP - ec.edu.monster</title>
  <style>
    body {{ font-family:'Segoe UI',sans-serif; background:#0f172a; color:#f8fafc; margin:0; padding:28px; }}
    .box {{ max-width:980px; margin:auto; background:#1e293b; border:1px solid #334155; border-radius:12px; padding:24px; }}
    h1 {{ margin-top:0; color:#38bdf8; font-size:22px; }}
    a {{ color:#38bdf8; font-weight:700; }}
    table {{ width:100%; border-collapse:collapse; margin-top:16px; background:#0f172a; border-radius:8px; overflow:hidden; }}
    th, td {{ padding:10px 12px; border-bottom:1px solid #334155; text-align:left; font-size:14px; }}
    th {{ background:#334155; color:#e2e8f0; }}
  </style>
</head>
<body>
  <div class='box'>
    <h1>Servidor SOAP .NET (CoreWCF) — EurekaBank GR04 (ec.edu.monster)</h1>
    <p>Contrato WSDL activo: <a href='/EurekaBankWS.svc?wsdl'>http://localhost:5120/EurekaBankWS.svc?wsdl</a></p>
    <p>Cuentas en VERDE: <b>{verdes}</b> | Cuentas en ROJO (Ocupadas): <b>{rojas}</b></p>
    <table>
      <thead><tr><th>Cuenta</th><th>Titular</th><th>Saldo</th><th>Sucursal</th><th>Semáforo MashUp</th></tr></thead>
      <tbody>{filas}</tbody>
    </table>
  </div>
</body>
</html>";
        }
    }
}
