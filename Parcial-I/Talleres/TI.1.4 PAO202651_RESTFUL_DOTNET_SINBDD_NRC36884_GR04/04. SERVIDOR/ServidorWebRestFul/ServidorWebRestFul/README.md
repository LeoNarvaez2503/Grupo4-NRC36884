# Servidor Web RESTful .NET (WCF) - Arquitectura MVC Tradicional (Headless Backend)
**NRC:** 36884 | **Grupo:** 04 | **Tema:** Servidor Web RESTful de Conversión de Unidades y Autenticación sin Base de Datos

---

## 📌 Descripción del Servidor

Este proyecto corresponde **exclusivamente al Servidor Web RESTful**, diseñado bajo el patrón arquitectónico **MVC (Modelo - Vista - Controlador) Tradicional**. 

Como servidor backend puro (**sin interfaz gráfica / sin GUI**), su único propósito es exponer servicios web RESTful mediante el protocolo HTTP y el formato de intercambio de datos **JSON**, actuando como el proveedor central para los diferentes clientes del sistema:
- 🌐 **Cliente Web**
- 🖥️ **Cliente de Escritorio**
- 💻 **Cliente de Consola**
- 📱 **Cliente Móvil**

---

## 🏛️ Patrón Arquitectónico MVC Tradicional en el Servidor (Backend RESTful)

En una arquitectura orientada a servicios REST sin GUI, los tres componentes clásicos de MVC se distribuyen de la siguiente manera:

```
                              ┌──────────────────────────────────────────────────────────────┐
                              │                    CLIENTES CONSUMIDORES                     │
                              │         (Cliente Web, Escritorio, Consola, Móvil)            │
                              └──────────────────────────────┬───────────────────────────────┘
                                                             │ Peticiones HTTP (JSON)
                                                             ▼
                                     ┌──────────────────────────────────────────────┐
                                     │         VISTA / DESPACHADOR REST             │
                                     │ (ServicioWebRest.svc / IWSConversorRest.cs)  │
                                     │  • Expone contratos HTTP (GET / POST)        │
                                     │  • Serialización de Representación JSON      │
                                     │  • Cabeceras HTTP y Soporte CORS             │
                                     └───────────────────────┬──────────────────────┘
                                                             │
                                                             ▼ Delega peticiones
                                     ┌──────────────────────────────────────────────┐
                                     │           CONTROLADOR (Controllers)          │
                                     │  • AutenticacionControlador.cs               │
                                     │  • ConversorControlador.cs                   │
                                     │  (Valida entradas, orquesta el negocio y     │
                                     │   genera los DTOs de salida)                 │
                                     └───────────────────────┬──────────────────────┘
                                                             │
                                                             ▼ Invoca reglas de negocio
┌────────────────────────────────────────────────────────────┴────────────────────────────────────────────────────────┐
│                                                  MODELO (Model)                                                     │
│                                                                                                                     │
│  • modelos/seguridad/    -> AdministradorCredenciales.cs (Monster/Monster9), AdministradorToken.cs (Tokens y vida)  │
│  • modelos/utilidades/   -> IConversor<T>, ConversorLongitud.cs, ConversorMasa.cs, ConversorTemperatura.cs         │
│  • modelos/servicios/    -> ServicioConversor.cs (Lógica genérica de negocio)                                       │
│  • modelos/mapeadores/   -> UnidadMapper.cs (Normalización y mapeo tolerante a variantes)                           │
│  • modelos/enums/        -> UnidadLongitud.cs, UnidadMasa.cs, UnidadTemperatura.cs                                  │
│  • modelos/dto/          -> LoginRequest/Response, ConversionRequest/Response, CambioContraseniaRequest, etc.       │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
```

1. **Modelo (`modelos/`):**
   - **Reglas de Negocio y Utilidades:** Conversión matemática de unidades (Longitud, Masa, Temperatura) a través de la interfaz genérica `IConversor<T>`.
   - **Seguridad en Memoria:** `AdministradorCredenciales` con almacenamiento concurrente seguro y `AdministradorToken` con expiración programada a 30 minutos.
   - **DTOs (Data Transfer Objects):** Clases con contratos `[DataContract]` y `[DataMember]` para la transferencia estructurada de datos.

2. **Controlador (`controladores/`):**
   - `AutenticacionControlador`: Procesa solicitudes de login, verificación de tokens vigentes, invalidación de sesiones y actualización de contraseñas.
   - `ConversorControlador`: Procesa magnitudes físicas, valida compatibilidad entre unidades iniciales y finales, ejecuta cálculos y empaqueta las respuestas `ConversionResponse`.

