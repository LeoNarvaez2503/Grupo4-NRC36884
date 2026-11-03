using System;
using System.Collections.Generic;
using System.Xml.Linq;
using static DocGen.DocHelpers;

namespace DocGen
{
    public static class ManualRestJavaContent
    {
        public static List<XElement> BuildElements()
        {
            var list = new List<XElement>();

            // =========================================================================
            // SECCIÓN 1: INTRODUCCIÓN
            // =========================================================================
            list.Add(Heading1("1 INTRODUCCIÓN"));
            list.Add(Paragraph("Un servicio web fundamentado en la Arquitectura RESTful (Representational State Transfer) sobre el protocolo HTTP/1.1 y HTTP/2 constituye el estándar contemporáneo por excelencia para la interoperabilidad, distribución y consumo eficiente de datos entre aplicaciones computacionales heterogéneas en redes corporativas e infraestructuras bancarias. A diferencia de las arquitecturas tradicionales orientadas a procedimientos y gobernadas por rigurosos sobres de mensajería SOAP (como JAX-WS clásico), los servicios RESTful eliminan la sobrecarga de transporte y procesamiento impuesta por el Lenguaje de Marcado Extensible (XML), esquemas XSD y direccionamientos tipados WS-*. En su lugar, el paradigma RESTful adopta la Notación de Objetos de JavaScript (JSON - RFC 8259) como formato de serialización universal, ligero y nativo, y aprovecha la semántica intrínseca de los métodos canónicos del protocolo HTTP (GET para consultas de solo lectura, POST para creación de recursos y procesamiento transaccional, PUT para actualizaciones y DELETE para eliminaciones). El contrato formal del servicio se define y publica mediante la especificación OpenAPI 3.0 / Swagger, describiendo sin ambigüedades las rutas de acceso (endpoints), parámetros de consulta y ruta, esquemas de carga útil (payloads) y los códigos de respuesta estandarizados (200 OK, 400 Bad Request, 401 Unauthorized, 404 Not Found, 409 Conflict)."));
            list.Add(Paragraph("En el sector financiero central (Core Banking), las transacciones ejecutadas sobre cuentas corrientes y de ahorro (tales como la recepción y acreditación de depósitos en efectivo, retiros por ventanilla y transferencias electrónicas entre cuentas de clientes) exigen el cumplimiento irrestricto de las propiedades ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad). Cuando múltiples ventanillas de atención física o canales de banca remota intentan acceder, debitar o consultar un mismo saldo de forma concurrente, emergen graves condiciones de carrera (race conditions), lecturas sucias (dirty reads) y posibles inconsistencias que comprometen la integridad del balance contable de la institución. Para mitigar estos riesgos operacionales, el presente proyecto implementa el modelo transaccional EurekaBank con exclusión mutua pesimista (mediante sentencias SQL SELECT ... FOR UPDATE a nivel de motor relacional InnoDB en MySQL 8.0) y un componente reactivo distribuido denominado Semáforo MashUp de Concurrencia en tiempo real."));
            list.Add(Paragraph("El despliegue del componente servidor central se efectúa sobre Payara Server Enterprise, un servidor de aplicaciones de grado industrial derivado de GlassFish compatible con el estándar Jakarta EE 10, el cual provee soporte nativo para la especificación Jakarta RESTful Web Services 3.1 (JAX-RS / Jersey). Por su parte, la persistencia de datos se gestiona de manera aislada y portable mediante un contenedor de base de datos relacional MySQL 8.0 (utilizando Podman o Docker Compose), lo cual garantiza independencia absoluta del sistema operativo anfitrión y reproducibilidad inmediata en cualquier estación de trabajo o servidor corporativo."));
            list.Add(Paragraph("Para la capa de presentación y consumo del servicio RESTful, se aplica con máxima rigurosidad el patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) a través de cuatro clientes totalmente heterogéneos: un Cliente de Consola interactivo para terminales y servidores headless con prueba de estrés concurrente, un Cliente de Escritorio de alto rendimiento desarrollado con Java Swing y despacho asíncrono con SwingWorker, un Cliente Móvil nativo para la plataforma Android (ec.edu.monster.climov) con comunicación HTTP/JSON y un Cliente Web reactivo desarrollado con Jakarta Faces (JSF 4.0) sobre Facelets y sondeo asíncrono AJAX periódico (polling cada 2.5 segundos). Todo el ecosistema opera bajo el dominio institucional unificado ec.edu.monster y respeta las credenciales maestras autorizadas (usuario: Monster, contraseña: Monster9), satisfaciendo los requerimientos de ingeniería de software establecidos para el Grupo 04 (GR04)."));

            // =========================================================================
            // SECCIÓN 2: OBJETIVOS
            // =========================================================================
            list.Add(Heading1("2 OBJETIVOS"));
            list.Add(Heading2("2.1 Objetivo General"));
            list.Add(Paragraph("Diseñar, implementar, desplegar y documentar un sistema bancario distribuido y transaccional denominado EurekaBank, fundamentado en servicios web RESTful (HTTP/JSON) desplegados en Payara Server Enterprise mediante Jakarta RESTful Web Services (Jersey) con persistencia relacional ACID en MySQL sobre contenedores locales (Docker/Podman), desacoplado mediante el patrón arquitectónico MVC Tradicional puro en todos sus niveles y consumido concurrentemente por cuatro clientes heterogéneos (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces) con soporte de monitoreo reactivo mediante Semáforo MashUp en tiempo real."));

            list.Add(Heading2("2.2 Objetivos Específicos"));
            list.Add(BulletParagraph("1.", "Configurar y desplegar el contenedor de base de datos relacional MySQL 8.0 mediante Podman/Docker Compose, estableciendo el esquema relacional íntegro de EurekaBank (sucursal, empleado, cliente, cuenta, control_mashup, movimiento), restricciones de clave foránea, registros de auditoría transaccional por ventanilla y credenciales maestras encriptadas mediante el algoritmo BCrypt ($2a$10$)."));
            list.Add(BulletParagraph("2.", "Construir la capa de persistencia JDBC transaccional del servidor RESTful implementando nivel de aislamiento TRANSACTION_READ_COMMITTED y bloqueos de fila pesimistas (SELECT ... FOR UPDATE) con el propósito de garantizar operaciones atómicas de depósito, retiro y transferencia libres de inconsistencias o sobregiros no autorizados."));
            list.Add(BulletParagraph("3.", "Diseñar e implementar el componente GestorConcurrencia en el servidor RESTful, coordinando los estados del Semáforo MashUp (VERDE para cuentas disponibles y ROJO para cuentas en operación transaccional activa en ventanilla) con persistencia instantánea en la tabla control_mashup y sincronización atómica en memoria mediante ConcurrentHashMap."));
            list.Add(BulletParagraph("4.", "Implementar y publicar el recurso Jakarta RESTful Web Services (EurekaBankResource) bajo la ruta base /api en Payara Server Enterprise, definiendo anotaciones declarativas JAX-RS (@Path, @Produces, @Consumes, @GET, @POST, @PathParam, @QueryParam), integrando filtro de seguridad CORS (CorsFilter) y documentando formalmente el contrato OpenAPI / Swagger."));
            list.Add(BulletParagraph("5.", "Desarrollar el Cliente de Consola (WSEurekaBankConsoleClient) bajo el patrón MVC Tradicional, consumiendo la API RESTful mediante java.net.http.HttpClient y com.google.code.gson.Gson, incorporando menús interactivos tabulados con códigos de color ANSI y una rutina multi-hilo de simulación de 3 ventanillas simultáneas para verificar la exclusión mutua."));
            list.Add(BulletParagraph("6.", "Construir el Cliente de Escritorio (WSEurekaBankDesktopClient) mediante formularios gráficos Java Swing, presentando una visualización en tiempo real del semáforo con renderizadores de color personalizados (SemaforoTableCellRenderer), extracción cronológica de movimientos, temporizador de refresco (Timer) y despacho asíncrono con SwingWorker para evitar bloqueos de interfaz."));
            list.Add(BulletParagraph("7.", "Implementar el Cliente Móvil (WSEurekaBankMobileClient) para la plataforma Android nativa en el paquete ec.edu.monster.climov, con layouts XML Material Design estructurados, serialización y parseo de peticiones HTTP/JSON vía HttpURLConnection y org.json, y despacho en subprocesos en segundo plano (ExecutorService) sincronizados con el hilo principal de interfaz (Handler y Looper)."));
            list.Add(BulletParagraph("8.", "Desarrollar el Cliente Web (WSEurekaBankWebClient) sobre Jakarta Faces (JSF 4.0), implementando Backing Beans gestionados (@Named, @SessionScoped) y plantillas Facelets reactivas mediante sondeo asíncrono AJAX periódico (polling cada 2.5 segundos) para reflejar visualmente la alternancia del semáforo entre VERDE y ROJO desde cualquier terminal del sistema."));

            // =========================================================================
            // SECCIÓN 3: MARCO TEÓRICO
            // =========================================================================
            list.Add(Heading1("3 MARCO TEÓRICO"));
            list.Add(Heading2("3.1 Servicios Web RESTful, Protocolo HTTP y Notación JSON"));
            list.Add(Paragraph("La arquitectura REST (Representational State Transfer), formalizada por Roy Fielding en el año 2000, define un conjunto de principios de diseño para construir sistemas hipermedia distribuidos, caracterizados por su escalabilidad, simplicidad y bajo acoplamiento. En una arquitectura de servicios web RESTful, los elementos fundamentales del dominio de negocio (tales como cuentas bancarias, clientes, sucursales y transacciones monetarias) se conciben y exponen como recursos abstractos identificados de forma unívoca a través de URIs (Uniform Resource Identifiers). La interacción entre clientes y servidores se efectúa mediante representaciones de dichos recursos, transfiriendo su estado a través de la red sin requerir protocolos de envoltura complejos ni máquinas de estado pesadas."));
            list.Add(Paragraph("Los principios arquitectónicos cardinales de RESTful comprenden:"));
            list.Add(BulletParagraph("• Separación Cliente-Servidor:", "Desacopla radicalmente las responsabilidades de almacenamiento y lógica financiera (radicadas en el servidor central) de las interfaces de usuario y captura de eventos (clientes de consola, escritorio, móvil y web), permitiendo la evolución independiente de cada plataforma."));
            list.Add(BulletParagraph("• Comunicación sin Estado (Statelessness):", "Cada solicitud HTTP transmitida por un cliente al servidor debe contener obligatoriamente toda la información requerida para su procesamiento, interpretación y autorización. El servidor no mantiene sesiones conversacionales en memoria vinculadas al protocolo de transporte, lo cual maximiza la escalabilidad horizontal y la tolerancia a fallos."));
            list.Add(BulletParagraph("• Capacidad de Almacenamiento en Caché (Cacheability):", "Las respuestas del servicio deben declararse implícita o explícitamente como almacenables en caché o no almacenables, permitiendo a los clientes reutilizar respuestas ante peticiones idénticas de solo lectura para optimizar el ancho de banda."));
            list.Add(BulletParagraph("• Interfaz Uniforme (Uniform Interface):", "Constituye el núcleo diferenciador de REST. Se basa en la identificación estandarizada de recursos mediante URIs, la manipulación de recursos a través de sus representaciones (JSON), el intercambio de mensajes autodescriptivos mediante cabeceras HTTP estándar (Content-Type: application/json) y el uso de hipermedios como motor del estado de la aplicación (HATEOAS)."));
            list.Add(BulletParagraph("• Sistema en Capas (Layered System):", "La arquitectura permite la interposición transparente de intermediarios (balanceadores de carga, servidores proxy, pasarelas de seguridad y firewalls) entre el cliente y el servidor de aplicaciones, sin que ninguna de las partes requiera modificar su código fuente."));
            list.Add(Paragraph("En contraste con los protocolos basados en SOAP que canalizan todas las operaciones indistintamente a través de peticiones HTTP POST con cargas útiles XML voluminosas, RESTful utiliza de forma estricta los métodos canónicos del protocolo HTTP según su semántica estándar:"));
            list.Add(BulletParagraph("• GET:", "Método seguro e idempotente utilizado de manera exclusiva para la consulta y recuperación de representaciones de recursos (por ejemplo: GET /api/cuentas, GET /api/sucursales, GET /api/mashup). No altera bajo ninguna circunstancia el estado de los datos en el servidor."));
            list.Add(BulletParagraph("• POST:", "Método no idempotente destinado a procesar entidades enviadas en el cuerpo de la petición. En el sistema bancario, se utiliza para ejecutar operaciones transaccionales críticas que transforman el estado contable (POST /api/movimientos/deposito, POST /api/movimientos/retiro, POST /api/movimientos/transferencia) y para validar credenciales en la autenticación de operadores (POST /api/auth/login)."));
            list.Add(BulletParagraph("• PUT / DELETE:", "Métodos diseñados para la actualización integral idempotente o la remoción de recursos en el servidor, complementando el ciclo de vida CRUD."));
            list.Add(Paragraph("El protocolo HTTP provee además un catálogo estandarizado de códigos de estado numéricos (HTTP Status Codes) que informan con precisión el desenlace de cada invocación:"));
            list.Add(BulletParagraph("• 200 OK:", "Indica que la operación o consulta se procesó satisfactoriamente y la respuesta incluye la representación JSON correspondiente en su cuerpo."));
            list.Add(BulletParagraph("• 400 Bad Request:", "Señaliza que la solicitud remitida por el cliente contiene errores sintácticos, campos obligatorios omitidos o parámetros inválidos (ej. cuerpo JSON vacío o monto no positivo)."));
            list.Add(BulletParagraph("• 401 Unauthorized:", "Indica que las credenciales provistas por el operador bancario (usuario o contraseña) son inválidas o inexistentes en la base de datos central."));
            list.Add(BulletParagraph("• 404 Not Found:", "Informa que el recurso solicitado no fue hallado en el sistema (por ejemplo, al intentar consultar una cuenta bancaria inexistente)."));
            list.Add(BulletParagraph("• 409 Conflict:", "Indica un conflicto con el estado actual del recurso de negocio. En la arquitectura EurekaBank, este código se retorna cuando una ventanilla intenta realizar una transacción sobre una cuenta que se encuentra bloqueada en estado ROJO por otra ventanilla activa (colisión de concurrencia MashUp)."));
            list.Add(BulletParagraph("• 500 Internal Server Error:", "Reporta un fallo no controlado en el servidor de aplicaciones o en el motor de base de datos relacional."));
            list.Add(Paragraph("Como formato de representación de datos, se emplea universalmente JSON (JavaScript Object Notation - RFC 8259). JSON es un formato de texto estructurado, compacto, legible y de bajo consumo de memoria basado en dos estructuras universales: colecciones de pares nombre/valor ({ \"campo\": \"valor\" }) y listas ordenadas de valores ([ elemento1, elemento2 ]). JSON elimina por completo la sobrecarga de procesamiento asociada al parseo de esquemas XML, etiquetas de cierre redundantes y namespaces complejos de SOAP, permitiendo que tanto navegadores modernos como dispositivos móviles Android y terminales de escritorio procesen las cargas útiles financieras con latencia mínima."));

            list.Add(Heading2("3.2 Principios de Diseño REST (Representational State Transfer) y OpenAPI / Swagger"));
            list.Add(Paragraph("El diseño de una API RESTful empresarial se fundamenta en la Arquitectura Orientada a Recursos (ROA - Resource-Oriented Architecture). En ROA, las URIs no representan verbos de acción o llamadas a funciones remotas (como ocurre en RPC o SOAP), sino nombres y jerarquías sustantivas de entidades de dominio. En el sistema EurekaBank, la estructura de direccionamiento RESTful se organiza limpiamente bajo el prefijo institucional /api:"));
            list.Add(BulletParagraph("• /api/sucursales:", "Colección de todas las sucursales bancarias habilitadas."));
            list.Add(BulletParagraph("• /api/cuentas:", "Colección de cuentas bancarias, filtrables opcionalmente por identificador de sucursal mediante parámetros de consulta (?sucursal=1)."));
            list.Add(BulletParagraph("• /api/cuentas/{numeroCuenta}:", "Recurso individual que identifica una cuenta específica mediante variable de ruta."));
            list.Add(BulletParagraph("• /api/cuentas/{numeroCuenta}/movimientos:", "Sub-recurso que expone el extracto cronológico de transacciones registradas sobre dicha cuenta."));
            list.Add(BulletParagraph("• /api/clientes/{dni}/cuentas:", "Sub-recurso que relaciona y retorna las cuentas bancarias pertenecientes al cliente portador de un número de cédula o DNI específico."));
            list.Add(BulletParagraph("• /api/movimientos/deposito y /api/movimientos/retiro:", "Puntos de procesamiento transaccional para acreditar o debitar fondos monetarios."));
            list.Add(BulletParagraph("• /api/mashup:", "Endpoint de consulta en tiempo real que expone el estado reactivo del semáforo (VERDE o ROJO) para cada cuenta."));
            list.Add(Paragraph("Para gobernar, documentar y validar formalmente la interfaz de programación sin reintroducir los acoplamientos rígidos de SOAP/WSDL, la industria tecnológica estandarizó la Especificación OpenAPI (originalmente conocida como Swagger). OpenAPI 3.0 es una especificación neutral independiente del lenguaje que describe en formato JSON o YAML las capacidades operacionales de la API: rutas disponibles, parámetros admitidos, formatos de entrada y esquemas de respuesta tipados según códigos HTTP."));
            list.Add(Paragraph("A diferencia de un archivo WSDL que exige la compilación obligatoria de stubs proxies locales con clases autogeneradas en el cliente (generando un acoplamiento binario frágil ante cambios mínimos de esquema), OpenAPI desacopla absolutamente el contrato de la implementación: los clientes pueden ser codificados mediante bibliotecas estándar de comunicación HTTP (como HttpClient de Java o HttpURLConnection en Android) o consumir la documentación interactiva generada dinámicamente mediante herramientas como Swagger UI, facilitando pruebas inmediatas sin dependencias externas."));

            list.Add(Heading2("3.3 Modelo Transaccional Bancario y Propiedades ACID en JDBC"));
            list.Add(Paragraph("Las operaciones monetarias bancarias (depósitos, retiros y transferencias) constituyen transacciones de misión crítica que deben satisfacer de manera rigurosa las propiedades ACID:"));
            list.Add(BulletParagraph("• Atomicidad (Atomicity):", "Toda operación transaccional se ejecuta como una unidad indivisible de trabajo. En una transferencia interbancaria donde se debitan $50.00 de la cuenta 001-0001 y se acreditan en la cuenta 001-0002, ambas modificaciones deben confirmarse conjuntamente mediante la instrucción commit(). Si ocurre una falla de red, un corte eléctrico o un error lógico antes de completar la segunda operación, el motor de base de datos ejecuta un rollback() total, revirtiendo todas las alteraciones intermedias y restaurando el balance exacto inicial."));
            list.Add(BulletParagraph("• Consistencia (Consistency):", "Cualquier transacción lleva a la base de datos de un estado válido a otro estado válido, respetando todas las reglas de negocio e invariantes definidas (por ejemplo: saldos monetarios mayores o iguales a cero, unicidad de número de movimiento por cuenta e integridad referencial foránea hacia clientes, sucursales y empleados)."));
            list.Add(BulletParagraph("• Aislamiento (Isolation):", "Las transacciones concurrentes ejecutadas por distintas ventanillas físicas o clientes remotos no deben interferir entre sí. En este proyecto se establece el nivel de aislamiento TRANSACTION_READ_COMMITTED en conjunto con bloqueos de fila pesimistas mediante sentencias SQL SELECT ... FOR UPDATE, previniendo condiciones de lectura sucia (dirty read) y asegurando que las modificaciones de saldo se atomicen secuencialmente a nivel de registro."));
            list.Add(BulletParagraph("• Durabilidad (Durability):", "Una vez confirmada la transacción mediante la instrucción commit(), los cambios persisten de manera irreversible en el almacenamiento secundario no volátil (registros redo log y tablespaces del motor InnoDB), subsistiendo incluso ante caídas abruptas del servidor o reinicios del contenedor."));
            list.Add(Paragraph("Para neutralizar de forma matemática la posibilidad de bloqueos mutuos (Deadlocks) durante transferencias entre cuentas bancarias concurrentes (donde la Ventanilla 1 intenta transferir de la Cuenta A a la Cuenta B y simultáneamente la Ventanilla 2 intenta transferir de la Cuenta B a la Cuenta A), se implementa una técnica de ordenamiento alfabético determinista sobre los números de cuenta. Al ordenar lexicográficamente las cuentas antes de solicitar los candados SELECT ... FOR UPDATE, todos los hilos del sistema adquieren los bloqueos de fila en un orden global idéntico, eliminando la espera circular y previniendo interbloqueos."));

            list.Add(Heading2("3.4 Patrón Arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro)"));
            list.Add(Paragraph("El patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) organiza los componentes de un aplicativo computacional en tres capas fundamentales con responsabilidades claramente delimitadas, sin admitir capas intermedias artificiales de 'Servicio' ajenas ni ViewModels desalineados con la arquitectura canónica:"));
            list.Add(BulletParagraph("1. Capa Modelo (Model):", "Representa la información del dominio del negocio bancario y las estructuras de datos (CuentaDetalleDTO, MovimientoDTO, OperacionRespuestaDTO, SucursalDTO, AuthRespuestaDTO) junto con los mecanismos de persistencia JDBC (CuentaDAO, MovimientoDAO, EmpleadoDAO, SucursalDAO) en el servidor central, o los servicios consumidores HTTP/JSON (EurekaBankService) en los clientes remotos. El Modelo no posee referencia alguna a interfaces gráficas ni al entorno de ejecución visual."));
            list.Add(BulletParagraph("2. Capa Vista (View):", "Es la interfaz encargada de presentar los datos de forma visual al usuario y capturar sus interacciones (eventos de teclado, clics de botones, selección de filas en tablas). En el Cliente Consola corresponde a ConsolaView; en Escritorio, a LoginView y DashboardView de Swing; en Móvil, a los layouts XML y Activities de Android; y en Web, a las plantillas Facelets XHTML. La Vista es pasiva y delega toda la gestión al Controlador."));
            list.Add(BulletParagraph("3. Capa Controlador (Controller):", "Actúa como el mediador central del flujo del aplicativo. Recibe los eventos disparados por la Vista, procesa y valida las entradas del usuario, invoca los métodos pertinentes del Modelo, evalúa las respuestas transaccionales y notifica a la Vista para que actualice sus elementos gráficos. En este ecosistema, el servidor central dispone de OperacionesController, mientras que cada cliente cuenta con un AppControlador desacoplado que canaliza las peticiones hacia la API RESTful."));

            list.Add(Heading2("3.5 Servidor de Aplicaciones Payara Server Enterprise y Jakarta RESTful Web Services (JAX-RS / Jersey)"));
            list.Add(Paragraph("Payara Server Enterprise es un servidor de aplicaciones de código abierto optimizado para infraestructuras corporativas de alto rendimiento, derivado de la base de código de GlassFish Server y completamente compatible con la especificación Jakarta EE 10. Payara incorpora una implementación de alto desempeño para servicios web RESTful basada en Eclipse Jersey y la especificación Jakarta RESTful Web Services 3.1 (JAX-RS)."));
            list.Add(Paragraph("Jakarta RESTful Web Services simplifica el desarrollo de servicios web al proporcionar un conjunto robusto de anotaciones declarativas del lenguaje Java:"));
            list.Add(BulletParagraph("• @ApplicationPath(\"/api\"):", "Define el prefijo base de ruteo para todos los recursos RESTful de la aplicación web."));
            list.Add(BulletParagraph("• @Path(\"/\"):", "Mapea la clase de recurso o método a una URI relativa dentro del contexto de la aplicación."));
            list.Add(BulletParagraph("• @Produces(MediaType.APPLICATION_JSON) y @Consumes(MediaType.APPLICATION_JSON):", "Establece que el recurso consume y genera cargas útiles en formato JSON, activando los serializadores automáticos del contenedor."));
            list.Add(BulletParagraph("• @GET y @POST:", "Asocian los métodos Java a los respectivos verbos de acción del protocolo HTTP."));
            list.Add(BulletParagraph("• @PathParam y @QueryParam:", "Permiten inyectar parámetros provenientes de segmentos de la ruta URL (ej. /cuentas/{numeroCuenta}) o de la cadena de consulta (?sucursal=1) directamente en las variables de los métodos."));
            list.Add(BulletParagraph("• Filtro CORS (@Provider / ContainerResponseFilter):", "Permite inyectar cabeceras HTTP de Cross-Origin Resource Sharing (Access-Control-Allow-Origin: *, Access-Control-Allow-Methods, Access-Control-Allow-Headers), facultando a navegadores web y clientes heterogéneos a consumir la API sin bloqueos de seguridad por política del mismo origen (Same-Origin Policy)."));

            list.Add(Heading2("3.6 Motor de Base de Datos MySQL 8.0 y Despliegue en Contenedores (Docker / Podman)"));
            list.Add(Paragraph("La persistencia relacional del sistema bancario se implementa sobre el motor de base de datos relacional MySQL 8.0 utilizando el motor de almacenamiento transaccional InnoDB. Para garantizar portabilidad absoluta e independencia del sistema operativo anfitrión, la base de datos se despliega dentro de un contenedor local gestionado mediante Podman o Docker Compose."));
            list.Add(Paragraph("Esta aproximación basada en contenedores ofrece ventajas técnicas decisivas:"));
            list.Add(BulletParagraph("• Aislamiento y Reproducibilidad:", "El entorno de ejecución de la base de datos (versión exacta de MySQL, configuraciones de variables de entorno, zona horaria y juegos de caracteres UTF-8) queda completamente sellado, garantizando que el comportamiento sea idéntico en cualquier máquina del equipo de trabajo."));
            list.Add(BulletParagraph("• Persistencia Segura de Datos:", "Mediante el uso de volúmenes persistentes nombrados (eurekabank_mysql_data), los datos de las cuentas bancarias, saldos y movimientos cronológicos sobreviven intactos a través de sucesivos reinicios o destrucciones del ciclo de vida del contenedor."));
            list.Add(BulletParagraph("• Inicialización Automatizada:", "A través del punto de montaje /docker-entrypoint-initdb.d/, el script DDL eureka_bank.sql se ejecuta automáticamente en el arranque inicial del contenedor, creando el esquema eurekabank_db con todas sus tablas e insertando los registros maestros sin intervención manual."));

            list.Add(Heading2("3.7 Mecanismo de Sincronización Reactiva Semáforo MashUp de Concurrencia"));
            list.Add(Paragraph("El Semáforo MashUp de Concurrencia es un mecanismo de coordinación reactivo diseñado para sincronizar en tiempo real el acceso a las cuentas bancarias entre ventanillas de atención física y canales remotos concurrentes. Cuando una ventanilla inicia una operación transaccional (depósito, retiro o transferencia) sobre una cuenta bancaria (ej. cuenta 001-0001), el sistema activa de forma inmediata el estado ROJO en el componente GestorConcurrencia y en la tabla control_mashup de MySQL, registrando el número de ventanilla operativa y el nombre del operador bancario responsable."));
            list.Add(Paragraph("Durante la ejecución de la transacción, el sistema introduce una ventana de latencia controlada (3.5 segundos) con fines de demostración académica y validación visual. En dicho lapso, cualquier otro cliente (Consola, Escritorio, Móvil o Web) que consulte la cuenta bancaria observará el indicador del semáforo en ROJO ('Ocupado en Ventanilla 1 por Monster') y no podrá solapar operaciones transaccionales sobre la misma cuenta, retornando el servidor un código HTTP 409 Conflict con el mensaje 'CONCURRENCIA_BLOQUEADA'. Una vez ejecutado el commit transaccional, el semáforo se libera automáticamente retornando al estado VERDE ('Libre'), permitiendo que el siguiente operador proceda de inmediato."));

            // =========================================================================
            // SECCIÓN 4: DESARROLLO DEL APLICATIVO
            // =========================================================================
            list.Add(Heading1("4 DESARROLLO DEL APLICATIVO"));
            list.Add(Paragraph("A continuación se describe de forma exhaustiva y paso a paso el procedimiento técnico de creación, configuración, codificación y verificación de cada uno de los subsistemas del ecosistema EurekaBank RESTful con base de datos en Java, incluyendo el Servidor Web RESTful y los cuatro clientes heterogéneos (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces). Cada bloque de código fuente relevante se presenta formalmente encapsulado en tablas numeradas para su correspondiente registro en el Índice de Tablas."));

            // -------------------------------------------------------------------------
            // 4.1 SERVIDOR WEB RESTful
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.1 Servidor Web RESTful (WS_EurekaBank_Java_REST)"));
            list.Add(Paragraph("El servidor web central aloja la lógica transaccional bancaria, la conexión JDBC con el contenedor de base de datos relacional MySQL, el gestor de concurrencia MashUp y el punto de enlace Jakarta RESTful Web Services (JAX-RS / Jersey) que publica los endpoints bajo el protocolo HTTP/JSON."));

            list.Add(Heading3("4.1.1 Paso 1: Configuración de la Base de Datos Relacional EurekaBank en MySQL Contenerizado"));
            list.Add(Paragraph("Dentro del directorio 03. BDD del proyecto se localizan el archivo de orquestación docker-compose.yml y el script SQL de definición y datos eureka_bank.sql. El archivo docker-compose.yml define el servicio de base de datos mysql-db exponiendo el puerto estándar 3306 hacia el sistema anfitrión:"));

            list.Add(TableCaption(1, "Archivo de Orquestación de Contenedores docker-compose.yml"));
            list.Add(CodeBox("""
version: "3.9"
services:
  mysql-db:
    image: docker.io/library/mysql:latest
    container_name: mysql-db
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: admin
      MYSQL_DATABASE: eurekabank_db
      TZ: America/Guayaquil
    ports:
      - "3306:3306"
    volumes:
      - eurekabank_mysql_data:/var/lib/mysql
      - ./eureka_bank.sql:/docker-entrypoint-initdb.d/01_eureka_bank.sql:ro
volumes:
  eurekabank_mysql_data:
"""));

            list.Add(Paragraph("Para iniciar el contenedor de base de datos en segundo plano, se abre una terminal de comandos en el directorio 03. BDD y se ejecuta:"));
            list.Add(CodeBox("podman compose up -d\n# O alternativamente con Docker:\ndocker compose up -d"));

            list.Add(Paragraph("El script DDL eureka_bank.sql establece la estructura relacional de las tablas del negocio bancario, garantizando tipos de datos monetarios exactos (DECIMAL(12,2)) e integridad referencial mediante claves foráneas con eliminación restrictiva:"));

            list.Add(TableCaption(2, "Script SQL Completo DDL y Datos Semilla eureka_bank.sql"));
            list.Add(CodeBox("""
-- =============================================================================
-- SISTEMA BANCARIO TRANSACCIÓNAL EUREKABANK - GRUPO 04 (GR04)
-- DOMINIO: ec.edu.monster | MOTOR: MySQL / MariaDB (Docker / Podman Local :3306)
-- CREDENCIAL MAESTRA: Usuario = Monster | Contraseña = Monster9
-- =============================================================================

DROP DATABASE IF EXISTS eurekabank_db;
CREATE DATABASE eurekabank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eurekabank_db;

-- 1. Tabla: sucursal
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tabla: empleado (Autenticación y Auditoría de 4 Ventanillas + Gerencia + Superadmin Monster)
CREATE TABLE empleado (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    rol ENUM('Superadmin Monster', 'Gerente', 'Cajero') NOT NULL,
    id_sucursal INT NOT NULL DEFAULT 1,
    ventanilla INT DEFAULT NULL,
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Tabla: cliente
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20),
    email VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabla: cuenta (Soporta bloqueo pesimista SELECT ... FOR UPDATE)
CREATE TABLE cuenta (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
    saldo DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    id_cliente INT NOT NULL,
    id_sucursal INT NOT NULL,
    estado ENUM('Activa', 'Bloqueada') NOT NULL DEFAULT 'Activa',
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tabla: control_mashup (Semáforo Cromático en Tiempo Real: VERDE = Libre, ROJO = Ocupada en Ventanilla)
CREATE TABLE control_mashup (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    estado_mashup ENUM('VERDE', 'ROJO') NOT NULL DEFAULT 'VERDE',
    ventanilla INT DEFAULT NULL,
    bloqueado_por VARCHAR(100) DEFAULT NULL,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Tabla: movimiento (Auditoría e Historial Cronológico ACID)
CREATE TABLE movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    numero_operacion VARCHAR(50) NOT NULL UNIQUE,
    numero_cuenta VARCHAR(20) NOT NULL,
    id_empleado INT NOT NULL,
    ventanilla INT DEFAULT 1,
    tipo VARCHAR(40) NOT NULL,
    monto DECIMAL(12, 2) NOT NULL,
    saldo_resultante DECIMAL(12, 2) NOT NULL,
    cuenta_referencia VARCHAR(20) DEFAULT NULL,
    descripcion VARCHAR(200) DEFAULT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DATOS SEMILLA (SEEDERS) - CREDENCIALES: Monster / Monster9
-- Hash BCrypt: $2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK
INSERT INTO sucursal (codigo, nombre, ciudad, direccion) VALUES
('001', 'Sucursal Matriz ESPE', 'Sangolquí', 'Av. Gral. Rumiñahui s/n'),
('002', 'Sucursal Norte La Carolina', 'Quito', 'Av. Amazonas y Naciones Unidas'),
('003', 'Sucursal Guayaquil Malecón', 'Guayaquil', 'Malecón Simón Bolívar y 9 de Octubre');

INSERT INTO empleado (usuario, password, nombre_completo, rol, id_sucursal, ventanilla) VALUES
('Monster', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'James P. Sullivan', 'Superadmin Monster', 1, 1),
('cajero1', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Mike Wazowski', 'Cajero', 1, 1),
('cajero2', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Randall Boggs', 'Cajero', 1, 2),
('cajero3', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Celia Mae', 'Cajero', 2, 3),
('cajero4', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Roz Waternoose', 'Cajero', 3, 4);

INSERT INTO cliente (dni, nombre, apellido, direccion, telefono, email) VALUES
('1710000001', 'Arturo', 'Mora', 'Quito Centro Histórico', '0991234567', 'amora@monster.com'),
('1710000002', 'Beatriz', 'Cueva', 'Cumbayá San Juan', '0987654321', 'bcueva@monster.com'),
('1710000003', 'Carlos', 'Valdivieso', 'Valle de los Chillos', '0971122334', 'cvaldivieso@monster.com');

INSERT INTO cuenta (numero_cuenta, moneda, saldo, id_cliente, id_sucursal, estado) VALUES
('001-0001', 'USD', 2750.00, 1, 1, 'Activa'),
('001-0002', 'USD', 1420.50, 2, 1, 'Activa'),
('001-0003', 'USD', 5800.00, 3, 2, 'Activa'),
('001-0004', 'USD', 950.00, 1, 3, 'Activa');

INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por) VALUES
('001-0001', 'VERDE', NULL, NULL),
('001-0002', 'VERDE', NULL, NULL),
('001-0003', 'VERDE', NULL, NULL),
('001-0004', 'VERDE', NULL, NULL);
"""));

            list.Add(TableCaption(3, "Diccionario de Datos del Esquema Relacional EurekaBank"));
            list.Add(TechnicalTable(
                new[] { "Tabla Relacional", "Columnas Principales y Tipos", "Restricciones / Claves", "Propósito en la Arquitectura Bancaria" },
                new[] { 2200, 2800, 2200, 2304 },
                new List<string[]>
                {
                    new[] { "sucursal", "id_sucursal (INT PK), codigo (VARCHAR(10)), nombre, ciudad, direccion", "PK AUTO_INCREMENT, UNIQUE(codigo)", "Representa las oficinas físicas donde operan las ventanillas bancarias." },
                    new[] { "empleado", "id_empleado (INT PK), usuario, password, nombre_completo, rol, id_sucursal, ventanilla", "PK, UNIQUE(usuario), FK(id_sucursal)", "Operadores bancarios; almacena credenciales encriptadas con BCrypt." },
                    new[] { "cliente", "id_cliente (INT PK), dni (VARCHAR(20)), nombre, apellido, email", "PK, UNIQUE(dni)", "Titulares de cuentas bancarias y personas naturales vinculadas." },
                    new[] { "cuenta", "numero_cuenta (VARCHAR(20) PK), moneda, saldo (DECIMAL(12,2)), estado, id_cliente, id_sucursal", "PK, FK(id_cliente), FK(id_sucursal)", "Entidad central de balance financiero; soporta bloqueo pesimista SELECT FOR UPDATE." },
                    new[] { "control_mashup", "numero_cuenta (VARCHAR(20) PK), estado_mashup (ENUM 'VERDE','ROJO'), ventanilla, bloqueado_por, actualizado_en", "PK, FK(numero_cuenta) ON DELETE CASCADE", "Coordina el Semáforo MashUp de Concurrencia entre ventanillas físicas y remotas en tiempo real." },
                    new[] { "movimiento", "id_movimiento (INT PK), numero_operacion, numero_cuenta, id_empleado, ventanilla, tipo, monto, saldo_resultante, fecha_hora", "PK, UNIQUE(numero_operacion), FK(cuenta), FK(empleado)", "Auditoría cronológica inmutable de depósitos, retiros y transferencias." }
                }
            ));

            list.Add(FigureCaption(1, "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker"));
            list.Add(ScreenshotBox(
                1,
                "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker",
                "Consola de comandos / CLI de Podman o Docker.",
                "Comando: podman compose up -d (o docker compose up -d) ejecutado en el subdirectorio 03. BDD.",
                "Verificar la inicialización del contenedor mysql-db y la carga exitosa de eureka_bank.sql.",
                "Contenedor en ejecución (Up) con puerto TCP 3306 expuesto y volumen eurekabank_mysql_data activo."
            ));

            list.Add(Heading3("4.1.2 Paso 2: Creación del Proyecto Web Maven y Configuración de Dependencias Jakarta EE"));
            list.Add(Paragraph("En NetBeans IDE se inicia el asistente New Project > Java with Maven > Web Application. Se define el nombre del proyecto como WS_EurekaBank_Java_REST, se establece el GroupId institucional ec.edu.monster y se selecciona Payara Server como servidor de destino. En el archivo pom.xml se configuran las dependencias de Jakarta EE 10 (JAX-RS / Jersey), el driver oficial MySQL Connector/J y la librería jBCrypt para verificación de contraseñas:"));

            list.Add(TableCaption(4, "Descriptor de Dependencias y Plugins del Servidor pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WS_EurekaBank_Java_REST</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>WS_EurekaBank_Java_REST</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-api</artifactId>
            <version>10.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.mindrot</groupId>
            <artifactId>jbcrypt</artifactId>
            <version>0.4</version>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>WS_EurekaBank_Java_REST</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
"""));

            list.Add(FigureCaption(2, "Creación y Estructura del Proyecto Servidor Maven en NetBeans IDE"));
            list.Add(ScreenshotBox(
                2,
                "Creación y Estructura del Proyecto Servidor Maven en NetBeans IDE",
                "NetBeans IDE - Asistente New Project > Java with Maven > Web Application.",
                "Project Name: WS_EurekaBank_Java_REST | Group Id: ec.edu.monster | Target Server: Payara Server.",
                "Crear el proyecto, abrir pom.xml y verificar la resolución de dependencias Maven.",
                "Árbol del proyecto generado con la carpeta Dependencies mostrando jakarta.jakartaee-api, mysql-connector-j y jbcrypt correctamente vinculados."
            ));

            list.Add(Heading3("4.1.3 Paso 3: Estructuración Modular de Paquetes del Servidor RESTful (MVC Tradicional)"));
            list.Add(Paragraph("Se estructuran los paquetes Java dentro de src/main/java siguiendo rigurosamente el patrón arquitectónico MVC Tradicional puro, organizando claramente los controladores, servicios transaccionales, DAOs de acceso a datos, modelos DTO y utilitarios de concurrencia:"));

            list.Add(TableCaption(5, "Estructura de Paquetes y Responsabilidades del Servidor RESTful (MVC Tradicional)"));
            list.Add(TechnicalTable(
                new[] { "Paquete", "Clases Principales", "Responsabilidad Arquitectónica (MVC Tradicional)" },
                new[] { 2800, 2700, 3004 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.rest", "RestApplication.java, EurekaBankResource.java, CorsFilter.java", "Punto de enlace Jakarta RESTful Web Services (/api/*), recurso principal JAX-RS y filtro CORS." },
                    new[] { "ec.edu.monster.controlador", "OperacionesController.java", "Controlador orquestador central que canaliza peticiones hacia la lógica transaccional y de seguridad." },
                    new[] { "ec.edu.monster.servicio", "TransaccionService.java, SeguridadService.java", "Servicios de negocio que gobiernan la lógica ACID, exclusión mutua de 3.5s y autenticación con BCrypt." },
                    new[] { "ec.edu.monster.dao", "CuentaDAO.java, MovimientoDAO.java, EmpleadoDAO.java, SucursalDAO.java", "Capa de persistencia JDBC con sentencias SQL preparadas, control transaccional ACID y bloqueos SELECT FOR UPDATE." },
                    new[] { "ec.edu.monster.modelo (y .peticion)", "CuentaDetalleDTO, MovimientoDTO, OperacionRespuestaDTO, AuthRespuestaDTO, LoginRequest, OperacionRequest, TransferenciaRequest", "Objetos de transferencia serializables automáticamente en JSON (DTOs)." },
                    new[] { "ec.edu.monster.util", "ConexionDB.java, GestorConcurrencia.java, SeguridadUtil.java", "Factoría de conexiones JDBC, gestor de concurrencia del Semáforo MashUp y utilitarios de cifrado." }
                }
            ));

            list.Add(FigureCaption(3, "Estructura de Paquetes del Servidor RESTful en NetBeans IDE"));
            list.Add(ScreenshotBox(
                3,
                "Estructura de Paquetes del Servidor RESTful en NetBeans IDE",
                "NetBeans IDE - Vista Projects / Files.",
                "Expandir el nodo Source Packages del proyecto WS_EurekaBank_Java_REST.",
                "Inspeccionar los paquetes ec.edu.monster.rest, controlador, dao, modelo, servicio y util.",
                "Estructura de paquetes visualizada con todos los archivos .java compilados sin errores ni advertencias."
            ));

            list.Add(Heading3("4.1.4 Paso 4: Capa de Conexión a Base de Datos (ConexionDB.java)"));
            list.Add(Paragraph("Se crea la clase ConexionDB en el paquete ec.edu.monster.util. Esta clase carga el driver com.mysql.cj.jdbc.Driver y establece conexiones JDBC con el contenedor local MySQL sobre el puerto 3306, soportando variables de entorno del sistema (EUREKABANK_DB_URL, EUREKABANK_DB_USER, EUREKABANK_DB_PASS) y configurando el nivel de aislamiento TRANSACTION_READ_COMMITTED:"));

            list.Add(TableCaption(6, "Factoría de Conexiones JDBC ConexionDB.java"));
            list.Add(CodeBox("""
package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/eurekabank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Guayaquil&characterEncoding=UTF-8";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontró el driver JDBC de MySQL: " + e.getMessage());
        }
    }

