# Taller TI.1.3: Servicio Web RESTful y 4 Clientes Multiplataforma (Sin Base de Datos)
**Universidad de las Fuerzas Armadas ESPE**  
**Carrera:** Ingeniería en Tecnologías de la Información  
**Materia:** Arquitectura de Software  
**Periodo:** PAO 202651 | **NRC:** 36884 | **Grupo:** 04  

---

## 📌 Descripción General
Implementación integral de una solución orientada a servicios basada en el estilo arquitectónico **RESTful** (Representational State Transfer) utilizando **Java 17 Puro y Jakarta EE 10**.

El sistema se compone de:
1. **04. SERVIDOR (`WS_ConversorUnidades_Java_REST`):** API RESTful desarrollada con **Jakarta REST 3.1 (JAX-RS)** y desplegada en **Payara Server 6**.
2. **04. CLICON (`WSConversorConsoleClient`):** Cliente de consola interactivo en Java con banner ASCII de James P. Sullivan.
3. **04. CLIESC (`WSConversorClient`):** Cliente de escritorio nativo con interfaz gráfica **Java Swing (JFrame)** e imagen oficial de Sullivan.
4. **04. CLIWEB (`WSConversorWebClient`):** Cliente Web en **Jakarta Faces (JSF 4.0 / Facelets XML)**, 0% JavaScript, 0% CSS externo, 100% Java.
5. **04. CLIMOV (`WSConversorMobileClient`):** Aplicación móvil nativa para Android en Java Puro con soporte de IP dinámica y APK compilada.

---

## 🏛️ Patrón Arquitectónico Estricto: MVC Tradicional
Todos los clientes implementan rigurosamente el patrón **Modelo-Vista-Controlador (MVC)** estructurado en **exactamente 3 paquetes** (sin paquetes `service` intermedios):

```
ec.edu.monster.<cliente>
├── model          # Modelos de datos, Sesión, Enums y ConversorService (comunicación HTTP REST)
├── view           # Vistas (ConsolaView / JFrames Swing / JSF Facelets / Activities Android)
└── controller     # AppControlador (Coordina y desacopla la vista del modelo)
```

---

## 🔐 Credenciales Oficiales de Acceso
* **Usuario:** `MONSTER`
* **Contraseña:** `MONSTER9`

---

## 🚀 Guía Rápida de Compilación de Todos los Módulos

### 1. Servidor Web RESTful (Payara 6)
```bash
cd "04. SERVIDOR/WS_ConversorUnidades_Java_REST"
mvn clean package
```
* Genera: `target/WS_ConversorUnidades_Java_REST.war`
* URL de la API: `http://localhost:8080/WS_ConversorUnidades_Java_REST/api`

### 2. Cliente de Consola
```bash
cd "04. CLICON/WSConversorConsoleClient"
mvn clean package
java -jar target/WSConversorConsoleClient-1.0-SNAPSHOT.jar
```

### 3. Cliente de Escritorio (Swing)
```bash
cd "04. CLIESC/WSConversorClient"
mvn clean package
java -jar target/WSConversorClient-1.0-SNAPSHOT.jar
```

### 4. Cliente Web (Jakarta Faces JSF 4.0)
```bash
cd "04. CLIWEB/WSConversorWebClient"
mvn clean package
```
* Genera: `target/WSConversorWebClient.war`
* Desplegar en Payara y acceder a: `http://localhost:8080/WSConversorWebClient/login.xhtml`

### 5. Cliente Móvil (Android)
```bash
cd "04. CLIMOV/WSConversorMobileClient"
./gradlew assembleDebug
```
* Genera: `app/build/outputs/apk/debug/app-debug.apk`
