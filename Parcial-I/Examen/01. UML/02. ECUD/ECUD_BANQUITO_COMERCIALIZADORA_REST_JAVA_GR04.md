# ESPECIFICACIÓN DE CASOS DE USO DETALLADA (ECUD)
## SISTEMA INTEGRADO BANCO BANQUITO (MÓDULO DE CRÉDITO) Y COMERCIALIZADORA DE TELÉFONOS CELULARES CON RESTFUL EN JAVA

---

### DATOS INFORMATIVOS
- **Carrera:** Ingeniería en Tecnologías de la Información / Software
- **Asignatura:** Computación Distribuida / Arquitectura SOA
- **NRC:** 36884 | **Grupo:** 04
- **Autores:** 
  - Flores Maya Alejandro Caetano
  - Morales Carvajal Anthony Alain
  - Narváez Criollo Leonardo Vinicio
- **Tutor:** Ing. Eduardo Mauricio Campaña Ortega, MIS. MDU. CCNA. CCIA. PhD. (c)
- **Fecha:** Noviembre 2026

---

## ÍNDICE DE CASOS DE USO DETALLADOS

1. [ECUD-01: Gestión Integral de Crédito y Facturación Comercializadora (General)](#ecud-01-gestión-integral-de-crédito-y-facturación-comercializadora-general)
2. [ECUD-02: Verificar si el Solicitante es Sujeto de Crédito (4 Políticas BanQuito)](#ecud-02-verificar-si-el-solicitante-es-sujeto-de-crédito-4-políticas-banquito)
3. [ECUD-03: Calcular Monto Máximo de Crédito Aprobado (Capacidad 90 Días)](#ecud-03-calcular-monto-máximo-de-crédito-aprobado-capacidad-90-días)
4. [ECUD-04: Otorgar Crédito Directo y Generar Amortización Francesa](#ecud-04-otorgar-crédito-directo-y-generar-amortización-francesa)
5. [ECUD-05: Consultar Tabla de Amortización de Crédito Otorgado](#ecud-05-consultar-tabla-de-amortización-de-crédito-otorgado)
6. [ECUD-06: Facturación de Celulares en Comercializadora (Efectivo / Crédito Directo)](#ecud-06-facturación-de-celulares-en-comercializadora-efectivo--crédito-directo)
7. [ECUD-07: Mantenimiento del Catálogo de Teléfonos Celulares](#ecud-07-mantenimiento-del-catálogo-de-teléfonos-celulares)

---

## ECUD-01: Gestión Integral de Crédito y Facturación Comercializadora (General)

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Gestión Integral de Crédito y Facturación Comercializadora (General) |
| **Identificador:** | `ECUD-01` |
| **Módulo:** | Ecosistema Distribuido BanQuito Core & Comercializadora |
| **Descripción:** | Describe la interacción completa del ecosistema comercial y financiero: la Comercializadora consulta el catálogo de celulares, realiza la facturación en Efectivo aplicando 42% de descuento o a Crédito Directo invocando los servicios RESTful del Banco BanQuito (verificación de sujeto, cálculo de cupo máximo, otorgamiento y generación de la tabla de amortización francesa). |
| **Meta:** | Proveer un marco unificado de interoperabilidad transaccional entre la Comercializadora de Celulares y el Core Bancario BanQuito mediante servicios web RESTful en Java. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Autores:** | Flores Caetano, Morales Anthony, Narváez Leonardo |
| **Fecha Creación:** | 04/11/2026 | **Fecha Modificación:** 04/11/2026 |
| **Precondiciones:** | 1. El servidor RESTful de BanQuito (`/api/credito`) se encuentra activo en el puerto 8080.<br>2. La base de datos `banquito_db` y `comercializadora_db` están operativas en MariaDB (puerto 3306 Docker/Podman).<br>3. Los catálogos de clientes y celulares están inicializados. |
| **Postcondiciones:** | Se emite la factura comercial y, de ser venta a crédito, queda registrado el crédito bancario activo con sus cuotas de amortización en BanQuito. |
| **Gráfico Asociado:** | `CU_General_BanQuito_Comercializadora.puml` |

### Flujo Básico (Camino Feliz):

| Paso | Acción del Actor (Vendedor / Cliente / Comercializadora) | Respuesta del Sistema (BanQuito Core / Comercializadora Backend) |
| :---: | :--- | :--- |
| **1** | El cliente acude a la Comercializadora y selecciona un teléfono celular del catálogo para adquirirlo. | El sistema Comercializadora recupera los datos del teléfono (marca, modelo, precio de lista). |
| **2** | El vendedor solicita al cliente la cédula de identidad y la forma de pago (Efectivo o Crédito Directo). | El sistema presenta las opciones de compra. |
| **3** | Si es en **Efectivo**, el sistema calcula el 42% de descuento sobre el precio de lista y genera la factura directa (ECUD-06). | Se registra la venta sin invocar servicios externos. |
| **4** | Si es a **Crédito Directo**, la Comercializadora invoca el servicio web RESTful de BanQuito `GET /api/credito/es-sujeto/{cedula}` (ECUD-02). | BanQuito evalúa las 4 políticas crediticias y retorna `esSujeto = true`. |
| **5** | La Comercializadora invoca `GET /api/credito/monto-maximo/{codCliente}` (ECUD-03). | BanQuito calcula el cupo crediticio basado en los depósitos y retiros de los últimos 90 días y retorna el monto máximo. |
| **6** | La Comercializadora valida que el precio de lista del teléfono sea menor o igual al cupo máximo aprobado. | El sistema confirma la viabilidad económica del financiamiento. |
| **7** | El cliente define el plazo del crédito (entre 3 y 18 meses). | El sistema captura el número de cuotas. |
| **8** | La Comercializadora emite la petición `POST /api/credito/otorgar` a BanQuito (ECUD-04). | BanQuito registra el crédito en estado `ACTIVO`, calcula las cuotas fijas francesas (16.5% anual) y persiste la tabla de amortización. |
| **9** | La Comercializadora recibe el código de crédito (`codCredito`) y registra la factura con forma de pago `CREDITO_DIRECTO`. | El sistema emite la factura oficial y entrega la tabla de amortización al cliente. |

### Flujos Alternativos y Excepciones:
- **FA-01: El cliente no es sujeto de crédito (ECUD-02):** BanQuito retorna `esSujeto = false` indicando la política violada. La Comercializadora cancela la opción de crédito y ofrece pago en efectivo con 42% de descuento.
- **FA-02: El precio del celular supera el cupo máximo (ECUD-03):** Se emite alerta de cupo insuficiente. El cliente puede seleccionar un teléfono más económico o pagar en efectivo.
- **FA-03: Caída del servicio BanQuito RESTful:** Si la API bancaria no responde o hay timeout HTTP, se captura la excepción y se informa al vendedor que el sistema bancario está temporalmente inaccesible.

---

## ECUD-02: Verificar si el Solicitante es Sujeto de Crédito (4 Políticas BanQuito)

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Verificar si el solicitante es sujeto de crédito |
| **Identificador:** | `ECUD-02` |
| **Módulo:** | Módulo de Crédito - BanQuito Core RESTful |
| **Descripción:** | Evalúa exhaustiva y secuencialmente las cuatro políticas crediticias obligatorias de BanQuito a partir de la cédula de identidad del solicitante. |
| **Meta:** | Determinar con precisión matemática y normativa si el cliente califica para una línea de crédito directo. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Autores:** | Flores Caetano, Morales Anthony, Narváez Leonardo |
| **Fecha Creación:** | 04/11/2026 | **Fecha Modificación:** 04/11/2026 |
| **Precondiciones:** | Cédula válida enviada en la URI `GET /api/credito/es-sujeto/{cedula}`. Base de datos `banquito_db` accesible. |
| **Postcondiciones:** | Retorna JSON con `esSujeto: boolean`, indicadores booleanos de cada regla y mensaje descriptivo. |
| **Gráfico Asociado:** | `CU_01_Verificar_Sujeto_Credito.puml` / `DA_01_Verificar_Sujeto_Credito.puml` / `DS_01_Verificar_Sujeto_Credito_Aprobado.puml` |

### Flujo Básico:
1. BanQuito recibe la petición HTTP `GET /api/credito/es-sujeto/{cedula}`.
2. **Regla RN-01 (Pertenencia):** Consulta la tabla `CLIENTE` por cédula. Verifica que el cliente exista y recupera su código, fecha de nacimiento y género.
3. **Regla RN-03 (Edad y Género):** Calcula la edad en años. Si `GENERO = 'M'`, verifica que `edad >= 25`. Si `GENERO = 'F'`, se aprueba sin restricción de edad mínima.
4. **Regla RN-02 (Actividad Reciente):** Consulta la tabla `MOVIMIENTO` vinculada a sus cuentas y verifica que registre al menos un depósito (`TIPO = 'DEP'`) dentro de los últimos 30 días calendario.
5. **Regla RN-04 (Exclusividad Crediticia):** Consulta la tabla `CREDITO` y comprueba que el cliente no mantenga créditos con `ESTADO = 'ACTIVO'`.
6. Si las 4 reglas son verdaderas, establece `esSujeto = true` y mensaje `"Aprobado: El cliente cumple con todas las políticas crediticias de BanQuito."`.
7. Retorna código HTTP `200 OK` con el payload JSON estructurado.

### Flujos Alternativos:
- **FA-01 (No es cliente):** RN-01 falla -> `esSujeto = false`, motivo `"Rechazado: El solicitante no se encuentra registrado como cliente de BanQuito"`.
- **FA-02 (Edad menor a 25 en varón):** RN-03 falla -> `esSujeto = false`, motivo `"Rechazado: Solicitante masculino tiene XX años (debe ser >= 25 años)"`.
- **FA-03 (Sin depósito en 30 días):** RN-02 falla -> `esSujeto = false`, motivo `"Rechazado: El cliente no registra depósitos en los últimos 30 días"`.
- **FA-04 (Crédito activo existente):** RN-04 falla -> `esSujeto = false`, motivo `"Rechazado: El cliente ya mantiene un crédito activo vigente en el banco"`.

---

## ECUD-03: Calcular Monto Máximo de Crédito Aprobado (Capacidad 90 Días)

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Calcular Monto Máximo de Crédito Aprobado |
| **Identificador:** | `ECUD-03` |
| **Módulo:** | Módulo de Crédito - BanQuito Core RESTful |
| **Descripción:** | Calcula la capacidad de endeudamiento del cliente analizando su flujo de caja histórico de depósitos y retiros de los últimos 3 meses (90 días). |
| **Meta:** | Cuantificar el límite máximo de dinero que BanQuito puede financiar responsablemente al cliente. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Fórmula de Negocio:** | $$\text{MontoMáximo} = ((\text{PromedioDepósitos}_{90d} - \text{PromedioRetiros}_{90d}) \times 30\%) \times 6$$ |
| **Precondiciones:** | `codCliente` válido recibido en `GET /api/credito/monto-maximo/{codCliente}`. |
| **Postcondiciones:** | Retorna JSON con promedios de depósitos, promedios de retiros, diferencia neta y monto máximo autorizado. |
| **Gráfico Asociado:** | `CU_02_Calcular_Monto_Maximo.puml` / `DS_03_Calcular_Monto_Maximo_Credito.puml` |

### Flujo Básico:
1. BanQuito recibe `GET /api/credito/monto-maximo/{codCliente}`.
2. Consulta la tabla `MOVIMIENTO` filtrando por las cuentas del cliente, tipo `'DEP'` y fecha dentro de los últimos 90 días (`FECHA >= CURDATE() - INTERVAL 90 DAY`). Calcula el promedio aritmético.
3. Consulta la tabla `MOVIMIENTO` filtrando por tipo `'RET'` en los últimos 90 días y calcula su promedio aritmético.
4. Calcula la diferencia neta: `Diferencia = PromedioDepósitos - PromedioRetiros`.
5. Si la diferencia es positiva, calcula: `MontoMáximo = ((Diferencia) * 0.30) * 6`, redondeado a 2 decimales.
6. Si la diferencia es menor o igual a cero, `MontoMáximo = 0.00`.
7. Retorna HTTP `200 OK` con el JSON de capacidad de pago.

---

## ECUD-04: Otorgar Crédito Directo y Generar Amortización Francesa

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Otorgar Crédito Directo y Generar Tabla de Amortización Francesa |
| **Identificador:** | `ECUD-04` |
| **Módulo:** | Módulo de Crédito - BanQuito Core RESTful |
| **Descripción:** | Formaliza la aprobación del crédito en BanQuito, insertando el registro principal en `CREDITO` y desglosando el cronograma de pagos mes a mes en `AMORTIZACION` bajo el sistema de cuota fija francesa con una tasa de interés del 16.5% anual. |
| **Meta:** | Crear la obligación financiera y generar la tabla de cuotas que el cliente pagará. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Fórmula de Negocio:** | $$\text{Cuota} = \frac{\text{Monto}}{\frac{1 - (1 + i)^{-n}}{i}}, \quad \text{donde } i = \frac{16.5\%}{12} = 0.01375$$ |
| **Precondiciones:** | Petición `POST /api/credito/otorgar` con payload JSON: `codCliente`, `precioTelefono`, `numeroCuotas` (plazo $3 \le n \le 18$). |
| **Postcondiciones:** | Se genera un registro con `ESTADO = 'ACTIVO'` en `CREDITO` y $n$ registros detallados en `AMORTIZACION`. Retorna HTTP 201 Created. |
| **Gráfico Asociado:** | `CU_03_Otorgar_Credito_Amortizacion.puml` / `DA_03_Calculo_Capacidad_Pago_y_Amortizacion.puml` / `DS_04_Otorgar_Credito_y_Generar_Amortizacion.puml` |

### Flujo Básico:
1. El backend de BanQuito recibe `POST /api/credito/otorgar`.
2. Valida que el plazo esté en el rango permitido ($3 \le n \le 18$ meses).
3. Inicia una transacción ACID en base de datos (`Connection.setAutoCommit(false)`).
4. Inserta el registro en la tabla `CREDITO`: código autogenerado, monto aprobado, plazo en meses, tasa 16.5%, fecha de aprobación actual y estado `'ACTIVO'`.
5. Calcula la tasa mensual: $i = 0.165 / 12 = 0.01375$.
6. Calcula la cuota fija mensual aplicando la fórmula del sistema francés.
7. Itera desde $k = 1$ hasta $n$:
   - Interés pagado: $\text{Interes}_k = \text{Saldo}_{k-1} \times i$.
   - Capital pagado: $\text{Capital}_k = \text{Cuota} - \text{Interes}_k$ (en la última cuota se ajusta al saldo remanente).
   - Saldo final: $\text{Saldo}_k = \text{Saldo}_{k-1} - \text{Capital}_k$.
   - Inserta la cuota en `AMORTIZACION`.
8. Ejecuta `COMMIT` de la transacción.
9. Retorna código HTTP `201 Created` con el ID del crédito y el desglose de cuotas.

---

## ECUD-05: Consultar Tabla de Amortización de Crédito Otorgado

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Consultar Tabla de Amortización de Crédito Otorgado |
| **Identificador:** | `ECUD-05` |
| **Módulo:** | Módulo de Crédito - BanQuito Core RESTful |
| **Descripción:** | Permite recuperar en cualquier momento la tabla de amortización completa y el estado actual de un crédito formalizado en BanQuito a través de su identificador numérico. |
| **Meta:** | Proveer transparencia y auditoría tanto al cliente como al oficial bancario sobre el cronograma de amortización. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Precondiciones:** | `codCredito` existente en la base de datos `banquito_db`. Endpoint: `GET /api/credito/tabla-amortizacion/{codCredito}`. |
| **Postcondiciones:** | Retorna la cabecera del crédito y la lista cronológica de cuotas con fecha de vencimiento, cuota, interés, capital y saldo. |
| **Gráfico Asociado:** | `CU_04_Consultar_Amortizacion.puml` / `DS_07_Consultar_Tabla_Amortizacion.puml` |

---

## ECUD-06: Facturación de Celulares en Comercializadora (Efectivo / Crédito Directo)

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Facturación de Celulares en Comercializadora |
| **Identificador:** | `ECUD-06` |
| **Módulo:** | Sistema de Facturación Comercializadora (Web / Desktop / Móvil / Consola) |
| **Descripción:** | Permite al vendedor emitir la factura de venta de un equipo celular seleccionado del catálogo, aplicando 42% de descuento si es en Efectivo, o integrando la aprobación y código de crédito de BanQuito si es a Crédito Directo. |
| **Meta:** | Registrar contable y legalmente la venta de teléfonos en la base de datos `comercializadora_db`. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Precondiciones:** | El teléfono celular seleccionado existe en el catálogo. Cédula del cliente proporcionada. |
| **Postcondiciones:** | Se crea la factura en `FACTURA` y el detalle en `DETALLE_FACTURA`. |
| **Gráfico Asociado:** | `CU_05_Facturacion_Celulares.puml` / `DA_02_Facturacion_Comercializadora.puml` / `DS_05_Facturacion_Venta_Credito_Directo.puml` / `DS_06_Facturacion_Venta_Efectivo_42_Descuento.puml` |

### Flujo Básico - Modalidad Efectivo:
1. El vendedor selecciona el celular y la forma de pago `EFECTIVO`.
2. El sistema calcula: $\text{Descuento} = \text{PrecioLista} \times 0.42$.
3. Total a cobrar: $\text{Total} = \text{PrecioLista} - \text{Descuento} = \text{PrecioLista} \times 0.58$.
4. Inserta el registro en `FACTURA` con `FORMA_PAGO = 'EFECTIVO'` y `COD_CREDITO = NULL`.
5. Inserta el item en `DETALLE_FACTURA`.
6. Emite el comprobante para el cliente.

### Flujo Básico - Modalidad Crédito Directo:
1. El vendedor selecciona el celular y la forma de pago `CREDITO_DIRECTO`.
2. El sistema consume el servicio RESTful de BanQuito `/api/credito/es-sujeto/{cedula}`.
3. Si califica, consume `/api/credito/monto-maximo/{codCliente}`.
4. Verifica que `PrecioLista <= MontoMaximo`.
5. Solicita el número de cuotas (3 a 18 meses).
6. Consume `/api/credito/otorgar`. BanQuito aprueba y retorna `codCredito`.
7. Inserta la factura en `comercializadora_db` con `FORMA_PAGO = 'CREDITO_DIRECTO'` y el `COD_CREDITO` asignado.
8. Imprime factura y tabla de amortización para firma del cliente.

---

## ECUD-07: Mantenimiento del Catálogo de Teléfonos Celulares

| Campo | Especificación |
| :--- | :--- |
| **Nombre Caso de Uso:** | Mantenimiento del Catálogo de Teléfonos Celulares |
| **Identificador:** | `ECUD-07` |
| **Módulo:** | Comercializadora de Teléfonos Celulares |
| **Descripción:** | Permite gestionar (Crear, Consultar, Modificar y Deshabilitar) los modelos de teléfonos celulares disponibles para la venta. Cumple con la restricción del examen de no llevar inventario físico (stock numérico), manejando disponibilidad por modelo. |
| **Meta:** | Mantener actualizado el portafolio comercial de terminales móviles con sus precios de lista. |
| **Estado:** | Aprobado | **Versión:** 1.0 |
| **Precondiciones:** | Usuario con rol administrativo o comercial en la aplicación. |
| **Postcondiciones:** | Los cambios quedan reflejados en la tabla `TELEFONO` de `comercializadora_db`. |
| **Gráfico Asociado:** | `CU_06_Mantenimiento_Catalogo.puml` |

---