    private ConexionDB() {}

    public static Connection getConexion() throws SQLException {
        String url = obtenerPropiedad("EUREKABANK_DB_URL", "eurekabank.db.url", DEFAULT_URL);
        String user = obtenerPropiedad("EUREKABANK_DB_USER", "eurekabank.db.user", DEFAULT_USER);
        String pass = obtenerPropiedad("EUREKABANK_DB_PASS", "eurekabank.db.pass", DEFAULT_PASS);
        Connection conn = DriverManager.getConnection(url, user, pass);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }

    private static String obtenerPropiedad(String envKey, String sysKey, String valorDefecto) {
        String sys = System.getProperty(sysKey);
        if (sys != null && !sys.isBlank()) return sys.trim();
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) return env.trim();
        return valorDefecto;
    }
}
"""));

            list.Add(Heading3("4.1.5 Paso 5: Implementación de Entidades del Dominio y Modelos DTO (Data Transfer Objects)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.modelo se implementan los objetos de transferencia de datos (DTOs) que representan las respuestas y solicitudes JSON. Estas clases implementan Serializable y encapsulan los atributos de cuentas bancarias, extractos cronológicos y estados del Semáforo MashUp:"));

            list.Add(TableCaption(7, "Modelo DTO de Cuenta Bancaria y Estado MashUp CuentaDetalleDTO.java"));
            list.Add(CodeBox("""
