# Taller Práctico 1: Consumo de APIs Públicas en Java con MySQL aplicando el Patrón MVC

Aplicación de escritorio Java Swing desarrollada con **Maven** y el patrón arquitectónico **MVC (Modelo - Vista - Controlador)** + **DAO**, que consume la API pública de países, persiste los registros en **MySQL** y cubre todas las actividades del taller.

## Estructura del Proyecto

```text
Taller-1/
├── pom.xml
├── sql/
│   └── api_paises.sql
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── Main.java
    │   │   ├── modelo/
    │   │   │   ├── Pais.java
    │   │   │   ├── Conexion.java
    │   │   │   ├── ConsumoAPI.java
    │   │   │   └── PaisDAO.java
    │   │   ├── controlador/
    │   │   │   └── PaisController.java
    │   │   └── vista/
    │   │       └── FrmPais.java
    │   └── resources/
    │       └── db.properties
    └── test/
        └── java/
            └── TallerPaisesTest.java
```

## Requisitos e Inicio Rápido

1. **Base de datos MySQL** (puerto `3306`):
   - Ejecutar `sql/api_paises.sql` para crear la base de datos `api_paises` y la tabla `pais`.
   - Configurar credenciales en `src/main/resources/db.properties` (o mediante variables de entorno `DB_URL`, `DB_USER`, `DB_PASS`). Por defecto apunta a `jdbc:mysql://localhost:3306/api_paises`, usuario `root`, contraseña `admin`.
2. **Compilar y probar con Maven**:
   ```bash
   mvn test
   ```
3. **Ejecutar la aplicación Swing** (desde NetBeans 21+ o por consola):
   ```bash
   mvn compile exec:java
   ```

## Funcionalidades Implementadas

- **Importación masiva desde API REST**: botón `IMPORTAR PAÍSES (API)` que obtiene los países en JSON con `Gson`, extrae nombre, capital, región, población y moneda, y los inserta/actualiza en MySQL sin duplicados.
- **Nota sobre REST Countries**: `ConsumoAPI` consulta primero `https://restcountries.com/v3.1/...` y, ante la deprecación de v3.1 (que exige migración a `v5` con API Key), realiza respaldo automático contra el espejo público sin clave `https://www.apicountries.com/...`, parseando ambos esquemas JSON.
- **Actividad 1**: Consulta de un país por nombre en la API pública (ej. `ecuador`) con carga en el formulario y persistencia opcional en MySQL.
- **Actividad 2**: CRUD completo sobre la tabla `pais` (Crear, Leer, Actualizar, Eliminar).
- **Actividad 3**: Visualización interactiva de países en `JTable` con ordenamiento por columnas y selección para edición.
- **Actividad 4**: Búsqueda y filtrado por región (`América`, `Europa`, `Asia`, `África`, `Oceanía`) tanto en MySQL como por importación desde la API.
- **Actividad 5**: Panel de reportes estadísticos en tiempo real:
  - Número de países por región.
  - País más poblado.
  - Región con más países.
