using ApiPaisesMVC_SQLServer.Models;
using ApiPaisesMVC_SQLServer.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ApiPaisesMVC_SQLServer.Controllers;

[Authorize]
public class PaisController : Controller
{
    private readonly AppDbContext _context;
    private readonly IPaisApiService _apiService;

    public PaisController(AppDbContext context, IPaisApiService apiService)
    {
        _context = context;
        _apiService = apiService;
    }

    public async Task<IActionResult> Index()
    {
        ViewBag.TotalPaises = await _context.Paises.CountAsync();
        return View();
    }

    /// <summary>
    /// Importa todos los países desde la API REST pública y los guarda/actualiza en SQL Server sin duplicados.
    /// </summary>
    public async Task<IActionResult> Importar()
    {
        List<Pais> paisesApi = await _apiService.ObtenerTodosLosPaisesAsync();
        if (paisesApi.Count == 0)
        {
            TempData["Error"] = "No se pudieron obtener países desde la API pública.";
            return RedirectToAction(nameof(Index));
        }

        var existentes = await _context.Paises
            .ToDictionaryAsync(p => p.Nombre.ToLower(), p => p);

        int insertados = 0;
        int actualizados = 0;

        foreach (Pais item in paisesApi)
        {
            string key = item.Nombre.Trim().ToLowerInvariant();
            if (existentes.TryGetValue(key, out Pais? actual))
            {
                actual.Capital = item.Capital;
                actual.Region = item.Region;
                actual.SubRegion = item.SubRegion;
                actual.Poblacion = item.Poblacion;
                actual.Area = item.Area;
                actual.Moneda = item.Moneda;
                actualizados++;
            }
            else
            {
                _context.Paises.Add(item);
                existentes[key] = item;
                insertados++;
            }
        }

        await _context.SaveChangesAsync();
        TempData["Mensaje"] = $"Importación completada ({_apiService.UltimaFuente}): {insertados} nuevo(s), {actualizados} actualizado(s).";
        return RedirectToAction(nameof(Lista));
    }

    /// <summary>
    /// Actividad 1: Consulta un país específico por nombre en la API pública (ej. ecuador) y opcionalmente lo guarda.
    /// </summary>
    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> ConsultarApiPorNombre(string nombrePais, bool guardarEnBd = true)
    {
        if (string.IsNullOrWhiteSpace(nombrePais))
        {
            TempData["Error"] = "Ingrese el nombre de un país (ejemplo: ecuador).";
            return RedirectToAction(nameof(Index));
        }

        List<Pais> encontrados = await _apiService.ObtenerPaisesPorNombreAsync(nombrePais);
        if (encontrados.Count == 0)
        {
            TempData["Error"] = $"No se encontró el país '{nombrePais}' en la API pública.";
            return RedirectToAction(nameof(Index));
        }

        if (guardarEnBd)
        {
            int insertados = 0;
            int actualizados = 0;
            foreach (Pais item in encontrados)
            {
                Pais? existente = await _context.Paises
                    .FirstOrDefaultAsync(p => p.Nombre.ToLower() == item.Nombre.ToLower());
                if (existente == null)
                {
                    _context.Paises.Add(item);
                    insertados++;
                }
                else
                {
                    existente.Capital = item.Capital;
                    existente.Region = item.Region;
                    existente.SubRegion = item.SubRegion;
                    existente.Poblacion = item.Poblacion;
                    existente.Area = item.Area;
                    existente.Moneda = item.Moneda;
                    actualizados++;
                }
            }
            await _context.SaveChangesAsync();
            TempData["Mensaje"] = $"Consulta API '{nombrePais}' ({_apiService.UltimaFuente}): {encontrados.Count} encontrado(s), {insertados} guardado(s), {actualizados} actualizado(s).";
        }

        ViewBag.TotalPaises = await _context.Paises.CountAsync();
        ViewBag.ResultadoApi = encontrados;
        ViewBag.BusquedaApi = nombrePais;
        return View("Index");
    }

