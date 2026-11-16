# Taller Práctico 2: Consumo de APIs Públicas en .NET con SQL Server aplicando el Patrón Arquitectónico MVC

Aplicación web en **ASP.NET Core MVC (.NET 10)** con **Entity Framework Core** y **SQL Server**, que incluye autenticación de usuario, consumo de APIs públicas REST (`REST Countries` y `Universities Hipolabs`), CRUD completo, filtros dinámicos y reportes estadísticos.

## Credenciales de Acceso

- **Usuario:** `Monster`
- **Contraseña:** `Monster9`
- **Imagen de Login:** `ApiPaisesMVC_SQLServer/wwwroot/images/login.jpeg`

## Estructura del Proyecto

```text
Taller-2/
├── sql/
│   └── ApiPaisesDB.sql
└── ApiPaisesMVC_SQLServer/
    ├── ApiPaisesMVC_SQLServer.csproj
    ├── appsettings.json
    ├── Program.cs
    ├── Controllers/
    │   ├── AccountController.cs
    │   ├── PaisController.cs
    │   └── UniversidadController.cs
    ├── Models/
    │   ├── AppDbContext.cs
    │   ├── LoginViewModel.cs
    │   ├── Pais.cs
    │   ├── ReportesViewModel.cs
    │   └── Universidad.cs
    ├── Services/
    │   ├── PaisApiService.cs
    │   └── UniversidadApiService.cs
    ├── Views/
    │   ├── Account/Login.cshtml
    │   ├── Pais/ (Index, Lista, Crear, Editar, Eliminar, Reportes)
    │   ├── Universidad/Index.cshtml
    │   └── Shared/_Layout.cshtml
    └── wwwroot/images/login.jpeg
```

## Cómo Ejecutar

1. **Base de datos SQL Server**:
   - Ejecutar `sql/ApiPaisesDB.sql` en SQL Server (`localhost,1433`), o revisar la cadena de conexión en `ApiPaisesMVC_SQLServer/appsettings.json`.
2. **Compilar y ejecutar**:
   ```bash
   cd ApiPaisesMVC_SQLServer
   dotnet run --urls http://localhost:5180
   ```
3. Abrir `http://localhost:5180` en el navegador e iniciar sesión con `Monster` / `Monster9`.

## Funcionalidades Implementadas

- **Login con Cookies MVC**: protección de rutas con `[Authorize]`, vista con imagen y validación de credenciales.
- **Importación masiva REST Countries**: parseo con `Newtonsoft.Json` de `Nombre`, `Capital`, `Region`, `SubRegion`, `Poblacion`, `Area` y `Moneda`, con respaldo automático en `https://www.apicountries.com` ante la deprecación de `restcountries.com/v3.1`.
- **Actividad 1**: Consulta de un país por nombre desde la API pública (ej. `ecuador`).
- **Actividad 2**: CRUD completo (`Crear`, `Lista`, `Editar`, `Eliminar`) sobre la tabla `Paises` en SQL Server.
- **Actividad 3 y 4**: Búsquedas por Nombre, Región o Capital y filtros dinámicos por región (`América`, `Europa`, `Asia`, `África`, `Oceanía`).
- **Actividad 5**: Reportes estadísticos (cantidad de países por región, país más poblado y promedio poblacional por región).
- **Actividad Avanzada**: Consumo e importación de universidades por país desde `http://universities.hipolabs.com/search?country=Ecuador` en la tabla `Universidades`.
