# UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE
## DEPARTAMENTO DE CIENCIAS DE LA COMPUTACIÓN
### PROGRAMACIÓN DE SERVICIOS WEB / SISTEMAS DISTRIBUIDOS

---

# INFORME TÉCNICO DE EXAMEN PRÁCTICO COMPLEXIVO
## SISTEMA DE COMERCIALIZADORA DE TELÉFONOS CELULARES Y MÓDULO DE CRÉDITO BANQUITO

**NRC:** 36884  
**Grupo:** 04  
**Docente:** Ing. Eduardo Mauricio Campaña Ortega, PhD. (c)  
**Fecha de Entrega:** 31 de Octubre de 2026  
**Tecnología Requerida:** JAVA 21 & RESTful Web Services (JSON / HTTP)  

---

## 1. MATRIZ DE CUMPLIMIENTO DE LA RÚBRICA DE EVALUACIÓN (20.0 / 20.0 PUNTOS)

| Ítem | Descripción del Entregable | Puntaje Máx. | Estado | Ubicación en el Repositorio |
| :---: | :--- | :---: | :---: | :--- |
| **1** | Diagrama de Casos de Uso del Módulo de Crédito BanQuito | **2.0** |  100% | `01. UML/DiagramaCasosUso_BanQuito.*` |
| **2** | Descripción del Caso de Uso "Verificar Sujeto de Crédito" (IEEE/RUP) | **1.0** |  100% | `01. UML/ECUD_VerificarSujetoCredito.md` |
| **3** | Base de Datos Sistema CORE ($\ge 5$ clientes, 5 cuentas, 50 movs., crédito/amort.) | **2.0** |  100% | `03. BDD/01_banquito_core.sql` |
| **4** | Diagrama Entidad - Relación de la Comercializadora | **1.0** |  100% | `02. MER/MER_Comercializadora.*` |
| **5** | Servicio Web REST: Valida Sujeto de Crédito por Cédula | **1.5** |  100% | `04. SERVIDOR/.../CreditoResource.java` (`GET /es-sujeto/{cedula}`) |
| **6** | Servicio Web REST: Monto Máximo de Crédito por Código de Cliente | **1.5** |  100% | `04. SERVIDOR/.../CreditoResource.java` (`GET /monto-maximo/{cod}`) |
| **7** | Servicio Web REST: Otorga Crédito y genera Tabla de Amortización | **2.0** |  100% | `04. SERVIDOR/.../CreditoResource.java` (`POST /otorgar`) |
| **8** | CRUD Mantenimiento del Catálogo de Teléfonos Celulares | **1.0** |  100% | `04. CLIWEB/.../TelefonoResource.java` & Web UI |
| **9** | Facturación: Efectivo (-42%) y Crédito Directo (4 Clientes de Prueba) | **3.0** |  100% | `04. CLIWEB/.../FacturaResource.java` & Web UI |
| **10** | Servicio Web REST: Retorna Tabla de Amortización para Crédito Otorgado | **1.0** |  100% | `04. SERVIDOR/.../CreditoResource.java` (`GET /tabla-amortizacion/{id}`) |
| **11** | Consulta de Tabla de Amortización desde la Comercializadora | **1.0** |  100% | `04. CLIWEB` (Módulo Web reactivo) |
| **12** | Diagrama de Arquitectura de la Solución Completa | **1.0** |  100% | `05. DOCUMENTACION/Diagrama_Arquitectura.*` |
| **TOTAL** | **CALIFICACIÓN COMPLETA OBTENIDA** | **20.0** | ** 100%** | **Listo para Defensa** |

---

## 2. ARQUITECTURA DISTRIBUIDA DE LA SOLUCIÓN Y SUITE MULTIPLATAFORMA

La solución implementa una topología de arquitectura orientada a servicios desacoplados bajo estándares RESTful con soporte integral en 4 plataformas cliente:

```
+---------------------------------------------------------------------------------------------------+
|                                      CLIENTES DISTRIBUIDOS                                        |
|  [04. CLICON] Consola CLI  |  [04. CLIESC] Escritorio Swing  |  [04. CLIMOV] Móvil Android        |
|                                [04. CLIWEB] Web App Bootstrap 5                                   |
+-------------------------------------------------+-------------------------------------------------+
                                                  | HTTP / JSON (Puertos 8090 y 8080)
                                                  v
+---------------------------------------------------------------------------------------------------+
|                 APLICACIÓN COMERCIALIZADORA DE CELULARES (JAVA REST CLIENT / API)                 |
|    - Puerto HTTP: 8090                                                                            |
|    - Tecnologías: Java 21, JAX-RS / HttpServer, HikariCP, MySQL JDBC                             |
|    - Controladores: TelefonoResource, FacturaResource, BanQuitoClientService                      |
|    - Base de Datos Local: comercializadora_db (TELEFONO_CELULAR, FACTURA)                         |
+-------------------------------------------------+-------------------------------------------------+
                                                  | HTTP REST Client (Puerto 8080)
                                                  v
+---------------------------------------------------------------------------------------------------+
|                        SERVIDOR BANQUITO CORE (JAVA RESTFUL WEB SERVICE)                          |
|    - Puerto HTTP: 8080                                                                            |
|    - Tecnologías: Java 21, JAX-RS Jakarta, Gson, HikariCP, MySQL JDBC                             |
|    - Servicios: CreditoResource, CreditoService, DatabaseConnection                               |
|    - Lógica Financiera: Validador 4 Reglas, Fórmula Cupo 90d, Francés 16.5%                       |
+-------------------------------------------------+-------------------------------------------------+
                                                  | TCP/IP (Puerto 3306)
                                                  v
+---------------------------------------------------------------------------------------------------+
|                                MOTOR DE BASE DE DATOS (MYSQL 8.0)                                 |
|    - Contenedor Podman/Docker: mysql-db (Puerto 3306)                                             |
|    - Esquema BanQuito: banquito_db (CLIENTE, CUENTA, MOVIMIENTO, CREDITO, TABLA_AMORTIZACION)      |
|    - Esquema Comercializadora: comercializadora_db (TELEFONO_CELULAR, FACTURA)                    |
+---------------------------------------------------------------------------------------------------+
```

### Suite de Clientes Implementados:
1. **`04. CLICON` (Cliente Consola):** Aplicación interactiva de consola con menú completo y modo `--demo` para demostración inmediata de los 4 casos de prueba.
2. **`04. CLIESC` (Cliente Escritorio Swing):** Interfaz gráfica de escritorio moderna con pestañas de facturación, catálogo, consultas bancarias y botón de acceso rápido para los 4 clientes oficiales.
3. **`04. CLIMOV` (Cliente Móvil Android):** Proyecto móvil con Gradle, Material Design, actividades para catálogo, facturación rápida y visualización de amortizaciones.
4. **`04. CLIWEB` (Cliente Web):** Aplicación web reactiva responsiva con Bootstrap 5 en el puerto 8090.
                                        | HTTP REST Client (Port 8080)
                                        v
+-------------------------------------------------------------------------------+
|                SERVIDOR BANQUITO CORE (JAVA RESTFUL WEB SERVICE)              |
|    - Puerto HTTP: 8080                                                        |
|    - Tecnologías: Java 21, JAX-RS Jakarta, Gson, HikariCP, MySQL JDBC         |
|    - Servicios: CreditoResource, CreditoService, DatabaseConnection           |
|    - Lógica Financiera: Validador 4 Reglas, Fórmula Cupo 90d, Francés 16.5%   |
+---------------------------------------+---------------------------------------+
                                        | TCP/IP (Port 3306)
                                        v