package ec.edu.monster.modelo;

import java.io.Serializable;

public class CuentaDetalleDTO implements Serializable {
    private String numeroCuenta;
    private String moneda;
    private double saldo;
    private String estadoCuenta;
    private String disponibilidadMashup; // "VERDE" o "ROJO"
    private int ventanillaBloqueo;
    private String bloqueadoPor;
    private int idCliente;
    private String dniCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private int idSucursal;
    private String nombreSucursal;
    private String ciudadSucursal;

    public CuentaDetalleDTO() {}

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
    public String getDisponibilidadMashup() { return disponibilidadMashup; }
    public void setDisponibilidadMashup(String disponibilidadMashup) { this.disponibilidadMashup = disponibilidadMashup; }
    public int getVentanillaBloqueo() { return ventanillaBloqueo; }
    public void setVentanillaBloqueo(int ventanillaBloqueo) { this.ventanillaBloqueo = ventanillaBloqueo; }
    public String getBloqueadoPor() { return bloqueadoPor; }
    public void setBloqueadoPor(String bloqueadoPor) { this.bloqueadoPor = bloqueadoPor; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }
    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    public String getNombreSucursal() { return nombreSucursal; }
    public void setNombreSucursal(String nombreSucursal) { this.nombreSucursal = nombreSucursal; }
}
"""));

            list.Add(TableCaption(8, "Modelo DTO de Auditoría e Historial Cronológico MovimientoDTO.java"));
            list.Add(CodeBox("""
package ec.edu.monster.modelo;

import java.io.Serializable;

public class MovimientoDTO implements Serializable {
    private int idMovimiento;
    private String numeroOperacion;
    private String numeroCuenta;
    private int idEmpleado;
    private String nombreEmpleado;
    private int ventanilla;
    private String tipo;
    private double monto;
    private double saldoResultante;
    private String cuentaReferencia;
    private String descripcion;
    private String fechaHora;

    public MovimientoDTO() {}

    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public double getSaldoResultante() { return saldoResultante; }
    public void setSaldoResultante(double saldoResultante) { this.saldoResultante = saldoResultante; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
}
"""));

            list.Add(TableCaption(9, "Modelo DTO de Respuesta de Operación Transaccional OperacionRespuestaDTO.java"));
            list.Add(CodeBox("""
package ec.edu.monster.modelo;

import java.io.Serializable;

public class OperacionRespuestaDTO implements Serializable {
    private boolean exito;
    private String codigo; // OK, SALDO_INSUFICIENTE, CONCURRENCIA_BLOQUEADA, CUENTA_NO_EXISTE, ERROR_VALIDACIÓN
    private String mensaje;
    private String numeroOperacion;
    private String numeroCuenta;
    private double saldoAnterior;
    private double monto;
    private double saldoActual;
    private String fechaHora;

    public OperacionRespuestaDTO() {}

    public static OperacionRespuestaDTO error(String codigo, String mensaje) {
        OperacionRespuestaDTO r = new OperacionRespuestaDTO();
        r.setExito(false);
        r.setCodigo(codigo);
        r.setMensaje(mensaje);
        return r;
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getNumeroOperacion() { return numeroOperacion; }
    public void setNumeroOperacion(String numeroOperacion) { this.numeroOperacion = numeroOperacion; }
    public double getSaldoActual() { return saldoActual; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
"""));

            list.Add(Heading3("4.1.6 Paso 6: Implementación de la Capa de Persistencia Transaccional ACID (CuentaDAO.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.dao se implementa la clase CuentaDAO, la cual ejecuta consultas y actualizaciones JDBC transaccionales sobre la base de datos MySQL. Se utiliza bloqueo pesimista mediante SELECT ... FOR UPDATE sobre la tabla cuenta para aislar las filas contra modificaciones concurrentes:"));

            list.Add(TableCaption(10, "Capa de Persistencia Transaccional ACID y Bloqueos Pesimistas CuentaDAO.java"));
            list.Add(CodeBox("""
package ec.edu.monster.dao;

import ec.edu.monster.modelo.CuentaDetalleDTO;
import ec.edu.monster.util.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    private static final String BASE_SELECT =
            "SELECT cu.numero_cuenta, cu.moneda, cu.saldo, cu.estado, cu.id_cliente, cu.id_sucursal, "
            + "cl.dni, cl.nombre AS nombre_cliente, cl.apellido AS apellido_cliente, cl.email, "
            + "su.nombre AS nombre_sucursal, su.ciudad AS ciudad_sucursal, "
            + "COALESCE(cm.estado_mashup, 'VERDE') AS estado_mashup, "
            + "COALESCE(cm.ventanilla, 0) AS ventanilla_bloqueo, "
            + "cm.bloqueado_por "
            + "FROM cuenta cu "
            + "INNER JOIN cliente cl ON cu.id_cliente = cl.id_cliente "
            + "INNER JOIN sucursal su ON cu.id_sucursal = su.id_sucursal "
            + "LEFT JOIN control_mashup cm ON cu.numero_cuenta = cm.numero_cuenta ";

    public CuentaDetalleDTO obtenerCuentaForUpdate(Connection con, String numeroCuenta) throws SQLException {
        String sql = "SELECT numero_cuenta, moneda, saldo, estado, id_cliente, id_sucursal "
                + "FROM cuenta WHERE numero_cuenta = ? FOR UPDATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CuentaDetalleDTO dto = new CuentaDetalleDTO();
                    dto.setNumeroCuenta(rs.getString("numero_cuenta"));
                    dto.setMoneda(rs.getString("moneda"));
                    dto.setSaldo(rs.getDouble("saldo"));
                    dto.setEstadoCuenta(rs.getString("estado"));
                    dto.setIdCliente(rs.getInt("id_cliente"));
                    dto.setIdSucursal(rs.getInt("id_sucursal"));
                    return dto;
                }
            }
        }
        return null;
    }

