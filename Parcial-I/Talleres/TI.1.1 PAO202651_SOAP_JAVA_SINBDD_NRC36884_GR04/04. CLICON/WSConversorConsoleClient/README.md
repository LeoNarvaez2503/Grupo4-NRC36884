# Cliente de Consola SOAP - Conversor de Unidades (Java Puro | MVC)

**NRC:** 36884 | **Grupo:** 4 | **PAO:** 202651

Aplicación de consola interactiva desarrollada en **Java Puro** implementando el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**, consumiendo el servicio web SOAP (`WSConversorUnidades`).

---

## 🏛️ Patrón Arquitectónico MVC en Consola

```
                     Entrada por Teclado
  Usuario (Terminal) ───────────────────>  [ CONTROLADOR ]
          │                                  - ConsolaControlador.java (Flujo y Menús)
          │                                  - AppControlador.java (Fachada de aplicación)
          │                                           │
          │                                           ▼
          │                                  [ MODELO & SERVICIO ]
          │                                  - ConversorService.java (JAX-WS)
          │                                  - Enums: UnidadLongitud, UnidadMasa, etc.
          │                                           │
          │                                           ▼ Invocación SOAP
          │                                  [ SERVIDOR SOAP (Payara) ]
          │                                  http://localhost:8080/WS_Conversor...
          │                                           │
          │ <─────────────────────────────────────────┘ Retorna Datos
          │
          ▼
  [ VISTA (Terminal) ]
  - ConsolaView.java (Entrada/Salida formateada con Scanner y System.out)
```

---

## 📂 Estructura de Paquetes y Clases

- **Controlador (`ec.edu.monster.wsconversorcon.controller`):**
  - `ConsolaControlador.java`: Gestiona el flujo del ciclo de vida, menús interactivos, login y despacho de opciones.
  - `AppControlador.java`: Fachada controladora de la lógica de aplicación (mismo contrato que los clientes de escritorio y web).

- **Vista (`ec.edu.monster.wsconversorcon.view`):**
  - `ConsolaView.java`: Maneja la lectura segura de teclado, validación numérica, presentación de menús y resultados.

- **Modelo (`ec.edu.monster.wsconversorcon.model`):**
  - `UnidadLongitud.java`, `UnidadMasa.java`, `UnidadTemperatura.java`: Enums de magnitudes.

- **Servicio y Cliente SOAP (`ec.edu.monster.wsconversorcon.service` y `ec.edu.monster.ws`):**
  - `ConversorService.java`: Servicio cliente JAX-WS que consume las operaciones SOAP.
  - `WSConversorUnidades.java` y `WSConversorUnidades_Service.java`: Clases de contrato e invocación JAX-WS.

- **Punto de Entrada (`ec.edu.monster.wsconversorcon`):**
  - `WSConversorConsoleClient.java`: Contiene el método `main(String[] args)`.

---

## 🔑 Credenciales Oficiales
- **Usuario:** `MONSTER`
- **Contraseña:** `MONSTER9`

---

## ⚙️ Compilación y Ejecución

Desde la carpeta del proyecto:
```bash
cd "Parcial-I/Talleres/TI.1.1 PAO202651_SOAP_JAVA_SINBDD_NRC36884_GR04/04. CLICON/WSConversorConsoleClient"
```

### Opción 1: Ejecutar directamente con Maven
```bash
mvn exec:java
```

### Opción 2: Compilar JAR ejecutable y correr con Java
```bash
mvn clean package
java -jar target/WSConversorConsoleClient-1.0-SNAPSHOT.jar
```
