using ApiPaisesMVC_SQLServer.Models;
using ApiPaisesMVC_SQLServer.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace ApiPaisesMVC_SQLServer.Controllers;

[Authorize]
public class UniversidadController : Controller
{
    private readonly AppDbContext _context;
    private readonly IUniversidadApiService _apiService;

    public UniversidadController(AppDbContext context, IUniversidadApiService apiService)
    {
        _context = context;
        _apiService = apiService;
    }

    public async Task<IActionResult> Index(string? paisFiltro)
    {
        IQueryable<Universidad> query = _context.Universidades.AsNoTracking();
        if (!string.IsNullOrWhiteSpace(paisFiltro))
        {
            string f = paisFiltro.Trim().ToLower();
            query = query.Where(u => u.Pais != null && u.Pais.ToLower().Contains(f));
        }

        ViewBag.PaisFiltro = paisFiltro;
        List<Universidad> lista = await query.OrderBy(u => u.Nombre).ToListAsync();
        return View(lista);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Importar(string pais = "Ecuador")
    {
        string targetPais = string.IsNullOrWhiteSpace(pais) ? "Ecuador" : pais.Trim();
        List<Universidad> obtenidas = await _apiService.ObtenerUniversidadesPorPaisAsync(targetPais);
        if (obtenidas.Count == 0)
        {
            TempData["Error"] = $"No se encontraron universidades en la API para '{targetPais}'.";
            return RedirectToAction(nameof(Index));
        }

        var existentes = await _context.Universidades
            .Select(u => (u.Nombre + "|" + (u.Pais ?? "")).ToLower())
            .ToHashSetAsync();

        int insertadas = 0;
        foreach (Universidad u in obtenidas)
        {
            string key = $"{u.Nombre}|{u.Pais ?? ""}".ToLowerInvariant();
            if (!existentes.Contains(key))
            {
                _context.Universidades.Add(u);
                existentes.Add(key);
                insertadas++;
            }
        }

        await _context.SaveChangesAsync();
        TempData["Mensaje"] = $"Universidades de '{targetPais}': {obtenidas.Count} consultadas, {insertadas} nueva(s) guardada(s) en SQL Server.";
        return RedirectToAction(nameof(Index), new { paisFiltro = targetPais });
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public async Task<IActionResult> Eliminar(int id)
    {
        Universidad? u = await _context.Universidades.FindAsync(id);
        if (u != null)
        {
            _context.Universidades.Remove(u);
            await _context.SaveChangesAsync();
            TempData["Mensaje"] = $"Universidad '{u.Nombre}' eliminada.";
        }
        return RedirectToAction(nameof(Index));
    }
}
