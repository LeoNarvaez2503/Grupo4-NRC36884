# Ítem 12: Diagrama de Arquitectura de la Solución Completa (Puntaje: 1.0 / 1.0)

La solución adopta un patrón arquitectónico distribuido orientado a servicios web RESTful, desacoplando completamente la aplicación comercializadora de venta de celulares del core bancario financiero BanQuito.

```mermaid
graph TD
    subgraph CapaPresentacion["Capa de Presentación (Frontend Web)"]
        Browser["💻 Navegador Web (Operador / Asesor de Ventas)<br/>• Interfaz Reactiva HTML5 / Bootstrap 5 / Vanilla JS<br/>• Verificación de Crédito en Tiempo Real (AJAX)<br/>• Visor Tabular de Amortización Francesa"]
    end

    subgraph CapaComercializadora["Nodo Comercializadora (Java Web App - Puerto :8090)"]
        WebApp["🚀 ComercializadoraApp<br/>(Servidor HTTP Embebido / Payara WAR)"]
        CatService["📦 Gestión de Catálogo CRUD (Sin Inventario)"]
        FacService["🧾 Servicio de Facturación<br/>• Efectivo: Aplica 42% descuento<br/>• Crédito: Orquesta consumo con BanQuito"]
        RestClient["🌐 BanQuitoRestClient<br/>(java.net.http.HttpClient + Gson)"]
    end

    subgraph CapaBanQuito["Nodo Core Bancario BanQuito (Servicios Web RESTful - Puerto :8080)"]
        RestApp["🏦 ServidorApp / JAX-RS CreditoResource<br/>(Contrato RESTful JSON)"]
        CredService["⚖️ CreditoService<br/>• Validación 4 Políticas Crediticias<br/>• Cálculo Monto Máximo: ((Dep-Ret)*30%)*6<br/>• Generador Amortización Francés 16.5%"]
        DAOBanquito["💾 Capa DAO JDBC BanQuito<br/>(ClienteDAO, CreditoDAO)"]
    end

    subgraph CapaPersistencia["Capa de Persistencia (MySQL 8.0 Contenerizado en Podman :3306)"]
        subgraph DBComercializadora["Base de Datos: comercializadora_db"]
            T_Tel["📱 TELEFONO_CELULAR"]
            T_Fac["📄 FACTURA"]
        end
        subgraph DBBanQuito["Base de Datos: banquito_db"]
            T_Cli["👤 CLIENTE (5)"]
            T_Cta["💳 CUENTA (5)"]
            T_Mov["📊 MOVIMIENTO (55+)"]
            T_Cre["📋 CREDITO"]
            T_Amort["📑 TABLA_AMORTIZACION"]
        end
    end

    Browser <==>|"HTTP / JSON (Puerto :8090)"| WebApp
    WebApp --> CatService
    WebApp --> FacService
    FacService --> RestClient

    CatService -->|"JDBC"| DBComercializadora
    FacService -->|"JDBC"| DBComercializadora

    RestClient <==>|"Protocolo RESTful HTTP / JSON (Puerto :8080)<br/>GET /es-sujeto, GET /monto-maximo, POST /otorgar"| RestApp

    RestApp --> CredService
    CredService --> DAOBanquito
    DAOBanquito -->|"JDBC"| DBBanQuito
```

---

### Descripción Técnica de las Capas Arquitectónicas:

1. **Capa de Presentación (Frontend Web)**:
   - Implementada en HTML5 estándar con **Bootstrap 5.3** y **Bootstrap Icons**.
   - Consume endpoints asíncronos mediante la API nativa `fetch` de JavaScript.
   - Brinda una botonera interactiva para probar los **4 Clientes Oficiales del Tribunal** con un solo clic.

2. **Capa de Lógica de Negocio Comercializadora (Java Web Client :8090)**:
   - Administra el catálogo de teléfonos celulares (Ítem 8).
   - Liquida ventas en **Efectivo** otorgando el **42% de descuento**.
   - Consume en vivo el servicio web RESTful de BanQuito cuando la forma de pago es **Crédito Directo**.

3. **Capa de Servicios Web BanQuito Core (Java RESTful :8080)**:
   - Publica endpoints RESTful bajo el estándar **Jakarta RESTful Web Services (JAX-RS / Jersey)**.
   - Ejecuta las 4 reglas de negocio de evaluación crediticia.
   - Aplica la fórmula oficial de monto máximo: `((Promedio Depósitos - Promedio Retiros) * 30%) * 6`.
   - Genera y persiste la tabla de amortización con cuota fija mensual al **16.5% anual**.

4. **Capa de Persistencia (MySQL 8.0 en Podman :3306)**:
   - Dos esquemas independientes y normalizados: `comercializadora_db` y `banquito_db`.
   - Poblado con datos históricos consistentes que satisfacen los 5 clientes, 5 cuentas y más de 50 movimientos requeridos.
