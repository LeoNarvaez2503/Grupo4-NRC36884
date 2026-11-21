using Microsoft.EntityFrameworkCore;

namespace ApiPaisesMVC_SQLServer.Models;

public class AppDbContext : DbContext
{
    public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
    {
    }

    public DbSet<Pais> Paises => Set<Pais>();
    public DbSet<Universidad> Universidades => Set<Universidad>();
}