    /// <summary>
    /// Actividades 3 y 4: Listado con búsqueda por Nombre, Región o Capital y filtro dinámico por región.
    /// </summary>
    public async Task<IActionResult> Lista(string? buscar, string? region)
    {
        IQueryable<Pais> query = _context.Paises.AsNoTracking();

        if (!string.IsNullOrWhiteSpace(buscar))
        {
            string term = buscar.Trim().ToLower();
            query = query.Where(p =>
                p.Nombre.ToLower().Contains(term) ||
                (p.Capital != null && p.Capital.ToLower().Contains(term)) ||
                p.Region.ToLower().Contains(term));
        }

        string regionNormalizada = NormalizarRegionApi(region);
        if (!string.IsNullOrWhiteSpace(regionNormalizada))
        {
            string rLower = regionNormalizada.ToLower();
            query = query.Where(p => p.Region.ToLower() == rLower);
        }

        List<Pais> lista = await query.OrderBy(p => p.Nombre).ToListAsync();
        ViewBag.Buscar = buscar;
        ViewBag.RegionSeleccionada = string.IsNullOrWhiteSpace(region) ? "Todas" : region;
        return View(lista);
    }

    // --- Actividad 2: CRUD Completo ---

    [HttpGet]
    public IActionResult Crear()
    {
        return View(new Pais());
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Crear(Pais pais)
    {
        if (!ModelState.IsValid)
        {
            return View(pais);
        }

        pais.Region = NormalizarRegionApi(pais.Region);
        _context.Paises.Add(pais);
        await _context.SaveChangesAsync();
        TempData["Mensaje"] = $"País '{pais.Nombre}' creado correctamente.";
        return RedirectToAction(nameof(Lista));
    }

    [HttpGet]
    public async Task<IActionResult> Editar(int id)
    {
        Pais? pais = await _context.Paises.FindAsync(id);
        if (pais == null)
        {
            return NotFound();
        }
        return View(pais);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Editar(int id, Pais pais)
    {
        if (id != pais.Id)
        {
            return BadRequest();
        }
        if (!ModelState.IsValid)
        {
            return View(pais);
        }

        pais.Region = NormalizarRegionApi(pais.Region);
        _context.Update(pais);
        await _context.SaveChangesAsync();
        TempData["Mensaje"] = $"País '{pais.Nombre}' actualizado correctamente.";
        return RedirectToAction(nameof(Lista));
    }

    [HttpGet]
    public async Task<IActionResult> Eliminar(int id)
    {
        Pais? pais = await _context.Paises.AsNoTracking().FirstOrDefaultAsync(p => p.Id == id);
        if (pais == null)
        {
            return NotFound();
        }
        return View(pais);
    }

    [HttpPost, ActionName("Eliminar")]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> EliminarConfirmado(int id)
    {
        Pais? pais = await _context.Paises.FindAsync(id);
        if (pais != null)
        {
            _context.Paises.Remove(pais);
            await _context.SaveChangesAsync();
            TempData["Mensaje"] = $"País '{pais.Nombre}' eliminado correctamente.";
        }
        return RedirectToAction(nameof(Lista));
    }

    /// <summary>
    /// Actividad 5: Reportes estadísticos (cantidad por región, país más poblado y promedio poblacional por región).
    /// </summary>
    public async Task<IActionResult> Reportes()
    {
        List<Pais> paises = await _context.Paises.AsNoTracking().ToListAsync();

        List<RegionEstadisticaItem> porRegion = paises
            .GroupBy(p => string.IsNullOrWhiteSpace(p.Region) ? "Sin región" : p.Region)
            .Select(g => new RegionEstadisticaItem
            {
                Region = g.Key,
                CantidadPaises = g.Count(),
                PromedioPoblacion = g.Average(x => (double)x.Poblacion),
                PoblacionTotal = g.Sum(x => x.Poblacion)
            })
            .OrderByDescending(r => r.CantidadPaises)
            .ThenBy(r => r.Region)
            .ToList();

        var vm = new ReportesViewModel
        {
            TotalPaises = paises.Count,
            PaisMasPoblado = paises.OrderByDescending(p => p.Poblacion).FirstOrDefault(),
            RegionConMasPaises = porRegion.FirstOrDefault(),
            EstadisticasPorRegion = porRegion
        };

        return View(vm);
    }

    public static string NormalizarRegionApi(string? region)
    {
        if (string.IsNullOrWhiteSpace(region))
        {
            return string.Empty;
        }
        string r = region.Trim();
        string lower = r.ToLowerInvariant();
        if (lower is "todas" or "all")
        {
            return string.Empty;
        }
        if (lower.Contains("américa") || lower.Contains("america"))
        {
            return "Americas";
        }
        if (lower.Contains("europa") || lower.Contains("europe"))
        {
            return "Europe";
        }
        if (lower.Contains("áfrica") || lower.Contains("africa"))
        {
            return "Africa";
        }
        if (lower.Contains("oceanía") || lower.Contains("oceania"))
        {
            return "Oceania";
        }
        if (lower.Contains("asia"))
        {
            return "Asia";
        }
        return r;
    }
}
