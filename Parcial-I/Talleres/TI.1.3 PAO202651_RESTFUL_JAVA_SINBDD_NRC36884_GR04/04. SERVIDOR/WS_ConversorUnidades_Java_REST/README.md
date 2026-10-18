# Servidor Web RESTful - Conversor de Unidades
**Universidad de las Fuerzas Armadas ESPE**  
**Carrera:** Ingeniería en Tecnologías de la Información  
**Materia:** Arquitectura de Software  
**Periodo:** PAO 202651 | **NRC:** 36884 | **Grupo:** 04  

---

## 📌 Descripción del Proyecto
Servicio Web **RESTful** de grado empresarial desarrollado en **Java Puro con Jakarta RESTful Web Services 3.1 (JAX-RS)** sobre la plataforma **Jakarta EE 10**, diseñado para su despliegue en **Payara Server 6 / GlassFish 7**.

Proporciona capacidades completas de:
1. **Autenticación en memoria sin base de datos** con gestión de tokens temporales únicos.
2. **Conversión de Unidades de Medida**:
   - **Longitud:** Milímetro, Centímetro, Metro, Kilómetro, Yarda.
   - **Masa:** Miligramo, Gramo, Kilogramo, Tonelada, Onza.
   - **Temperatura:** Celsius, Fahrenheit, Kelvin, Rankine.
3. **Soporte Multi-formato:** Soporta tanto solicitudes HTTP `POST` con cuerpo JSON como `GET` con Query Parameters para simplificar la integración con cualquier cliente (Consola, Escritorio, Móvil y Web).
4. **Filtro CORS:** Habilitado para permitir solicitudes cross-origin desde navegadores y aplicaciones móviles.

---

## 🏗️ Arquitectura de Paquetes
```
ec.edu.monster
├── controladores.rest      # Controladores RESTful JAX-RS y Filtros (CorsFilter)
├── modelos
│   ├── dto                 # Objetos de Transferencia de Datos (Request/Response JSON)
│   └── utilidades          # Lógica pura de conversión e interfaces (IConversor)
│       ├── enums           # Enumeraciones de unidades (Longitud, Masa, Temperatura)
│       └── mapeadores      # UnidadMapper para normalización de texto y tildes
├── seguridad               # Gestión de credenciales en memoria y tokens UUID
└── servicios               # Servicios de aplicación desacoplados (ServicioConversor)
```

---

## 🔐 Credenciales Oficiales
* **Usuario:** `MONSTER`
* **Contraseña:** `MONSTER9`

---

## 🌐 Endpoints de la API (`/api/*`)

### 1. Autenticación (`/api/auth`)
| Método | Endpoint | Descripción | Formato Entrada | Formato Salida |
| :---: | :--- | :--- | :---: | :---: |
| `POST` | `/api/auth/login` | Inicia sesión y retorna token | JSON `{ "usuario", "contrasenia" }` | JSON (`LoginResponse`) |
| `POST` | `/api/auth/cambiar-contrasenia` | Actualiza la contraseña | JSON `{ "contraseniaActual", "contraseniaNueva" }` | JSON (`RespuestaGeneral`) |
| `GET` | `/api/auth/validar-token` | Verifica si el token es válido | Header `Authorization: Bearer <token>` o `?token=` | JSON |
| `POST` | `/api/auth/logout` | Invalida el token | Header `Authorization: Bearer <token>` o `?token=` | JSON |

### 2. Conversor de Unidades (`/api/conversor`)
| Método | Endpoint | Parámetros Query / Body JSON | Ejemplo Salida |
| :---: | :--- | :--- | :--- |
| `GET` / `POST` | `/api/conversor/longitud` | `valor=1000&unidadInicial=METRO&unidadFinal=KILOMETRO` | `{ "exito": true, "valorFinal": 1.0, ... }` |
| `GET` / `POST` | `/api/conversor/masa` | `valor=5000&unidadInicial=GRAMO&unidadFinal=KILOGRAMO` | `{ "exito": true, "valorFinal": 5.0, ... }` |
| `GET` / `POST` | `/api/conversor/temperatura` | `valor=100&unidadInicial=CELSIUS&unidadFinal=FAHRENHEIT` | `{ "exito": true, "valorFinal": 212.0, ... }` |
| `GET` | `/api/conversor/unidades` | Ninguno | Retorna el catálogo completo de unidades |

### 3. Endpoint de Compatibilidad SOAP (`/api/WSConversorUnidades`)
Permite llamar a los métodos con la nomenclatura exacta que se usaba en el Web Service SOAP:
* `/api/WSConversorUnidades/login`
* `/api/WSConversorUnidades/cambiarContrasenia`
* `/api/WSConversorUnidades/convertirLongitud`
* `/api/WSConversorUnidades/convertirMasa`
* `/api/WSConversorUnidades/convertirTemperatura`

---

## 🚀 Compilación y Despliegue

### Compilar y Ejecutar Pruebas:
```bash
mvn clean test
```

### Generar el archivo WAR:
```bash
mvn clean package
```
El archivo resultante se genera en:  
`target/WS_ConversorUnidades_Java_REST.war`

### Despliegue en Payara Server 6:
1. Copiar el archivo `target/WS_ConversorUnidades_Java_REST.war` al directorio `autodeploy` de Payara:
   ```bash
   cp target/WS_ConversorUnidades_Java_REST.war ~/payara6/glassfish/domains/domain1/autodeploy/
   ```
2. O desplegarlo directamente desde Apache NetBeans haciendo clic derecho en el proyecto -> **Deploy**.
3. Acceder a la documentación y pruebas visuales en el navegador:
   ```
   http://localhost:8080/WS_ConversorUnidades_Java_REST/
   ```
