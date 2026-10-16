# Cliente Móvil SOAP - Conversor de Unidades (Java Puro | Android MVC)

**NRC:** 36884 | **Grupo:** 4 | **PAO:** 202651

Aplicación Móvil Nativa para Android desarrollada en **Java Puro** implementando estrictamente el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**, consumiendo el servicio web SOAP (`WSConversorUnidades`).

---

## 🏛️ Patrón Arquitectónico MVC en Android

```
                   Interacción del Usuario (Touch / Clicks)
  Usuario (Móvil) ──────────────────────────────────────────> [ CONTROLADOR ]
         │                                                      ├── LoginActivity.java
         │                                                      ├── ConversorActivity.java
         │                                                      └── AppControlador.java (Fachada de negocio)
         │                                                                   │
         │                                                                   ▼
         │                                                      [ MODELO & SERVICIO ]
         │                                                      ├── ConversorService.java (SOAP HttpURLConnection)
         │                                                      ├── UsuarioSession.java
         │                                                      ├── ConfiguracionConexion.java
         │                                                      └── Enums: UnidadLongitud, UnidadMasa, etc.
         │                                                                   │
         │                                                                   ▼ Invocación SOAP (HTTP XML)
         │                                                      [ SERVIDOR SOAP (Payara) ]
         │                                                      http://10.0.2.2:8080/WS_Conversor...
         │                                                                   │
         │ <─────────────────────────────────────────────────────────────────┘ Retorna Resultado
         │
         ▼
  [ VISTA (Layouts XML) ]
  ├── activity_login.xml     (Interfaz de login sencilla: Usuario, Contraseña, Mostrar contraseña, Ingresar, Limpiar)
  └── activity_conversor.xml (Conversor sencillo: Magnitud, Valor, undInit "a" undFinal, Convertir, Resultado)
```

---

## 📂 Estructura del Proyecto

```
04. CLIMOV/WSConversorMobileClient/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew / gradlew.bat
├── README.md
├── .gitignore
└── app/
    ├── build.gradle
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── java/ec/edu/monster/wsconversormov/
        │   │   ├── model/
        │   │   │   ├── UnidadLongitud.java
        │   │   │   ├── UnidadMasa.java
        │   │   │   ├── UnidadTemperatura.java
        │   │   │   ├── UsuarioSession.java
        │   │   │   └── ConfiguracionConexion.java
        │   │   ├── service/
        │   │   │   └── ConversorService.java        # Cliente SOAP en Java Puro con HttpURLConnection
        │   │   └── controller/
        │   │       ├── AppControlador.java          # Mismo contrato que CLICON, CLIESC y CLIWEB
        │   │       ├── LoginActivity.java           # Controlador de pantalla de inicio de sesión
        │   │       └── ConversorActivity.java       # Controlador de pantalla del conversor
        │   └── res/
        │       ├── layout/
        │       │   ├── activity_login.xml           # Vista simple de Login (idéntica a la de escritorio)
        │       │   └── activity_conversor.xml       # Vista simple de Conversión (idéntica a la de escritorio)
        │       └── values/
        │           ├── strings.xml
        │           ├── colors.xml
        │           └── themes.xml
        └── test/java/ec/edu/monster/wsconversormov/
            └── AppControladorTest.java
```

---

## 📱 Pantallas de la Aplicación Móvil (Sencillas como la de Escritorio)

### 1. Pantalla de Inicio de Sesión (`activity_login.xml`)
- **Título:** `INICIAR SESIÓN`
- **Campos:** `Usuario:` y `Contraseña:`
- **Checkbox:** `[✓] Mostrar contraseña` (alterna visibilidad)
- **Botones:**
  - `[ Ingresar ]`: Conecta con el servidor SOAP y abre el conversor.
  - `[ Limpiar ]`: Borra ambos campos de texto.
- **Configuración de IP:** Enlace inferior para cambiar la IP del servidor si se prueba en un dispositivo físico.

### 2. Pantalla del Conversor (`activity_conversor.xml`)
- **Barra superior:** `Usuario: MONSTER` y botón `[ Cerrar Sesión ]`
- **Título dinámico:** Cambia según la magnitud (`Conversor de Longitudes`, `Conversor de Masa` o `Conversor de Temperatura`).
- **Fila 1:**
  - `Magnitud:` Spinner con `[ LONGITUD | MASA | TEMPERATURA ]`
  - Caja de texto para el número: `[ Valor ]`
- **Fila 2:**
  - Spinner origen `[ undInit ]` + texto `a` + Spinner destino `[ undFinal ]`
- **Botón:** `[ Convertir ]`
- **Fila 3:** `Resultado: [ 100.0000 ]`

---

## 🌐 Configuración de Red para el Servidor SOAP

- **En Emulador de Android (Android Studio):**
  El emulador mapea el `localhost` de la computadora en la dirección especial **`10.0.2.2`**.
  URL por defecto configurada:
  `http://10.0.2.2:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades`
- **En Dispositivo Físico:**
  Usa la IP de tu computadora en la red Wi-Fi (ej. `http://192.168.1.15:8080/...`), configurable directamente desde la pantalla de login con el botón *⚙ Configurar IP Servidor*.

---

## 🔑 Credenciales Oficiales
- **Usuario:** `MONSTER`
- **Contraseña:** `MONSTER9`

---

## 🚀 Cómo Abrir y Ejecutar el Proyecto

1. **Abrir en Android Studio:**
   - Seleccionar **File -> Open...**
   - Navegar hasta `04. CLIMOV/WSConversorMobileClient` y hacer clic en **OK**.
2. **Sincronizar Gradle:**
   - Android Studio detectará el proyecto automáticamente y descargará las dependencias de AndroidX.
3. **Ejecutar:**
   - Seleccionar un emulador o dispositivo físico y presionar **Run 'app'** (Shift + F10).
