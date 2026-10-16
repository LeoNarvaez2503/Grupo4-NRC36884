# Cliente Web SOAP - Conversor de Unidades (Jakarta Faces / JSF + XML | MVC)

**NRC:** 36884 | **Grupo:** 4 | **PAO:** 202651

Aplicación Web desarrollada en **Java Puro con Jakarta Faces (JSF 4.0) y Facelets XML** para despliegue en **Payara Server / GlassFish**, implementando estrictamente el patrón arquitectónico **Modelo-Vista-Controlador (MVC)** y consumiendo el servicio web SOAP (`WSConversorUnidades`).

---

## 🏛️ Patrón Arquitectónico MVC (Jakarta Faces / JSF)

En Jakarta Faces, la interfaz de usuario se define mediante **vistas XML declarativas (`.xhtml`)** con componentes visuales de Java (`<h:form>`, `<h:inputText>`, `<h:commandButton>`), y la lógica de negocio y control reside 100% en **Beans de Java (`@Named @SessionScoped`)**:

```
                       Petición HTTP / AJAX
Navegador Web ──────────────────────────────────────> [ FACES SERVLET ]
(Cliente)                                                    │
   │                                                         ▼
   │                                                 [ CONTROLADORES ]
   │                                                 - LoginBean.java (@Named)
   │                                                 - ConversorBean.java (@Named)
   │                                                 - AppControlador.java
   │                                                         │
   │                                                         ▼
   │                                                 [ MODELO & SERVICIO ]
   │                                                 - ConversorService.java (JAX-WS)
   │                                                 - UsuarioSession.java
   │                                                 - HistorialItem.java
   │                                                 - Enums: Magnitudes
   │                                                         │
   │                                                         ▼ Invoca SOAP
   │                                                 [ SERVIDOR PAYARA ]
   │                                                 WSConversorUnidades?wsdl
   │                                                         │
   │ <───────────────────────────────────────────────────────┘
   ▼
[ VISTA XML DECLARATIVA (.xhtml) ]
- login.xhtml (Vista en XML estricto con Facelets y avatar de Sullivan)
- conversor.xhtml (Conversor tipo ventana de escritorio con <f:ajax> dinámico)
- images/sullivan.png (Portada de James P. Sullivan de Monsters Inc)
```

---

## 📂 Estructura de Paquetes y Clases

- **Controladores / Managed Beans (`ec.edu.monster.wsconversorweb.controller`):**
  - `LoginBean.java`: Controlador CDI (`@Named @SessionScoped`) para la autenticación y control de sesión.
  - `ConversorBean.java`: Controlador CDI para la gestión de magnitudes, invocación de conversiones y actualización de componentes con `<f:ajax>`.
  - `AppControlador.java`: Fachada controladora de la lógica de aplicación.
  - `LoginServlet.java`, `ConversorServlet.java`, `AuthFilter.java`: Soporte para endpoints Servlet tradicionales.

- **Vistas XML (`src/main/webapp/`):**
  - `login.xhtml`: Formulario XML con componentes `<h:form>`, `<h:inputText>`, `<h:inputSecret>` y `<h:commandButton>`.
  - `conversor.xhtml`: Vista XML con selector dinámico de magnitud, conversión con AJAX nativo de Faces y tabla de historial.
  - `WEB-INF/beans.xml`: Descriptor CDI 4.0 (`bean-discovery-mode="annotated"`).
  - `WEB-INF/faces-config.xml`: Descriptor de configuración de Jakarta Faces 4.0.
  - `WEB-INF/web.xml`: Mapeo de `FacesServlet` sobre `*.xhtml` y página de bienvenida `login.xhtml`.

- **Servicio & SOAP (`ec.edu.monster.wsconversorweb.service` / `ec.edu.monster.ws`):**
  - `ConversorService.java`: Servicio cliente JAX-WS que consume las operaciones SOAP.
  - `WSConversorUnidades.java` y `WSConversorUnidades_Service.java`: Clases SEI y Service JAX-WS.

- **Modelo (`ec.edu.monster.wsconversorweb.model`):**
  - `UsuarioSession.java`: Modelo de datos de sesión.
  - `HistorialItem.java`: Modelo de cada conversión efectuada.
  - `UnidadLongitud.java`, `UnidadMasa.java`, `UnidadTemperatura.java`: Enumeraciones de unidades.

---

## 🚀 Despliegue y Ejecución en Payara Server

1. **Compilar el archivo `.war`:**
   ```bash
   mvn clean package
   ```
2. **Desplegar en Payara:**
   - Copiar `target/WSConversorWebClient-1.0-SNAPSHOT.war` a la carpeta `glassfish/domains/domain1/autodeploy/`.
   - O desplegar desde NetBeans haciendo clic derecho en el proyecto -> **Run**.
3. **Acceso:**
   - La aplicación iniciará en:
     ```text
     http://localhost:8080/WSConversorWebClient/login.xhtml
     ```
