# Ítem 2: Especificación de Caso de Uso Detallado (ECUD) (Puntaje: 1.0 / 1.0)

## ECUD_VERIFICAR_SUJETO_CREDITO

| Campo | Especificación |
| :--- | :--- |
| **Nombre del Caso de Uso:** | Verificar si el cliente es sujeto de crédito |
| **Identificador:** | `ECUD-BANQUITO-01` |
| **Módulo:** | Módulo de Crédito - BanQuito Core |
| **Autores:** | Grupo 04 - NRC 36884 |
| **Tutor:** | Ing. Eduardo Mauricio Campaña Ortega |
| **Descripción:** | Permite verificar automáticamente si una persona natural que desea adquirir un teléfono celular mediante la alianza comercial califica como sujeto de crédito bancario, evaluando su pertenencia al banco, su comportamiento transaccional en el último mes, su edad/género y la ausencia de créditos activos previos. |
| **Meta / Propósito:** | Determinar con precisión matemática y estricto cumplimiento normativo la aptitud crediticia del solicitante para habilitar la línea de crédito directo. |
| **Actores:** | **Comercializadora de Teléfonos Celulares** (Sistema Web Consumidor RESTful), **Cliente / Solicitante** (Iniciador indirecto). |
| **Tipo:** | Primario / Transaccional Automatizado. |
| **Precondiciones:** | 1. El servicio web RESTful de BanQuito (`/api/credito/es-sujeto/{cedula}`) debe encontrarse activo y en ejecución.<br>2. La base de datos relacional `banquito_db` debe estar accesible en el puerto 3306.<br>3. La Comercializadora envía una cédula de identidad válida no vacía. |
| **Postcondiciones:** | El sistema retorna un objeto JSON estructurado con la decisión de aprobación (`esSujeto: true`) o rechazo (`esSujeto: false`), detallando el cumplimiento o incumplimiento de cada una de las 4 políticas crediticias evaluadas. |

---

### Flujo Principal de Eventos (Camino Básico / Feliz):

| Paso | Acción del Actor (Comercializadora / Cliente) | Respuesta del Sistema (Core BanQuito) |
| :---: | :--- | :--- |
| **1** | El cliente proporciona su número de cédula en la aplicación web de la Comercializadora para solicitar financiamiento. | |
| **2** | La Comercializadora emite una petición HTTP `GET` al endpoint RESTful `/api/credito/es-sujeto/{cedula}`. | |
| **3** | | El sistema recibe la cédula y consulta la tabla `CLIENTE` de la base de datos `banquito_db` (Regla RN-01). |
| **4** | | El sistema comprueba que el cliente existe y recupera su código, nombre, género y fecha de nacimiento. |
| **5** | | El sistema evalúa la fecha de nacimiento para calcular la edad exacta en años cumplidos (Regla RN-03). |
| **6** | | Si el género es masculino (`GENERO = 'M'`), verifica que la edad sea mayor o igual a 25 años. Si es femenino, no aplica restricción de edad mínima. |
| **7** | | El sistema consulta la tabla `MOVIMIENTO` vinculada a las cuentas del cliente para verificar que registre al menos un depósito (`TIPO = 'DEP'`) dentro de los últimos 30 días calendario (Regla RN-02). |
| **8** | | El sistema consulta la tabla `CREDITO` para certificar que el cliente no mantenga ningún crédito en estado `'ACTIVO'` (Regla RN-04). |
| **9** | | Al cumplirse satisfactoriamente todas las condiciones, el sistema genera la respuesta JSON con `esSujeto = true` y el motivo `"Aprobado: El cliente cumple con todas las políticas crediticias de BanQuito."`. |
| **10** | | El sistema responde con código HTTP `200 OK` y el payload JSON. |
| **11** | La Comercializadora recibe la calificación positiva y procede a consultar el monto máximo de crédito aprobado. | |

---

### Flujos Alternativos y Excepciones:

- **FA-01: El solicitante no está registrado como cliente del banco (RN-01)**
  - En el **Paso 3**, si la cédula no existe en la tabla `CLIENTE`:
  - El sistema establece `esSujeto = false`, `cumpleCliente = false` y motivo `"Rechazado: La persona solicitante no se encuentra registrada como cliente de BanQuito."`.
  - El sistema retorna HTTP `200 OK` con la decisión negativa.

- **FA-02: Solicitante masculino menor de 25 años (RN-03)**
  - En el **Paso 6**, si `GENERO = 'M'` y la edad calculada es menor a 25 años (ej. Mateo Salazar, 22 años):
  - El sistema establece `esSujeto = false`, `cumpleEdadGenero = false` y motivo `"Rechazado: Restricción de edad: El solicitante masculino tiene XX años (debe ser mayor o igual a 25 años)."`.
  - El sistema continúa y retorna la respuesta con las razones específicas del rechazo.

- **FA-03: Cliente sin transacciones de depósito en el último mes (RN-02)**
  - En el **Paso 7**, si el recuento de movimientos con `TIPO = 'DEP'` y `FECHA >= CURDATE() - INTERVAL 30 DAY` es igual a cero (ej. Elena Castro):
  - El sistema establece `esSujeto = false`, `cumpleDepositoUltimoMes = false` y motivo `"Rechazado: Falta de depósitos: El cliente no registra ningún depósito en los últimos 30 días."`.

- **FA-04: Cliente con crédito activo preexistente en el banco (RN-04)**
  - En el **Paso 8**, si existe al menos un registro en la tabla `CREDITO` con `ESTADO = 'ACTIVO'` para dicho cliente (ej. Roberto Benítez):
  - El sistema establece `esSujeto = false`, `cumpleSinCreditoActivo = false` y motivo `"Rechazado: Crédito vigente: El cliente ya mantiene un crédito activo en el banco."`.

- **FA-05: Petición con parámetro de cédula vacío o nulo**
  - Si el parámetro cédula está ausente, el servidor retorna código HTTP `400 Bad Request` con mensaje de error descriptivo.

---

### Reglas de Negocio Asociadas:

| Código | Nombre de la Regla | Descripción y Lógica Aplicada |
| :---: | :--- | :--- |
| **RN-01** | Pertenencia al Banco | La persona debe existir previamente en la entidad `CLIENTE` del Core Bancario. |
| **RN-02** | Actividad Reciente | Debe registrar $\ge 1$ movimiento con `TIPO = 'DEP'` en los últimos 30 días calendario respecto a la fecha actual del sistema. |
| **RN-03** | Política de Edad por Género | Si el solicitante tiene `GENERO = 'M'` (Masculino), su edad calculada debe ser $\ge 25$ años. Las solicitantes de género `'F'` (Femenino) no presentan restricción de edad mínima. |
| **RN-04** | Exclusividad Crediticia | No puede poseer créditos simultáneos; la entidad `CREDITO` debe tener cero registros con `ESTADO = 'ACTIVO'` para su código de cliente. |