    public boolean actualizarSaldo(Connection con, String numeroCuenta, double nuevoSaldo) throws SQLException {
        String sql = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setString(2, numeroCuenta.trim());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CuentaDetalleDTO> listarCuentasPorSucursal(int sucursalId) {
        List<CuentaDetalleDTO> lista = new ArrayList<>();
        String sql = sucursalId <= 0
                ? BASE_SELECT + "ORDER BY cu.numero_cuenta ASC"
                : BASE_SELECT + "WHERE cu.id_sucursal = ? ORDER BY cu.numero_cuenta ASC";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (sucursalId > 0) ps.setInt(1, sucursalId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CuentaDetalleDTO dto = new CuentaDetalleDTO();
                    dto.setNumeroCuenta(rs.getString("numero_cuenta"));
                    dto.setSaldo(rs.getDouble("saldo"));
                    dto.setDisponibilidadMashup(rs.getString("estado_mashup"));
                    dto.setVentanillaBloqueo(rs.getInt("ventanilla_bloqueo"));
                    dto.setBloqueadoPor(rs.getString("bloqueado_por"));
                    dto.setNombreCliente(rs.getString("nombre_cliente"));
                    dto.setApellidoCliente(rs.getString("apellido_cliente"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cuentas: " + e.getMessage());
        }
        return lista;
    }
}
"""));

            list.Add(Heading3("4.1.7 Paso 7: Implementación del Gestor de Concurrencia y Sincronización MashUp (GestorConcurrencia.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.util se implementa GestorConcurrencia. Esta clase coordina los estados del semáforo atómicamente en memoria utilizando ConcurrentHashMap y respalda de inmediato el estado en la tabla control_mashup de MySQL:"));

            list.Add(TableCaption(11, "Componente de Coordinación del Semáforo MashUp GestorConcurrencia.java"));
            list.Add(CodeBox("""
package ec.edu.monster.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class GestorConcurrencia {
    public static final String VERDE = "VERDE";
    public static final String ROJO = "ROJO";
    private static final GestorConcurrencia INSTANCIA = new GestorConcurrencia();

    public static class InfoBloqueo {
        private final String estado;
        private final int ventanilla;
        private final String usuario;
        public InfoBloqueo(String estado, int ventanilla, String usuario) {
            this.estado = estado; this.ventanilla = ventanilla; this.usuario = usuario;
        }
        public String getEstado() { return estado; }
        public int getVentanilla() { return ventanilla; }
        public String getUsuario() { return usuario; }
    }

    private final ConcurrentHashMap<String, InfoBloqueo> bloqueosMemoria = new ConcurrentHashMap<>();

    private GestorConcurrencia() {}

    public static GestorConcurrencia getInstancia() { return INSTANCIA; }

    public synchronized boolean bloquearCuenta(String numeroCuenta, int ventanilla, String usuario) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) return false;
        String key = numeroCuenta.trim();
        InfoBloqueo actual = obtenerInfoBloqueo(key);
        if (ROJO.equalsIgnoreCase(actual.getEstado())) return false;

        String sql = "INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por, actualizado_en) "
                + "VALUES (?, 'ROJO', ?, ?, CURRENT_TIMESTAMP) "
                + "ON DUPLICATE KEY UPDATE "
                + "estado_mashup = IF(estado_mashup = 'VERDE', 'ROJO', estado_mashup), "
                + "ventanilla = IF(estado_mashup = 'ROJO', VALUES(ventanilla), ventanilla), "
                + "bloqueado_por = IF(estado_mashup = 'ROJO', VALUES(bloqueado_por), bloqueado_por), "
                + "actualizado_en = CURRENT_TIMESTAMP";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, key); ps.setInt(2, ventanilla); ps.setString(3, usuario);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                bloqueosMemoria.put(key, new InfoBloqueo(ROJO, ventanilla, usuario));
                return true;
            }
        } catch (SQLException e) { System.err.println("Error SQL al bloquear: " + e.getMessage()); }
        return false;
    }

    public synchronized void liberarCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) return;
        String key = numeroCuenta.trim();
        String sql = "UPDATE control_mashup SET estado_mashup = 'VERDE', ventanilla = NULL, bloqueado_por = NULL, actualizado_en = CURRENT_TIMESTAMP WHERE numero_cuenta = ?";
        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
        bloqueosMemoria.put(key, new InfoBloqueo(VERDE, 0, ""));
    }

    public InfoBloqueo obtenerInfoBloqueo(String numeroCuenta) {
        return bloqueosMemoria.getOrDefault(numeroCuenta.trim(), new InfoBloqueo(VERDE, 0, ""));
    }
}
"""));

            list.Add(Heading3("4.1.8 Paso 8: Implementación y Publicación del Recurso Jakarta RESTful (EurekaBankResource.java y CorsFilter.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.rest se implementan la clase RestApplication para registrar la ruta base /api, CorsFilter para permitir llamadas entre orígenes distintos, y EurekaBankResource como el punto de enlace JAX-RS que expone las operaciones financieras sobre HTTP/JSON:"));

            list.Add(TableCaption(12, "Recurso Jakarta RESTful Principal EurekaBankResource.java y CorsFilter.java"));
            list.Add(CodeBox("""
package ec.edu.monster.rest;