3. **Vista / Capa de Representación REST (`ServicioWebRest.svc` & `IWSConversorRest`):**
   - En una API REST pura, la "Vista" corresponde a la **capa de representación de datos**: las respuestas serializadas en formato JSON estructurado que viajan de vuelta hacia los clientes con sus respectivos códigos de estado HTTP y cabeceras de CORS.

---

## 🔐 Credenciales del Servidor
* **Usuario:** `Monster`
* **Contraseña:** `Monster9`
* **Almacenamiento:** En memoria mediante `ConcurrentDictionary` (sin base de datos, persistente durante la ejecución del proceso).

---

## 📡 Endpoints RESTful para los Clientes

Todos los endpoints producen y consumen `application/json`.

### 1. Autenticación (`/auth`)

* **Iniciar Sesión (JSON)**
  * **Método:** `POST`
  * **Ruta:** `/ServicioWebRest.svc/auth/login`
  * **Cuerpo:**
    ```json
    {
      "usuario": "Monster",
      "contrasenia": "Monster9"
    }
    ```
  * **Respuesta Exitosa (200 OK):**
    ```json
    {
      "exito": true,
      "token": "7a94cb4c3d824d77b084e318df82e6d1",
      "usuario": "MONSTER",
      "mensaje": "Autenticación exitosa"
    }
    ```

* **Iniciar Sesión (URL Query Params)**
  * **Método:** `GET`
  * **Ruta:** `/ServicioWebRest.svc/auth/login?usuario=Monster&contrasenia=Monster9`

* **Cambiar Contraseña**
  * **Método:** `POST`
  * **Ruta:** `/ServicioWebRest.svc/auth/cambiar-contrasenia`
  * **Cuerpo:**
    ```json
    {
      "contraseniaActual": "Monster9",
      "contraseniaNueva": "NuevoPassword123"
    }
    ```

* **Validar Token**
  * **Método:** `GET`
  * **Ruta:** `/ServicioWebRest.svc/auth/validar-token?token={token}`

* **Cerrar Sesión**
  * **Método:** `POST`
  * **Ruta:** `/ServicioWebRest.svc/auth/logout?token={token}`

---

### 2. Conversor de Unidades (`/conversor`)

* **Conversión vía GET (Query Params):**
  * **Ruta:** `/ServicioWebRest.svc/conversor/{tipo}?valor={valor}&unidadInicial={origen}&unidadFinal={destino}`
  * Donde `{tipo}` puede ser: `longitud`, `masa`, `temperatura`.
  * **Ejemplo Longitud:** `/ServicioWebRest.svc/conversor/longitud?valor=5000&unidadInicial=METRO&unidadFinal=KILOMETRO`
  * **Respuesta:**
    ```json
    {
      "exito": true,
      "tipo": "LONGITUD",
      "valorInicial": 5000.0,
      "unidadInicial": "METRO",
      "valorFinal": 5.0,
      "unidadFinal": "KILOMETRO",
      "mensaje": "Conversión de longitud realizada exitosamente",
      "timestamp": "2026-10-24T20:00:00Z"
    }
    ```

* **Conversión vía POST (Cuerpo JSON):**
  * **Ruta:** `/ServicioWebRest.svc/conversor/{tipo}`
  * **Cuerpo:**
    ```json
    {
      "valor": 100,
      "unidadInicial": "CELSIUS",
      "unidadFinal": "FAHRENHEIT"
    }
    ```
  * **Respuesta:**
    ```json
    {
      "exito": true,
      "tipo": "TEMPERATURA",
      "valorInicial": 100.0,
      "unidadInicial": "CELSIUS",
      "valorFinal": 212.0,
      "unidadFinal": "FAHRENHEIT",
      "mensaje": "Conversión de temperatura realizada exitosamente"
    }
    ```

* **Catálogo de Unidades:**
  * **Método:** `GET`
  * **Ruta:** `/ServicioWebRest.svc/conversor/unidades`
  * **Respuesta:** Retorna las listas completas de unidades permitidas para cada magnitud.

---

### 3. Rutas de Compatibilidad Directa
Para compatibilidad con invocaciones directas de clientes heredados:
- `POST /login` y `GET /login?usuario=..&contrasenia=..`
- `GET /convertirLongitud?valor=..&unidadInicial=..&unidadFinal=..`
- `GET /convertirMasa?valor=..&unidadInicial=..&unidadFinal=..`
- `GET /convertirTemperatura?valor=..&unidadInicial=..&unidadFinal=..`
- `POST /cambiarContrasenia`
- Todas las rutas están disponibles tanto bajo `/ServicioWebRest.svc/` como `/Service1.svc/`.