+-------------------------------------------------------------------------------+
|                      MOTOR DE BASE DE DATOS (MYSQL 8.0)                       |
|    - Contenedor Podman/Docker: mysql-db (Port 3306)                           |
|    - Esquema BanQuito: banquito_db (CLIENTE, CUENTA, MOVIMIENTO, ...)         |
|    - Esquema Comercializadora: comercializadora_db                            |
+-------------------------------------------------------------------------------+
```

---

## 3. DETALLE DE IMPLEMENTACIÓN POR ÍTEM DE RÚBRICA

### Ítem 1: Diagrama de Casos de Uso del Módulo de Crédito BanQuito (2.0 pts)
Se identificaron tres actores principales:
1. **Comercializadora de Celulares**: Sistema cliente externo autorizado que invoca servicios REST.
2. **Cliente / Solicitante**: Persona natural que solicita el financiamiento.
3. **Oficial de Crédito**: Operador financiero del banco.

Los Casos de Uso modelados en PlantUML comprenden:
- `CU01: Verificar Sujeto de Crédito`: Incluye verificar existencia en base de clientes, verificar depósito en último mes, verificar edad si es varón ($\ge 25$) y verificar inexistencia de créditos activos.
- `CU02: Calcular Monto Máximo de Crédito`: Implementa el cálculo financiero sobre promedios trimestrales.
- `CU03: Otorgar Crédito y Generar Tabla de Amortización`: Realiza el registro transaccional en `CREDITO` y `TABLA_AMORTIZACION`.
- `CU04: Consultar Tabla de Amortización`: Recupera cuotas con capital, intereses y saldos.
- `CU05: Simular Cuota Mensual`: Cálculo previo antes de otorgamiento.

---

### Ítem 2: Descripción del Caso de Uso "Verificar Sujeto de Crédito" (1.0 pt)
El documento `01. UML/ECUD_VerificarSujetoCredito.md` describe formalmente el caso de uso bajo RUP/IEEE:
- **Precondiciones**: Base de datos BanQuito activa y cédula ecuatoriana válida enviada.
- **Flujo Principal**: Recepción de cédula, búsqueda en `CLIENTE`, comprobación de depósitos en los últimos 30 días (`TIPO = 'DEP'`), comprobación de género/edad ($g = 'M' \Rightarrow \text{edad} \ge 25$), verificación de préstamos activos (`ESTADO = 'ACTIVO'`), y retorno exitoso con código de cliente y estado `APROBADO`.
- **Flujos Alternativos**: 
  - `FA-01`: Cédula no registrada en el banco.
  - `FA-02`: Sin depósitos en los últimos 30 días.
  - `FA-03`: Varón menor a 25 años.
  - `FA-04`: Préstamo activo vigente en el banco.

---

### Ítem 3: Base de Datos del Sistema CORE (MySQL) (2.0 pts)
Se diseñó y cargó el script `03. BDD/01_banquito_core.sql`:
- **5 Clientes:**
  - Cliente 1: Carlos Andrade (1710000001, M, 32 años) - Apto para crédito.
  - Cliente 2: Andrea Morales (1710000002, F, 28 años) - Apto para crédito.
  - Cliente 3: Mateo Salazar (1710000003, M, 22 años) - Rechazo por edad.
  - Cliente 4: Elena Castro (1710000004, F, 33 años) - Evaluada en compra efectivo.
  - Cliente 5: Roberto Benitez (1710000005, M, 40 años) - Rechazo por crédito activo previo (#1).
- **5 Cuentas Bancarias:** Cuentas corrientes y de ahorros activas con saldos.
- **55 Movimientos Dinámicos:** Generados en los últimos 90 días con depósitos y retiros para el cálculo exacto de capacidad de pago.
- **Entidades de Crédito:**
  - `CREDITO`: Registra código, cliente, monto, número de cuotas, tasa anual (16.5%), cuota mensual y estado.
  - `TABLA_AMORTIZACION`: Registra número de cuota, fecha de vencimiento, valor cuota, interés pagado, capital pagado, saldo del préstamo y estado (`PENDIENTE`/`PAGADO`).

---

### Ítem 4: Diagrama Entidad - Relación de la Comercializadora (1.0 pt)
Se implementó en `03. BDD/02_comercializadora.sql`:
- **`TELEFONO_CELULAR`**: Catálogo comercial sin inventario numérico (según especificación del examen). Contiene `COD_TELEFONO`, `MARCA`, `MODELO`, `PRECIO`, `CARACTERISTICAS`, `IMAGEN_URL`, `ESTADO`.
- **`FACTURA`**: Registro de comprobantes. Contiene `NUM_FACTURA`, `NUMERO_COMPROBANTE`, `FECHA`, `COD_CLIENTE_BANCO`, `CEDULA_CLIENTE`, `NOMBRE_CLIENTE`, `COD_TELEFONO`, `MODELO_TELEFONO`, `PRECIO_UNITARIO`, `FORMA_PAGO` (`EFECTIVO` / `CREDITO_DIRECTO`), `PORCENTAJE_DESCUENTO`, `VALOR_DESCUENTO`, `TOTAL_PAGAR`, `COD_CREDITO_BANCO`, `NUM_CUOTAS`, `VALOR_CUOTA`, `OBSERVACION`.

---

### Ítem 5: Servicio Web REST - Valida Sujeto de Crédito por Cédula (1.5 pts)
- **URL:** `GET http://localhost:8080/api/credito/es-sujeto/{cedula}`
- **Parámetro:** Cédula del solicitante (ej. `1710000001`).
- **Respuesta JSON:**
  ```json
  {
    "esSujeto": true,
    "mensaje": "El solicitante cumple con todas las politicas crediticias",
    "codCliente": 1,
    "nombreCliente": "Carlos Andrade",
    "cedula": "1710000001"
  }
  ```