import ec.edu.monster.controlador.OperacionesController;
import ec.edu.monster.modelo.*;
import ec.edu.monster.modelo.peticion.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EurekaBankResource {

    private final OperacionesController controlador = new OperacionesController();

    @POST
    @Path("/auth/login")
    public Response autenticarUsuario(LoginRequest req) {
        if (req == null) return Response.status(Response.Status.BAD_REQUEST).build();
        AuthRespuestaDTO resp = controlador.autenticarUsuario(req.getUsuario(), req.getClave());
        return resp.isAutenticado() ? Response.ok(resp).build() : Response.status(Response.Status.UNAUTHORIZED).entity(resp).build();
    }

    @GET
    @Path("/cuentas")
    public List<CuentaDetalleDTO> listarCuentasPorSucursal(@QueryParam("sucursal") @DefaultValue("0") int sucursalId) {
        return controlador.listarCuentasPorSucursal(sucursalId);
    }

    @GET
    @Path("/cuentas/{numeroCuenta}")
    public Response obtenerCuentaPorNumero(@PathParam("numeroCuenta") String numeroCuenta) {
        CuentaDetalleDTO cuenta = controlador.obtenerCuentaPorNumero(numeroCuenta);
        if (cuenta == null) return Response.status(Response.Status.NOT_FOUND).entity(OperacionRespuestaDTO.error("CUENTA_NO_EXISTE", "No existe")).build();
        return Response.ok(cuenta).build();
    }

    @GET
    @Path("/cuentas/{numeroCuenta}/movimientos")
    public List<MovimientoDTO> consultarExtracto(@PathParam("numeroCuenta") String numeroCuenta) {
        return controlador.consultarExtracto(numeroCuenta);
    }

    @GET
    @Path("/mashup")
    public List<CuentaDetalleDTO> consultarEstadoMashUp() {
        return controlador.listarCuentasPorSucursal(0);
    }

    @POST
    @Path("/movimientos/deposito")
    public Response depositar(OperacionRequest req) {
        OperacionRespuestaDTO res = controlador.depositar(req.getNumeroCuenta(), req.getMonto(), req.getEmpleadoId(), req.getVentanilla());
        return construirRespuestaOperacion(res);
    }

    @POST
    @Path("/movimientos/transferencia")
    public Response transferir(TransferenciaRequest req) {
        OperacionRespuestaDTO res = controlador.transferir(req.getCuentaOrigen(), req.getCuentaDestino(), req.getMonto(), req.getEmpleadoId(), req.getVentanilla());
        return construirRespuestaOperacion(res);
    }

    private Response construirRespuestaOperacion(OperacionRespuestaDTO res) {
        if (res.isExito()) return Response.ok(res).build();
        if ("CONCURRENCIA_BLOQUEADA".equals(res.getCodigo())) return Response.status(Response.Status.CONFLICT).entity(res).build();
        if ("CUENTA_NO_EXISTE".equals(res.getCodigo())) return Response.status(Response.Status.NOT_FOUND).entity(res).build();
        return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
    }
}
"""));

            list.Add(TableCaption(13, "Catálogo de Endpoints y Operaciones de la API RESTful de EurekaBank"));
            list.Add(TechnicalTable(
                new[] { "Método HTTP y Ruta URI", "Parámetros de Entrada", "Tipo de Retorno / Códigos", "Propósito en la Arquitectura Bancaria" },
                new[] { 2600, 2400, 2000, 2104 },
                new List<string[]>
                {
                    new[] { "POST /api/auth/login", "JSON: { usuario, clave }", "AuthRespuestaDTO (200 / 401)", "Valida credenciales maestras (Monster/Monster9) con hash BCrypt retornando rol y ventanilla." },
                    new[] { "GET /api/sucursales", "Ninguno", "List<SucursalDTO> (200)", "Retorna el listado de agencias y sucursales físicas habilitadas en el sistema bancario." },
                    new[] { "GET /api/cuentas", "QueryParam: sucursal (int)", "List<CuentaDetalleDTO> (200)", "Retorna cuentas bancarias con saldo y disponibilidad cromática MashUp (VERDE/ROJO)." },
                    new[] { "GET /api/cuentas/{nro}", "PathParam: numeroCuenta", "CuentaDetalleDTO (200 / 404)", "Obtiene los detalles integrales y balance de una cuenta bancaria específica." },
                    new[] { "GET /api/clientes/{dni}/cuentas", "PathParam: dni (String)", "List<CuentaDetalleDTO> (200)", "Consulta todas las cuentas bancarias asociadas al documento de identidad del cliente." },
                    new[] { "GET /api/cuentas/{nro}/movimientos", "PathParam: numeroCuenta", "List<MovimientoDTO> (200)", "Extrae el historial cronológico de auditoría con número, tipo, monto y fecha de operación." },
                    new[] { "GET /api/mashup", "Ninguno", "List<CuentaDetalleDTO> (200)", "Consulta en tiempo real del estado VERDE/ROJO para sincronización reactiva de ventanillas." },
                    new[] { "POST /api/movimientos/deposito", "JSON: { numeroCuenta, monto, ... }", "OperacionRespuestaDTO (200/409)", "Acredita fondos con exclusión mutua de 3.5s, semáforo ROJO temporal y commit en InnoDB." },
                    new[] { "POST /api/movimientos/retiro", "JSON: { numeroCuenta, monto, ... }", "OperacionRespuestaDTO (200/409)", "Debita fondos con validación de saldo positivo, semáforo ROJO y registro de auditoría." },
                    new[] { "POST /api/movimientos/transferencia", "JSON: { origen, destino, monto, ... }", "OperacionRespuestaDTO (200/409)", "Transferencia atómica interbancaria con ordenamiento alfabético anti-deadlock." }
                }
            ));

            list.Add(Heading3("4.1.9 Paso 9: Compilación y Despliegue en Payara Server Enterprise"));
            list.Add(Paragraph("Se compila y empaqueta el artefacto WAR mediante Maven ejecutando mvn clean package. Se inicia la instancia de Payara Server y se despliega el archivo WS_EurekaBank_Java_REST.war. La API RESTful queda publicada y accesible en la dirección: http://localhost:8080/WS_EurekaBank_Java_REST/api/cuentas"));

            list.Add(FigureCaption(4, "Endpoint RESTful de Cuentas Publicado en Payara Server (http://localhost:8080/WS_EurekaBank_Java_REST/api/cuentas)"));
            list.Add(ScreenshotBox(
                4,
                "Endpoint RESTful de Cuentas Publicado en Payara Server (http://localhost:8080/WS_EurekaBank_Java_REST/api/cuentas)",
                "Navegador Web (Google Chrome / Mozilla Firefox / Microsoft Edge) o cliente HTTP (Postman).",
                "URL: http://localhost:8080/WS_EurekaBank_Java_REST/api/cuentas (Método GET)",
                "Ingresar a la URL y verificar la respuesta JSON serializada por Jakarta RESTful Web Services.",
                "Estructura JSON completa conteniendo el arreglo de cuentas bancarias (numeroCuenta, saldo, disponibilidadMashup, nombreCliente, apellidoCliente) serializadas en tiempo real."
            ));

            list.Add(FigureCaption(5, "Consola de Administración de Payara Server con la Aplicación Desplegada"));
            list.Add(ScreenshotBox(
                5,
                "Consola de Administración de Payara Server con la Aplicación Desplegada",
                "Payara Server Administration Console (http://localhost:4848).",
                "Navegación: Applications > WS_EurekaBank_Java_REST.",
                "Verificar que la aplicación web se encuentre en estado 'Enabled' y los endpoints web services activos.",
                "Panel de Payara mostrando la aplicación habilitada con enlace de prueba al servicio RESTful y estado de salud óptimo."
            ));

            // -------------------------------------------------------------------------
            // 4.2 CLIENTE CONSOLA
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.2 Cliente Consola (WSEurekaBankConsoleClient)"));
            list.Add(Paragraph("El Cliente de Consola es un aplicativo Java puro basado en Maven que interactúa con el operador bancario a través de la terminal de comandos. Está diseñado bajo el patrón arquitectónico MVC Tradicional Puro y contiene una rutina especial multi-hilo de simulación de concurrencia que pone a prueba la exclusión mutua coordinada por el Semáforo MashUp."));

            list.Add(Heading3("4.2.1 Paso 1: Creación del Proyecto Maven y Configuración de Dependencias (HttpClient + Gson)"));
            list.Add(Paragraph("En NetBeans IDE se inicia el asistente New Project > Java with Maven > Java Application con el nombre WSEurekaBankConsoleClient y GroupId ec.edu.monster. En pom.xml se configura la dependencia com.google.code.gson para la serialización y deserialización de estructuras JSON, utilizando la API estándar java.net.http.HttpClient de Java 17 para la comunicación HTTP sin requerir generadores de código SOAP:"));

            list.Add(TableCaption(14, "Configuración de Dependencias del Cliente Consola pom.xml (Gson + HttpClient)"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankConsoleClient</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>WSEurekaBankConsoleClient_REST</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <exec.mainClass>ec.edu.monster.clicon.WSEurekaBankConsoleClient</exec.mainClass>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
"""));

            list.Add(FigureCaption(6, "Consumo de la API RESTful y Compilación del Cliente Consola en NetBeans IDE"));
            list.Add(ScreenshotBox(
                6,
                "Consumo de la API RESTful y Compilación del Cliente Consola en NetBeans IDE",
                "NetBeans IDE - Ejecución de Maven Build / Compile.",
                "Endpoint Base RESTful: http://localhost:8080/WS_EurekaBank_Java_REST/api/.",
                "Ejecutar 'Clean and Build' sobre el proyecto WSEurekaBankConsoleClient.",
                "Directorio target poblado con los binarios compilados y la dependencia com.google.code.gson resuelta para la comunicación RESTful con el servidor."
            ));

            list.Add(Heading3("4.2.2 Paso 2: Estructuración Modular MVC Tradicional del Cliente Consola"));
            list.Add(Paragraph("Se estructuran los paquetes del cliente consola separando estrictamente modelo, vista y controlador:"));

            list.Add(TableCaption(15, "Estructura de Paquetes MVC del Cliente Consola"));
            list.Add(TechnicalTable(
                new[] { "Paquete", "Clases Principales", "Responsabilidad en el Cliente Consola" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.clicon.controller", "AppControlador.java", "Maneja la sesión del operador, valida entradas y orquesta peticiones entre la vista y el servicio RESTful." },
                    new[] { "ec.edu.monster.clicon.view", "ConsolaView.java", "Imprime menús tabulados con secuencias ANSI, lee entradas con Scanner y ejecuta la rutina multi-hilo de 3 ventanillas." },
                    new[] { "ec.edu.monster.clicon.model", "EurekaBankService.java, ConfiguracionServidor.java, CuentaModel.java, MovimientoModel.java, ResultadoOperacion.java, UsuarioSesion.java", "Consumidor HTTP nativo con java.net.http.HttpClient y Gson, encapsulando las llamadas a los endpoints RESTful." }
                }
            ));

            list.Add(Heading3("4.2.3 Paso 3: Implementación del Consumidor RESTful y Modelo de Datos (EurekaBankService.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.clicon.model se implementa EurekaBankService. Esta clase instancia HttpClient y Gson para invocar los endpoints JSON del servidor central:"));

            list.Add(TableCaption(16, "Consumidor HTTP/JSON RESTful EurekaBankService.java (Consola)"));
            list.Add(CodeBox("""
package ec.edu.monster.clicon.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class EurekaBankService {
    private final ConfiguracionServidor config = new ConfiguracionServidor();
    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final Gson gson = new Gson();

    public UsuarioSesion login(String usuario, String clave) {
        Map<String, Object> body = new HashMap<>();
        body.put("usuario", usuario);
        body.put("clave", clave);
        String jsonResp = enviarPost("/auth/login", gson.toJson(body));
        return gson.fromJson(jsonResp, UsuarioSesion.class);
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        String jsonResp = enviarGet("/cuentas?sucursal=" + sucursalId);
        List<CuentaModel> list = gson.fromJson(jsonResp, new TypeToken<List<CuentaModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public ResultadoOperacion depositar(String numeroCuenta, double monto, int empleadoId, int ventanilla) {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", numeroCuenta);
        body.put("monto", monto);
        body.put("empleadoId", empleadoId);
        body.put("ventanilla", ventanilla);
        return gson.fromJson(enviarPost("/movimientos/deposito", gson.toJson(body)), ResultadoOperacion.class);
    }

    public ResultadoOperacion transferir(String orig, String dest, double monto, int empId, int vent) {
        Map<String, Object> body = new HashMap<>();
        body.put("cuentaOrigen", orig); body.put("cuentaDestino", dest);
        body.put("monto", monto); body.put("empleadoId", empId); body.put("ventanilla", vent);
        return gson.fromJson(enviarPost("/movimientos/transferencia", gson.toJson(body)), ResultadoOperacion.class);
    }

    private String enviarGet(String ruta) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(config.getEndpointUrl() + ruta)).header("Accept", "application/json").GET().build();
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        } catch (Exception e) { throw new RuntimeException("Error GET: " + e.getMessage(), e); }
    }

    private String enviarPost(String ruta, String json) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(config.getEndpointUrl() + ruta))
                    .header("Content-Type", "application/json; charset=UTF-8").header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        } catch (Exception e) { throw new RuntimeException("Error POST: " + e.getMessage(), e); }
    }
}
"""));

            list.Add(Heading3("4.2.4 Paso 4: Implementación del Controlador de Consola (AppControlador.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.clicon.controller se implementa AppControlador. Administra la sesión del operador autenticado y coordina la delegación de transacciones hacia EurekaBankService:"));

            list.Add(TableCaption(17, "Controlador Orquestador MVC del Cliente Consola AppControlador.java"));
            list.Add(CodeBox("""
package ec.edu.monster.clicon.controller;

import ec.edu.monster.clicon.model.*;
import java.util.List;

public class AppControlador {
    private final EurekaBankService servicio = new EurekaBankService();
    private UsuarioSesion sesionActual;
    private int ventanillaActiva = 1;

    public UsuarioSesion iniciarSesion(String usuario, String clave) {
        UsuarioSesion res = servicio.login(usuario, clave);
        if (res != null && res.isAutenticado()) {
            this.sesionActual = res;
            if (res.getVentanilla() >= 1 && res.getVentanilla() <= 4) this.ventanillaActiva = res.getVentanilla();
        }
        return res;
    }

    public void cerrarSesion() { this.sesionActual = null; }
    public UsuarioSesion getSesionActual() { return sesionActual; }
    public int getVentanillaActiva() { return ventanillaActiva; }
    public void setVentanillaActiva(int v) { if (v >= 1 && v <= 4) this.ventanillaActiva = v; }
    public List<CuentaModel> obtenerCuentasPorSucursal(int sucursalId) { return servicio.listarCuentasPorSucursal(sucursalId); }
    public ResultadoOperacion depositar(String cuenta, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.depositar(cuenta, monto, empId, ventanillaActiva);
    }
    public ResultadoOperacion transferir(String orig, String dest, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.transferir(orig, dest, monto, empId, ventanillaActiva);
    }
}
"""));

            list.Add(Heading3("4.2.5 Paso 5: Implementación de la Vista de Consola y Rutina Multi-Hilo de Concurrencia (ConsolaView.java)"));
            list.Add(Paragraph("En ec.edu.monster.clicon.view se implementa ConsolaView, responsable del bucle interactivo del menú y de la simulación multi-hilo de 3 ventanillas concurrentes utilizando ExecutorService:"));

            list.Add(TableCaption(18, "Interfaz de Consola Interactiva y Simulación de 3 Ventanillas Concurrentes ConsolaView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.clicon.view;

import ec.edu.monster.clicon.controller.AppControlador;
import ec.edu.monster.clicon.model.*;
import java.util.*;
import java.util.concurrent.*;

public class ConsolaView {
    private static final String ANSI_RESET = "\u001B[0m", ANSI_GREEN = "\u001B[32m", ANSI_RED = "\u001B[31m";
    private final AppControlador controlador;
    private final Scanner scanner = new Scanner(System.in);

    public ConsolaView(AppControlador controlador) { this.controlador = controlador; }

    public void iniciar() {
        System.out.println("====================================================================");
        System.out.println("       EUREKABANK - ESTACIÓN DE CONSOLA BANCARIA (RESTful JAVA)       ");
        System.out.println("                 GRUPO 04 - DOMINIO ec.edu.monster                 ");
        System.out.println("====================================================================");

        while (controlador.getSesionActual() == null) {
            System.out.print("\nIngrese Usuario Operador (ej. Monster): ");
            String usr = scanner.nextLine().trim();
            System.out.print("Ingrese Contraseña (ej. Monster9): ");
            String pwd = scanner.nextLine().trim();
            UsuarioSesion s = controlador.iniciarSesion(usr, pwd);
            if (s != null && s.isAutenticado()) {
                System.out.println("[OK] Bienvenido " + s.getNombreCompleto() + " | Rol: " + s.getRol());
            } else { System.out.println("[ERROR] Credenciales inválidas."); }
        }
        mostrarMenu();
    }

    private void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n----------------- MENU DE OPERACIÓNES BANCARIAS ------------------");
            System.out.println("1. Listar Cuentas y Semáforo MashUp (VERDE / ROJO)");
            System.out.println("4. Registrar Depósito Monetario (+)");
            System.out.println("6. Registrar Transferencia entre Cuentas (<=>)");
            System.out.println("8. Simular Escenario Crítico: 3 Ventanillas Simultáneas (MashUp)");
            System.out.println("0. Cerrar Sesión");
            System.out.print("Seleccione una opción: ");
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1" -> listarCuentas();
                case "4" -> registrarDeposito();
                case "8" -> simularConcurrenciaTresVentanillas("001-0001", 25.00);
                case "0" -> { controlador.cerrarSesion(); salir = true; }
            }
        }
    }

    private void simularConcurrenciaTresVentanillas(String cuenta, double monto) {
        System.out.println("\n>>> INICIANDO SIMULACIÓN CONCURRENTE (3 VENTANILLAS) SOBRE " + cuenta + " <<<");
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Callable<String>> tareas = List.of(
                () -> "[V1] " + controlador.depositar(cuenta, monto).getMensaje(),
                () -> "[V2] " + controlador.depositar(cuenta, monto).getMensaje(),
                () -> "[V3] " + controlador.depositar(cuenta, monto).getMensaje()
            );
            for (Future<String> f : pool.invokeAll(tareas)) System.out.println(f.get());
            System.out.println(">>> SIMULACIÓN CONCLUIDA CON EXCLUSIÓN MUTUA VERIFICADA <<<\n");
        } catch (Exception e) { System.out.println(ANSI_RED + "Error: " + e.getMessage() + ANSI_RESET); }
        finally { pool.shutdownNow(); }
    }

    private void listarCuentas() {
        List<CuentaModel> lista = controlador.obtenerCuentasPorSucursal(0);
        System.out.println("\n+----------+----------------------+-----------+-------------------------+");
        System.out.println("|  CUENTA  | TITULAR              | SALDO USD | SEMÁFORO MASHUP         |");
        System.out.println("+----------+----------------------+-----------+-------------------------+");
        for (CuentaModel c : lista) {
            boolean libre = "VERDE".equalsIgnoreCase(c.getDisponibilidadMashup());
            String tag = libre ? ANSI_GREEN + "[LIBRE] VERDE" + ANSI_RESET : ANSI_RED + "[OCUP] ROJO" + ANSI_RESET;
            System.out.printf("| %-8s | %-20s | %9.2f | %-23s |\n", c.getNumeroCuenta(), c.getNombreCliente(), c.getSaldo(), tag);
        }
        System.out.println("+----------+----------------------+-----------+-------------------------+");
    }

    private void registrarDeposito() {
        System.out.print("Cuenta destino: "); String c = scanner.nextLine().trim();
        System.out.print("Monto USD: "); double m = Double.parseDouble(scanner.nextLine().trim());
        ResultadoOperacion res = controlador.depositar(c, m);
        System.out.println(res.isExito() ? "[OK] " + res.getMensaje() : "[FALLO] " + res.getMensaje());
    }
}
"""));

            list.Add(FigureCaption(7, "Ejecución del Cliente Consola e Inicio de Sesión de Operador"));
            list.Add(ScreenshotBox(
                7,
                "Ejecución del Cliente Consola e Inicio de Sesión de Operador",
                "Consola de comandos / Terminal de NetBeans.",
                "Parámetros: Usuario = Monster | Contraseña = Monster9.",
                "Ejecutar la clase principal WSEurekaBankConsoleClient.",
                "Mensaje de bienvenida [OK] con el nombre del operador James P. Sullivan, rol Superadmin Monster y despliegue del menú de operaciones bancarias."
            ));

            list.Add(FigureCaption(8, "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola"));
            list.Add(ScreenshotBox(
                8,
                "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola",
                "Consola de comandos / Terminal de NetBeans.",
                "Opción 1: Listar Cuentas y Semáforo MashUp.",
                "Seleccionar opción 1 en el menú interactivo.",
                "Tabla tabulada con columnas CUENTA, TITULAR, SALDO y SEMÁFORO MASHUP mostrando los estados VERDE (Libre) y ROJO (Ocupado en Ventanilla)."
            ));

            list.Add(FigureCaption(9, "Simulación de 3 Ventanillas Simultáneas en Cliente Consola"));
            list.Add(ScreenshotBox(
                9,
                "Simulación de 3 Ventanillas Simultáneas en Cliente Consola",
                "Consola de comandos / Terminal de NetBeans.",
                "Opción 8: Simular Escenario Crítico: 3 Ventanillas Simultáneas sobre 001-0001.",
                "Seleccionar la opción 8 para disparar la rutina multi-hilo con ExecutorService.",
                "Trazas de consola mostrando Ventanilla 1 adquiriendo el semáforo (ROJO), Ventanilla 2 y 3 detectando la exclusión mutua CONCURRENCIA_BLOQUEADA (HTTP 409 Conflict), y la actualización final acumulada del saldo."
            ));

            // -------------------------------------------------------------------------
            // 4.3 CLIENTE ESCRITORIO
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.3 Cliente Escritorio (WSEurekaBankDesktopClient)"));
            list.Add(Paragraph("El Cliente de Escritorio está desarrollado en Java Swing con interfaz visual profesional, tablas personalizadas con semáforos cromáticos (verde/rojo), integración completa con el servicio RESTful y despacho asíncrono con SwingWorker para mantener la fluidez de la interfaz."));

            list.Add(Heading3("4.3.1 Paso 1: Creación del Proyecto de Escritorio y Dependencias Maven (HttpClient + Gson)"));
            list.Add(Paragraph("En NetBeans IDE se crea el proyecto Maven WSEurekaBankDesktopClient con GroupId ec.edu.monster. En el archivo pom.xml se incluye la librería Gson para consumir el servicio RESTful:"));

            list.Add(TableCaption(19, "Descriptor de Dependencias del Cliente de Escritorio pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankDesktopClient</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>WSEurekaBankDesktopClient_REST</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <exec.mainClass>ec.edu.monster.cliesc.WSEurekaBankDesktopClient</exec.mainClass>
    </properties>

    <dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>
</project>
"""));

            list.Add(Heading3("4.3.2 Paso 2: Estructuración Modular MVC Tradicional de Escritorio"));
            list.Add(Paragraph("Se estructuran los paquetes en ec.edu.monster.cliesc.model, ec.edu.monster.cliesc.controller y ec.edu.monster.cliesc.view:"));

            list.Add(TableCaption(20, "Estructura de Paquetes MVC del Cliente Escritorio"));
            list.Add(TechnicalTable(
                new[] { "Paquete", "Clases Principales", "Responsabilidad en el Cliente Escritorio" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.cliesc.view", "LoginView.java, DashboardView.java, SemaforoTableCellRenderer", "Formularios NetBeans GUI Matisse, renderizadores cromáticos y paneles de operaciones bancarias." },
                    new[] { "ec.edu.monster.cliesc.controller", "AppControlador.java", "Maneja la sesión del cajero, valida datos y orquesta peticiones hacia EurekaBankService." },
                    new[] { "ec.edu.monster.cliesc.model", "EurekaBankService.java, ConfiguracionServidor.java, CuentaModel.java, MovimientoModel.java, ResultadoOperacion.java, UsuarioSesion.java", "Consumidor HTTP/JSON RESTful con java.net.http.HttpClient y Gson." }
                }
            ));

            list.Add(Heading3("4.3.3 Paso 3: Implementación de la Vista de Autenticación (LoginView.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.cliesc.view se crea LoginView, permitiendo al operador autenticarse con las credenciales maestras Monster / Monster9 e iniciar en la ventanilla asignada:"));

            list.Add(TableCaption(21, "Interfaz Gráfica de Autenticación de Ventanilla LoginView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.UsuarioSesion;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class LoginView extends javax.swing.JFrame {
    private final AppControlador controlador;

    public LoginView(AppControlador controlador) {
        this.controlador = controlador;
        initComponents();
        setLocationRelativeTo(null);
    }

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();
        int ventanilla = cmbVentanilla.getSelectedIndex() + 1;

        btnIngresar.setEnabled(false);
        SwingWorker<UsuarioSesion, Void> worker = new SwingWorker<>() {
            @Override
            protected UsuarioSesion doInBackground() {
                UsuarioSesion sesion = controlador.iniciarSesion(usuario, clave);
                controlador.setVentanillaActiva(ventanilla);
                return sesion;
            }
            @Override
            protected void done() {
                btnIngresar.setEnabled(true);
                try {
                    UsuarioSesion s = get();
                    if (s != null && s.isAutenticado()) {
                        new DashboardView(controlador).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginView.this, "Credenciales inválidas.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginView.this, "Error de red: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }
}
"""));

            list.Add(FigureCaption(10, "Diseño e Interfaz Gráfica de Autenticación de Ventanilla (LoginView)"));
            list.Add(ScreenshotBox(
                10,
                "Diseño e Interfaz Gráfica de Autenticación de Ventanilla (LoginView)",
                "Aplicación de Escritorio Java Swing - Ventana LoginView.",
                "Usuario: Monster | Clave: Monster9 | Ventanilla: Ventanilla 1 | Endpoint: http://localhost:8080/WS_EurekaBank_Java_REST/api/.",
                "Ingresar las credenciales maestras y presionar el botón 'INGRESAR A ESTACIÓN BANCARIA'.",
                "Formulario con logotipo institucional de Sullivan, validación exitosa y transición automática hacia DashboardView."
            ));

            list.Add(Heading3("4.3.4 Paso 4: Implementación del Renderizador Cromático de Semáforo MashUp (SemaforoTableCellRenderer)"));
            list.Add(Paragraph("Se implementa SemaforoTableCellRenderer extendiendo DefaultTableCellRenderer para pintar las celdas de la tabla de cuentas en verde esmeralda (Libre) o rojo carmesí (Ocupado en Ventanilla):"));

            list.Add(TableCaption(22, "Renderizador Cromático de Semáforo MashUp en Tabla Swing SemaforoTableCellRenderer"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SemaforoTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        String estado = (String) table.getModel().getValueAt(row, 3);

        if (col == 3) {
            setHorizontalAlignment(SwingConstants.CENTER);
            if ("VERDE".equalsIgnoreCase(estado)) {
                c.setBackground(new Color(220, 252, 231));
                c.setForeground(new Color(22, 101, 52));
                setText("● VERDE (Libre)");
            } else {
                c.setBackground(new Color(254, 226, 226));
                c.setForeground(new Color(153, 27, 27));
                setText("● ROJO (Ocupado en Ventanilla)");
            }
        } else if (!isSelected) {
            c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            c.setForeground(new Color(15, 23, 42));
        }
        return c;
    }
}
"""));

            list.Add(Heading3("4.3.5 Paso 5: Implementación del Monitor MashUp y Dashboard Bancario Swing (DashboardView.java)"));
            list.Add(Paragraph("En DashboardView se ensamblan la tabla de cuentas con el semáforo visual reactivo, el panel de operaciones monetarias (depósito, retiro, transferencia) y el temporizador Timer que refresca el semáforo cada 2.0 segundos mediante SwingWorker:"));

            list.Add(TableCaption(23, "Monitor MashUp y Panel Transaccional Swing DashboardView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.AppControlador;
import ec.edu.monster.cliesc.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.util.List;

public class DashboardView extends javax.swing.JFrame {
    private final AppControlador controlador;
    private DefaultTableModel modeloCuentas;
    private Timer timerMashup;

    public DashboardView(AppControlador controlador) {
        this.controlador = controlador;
        initComponents();
        configurarTablaMashUp();
        iniciarTimerMashUp();
    }

    private void configurarTablaMashUp() {
        tablaCuentas.setDefaultRenderer(Object.class, new SemaforoTableCellRenderer());
        tablaCuentas.setRowHeight(28);
    }

    private void iniciarTimerMashUp() {
        timerMashup = new Timer(2000, e -> refrescarMashupAsync());
        timerMashup.start();
        refrescarMashupAsync();
    }

    private void refrescarMashupAsync() {
        SwingWorker<List<CuentaModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<CuentaModel> doInBackground() {
                return controlador.obtenerCuentasPorSucursal(0);
            }
            @Override
            protected void done() {
                try {
                    List<CuentaModel> cuentas = get();
                    modeloCuentas.setRowCount(0);
                    for (CuentaModel c : cuentas) {
                        modeloCuentas.addRow(new Object[]{
                            c.getNumeroCuenta(), c.getNombreCliente(), c.getSaldo(), c.getDisponibilidadMashup()
                        });
                    }
                } catch (Exception ignored) {}
            }
        };
        worker.execute();
    }
}
"""));

            list.Add(FigureCaption(11, "Panel Principal del Dashboard Bancario con Semáforo MashUp en Tiempo Real"));
            list.Add(ScreenshotBox(
                11,
                "Panel Principal del Dashboard Bancario con Semáforo MashUp en Tiempo Real",
                "Aplicación de Escritorio Java Swing - Ventana DashboardView.",
                "Sesión activa: Monster (James P. Sullivan) | Ventanilla: 1 | Dominio: ec.edu.monster.",
                "Visualizar la tabla de cuentas bancarias en reposo.",
                "Todas las cuentas en estado VERDE (Libre), saldos actualizados en USD y formulario transaccional habilitado."
            ));

            list.Add(Heading3("4.3.6 Paso 6: Implementación del Controlador de Escritorio y Despacho Asíncrono (AppControlador.java)"));
            list.Add(Paragraph("En AppControlador se canalizan los métodos de depósito, retiro y transferencia hacia EurekaBankService, garantizando que ninguna operación transaccional se ejecute en el hilo de despacho de eventos (EDT):"));

            list.Add(TableCaption(24, "Controlador Orquestador MVC del Cliente Escritorio AppControlador.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.controller;

import ec.edu.monster.cliesc.model.*;
import java.util.List;

public class AppControlador {
    private final EurekaBankService servicio = new EurekaBankService();
    private UsuarioSesion sesionActual;
    private int ventanillaActiva = 1;

    public UsuarioSesion iniciarSesion(String usuario, String clave) {
        UsuarioSesion res = servicio.login(usuario, clave);
        if (res != null && res.isAutenticado()) {
            this.sesionActual = res;
            if (res.getVentanilla() >= 1 && res.getVentanilla() <= 4) this.ventanillaActiva = res.getVentanilla();
        }
        return res;
    }

    public void cerrarSesion() { this.sesionActual = null; }
    public UsuarioSesion getSesionActual() { return sesionActual; }
    public int getVentanillaActiva() { return ventanillaActiva; }
    public void setVentanillaActiva(int v) { if (v >= 1 && v <= 4) this.ventanillaActiva = v; }
    public List<CuentaModel> obtenerCuentasPorSucursal(int sucursalId) { return servicio.listarCuentasPorSucursal(sucursalId); }

    public ResultadoOperacion depositar(String cuenta, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.depositar(cuenta, monto, empId, ventanillaActiva);
    }

    public ResultadoOperacion transferir(String orig, String dest, double monto) {
        int empId = sesionActual != null ? sesionActual.getIdEmpleado() : 1;
        return servicio.transferir(orig, dest, monto, empId, ventanillaActiva);
    }
}
"""));

            list.Add(FigureCaption(12, "Ejecución de Operación Transaccional de Depósito en Cliente Escritorio"));
            list.Add(ScreenshotBox(
                12,
                "Ejecución de Operación Transaccional de Depósito en Cliente Escritorio",
                "Aplicación de Escritorio Java Swing - Ventana DashboardView.",
                "Cuenta: 001-0001 | Monto: 50.00 USD | Tipo: Depósito.",
                "Seleccionar cuenta 001-0001, ingresar $50.00 y pulsar el botón 'ACREDITAR DEPÓSITO (+)'.",
                "Mensaje de confirmación en pantalla, incremento del saldo a $2800.00 y registro automático de la nueva fila en el Extracto Cronológico de Movimientos."
            ));

            list.Add(FigureCaption(13, "Ejecución de Operación de Transferencia entre Cuentas con Verificación ACID"));
            list.Add(ScreenshotBox(
                13,
                "Ejecución de Operación de Transferencia entre Cuentas con Verificación ACID",
                "Aplicación de Escritorio Java Swing - Ventana DashboardView.",
                "Cuenta Origen: 001-0001 | Cuenta Destino: 001-0002 | Monto: 100.00 USD.",
                "Ingresar los datos y presionar el botón 'EJECUTAR TRANSFERENCIA (<=>)'.",
                "Diálogo de éxito confirmando el débito de $100.00 en 001-0001 y crédito correspondiente en 001-0002 bajo transacción atómica indivisible."
            ));

            // -------------------------------------------------------------------------
            // 4.4 CLIENTE MÓVIL
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.4 Cliente Móvil (WSEurekaBankMobileClient)"));
            list.Add(Paragraph("El Cliente Móvil está desarrollado para la plataforma Android nativa en Android Studio bajo el paquete institucional ec.edu.monster.climov. Consume la API RESTful central mediante peticiones HTTP/JSON y despachos en segundo plano para garantizar una interfaz táctil fluida con monitoreo reactivo del Semáforo MashUp."));

            list.Add(Heading3("4.4.1 Paso 1: Creación del Proyecto en Android Studio y Configuración de Gradle"));
            list.Add(Paragraph("En Android Studio se selecciona New Project > Empty Views Activity con el nombre WSEurekaBankMobileClient y Package name ec.edu.monster.climov. En el archivo build.gradle (Module: app) se configuran compileSdk 34, minSdk 24, targetSdk 34 y compatibilidad con Java 17:"));

            list.Add(TableCaption(25, "Configuración de Compilación y SDKs del Cliente Móvil build.gradle"));
            list.Add(CodeBox("""
plugins {
    id 'com.android.application'
}

android {
    namespace 'ec.edu.monster.climov'
    compileSdk 34

    defaultConfig {
        applicationId "ec.edu.monster.climov.rest"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
}
"""));

            list.Add(Heading3("4.4.2 Paso 2: Configuración del Manifiesto de Android y Tráfico en Texto Plano (AndroidManifest.xml)"));
            list.Add(Paragraph("En AndroidManifest.xml se declaran los permisos de red (INTERNET y ACCESS_NETWORK_STATE) y la directiva usesCleartextTraffic=\"true\" para permitir la comunicación HTTP local con Payara Server en entornos de emulación (utilizando la IP 10.0.2.2 como alias del host):"));

            list.Add(TableCaption(26, "Descriptor de Seguridad y Permisos de Red AndroidManifest.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.WSEurekaBankMobileClient"
        android:usesCleartextTraffic="true">

        <activity
            android:name=".view.LoginView"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".view.DashboardView"
            android:exported="false" />

    </application>
</manifest>
"""));

            list.Add(Heading3("4.4.3 Paso 3: Estructuración Modular MVC en Android Studio (ec.edu.monster.climov)"));
            list.Add(Paragraph("Se estructuran los paquetes en ec.edu.monster.climov separando modelo, vista y controlador:"));

            list.Add(TableCaption(27, "Estructura de Paquetes y Recursos del Cliente Móvil en Android Studio"));
            list.Add(TechnicalTable(
                new[] { "Paquete / Carpeta", "Artefactos Principales", "Responsabilidad en la Plataforma Android" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.climov.view", "LoginView.java, DashboardView.java", "Actividades Android derivadas de AppCompatActivity, captura de eventos táctiles y pintado de semáforos." },
                    new[] { "ec.edu.monster.climov.controller", "AppControlador.java", "Controlador Singleton que administra la sesión del cajero y orquesta peticiones hacia EurekaBankService." },
                    new[] { "ec.edu.monster.climov.model", "EurekaBankService.java, ConfiguracionConexion.java, CuentaModel.java, MovimientoModel.java, ResultadoOperacion.java, UsuarioSesion.java", "Consumidor HTTP nativo con HttpURLConnection y parseo JSON con org.json." },
                    new[] { "res/layout", "activity_login.xml, activity_dashboard.xml", "Diseños visuales basados en ScrollView, LinearLayout y tarjetas de Material Design." }
                }
            ));

            list.Add(FigureCaption(14, "Estructura del Proyecto y Layouts XML en Android Studio"));
            list.Add(ScreenshotBox(
                14,
                "Estructura del Proyecto y Layouts XML en Android Studio",
                "Android Studio - Vista Android / Project.",
                "Estructura de paquetes: ec.edu.monster.climov.model, view y controller.",
                "Explorar el directorio app/src/main/res/layout con activity_login.xml y activity_dashboard.xml.",
                "Proyecto compilando con Gradle Sync exitoso y layouts visualizados en el diseñador de Material Design."
            ));

            list.Add(Heading3("4.4.4 Paso 4: Implementación de Interfaces Visuales XML y Componentes de Material Design"));
            list.Add(Paragraph("Se crean los archivos de interfaz XML activity_login.xml y activity_dashboard.xml con componentes de Material Design:"));

            list.Add(TableCaption(28, "Layout XML de la Pantalla de Autenticación activity_login.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#0F172A"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:orientation="vertical"
        android:padding="22dp">

        <ImageView
            android:layout_width="96dp"
            android:layout_height="96dp"
            android:contentDescription="Monster Sullivan"
            android:src="@drawable/sullivan" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="EUREKABANK BANCA MÓVIL"
            android:textColor="#38BDF8"
            android:textSize="20sp"
            android:textStyle="bold" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="18dp"
            android:text="Grupo 04 | ec.edu.monster | MVC Android"
            android:textColor="#94A3B8"
            android:textSize="12sp" />

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#FFFFFF"
            android:orientation="vertical"
            android:padding="20dp">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Iniciar Sesión de Operador"
                android:textColor="#0F172A"
                android:textSize="16sp"
                android:textStyle="bold" />

            <EditText
                android:id="@+id/etHost"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Host IP (ej. 10.0.2.2)"
                android:text="10.0.2.2" />

            <EditText
                android:id="@+id/etUsuario"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Usuario (Monster)"
                android:text="Monster" />

            <EditText
                android:id="@+id/etClave"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="Contraseña (Monster9)"
                android:inputType="textPassword"
                android:text="Monster9" />

            <Button
                android:id="@+id/btnIngresar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="14dp"
                android:backgroundTint="#0284C7"
                android:text="INGRESAR A VENTANILLA" />
        </LinearLayout>
    </LinearLayout>
</ScrollView>
"""));

            list.Add(TableCaption(29, "Layout XML del Dashboard de Cuentas y Semáforos activity_dashboard.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#F1F5F9"
    android:fillViewport="true">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="14dp">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#0F172A"
            android:orientation="vertical"
            android:padding="14dp">

            <TextView
                android:id="@+id/tvUsuarioHeader"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="EUREKABANK MÓVIL (GR04)"
                android:textColor="#38BDF8"
                android:textSize="16sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tvRolHeader"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Monitor Semáforo MashUp en Tiempo Real (2.5s)"
                android:textColor="#CBD5E1"
                android:textSize="12sp" />
        </LinearLayout>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/tvKpiVerde"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:background="#DCFCE7"
                android:padding="10dp"
                android:text="VERDE (Libres): 0"
                android:textColor="#166534"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/tvKpiRojo"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_marginStart="8dp"
                android:layout_weight="1"
                android:background="#FEE2E2"
                android:padding="10dp"
                android:text="ROJO (Ocupadas): 0"
                android:textColor="#991B1B"
                android:textStyle="bold" />
        </LinearLayout>

        <LinearLayout
            android:id="@+id/contenedorCuentas"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:orientation="vertical" />
    </LinearLayout>
</ScrollView>
"""));

            list.Add(Heading3("4.4.5 Paso 5: Implementación del Servicio Consumidor RESTful HTTP/JSON (EurekaBankService.java)"));
            list.Add(Paragraph("En ec.edu.monster.climov.model se implementa EurekaBankService, utilizando HttpURLConnection y las clases nativas org.json.JSONObject y JSONArray para comunicarse con la API RESTful:"));

            list.Add(TableCaption(30, "Consumidor HTTP/JSON Nativo Android EurekaBankService.java (Android)"));
            list.Add(CodeBox("""
package ec.edu.monster.climov.model;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

public class EurekaBankService {
    public String getEndpointActual() { return ConfiguracionConexion.getRestBaseUrl(); }

    public UsuarioSesion login(String usuario, String clave) {
        try {
            JSONObject req = new JSONObject();
            req.put("usuario", usuario); req.put("clave", clave);
            JSONObject obj = new JSONObject(enviarPost("/auth/login", req.toString()));
            UsuarioSesion s = new UsuarioSesion();
            s.setAutenticado(obj.optBoolean("autenticado", false));
            s.setMensaje(obj.optString("mensaje", ""));
            s.setNombreCompleto(obj.optString("nombreCompleto", ""));
            s.setRol(obj.optString("rol", ""));
            s.setVentanilla(obj.optInt("ventanilla", 1));
            return s;
        } catch (Exception e) {
            UsuarioSesion err = new UsuarioSesion();
            err.setAutenticado(false); err.setMensaje(e.getMessage()); return err;
        }
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        try {
            String jsonResp = enviarGet("/cuentas?sucursal=" + sucursalId);
            JSONArray arr = new JSONArray(jsonResp);
            List<CuentaModel> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                CuentaModel c = new CuentaModel();
                c.setNumeroCuenta(o.optString("numeroCuenta"));
                c.setMoneda(o.optString("moneda", "USD"));
                c.setSaldo(o.optDouble("saldo", 0.0));
                c.setDisponibilidadMashup(o.optString("disponibilidadMashup", "VERDE"));
                c.setNombreCliente(o.optString("nombreCliente", ""));
                c.setApellidoCliente(o.optString("apellidoCliente", ""));
                lista.add(c);
            }
            return lista;
        } catch (Exception e) { return new ArrayList<>(); }
    }

    private String enviarGet(String path) throws Exception {
        URL url = new URL(getEndpointActual() + path);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("GET"); c.setRequestProperty("Accept", "application/json"); c.setConnectTimeout(8000);
        try (InputStream in = (c.getResponseCode() >= 400 ? c.getErrorStream() : c.getInputStream());
             Scanner s = new Scanner(in, StandardCharsets.UTF_8.name())) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

    private String enviarPost(String path, String jsonBody) throws Exception {
        URL url = new URL(getEndpointActual() + path);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST"); c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        c.setRequestProperty("Accept", "application/json"); c.setDoOutput(true); c.setConnectTimeout(10000);
        try (OutputStream os = c.getOutputStream()) { os.write(jsonBody.getBytes(StandardCharsets.UTF_8)); }
        try (InputStream in = (c.getResponseCode() >= 400 ? c.getErrorStream() : c.getInputStream());
             Scanner s = new Scanner(in, StandardCharsets.UTF_8.name())) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }
}
"""));

            list.Add(Heading3("4.4.6 Paso 6: Implementación del Controlador y Actividades Visuales (LoginView.java y DashboardView.java)"));
            list.Add(Paragraph("En DashboardView se programa la recarga automática del semáforo MashUp cada 2.5 segundos mediante un Handler sincronizado con el hilo principal (Looper.getMainLooper()) y tareas de fondo con ExecutorService:"));

            list.Add(TableCaption(31, "Controlador y Actividad Principal Móvil con Sincronización MashUp (2.5s) DashboardView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.climov.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import ec.edu.monster.climov.R;
import ec.edu.monster.climov.controller.AppControlador;
import ec.edu.monster.climov.model.*;
import java.util.List;
import java.util.concurrent.*;

public class DashboardView extends AppCompatActivity {
    private final AppControlador controlador = AppControlador.getInstancia();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Handler mashupHandler = new Handler(Looper.getMainLooper());
    private LinearLayout contenedorCuentas;
    private TextView tvKpiVerde, tvKpiRojo;
    private boolean activo = false;

    private final Runnable tareaPolling = new Runnable() {
        @Override
        public void run() {
            if (!activo) return;
            sincronizarCuentasMashup();
            mashupHandler.postDelayed(this, 2500); // Polling cada 2.5s
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        contenedorCuentas = findViewById(R.id.contenedorCuentas);
        tvKpiVerde = findViewById(R.id.tvKpiVerde);
        tvKpiRojo = findViewById(R.id.tvKpiRojo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activo = true;
        mashupHandler.post(tareaPolling);
    }

    @Override
    protected void onPause() {
        super.onPause();
        activo = false;
        mashupHandler.removeCallbacks(tareaPolling);
    }

    private void sincronizarCuentasMashup() {
        executor.execute(() -> {
            List<CuentaModel> cuentas = controlador.listarCuentasPorSucursal(0);
            mashupHandler.post(() -> renderizarCuentas(cuentas));
        });
    }

    private void renderizarCuentas(List<CuentaModel> cuentas) {
        int verdes = 0, rojos = 0;
        contenedorCuentas.removeAllViews();
        for (CuentaModel c : cuentas) {
            boolean libre = "VERDE".equalsIgnoreCase(c.getDisponibilidadMashup());
            if (libre) verdes++; else rojos++;
            TextView tv = new TextView(this);
            tv.setText(c.getNumeroCuenta() + " - " + c.getNombreCliente() + " | $" + c.getSaldo() + " | " + (libre ? "VERDE" : "ROJO"));
            contenedorCuentas.addView(tv);
        }
        tvKpiVerde.setText("VERDE (Libres): " + verdes);
        tvKpiRojo.setText("ROJO (Ocupadas): " + rojos);
    }
}
"""));

            list.Add(FigureCaption(15, "Ejecución del Cliente Móvil en Emulador Android (Login y Selección de Ventanilla)"));
            list.Add(ScreenshotBox(
                15,
                "Ejecución del Cliente Móvil en Emulador Android (Login y Selección de Ventanilla)",
                "Android Virtual Device (AVD) Pixel 7 Pro / NetBeans Android Plugin.",
                "Host: 10.0.2.2 | Puerto: 8080 | Usuario: Monster | Clave: Monster9.",
                "Iniciar la aplicación WSEurekaBankMobileClient y pulsar 'INGRESAR A VENTANILLA'.",
                "Formulario de autenticación móvil con validación exitosa y transición hacia la vista de monitoreo DashboardView."
            ));

            list.Add(FigureCaption(16, "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil"));
            list.Add(ScreenshotBox(
                16,
                "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil",
                "Android Virtual Device (AVD) Pixel 7 Pro.",
                "Vista principal DashboardView con auto-refresco del Semáforo MashUp activo.",
                "Observar las tarjetas de cuentas con indicadores de color verde esmeralda y rojo carmesí.",
                "Pantalla mostrando contadores KPI (VERDE: 4, ROJO: 0), listado completo de saldos bancarios y formulario para registrar depósitos y transferencias."
            ));

            // -------------------------------------------------------------------------
            // 4.5 CLIENTE WEB
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.5 Cliente Web (WSEurekaBankWebClient)"));
            list.Add(Paragraph("El Cliente Web está desarrollado sobre la especificación Jakarta Faces (JSF 4.0) utilizando componentes Facelets y Backing Beans reactivos en Payara Server. Incorpora sondeo asíncrono AJAX periódico (polling a 2.5 segundos) para refrescar dinámicamente los semáforos de las cuentas bancarias sin recargar la página."));

            list.Add(Heading3("4.5.1 Paso 1: Creación del Proyecto Web Maven y Descriptores de Despliegue (pom.xml y web.xml)"));
            list.Add(Paragraph("En NetBeans IDE se crea el proyecto Web Maven WSEurekaBankWebClient. En pom.xml se configuran las dependencias de Jakarta EE Web API 10.0.0 y Gson 2.10.1:"));

            list.Add(TableCaption(32, "Descriptor de Dependencias de Jakarta Faces 4.0 pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankWebClient</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    <name>WSEurekaBankWebClient_REST</name>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>10.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>

    <build>
        <finalName>WSEurekaBankWebClient_REST</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.12.1</version>
                <configuration>
                    <release>17</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
"""));

            list.Add(Paragraph("En web.xml se declara el FacesServlet y el mapeo de extensiones .xhtml:"));

            list.Add(TableCaption(33, "Descriptor de Despliegue de Servlets web.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">
    <servlet>
        <servlet-name>Faces Servlet</servlet-name>
        <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>Faces Servlet</servlet-name>
        <url-pattern>*.xhtml</url-pattern>
    </servlet-mapping>
    <welcome-file-list>
        <welcome-file>login.xhtml</welcome-file>
    </welcome-file-list>
</web-app>
"""));

            list.Add(Heading3("4.5.2 Paso 2: Estructuración Modular MVC en Jakarta Faces (JSF)"));
            list.Add(Paragraph("Se estructuran los paquetes en ec.edu.monster.cliweb.view, ec.edu.monster.cliweb.controller y ec.edu.monster.cliweb.model:"));

            list.Add(TableCaption(34, "Estructura de Paquetes MVC del Cliente Web Jakarta Faces"));
            list.Add(TechnicalTable(
                new[] { "Paquete / Carpeta", "Artefactos Principales", "Responsabilidad Arquitectónica" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.cliweb.view", "LoginView.java, DashboardView.java", "Backing Beans CDI (@Named, @SessionScoped) que gestionan el estado visual y la sincronización MashUp." },
                    new[] { "ec.edu.monster.cliweb.controller", "AppControlador.java", "Controlador orquestador que canaliza las peticiones de los Backing Beans hacia EurekaBankService." },
                    new[] { "ec.edu.monster.cliweb.model", "EurekaBankService.java, ConfiguracionServidor.java, CuentaModel.java, MovimientoModel.java, ResultadoOperacion.java", "Consumidor HTTP nativo con java.net.http.HttpClient y Gson para invocar la API RESTful." },
                    new[] { "src/main/webapp", "login.xhtml, dashboard.xhtml", "Plantillas Facelets XHTML reactivas con componentes estándar JSF (<h:dataTable>, <f:ajax>) y CSS personalizado." }
                }
            ));

            list.Add(Heading3("4.5.3 Paso 3: Implementación del Consumidor RESTful y Controlador Web (EurekaBankService.java y AppControlador.java)"));
            list.Add(Paragraph("En ec.edu.monster.cliweb.model se implementa EurekaBankService utilizando java.net.http.HttpClient para despachar peticiones HTTP/JSON hacia Payara Server:"));

            list.Add(TableCaption(35, "Consumidor RESTful y Orquestador MVC del Cliente Web (EurekaBankService.java y AppControlador.java)"));
            list.Add(CodeBox("""
package ec.edu.monster.cliweb.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

public class EurekaBankService implements java.io.Serializable {
    private final ConfiguracionServidor config = new ConfiguracionServidor();
    private final transient HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final transient Gson gson = new Gson();

    public UsuarioSesion login(String usuario, String clave) {
        Map<String, Object> body = new HashMap<>();
        body.put("usuario", usuario); body.put("clave", clave);
        String jsonResp = enviarPost("/auth/login", gson.toJson(body));
        return gson.fromJson(jsonResp, UsuarioSesion.class);
    }

    public List<CuentaModel> listarCuentasPorSucursal(int sucursalId) {
        String jsonResp = enviarGet("/cuentas?sucursal=" + sucursalId);
        List<CuentaModel> list = gson.fromJson(jsonResp, new TypeToken<List<CuentaModel>>() {}.getType());
        return list != null ? list : new ArrayList<>();
    }

    public ResultadoOperacion depositar(String cuenta, double monto, int empId, int vent) {
        Map<String, Object> body = new HashMap<>();
        body.put("numeroCuenta", cuenta); body.put("monto", monto); body.put("empleadoId", empId); body.put("ventanilla", vent);
        return gson.fromJson(enviarPost("/movimientos/deposito", gson.toJson(body)), ResultadoOperacion.class);
    }

    private String enviarGet(String ruta) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(config.getEndpointUrl() + ruta)).header("Accept", "application/json").GET().build();
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        } catch (Exception e) { throw new RuntimeException("Error GET: " + e.getMessage(), e); }
    }

    private String enviarPost(String ruta, String json) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(config.getEndpointUrl() + ruta))
                    .header("Content-Type", "application/json; charset=UTF-8").header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8)).build();
            return httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).body();
        } catch (Exception e) { throw new RuntimeException("Error POST: " + e.getMessage(), e); }
    }
}
"""));

            list.Add(Heading3("4.5.4 Paso 4: Implementación de las Vistas Gestionadas Backing Beans (LoginView.java y DashboardView.java)"));
            list.Add(Paragraph("En ec.edu.monster.cliweb.view se implementan los Backing Beans LoginView y DashboardView con anotaciones CDI @Named y @SessionScoped, incorporando la simulación concurrente multihilo de 3 ventanillas simultáneas para verificación en tiempo real del Semáforo MashUp:"));

            list.Add(TableCaption(36, "Backing Bean del Monitor MashUp y Operaciones Web DashboardView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliweb.view;

import ec.edu.monster.cliweb.controller.AppControlador;
import ec.edu.monster.cliweb.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Named("dashboardView")
@SessionScoped
public class DashboardView implements Serializable {
    @Inject private LoginView loginView;
    private int sucursalSeleccionada = 0;
    private String cuentaSeleccionada = "001-0001";
    private String cuentaDestino = "001-0002";
    private double montoOperacion = 50.00;
    private String mensajeAlerta = "";
    private boolean exitoAlerta = true;
    private List<CuentaModel> cuentas = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (loginView != null && loginView.isAutenticado()) cargarDatosIniciales();
    }

    public void cargarDatosIniciales() {
        AppControlador ctrl = loginView.getControlador();
        this.cuentas = ctrl.obtenerCuentasPorSucursal(sucursalSeleccionada);
    }

    public void sincronizarMashup() {
        if (loginView == null || !loginView.isAutenticado()) return;
        AppControlador ctrl = loginView.getControlador();
        this.cuentas = ctrl.obtenerCuentasPorSucursal(sucursalSeleccionada);
    }

    public void ejecutarDeposito() {
        procesar(loginView.getControlador().depositar(cuentaSeleccionada, montoOperacion));
    }

    public void ejecutarRetiro() {
        procesar(loginView.getControlador().retirar(cuentaSeleccionada, montoOperacion));
    }

    public void ejecutarTransferencia() {
        procesar(loginView.getControlador().transferir(cuentaSeleccionada, cuentaDestino, montoOperacion));
    }

    public void simularConcurrenciaTresVentanillas() {
        if (loginView == null || !loginView.isAutenticado()) return;
        final String cta = (cuentaSeleccionada != null && !cuentaSeleccionada.isBlank()) ? cuentaSeleccionada.trim() : "001-0001";
        AppControlador ctrl = loginView.getControlador();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            List<Future<String>> futuros = new ArrayList<>();
            for (int v = 1; v <= 3; v++) {
                final int vent = v;
                futuros.add(pool.submit(() -> {
                    if (vent > 1) Thread.sleep(220L * vent);
                    ResultadoOperacion r = ctrl.depositarDesdeVentanilla(cta, 20.00, vent);
                    return "[Ventanilla " + vent + "] " + (r.isExito() ? "ÉXITO: " : "BLOQUEADA: ") + r.getMensaje();
                }));
            }
            StringBuilder resumen = new StringBuilder("Escenario Crítico 3 Ventanillas sobre " + cta + ":\n");
            for (Future<String> f : futuros) resumen.append(f.get()).append("\n");
            this.mensajeAlerta = resumen.toString();
            this.exitoAlerta = true;
        } catch (Exception ex) {
            this.mensajeAlerta = "Error en simulación: " + ex.getMessage();
            this.exitoAlerta = false;
        } finally {
            pool.shutdownNow();
        }
        sincronizarMashup();
    }

    private void procesar(ResultadoOperacion res) {
        if (res != null) {
            this.exitoAlerta = res.isExito();
            this.mensajeAlerta = res.getMensaje();
        }
        sincronizarMashup();
    }
}
"""));

            list.Add(Heading3("4.5.5 Paso 5: Implementación de las Plantillas Facelets Reactivas con Polling AJAX (login.xhtml y dashboard.xhtml)"));
            list.Add(Paragraph("En dashboard.xhtml se implementa la tabla reactiva con <h:dataTable>, retroalimentación visual inmediata con animación cromática roja pulsante mediante la función JavaScript marcarEnRojo() ejecutada en el evento onclick de cada transacción, y un temporizador periódico de sondeo (setInterval cada 1200ms) que dispara el comando asíncrono <f:ajax> para sincronizar el estado del Semáforo MashUp en tiempo real:"));

            list.Add(TableCaption(37, "Plantilla Facelets del Monitor MashUp Reactivo dashboard.xhtml"));
            list.Add(CodeBox("""
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="jakarta.faces.html"
      xmlns:f="jakarta.faces.core">
<h:head>
    <meta charset="UTF-8"/>
    <title>Monitor MashUp &amp; Operaciones - EurekaBank Web (GR04)</title>
    <style>
        .badge-verde { background: #dcfce7; color: #166534; padding: 4px 10px; border-radius: 999px; font-weight: 700; }
        .badge-rojo { background: #fee2e2; color: #991b1b; padding: 4px 10px; border-radius: 999px; font-weight: 700; }
        @keyframes pulse-red { 0%, 100% { transform: scale(1); opacity: 1; } 50% { transform: scale(1.05); opacity: 0.85; } }
        .pulse-rojo { animation: pulse-red 0.9s infinite ease-in-out; box-shadow: 0 0 10px rgba(220, 38, 38, 0.45); }
    </style>
</h:head>
<h:body>
    <h:form id="topForm">
        <h:selectOneMenu id="selVentanilla" value="#{loginView.ventanilla}">
            <f:selectItem itemValue="1" itemLabel="Ventanilla 1" />
            <f:selectItem itemValue="2" itemLabel="Ventanilla 2" />
            <f:selectItem itemValue="3" itemLabel="Ventanilla 3" />
        </h:selectOneMenu>
    </h:form>

    <h:form id="formMashup">
        <h:panelGroup id="panelMashup" layout="block">
            <h:dataTable value="#{dashboardView.cuentas}" var="c" styleClass="bank-table">
                <h:column><f:facet name="header">Cuenta</f:facet>#{c.numeroCuenta}</h:column>
                <h:column><f:facet name="header">Titular</f:facet>#{c.titularCompleto}</h:column>
                <h:column><f:facet name="header">Saldo USD</f:facet>$#{c.saldo}</h:column>
                <h:column>
                    <f:facet name="header">Semáforo MashUp</f:facet>
                    <h:panelGroup layout="block" styleClass="#{c.disponibilidadMashup eq 'VERDE' ? 'badge-verde' : 'badge-rojo'}">
                        #{c.disponibilidadMashup eq 'VERDE' ? '● VERDE (Libre)' : '● ROJO (Ocupado en Ventanilla)'}
                    </h:panelGroup>
                </h:column>
            </h:dataTable>
            <!-- Botón oculto disparado automáticamente cada 1.2s para sincronización MashUp en vivo -->
            <h:commandButton id="btnPollMashup" value="poll" action="#{dashboardView.sincronizarMashup}" style="display:none;">
                <f:ajax execute="@this" render="panelMashup" />
            </h:commandButton>
        </h:panelGroup>
    </h:form>

    <h:form id="formOperaciones">
        <h:inputText id="txtCuentaOrigen" value="#{dashboardView.cuentaSeleccionada}" />
        <h:inputText id="txtCuentaDestino" value="#{dashboardView.cuentaDestino}" />
        <h:inputText id="txtMonto" value="#{dashboardView.montoOperacion}" />
        <h:commandButton value="Registrar Depósito (+)" action="#{dashboardView.ejecutarDeposito}" onclick="marcarEnRojo(false);">
            <f:ajax execute="@form" render="@form :formMashup:panelMashup :formExtracto" />
        </h:commandButton>
        <h:commandButton value="Simular 3 Ventanillas Simultáneas" action="#{dashboardView.simularConcurrenciaTresVentanillas}" onclick="marcarEnRojo(false);">
            <f:ajax execute="@form" render="@form :formMashup:panelMashup :formExtracto" />
        </h:commandButton>
    </h:form>

    <script type="text/javascript">
        function marcarEnRojo(esTransferencia) {
            try {
                var ctaOrig = document.getElementById('formOperaciones:txtCuentaOrigen').value.trim();
                var rows = document.querySelectorAll('#formMashup table tbody tr');
                rows.forEach(function(r) {
                    var tds = r.querySelectorAll('td');
                    if (tds.length >= 6 &amp;&amp; tds[0].innerText.trim() === ctaOrig) {
                        tds[5].innerHTML = '&lt;span class="badge-rojo pulse-rojo"&gt;● ROJO (OCUPADA)&lt;/span&gt;';
                    }
                });
            } catch (e) {}
        }
        setInterval(function () {
            var btn = document.getElementById('formMashup:btnPollMashup');
            if (btn) btn.click();
        }, 1200);
    </script>
</h:body>
</html>
"""));

            list.Add(FigureCaption(17, "Interfaz Web de Inicio de Sesión de Ventanilla (login.xhtml)"));
            list.Add(ScreenshotBox(
                17,
                "Interfaz Web de Inicio de Sesión de Ventanilla (login.xhtml)",
                "Navegador Web (Google Chrome / Mozilla Firefox).",
                "URL: http://localhost:8080/WSEurekaBankWebClient/login.xhtml.",
                "Ingresar las credenciales maestras: Usuario = Monster | Contraseña = Monster9.",
                "Pantalla de autenticación con tarjeta visual oscura, validación exitosa y redirección a dashboard.xhtml."
            ));

            list.Add(FigureCaption(18, "Monitor MashUp y Banca Web en Tiempo Real (dashboard.xhtml)"));
            list.Add(ScreenshotBox(
                18,
                "Monitor MashUp y Banca Web en Tiempo Real (dashboard.xhtml)",
                "Navegador Web (Google Chrome / Mozilla Firefox).",
                "URL: http://localhost:8080/WSEurekaBankWebClient/dashboard.xhtml.",
                "Visualizar la tabla de cuentas y el estado de los semáforos en reposo.",
                "Tabla reactiva de cuentas bancarias mostrando todas las insignias en estado VERDE (Libre), con saldos en USD y panel de operaciones transaccionales activo."
            ));

            list.Add(FigureCaption(19, "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web"));
            list.Add(ScreenshotBox(
                19,
                "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web",
                "Navegador Web (Google Chrome / Mozilla Firefox).",
                "Operación simultánea: Disparar un depósito en el Cliente Escritorio (Ventanilla 1) sobre 001-0001.",
                "Observar el reflejo automático sin recarga de página en la vista web de dashboard.xhtml.",
                "La fila de la cuenta 001-0001 cambia inmediatamente a insignia ROJA con el texto 'ROJO (Ocupado en Ventanilla 1 por Monster Admin)' durante los 3.5 segundos de la transacción, retornando a VERDE al finalizar."
            ));

            // -------------------------------------------------------------------------
            // 4.6 PRUEBAS Y VERIFICACIÓN TRANSACCIÓNAL
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.6 Matriz de Casos de Prueba Transaccionales y de Concurrencia"));
            list.Add(Paragraph("Se diseñó y ejecutó una batería de casos de prueba formales para comprobar la robustez de las transacciones ACID, la autenticación y la exclusión mutua coordinada por el Semáforo MashUp sobre los endpoints RESTful:"));

            list.Add(TableCaption(38, "Matriz de Casos de Prueba Transaccionales y de Concurrencia MashUp"));
            list.Add(TechnicalTable(
                new[] { "ID", "Caso de Prueba", "Precondición", "Datos de Entrada (HTTP/JSON)", "Resultado Esperado / Código HTTP", "Estado" },
                new[] { 600, 1800, 1600, 1800, 2104, 600 },
                new List<string[]>
                {
                    new[] { "CP-01", "Autenticación válida", "Servidor Payara activo", "POST /auth/login {usr:Monster, pwd:Monster9}", "HTTP 200 OK: Retorno de token, nombre James P. Sullivan y rol Superadmin.", "EXITOSO" },
                    new[] { "CP-02", "Autenticación inválida", "Servidor Payara activo", "POST /auth/login {usr:Invalido, pwd:123}", "HTTP 401 Unauthorized: Rechazo de acceso con mensaje de credenciales erróneas.", "EXITOSO" },
                    new[] { "CP-03", "Depósito atómico (+)", "Cuenta 001-0001 activa", "POST /movimientos/deposito {nro:001-0001, monto:50}", "HTTP 200 OK: Saldo incrementado a $2800.00, nuevo movimiento y commit confirmado.", "EXITOSO" },
                    new[] { "CP-04", "Retiro con saldo insuficiente", "Cuenta 001-0004 ($950.00)", "POST /movimientos/retiro {nro:001-0004, monto:1500}", "HTTP 400 Bad Request: Rechazo por fondos insuficientes, rollback total y saldo intacto.", "EXITOSO" },
                    new[] { "CP-05", "Transferencia atómica (<=>)", "001-0001 y 001-0002 activas", "POST /movimientos/transferencia {orig:001-0001, dest:001-0002, m:100}", "HTTP 200 OK: Débito en origen, crédito en destino y 2 movimientos registrados.", "EXITOSO" },
                    new[] { "CP-06", "Concurrencia MashUp simultánea", "2 ventanillas operando", "Ventanillas 1 y 2 simultáneas sobre 001-0001", "V1 adquiere semáforo ROJO (200 OK); V2 recibe HTTP 409 Conflict (CONCURRENCIA_BLOQUEADA).", "EXITOSO" },
                    new[] { "CP-07", "Desbloqueo administrativo", "Cuenta 001-0001 en ROJO", "POST /cuentas/001-0001/desbloquear {admin:Monster}", "HTTP 200 OK: Retorno inmediato del semáforo a VERDE (Libre) en MySQL y memoria.", "EXITOSO" }
                }
            ));

            list.Add(TableCaption(39, "Guía de Diagnóstico y Resolución de Errores Frecuentes (Troubleshooting)"));
            list.Add(TechnicalTable(
                new[] { "Problema Técnico Observado", "Causa Raíz Identificada", "Procedimiento de Corrección y Solución" },
                new[] { 2600, 2700, 3204 },
                new List<string[]>
                {
                    new[] { "Error de conexión MySQL (Connection Refused en puerto 3306)", "El contenedor Podman/Docker de MySQL no fue inicializado previamente.", "Ejecutar 'podman compose up -d' o 'docker compose up -d' dentro de la carpeta 03. BDD y verificar con 'podman ps'." },
                    new[] { "Fallo de despliegue en Payara Server (Address already in use: 8080)", "Conflicto con otro servicio o instancia previa ocupando el puerto 8080.", "Detener el proceso en conflicto mediante PowerShell: 'Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process' y reiniciar Payara." },
                    new[] { "Android: NetworkOnMainThreadException al invocar la API RESTful", "Intento de ejecutar llamadas de red en el hilo principal de interfaz (UI Thread).", "Utilizar ExecutorService para despachar peticiones HTTP en segundo plano y sincronizar con la UI mediante Handler(Looper.getMainLooper())." },
                    new[] { "Android: java.io.IOException: Cleartext HTTP traffic not permitted", "Políticas de seguridad de Android 9+ que bloquean tráfico HTTP no cifrado.", "Añadir el atributo android:usesCleartextTraffic='true' en la etiqueta <application> del archivo AndroidManifest.xml." },
                    new[] { "Jakarta Faces: ViewExpiredException al enviar formularios tras inactividad", "La sesión web de JSF expiró o se reinició el contexto del servidor Payara.", "Configurar el parámetro de contexto jakarta.faces.STATE_SAVING_METHOD a 'client' en web.xml o renovar la sesión en login.xhtml." }
                }
            ));

            // =========================================================================
            // SECCIÓN 5: CONCLUSIONES
            // =========================================================================
            list.Add(Heading1("5 CONCLUSIONES"));
            list.Add(BulletParagraph("1.", "La implementación de la arquitectura de servicios web RESTful fundamentada en el protocolo HTTP y la serialización JSON demostró ser una solución técnica altamente superior a los esquemas tradicionales SOAP/WSDL, eliminando la sobrecarga computacional de parseo de esquemas XML y permitiendo una interoperabilidad fluida y ligera entre plataformas heterogéneas (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces)."));
            list.Add(BulletParagraph("2.", "El cumplimiento irrestricto de las propiedades ACID a través de la capa de persistencia JDBC y el motor relacional MySQL InnoDB (con aislamiento TRANSACTION_READ_COMMITTED y bloqueos pesimistas SELECT ... FOR UPDATE) garantizó la integridad absoluta de los saldos bancarios, previniendo condiciones de carrera, lecturas sucias y sobregiros no autorizados."));
            list.Add(BulletParagraph("3.", "El mecanismo de sincronización reactiva Semáforo MashUp ofreció una solución visual y técnica eficaz para resolver la concurrencia entre ventanillas físicas y canales remotos, alternando eficientemente entre los estados VERDE y ROJO tanto en memoria compartida atómica (ConcurrentHashMap) como en la tabla relacional control_mashup, retornando el código HTTP 409 Conflict ante colisiones transaccionales."));
            list.Add(BulletParagraph("4.", "La estricta aplicación del patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) en todos los niveles del ecosistema aseguró una clara separación de responsabilidades, facilitando el mantenimiento, la depuración y la extensión del software sin incurrir en acoplamientos innecesarios ni dependencias cruzadas."));
            list.Add(BulletParagraph("5.", "El despliegue contenerizado de la base de datos relacional mediante Podman/Docker Compose eliminó las discrepancias de entorno entre estaciones de desarrollo, permitiendo inicializar y restaurar el esquema bancario completo con sus datos maestros y hash BCrypt en cuestión de segundos."));
            list.Add(BulletParagraph("6.", "La ejecución de la matriz de pruebas transaccionales evidenció que la técnica de ordenamiento alfabético de números de cuenta previo a la adquisición de bloqueos pesimistas en transferencias bancarias eliminó por completo la posibilidad de interbloqueos mutuos (deadlocks), asegurando alta disponibilidad operativa en ventanillas concurrentes."));

            // =========================================================================
            // SECCIÓN 6: RECOMENDACIONES
            // =========================================================================
            list.Add(Heading1("6 RECOMENDACIONES"));
            list.Add(BulletParagraph("1.", "Para entornos de producción financiera de alta concurrencia, se recomienda migrar el protocolo de transporte hacia canales cifrados obligatorios HTTPS/TLS 1.3 con certificados digitales X.509, incorporando autenticación basada en JSON Web Tokens (JWT) y cabeceras de autorización Bearer en sustitución del intercambio de credenciales directas."));
            list.Add(BulletParagraph("2.", "Se aconseja implementar un pool de conexiones administrado por el propio servidor de aplicaciones Payara Server Enterprise (mediante JDBC Connection Pools y Recursos JNDI) en sustitución del DriverManager directo, optimizando la reutilización de conexiones físicas y minimizando la latencia de apertura de sockets TCP hacia MySQL."));
            list.Add(BulletParagraph("3.", "En el Cliente Móvil Android, se sugiere evaluar la integración de la biblioteca Retrofit 2 con OkHttp y GsonConverterFactory para la gestión declarativa de endpoints RESTful, junto con arquitectura MVVM (ViewModel y StateFlow) para desacoplar el ciclo de vida de los componentes gráficos frente a rotaciones de pantalla."));
            list.Add(BulletParagraph("4.", "Para el Semáforo MashUp en la capa web, se recomienda evaluar la implementación de Server-Sent Events (SSE) o WebSockets (Jakarta WebSocket API) en lugar de sondeos periódicos AJAX (polling a 2.5s), reduciendo el consumo de ancho de banda y la sobrecarga de peticiones HTTP redundantes cuando el estado de las cuentas permanece sin cambios."));
            list.Add(BulletParagraph("5.", "Se sugiere configurar políticas de respaldo automatizado continuo (mysqldump / replication) sobre el volumen persistente de la base de datos y establecer réplicas de sólo lectura (Read Replicas) para descargar las consultas de extractos cronológicos del nodo transaccional maestro."));
            list.Add(BulletParagraph("6.", "Implementar un middleware de observabilidad y trazabilidad distribuida mediante OpenTelemetry y Jaeger para trazar de forma unificada el recorrido de las solicitudes RESTful desde que parten de cualquier cliente hasta que se confirman en el motor InnoDB de la base de datos relacional."));

            // =========================================================================
            // SECCIÓN 7: REFERENCIAS BIBLIOGRÁFICAS
            // =========================================================================
            list.Add(Heading1("7 REFERENCIAS BIBLIOGRÁFICAS"));
            list.Add(Paragraph("[1] R. T. Fielding, 'Architectural Styles and the Design of Network-based Software Architectures', Ph.D. dissertation, Dept. Inf. Comput. Sci., Univ. California, Irvine, CA, USA, 2000."));
            list.Add(Paragraph("[2] R. Fielding, J. Gettys, J. Mogul, H. Frystyk, L. Masinter, P. Leach, y T. Berners-Lee, 'Hypertext Transfer Protocol -- HTTP/1.1', RFC 2616 / RFC 7231, IETF, 2014. [Online]. Disponible: https://datatracker.ietf.org/doc/html/rfc7231"));
            list.Add(Paragraph("[3] T. Bray, Ed., 'The JavaScript Object Notation (JSON) Data Interchange Format', RFC 8259, Internet Engineering Task Force (IETF), Dic. 2017. [Online]. Disponible: https://datatracker.ietf.org/doc/html/rfc8259"));
            list.Add(Paragraph("[4] Eclipse Foundation, 'Jakarta RESTful Web Services Specification 3.1', Eclipse EE.next Working Group, 2022. [Online]. Disponible: https://jakarta.ee/specifications/restful-ws/3.1/"));
            list.Add(Paragraph("[5] OpenAPI Initiative, 'OpenAPI Specification Version 3.0.3', Linux Foundation, 2020. [Online]. Disponible: https://spec.openapis.org/oas/v3.0.3"));
            list.Add(Paragraph("[6] Oracle Corporation, 'MySQL 8.0 Reference Manual: The InnoDB Storage Engine and Transaction Model', Oracle Help Center, 2024. [Online]. Disponible: https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html"));
            list.Add(Paragraph("[7] Payara Services Ltd., 'Payara Server Enterprise Documentation: Jakarta EE Web Services and Administration', 2024. [Online]. Disponible: https://docs.payara.fish/"));
            list.Add(Paragraph("[8] E. Gamma, R. Helm, R. Johnson, y J. Vlissides, 'Design Patterns: Elements of Reusable Object-Oriented Software', Boston, MA, USA: Addison-Wesley, 1994."));
            list.Add(Paragraph("[9] M. Fowler, 'Patterns of Enterprise Application Architecture', Boston, MA, USA: Addison-Wesley Professional, 2002."));
            list.Add(Paragraph("[10] IEEE Computer Society, 'IEEE Standard for Software Quality Assurance Processes', IEEE Std 730-2014, Jun. 2014, doi: 10.1109/IEEESTD.2014.6835105."));

            return list;
        }
    }
}
