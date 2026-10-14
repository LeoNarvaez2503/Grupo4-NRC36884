# Cliente Web SOAP - Conversor de Unidades (Java Puro | Jakarta EE MVC)

**NRC:** 36884 | **Grupo:** 4 | **PAO:** 202651

Aplicación Web desarrollada en **Java Puro con Jakarta EE 10** para despliegue en **Payara Server / GlassFish**, implementando estrictamente el patrón arquitectónico **Modelo-Vista-Controlador (MVC)** y consumiendo el servicio web SOAP (`WSConversorUnidades`).

---

## 🏛️ Patrón Arquitectónico MVC (Java EE / Jakarta EE)

```
             Petición HTTP (GET/POST)
Cliente  ───────────────────────────────>  [ CONTROLADOR ]
(Browser)                                   - LoginServlet.java
   │                                        - ConversorServlet.java
   │                                        - LogoutServlet.java
   │                                        - AuthFilter.java (Seguridad)
   │                                                │
   │                                                ▼
   │                                        [ MODELO & SERVICIO ]
   │                                        - AppControlador.java
   │                                        - ConversorService.java (JAX-WS)
   │                                        - UsuarioSession.java
   │                                        - HistorialItem.java
   │                                        - Enums: UnidadLongitud, UnidadMasa, etc.
   │                                                │
   │                                                ▼ Invoca SOAP
   │                                        [ SERVIDOR SOAP ] (Payara)
   │                                        http://localhost:8080/WS_Conversor...
   │                                                │
   │ <──────────────────────────────────────────────┘ Retorna Datos
   │
   ▼
[ VISTA (JSP) ]
- login.jsp (Formulario con glassmorphism y auto-llenado de credenciales)
- conversor.jsp (Selector por pestañas, tarjetas dinámicas e historial de sesión)
- css/styles.css & js/app.js (Interfaz moderna con estética dark glassmorphism)
```

---

## 📂 Estructura de Paquetes y Clases

- **Controlador (`ec.edu.monster.wsconversorweb.controller`):**
  - `LoginServlet.java`: Gestiona la autenticación con el servidor SOAP y almacena la sesión.
  - `ConversorServlet.java`: Recibe peticiones de conversión, invoca al modelo y gestiona el historial.
  - `LogoutServlet.java`: Invalida la sesión del usuario.
  - `AuthFilter.java`: Filtro Jakarta EE que protege las rutas `/conversor` y `/conversor.jsp`.
  - `AppControlador.java`: Fachada controladora de la lógica de aplicación.

- **Servicio & SOAP (`ec.edu.monster.wsconversorweb.service` / `ec.edu.monster.ws`):**
  - `ConversorService.java`: Servicio cliente JAX-WS que consume las operaciones SOAP.
  - `WSConversorUnidades.java`: Interfaz SEI Jakarta XML Web Services.
  - `WSConversorUnidades_Service.java`: Clase Service Jakarta JAX-WS que conecta con el WSDL.

- **Modelo (`ec.edu.monster.wsconversorweb.model`):**
  - `UsuarioSession.java`: Bean de sesión para el usuario autenticado.
  - `HistorialItem.java`: Bean que representa cada conversión realizada en la sesión.
  - `UnidadLongitud.java`, `UnidadMasa.java`, `UnidadTemperatura.java`: Enums de magnitudes.

- **Vistas (`src/main/webapp`):**
  - `login.jsp`: Vista de autenticación con feedback de errores e indicador de credenciales.
  - `conversor.jsp`: Vista del conversor multimagnitud con pestañas, tarjetas reactivas y tabla de historial.
  - `css/styles.css`: Estilos visuales en modo oscuro (glassmorphism).
  - `js/app.js`: Interactividad de cliente (cambio de unidades, botón de intercambio ⇄, copiar resultado).
  - `WEB-INF/web.xml`: Descriptor de despliegue Jakarta EE 10.

---

## 🔑 Credenciales Oficiales
- **Usuario:** `MONSTER`
- **Contraseña:** `MONSTER9`

---

## ⚙️ Despliegue y Ejecución

### 1. Iniciar el Servidor SOAP en Payara
Asegurarse de que el servicio SOAP esté desplegado y accesible:
`http://localhost:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl`

### 2. Desplegar el Cliente Web en Payara Server

#### Desde NetBeans:
1. Abrir el proyecto `WSConversorWebClient` en NetBeans.
2. Hacer clic derecho en el proyecto y seleccionar **Run** o **Deploy** (utiliza Payara Server configurado).

#### Desde la Terminal con Maven:
```bash
mvn clean package
```
El archivo `.war` generado se encuentra en:
`target/WSConversorWebClient-1.0-SNAPSHOT.war`

Copia este archivo a la carpeta `autodeploy` de tu Payara Server:
```bash
cp target/WSConversorWebClient-1.0-SNAPSHOT.war $PAYARA_HOME/glassfish/domains/domain1/autodeploy/
```

### 3. Acceder en el Navegador
👉 **`http://localhost:8080/WSConversorWebClient/`** o **`http://localhost:8080/WSConversorWebClient/login`**
