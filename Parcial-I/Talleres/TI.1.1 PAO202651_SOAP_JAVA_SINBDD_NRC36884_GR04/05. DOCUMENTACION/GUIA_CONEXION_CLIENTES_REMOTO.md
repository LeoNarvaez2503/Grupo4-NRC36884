# Guía de Conexión de Clientes Remotos (Payara Server SOAP)

**NRC:** 36884 | **Grupo:** 4 | **PAO:** 202651  
**Materia:** Arquitectura de Software  

Esta guía explica detalladamente cómo conectar **cualquier cliente (Web, Móvil, Escritorio o Consola)** que se encuentre en otra computadora o dispositivo celular hacia tu servidor **Payara Server** donde está desplegado el servicio SOAP `WS_ConversorUnidades_Java_SOAP`.

---

## 📋 Resumen Rápido de Conexión

| Tipo de Cliente | Dispositivo del Amigo | Dónde se Configura | URL que debe usar |
| :--- | :--- | :--- | :--- |
| **🌐 Cliente Web** | PC, Laptop o Celular | Navegador web (Chrome, Firefox, etc.) | `http://<TU_IP>:8080/WSConversorWebClient/` |
| **📱 Cliente Móvil** | Celular Android / Emulador | Botón `⚙ Configurar IP Servidor` en la app | `http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades` |
| **🖥️ Cliente Escritorio** | PC con Java / NetBeans | `ConversorService.java` / WSDL | `http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl` |
| **💻 Cliente Consola** | PC con Java / Maven | `WSConversorUnidades_Service.java` / WSDL | `http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl` |

> [!IMPORTANT]
> **Credenciales Oficiales de Autenticación:**  
> - **Usuario:** `MONSTER`  
> - **Contraseña:** `MONSTER9`

---

## 1. Configuración Previa en tu Servidor (Host)

Para que tu máquina permita conexiones de otros dispositivos, realiza estos dos pasos:

### 1.1. Obtener tu Dirección IP
Abre una terminal en tu equipo y ejecuta:
```bash
ip -4 addr show
```
Identifica la IP según tu entorno:
- **Misma red Wi-Fi / Red local:** Busca la interfaz inalámbrica (`wlan0`, `wlp...`) o cableada (`eth0`, `enp...`).  
  *Ejemplo típico:* `192.168.18.249` o `192.168.1.X`.
- **Diferentes ubicaciones / Internet (Hamachi / Radmin VPN):** Busca la interfaz de Hamachi (`ham0`).  
  *Ejemplo:* `25.29.232.145`. Ambos deben estar dentro de la misma sala de Hamachi.

### 1.2. Habilitar el Puerto 8080 en el Firewall de Linux
Por seguridad, Linux suele bloquear peticiones entrantes. Habilita el puerto de Payara ejecutando:
```bash
sudo ufw allow 8080/tcp
```
*(Si usas `firewalld`: `sudo firewall-cmd --add-port=8080/tcp --permanent && sudo firewall-cmd --reload`)*.

### 1.3. Prueba de Verificación de Red
Antes de abrir los clientes, pídele a tu amigo que desde su navegador abra:
```
http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl
```
*(Reemplazando `<TU_IP>` por tu dirección real, ej. `http://192.168.18.249:8080/...`)*.

Si en la pantalla aparece el archivo XML con la definición del WSDL, **la conectividad de red es exitosa**.

---

## 2. Pasos Detallados según el Cliente

---

### 🌐 Caso A: Tu amigo quiere ser CLIENTE WEB
**Ventaja:** Tu amigo **NO necesita instalar Java, ni NetBeans, ni compilar nada**.

1. Asegúrate de tener desplegado el proyecto `WSConversorWebClient` en tu Payara Server.
2. Tu amigo abre su navegador favorito (en PC, Mac o smartphone).
3. Ingresa la URL:
   ```
   http://<TU_IP>:8080/WSConversorWebClient/
   ```
