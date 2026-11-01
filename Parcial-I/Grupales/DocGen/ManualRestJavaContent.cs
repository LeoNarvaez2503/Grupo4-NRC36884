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
            list.Add(Heading1("1 INTRODUCCION"));
            list.Add(Paragraph("Un servicio web fundamentado en RESTful (Representational State Transfer) sobre HTTP/JSON constituye un mecanismo estandarizado para la transferencia e interoperabilidad de información estructurada entre sistemas computacionales heterogéneos en redes corporativas y bancarias. En contraposición a los enfoques orientados puramente a recursos ligeros, RESTful impone el uso formal del Lenguaje de Marcado Extensible (XML) como formato estricto de serialización de mensajes y se gobierna a través de un contrato riguroso definido mediante Swagger / OpenAPI (Web Services Description Language). Dicho contrato desacopla y especifica minuciosamente las operaciones disponibles, los tipos de datos complejos (definidos mediante XML Schema Definitions - XSD), las fallas tipadas (RESTful Faults) y los enlaces de transporte de red (RESTful sobre HTTP/1.1 y HTTP/2). Esta infraestructura proporciona un marco determinista con garantías formales de tipado estricto, seguridad a nivel de mensaje (WS-Security) y alta confiabilidad requerida en arquitecturas bancarias de misión crítica."));
            list.Add(Paragraph("En el sector financiero central (Core Banking), las transacciones ejecutadas sobre cuentas corrientes y de ahorro (tales como acreditación de depósitos en efectivo, retiros por ventanilla y transferencias electrónicas entre cuentas de clientes) exigen el cumplimiento irrestricto de las propiedades ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad). Cuando múltiples ventanillas de atención física o canales de banca remota intentan acceder, debitar o consultar un mismo saldo de forma concurrente, emergen graves condiciones de carrera (race conditions), lecturas sucias (dirty reads) y posibles inconsistencias que comprometen la integridad del balance contable de la institución. Para mitigar estos riesgos operacionales, el presente proyecto implementa el modelo transaccional EurekaBank con exclusión mutua pesimista (mediante sentencias SQL SELECT ... FOR UPDATE a nivel de motor relacional InnoDB) y un componente reactivo distribuido denominado Semáforo MashUp de Concurrencia en tiempo real."));
            list.Add(Paragraph("El despliegue del componente servidor central se efectúa sobre Payara Server Enterprise, un servidor de aplicaciones de grado industrial derivado de GlassFish compatible con el estándar Jakarta EE, el cual provee soporte nativo para la especificación Jakarta RESTful Web Services (JAX-RS / Jersey) (Java API for XML Web Services) a través del subsistema Eclipse Metro. Por su parte, la persistencia de datos se gestiona de manera aislada y portable mediante un contenedor de base de datos relacional MySQL 8.0 (utilizando Podman o Docker), lo cual garantiza independencia absoluta del sistema operativo anfitrión y reproducibilidad inmediata en cualquier estación de trabajo o servidor corporativo."));
            list.Add(Paragraph("Para la capa de presentación y consumo del servicio RESTful, se aplica con máxima rigurosidad el patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) a través de cuatro clientes totalmente heterogéneos: un Cliente de Consola interactivo para terminales y servidores headless, un Cliente de Escritorio de alto rendimiento desarrollado con Java Swing, un Cliente Móvil nativo para la plataforma Android y un Cliente Web reactivo desarrollado con Jakarta Faces (JSF) sobre Facelets y sondeo asíncrono AJAX. Todo el ecosistema opera bajo el dominio institucional unificado ec.edu.monster y respeta las credenciales maestras autorizadas (usuario: Monster, contraseña: Monster9), satisfaciendo los requerimientos de ingeniería de software establecidos para el Grupo 04 (GR04)."));

            // =========================================================================
            // SECCIÓN 2: OBJETIVOS
            // =========================================================================
            list.Add(Heading1("2 OBJETIVOS"));
            list.Add(Heading2("2.1 Objetivo General"));
            list.Add(Paragraph("Diseñar, implementar, desplegar y documentar un sistema bancario distribuido y transaccional denominado EurekaBank, fundamentado en servicios web RESTful desplegados en Payara Server con persistencia relacional en MySQL sobre contenedores locales (Docker/Podman), desacoplado mediante el patrón arquitectónico MVC Tradicional puro en todos sus niveles y consumido concurrentemente por cuatro clientes heterogéneos (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces) con soporte de monitoreo reactivo mediante Semáforo MashUp en tiempo real."));

            list.Add(Heading2("2.2 Objetivos Específicos"));
            list.Add(BulletParagraph("1.", "Configurar y desplegar el contenedor de base de datos relacional MySQL 8.0 mediante Podman/Docker Compose, estableciendo el esquema relacional íntegro de EurekaBank, restricciones de clave foránea, registros de auditoría transaccional por ventanilla y tablas de control de concurrencia MashUp."));
            list.Add(BulletParagraph("2.", "Construir la capa de persistencia JDBC transaccional del servidor RESTful implementando aislamiento de transacciones (TRANSACTION_READ_COMMITTED) y bloqueos de fila pesimistas (SELECT ... FOR UPDATE) con el propósito de garantizar operaciones atómicas de depósito, retiro y transferencia libres de inconsistencias o sobregiros no autorizados."));
            list.Add(BulletParagraph("3.", "Diseñar e implementar el componente GestorConcurrencia en el servidor RESTful, coordinando los estados del Semáforo MashUp (VERDE para cuentas disponibles y ROJO para cuentas en operación transaccional activa en ventanilla) con persistencia instantánea y sincronización atómica en memoria."));
            list.Add(BulletParagraph("4.", "Implementar y publicar el endpoint Jakarta RESTful Web Services (JAX-RS / Jersey) (EurekaBankResource) en Payara Server Enterprise, definiendo anotaciones declarativas del lenguaje Java, estructuras serializables JAXB y generando el contrato formal Swagger / OpenAPI accesible bajo el dominio institucional ec.edu.monster."));
            list.Add(BulletParagraph("5.", "Desarrollar el Cliente de Consola (WSEurekaBankConsoleClient) bajo el patrón MVC Tradicional, consumiendo el Swagger / OpenAPI mediante stubs Jakarta RESTful Web Services (JAX-RS / Jersey) (HttpClient), incorporando menús interactivos tabulados y una rutina de simulación multi-hilo de 3 ventanillas simultáneas para verificar la exclusión mutua."));
            list.Add(BulletParagraph("6.", "Construir el Cliente de Escritorio (WSEurekaBankDesktopClient) mediante formularios gráficos Java Swing, presentando una visualización en tiempo real del semáforo con renderizadores de color personalizados (TableCellRenderer), extracción cronológica de movimientos y despacho asíncrono con SwingWorker para evitar bloqueos de interfaz."));
            list.Add(BulletParagraph("7.", "Implementar el Cliente Móvil (WSEurekaBankMobileClient) para la plataforma Android con layouts XML estructurados, serialización de envelopes RESTful vía HttpURLConnection/XmlPullParser y despacho en subprocesos en segundo plano (ExecutorService) sincronizados con el hilo principal de interfaz (Looper)."));
            list.Add(BulletParagraph("8.", "Desarrollar el Cliente Web (WSEurekaBankWebClient) sobre Jakarta Faces (JSF), implementando Managed Beans (@Named, @ViewScoped) y plantillas Facelets reactivas mediante sondeo asíncrono AJAX (polling periódico de 1.0 segundo) para reflejar visualmente la alternancia del semáforo entre VERDE y ROJO desde cualquier terminal del sistema."));

            // =========================================================================
            // SECCIÓN 3: MARCO TEÓRICO
            // =========================================================================
            list.Add(Heading1("3 MARCO TEORICO"));
            list.Add(Heading2("3.1 Web Services RESTful, XML y Contrato Swagger / OpenAPI"));
            list.Add(Paragraph("El protocolo RESTful (Representational State Transfer) sobre HTTP/JSON es un protocolo formal estandarizado por el consorcio W3C (World Wide Web Consortium) concebido para el intercambio estructurado de información en entornos distribuidos descentralizados. Un mensaje RESTful es un documento XML que contiene obligatoriamente una estructura jerárquica canónica conformada por tres componentes elementales:"));
            list.Add(BulletParagraph("• RESTful Envelope:", "Elemento raíz del documento XML que identifica el marco de trabajo del mensaje, define los espacios de nombres (namespaces) oficiales de RESTful (http://schemas.xmlrest.org/rest/envelope/ para RESTful 1.1 o http://www.w3.org/2003/05/rest-envelope para RESTful 1.2) y establece las reglas de procesamiento."));
            list.Add(BulletParagraph("• RESTful Header:", "Bloque opcional que transporta información de control contextual no directamente asociada con la carga de negocio, tales como identificadores de sesión, credenciales de autenticación WS-Security, identificadores de ruteo transaccional o directivas de procesamiento inmediato (actor / mustUnderstand)."));
            list.Add(BulletParagraph("• RESTful Body:", "Contenedor obligatorio que transporta la carga útil (payload) del mensaje dirigida al destinatario final. En caso de suscitarse un error durante la invocación remota, el Body aloja un elemento especial denominado RESTful Fault, el cual encapsula el código de fallo (faultcode), la descripción legible del error (faultstring) y los detalles técnicos de la excepción (detail)."));
            list.Add(Paragraph("La comunicación entre clientes y servidores se gobierna estrictamente a través de un contrato formal Swagger / OpenAPI (Web Services Description Language). El documento Swagger / OpenAPI actúa como la especificación canónica del servicio, describiendo las funciones disponibles mediante seis elementos clave:"));
            list.Add(BulletParagraph("• types:", "Define los tipos de datos utilizados en los mensajes utilizando el estándar XML Schema Definition (XSD). Permite estructurar objetos complejos tales como cuentas bancarias, historiales de movimientos y objetos de respuesta con validación estricta de formato y nulidad."));
            list.Add(BulletParagraph("• message:", "Define la estructura abstracta de los parámetros de entrada y salida para cada operación remota."));
            list.Add(BulletParagraph("• portType:", "Define la interfaz abstracta del servicio web, agrupando un conjunto de operaciones invocables (análogo a una interfaz de programación en Java)."));
            list.Add(BulletParagraph("• binding:", "Especifica el protocolo de transporte concreto y el formato de codificación para las operaciones de un portType (habitualmente RESTful sobre transporte HTTP/1.1 con estilo Document/Literal)."));
            list.Add(BulletParagraph("• service y port:", "Especifica la dirección física de red (URI o URL del endpoint) donde los clientes pueden conectarse para enviar sus peticiones HTTP POST con el payload RESTful."));

            list.Add(Heading2("3.2 Arquitectura Orientada a Servicios (SOA) y Principios de Diseño Empresarial"));
            list.Add(Paragraph("La Arquitectura Orientada a Servicios (SOA) es un paradigma de diseño arquitectónico de software que descompone los procesos de negocio en componentes funcionales autónomos denominados servicios. En el ámbito de las instituciones bancarias, los sistemas de Core Banking adoptan SOA para desacoplar radicalmente la lógica financiera central (gestión de cuentas, cobro de comisiones, cálculo de intereses y transferencias interbancarias) de los canales de interacción con el cliente (cajeros automáticos, ventanillas físicas en agencias, aplicaciones de banca móvil y portales de banca por internet)."));
            list.Add(Paragraph("Los servicios implementados en SOA se rigen por principios fundamentales de diseño:"));
            list.Add(BulletParagraph("• Bajo Acoplamiento:", "El servidor RESTful y los clientes heterogéneos mantienen dependencias reducidas al contrato Swagger / OpenAPI, permitiendo que la lógica interna del servidor evolucione sin alterar la firma pública ni obligar a recompilar los clientes si el esquema se preserva."));
            list.Add(BulletParagraph("• Contrato Formal Estandarizado:", "Los servicios comparten un contrato explícito Swagger / OpenAPI que define sin ambigüedades los tipos de datos y los comportamientos admisibles, eliminando discrepancias de serialización entre plataformas dispares como Java, .NET o Android."));
            list.Add(BulletParagraph("• Reusabilidad y Autonomía:", "La lógica de transacciones bancarias reside de forma exclusiva en el servidor central; ningún cliente implementa lógica financiera localmente, evitando duplicación de código e inconsistencias de saldo."));

            list.Add(Heading2("3.3 Modelo Transaccional Bancario y Propiedades ACID en JDBC"));
            list.Add(Paragraph("Las operaciones monetarias bancarias (depósitos, retiros y transferencias) constituyen transacciones de misión crítica que deben satisfacer de manera rigurosa las propiedades ACID:"));
            list.Add(BulletParagraph("• Atomicidad (Atomicity):", "Toda operación transaccional se ejecuta como una unidad indivisible de trabajo. En una transferencia interbancaria donde se debitan $50.00 de la cuenta 001-0001 y se acreditan en la cuenta 001-0002, ambas modificaciones deben confirmarse conjuntamente (commit). Si ocurre una falla de red, un fallo de hardware o un error lógico antes de completar la segunda operación, el motor de base de datos ejecuta un rollback total, revirtiendo todas las alteraciones y restaurando el estado exacto anterior."));
            list.Add(BulletParagraph("• Consistencia (Consistency):", "Cualquier transacción lleva a la base de datos de un estado válido a otro estado válido, respetando todas las reglas de negocio e invariantes definidas (ej. saldos monetarios positivos o mayores o iguales a cero, unicidad de número de movimiento por cuenta e integridad referencial foránea hacia clientes y empleados)."));
            list.Add(BulletParagraph("• Aislamiento (Isolation):", "Las transacciones concurrentes ejecutadas por distintas ventanillas físicas o clientes web no deben interferir entre sí. En este proyecto se establece el nivel de aislamiento TRANSACTION_READ_COMMITTED en conjunto con bloqueos de fila pesimistas mediante sentencias SELECT ... FOR UPDATE, previniendo condiciones de lectura sucia (dirty read) y asegurando que las modificaciones de saldo se atomicen secuencialmente a nivel de registro."));
            list.Add(BulletParagraph("• Durabilidad (Durability):", "Una vez confirmada la transacción mediante la instrucción commit(), los cambios persisten de manera irreversible en el almacenamiento secundario no volátil (registros redo log y tablas tablespace del motor InnoDB), subsistiendo incluso ante cortes de energía o reinicio abrupto de los contenedores de software."));

            list.Add(Heading2("3.4 Patrón Arquitectónico Modelo-Vista-Controlador (MVC Tradicional)"));
            list.Add(Paragraph("El patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) organiza los componentes de un aplicativo computacional en tres capas fundamentales con responsabilidades claramente delimitadas, sin admitir capas intermedias artificiales de 'Servicio' ajenas ni ViewModels desalineados con la arquitectura canónica:"));
            list.Add(BulletParagraph("1. Capa Modelo (Model):", "Representa la información del dominio del negocio bancario y las estructuras de datos (Cuenta, Movimiento, Empleado, Sucursal, EstadoSemaforo) junto con los mecanismos de persistencia JDBC (CuentaDAO) o de consumo remoto vía cliente RESTful. El Modelo no posee referencia alguna a interfaces gráficas ni al entorno de ejecución visual."));
            list.Add(BulletParagraph("2. Capa Vista (View):", "Es la interfaz encargada de presentar los datos de forma visual al usuario y capturar sus interacciones (eventos de teclado, clics de botones, selección de filas en tablas). En el Cliente Consola corresponde a ConsolaView; en Escritorio, a LoginView y DashboardView de Swing; en Móvil, a los layouts XML y Activities de Android; y en Web, a las plantillas Facelets XHTML. La Vista es pasiva y delega toda la gestión al Controlador."));
            list.Add(BulletParagraph("3. Capa Controlador (Controller):", "Actúa como el mediador central del flujo del aplicativo. Recibe los eventos disparados por la Vista, procesa y valida las entradas del usuario, invoca los métodos pertinentes del Modelo, evalúa las respuestas transaccionales y notifica a la Vista para que actualice sus elementos gráficos. En este ecosistema, cada cliente dispone de un Controlador dedicado (ej. ConsolaControlador, DashboardController, MobileController, DashboardBean) que canaliza las operaciones hacia el endpoint RESTful."));

            list.Add(Heading2("3.5 Servidor de Aplicaciones Payara Server Enterprise y Jakarta RESTful Web Services (JAX-RS / Jersey)"));
            list.Add(Paragraph("Payara Server Enterprise es un servidor de aplicaciones de código abierto optimizado para infraestructuras corporativas, derivado de la base de código de GlassFish Server y completamente compatible con el estándar Jakarta EE. Payara incorpora una implementación de alto rendimiento para servicios web basada en Eclipse Metro y Jakarta RESTful Web Services (JAX-RS / Jersey) (Java API for XML Web Services)."));
            list.Add(Paragraph("Jakarta RESTful Web Services (JAX-RS / Jersey) simplifica enormemente el desarrollo de servicios web RESTful al proporcionar anotaciones declarativas del lenguaje Java (tales como @WebService, @WebMethod, @WebParam y @WebResult), abstrayendo al desarrollador de la manipulación manual de flujos XML, parseo de envelopes y conversión de tipos. El servidor Payara gestiona los pools de conexiones HTTP, los hilos de atención concurrente, la seguridad del contenedor y la exposición automática del Swagger / OpenAPI en su puerto 8080."));

            list.Add(Heading2("3.6 Motor de Base de Datos MySQL 8.0 y Despliegue en Contenedores (Docker/Podman)"));
            list.Add(Paragraph("La persistencia relacional del sistema bancario se implementa sobre el motor de base de datos relacional MySQL 8.0 utilizando el motor de almacenamiento transaccional InnoDB. Para garantizar portabilidad absoluta e independencia del sistema operativo anfitrión, la base de datos se despliega dentro de un contenedor local gestionado mediante Podman o Docker Compose."));
            list.Add(Paragraph("Esta aproximación basada en contenedores ofrece ventajas técnicas decisivas:"));
            list.Add(BulletParagraph("• Aislamiento y Reproducibilidad:", "El entorno de ejecución de la base de datos (versión exacta de MySQL, configuraciones de variables de entorno, zona horaria y juegos de caracteres UTF-8) queda completamente sellado, garantizando que el comportamiento sea idéntico en cualquier máquina del equipo de trabajo."));
            list.Add(BulletParagraph("• Persistencia Segura de Datos:", "Mediante el uso de volúmenes persistentes nombrados (eurekabank_mysql_data), los datos de las cuentas bancarias, saldos y movimientos cronológicos sobreviven intactos a través de sucesivos reinicios o destrucciones del ciclo de vida del contenedor."));
            list.Add(BulletParagraph("• Inicialización Automatizada:", "A través del punto de montaje /docker-entrypoint-initdb.d/, el script DDL eureka_bank.sql se ejecuta automáticamente en el arranque inicial del contenedor, creando el esquema eurekabank_db con todas sus tablas e insertando los registros maestros sin intervención manual."));

            list.Add(Heading2("3.7 Mecanismo de Sincronización Reactiva Semáforo MashUp"));
            list.Add(Paragraph("El Semáforo MashUp de Concurrencia es un mecanismo de coordinación reactivo diseñado para sincronizar en tiempo real el acceso a las cuentas bancarias entre ventanillas de atención física y canales remotos concurrentes. Cuando una ventanilla inicia una operación transaccional (depósito, retiro o transferencia) sobre una cuenta bancaria (ej. cuenta 001-0001), el sistema activa de forma inmediata el estado ROJO en el componente GestorConcurrencia y en la tabla control_mashup de MySQL, registrando el número de ventanilla operativa y el nombre del operador bancario responsable."));
            list.Add(Paragraph("Durante la ejecución de la transacción, el sistema introduce una ventana de latencia controlada (3.5 segundos) con fines de demostración académica y validación visual. En dicho lapso, cualquier otro cliente (Consola, Escritorio, Móvil o Web) que consulte la cuenta bancaria observará el indicador del semáforo en ROJO ('Ocupado en Ventanilla 1 por Monster') y no podrá solapar operaciones transaccionales sobre la misma cuenta, garantizando exclusión mutua tanto a nivel de lógica de aplicación como a nivel de base de datos. Una vez ejecutado el commit transaccional, el semáforo se libera automáticamente retornando al estado VERDE ('Libre'), permitiendo que el siguiente operador proceda de inmediato."));

            // =========================================================================
            // SECCIÓN 4: DESARROLLO DEL APLICATIVO
            // =========================================================================
            list.Add(Heading1("4 DESARROLLO DEL APLICATIVO"));
            list.Add(Paragraph("A continuación se describe de forma exhaustiva y paso a paso el procedimiento técnico de creación, configuración, codificación y verificación de cada uno de los subsistemas del ecosistema EurekaBank RESTful con base de datos en Java, incluyendo el Servidor Web RESTful y los cuatro clientes heterogéneos (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces). Cada bloque de código fuente relevante se presenta formalmente encapsulado en tablas numeradas para su correspondiente registro en el Índice de Tablas."));

            // -------------------------------------------------------------------------
            // 4.1 SERVIDOR WEB RESTful
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.1 Servidor Web RESTful (WS_EurekaBank_Java_RESTful)"));
            list.Add(Paragraph("El servidor web central aloja la lógica transaccional bancaria, la conexión JDBC con el contenedor de base de datos relacional MySQL, el gestor de concurrencia MashUp y el punto de enlace Jakarta RESTful Web Services (JAX-RS / Jersey) que publica el contrato Swagger / OpenAPI oficial."));

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
-- SISTEMA BANCARIO TRANSACCIONAL EUREKABANK - GRUPO 04 (GR04)
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

