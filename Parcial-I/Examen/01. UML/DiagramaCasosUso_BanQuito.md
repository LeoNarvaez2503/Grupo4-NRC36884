# Ítem 1: Diagrama de Casos de Uso del Módulo de Crédito del Banco BanQuito (Puntaje: 2.0 / 2.0)

```mermaid
flowchart LR
    subgraph Actores["Actores del Sistema"]
        Cliente["👤 Cliente / Solicitante"]
        Comercializadora["🏢 Comercializadora Celulares\n(Sistema Externo Web)"]
        Oficial["👔 Oficial / Core BanQuito"]
    end

    subgraph ModuloCredito["Sistema BanQuito - Módulo de Crédito"]
        CU01["CU01: Verificar si el Cliente es Sujeto de Crédito"]
        CU02["CU02: Calcular Monto Máximo de Crédito Aprobado"]
        CU03["CU03: Otorgar Crédito y Registrar Tabla Amortización"]
        CU04["CU04: Consultar Tabla de Amortización"]
        CU05["CU05: Simular Cuota Fija Mensual (Sistema Francés)"]

        RN1["«include» Validar Registro Cliente en Core"]
        RN2["«include» Validar Depósito en Último Mes (30d)"]
        RN3["«include» Validar Edad >= 25 años si es Masculino"]
        RN4["«include» Validar Inexistencia de Crédito Activo"]
    end

    Cliente -->|Solicita compra a crédito| Comercializadora
    Comercializadora -->|GET /api/credito/es-sujeto| CU01
    Comercializadora -->|GET /api/credito/monto-maximo| CU02
    Comercializadora -->|POST /api/credito/otorgar| CU03
    Comercializadora -->|GET /api/credito/tabla-amortizacion| CU04
    Comercializadora -->|GET /api/credito/simular| CU05

    CU01 -.->|include| RN1
    CU01 -.->|include| RN2
    CU01 -.->|include| RN3
    CU01 -.->|include| RN4

    CU03 -.->|include| CU01
    CU03 -.->|include| CU02

    Oficial -->|Consulta y Auditoría| CU04
```

### Relación de Casos de Uso y Servicios Web RESTful Expuestos:

| Identificador | Caso de Uso | Método HTTP | Endpoint RESTful |
| :--- | :--- | :---: | :--- |
| **CU01** | Verificar Sujeto de Crédito | `GET` | `/api/credito/es-sujeto/{cedula}` |
| **CU02** | Calcular Monto Máximo Aprobado | `GET` | `/api/credito/monto-maximo/{codCliente}` |
| **CU03** | Otorgar Crédito Directo y Tabla | `POST` | `/api/credito/otorgar` |
| **CU04** | Consultar Tabla de Amortización | `GET` | `/api/credito/tabla-amortizacion/{codCredito}` |
| **CU05** | Simulación Cuota Fija Francesa | `GET` | `/api/credito/simular?monto=450&cuotas=6` |