4. Aparecerá la pantalla de **INICIAR SESIÓN**.
5. Ingresa con `MONSTER` / `MONSTER9` y presiona **Ingresar**.
6. Ya puede convertir unidades en tiempo real consumiendo tu servidor.

---

### 📱 Caso B: Tu amigo quiere ser CLIENTE MÓVIL (Android)
**Ventaja:** La aplicación incluye un selector dinámico de IP en la pantalla de inicio.

1. Tu amigo instala la app en su teléfono o la abre en **Android Studio** desde la carpeta `04. CLIMOV/WSConversorMobileClient`.
2. En la pantalla inicial de login, toca el texto inferior azul:  
   👉 **`⚙ Configurar IP Servidor`**.
3. En la ventana emergente, cambia la URL:
   - **Si usa un teléfono físico en la misma red Wi-Fi:**
     ```
     http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades
     ```
   - **Si usa el Emulador de Android Studio en tu misma PC:**
     ```
     http://10.0.2.2:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades
     ```
4. Presiona **Guardar**.
5. Ingresa `MONSTER` y `MONSTER9` y presiona **Ingresar**.

---

### 🖥️ Caso C: Tu amigo quiere ser CLIENTE DE ESCRITORIO (Swing)
Si tu amigo tiene el proyecto `WSConversorClient` abierto en su propia computadora:

1. En el árbol de proyectos de NetBeans, navega a:  
   `WSConversorClient` -> `Source Packages` -> `ec.edu.monster.wsconversorclient.service` -> **`ConversorService.java`**
   *(o en la clase generada `ec.edu.monster.ws.WSConversorUnidades_Service.java`)*.
2. Localiza la línea con la URL del WSDL:
   ```java
   // Cambiar de:
   "http://localhost:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl"
   
   // A (con tu IP):
   "http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl"
   ```
3. Guarda el archivo y ejecuta el proyecto (**Clean and Build** y luego **Run**).
4. La ventana de escritorio se abrirá y consumirá tu Payara Server remoto.

---

### 💻 Caso D: Tu amigo quiere ser CLIENTE DE CONSOLA
Si tu amigo quiere ejecutar el cliente de consola desde su terminal:

1. En la carpeta `04. CLICON/WSConversorConsoleClient`:
2. Abre el archivo:  
   `src/main/java/ec/edu/monster/ws/WSConversorUnidades_Service.java`
3. Cambia la constante de la URL:
   ```java
   // Reemplazar localhost por tu IP:
   url = new URL("http://<TU_IP>:8080/WS_ConversorUnidades_Java_SOAP/WSConversorUnidades?wsdl");
   ```
4. En la terminal de su equipo ejecuta:
   ```bash
   mvn clean package
   mvn exec:java
   ```
5. Podrá iniciar sesión e interactuar desde la consola.

---

## 3. Diagnóstico y Solución de Problemas (Troubleshooting)

### Error 1: "Connection Refused" / "No se puede conectar al servidor"
- **Causa:** El puerto 8080 está bloqueado por el firewall o Payara está apagado.
- **Solución:** Ejecuta en tu máquina `sudo ufw allow 8080/tcp` y verifica que Payara esté iniciado en NetBeans o la consola.

### Error 2: "Host is unreachable" / "Tiempo de espera agotado"
- **Causa:** Los dos dispositivos no están en la misma subred o la IP cambió por DHCP.
- **Solución:** 
  1. Verifica que ambos estén conectados a la misma red Wi-Fi (no red de invitados).
  2. Vuelve a ejecutar `ip -4 addr show` en tu máquina para verificar si el router te asignó una nueva IP.
  3. Si están en casas diferentes, asegúrense de que ambos tengan el cliente Hamachi encendido y con luz verde.

### Error 3: "Usuario o contraseña incorrectos"
- **Causa:** Ingreso de credenciales distintas a las únicas permitidas en el servidor.
- **Solución:** Utilizar estrictamente:
  - **Usuario:** `MONSTER` *(en mayúsculas)*
  - **Contraseña:** `MONSTER9`