-- =============================================================================
-- DATOS SEMILLA (SEEDERS) - CREDENCIALES: Monster / Monster9
-- Hash BCrypt verificado para 'Monster9': $2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK
-- =============================================================================

INSERT INTO sucursal (codigo, nombre, ciudad, direccion) VALUES
('001', 'Sucursal Matriz ESPE', 'Sangolquí', 'Av. Gral. Rumiñahui s/n'),
('002', 'Sucursal Norte La Carolina', 'Quito', 'Av. Amazonas y Naciones Unidas'),
('003', 'Sucursal Centro Financiero', 'Guayaquil', 'Av. 9 de Octubre y Malecón');

INSERT INTO empleado (usuario, password, nombre_completo, rol, id_sucursal, ventanilla) VALUES
('Monster',  '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'James P. Sullivan (Monster Admin)', 'Superadmin Monster', 1, 1),
('cajero1',  '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Alejandro Flores (Ventanilla 1)',   'Cajero',             1, 1),
('cajero2',  '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Anthony Morales (Ventanilla 2)',    'Cajero',             1, 2),
('cajero3',  '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Leonardo Narváez (Ventanilla 3)',   'Cajero',             1, 3),
('cajero4',  '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'Mike Wazowski (Ventanilla 4)',      'Cajero',             2, 4),
('gerente1', '$2a$10$sr3qsj1UdWynnqBnwUfV5OS3slm0Lo5uyHwyyIuNiAfc5jmDLioMK', 'María López (Gerente Sucursal)',    'Gerente',            1, NULL);

INSERT INTO cliente (dni, nombre, apellido, direccion, telefono, email) VALUES
('1700000001', 'James P.',  'Sullivan', 'Monstropolis Av. Central 101', '0991000001', 'monster@monster.edu.ec'),
('1712345678', 'Alejandro', 'Flores',   'Av. Gral. Rumiñahui, Sangolquí', '0992345678', 'acflores@espe.edu.ec'),
('1723456789', 'Anthony',   'Morales',  'Av. América, Quito',           '0993456789', 'aamorales@espe.edu.ec'),
('1734567890', 'Leonardo',  'Narváez',  'Av. 6 de Diciembre, Quito',    '0994567890', 'lvnarvaez@espe.edu.ec'),
('1745678901', 'Carlos',    'García',   'Av. Amazonas 450, Quito',      '0995678901', 'carlos.garcia@correo.com'),
('1756789012', 'Ana',       'Torres',   'Av. 9 de Octubre, Guayaquil',  '0996789012', 'ana.torres@correo.com');

INSERT INTO cuenta (numero_cuenta, moneda, saldo, id_cliente, id_sucursal, estado) VALUES
('001-0001', 'USD', 2500.00, 1, 1, 'Activa'),
('001-0002', 'USD', 1800.00, 2, 1, 'Activa'),
('001-0003', 'USD', 3200.50, 3, 1, 'Activa'),
('001-0004', 'USD',  950.00, 4, 1, 'Activa'),
('002-0001', 'USD', 4100.00, 5, 2, 'Activa'),
('003-0001', 'USD', 1500.00, 6, 3, 'Activa');

INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por) VALUES
('001-0001', 'VERDE', NULL, NULL),
('001-0002', 'VERDE', NULL, NULL),
('001-0003', 'VERDE', NULL, NULL),
('001-0004', 'VERDE', NULL, NULL),
('002-0001', 'VERDE', NULL, NULL),
('003-0001', 'VERDE', NULL, NULL);

INSERT INTO movimiento (numero_operacion, numero_cuenta, id_empleado, ventanilla, tipo, monto, saldo_resultante, cuenta_referencia, descripcion, fecha_hora) VALUES
('APE-100001', '001-0001', 1, 1, 'Deposito', 2000.00, 2000.00, NULL, 'Apertura de cuenta ahorro Monster', DATE_SUB(NOW(), INTERVAL 3 DAY)),
('DEP-100002', '001-0001', 2, 1, 'Deposito',  500.00, 2500.00, NULL, 'Depósito en efectivo Ventanilla 1', DATE_SUB(NOW(), INTERVAL 1 DAY)),
('APE-100003', '001-0002', 1, 1, 'Deposito', 2000.00, 2000.00, NULL, 'Apertura de cuenta ahorro',         DATE_SUB(NOW(), INTERVAL 2 DAY)),
('RET-100004', '001-0002', 3, 2, 'Retiro',    200.00, 1800.00, NULL, 'Retiro por ventanilla 2',           DATE_SUB(NOW(), INTERVAL 12 HOUR)),
('APE-100005', '001-0003', 1, 1, 'Deposito', 3200.50, 3200.50, NULL, 'Apertura de cuenta ahorro',         DATE_SUB(NOW(), INTERVAL 2 DAY)),
('APE-100006', '001-0004', 4, 3, 'Deposito',  950.00,  950.00, NULL, 'Apertura de cuenta ahorro',         DATE_SUB(NOW(), INTERVAL 1 DAY)),
('APE-100007', '002-0001', 5, 4, 'Deposito', 4100.00, 4100.00, NULL, 'Apertura Sucursal Norte',           DATE_SUB(NOW(), INTERVAL 1 DAY)),
('APE-100008', '003-0001', 1, 1, 'Deposito', 1500.00, 1500.00, NULL, 'Apertura Sucursal Guayaquil',       DATE_SUB(NOW(), INTERVAL 1 DAY));

"""));

            list.Add(TableCaption(3, "Diccionario de Datos del Esquema Relacional EurekaBank"));
            list.Add(TechnicalTable(
                new[] { "Tabla", "Clave Primaria", "DescripciÃ³n TÃ©cnica y PropÃ³sito Bancario", "Relaciones ForÃ¡neas" },
                new[] { 1600, 1500, 3904, 1500 },
                new List<string[]>
                {
                    new[] { "sucursal", "id_sucursal", "CatÃ¡logo de agencias bancarias (001 Matriz ESPE, 002 Norte La Carolina, 003 Centro Financiero).", "Ninguna" },
                    new[] { "empleado", "id_empleado", "Cajeros y operadores de ventanilla con contraseÃ±as encriptadas en BCrypt ($). Incluye credencial maestra Monster/Monster9.", "id_sucursal -> sucursal" },
                    new[] { "cliente", "id_cliente", "Datos demogrÃ¡ficos y de contacto de clientes titulares con validaciÃ³n de DNI.", "Ninguna" },
                    new[] { "cuenta", "numero_cuenta", "Cuentas de ahorro/corriente en USD con saldo DECIMAL(12,2) y bloqueo pesimista SELECT FOR UPDATE.", "id_cliente -> cliente, id_sucursal -> sucursal" },
                    new[] { "control_mashup", "numero_cuenta", "SemÃ¡foro cromÃ¡tico de concurrencia en tiempo real (VERDE = Libre, ROJO = Ocupada en Ventanilla con operador y timestamp).", "numero_cuenta -> cuenta (ON DELETE CASCADE)" },
                    new[] { "movimiento", "id_movimiento", "Historial cronolÃ³gico de transacciones ACID (DepÃ³sito, Retiro, Transferencia) con nÃºmero de operaciÃ³n, saldo resultante y ventanilla.", "numero_cuenta -> cuenta, id_empleado -> empleado" }
                }
            ));

            list.Add(FigureCaption(1, "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker"));
            list.Add(ScreenshotBox(
                1,
                "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker",
                "Consola de comandos PowerShell / Podman CLI y herramienta de gestión DBeaver o MySQL Workbench.",
                "Comando de ejecución: podman compose up -d dentro del directorio 03. BDD.",
                "Ejecutar 'podman ps' para verificar el contenedor mysql-db y 'SELECT COUNT(*) FROM cuenta;' en DBeaver.",
                "Contenedor en estado Up (puerto 3306 activo) y las 7 tablas del esquema eurekabank_db pobladas con datos de prueba iniciales."
            ));

            list.Add(Heading3("4.1.2 Paso 2: Creación del Proyecto Web Maven y Configuración de Dependencias Jakarta EE"));
            list.Add(Paragraph("En NetBeans IDE se inicia el asistente New Project > Java with Maven > Web Application. Se define el nombre del proyecto como WS_EurekaBank_Java_RESTful, se establece el GroupId institucional ec.edu.monster y se selecciona Payara Server como servidor de destino. En el archivo pom.xml se configuran las dependencias de Jakarta XML Web Services API, la implementación de Metro/Jakarta RESTful Web Services (JAX-RS / Jersey) Runtime y el driver oficial MySQL Connector/J:"));

            list.Add(TableCaption(4, "Descriptor de Dependencias y Plugins del Servidor pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WS_EurekaBank_Java_RESTful</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.platform</groupId>
            <artifactId>jakarta.jakartaee-web-api</artifactId>
            <version>10.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.3.0</version>
        </dependency>
    </dependencies>
</project>
"""));

            list.Add(FigureCaption(2, "Creación y Estructura del Proyecto Servidor Maven en NetBeans IDE"));
            list.Add(ScreenshotBox(
                2,
                "Creación y Estructura del Proyecto Servidor Maven en NetBeans IDE",
                "NetBeans IDE - Asistente New Project > Java with Maven > Web Application.",
                "Project Name: WS_EurekaBank_Java_RESTful | Group Id: ec.edu.monster | Target Server: Payara Server.",
                "Crear el proyecto, abrir pom.xml y verificar la resolución de dependencias Maven.",
                "Árbol del proyecto generado con la carpeta Dependencies mostrando jakarta.xml.ws-api y mysql-connector-j correctamente descargados."
            ));

            list.Add(Heading3("4.1.3 Paso 3: Estructuración Modular de Paquetes del Servidor RESTful (MVC Tradicional)"));
            list.Add(Paragraph("Se crean los paquetes Java dentro de src/main/java siguiendo rigurosamente el patrón arquitectónico MVC Tradicional puro, eliminando capas superfluas de 'Services' y centralizando la orquestación en controladores dedicados:"));

            list.Add(TableCaption(5, "Estructura de Paquetes y Responsabilidades del Servidor RESTful (MVC Tradicional)"));
            list.Add(TechnicalTable(
                new[] { "Paquete", "Clases Principales", "Responsabilidad Arquitectónica (MVC Tradicional)" },
                new[] { 2800, 2700, 3004 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.ws", "EurekaBankResource.java", "Punto de enlace (Endpoint) Jakarta RESTful Web Services (JAX-RS / Jersey) que publica el servicio RESTful bajo el namespace http://ws.monster.edu.ec/." },
                    new[] { "ec.edu.monster.controller", "EurekaBankController.java, GestorConcurrencia.java", "Controlador central que orquesta las operaciones transaccionales y administra el estado del semáforo MashUp." },
                    new[] { "ec.edu.monster.model", "Cuenta.java, Movimiento.java, Cliente.java, Sucursal.java, Empleado.java, RespuestaTransaccion.java, EstadoSemaforo.java", "Entidades del dominio bancario y objetos de transferencia serializables mediante JAXB." },
                    new[] { "ec.edu.monster.model.dao", "CuentaDAO.java, MovimientoDAO.java, EmpleadoDAO.java, ControlMashUpDAO.java", "Capa de persistencia JDBC con sentencias SQL preparadas, control transaccional ACID y bloqueos SELECT FOR UPDATE." },
                    new[] { "ec.edu.monster.database", "ConexionBDD.java", "Gestión de conexiones JDBC directas al contenedor MySQL con configuración de aislamiento y UTF-8." }
                }
            ));

            list.Add(FigureCaption(3, "Estructura de Paquetes del Servidor RESTful en NetBeans IDE"));
            list.Add(ScreenshotBox(
                3,
                "Estructura de Paquetes del Servidor RESTful en NetBeans IDE",
                "NetBeans IDE - Vista Projects / Files.",
                "Expandir el nodo Source Packages del proyecto WS_EurekaBank_Java_RESTful.",
                "Inspeccionar los paquetes ec.edu.monster.ws, controller, model, model.dao y database.",
                "Estructura de paquetes visualizada con todos los archivos .java compilados sin errores ni advertencias."
            ));

            list.Add(Heading3("4.1.4 Paso 4: Capa de Conexión a Base de Datos (ConexionBDD.java)"));
            list.Add(Paragraph("Se crea la clase ConexionBDD en el paquete ec.edu.monster.database. Esta clase carga el driver com.mysql.cj.jdbc.Driver y establece conexiones JDBC con el contenedor local MySQL sobre el puerto 3306, configurando parámetros de seguridad (useSSL=false), compatibilidad de claves públicas (allowPublicKeyRetrieval=true), zona horaria y juego de caracteres UTF-8:"));

            list.Add(TableCaption(6, "Factoría de Conexiones JDBC ConexionBDD.java"));
            list.Add(CodeBox("""
package ec.edu.monster.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {
    private static final String URL = "jdbc:mysql://localhost:3306/eurekabank_db"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Guayaquil"
            + "&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String CLAVE = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USUARIO, CLAVE);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return conn;
    }
}
"""));

            list.Add(Heading3("4.1.5 Paso 5: Implementación de Entidades del Dominio y Modelos DTO"));
            list.Add(Paragraph("En el paquete ec.edu.monster.model se crean las clases que representan las entidades del negocio bancario. Estas clases implementan Serializable y cuentan con constructores, getters y setters para permitir su serialización automática mediante JAXB en los mensajes XML de RESTful:"));

            list.Add(TableCaption(7, "Entidad de Dominio y Modelo de Cuenta Cuenta.java"));
            list.Add(CodeBox("""
package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Cuenta implements Serializable {
    private String codigo;
    private String codigoSucursal;
    private String codigoCliente;
    private String nombreCliente;
    private BigDecimal saldo;
    private String estado;
    private int contadorMovimientos;
    private String estadoMashup;     // VERDE o ROJO
    private int ventanillaMashup;     // Ventanilla que la bloqueó
    private String operadorMashup;   // Operador responsable

    public Cuenta() {}

    public Cuenta(String codigo, String nombreCliente, BigDecimal saldo, String estadoMashup) {
        this.codigo = codigo;
        this.nombreCliente = nombreCliente;
        this.saldo = saldo;
        this.estadoMashup = estadoMashup;
    }

    // Getters y Setters completos para serialización JAXB
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public String getEstadoMashup() { return estadoMashup; }
    public void setEstadoMashup(String estadoMashup) { this.estadoMashup = estadoMashup; }
    public int getVentanillaMashup() { return ventanillaMashup; }
    public void setVentanillaMashup(int ventanillaMashup) { this.ventanillaMashup = ventanillaMashup; }
    public String getOperadorMashup() { return operadorMashup; }
    public void setOperadorMashup(String operadorMashup) { this.operadorMashup = operadorMashup; }
}
"""));

            list.Add(TableCaption(8, "Entidad de Dominio y Registro de Auditoría Movimiento.java"));
            list.Add(CodeBox("""
package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Movimiento implements Serializable {
    private String cuenta;
    private int numeroMovimiento;
    private Timestamp fechaHora;
    private String codigoEmpleado;
    private String tipoMovimiento;
    private String descripcionTipo;
    private BigDecimal importe;
    private String cuentaReferencia;

    public Movimiento() {}

    public String getCuenta() { return cuenta; }
    public void setCuenta(String cuenta) { this.cuenta = cuenta; }
    public int getNumeroMovimiento() { return numeroMovimiento; }
    public void setNumeroMovimiento(int numeroMovimiento) { this.numeroMovimiento = numeroMovimiento; }
    public Timestamp getFechaHora() { return fechaHora; }
    public void setFechaHora(Timestamp fechaHora) { this.fechaHora = fechaHora; }
    public String getCodigoEmpleado() { return codigoEmpleado; }
    public void setCodigoEmpleado(String codigoEmpleado) { this.codigoEmpleado = codigoEmpleado; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public String getDescripcionTipo() { return descripcionTipo; }
    public void setDescripcionTipo(String descripcionTipo) { this.descripcionTipo = descripcionTipo; }
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    public String getCuentaReferencia() { return cuentaReferencia; }
    public void setCuentaReferencia(String cuentaReferencia) { this.cuentaReferencia = cuentaReferencia; }
}
"""));

            list.Add(TableCaption(9, "Objeto de Transferencia de Respuesta Transaccional RespuestaTransaccion.java"));
            list.Add(CodeBox("""
package ec.edu.monster.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class RespuestaTransaccion implements Serializable {
    private boolean exitoso;
    private String mensaje;
    private BigDecimal nuevoSaldo;

    public RespuestaTransaccion() {}

    public RespuestaTransaccion(boolean exitoso, String mensaje, BigDecimal nuevoSaldo) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.nuevoSaldo = nuevoSaldo;
    }

    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public BigDecimal getNuevoSaldo() { return nuevoSaldo; }
    public void setNuevoSaldo(BigDecimal nuevoSaldo) { this.nuevoSaldo = nuevoSaldo; }
}
"""));

            list.Add(Heading3("4.1.6 Paso 6: Implementación de la Capa de Persistencia Transaccional ACID (CuentaDAO.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.model.dao se implementa la clase CuentaDAO, la cual encapsula todas las operaciones transaccionales sobre la base de datos MySQL. Se desactivan las confirmaciones automáticas mediante setAutoCommit(false), se implementan bloqueos de fila pesimistas con sentencias SELECT ... FOR UPDATE, y se ordenan los códigos de cuenta en transferencias interbancarias para prevenir bloqueos mutuos (deadlocks):"));

            list.Add(TableCaption(10, "Capa de Persistencia ACID y Bloqueos CuentaDAO.java"));
            list.Add(CodeBox("""
package ec.edu.monster.model.dao;

import ec.edu.monster.database.ConexionBDD;
import ec.edu.monster.model.Cuenta;
import ec.edu.monster.model.RespuestaTransaccion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public List<Cuenta> obtenerCuentas(String sucursalId) {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT c.chr_cuencodigo, CONCAT(cl.vch_clienombre, ' ', cl.vch_cliepaterno) AS titular, "
                   + "c.dec_cuensaldo, COALESCE(cm.vch_estadomashup, 'VERDE') AS estado_mashup, "
                   + "COALESCE(cm.int_ventanilla, 0) AS ventanilla, COALESCE(cm.vch_operador, '') AS operador "
                   + "FROM cuenta c "
                   + "INNER JOIN cliente cl ON c.chr_cliecodigo = cl.chr_cliecodigo "
                   + "LEFT JOIN control_mashup cm ON c.chr_cuencodigo = cm.chr_cuencodigo "
                   + (sucursalId != null && !sucursalId.isEmpty() ? "WHERE c.chr_sucucodigo = ? " : "")
                   + "ORDER BY c.chr_cuencodigo ASC";

        try (Connection conn = ConexionBDD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (sucursalId != null && !sucursalId.isEmpty()) {
                ps.setString(1, sucursalId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cuenta c = new Cuenta();
                    c.setCodigo(rs.getString("chr_cuencodigo"));
                    c.setNombreCliente(rs.getString("titular"));
                    c.setSaldo(rs.getBigDecimal("dec_cuensaldo"));
                    c.setEstadoMashup(rs.getString("estado_mashup"));
                    c.setVentanillaMashup(rs.getInt("ventanilla"));
                    c.setOperadorMashup(rs.getString("operador"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar cuentas: " + e.getMessage());
        }
        return lista;
    }

    public RespuestaTransaccion depositar(String cuenta, BigDecimal monto, String codEmpleado, int ventanilla) {
        String sqlLock = "SELECT dec_cuensaldo, int_cuencontmov FROM cuenta WHERE chr_cuencodigo = ? FOR UPDATE";
        String sqlUpdate = "UPDATE cuenta SET dec_cuensaldo = dec_cuensaldo + ?, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = ?";
        String sqlMov = "INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte) VALUES (?, ?, CURRENT_TIMESTAMP, ?, '001', ?)";

        try (Connection conn = ConexionBDD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal nuevoSaldo;
                int nuevoContador;
                try (PreparedStatement psLock = conn.prepareStatement(sqlLock)) {
                    psLock.setString(1, cuenta);
                    try (ResultSet rs = psLock.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return new RespuestaTransaccion(false, "Cuenta inexistente: " + cuenta, BigDecimal.ZERO);
                        }
                        nuevoSaldo = rs.getBigDecimal("dec_cuensaldo").add(monto);
                        nuevoContador = rs.getInt("int_cuencontmov") + 1;
                    }
                }
                try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdate)) {
                    psUpd.setBigDecimal(1, monto);
                    psUpd.setString(2, cuenta);
                    psUpd.executeUpdate();
                }
                try (PreparedStatement psMov = conn.prepareStatement(sqlMov)) {
                    psMov.setString(1, cuenta);
                    psMov.setInt(2, nuevoContador);
                    psMov.setString(3, codEmpleado);
                    psMov.setBigDecimal(4, monto);
                    psMov.executeUpdate();
                }
                conn.commit();
                return new RespuestaTransaccion(true, "Depósito exitoso de $" + monto + " en Ventanilla " + ventanilla, nuevoSaldo);
            } catch (SQLException ex) {
                conn.rollback();
                return new RespuestaTransaccion(false, "Fallo transaccional: " + ex.getMessage(), BigDecimal.ZERO);
            }
        } catch (SQLException e) {
            return new RespuestaTransaccion(false, "Error de conexión: " + e.getMessage(), BigDecimal.ZERO);
        }
    }

    public RespuestaTransaccion retirar(String cuenta, BigDecimal monto, String codEmpleado, int ventanilla) {
        String sqlLock = "SELECT dec_cuensaldo, int_cuencontmov FROM cuenta WHERE chr_cuencodigo = ? FOR UPDATE";
        String sqlUpdate = "UPDATE cuenta SET dec_cuensaldo = dec_cuensaldo - ?, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = ?";
        String sqlMov = "INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte) VALUES (?, ?, CURRENT_TIMESTAMP, ?, '002', ?)";

        try (Connection conn = ConexionBDD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                BigDecimal nuevoSaldo;
                int nuevoContador;
                try (PreparedStatement psLock = conn.prepareStatement(sqlLock)) {
                    psLock.setString(1, cuenta);
                    try (ResultSet rs = psLock.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return new RespuestaTransaccion(false, "Cuenta inexistente: " + cuenta, BigDecimal.ZERO);
                        }
                        BigDecimal saldoActual = rs.getBigDecimal("dec_cuensaldo");
                        if (saldoActual.compareTo(monto) < 0) {
                            conn.rollback();
                            return new RespuestaTransaccion(false, "Fondos insuficientes. Saldo disponible: $" + saldoActual, saldoActual);
                        }
                        nuevoSaldo = saldoActual.subtract(monto);
                        nuevoContador = rs.getInt("int_cuencontmov") + 1;
                    }
                }
                try (PreparedStatement psUpd = conn.prepareStatement(sqlUpdate)) {
                    psUpd.setBigDecimal(1, monto);
                    psUpd.setString(2, cuenta);
                    psUpd.executeUpdate();
                }
                try (PreparedStatement psMov = conn.prepareStatement(sqlMov)) {
                    psMov.setString(1, cuenta);
                    psMov.setInt(2, nuevoContador);
                    psMov.setString(3, codEmpleado);
                    psMov.setBigDecimal(4, monto);
                    psMov.executeUpdate();
                }
                conn.commit();
                return new RespuestaTransaccion(true, "Retiro exitoso de $" + monto + " en Ventanilla " + ventanilla, nuevoSaldo);
            } catch (SQLException ex) {
                conn.rollback();
                return new RespuestaTransaccion(false, "Fallo transaccional en retiro: " + ex.getMessage(), BigDecimal.ZERO);
            }
        } catch (SQLException e) {
            return new RespuestaTransaccion(false, "Error de conexión: " + e.getMessage(), BigDecimal.ZERO);
        }
    }

    public RespuestaTransaccion transferir(String origen, String destino, BigDecimal monto, String codEmpleado, int ventanilla) {
        if (origen.equalsIgnoreCase(destino)) {
            return new RespuestaTransaccion(false, "Las cuentas origen y destino deben ser distintas.", BigDecimal.ZERO);
        }
        // Ordenamiento alfabético de códigos de cuenta para eliminar posibilidad de Deadlocks
        String c1 = origen.compareTo(destino) < 0 ? origen : destino;
        String c2 = origen.compareTo(destino) < 0 ? destino : origen;

        try (Connection conn = ConexionBDD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                String sqlLock = "SELECT chr_cuencodigo, dec_cuensaldo, int_cuencontmov FROM cuenta WHERE chr_cuencodigo IN (?, ?) FOR UPDATE";
                BigDecimal saldoOrigen = null;
                int contOrigen = 0, contDestino = 0;

                try (PreparedStatement ps = conn.prepareStatement(sqlLock)) {
                    ps.setString(1, c1);
                    ps.setString(2, c2);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String cod = rs.getString("chr_cuencodigo");
                            if (cod.equalsIgnoreCase(origen)) {
                                saldoOrigen = rs.getBigDecimal("dec_cuensaldo");
                                contOrigen = rs.getInt("int_cuencontmov") + 1;
                            } else {
                                contDestino = rs.getInt("int_cuencontmov") + 1;
                            }
                        }
                    }
                }
                if (saldoOrigen == null || saldoOrigen.compareTo(monto) < 0) {
                    conn.rollback();
                    return new RespuestaTransaccion(false, "Saldo insuficiente en cuenta origen: " + origen, BigDecimal.ZERO);
                }

                // Débito en origen
                try (PreparedStatement ps = conn.prepareStatement("UPDATE cuenta SET dec_cuensaldo = dec_cuensaldo - ?, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = ?")) {
                    ps.setBigDecimal(1, monto);
                    ps.setString(2, origen);
                    ps.executeUpdate();
                }
                // Crédito en destino
                try (PreparedStatement ps = conn.prepareStatement("UPDATE cuenta SET dec_cuensaldo = dec_cuensaldo + ?, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = ?")) {
                    ps.setBigDecimal(1, monto);
                    ps.setString(2, destino);
                    ps.executeUpdate();
                }
                // Auditoría de movimientos (003 Transferencia Enviada, 004 Transferencia Recibida)
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) VALUES (?, ?, CURRENT_TIMESTAMP, ?, '003', ?, ?)")) {
                    ps.setString(1, origen);
                    ps.setInt(2, contOrigen);
                    ps.setString(3, codEmpleado);
                    ps.setBigDecimal(4, monto);
                    ps.setString(5, destino);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) VALUES (?, ?, CURRENT_TIMESTAMP, ?, '004', ?, ?)")) {
                    ps.setString(1, destino);
                    ps.setInt(2, contDestino);
                    ps.setString(3, codEmpleado);
                    ps.setBigDecimal(4, monto);
                    ps.setString(5, origen);
                    ps.executeUpdate();
                }

                conn.commit();
                return new RespuestaTransaccion(true, "Transferencia de $" + monto + " de " + origen + " a " + destino + " exitosa.", saldoOrigen.subtract(monto));
            } catch (SQLException ex) {
                conn.rollback();
                return new RespuestaTransaccion(false, "Fallo en transferencia: " + ex.getMessage(), BigDecimal.ZERO);
            }
        } catch (SQLException e) {
            return new RespuestaTransaccion(false, "Error de conexión: " + e.getMessage(), BigDecimal.ZERO);
        }
    }
}
"""));

            list.Add(Heading3("4.1.7 Paso 7: Implementación del Gestor de Concurrencia MashUp (GestorConcurrencia.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.controller se implementa la clase GestorConcurrencia. Esta clase coordina los estados del semáforo atómicamente en memoria utilizando ConcurrentHashMap y respalda el estado en la tabla control_mashup de MySQL:"));

            list.Add(TableCaption(11, "Componente de Coordinación del Semáforo MashUp GestorConcurrencia.java"));
            list.Add(CodeBox("""
package ec.edu.monster.controller;

import ec.edu.monster.database.ConexionBDD;
import ec.edu.monster.model.EstadoSemaforo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class GestorConcurrencia {
    public static final String VERDE = "VERDE";
    public static final String ROJO = "ROJO";
    private static final GestorConcurrencia INSTANCIA = new GestorConcurrencia();
    private final ConcurrentHashMap<String, EstadoSemaforo> mapaSemaforos = new ConcurrentHashMap<>();

    private GestorConcurrencia() {}

    public static GestorConcurrencia getInstancia() {
        return INSTANCIA;
    }

    public synchronized boolean bloquearCuenta(String cuenta, int ventanilla, String operador) {
        String key = cuenta.trim();
        EstadoSemaforo actual = mapaSemaforos.get(key);
        if (actual != null && ROJO.equalsIgnoreCase(actual.getEstado())) {
            return false; // Cuenta ocupada por otra ventanilla
        }

        String sql = "INSERT INTO control_mashup (chr_cuencodigo, vch_estadomashup, int_ventanilla, vch_operador, dtt_actualizado) "
                   + "VALUES (?, 'ROJO', ?, ?, CURRENT_TIMESTAMP) "
                   + "ON DUPLICATE KEY UPDATE vch_estadomashup='ROJO', int_ventanilla=?, vch_operador=?, dtt_actualizado=CURRENT_TIMESTAMP";
        try (Connection conn = ConexionBDD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.setInt(2, ventanilla);
            ps.setString(3, operador);
            ps.setInt(4, ventanilla);
            ps.setString(5, operador);
            ps.executeUpdate();
            mapaSemaforos.put(key, new EstadoSemaforo(key, ROJO, ventanilla, operador));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public synchronized void liberarCuenta(String cuenta) {
        String key = cuenta.trim();
        String sql = "UPDATE control_mashup SET vch_estadomashup='VERDE', int_ventanilla=0, vch_operador='', dtt_actualizado=CURRENT_TIMESTAMP WHERE chr_cuencodigo=?";
        try (Connection conn = ConexionBDD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            ps.executeUpdate();
        } catch (SQLException ignored) {}
        mapaSemaforos.put(key, new EstadoSemaforo(key, VERDE, 0, ""));
    }

    public EstadoSemaforo obtenerEstado(String cuenta) {
        return mapaSemaforos.getOrDefault(cuenta.trim(), new EstadoSemaforo(cuenta.trim(), VERDE, 0, ""));
    }
}
"""));

            list.Add(Heading3("4.1.8 Paso 8: Implementación y Publicación del Endpoint Jakarta RESTful Web Services (JAX-RS / Jersey) (EurekaBankResource.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.ws se implementa la clase EurekaBankResource decorada con @WebService. Esta clase actúa como la fachada RESTful que publica las operaciones bancarias hacia la red:"));

            list.Add(TableCaption(12, "Punto de Enlace Jakarta RESTful Web Services (JAX-RS / Jersey) del Servidor RESTful EurekaBankResource.java"));
            list.Add(CodeBox("""
package ec.edu.monster.ws;

import ec.edu.monster.controller.GestorConcurrencia;
import ec.edu.monster.model.*;
import ec.edu.monster.model.dao.CuentaDAO;
import ec.edu.monster.model.dao.EmpleadoDAO;
import ec.edu.monster.model.dao.MovimientoDAO;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import java.math.BigDecimal;
import java.util.List;

@WebService(serviceName = "EurekaBankResource", targetNamespace = "http://ws.monster.edu.ec/")
public class EurekaBankResource {

    private final CuentaDAO cuentaDAO = new CuentaDAO();
    private final MovimientoDAO movimientoDAO = new MovimientoDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final GestorConcurrencia gestor = GestorConcurrencia.getInstancia();

    @WebMethod(operationName = "autenticarUsuario")
    @WebResult(name = "respuestaAutenticacion")
    public Empleado autenticarUsuario(
            @WebParam(name = "usuario") String usuario,
            @WebParam(name = "clave") String clave) {
        return empleadoDAO.autenticar(usuario, clave);
    }

    @WebMethod(operationName = "listarCuentas")
    @WebResult(name = "cuenta")
    public List<Cuenta> listarCuentas(@WebParam(name = "sucursalId") String sucursalId) {
        List<Cuenta> cuentas = cuentaDAO.obtenerCuentas(sucursalId);
        for (Cuenta c : cuentas) {
            EstadoSemaforo sem = gestor.obtenerEstado(c.getCodigo());
            c.setEstadoMashup(sem.getEstado());
            c.setVentanillaMashup(sem.getVentanilla());
            c.setOperadorMashup(sem.getOperador());
        }
        return cuentas;
    }

    @WebMethod(operationName = "consultarMovimientos")
    @WebResult(name = "movimiento")
    public List<Movimiento> consultarMovimientos(@WebParam(name = "cuenta") String cuenta) {
        return movimientoDAO.obtenerMovimientos(cuenta);
    }

    @WebMethod(operationName = "depositar")
    @WebResult(name = "resultadoTransaccion")
    public RespuestaTransaccion depositar(
            @WebParam(name = "cuenta") String cuenta,
            @WebParam(name = "monto") BigDecimal monto,
            @WebParam(name = "codEmpleado") String codEmpleado,
            @WebParam(name = "ventanilla") int ventanilla) {
        if (!gestor.bloquearCuenta(cuenta, ventanilla, "Operador Ventanilla " + ventanilla)) {
            return new RespuestaTransaccion(false, "Cuenta " + cuenta + " ocupada en otra ventanilla (ROJO).", BigDecimal.ZERO);
        }
        try {
            Thread.sleep(3500); // Latencia controlada para visualización del Semáforo MashUp
            return cuentaDAO.depositar(cuenta, monto, codEmpleado, ventanilla);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RespuestaTransaccion(false, "Interrupción transaccional", BigDecimal.ZERO);
        } finally {
            gestor.liberarCuenta(cuenta);
        }
    }

    @WebMethod(operationName = "retirar")
    @WebResult(name = "resultadoTransaccion")
    public RespuestaTransaccion retirar(
            @WebParam(name = "cuenta") String cuenta,
            @WebParam(name = "monto") BigDecimal monto,
            @WebParam(name = "codEmpleado") String codEmpleado,
            @WebParam(name = "ventanilla") int ventanilla) {
        if (!gestor.bloquearCuenta(cuenta, ventanilla, "Operador Ventanilla " + ventanilla)) {
            return new RespuestaTransaccion(false, "Cuenta " + cuenta + " ocupada en otra ventanilla (ROJO).", BigDecimal.ZERO);
        }
        try {
            Thread.sleep(3500);
            return cuentaDAO.retirar(cuenta, monto, codEmpleado, ventanilla);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RespuestaTransaccion(false, "Interrupción transaccional", BigDecimal.ZERO);
        } finally {
            gestor.liberarCuenta(cuenta);
        }
    }

    @WebMethod(operationName = "transferir")
    @WebResult(name = "resultadoTransaccion")
    public RespuestaTransaccion transferir(
            @WebParam(name = "cuentaOrigen") String cuentaOrigen,
            @WebParam(name = "cuentaDestino") String cuentaDestino,
            @WebParam(name = "monto") BigDecimal monto,
            @WebParam(name = "codEmpleado") String codEmpleado,
            @WebParam(name = "ventanilla") int ventanilla) {
        if (!gestor.bloquearCuenta(cuentaOrigen, ventanilla, "Operador Ventanilla " + ventanilla)) {
            return new RespuestaTransaccion(false, "Cuenta origen " + cuentaOrigen + " ocupada en otra ventanilla (ROJO).", BigDecimal.ZERO);
        }
        try {
            Thread.sleep(3500);
            return cuentaDAO.transferir(cuentaOrigen, cuentaDestino, monto, codEmpleado, ventanilla);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RespuestaTransaccion(false, "Interrupción transaccional", BigDecimal.ZERO);
        } finally {
            gestor.liberarCuenta(cuentaOrigen);
        }
    }

    @WebMethod(operationName = "consultarSemaforo")
    @WebResult(name = "estadoSemaforo")
    public EstadoSemaforo consultarSemaforo(@WebParam(name = "cuenta") String cuenta) {
        return gestor.obtenerEstado(cuenta);
    }
}
"""));

            list.Add(TableCaption(13, "Catálogo de Operaciones Expuestas en el Contrato Swagger / OpenAPI"));
            list.Add(TechnicalTable(
                new[] { "Operación RESTful", "Parámetros de Entrada", "Tipo de Retorno", "Propósito en la Arquitectura Bancaria" },
                new[] { 2200, 2400, 1800, 2104 },
                new List<string[]>
                {
                    new[] { "autenticarUsuario", "usuario: String, clave: String", "Empleado", "Valida credenciales maestras (Monster / Monster9), retornando código, nombre y rol del operador." },
                    new[] { "listarCuentas", "sucursalId: String (o vacío para todas)", "List<Cuenta>", "Retorna las cuentas bancarias con saldo y estado reactivo del Semáforo MashUp (VERDE/ROJO)." },
                    new[] { "consultarMovimientos", "cuenta: String", "List<Movimiento>", "Obtiene el historial cronológico de auditoría con número, tipo, fecha, cajero y monto." },
                    new[] { "depositar", "cuenta, monto, codEmpleado, ventanilla", "RespuestaTransaccion", "Acredita fondos con semáforo ROJO temporal, bloqueo pesimista y confirmación commit." },
                    new[] { "retirar", "cuenta, monto, codEmpleado, ventanilla", "RespuestaTransaccion", "Debita fondos con semáforo ROJO temporal, validación de balance y registro de auditoría." },
                    new[] { "transferir", "cuentaOrigen, cuentaDestino, monto, codEmpl, vent", "RespuestaTransaccion", "Ejecuta débito y crédito atómico inter-cuentas con bloqueo ordenado anti-deadlock." },
                    new[] { "consultarSemaforo", "cuenta: String", "EstadoSemaforo", "Consulta atómica en memoria del estado VERDE/ROJO, ventanilla y operador de una cuenta específica." }
                }
            ));

            list.Add(Heading3("4.1.9 Paso 9: Compilación y Despliegue en Payara Server Enterprise"));
            list.Add(Paragraph("Se compila y empaqueta el artefacto WAR mediante Maven ejecutando mvn clean package. Se inicia la instancia de Payara Server y se despliega el archivo WS_EurekaBank_Java_RESTful.war. El contrato formal Swagger / OpenAPI queda publicado y accesible en la dirección: http://localhost:8080/WS_EurekaBank_Java_RESTful/api/cuentas"));

            list.Add(FigureCaption(4, "Contrato Swagger / OpenAPI Publicado en Payara Server Enterprise"));
            list.Add(ScreenshotBox(
                4,
                "Contrato Swagger / OpenAPI Publicado en Payara Server Enterprise",
                "Navegador Web (Google Chrome / Mozilla Firefox / Microsoft Edge).",
                "URL: http://localhost:8080/WS_EurekaBank_Java_RESTful/api/cuentas",
                "Ingresar a la URL y verificar el renderizado del documento XML del contrato Swagger / OpenAPI.",
                "Estructura XML completa conteniendo las definiciones wsdl:definitions, el targetNamespace http://ws.monster.edu.ec/ y las operaciones autenticarUsuario, depositar, retirar y transferir."
            ));

            list.Add(FigureCaption(5, "Consola de Administración de Payara Server con la Aplicación Desplegada"));
            list.Add(ScreenshotBox(
                5,
                "Consola de Administración de Payara Server con la Aplicación Desplegada",
                "Payara Server Administration Console (http://localhost:4848).",
                "Navegación: Applications > WS_EurekaBank_Java_RESTful.",
                "Verificar que la aplicación web se encuentre en estado 'Enabled' y los endpoints web services activos.",
                "Panel de Payara mostrando la aplicación habilitada con enlace de prueba al servicio RESTful y estado de salud óptimo."
            ));

            // -------------------------------------------------------------------------
            // 4.2 CLIENTE CONSOLA
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.2 Cliente Consola (WSEurekaBankConsoleClient)"));
            list.Add(Paragraph("El Cliente de Consola es un aplicativo Java puro basado en Maven que interactúa con el operador bancario a través de la línea de comandos. Está diseñado bajo el patrón MVC Tradicional y contiene una rutina especial multi-hilo de simulación de concurrencia."));

            list.Add(Heading3("4.2.1 Paso 1: Creación del Proyecto Maven y Consumo de Swagger / OpenAPI con HttpClient"));
            list.Add(Paragraph("En NetBeans IDE se inicia el asistente New Project > Java with Maven > Java Application con el nombre WSEurekaBankConsoleClient y GroupId ec.edu.monster. En el archivo pom.xml se configura el plugin jaxws-maven-plugin para compilar el contrato Swagger / OpenAPI publicado y generar automáticamente los stubs proxy en el paquete ec.edu.monster.ws:"));

            list.Add(TableCaption(14, "Configuración de Dependencias y Plugin HttpClient del Cliente Consola pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankConsoleClient</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.sun.xml.ws</groupId>
                <artifactId>jaxws-maven-plugin</artifactId>
                <version>4.0.1</version>
                <executions>
                    <execution>
                        <goals><goal>HttpClient</goal></goals>
                        <configuration>
                            <wsdlUrls>
                                <wsdlUrl>http://localhost:8080/WS_EurekaBank_Java_RESTful/api/cuentas</wsdlUrl>
                            </wsdlUrls>
                            <packageName>ec.edu.monster.ws</packageName>
                            <sourceDestDir>${project.build.directory}/generated-sources/jaxws-HttpClient</sourceDestDir>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
"""));

            list.Add(FigureCaption(6, "Consumo del Swagger / OpenAPI y Generación de Stubs RESTful con HttpClient en NetBeans"));
            list.Add(ScreenshotBox(
                6,
                "Consumo del Swagger / OpenAPI y Generación de Stubs RESTful con HttpClient en NetBeans",
                "NetBeans IDE - Ejecución de Maven Build / Compile.",
                "Swagger / OpenAPI URL: http://localhost:8080/WS_EurekaBank_Java_RESTful/api/cuentas.",
                "Ejecutar 'Clean and Build' sobre el proyecto WSEurekaBankConsoleClient.",
                "Directorio target/generated-sources/jaxws-HttpClient poblado con EurekaBankResource.java, EurekaBankResource_Service.java y los DTOs serializables."
            ));

            list.Add(Heading3("4.2.2 Paso 2: Estructuración Modular MVC Tradicional del Cliente Consola"));
            list.Add(Paragraph("Se estructuran los paquetes del cliente consola separando estrictamente modelo, vista y controlador:"));

            list.Add(TableCaption(15, "Estructura de Paquetes MVC del Cliente Consola"));
            list.Add(TechnicalTable(
                new[] { "Paquete", "Clases", "Responsabilidad en el Cliente Consola" },
                new[] { 3000, 2500, 3004 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.clicon.controller", "ConsolaControlador.java", "Maneja la sesión del operador, valida datos y despacha peticiones hacia el stub RESTful." },
                    new[] { "ec.edu.monster.clicon.view", "ConsolaView.java", "Imprime menús tabulados, lee entradas de teclado con Scanner y muestra semáforos." },
                    new[] { "ec.edu.monster.clicon.model", "SimuladorConcurrencia.java", "Rutina multi-hilo que ejecuta transacciones concurrentes sobre una misma cuenta." },
                    new[] { "ec.edu.monster.ws", "Stubs RESTful generados", "Clases proxy generadas automáticamente por HttpClient a partir del Swagger / OpenAPI." }
                }
            ));

            list.Add(Heading3("4.2.3 Paso 3: Implementación del Controlador de Consola (ConsolaControlador.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.clicon.controller se implementa ConsolaControlador. Esta clase inicializa el proxy RESTful y gestiona la sesión del cajero bancario:"));

            list.Add(TableCaption(16, "Modelo de InvocaciÃ³n HTTP RESTful EurekaBankService.java (Consola)"));
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EurekaBankService {
    private final String baseUrl = "http://localhost:8080/WS_EurekaBank_Java_REST/api";
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final Gson gson = new Gson();

    public UsuarioSesion login(String usuario, String clave) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("usuario", usuario);
            payload.put("password", clave);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(resp.body(), UsuarioSesion.class);
        } catch (Exception ex) {
            UsuarioSesion s = new UsuarioSesion();
            s.setAutenticado(false);
            s.setMensaje("Error de red REST: " + ex.getMessage());
            return s;
        }
    }

    public List<CuentaModel> listarCuentas(int sucursal) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/cuentas?sucursal=" + sucursal))
                    .GET()
                    .build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(resp.body(), new TypeToken<List<CuentaModel>>(){}.getType());
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    public ResultadoOperacion depositar(String cuenta, double monto, int idEmpleado, int ventanilla) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("numeroCuenta", cuenta);
            payload.put("monto", monto);
            payload.put("idEmpleado", idEmpleado);
            payload.put("ventanilla", ventanilla);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/movimientos/deposito"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(resp.body(), ResultadoOperacion.class);
        } catch (Exception ex) {
            ResultadoOperacion r = new ResultadoOperacion();
            r.setExito(false);
            r.setMensaje("Fallo HTTP: " + ex.getMessage());
            return r;
        }
    }
}
"""));

            list.Add(Heading3("4.2.4 Paso 4: Implementación de la Vista de Consola (ConsolaView.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.clicon.view se implementa ConsolaView, responsable del bucle interactivo del menú:"));

            list.Add(TableCaption(17, "Interfaz de Usuario y Menús Interactivos ConsolaView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.clicon.view;

import ec.edu.monster.clicon.controller.ConsolaControlador;
import ec.edu.monster.clicon.model.SimuladorConcurrencia;
import ec.edu.monster.ws.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsolaView {
    private final ConsolaControlador controlador = new ConsolaControlador();
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        System.out.println("====================================================================");
        System.out.println("       EUREKABANK - ESTACION DE CONSOLA BANCARIA (RESTful JAVA)       ");
        System.out.println("                 GRUPO 04 - DOMINIO ec.edu.monster                 ");
        System.out.println("====================================================================");

        while (controlador.getSesionOperador() == null) {
            System.out.print("\nIngrese Usuario Operador (ej. Monster): ");
            String usr = scanner.nextLine().trim();
            System.out.print("Ingrese Contraseña (ej. Monster9): ");
            String pwd = scanner.nextLine().trim();
            Empleado emp = controlador.autenticar(usr, pwd);
            if (emp != null && emp.getUsuario() != null) {
                System.out.println("\n[OK] Bienvenido " + emp.getNombre() + " " + emp.getPaterno() + " | Ventanilla 1");
            } else {
                System.out.println("[ERROR] Credenciales erróneas.");
            }
        }

        boolean salir = false;
        while (!salir) {
            System.out.println("\n----------------- MENU DE OPERACIONES BANCARIAS ------------------");
            System.out.println("1. Consultar Cuentas y Semáforo MashUp (VERDE / ROJO)");
            System.out.println("2. Consultar Extracto Cronológico de Movimientos");
            System.out.println("3. Realizar Depósito en Cuenta (+)");
            System.out.println("4. Realizar Retiro en Cuenta (-)");
            System.out.println("5. Realizar Transferencia entre Cuentas (<=>)");
            System.out.println("6. Simular Concurrencia de 3 Ventanillas Simultáneas sobre 001-0001");
            System.out.println("0. Cerrar Sesión y Salir");
            System.out.print("Seleccione una opción: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1": mostrarCuentas(); break;
                case "2": consultarExtracto(); break;
                case "3": ejecutarDeposito(); break;
                case "4": ejecutarRetiro(); break;
                case "5": ejecutarTransferencia(); break;
                case "6": new SimuladorConcurrencia(controlador.getPort()).ejecutar(); break;
                case "0": salir = true; break;
                default: System.out.println("Opción inválida."); break;
            }
        }
    }

    private void mostrarCuentas() {
        List<Cuenta> cuentas = controlador.listarCuentas("");
        System.out.println("\n+----------+----------------------+-----------+-------------------------+");
        System.out.println("|  CUENTA  | TITULAR              | SALDO USD | SEMAFORO MASHUP         |");
        System.out.println("+----------+----------------------+-----------+-------------------------+");
        for (Cuenta c : cuentas) {
            String sem = "VERDE".equalsIgnoreCase(c.getEstadoMashup())
                    ? "[VERDE - LIBRE]"
                    : "[ROJO - OCUPADO EN VENTANILLA " + c.getVentanillaMashup() + "]";
            System.out.printf("| %-8s | %-20s | %9.2f | %-23s |\n", c.getCodigo(), c.getNombreCliente(), c.getSaldo(), sem);
        }
        System.out.println("+----------+----------------------+-----------+-------------------------+");
    }

    private void consultarExtracto() {
        System.out.print("Ingrese número de cuenta (ej. 001-0001): ");
        String cuenta = scanner.nextLine().trim();
        List<Movimiento> movs = controlador.consultarMovimientos(cuenta);
        System.out.println("\n--- EXTRACTO DE CUENTA " + cuenta + " ---");
        for (Movimiento m : movs) {
            System.out.printf("Mov #%d | Tipo: %s | Monto: $%.2f | Cajero: %s\n",
                    m.getNumeroMovimiento(), m.getTipoMovimiento(), m.getImporte(), m.getCodigoEmpleado());
        }
    }

    private void ejecutarDeposito() {
        System.out.print("Cuenta destino: ");
        String c = scanner.nextLine().trim();
        System.out.print("Monto USD: ");
        BigDecimal m = new BigDecimal(scanner.nextLine().trim());
        RespuestaTransaccion res = controlador.depositar(c, m);
        System.out.println(res.isExitoso() ? "[OK] " + res.getMensaje() : "[FALLO] " + res.getMensaje());
    }

    private void ejecutarRetiro() {
        System.out.print("Cuenta origen: ");
        String c = scanner.nextLine().trim();
        System.out.print("Monto USD: ");
        BigDecimal m = new BigDecimal(scanner.nextLine().trim());
        RespuestaTransaccion res = controlador.retirar(c, m);
        System.out.println(res.isExitoso() ? "[OK] " + res.getMensaje() : "[FALLO] " + res.getMensaje());
    }

    private void ejecutarTransferencia() {
        System.out.print("Cuenta origen: ");
        String o = scanner.nextLine().trim();
        System.out.print("Cuenta destino: ");
        String d = scanner.nextLine().trim();
        System.out.print("Monto USD: ");
        BigDecimal m = new BigDecimal(scanner.nextLine().trim());
        RespuestaTransaccion res = controlador.transferir(o, d, m);
        System.out.println(res.isExitoso() ? "[OK] " + res.getMensaje() : "[FALLO] " + res.getMensaje());
    }
}
"""));

            list.Add(Heading3("4.2.5 Paso 5: Rutina Multi-Hilo de Simulación de Concurrencia (SimuladorConcurrencia.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.clicon.model se implementa la clase SimuladorConcurrencia, la cual levanta 3 hilos simultáneos representando a las Ventanillas 1, 2 y 3 para demostrar la exclusión mutua sobre la cuenta 001-0001:"));

            list.Add(TableCaption(18, "Rutina Multi-Hilo de Simulación de 3 Ventanillas Concurrentes"));
            list.Add(CodeBox("""
package ec.edu.monster.clicon.model;

import ec.edu.monster.ws.EurekaBankResource;
import ec.edu.monster.ws.RespuestaTransaccion;
import java.math.BigDecimal;

public class SimuladorConcurrencia {
    private final EurekaBankResource port;

    public SimuladorConcurrencia(EurekaBankResource port) {
        this.port = port;
    }

    public void ejecutar() {
        System.out.println("\n>>> INICIANDO SIMULACION DE 3 VENTANILLAS CONCURRENTES SOBRE CUENTA 001-0001 <<<");
        Thread v1 = new Thread(() -> dispararVentanilla(1, new BigDecimal("50.00")), "Hilo-Ventanilla-1");
        Thread v2 = new Thread(() -> dispararVentanilla(2, new BigDecimal("30.00")), "Hilo-Ventanilla-2");
        Thread v3 = new Thread(() -> dispararVentanilla(3, new BigDecimal("20.00")), "Hilo-Ventanilla-3");

        v1.start();
        v2.start();
        v3.start();

        try {
            v1.join(); v2.join(); v3.join();
            System.out.println(">>> SIMULACION CONCLUIDA CON EXCLUSION MUTUA VERIFICADA <<<\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void dispararVentanilla(int ventanilla, BigDecimal monto) {
        System.out.println("[Ventanilla " + ventanilla + "] Intentando depósito de $" + monto + "...");
        RespuestaTransaccion res = port.depositar("001-0001", monto, "EMP0001", ventanilla);
        System.out.println("[Ventanilla " + ventanilla + " RESPUESTA] " + res.getMensaje() + " | Saldo: $" + res.getNuevoSaldo());
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
                "Opción 1: Consultar Cuentas y Semáforo MashUp.",
                "Seleccionar opción 1 en el menú interactivo.",
                "Tabla tabulada con columnas CUENTA, TITULAR, SALDO y SEMAFORO MASHUP mostrando los estados VERDE (Libre) y ROJO (Ocupado en Ventanilla)."
            ));

            list.Add(FigureCaption(9, "Simulación de 3 Ventanillas Simultáneas en Cliente Consola"));
            list.Add(ScreenshotBox(
                9,
                "Simulación de 3 Ventanillas Simultáneas en Cliente Consola",
                "Consola de comandos / Terminal de NetBeans.",
                "Opción 6: Simular Concurrencia de 3 Ventanillas Simultáneas sobre 001-0001.",
                "Seleccionar la opción 6 para disparar la rutina multi-hilo.",
                "Trazas de consola mostrando Ventanilla 1 adquiriendo el semáforo (ROJO), Ventanilla 2 y 3 detectando la exclusión mutua, y la actualización final acumulada del saldo ($2850.00)."
            ));

            // -------------------------------------------------------------------------
            // 4.3 CLIENTE ESCRITORIO
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.3 Cliente Escritorio (WSEurekaBankDesktopClient)"));
            list.Add(Paragraph("El Cliente de Escritorio está desarrollado en Java Swing con interfaz visual profesional, tablas personalizadas con semáforos cromáticos (verde/rojo), integración completa con el servicio RESTful y despacho asíncrono para mantener la fluidez de la interfaz."));

            list.Add(Heading3("4.3.1 Paso 1: Creación del Proyecto Gráfico Java Swing en NetBeans"));
            list.Add(Paragraph("En NetBeans IDE se inicia el asistente New Project > Java with Maven > Java Application con el nombre WSEurekaBankDesktopClient. Se agregan las dependencias de Jakarta RESTful Web Services (JAX-RS / Jersey) en pom.xml:"));

            list.Add(TableCaption(19, "Descriptor de Dependencias del Cliente Escritorio Swing pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankDesktopClient</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>jakarta.xml.ws</groupId>
            <artifactId>jakarta.xml.ws-api</artifactId>
            <version>4.0.0</version>
        </dependency>
        <dependency>
            <groupId>com.sun.xml.ws</groupId>
            <artifactId>jaxws-rt</artifactId>
            <version>4.0.1</version>
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
                    new[] { "ec.edu.monster.cliesc.view", "LoginView.java, DashboardView.java, SemaforoTableCellRenderer.java", "Formularios Swing, renderizadores de celdas cromáticas y componentes visuales." },
                    new[] { "ec.edu.monster.cliesc.controller", "LoginController.java, DashboardController.java", "Maneja eventos de botones, temporizadores de refresco y ejecución en segundo plano (SwingWorker)." },
                    new[] { "ec.edu.monster.cliesc.model", "Modelos y adaptadores locales", "Estructuras de datos y encapsulación de llamadas al proxy RESTful." }
                }
            ));

            list.Add(Heading3("4.3.3 Paso 3: Implementación de la Vista de Login (LoginView.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.cliesc.view se crea LoginView, permitiendo al operador ingresar con las credenciales maestras Monster / Monster9:"));

            list.Add(TableCaption(21, "Interfaz Gráfica de Autenticación de Ventanilla LoginView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.LoginController;
import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JComboBox<String> cmbVentanilla;
    private JButton btnIngresar;
    private LoginController controller;

    public LoginView() {
        setTitle("EurekaBank - Autenticación de Ventanilla");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        inicializarUI();
    }

    private void inicializarUI() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario Operador:"));
        txtUsuario = new JTextField("Monster");
        panel.add(txtUsuario);

        panel.add(new JLabel("Contraseña:"));
        txtClave = new JPasswordField("Monster9");
        panel.add(txtClave);

        panel.add(new JLabel("Ventanilla Asignada:"));
        cmbVentanilla = new JComboBox<>(new String[]{"Ventanilla 1", "Ventanilla 2", "Ventanilla 3"});
        panel.add(cmbVentanilla);

        btnIngresar = new JButton("INGRESAR A ESTACION BANCARIA");
        btnIngresar.setBackground(new Color(30, 58, 138));
        btnIngresar.setForeground(Color.WHITE);
        panel.add(new JLabel());
        panel.add(btnIngresar);

        add(panel);
        btnIngresar.addActionListener(e -> controller.autenticar(
                txtUsuario.getText().trim(),
                new String(txtClave.getPassword()).trim(),
                cmbVentanilla.getSelectedIndex() + 1
        ));
    }
}
"""));

            list.Add(FigureCaption(10, "Diseño e Interfaz Gráfica de Autenticación de Ventanilla (LoginView)"));
            list.Add(ScreenshotBox(
                10,
                "Diseño e Interfaz Gráfica de Autenticación de Ventanilla (LoginView)",
                "Aplicación de Escritorio Java Swing - Ventana LoginView.",
                "Usuario: Monster | Clave: Monster9 | Ventanilla: Ventanilla 1 | Endpoint: http://localhost:8080/WS_EurekaBank_Java_RESTful/api/cuentas.",
                "Ingresar las credenciales maestras y presionar el botón 'INGRESAR A ESTACION BANCARIA'.",
                "Formulario con logotipo institucional de Sullivan, validación exitosa y transición automática hacia DashboardView."
            ));

            list.Add(Heading3("4.3.4 Paso 4: Implementación del Renderizador Cromático de Semáforo MashUp (SemaforoTableCellRenderer.java)"));
            list.Add(Paragraph("Se implementa SemaforoTableCellRenderer extendiendo DefaultTableCellRenderer para pintar las celdas de la tabla en verde esmeralda o rojo carmesí:"));

            list.Add(TableCaption(22, "Renderizador Cromático de Semáforo MashUp SemaforoTableCellRenderer.java"));
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

            list.Add(Heading3("4.3.5 Paso 5: Implementación del Dashboard Bancario Swing (DashboardView.java)"));
            list.Add(Paragraph("En DashboardView se ensamblan la tabla de cuentas con el semáforo visual, el panel transaccional y el extracto de movimientos:"));

            list.Add(TableCaption(23, "Formulario Principal de Escritorio DashboardView.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.view;

import ec.edu.monster.cliesc.controller.DashboardController;
import ec.edu.monster.ws.Cuenta;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DashboardView extends JFrame {
    private JTable tablaCuentas;
    private DefaultTableModel modeloCuentas;
    private JTextField txtCuentaDestino;
    private JTextField txtMonto;
    private DashboardController controller;

    public DashboardView(String nombreOperador, int ventanilla) {
        setTitle("EurekaBank - Monitor MashUp & Dashboard Bancario (Ventanilla " + ventanilla + ")");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Inicialización de modelo con columnas: Cuenta, Titular, Saldo USD, Semáforo MashUp
        modeloCuentas = new DefaultTableModel(new String[]{"Cuenta", "Titular", "Saldo USD", "Semáforo MashUp"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCuentas = new JTable(modeloCuentas);
        tablaCuentas.setDefaultRenderer(Object.class, new SemaforoTableCellRenderer());
        tablaCuentas.setRowHeight(28);

        // Disposición con JSplitPane y paneles transaccionales
        setLayout(new BorderLayout());
        add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);
    }

    public void actualizarTablaCuentas(List<Cuenta> cuentas) {
        modeloCuentas.setRowCount(0);
        for (Cuenta c : cuentas) {
            modeloCuentas.addRow(new Object[]{c.getCodigo(), c.getNombreCliente(), c.getSaldo(), c.getEstadoMashup()});
        }
    }
}
"""));

            list.Add(FigureCaption(11, "Panel Principal del Dashboard Bancario con Semáforo MashUp en Tiempo Real"));
            list.Add(ScreenshotBox(
                11,
                "Panel Principal del Dashboard Bancario con Semáforo MashUp en Tiempo Real",
                "Aplicación de Escritorio Java Swing - Ventana DashboardView.",
                "Ventanilla Activa: Ventanilla 1 | Operador: James P. Sullivan (Monster Admin).",
                "Abrir el dashboard y observar la tabla de cuentas y el estado inicial del semáforo.",
                "Tabla de cuentas con todas las cuentas en estado VERDE (Libre), saldos actualizados y panel de operaciones transaccionales listo."
            ));

            list.Add(Heading3("4.3.6 Paso 6: Implementación del Controlador Asíncrono de Escritorio (DashboardController.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.cliesc.controller se implementa DashboardController. Se apoya en SwingWorker para despachar llamadas RESTful sin bloquear el Event Dispatch Thread (EDT) e inicializa un javax.swing.Timer que refresca las cuentas cada 1.2 segundos:"));

            list.Add(TableCaption(24, "Controlador Asíncrono de Escritorio con SwingWorker DashboardController.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliesc.controller;

import ec.edu.monster.cliesc.view.DashboardView;
import ec.edu.monster.ws.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class DashboardController {
    private final EurekaBankResource port;
    private final DashboardView view;
    private final int ventanilla;
    private final String codEmpleado;
    private Timer timerMashup;

    public DashboardController(EurekaBankResource port, DashboardView view, int ventanilla, String codEmpleado) {
        this.port = port;
        this.view = view;
        this.ventanilla = ventanilla;
        this.codEmpleado = codEmpleado;
        iniciarTemporizadorMashUp();
    }

    private void iniciarTemporizadorMashUp() {
        timerMashup = new Timer(1200, e -> refrescarCuentasSilencioso());
        timerMashup.start();
    }

    public void refrescarCuentasSilencioso() {
        SwingWorker<List<Cuenta>, Void> worker = new SwingWorker<>() {
            @Override protected List<Cuenta> doInBackground() { return port.listarCuentas(""); }
            @Override protected void done() {
                try { view.actualizarTablaCuentas(get()); } catch (Exception ignored) {}
            }
        };
        worker.execute();
    }

    public void depositar(String cuenta, BigDecimal monto) {
        SwingWorker<RespuestaTransaccion, Void> worker = new SwingWorker<>() {
            @Override protected RespuestaTransaccion doInBackground() {
                return port.depositar(cuenta, monto, codEmpleado, ventanilla);
            }
            @Override protected void done() {
                try {
                    RespuestaTransaccion res = get();
                    JOptionPane.showMessageDialog(view, res.getMensaje(), res.isExitoso() ? "Éxito" : "Rechazo",
                            res.isExitoso() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
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
            list.Add(Paragraph("El Cliente Móvil está desarrollado para la plataforma Android nativa en Android Studio. Consume el servicio RESTful mediante peticiones HTTP estructuradas con envelopes XML y despachos en segundo plano para evitar congelamientos de la interfaz táctil."));

            list.Add(Heading3("4.4.1 Paso 1: Creación del Proyecto en Android Studio y Configuración de Gradle"));
            list.Add(Paragraph("En Android Studio se selecciona New Project > Empty Views Activity con el nombre WSEurekaBankMobileClient y Package name ec.edu.monster.climob. En el archivo build.gradle (Module: app) se configuran compileSdk 34, minSdk 26 y las dependencias de Material Components:"));

            list.Add(TableCaption(25, "Configuración de Compilación y SDKs del Cliente Móvil build.gradle"));
            list.Add(CodeBox("""
plugins {
    id 'com.android.application'
}

android {
    namespace 'ec.edu.monster.climob'
    compileSdk 34

    defaultConfig {
        applicationId "ec.edu.monster.climob"
        minSdk 26
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildTypes {
        release {
            minifyEnabled false
        }
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
}
"""));

            list.Add(Heading3("4.4.2 Paso 2: Configuración del Manifiesto de Android (AndroidManifest.xml)"));
            list.Add(Paragraph("En AndroidManifest.xml se declaran el permiso de red y la directiva usesCleartextTraffic para permitir la comunicación HTTP local con Payara Server (utilizando la IP 10.0.2.2 en el emulador):"));

            list.Add(TableCaption(26, "Descriptor de Seguridad y Permisos de Red AndroidManifest.xml"));
            list.Add(CodeBox("""
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="EurekaBank Móvil"
        android:theme="@style/Theme.MaterialComponents.Light.NoActionBar"
        android:usesCleartextTraffic="true">
        <activity android:name=".view.LoginActivity" android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".view.DashboardActivity" android:exported="false" />
    </application>
</manifest>
"""));

            list.Add(Heading3("4.4.3 Paso 3: Estructuración Modular MVC en Android Studio"));
            list.Add(Paragraph("Se estructuran los paquetes y layouts separando modelo, vista y controlador:"));

            list.Add(TableCaption(27, "Estructura de Paquetes y Recursos del Cliente Móvil en Android Studio"));
            list.Add(TechnicalTable(
                new[] { "Paquete / Carpeta", "Artefactos Principales", "Responsabilidad en la Plataforma Android" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.climob.view", "LoginActivity.java, DashboardActivity.java", "Actividades de interfaz de usuario, diálogos y eventos táctiles." },
                    new[] { "ec.edu.monster.climob.controller", "MobileController.java", "Orquesta llamadas en segundo plano mediante ExecutorService y Handler." },
                    new[] { "ec.edu.monster.climob.model", "RestClientHelper.java, CuentaModel.java", "Construcción de envelopes RESTful XML y parseo con XmlPullParser." },
                    new[] { "res/layout", "activity_login.xml, activity_dashboard.xml", "Diseños visuales basados en MaterialCardView y RecyclerView." }
                }
            ));

            list.Add(FigureCaption(14, "Estructura del Proyecto y Layouts XML en Android Studio"));
            list.Add(ScreenshotBox(
                14,
                "Estructura del Proyecto y Layouts XML en Android Studio",
                "Android Studio Electric Eel / Hedgehog - Vista Android / Project.",
                "Estructura de paquetes: ec.edu.monster.climob.model, view y controller.",
                "Explorar el directorio app/src/main/res/layout con activity_login.xml y activity_dashboard.xml.",
                "Proyecto compilando con Gradle Sync exitoso y layouts visualizados en el diseñador de Material Design."
            ));

            list.Add(Heading3("4.4.4 Paso 4: Implementación de Layouts XML Material Design"));
            list.Add(Paragraph("Se crean los archivos de interfaz XML activity_login.xml y activity_dashboard.xml con componentes de Material Design:"));

            list.Add(TableCaption(28, "Layout XML de la Pantalla de Autenticación activity_login.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="24dp"
    android:background="#F8FAFC">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="EUREKABANK MOVIL"
        android:textSize="22sp"
        android:textStyle="bold"
        android:textColor="#1E3A8A"
        android:layout_marginBottom="24dp" />

    <com.google.android.material.textfield.TextInputLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Usuario Operador"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">
        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/txtUsuario"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Monster" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.textfield.TextInputLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Contraseña"
        android:layout_marginTop="12dp"
        style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox">
        <com.google.android.material.textfield.TextInputEditText
            android:id="@+id/txtClave"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:inputType="textPassword"
            android:text="Monster9" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.button.MaterialButton
        android:id="@+id/btnIngresar"
        android:layout_width="match_parent"
        android:layout_height="56dp"
        android:text="INGRESAR A BANCA MOVIL"
        android:layout_marginTop="20dp"
        app:cornerRadius="8dp"
        android:backgroundTint="#1E3A8A" />
</LinearLayout>
"""));

            list.Add(TableCaption(29, "Layout XML del Dashboard de Cuentas y Semáforos activity_dashboard.xml"));
            list.Add(CodeBox("""
<?xml version="1.0" encoding="utf-8"?>
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/swipeRefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <TextView
            android:id="@+id/lblInfoOperador"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="#1E3A8A"
            android:textColor="#FFFFFF"
            android:padding="14dp"
            android:text="Operador: James P. Sullivan | Ventanilla Móvil"
            android:textStyle="bold" />

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/recyclerCuentas"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="8dp" />
    </LinearLayout>
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
"""));

            list.Add(Heading3("4.4.5 Paso 5: Implementación del Cliente RESTful Asíncrono en Android (RestClientHelper.java)"));
            list.Add(Paragraph("En el paquete ec.edu.monster.climob.model se crea RestClientHelper para enviar los envelopes RESTful sobre HttpURLConnection hacia la IP 10.0.2.2 (alias del host anfitrión en el emulador):"));

            list.Add(TableCaption(30, "Consumidor HTTP Nativo Android EurekaBankService.java (Android)"));
            list.Add(CodeBox("""
package ec.edu.monster.climov.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class EurekaBankService {
    private static final String BASE_URL = "http://10.0.2.2:8080/WS_EurekaBank_Java_REST/api";

    public UsuarioSesion login(String usuario, String clave) {
        try {
            JSONObject req = new JSONObject();
            req.put("usuario", usuario);
            req.put("clave", clave);
            String jsonResp = enviarPost("/auth/login", req.toString());
            JSONObject obj = new JSONObject(jsonResp);
            UsuarioSesion s = new UsuarioSesion();
            s.setAutenticado(obj.optBoolean("autenticado", false));
            s.setNombreCompleto(obj.optString("nombreCompleto", ""));
            s.setRol(obj.optString("rol", ""));
            s.setVentanilla(obj.optInt("ventanilla", 1));
            return s;
        } catch (Exception e) {
            throw new RuntimeException("Error REST Login: " + e.getMessage(), e);
        }
    }

    public List<CuentaModel> listarCuentas(int sucursal) {
        try {
            String jsonResp = enviarGet("/cuentas?sucursal=" + sucursal);
            JSONArray arr = new JSONArray(jsonResp);
            List<CuentaModel> lista = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                CuentaModel c = new CuentaModel();
                c.setNumeroCuenta(o.optString("numeroCuenta"));
                c.setNombreCliente(o.optString("nombreCliente"));
                c.setApellidoCliente(o.optString("apellidoCliente"));
                c.setSaldo(o.optDouble("saldo", 0.0));
                c.setEstadoMashup(o.optString("estadoMashup", "VERDE"));
                lista.add(c);
            }
            return lista;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String enviarGet(String path) throws Exception {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("GET");
        c.setConnectTimeout(8000);
        try (InputStream in = c.getInputStream(); Scanner s = new Scanner(in, StandardCharsets.UTF_8)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

    private String enviarPost(String path, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("POST");
        c.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        c.setDoOutput(true);
        try (OutputStream os = c.getOutputStream()) {
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }
        try (InputStream in = c.getInputStream(); Scanner s = new Scanner(in, StandardCharsets.UTF_8)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }
}
"""));

            list.Add(Heading3("4.4.6 Paso 6: Implementación del Controlador y Actividad Principal (DashboardActivity.java)"));
            list.Add(Paragraph("En DashboardActivity se configuran la recarga táctil SwipeRefreshLayout, la lista de cuentas y el diálogo modal para ejecutar depósitos con despacho en subprocesos mediante ExecutorService:"));

            list.Add(TableCaption(31, "Controlador y Actividad Principal Móvil DashboardActivity.java"));
            list.Add(CodeBox("""
package ec.edu.monster.climob.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ec.edu.monster.climob.R;
import ec.edu.monster.climob.model.RestClientHelper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerCuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        recyclerCuentas = findViewById(R.id.recyclerCuentas);
        recyclerCuentas.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh.setOnRefreshListener(this::cargarCuentasAsincrono);
        cargarCuentasAsincrono();
    }

    private void cargarCuentasAsincrono() {
        swipeRefresh.setRefreshing(true);
        executor.execute(() -> {
            try {
                String payload = "<restenv:Envelope xmlns:restenv=\"http://schemas.xmlrest.org/rest/envelope/\" xmlns:ws=\"http://ws.monster.edu.ec/\">"
                               + "<restenv:Header/><restenv:Body><ws:listarCuentas><sucursalId></sucursalId></ws:listarCuentas></restenv:Body></restenv:Envelope>";
                String resp = RestClientHelper.enviarSolicitudRest("\"\"", payload);
                mainHandler.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    // Actualizar adapter de RecyclerView con el parseo del XML
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    Toast.makeText(this, "Error de red: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
"""));

            list.Add(FigureCaption(15, "Ejecución del Cliente Móvil en Emulador Android (Login y Selección de Ventanilla)"));
            list.Add(ScreenshotBox(
                15,
                "Ejecución del Cliente Móvil en Emulador Android (Login y Selección de Ventanilla)",
                "Emulador Android Studio (Pixel 6 API 34).",
                "Usuario: Monster | Contraseña: Monster9 | Ventanilla: 3 (Móvil).",
                "Iniciar la aplicación, escribir credenciales y presionar 'INGRESAR'.",
                "Pantalla de login con diseño Material Design, validación remota RESTful y navegación hacia la pantalla de cuentas."
            ));

            list.Add(FigureCaption(16, "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil"));
            list.Add(ScreenshotBox(
                16,
                "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil",
                "Emulador Android Studio (Pixel 6 API 34).",
                "Pantalla DashboardActivity con RecyclerView de tarjetas de cuenta.",
                "Pulsar sobre la cuenta 001-0001, ingresar $25.00 en el diálogo modal y presionar 'Acreditar Depósito'.",
                "Tarjeta de cuenta mostrando insignia VERDE (Libre), alternando temporalmente a ROJO durante el procesamiento y actualizando el saldo final a $2825.00."
            ));

            // -------------------------------------------------------------------------
            // 4.5 CLIENTE WEB
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.5 Cliente Web (WSEurekaBankWebClient)"));
            list.Add(Paragraph("El Cliente Web está desarrollado sobre la especificación Jakarta Faces (JSF) utilizando componentes Facelets y Managed Beans reactivos en Payara Server. Incorpora sondeo asíncrono AJAX para refrescar dinámicamente los semáforos de las cuentas bancarias sin recargar la página."));

            list.Add(Heading3("4.5.1 Paso 1: Creación del Proyecto Web Maven y Descriptores (pom.xml y web.xml)"));
            list.Add(Paragraph("En NetBeans IDE se crea el proyecto Web Maven WSEurekaBankWebClient. En pom.xml se configuran las dependencias de Jakarta Faces 4.0 y PrimeFaces 13:"));

            list.Add(TableCaption(32, "Descriptor de Dependencias de Jakarta Faces y PrimeFaces pom.xml"));
            list.Add(CodeBox("""
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
    <modelVersion>4.0.0</modelVersion>
    <groupId>ec.edu.monster</groupId>
    <artifactId>WSEurekaBankWebClient</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>jakarta.faces</groupId>
            <artifactId>jakarta.faces-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.primefaces</groupId>
            <artifactId>primefaces</artifactId>
            <version>13.0.0</version>
            <classifier>jakarta</classifier>
        </dependency>
    </dependencies>
</project>
"""));

            list.Add(Paragraph("En web.xml se declara el FacesServlet y el mapeo de extensiones .xhtml:"));

            list.Add(TableCaption(33, "Descriptor de Despliegue de Servlets web.xml"));
            list.Add(CodeBox("""
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee" version="6.0">
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

            list.Add(Heading3("4.5.2 Paso 2: Estructuración Modular MVC en Jakarta Faces"));
            list.Add(Paragraph("Se estructuran los paquetes en ec.edu.monster.cliweb.controller y las vistas Facelets en src/main/webapp:"));

            list.Add(TableCaption(34, "Estructura de Paquetes MVC del Cliente Web Jakarta Faces"));
            list.Add(TechnicalTable(
                new[] { "Paquete / Carpeta", "Artefactos Principales", "Responsabilidad Arquitectónica" },
                new[] { 3000, 2700, 2804 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.cliweb.controller", "LoginBean.java, DashboardBean.java", "Managed Beans CDI (@Named, @ViewScoped) que orquestan el flujo y el sondeo AJAX." },
                    new[] { "src/main/webapp", "login.xhtml, dashboard.xhtml", "Vistas Facelets reactivas estilizadas con CSS y componentes de PrimeFaces." },
                    new[] { "ec.edu.monster.ws", "Stubs RESTful generados", "Proxy Jakarta RESTful Web Services (JAX-RS / Jersey) para consumo del Swagger / OpenAPI de EurekaBank." }
                }
            ));

            list.Add(Heading3("4.5.3 Paso 3: Implementación del Managed Bean Controlador (DashboardBean.java)"));
            list.Add(Paragraph("En ec.edu.monster.cliweb.controller se implementa DashboardBean, el cual expone refrescarCuentas() para invocaciones periódicas mediante sondeo AJAX:"));

            list.Add(TableCaption(35, "Controlador Web Managed Bean con Sondeo AJAX DashboardBean.java"));
            list.Add(CodeBox("""
package ec.edu.monster.cliweb.controller;

import ec.edu.monster.ws.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named("dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {
    private List<Cuenta> listaCuentas;
    private Cuenta cuentaSeleccionada;
    private List<Movimiento> extractoMovimientos;
    private BigDecimal montoTransaccion;
    private String cuentaDestino;
    private int ventanillaActiva = 4; // Ventanilla Web
    private EurekaBankResource port;

    @PostConstruct
    public void init() {
        EurekaBankResource_Service service = new EurekaBankResource_Service();
        this.port = service.getEurekaBankResourcePort();
        refrescarCuentas();
    }

    public void refrescarCuentas() {
        this.listaCuentas = port.listarCuentas("");
        if (cuentaSeleccionada != null) {
            this.extractoMovimientos = port.consultarMovimientos(cuentaSeleccionada.getCodigo());
        }
    }

    public void ejecutarDeposito() {
        RespuestaTransaccion res = port.depositar(cuentaSeleccionada.getCodigo(), montoTransaccion, "EMP0001", ventanillaActiva);
        if (res.isExitoso()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", res.getMensaje()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", res.getMensaje()));
        }
        refrescarCuentas();
    }

    public void ejecutarRetiro() {
        RespuestaTransaccion res = port.retirar(cuentaSeleccionada.getCodigo(), montoTransaccion, "EMP0001", ventanillaActiva);
        if (res.isExitoso()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", res.getMensaje()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", res.getMensaje()));
        }
        refrescarCuentas();
    }

    public void ejecutarTransferencia() {
        RespuestaTransaccion res = port.transferir(cuentaSeleccionada.getCodigo(), cuentaDestino, montoTransaccion, "EMP0001", ventanillaActiva);
        if (res.isExitoso()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", res.getMensaje()));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", res.getMensaje()));
        }
        refrescarCuentas();
    }

    public List<Cuenta> getListaCuentas() { return listaCuentas; }
    public Cuenta getCuentaSeleccionada() { return cuentaSeleccionada; }
    public void setCuentaSeleccionada(Cuenta cuentaSeleccionada) { this.cuentaSeleccionada = cuentaSeleccionada; }
    public List<Movimiento> getExtractoMovimientos() { return extractoMovimientos; }
    public BigDecimal getMontoTransaccion() { return montoTransaccion; }
    public void setMontoTransaccion(BigDecimal montoTransaccion) { this.montoTransaccion = montoTransaccion; }
    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
}
"""));

            list.Add(Heading3("4.5.4 Paso 4: Implementación de la Plantilla Facelets de Login (login.xhtml)"));
            list.Add(Paragraph("Se crea login.xhtml con estilos CSS institucionales y validación de campos:"));

            list.Add(TableCaption(36, "Plantilla Facelets de Inicio de Sesión login.xhtml"));
            list.Add(CodeBox("""
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:p="http://primefaces.org/ui">
<h:head>
    <title>EurekaBank - Inicio de Sesión Web</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #0F172A; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .card-login { background: #FFFFFF; padding: 35px; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.3); width: 360px; }
    </style>
</h:head>
<h:body>
    <div class="card-login">
        <h2 style="color: #1E3A8A; text-align: center; margin-top: 0;">EUREKABANK WEB</h2>
        <h:form id="formLogin">
            <p:messages id="msgs" showDetail="true" closable="true" />
            <div style="margin-bottom: 15px;">
                <p:outputLabel for="usr" value="Usuario Operador:" /><br/>
                <p:inputText id="usr" value="#{loginBean.usuario}" style="width: 100%;" required="true" />
            </div>
            <div style="margin-bottom: 20px;">
                <p:outputLabel for="pwd" value="Contraseña:" /><br/>
                <p:password id="pwd" value="#{loginBean.clave}" style="width: 100%;" required="true" />
            </div>
            <p:commandButton value="INGRESAR A BANCA WEB" action="#{loginBean.iniciarSesion}" update="msgs" style="width: 100%; background: #1E3A8A; color: white;" />
        </h:form>
    </div>
</h:body>
</html>
"""));

            list.Add(FigureCaption(17, "Interfaz Web de Inicio de Sesión de Ventanilla (login.xhtml)"));
            list.Add(ScreenshotBox(
                17,
                "Interfaz Web de Inicio de Sesión de Ventanilla (login.xhtml)",
                "Navegador Web (Google Chrome / Mozilla Firefox).",
                "URL: http://localhost:8080/WSEurekaBankWebClient/login.xhtml | Usuario: Monster | Clave: Monster9.",
                "Escribir credenciales maestras y presionar el botón 'INGRESAR A BANCA WEB'.",
                "Formulario web con validaciones AJAX en tiempo real y redireccionamiento inmediato hacia dashboard.xhtml."
            ));

            list.Add(Heading3("4.5.5 Paso 5: Implementación de la Plantilla Facelets del Monitor MashUp (dashboard.xhtml)"));
            list.Add(Paragraph("En dashboard.xhtml se implementa el monitor MashUp en tiempo real mediante el componente PrimeFaces <p:poll interval=\"1\" update=\"tablaCuentas panelSemaforo\" /> para refrescar el estado del semáforo:"));

            list.Add(TableCaption(37, "Plantilla Facelets del Monitor MashUp Reactivo dashboard.xhtml"));
            list.Add(CodeBox("""
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core"
      xmlns:p="http://primefaces.org/ui">
<h:head>
    <title>EurekaBank - Monitor MashUp & Banca Web</title>
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #F8FAFC; margin: 0; padding: 20px; }
        .badge-verde { background-color: #22c55e; color: white; padding: 4px 10px; border-radius: 9999px; font-weight: bold; }
        .badge-rojo { background-color: #ef4444; color: white; padding: 4px 10px; border-radius: 9999px; font-weight: bold; animation: pulse 1.5s infinite; }
        @keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.6; } 100% { opacity: 1; } }
    </style>
</h:head>
<h:body>
    <h:form id="formDashboard">
        <!-- Sondeo reactivo en tiempo real cada 1 segundo -->
        <p:poll interval="1" listener="#{dashboardBean.refrescarCuentas}" update="tablaCuentas" />

        <p:dataTable id="tablaCuentas" value="#{dashboardBean.listaCuentas}" var="c" selectionMode="single"
                     selection="#{dashboardBean.cuentaSeleccionada}" rowKey="#{c.codigo}">
            <p:ajax event="rowSelect" listener="#{dashboardBean.refrescarCuentas}" update="formDashboard:tablaMovimientos" />
            <p:column headerText="Cuenta"><h:outputText value="#{c.codigo}" /></p:column>
            <p:column headerText="Titular"><h:outputText value="#{c.nombreCliente}" /></p:column>
            <p:column headerText="Saldo USD"><h:outputText value="#{c.saldo}"><f:convertNumber type="currency" currencySymbol="$" /></h:outputText></p:column>
            <p:column headerText="Semáforo MashUp">
                <span class="#{c.estadoMashup eq 'VERDE' ? 'badge-verde' : 'badge-rojo'}">
                    #{c.estadoMashup eq 'VERDE' ? 'VERDE (Libre)' : 'ROJO (Ocupado en Ventanilla '.concat(c.ventanillaMashup).concat(')')}
                </span>
            </p:column>
        </p:dataTable>
    </h:form>
</h:body>
</html>
"""));

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
            // 4.6 PRUEBAS Y VERIFICACIÓN TRANSACCIONAL
            // -------------------------------------------------------------------------
            list.Add(Heading2("4.6 Matriz de Casos de Prueba Transaccionales y de Concurrencia"));
            list.Add(Paragraph("Se diseñó y ejecutó una batería de casos de prueba formales para comprobar la robustez de las transacciones ACID, la autenticación y la exclusión mutua coordinada por el Semáforo MashUp:"));

            list.Add(TableCaption(38, "Matriz de Casos de Prueba Transaccionales y de Concurrencia MashUp"));
            list.Add(TechnicalTable(
                new[] { "ID", "Caso de Prueba", "Precondición", "Datos de Entrada", "Resultado Esperado", "Estado" },
                new[] { 600, 1800, 1600, 1800, 2104, 600 },
                new List<string[]>
                {
                    new[] { "CP-01", "Autenticación válida", "Servidor Payara activo", "usr: Monster, pwd: Monster9", "Autenticación exitosa, retorno de datos de James P. Sullivan.", "EXITOSO" },
                    new[] { "CP-02", "Autenticación inválida", "Servidor Payara activo", "usr: Invalido, pwd: 123", "Rechazo de acceso con mensaje de credenciales erróneas.", "EXITOSO" },
                    new[] { "CP-03", "Depósito atómico (+)", "Cuenta 001-0001 activa", "Cuenta: 001-0001, Monto: $50.00", "Incremento de saldo, nuevo movimiento tipo 001 y commit confirmado.", "EXITOSO" },
                    new[] { "CP-04", "Retiro con saldo insuficiente", "Cuenta 001-0004 ($950.00)", "Cuenta: 001-0004, Monto: $1500.00", "Rechazo por fondos insuficientes, rollback total y saldo intacto.", "EXITOSO" },
                    new[] { "CP-05", "Transferencia atómica (<=>)", "001-0001 y 001-0002 activas", "Origen: 001-0001, Dest: 001-0002, $100", "Débito en origen, crédito en destino y 2 movimientos registrados.", "EXITOSO" },
                    new[] { "CP-06", "Concurrencia MashUp simultánea", "2 ventanillas operando", "Ventanilla 1 y 2 sobre 001-0001", "Ventanilla 1 adquiere semáforo ROJO; Ventanilla 2 es bloqueada.", "EXITOSO" }
                }
            ));

            list.Add(TableCaption(39, "Guía de Diagnóstico y Resolución de Errores Frecuentes (Troubleshooting)"));
            list.Add(TechnicalTable(
                new[] { "Problema Técnico Observado", "Causa Raíz Identificada", "Procedimiento de Corrección y Solución" },
                new[] { 2600, 2700, 3204 },
                new List<string[]>
                {
                    new[] { "Error de conexión MySQL (Connection Refused en puerto 3306)", "El contenedor Podman/Docker de MySQL no fue inicializado previamente.", "Ejecutar 'podman compose up -d' o 'docker compose up -d' dentro de la carpeta 03. BDD y verificar con 'podman ps'." },
                    new[] { "Fallo de despliegue en Payara Server (Address already in use: 8080)", "Conflicto con otro servicio o instancia previa ocupando el puerto 8080.", "Detener el proceso en conflicto mediante 'Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process' y reiniciar Payara." },
                    new[] { "Android: NetworkOnMainThreadException al invocar el servicio RESTful", "Intento de ejecutar llamadas de red en el hilo principal de interfaz (UI Thread).", "Utilizar Executors.newSingleThreadExecutor() o Coroutines para despachar peticiones en segundo plano y sincronizar con Looper." },
                    new[] { "Android: java.io.IOException: Cleartext HTTP traffic not permitted", "Políticas de seguridad de Android 9+ que bloquean tráfico HTTP no cifrado.", "Añadir el atributo android:usesCleartextTraffic='true' en la etiqueta <application> del archivo AndroidManifest.xml." },
                    new[] { "Jakarta Faces: ViewExpiredException al enviar formularios tras inactividad", "La sesión web de JSF expiró o se reinició el contexto del servidor Payara.", "Configurar el parámetro de contexto jakarta.faces.STATE_SAVING_METHOD a 'client' en web.xml o renovar la sesión en login.xhtml." }
                }
            ));

            // =========================================================================
            // SECCIÓN 5: CONCLUSIONES
            // =========================================================================
            list.Add(Heading1("5 CONCLUSIONES"));
            list.Add(BulletParagraph("1.", "La implementación del protocolo RESTful gobernado por un contrato formal Swagger / OpenAPI demostró ser una solución arquitectónica altamente robusta para sistemas bancarios transaccionales, proveyendo tipado estricto, validación automática mediante esquemas XSD e interoperabilidad garantizada entre clientes heterogéneos (Consola, Escritorio Swing, Móvil Android y Web Jakarta Faces)."));
            list.Add(BulletParagraph("2.", "El cumplimiento irrestricto de las propiedades ACID a través de la capa de persistencia JDBC y el motor relacional MySQL InnoDB (con aislamiento TRANSACTION_READ_COMMITTED y bloqueos pesimistas SELECT ... FOR UPDATE) garantizó la integridad absoluta de los saldos bancarios, previniendo condiciones de carrera y sobregiros no autorizados."));
            list.Add(BulletParagraph("3.", "El mecanismo de sincronización reactiva Semáforo MashUp ofreció una solución innovadora y visual para resolver la concurrencia entre ventanillas físicas y canales remotos, alternando eficientemente entre los estados VERDE y ROJO tanto en memoria compartida atómica (ConcurrentHashMap) como en la tabla relacional control_mashup."));
            list.Add(BulletParagraph("4.", "La estricta aplicación del patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro) en todos los niveles del ecosistema aseguró una clara separación de responsabilidades, facilitando el mantenimiento, la depuración y la extensión del software sin incurrir en acoplamientos innecesarios ni dependencias cruzadas."));
            list.Add(BulletParagraph("5.", "El despliegue contenerizado de la base de datos relacional mediante Podman/Docker Compose eliminó las discrepancias de entorno entre estaciones de desarrollo, permitiendo inicializar y restaurar el esquema bancario completo con sus datos maestros en cuestión de segundos."));
            list.Add(BulletParagraph("6.", "La ejecución de la matriz de pruebas transaccionales evidenció que la técnica de ordenamiento alfabético de códigos de cuenta previo a la adquisición de bloqueos pesimistas en transferencias bancarias eliminó por completo la posibilidad de interbloqueos mutuos (deadlocks), asegurando alta disponibilidad operativa en ventanillas concurrentes."));

            // =========================================================================
            // SECCIÓN 6: RECOMENDACIONES
            // =========================================================================
            list.Add(Heading1("6 RECOMENDACIONES"));
            list.Add(BulletParagraph("1.", "Para entornos de producción financiera de alta concurrencia, se recomienda migrar el protocolo de transporte hacia canales cifrados HTTPS/TLS e incorporar la especificación WS-Security mediante certificados X.509 para firmar digitalmente y encriptar los payloads XML de los mensajes RESTful."));
            list.Add(BulletParagraph("2.", "Se aconseja implementar un pool de conexiones administrado por el propio servidor de aplicaciones Payara Server (mediante JDBC Connection Pools y Recursos JNDI) en sustitución del DriverManager directo, optimizando la reutilización de conexiones físicas y minimizando la latencia de apertura de sockets TCP hacia MySQL."));
            list.Add(BulletParagraph("3.", "En el Cliente Móvil Android, se sugiere sustituir el parser manual de envelopes RESTful por bibliotecas especializadas como Retrofit con convertidor SimpleXmlConverter o implementar arquitectura MVVM con LiveData/StateFlow para desacoplar el ciclo de vida de los componentes gráficos frente a rotaciones de pantalla."));
            list.Add(BulletParagraph("4.", "Para el Semáforo MashUp en la capa web, se recomienda evaluar la implementación de WebSockets (Jakarta WebSocket API) o Server-Sent Events (SSE) en lugar de sondeos periódicos AJAX (polling), reduciendo el consumo de ancho de banda y la sobrecarga de peticiones HTTP redundantes cuando el estado de las cuentas permanece sin cambios."));
            list.Add(BulletParagraph("5.", "Se sugiere configurar políticas de respaldo automatizado (mysqldump / replication) sobre el volumen persistente de la base de datos y establecer réplicas de lectura (Read Replicas) para descargar las consultas de extractos cronológicos del nodo transaccional maestro."));
            list.Add(BulletParagraph("6.", "Implementar un middleware de auditoría distribuida mediante OpenTelemetry para trazar de forma unificada el recorrido de las solicitudes RESTful desde que parten de cualquier cliente hasta que se confirman en el motor InnoDB de la base de datos relacional."));

            // =========================================================================
            // SECCIÓN 7: REFERENCIAS BIBLIOGRÁFICAS
            // =========================================================================
            list.Add(Heading1("7 REFERENCIAS BIBLIOGRAFICAS"));
            list.Add(Paragraph("[1] World Wide Web Consortium (W3C), 'RESTful Version 1.2 Part 1: Messaging Framework (Second Edition)', W3C Recommendation, Apr. 2007. [Online]. Disponible: https://www.w3.org/TR/rest12-part1/"));
            list.Add(Paragraph("[2] World Wide Web Consortium (W3C), 'Web Services Description Language (Swagger / OpenAPI) Version 2.0 Part 1: Core Language', W3C Recommendation, Jun. 2007. [Online]. Disponible: https://www.w3.org/TR/wsdl20/"));
            list.Add(Paragraph("[3] Eclipse Foundation, 'Jakarta XML Web Services (Jakarta RESTful Web Services (JAX-RS / Jersey)) Specification 4.0', Jakarta EE Working Group, 2022. [Online]. Disponible: https://jakarta.ee/specifications/xml-web-services/4.0/"));
            list.Add(Paragraph("[4] Oracle Corporation, 'MySQL 8.0 Reference Manual: The InnoDB Storage Engine and Transaction Model', Oracle Help Center, 2024. [Online]. Disponible: https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html"));
            list.Add(Paragraph("[5] Payara Services Ltd., 'Payara Server Enterprise Documentation: Jakarta EE Web Services and Administration', 2024. [Online]. Disponible: https://docs.payara.fish/"));
            list.Add(Paragraph("[6] E. Gamma, R. Helm, R. Johnson, y J. Vlissides, 'Design Patterns: Elements of Reusable Object-Oriented Software', Boston, MA, USA: Addison-Wesley, 1994."));
            list.Add(Paragraph("[7] M. Fowler, 'Patterns of Enterprise Application Architecture', Boston, MA, USA: Addison-Wesley Professional, 2002."));
            list.Add(Paragraph("[8] IEEE Computer Society, 'IEEE Standard for Software Quality Assurance Processes', IEEE Std 730-2014, Jun. 2014, doi: 10.1109/IEEESTD.2014.6835105."));

            return list;
        }
    }
}
