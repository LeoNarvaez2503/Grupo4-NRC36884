# Guía de Defensa ante el Tribunal de Evaluación (Calificación 20 / 20)

**Asignatura:** Programación de Servicios Web / Sistemas Distribuidos  
**NRC:** 36884 | **Grupo:** 04  
**Universidad de las Fuerzas Armadas ESPE** - DECC  

---

## 1. Resumen Ejecutivo de la Solución Desarrollada

El proyecto implementa la alianza comercial estratégica entre la **Comercializadora de Teléfonos Celulares** y el **Banco BanQuito (Módulo de Crédito)** mediante arquitectura distribuida desacoplada en **JAVA** y **Servicios Web RESTful**.

- **Servidor BanQuito Core (RESTful API)**: Desplegado en el puerto **`8080`** (`http://localhost:8080/api/credito`).
- **Cliente Comercializadora (Web App)**: Desplegado en el puerto **`8090`** (`http://localhost:8090/`).
- **Base de Datos**: MySQL 8.0 en contenedor Podman `mysql-db` (puerto **`3306`**).

---

## 2. Demostración Paso a Paso de los 12 Ítems de la Rúbrica Oficial

### Ítem 1: Diagrama de Casos de Uso del Módulo de Crédito BanQuito (/ 2.0 pts)
- **Ubicación:** Carpeta `01. UML/DiagramaCasosUso_BanQuito.md` y `.puml`.
- **Explicación al Tribunal:**
  - Actores identificados: *Cliente/Solicitante*, *Comercializadora de Celulares* (Sistema Externo REST) y *Oficial de Crédito*.
  - Casos de uso expuestos: `CU01 Verificar Sujeto de Crédito` (con inclusión de las 4 políticas), `CU02 Calcular Monto Máximo`, `CU03 Otorgar Crédito y Tabla Amortización`, `CU04 Consultar Tabla Amortización` y `CU05 Simular Cuota`.

---

### Ítem 2: Descripción del Caso de Uso que Verifica Sujeto de Crédito (/ 1.0 pt)
- **Ubicación:** Carpeta `01. UML/ECUD_VerificarSujetoCredito.md`.
- **Explicación al Tribunal:**
  - Especificación formal bajo estándar IEEE/RUP con precondiciones, flujo principal, 5 flujos alternativos/excepciones y el detalle de las reglas de negocio **RN-01 a RN-04**.

---

### Ítem 3: Implementación de Base de Datos del Sistema CORE (/ 2.0 pts)
- **Ubicación:** Carpeta `03. BDD/01_banquito_core.sql` y `02_comercializadora.sql`.
- **Evidencia en MySQL:**
  - `banquito_db.CLIENTE`: **5 clientes** diseñados para testear cada regla.
  - `banquito_db.CUENTA`: **5 cuentas** asociadas.
  - `banquito_db.MOVIMIENTO`: **55 movimientos** históricos en los últimos 90 días.
  - Entidades de crédito: `CREDITO` y `TABLA_AMORTIZACION`.

---

### Ítem 4: Diagrama Entidad - Relación de la Comercializadora (/ 1.0 pt)
- **Ubicación:** Carpeta `02. MER/MER_Comercializadora.md` y `.puml`.
- **Explicación al Tribunal:**
  - Entidades `TELEFONO_CELULAR` (catálogo sin inventario, según especificación) y `FACTURA` (con campos de descuentos, cuotas y llave foránea hacia BanQuito).

---

### Ítem 5: Servicio Web que Valida si es Sujeto de Crédito por Cédula (/ 1.5 pts)
- **Endpoint:** `GET http://localhost:8080/api/credito/es-sujeto/{cedula}`
- **Demostración en Vivo:**
  ```powershell
  # Cliente 1 (Aprobado):
  curl http://localhost:8080/api/credito/es-sujeto/1710000001
  # Cliente 3 (Rechazado por edad masculina < 25 años):
  curl http://localhost:8080/api/credito/es-sujeto/1710000003
  ```

---