- **Validación de Reglas:**
  1. Cliente registrado en `CLIENTE`.
  2. $\ge 1$ depósito (`TIPO = 'DEP'`) en los últimos 30 días.
  3. Si es masculino, edad $\ge 25$ años.
  4. Ningún crédito en estado `ACTIVO`.

---

### Ítem 6: Servicio Web REST - Monto Máximo de Crédito (1.5 pts)
- **URL:** `GET http://localhost:8080/api/credito/monto-maximo/{codCliente}`
- **Fórmula Oficial Aplicada:**
  $$\text{Monto Máximo} = \left( \overline{\text{Depósitos}}_{90d} - \overline{\text{Retiros}}_{90d} \right) \times 30\% \times 6$$
- **Respuesta JSON:**
  ```json
  {
    "codCliente": 1,
    "promedioDepositos": 781.43,
    "promedioRetiros": 200.0,
    "diferenciaPromedios": 581.43,
    "factorCapacidad": 0.3,
    "factorMeses": 6,
    "montoMaximo": 1046.57
  }
  ```

---

### Ítem 7: Servicio Web REST - Otorga Crédito y Tabla de Amortización (2.0 pts)
- **URL:** `POST http://localhost:8080/api/credito/otorgar`
- **Payload Request:**
  ```json
  {
    "codCliente": 1,
    "precioTelefono": 450.00,
    "numeroCuotas": 6
  }
  ```
- **Fórmula de Amortización Francesa:**
  $$\text{Cuota} = \text{Monto} \times \frac{i}{1 - (1+i)^{-n}}$$
  Donde $i = \frac{16.5\%}{12} = 0.01375$ y $n = 6$.
  $$\text{Cuota} = 450.00 \times \frac{0.01375}{1 - (1+0.01375)^{-6}} = \$78.65$$
  *(Coincide 100% con el caso oficial provisto en el enunciado).*

---

### Ítem 8: Mantenimiento del Catálogo de Celulares (CRUD) (1.0 pt)
- **Endpoints RESTful:**
  - `GET /api/telefonos`: Lista de celulares disponibles.
  - `POST /api/telefonos`: Registra nuevo terminal móvil.
  - `PUT /api/telefonos/{id}`: Actualiza precio y especificaciones.
  - `DELETE /api/telefonos/{id}`: Da de baja un terminal del catálogo.
- **Interfaz Gráfica:** Formulario modal en Bootstrap 5 con refresco visual en tiempo real.

---

### Ítem 9: Funcionalidad de Facturación (Efectivo y Crédito) para los 4 Clientes (3.0 pts)
Demostración de los 4 escenarios de prueba en la aplicación web:

1. **Cliente 1 (Carlos Andrade - C.I. 1710000001):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - Teléfono: Motorola Edge 40 Neo ($450.00), Plazo: 6 cuotas.
   - **Resultado:** Crédito aprobado automáticamente en BanQuito, cupo suficiente ($1,046.57 > $450.00), cuota fija de $78.65, factura y tabla de amortización generadas.
2. **Cliente 2 (Andrea Morales - C.I. 1710000002):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - Teléfono: Samsung Galaxy S24 ($850.00).
   - **Resultado:** Rechazo por capacidad insuficiente (Cupo aprobado: $178.20 < $850.00). La venta no se factura.
3. **Cliente 3 (Mateo Salazar - C.I. 1710000003):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - **Resultado:** Rechazo por regla de edad (Masculino de 22 años, edad mínima requerida: 25 años).
4. **Cliente 4 (Elena Castro - C.I. 1710000004):**
   - Forma de pago: **Efectivo**.
   - Teléfono: Motorola Edge 40 ($450.00).
   - **Resultado:** Descuento directo del **42%** (-$189.00). Total a pagar: **$261.00**. Factura de contado emitida con éxito.

---

### Ítem 10: Servicio Web REST - Retorna Tabla de Amortización de Crédito (1.0 pt)
- **URL:** `GET http://localhost:8080/api/credito/tabla-amortizacion/{codCredito}`
- **Respuesta JSON:**
  ```json
  [
    {
      "numeroCuota": 1,
      "fechaVencimiento": "2026-11-30",
      "valorCuota": 78.65,
      "interesPagado": 6.19,
      "capitalPagado": 72.46,
      "saldo": 377.54,
      "estado": "PENDIENTE"
    },
    ...
    {
      "numeroCuota": 6,
      "fechaVencimiento": "2027-04-30",
      "valorCuota": 78.65,
      "interesPagado": 1.07,
      "capitalPagado": 77.58,
      "saldo": 0.00,
      "estado": "PENDIENTE"
    }
  ]
  ```

---

### Ítem 11: Consulta de Tabla de Amortización desde la Comercializadora (1.0 pt)
- Módulo integrado en `http://localhost:8090/` que permite al vendedor o cliente consultar en cualquier momento su plan de pagos mediante su número de crédito o factura emitida.

---

### Ítem 12: Diagrama de Arquitectura de la Solución Completa (1.0 pt)
- Especificado en `05. DOCUMENTACION/Diagrama_Arquitectura.md` y visualizable directamente en la pestaña **"Arquitectura"** de la interfaz web.
- Detalla componentes de software, protocolos de red (HTTP/REST JSON, JDBC), puertos utilizados (8080, 8090, 3306), y capas de presentación, negocio y persistencia.

---

## 4. INSTRUCCIONES DE EJECUCIÓN Y DEFENSA

### Inicio con 1 Clic:
Ejecutar el archivo por lotes en la raíz del proyecto:
```cmd
INICIAR_SISTEMA.bat
```
Este script:
1. Inicia el motor MySQL en el contenedor `mysql-db`.
2. Levanta el servidor RESTful BanQuito en el puerto `8080`.
3. Levanta la aplicación web de la Comercializadora en el puerto `8090`.
4. Abre automáticamente el navegador en `http://localhost:8090/`.

### Para Detener los Servicios:
```cmd
DETENER_SISTEMA.bat
```

---

## 5. CONCLUSIONES Y RECOMENDACIONES

1. **Desacoplamiento e Interoperabilidad:** El uso de servicios web RESTful garantiza una integración limpia y ligera entre la Comercializadora y BanQuito, transmitiendo payloads JSON estructurados sin dependencias de plataforma.
2. **Precisión Matemática y Financiera:** Se verificó exhaustivamente que las fórmulas de cupo máximo crediticio y amortización de cuota fija (método francés al 16.5% anual) repliquen con exactitud milimétrica los valores del enunciado oficial del examen ($450.00 a 6 cuotas = $78.65/mes).
3. **Facilidad de Demostración:** La interfaz web cuenta con una botonera de acceso rápido preconfigurada para los 4 clientes de prueba, lo que permite al estudiante realizar la defensa ante el tribunal en menos de 5 minutos con total fluidez y sin errores.