### Ítem 6: Servicio Web de Monto Máximo de Crédito (/ 1.5 pts)
- **Endpoint:** `GET http://localhost:8080/api/credito/monto-maximo/{codCliente}`
- **Fórmula Oficial Aplicada:** `((Promedio Depósitos - Promedio Retiros) * 30%) * 6`
- **Demostración en Vivo:**
  ```powershell
  curl http://localhost:8080/api/credito/monto-maximo/1
  ```
  *Retorna depósitos promedio ($781.43) - retiros ($200.00) = diferencia $581.43 -> Cupo Máximo: **$1,046.57**.*

---

### Ítem 7: Servicio Web que Otorga Crédito y Crea Tabla de Amortización (/ 2.0 pts)
- **Endpoint:** `POST http://localhost:8080/api/credito/otorgar`
- **Parámetros:** `codCliente`, `precioTelefono`, `numeroCuotas`.
- **Reglas:** Tasa anual **16.5%** ($i = 0.01375$ mensual), plazo de 3 a 18 meses. Coincide exactamente con el caso oficial de prueba: Préstamo $\$450.00$ a 6 meses $\rightarrow$ Cuota fija mensual de **$\$78.65$**.

---

### Ítem 8: Mantenimiento del Catálogo de Teléfonos Celulares (CRUD) (/ 1.0 pt)
- **Ubicación:** Pestaña **"Catálogo Teléfonos"** en `http://localhost:8090/`.
- **Funcionalidad:** Permite crear nuevos modelos, editar precios/características y retirar de venta equipos con actualización reactiva instantánea.

---

### Ítem 9: Funcionalidad de Facturación para los Cuatro Clientes (/ 3.0 pts)
En la pantalla de Facturación (`http://localhost:8090/`), presione el botón de cada cliente en la botonera de acceso rápido:
1. **Cliente 1 (Carlos Andrade - C.I. 1710000001):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - Teléfono: Motorola Edge 40 ($450.00) a 6 meses.
   - **Resultado:** Crédito aprobado en tiempo real, cuota fija de $78.65 y factura emitida con tabla de amortización completa.
2. **Cliente 2 (Andrea Morales - C.I. 1710000002):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - Teléfono: Samsung Galaxy S24 ($850.00).
   - **Resultado:** Alerta roja de rechazo: *"El valor del celular ($850.00) excede el monto máximo aprobado ($178.20). Capacidad insuficiente"*.
3. **Cliente 3 (Mateo Salazar - C.I. 1710000003):**
   - Forma de pago: **Crédito Directo BanQuito**.
   - **Resultado:** Alerta roja de rechazo: *"Restricción de edad: El solicitante masculino tiene 22 años (debe ser mayor o igual a 25 años)"*.
4. **Cliente 4 (Elena Castro - C.I. 1710000004):**
   - Forma de pago: **Efectivo**.
   - Teléfono: Motorola Edge 40 ($450.00).
   - **Resultado:** Descuento automático oficial del **42%** (-$189.00). Total a pagar: **$261.00**. Factura emitida de inmediato.

---

### Ítem 10: Servicio Web que Retorna Tabla de Amortización Otorgada (/ 1.0 pt)
- **Endpoint:** `GET http://localhost:8080/api/credito/tabla-amortizacion/{codCredito}`
- **Demostración en Vivo:**
  ```powershell
  curl http://localhost:8080/api/credito/tabla-amortizacion/1
  ```
  *Devuelve todas las cuotas generadas con `# Cuota`, `Valor Cuota`, `Interés Pagado`, `Capital Pagado` y `Saldo`.*

---

### Ítem 11: Consulta de Tabla de Amortización desde la Comercializadora (/ 1.0 pt)
- **Ubicación:** Pestaña **"Consulta Amortización"** en `http://localhost:8090/`.
- Permite ingresar el número de crédito o la cédula del cliente para renderizar la tabla tabular oficial.

---

### Ítem 12: Diagrama de Arquitectura de la Solución Completa (/ 1.0 pt)
- **Ubicación:** Carpeta `05. DOCUMENTACION/Diagrama_Arquitectura.md` y pestaña **"Arquitectura"** en la aplicación web.
- Describe el flujo Browser $\rightarrow$ Comercializadora Java (:8090) $\rightarrow$ RESTful API BanQuito (:8080) $\rightarrow$ MySQL 8.0 (:3306).
