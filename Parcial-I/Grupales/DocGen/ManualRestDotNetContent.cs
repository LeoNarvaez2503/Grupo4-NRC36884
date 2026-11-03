using System;
using System.Collections.Generic;
using System.Xml.Linq;
using static DocGen.DocHelpers;

namespace DocGen
{
    public static class ManualRestDotNetContent
    {
        public static List<XElement> BuildElements()
        {
            var list = new List<XElement>();

            // =========================================================================
            // SECCIÓN 1: INTRODUCCIÓN
            // =========================================================================
            list.Add(Heading1("1 INTRODUCCIÓN"));
            list.Add(Paragraph("Un servicio web fundamentado en el estilo arquitectónico RESTful (Representational State Transfer) sobre el protocolo HTTP/JSON dentro del ecosistema moderno de Microsoft .NET (.NET 10) representa una solución empresarial estandarizada, ligera, altamente escalable y de máximo rendimiento para la interoperabilidad de datos entre aplicaciones corporativas distribuidas. A diferencia de las arquitecturas rígidas y fuertemente acopladas basadas en SOAP y contratos XML formales (WSDL), una API RESTful adopta el modelo de arquitectura orientada a recursos (ROA) gobernado por los estándares nativos de la web (RFC 9110). En esta arquitectura, cada entidad del dominio se identifica mediante un URI unívoco, las operaciones se canalizan a través de verbos semánticos HTTP (GET, POST), y el intercambio de información se realiza mediante el formato estructurado JSON (JavaScript Object Notation), serializado y deserializado de forma ultrarrápida mediante System.Text.Json."));
            list.Add(Paragraph("En el ámbito financiero transaccional (Core Banking), las operaciones bancarias ejecutadas sobre cuentas corrientes y de ahorros (tales como la recepción y acreditación de depósitos en efectivo, retiros en ventanilla y transferencias interbancarias o intrabancarias) demandan el estricto cumplimiento de las propiedades ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad). Cuando múltiples ventanillas de atención física o canales de autoservicio digital interactúan de forma simultánea sobre los mismos registros de cuenta, pueden surgir graves condiciones de carrera (race conditions), lecturas sucias (dirty reads) y anomalías contables que ponen en riesgo la solidez financiera de la institución. Para prevenir de raíz estas incidencias, el presente proyecto implementa el sistema EurekaBank con soporte de bloqueo pesimista (SELECT ... FOR UPDATE) a nivel del motor relacional InnoDB en MySQL/MariaDB y un módulo reactivo de exclusión mutua denominado Semáforo MashUp de Concurrencia en tiempo real."));
            list.Add(Paragraph("El componente servidor central se hospeda de forma ligera y eficiente sobre el runtime unificado de .NET 10 utilizando ASP.NET Core Web API y el servidor web Kestrel en el puerto TCP 5140, publicando su documentación interactiva OpenAPI 3.0 mediante Swagger UI en la ruta estándar /swagger. Por su parte, la persistencia de la información bancaria se gestiona a través de un contenedor local de base de datos MySQL 8.0 orquestado mediante Podman o Docker Compose, asegurando total portabilidad, aislamiento del entorno anfitrión e inicialización determinista mediante el script relacional eureka_bank.sql."));
            list.Add(Paragraph("Para el consumo y presentación de los servicios RESTful, se aplica con máxima rigurosidad el patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro), implementando exclusivamente las capas Model, View y Controller sin capas intermedias superfluas ni frameworks de inyección externa innecesarios. Se desarrollan cuatro clientes totalmente heterogéneos bajo el lenguaje C#: un Cliente de Consola interactivo con renderizado de secuencias ANSI y simulación multi-hilo de 3 ventanillas simultáneas, un Cliente de Escritorio de alto rendimiento con Windows Forms y cuadrícula cromática DataGridView, un Cliente Móvil nativo para la plataforma Android desarrollado con .NET MAUI y un Cliente Web responsivo desarrollado sobre ASP.NET Core MVC con vistas Razor y sondeo reactivo en tiempo real (polling a 0.9 segundos). Todo el sistema opera bajo el dominio institucional unificado ec.edu.monster y respeta las credenciales maestras autorizadas (usuario: Monster, contraseña: Monster9), satisfaciendo los requerimientos de ingeniería de software establecidos para el Grupo 04 (GR04)."));

            // =========================================================================
            // SECCIÓN 2: OBJETIVOS
            // =========================================================================
            list.Add(Heading1("2 OBJETIVOS"));
            list.Add(Heading2("2.1 Objetivo General"));
            list.Add(Paragraph("Diseñar, implementar, desplegar y documentar un sistema bancario distribuido y transaccional denominado EurekaBank, fundamentado en servicios web RESTful con arquitectura orientada a recursos sobre HTTP/JSON hospedados en ASP.NET Core Kestrel mediante ASP.NET Core Web API bajo la plataforma .NET 10, con persistencia relacional ACID en MySQL sobre contenedores locales (Docker/Podman), desacoplado mediante el patrón arquitectónico MVC Tradicional puro en todos sus niveles y consumido concurrentemente por cuatro clientes heterogéneos (Consola, Escritorio Windows Forms, Móvil .NET MAUI y Web ASP.NET Core MVC) con soporte de monitoreo reactivo mediante Semáforo MashUp en tiempo real."));

            list.Add(Heading2("2.2 Objetivos Específicos"));
            list.Add(BulletParagraph("1.", "Configurar y desplegar el motor relacional de base de datos MySQL 8.0 en un contenedor local mediante Podman/Docker Compose, inicializando el esquema completo de tablas bancarias, restricciones de integridad referencial, registros de auditoría y la tabla control_mashup de concurrencia mediante el script eureka_bank.sql."));
            list.Add(BulletParagraph("2.", "Construir la capa de persistencia relacional y control transaccional del servidor RESTful en C# (EurekaBankBddModel.cs), utilizando el driver MySqlConnector para ejecutar transacciones con aislamiento y bloqueo de filas pesimista (SELECT ... FOR UPDATE), verificando credenciales con encriptación BCrypt ($2a$10$)."));
            list.Add(BulletParagraph("3.", "Diseñar e implementar el componente Gestor de Concurrencia MashUp (ConcurrenciaMashupModel.cs), administrando la alternancia del semáforo cromático (VERDE para cuentas libres y ROJO para cuentas en operación transaccional activa en ventanilla) con persistencia directa en base de datos."));
            list.Add(BulletParagraph("4.", "Implementar y publicar la API RESTful (WS_EurekaBank_DotNet_REST) mediante ASP.NET Core Web API en .NET 10, exponiendo el controlador EurekaBankRestController con verbos semánticos HTTP ([HttpGet], [HttpPost]), códigos de respuesta semánticos (200, 400, 401, 404, 409) y documentación interactiva OpenAPI mediante Swagger UI accesible bajo el puerto 5140 y dominio institucional ec.edu.monster."));
            list.Add(BulletParagraph("5.", "Desarrollar el Cliente de Consola (WSEurekaBankConsoleClient) bajo el patrón MVC Tradicional, consumiendo el servicio RESTful mediante HttpClient y System.Text.Json, incorporando interfaces de terminal estructuradas con colores ANSI y una rutina multi-hilo de simulación de 3 ventanillas simultáneas para verificar la exclusión mutua."));
            list.Add(BulletParagraph("6.", "Construir el Cliente de Escritorio (WSEurekaBankDesktopClient) mediante Windows Forms en C#, presentando un DataGridView con colores dinámicos (verde claro para cuentas disponibles y rojo suave para cuentas ocupadas), temporizador de refresco periódico y ejecución de transacciones bancarias asíncronas con async/await."));
            list.Add(BulletParagraph("7.", "Implementar el Cliente Móvil (WSEurekaBankMobileClient) utilizando la plataforma multiplataforma .NET MAUI en C#, consumiendo el endpoint RESTful a través de HttpClient con resolución de red para emulador Android (10.0.2.2:5140/api) y presentando vistas XAML reactivas con tarjetas cromáticas de semáforo."));
            list.Add(BulletParagraph("8.", "Desarrollar el Cliente Web (WSEurekaBankWebClient) sobre ASP.NET Core MVC con vistas Razor, implementando controladores desacoplados, autenticación mediante cookies seguras y sondeo asíncrono AJAX en JavaScript cada 0.9 segundos para reflejar en tiempo real la concurrencia entre ventanillas sin recargar la página."));

            // =========================================================================
            // SECCIÓN 3: MARCO TEÓRICO
            // =========================================================================
            list.Add(Heading1("3 MARCO TEÓRICO"));
            list.Add(Heading2("3.1 Servicios Web RESTful en .NET Moderno y ASP.NET Core Web API"));
            list.Add(Paragraph("El estilo arquitectónico REST (Representational State Transfer) fue propuesto formalmente por Roy Fielding en el año 2000 como un conjunto de restricciones arquitectónicas para sistemas hipermedia distribuidos en la World Wide Web. A diferencia de los protocolos basados en llamadas a procedimientos remotos (RPC) o especificaciones pesadas basadas en XML como SOAP, REST aprovecha el propio protocolo de transporte HTTP como protocolo de aplicación. Los componentes de software interactúan manipulando representaciones de recursos (generalmente documentos JSON) mediante una interfaz uniforme estandarizada."));
            list.Add(Paragraph("En el ecosistema unificado de Microsoft .NET (.NET 6 a .NET 10), ASP.NET Core Web API constituye el marco de trabajo oficial y de alto rendimiento para el desarrollo de servicios web RESTful. Basado en el servidor web asíncrono Kestrel, ASP.NET Core Web API proporciona una arquitectura modular basada en controladores (ControllerBase) o Minimal APIs, inyección de dependencias nativa, filtrado de peticiones, enrutamiento semántico declarativo mediante atributos ([Route], [HttpGet], [HttpPost]) y serialización de alto rendimiento con System.Text.Json. Su diseño desacoplado prescinde de envolturas XML rígidas, contratos WSDL complejos y enlaces de transporte pesados, entregando una latencia mínima, bajo consumo de memoria y portabilidad total sobre entornos Windows, Linux y contenedores Docker/Podman."));

            list.Add(Heading2("3.2 Principios de la Arquitectura RESTful, Verbos HTTP y Especificación OpenAPI / Swagger"));
            list.Add(Paragraph("El diseño de una API RESTful moderna se fundamenta en seis principios o restricciones arquitectónicas esenciales:"));
            list.Add(BulletParagraph("• Separación Cliente-Servidor:", "Desacopla completamente las interfaces de usuario (clientes móviles, web, escritorio o consola) de la capa de almacenamiento y persistencia del servidor, permitiendo que ambas partes evolucionen de forma independiente."));
            list.Add(BulletParagraph("• Sin Estado (Statelessness):", "Cada petición enviada desde un cliente hacia el servidor RESTful debe contener toda la información requerida para procesarla. El servidor no mantiene estado de sesión transaccional entre llamadas sucesivas, facilitando el balanceo de carga y la escalabilidad horizontal."));
            list.Add(BulletParagraph("• Capacidad de Almacenamiento en Caché (Cacheability):", "Las respuestas HTTP deben etiquetarse implícita o explícitamente como almacenables o no almacenables en caché, optimizando la eficiencia de la red en lecturas repetitivas."));
            list.Add(BulletParagraph("• Sistema en Capas (Layered System):", "La arquitectura permite intercalar intermediarios transparentes (proxies inversos, balanceadores de carga, puertas de enlace de API) sin que el cliente requiera conocer la topología de red subyacente."));
            list.Add(BulletParagraph("• Interfaz Uniforme (Uniform Interface):", "Es la restricción central de REST que simplifica la interacción mediante cuatro pilares: identificación unívoca de recursos a través de URIs (ej. /api/cuentas, /api/movimientos/deposito), manipulación de recursos mediante representaciones estructuradas (JSON), mensajes autodescriptivos mediante encabezados Content-Type y el uso de hipermedios para guiar la navegación."));

            list.Add(Paragraph("Para la manipulación de recursos y transacciones bancarias, el sistema EurekaBank implementa verbos semánticos HTTP estandarizados:"));
            list.Add(BulletParagraph("• GET (Lectura Segura e Idempotente):", "Se emplea exclusivamente para recuperar información sin provocar efectos colaterales en la base de datos (ej. GET /api/sucursales para listar sucursales, GET /api/cuentas para listar cuentas, GET /api/cuentas/{numeroCuenta} para obtener el saldo y titular, GET /api/cuentas/{numeroCuenta}/movimientos para consultar el extracto cronológico)."));
            list.Add(BulletParagraph("• POST (Procesamiento y Creación de Transacciones):", "Se utiliza para someter operaciones financieras con mutación de estado o procesamiento de negocio mediante cargas útiles JSON en el cuerpo de la petición (ej. POST /api/auth/login para autenticación, POST /api/movimientos/deposito para acreditar depósitos, POST /api/movimientos/retiro para débitos en ventanilla y POST /api/movimientos/transferencia para traspasos entre cuentas)."));

            list.Add(Paragraph("Asimismo, el servidor RESTful comunica el resultado de cada operación mediante códigos de estado semánticos del protocolo HTTP:"));
            list.Add(BulletParagraph("• 200 OK:", "La petición fue recibida, procesada y completada exitosamente, retornando los datos solicitados o la confirmación de la transacción."));
            list.Add(BulletParagraph("• 400 Bad Request:", "La solicitud contiene parámetros no válidos, montos menores o iguales a cero, o infringe una regla de negocio bancaria (saldo insuficiente para retiro)."));
            list.Add(BulletParagraph("• 401 Unauthorized:", "Fallo de autenticación debido a credenciales de usuario o contraseña incorrectas al consultar el endpoint /api/auth/login."));
            list.Add(BulletParagraph("• 403 Forbidden:", "Acceso denegado debido a privilegios insuficientes del operador bancario."));
            list.Add(BulletParagraph("• 404 Not Found:", "El recurso bancario solicitado no existe en el sistema (ej. número de cuenta no encontrado en la base de datos)."));
            list.Add(BulletParagraph("• 409 Conflict:", "Conflicto transaccional de concurrencia. Se emite cuando una ventanilla intenta operar sobre una cuenta que se encuentra bloqueada en estado ROJO por otra ventanilla activa."));

            list.Add(Paragraph("En lugar de contratos XML rígidos (WSDL), la arquitectura RESTful documenta sus servicios mediante la Especificación OpenAPI (OAS 3.0). El paquete Swashbuckle.AspNetCore inspecciona los controladores de ASP.NET Core en tiempo de ejecución, genera automáticamente el documento de especificación JSON y expone la interfaz interactiva Swagger UI en la ruta /swagger, permitiendo a los desarrolladores explorar endpoints, esquemas JSON y ejecutar pruebas transaccionales directamente desde el navegador web."));

            list.Add(Heading2("3.3 Patrón Arquitectónico MVC Tradicional en el Ecosistema .NET"));
            list.Add(Paragraph("El patrón Modelo-Vista-Controlador (MVC Tradicional) divide la responsabilidad de un sistema de software en tres componentes fundamentales y desacoplados:"));
            list.Add(BulletParagraph("• Modelo (Model):", "Encapsula la estructura de datos del dominio, las entidades de negocio y la comunicación remota con la API RESTful (EurekaBankRestModel.cs). En la arquitectura de cliente delgado, el modelo no contiene reglas de negocio financieras ni conexión directa a bases de datos, sino que actúa como proxy cliente HTTP/JSON utilizando HttpClient y System.Text.Json. En el servidor, el modelo (EurekaBankBddModel.cs y ConcurrenciaMashupModel.cs) gestiona la persistencia relacional y el bloqueo ACID."));
            list.Add(BulletParagraph("• Vista (View):", "Es la encargada de presentar los datos de forma visual al usuario y capturar los eventos o entradas del operador (interfaces de consola ANSI, formularios de Windows Forms, páginas Razor en ASP.NET Core o páginas XAML en .NET MAUI). La vista no procesa datos ni realiza llamadas de red directamente; delega toda acción al controlador."));
            list.Add(BulletParagraph("• Controlador (Controller):", "Coordina el flujo de ejecución entre la vista y el modelo. Recibe las interacciones del operador desde la vista, valida los parámetros de entrada, invoca las operaciones del modelo y actualiza la vista con el resultado de la operación (AppControlador.cs en los clientes y EurekaBankRestController.cs en el servidor)."));
            list.Add(Paragraph("En cumplimiento estricto con las directrices de la cátedra de Ingeniería de Software, no se incorporan capas adicionales de servicios intermedios (como Application Services, Service Layers o Unit of Work externos) que desvirtúen el patrón MVC Tradicional Puro."));

            list.Add(Heading2("3.4 Concurrencia Transaccional ACID y Bloqueo Pesimista en Motores Relacionales"));
            list.Add(Paragraph("En un sistema bancario que atiende transacciones simultáneas desde múltiples ventanillas físicas y canales remotos, las propiedades ACID garantizan que el balance de las cuentas se mantenga íntegro en todo momento:"));
            list.Add(BulletParagraph("• Atomicidad:", "La transacción bancaria es una unidad indivisible: o se aplican todas sus operaciones (débito, crédito y registro en auditoría) o no se aplica ninguna, revirtiendo el estado mediante ROLLBACK."));
            list.Add(BulletParagraph("• Consistencia:", "Una transacción solo puede transformar la base de datos de un estado válido a otro estado válido, respetando las restricciones de saldo no negativo, tipos de datos y claves foráneas."));
            list.Add(BulletParagraph("• Aislamiento:", "Las operaciones concurrentes no interfieren entre sí. Para evitar la sobreescritura de balances por lecturas concurrentes obsoletas, el sistema implementa bloqueo pesimista a nivel de fila mediante sentencias SQL SELECT saldo, estado FROM cuenta WHERE numero_cuenta = @cta FOR UPDATE, bloqueando la fila hasta que la transacción confirme con COMMIT."));
            list.Add(BulletParagraph("• Durabilidad:", "Una vez confirmada la transacción mediante COMMIT, los cambios persisten de manera irreversible en el almacenamiento secundario, resistiendo caídas de energía o fallos del servidor."));

            list.Add(Heading2("3.5 Mecanismo MashUp de Monitoreo Cromático para Ventanillas"));
            list.Add(Paragraph("El Semáforo MashUp de Concurrencia es un componente visual y distribuido que refleja en tiempo real el estado operacional de cada cuenta bancaria del sistema:"));
            list.Add(BulletParagraph("• Estado VERDE (Libre):", "Indica que la cuenta bancaria está disponible para ser consultada o para iniciar una transacción desde cualquier ventanilla autorizada."));
            list.Add(BulletParagraph("• Estado ROJO (Ocupada en Ventanilla):", "Indica que un cajero u operador bancario está procesando una transacción activa de depósito, retiro o transferencia sobre la cuenta. Durante el intervalo transaccional (delimitado por un retardo simulado de 4.5 segundos para propósitos pedagógicos), ninguna otra ventanilla puede operar sobre dicha cuenta, retornando el código HTTP 409 Conflict con un mensaje de exclusión mutua."));

            list.Add(Heading2("3.6 Tecnologías de Interfaz de Usuario en C# (.NET 10)"));
            list.Add(Paragraph("Para demostrar la interoperabilidad del servicio RESTful en .NET, se implementan cuatro tecnologías de cliente heterogéneas:"));
            list.Add(BulletParagraph("• Consola Interactiva ANSI:", "Aplicación ligera de línea de comandos en C# que utiliza códigos de escape ANSI (\\x1b[32m para verde y \\x1b[31m para rojo) y tablas formateadas para operar en entornos de servidor o terminales Linux/Windows."));
            list.Add(BulletParagraph("• Windows Forms (WinForms):", "Marco de trabajo maduro y de alto rendimiento para el desarrollo de interfaces de usuario de escritorio en Windows, empleando cuadrículas DataGridView con estilos cromáticos condicionales y temporizadores System.Windows.Forms.Timer."));
            list.Add(BulletParagraph("• .NET MAUI (Multi-platform App UI):", "Plataforma moderna y unificada de Microsoft para crear aplicaciones cliente nativas en Android, iOS, macOS y Windows desde una única base de código en C#, empleando XAML para el diseño declarativo de pantallas y CollectionView para listas reactivas."));
            list.Add(BulletParagraph("• ASP.NET Core MVC (Web):", "Marco web robusto basado en el motor de renderizado Razor, complementado con peticiones asíncronas de sondeo en JavaScript (AJAX fetch cada 0.9 segundos) para actualizar el estado del semáforo cromático sin recargar la página."));

            // =========================================================================
            // SECCIÓN 4: GUÍA TÉCNICA DE INSTALACIÓN, CONFIGURACIÓN Y DESARROLLO
            // =========================================================================
            list.Add(Heading1("4 GUÍA TÉCNICA DE INSTALACIÓN, CONFIGURACIÓN Y DESARROLLO PASO A PASO"));

            // 4.1 Infraestructura de Base de Datos
            list.Add(Heading2("4.1 Configuración de la Infraestructura de Base de Datos Contenerizada"));
            list.Add(Paragraph("El almacenamiento de datos bancarios de EurekaBank se encapsula en un contenedor MySQL 8.0 gestionado mediante Podman o Docker Compose. Esta arquitectura asegura que el motor de base de datos se ejecute en un entorno reproducible y aislado, vinculando un volumen local para la persistencia física de los archivos y montando el script de inicialización SQL en el directorio /docker-entrypoint-initdb.d/ para su ejecución automática en el primer arranque."));

            list.Add(TableCaption(1, "Archivo de Orquestación de Contenedores docker-compose.yml"));
            list.Add(CodeBox("""
version: "3.9"

# Configuración de persistencia local contenerizada (Docker / Podman) - Grupo 04 (GR04)
# Contenedor activo compatible: mysql-db (puerto 3306:3306, usuario root, clave admin)
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

            list.Add(Paragraph("Para iniciar el contenedor de base de datos relacional en segundo plano, se abre una terminal de PowerShell o bash en el directorio 03. BDD y se ejecuta:"));
            list.Add(CodeBox("podman compose up -d\n# O alternativamente con Docker:\ndocker compose up -d"));

            list.Add(Paragraph("A continuación, se presenta el script SQL completo eureka_bank.sql, conteniendo la totalidad de las sentencias DDL para la creación de las tablas relacionales y las sentencias DML para la inserción de los datos semilla (seeders), incluyendo los operadores con contraseña encriptada en BCrypt para 'Monster9':"));

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
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabla: cuenta (Saldos en USD con bloqueo SELECT FOR UPDATE)
CREATE TABLE cuenta (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_sucursal INT NOT NULL,
    moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
    saldo DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    estado ENUM('Activa', 'Bloqueada') NOT NULL DEFAULT 'Activa',
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tabla: control_mashup (Semáforo de Concurrencia en Tiempo Real)
CREATE TABLE control_mashup (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    estado_mashup ENUM('VERDE', 'ROJO') NOT NULL DEFAULT 'VERDE',
    ventanilla INT DEFAULT NULL,
    bloqueado_por VARCHAR(100) DEFAULT NULL,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Tabla: movimiento (Auditoría cronológica de transacciones ACID)
CREATE TABLE movimiento (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    numero_operacion VARCHAR(50) NOT NULL UNIQUE,
    numero_cuenta VARCHAR(20) NOT NULL,
    id_empleado INT NOT NULL,
    ventanilla INT DEFAULT 1,
    tipo ENUM('Deposito', 'Retiro', 'Transferencia') NOT NULL,
    monto DECIMAL(12, 2) NOT NULL,
    saldo_resultante DECIMAL(12, 2) NOT NULL,
    cuenta_referencia VARCHAR(20) DEFAULT NULL,
    descripcion VARCHAR(200) DEFAULT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos Semilla (Seeders)
INSERT INTO sucursal (id_sucursal, codigo, nombre, ciudad, direccion) VALUES
(1, 'SUC-001', 'Matriz ESPE Sangolquí', 'Sangolquí', 'Av. General Rumiñahui s/n'),
(2, 'SUC-002', 'Sucursal Norte La Carolina', 'Quito', 'Av. Amazonas y República'),
(3, 'SUC-003', 'Sucursal Guayaquil Centro', 'Guayaquil', 'Av. 9 de Octubre y Malecón');

-- Empleados: contraseña 'Monster9' hasheada con BCrypt ($2a$10$wE99QO1U76...)
INSERT INTO empleado (id_empleado, usuario, password, nombre_completo, rol, id_sucursal, ventanilla) VALUES
(1, 'Monster',   '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Monster Admin General', 'Superadmin Monster', 1, 1),
(2, 'cajero1',   '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Cajero Ventanilla 1',   'Cajero',             1, 1),
(3, 'cajero2',   '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Cajero Ventanilla 2',   'Cajero',             1, 2),
(4, 'cajero3',   '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Cajero Ventanilla 3',   'Cajero',             2, 3),
(5, 'cajero4',   '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Cajero Ventanilla 4',   'Cajero',             3, 4),
(6, 'gerente01', '$2a$10$wE99QO1U76qQJ0yK8U4bOu8uE1Gk0u0m5o6L4V6zFf5vN2m3k7Y2S', 'Gerente Operativo ESPE', 'Gerente',            1, NULL);

INSERT INTO cliente (id_cliente, dni, nombre, apellido, telefono, email, direccion) VALUES
(1, '1710000001', 'James P.', 'Sullivan', '0991111111', 'sullivan@monster.edu.ec', 'Monsters Inc. Sector A'),
(2, '1710000002', 'Mike',     'Wazowski', '0992222222', 'mike@monster.edu.ec',     'Monsters Inc. Sector B'),
(3, '1710000003', 'Randall',  'Boggs',    '0993333333', 'randall@monster.edu.ec',  'Scream Floor 3'),
(4, '1710000004', 'Celia',    'Mae',      '0994444444', 'celia@monster.edu.ec',    'Reception Tower');

INSERT INTO cuenta (numero_cuenta, id_cliente, id_sucursal, moneda, saldo, estado) VALUES
('001-0001', 1, 1, 'USD', 2500.00, 'Activa'),
('001-0002', 2, 1, 'USD', 1800.00, 'Activa'),
('001-0003', 3, 1, 'USD', 3200.50, 'Activa'),
('001-0004', 4, 1, 'USD',  950.00, 'Activa'),
('002-0001', 1, 2, 'USD', 4100.00, 'Activa'),
('003-0001', 2, 3, 'USD', 1500.00, 'Activa');

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
                new[] { "Tabla", "Clave Primaria", "Descripción Técnica y Propósito Bancario", "Relaciones Foráneas" },
                new[] { 1600, 1500, 3904, 1500 },
                new List<string[]>
                {
                    new[] { "sucursal", "id_sucursal", "Catálogo de agencias bancarias físicas (001 Matriz ESPE, 002 Norte La Carolina, 003 Centro Financiero).", "Ninguna" },
                    new[] { "empleado", "id_empleado", "Operadores y cajeros con contraseñas encriptadas en BCrypt ($2a$10$). Incluye credencial maestra Monster/Monster9.", "id_sucursal -> sucursal" },
                    new[] { "cliente", "id_cliente", "Datos demográficos y contacto de titulares de cuentas con validación de DNI.", "Ninguna" },
                    new[] { "cuenta", "numero_cuenta", "Cuentas bancarias de ahorros/corrientes en USD con saldo DECIMAL(12,2) y bloqueo SELECT FOR UPDATE.", "id_cliente -> cliente, id_sucursal -> sucursal" },
                    new[] { "control_mashup", "numero_cuenta", "Semáforo cromático de concurrencia en tiempo real (VERDE = Libre, ROJO = Ocupada con operador y timestamp).", "numero_cuenta -> cuenta (ON DELETE CASCADE)" },
                    new[] { "movimiento", "id_movimiento", "Historial cronológico de transacciones ACID (Depósito, Retiro, Transferencia) con número de operación y ventanilla.", "numero_cuenta -> cuenta, id_empleado -> empleado" }
                }
            ));

            list.Add(FigureCaption(1, "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker"));
            list.Add(ScreenshotBox(
                1,
                "Esquema de Base de Datos Relacional EurekaBank en Contenedor Podman/Docker",
                "Consola de comandos PowerShell / Podman CLI y herramienta DBeaver o HeidiSQL.",
                "Comando de ejecución: podman compose up -d dentro del directorio 03. BDD.",
                "Ejecutar 'podman ps' para verificar el contenedor mysql-db y 'SELECT COUNT(*) FROM cuenta;' en DBeaver.",
                "Contenedor en estado Up (puerto 3306 activo) y las 6 tablas del esquema eurekabank_db pobladas con datos de prueba iniciales."
            ));

            // 4.2 Servidor ASP.NET Core Web API
            list.Add(Heading2("4.2 Creación del Proyecto Servidor RESTful ASP.NET Core Web API en .NET 10"));
            list.Add(Paragraph("Para la construcción del servidor RESTful central, se utiliza el SDK oficial de .NET 10 y el marco web ASP.NET Core Web API sobre el servidor Kestrel. Desde la terminal de comandos de PowerShell o Visual Studio 2026/2022, se genera un proyecto de tipo Web API mediante el comando:"));
            list.Add(CodeBox("dotnet new webapi -n WS_EurekaBank_DotNet_REST\ncd WS_EurekaBank_DotNet_REST"));

            list.Add(Paragraph("A continuación, se instalan los paquetes NuGet necesarios para la conectividad nativa a MySQL (MySqlConnector), validación criptográfica de contraseñas mediante BCrypt (BCrypt.Net-Next) y la generación automática de la documentación interactiva OpenAPI mediante Swagger UI (Swashbuckle.AspNetCore):"));
            list.Add(CodeBox("dotnet add package MySqlConnector --version 2.5.0\ndotnet add package BCrypt.Net-Next --version 4.0.3\ndotnet add package Swashbuckle.AspNetCore --version 6.9.0"));

            list.Add(TableCaption(4, "Descriptor de Proyecto Servidor ASP.NET Core Web API WS_EurekaBank_DotNet_REST.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WS_EurekaBank_DotNet_REST</AssemblyName>
    <NoWarn>$(NoWarn);NU1901;NU1902;NU1903;NU1904</NoWarn>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="MySqlConnector" Version="2.5.0" />
    <PackageReference Include="BCrypt.Net-Next" Version="4.0.3" />
    <PackageReference Include="Swashbuckle.AspNetCore" Version="6.9.0" />
  </ItemGroup>
</Project>
"""));

            list.Add(FigureCaption(2, "Creación y Estructura del Proyecto Servidor RESTful ASP.NET Core Web API en Visual Studio / CLI"));
            list.Add(ScreenshotBox(
                2,
                "Creación y Estructura del Proyecto Servidor RESTful ASP.NET Core Web API en Visual Studio / CLI",
                "Visual Studio 2026 / Visual Studio 2022 Community o terminal PowerShell con .NET 10 SDK.",
                "Creación del proyecto ASP.NET Core Web API 'WS_EurekaBank_DotNet_REST' agregando los paquetes NuGet correspondientes.",
                "Verificar la instalación de paquetes con 'dotnet list package' y la compilación exitosa mediante 'dotnet build'.",
                "Compilación limpia sin errores ni advertencias, generando el ensamblado WS_EurekaBank_DotNet_REST.dll en net10.0."
            ));

            // 4.3 Organización Modular MVC
            list.Add(Heading2("4.3 Organización Modular de Namespaces del Servidor (MVC Tradicional)"));
            list.Add(Paragraph("En estricta observancia del patrón arquitectónico MVC Tradicional Puro, el proyecto servidor organiza su código fuente bajo el espacio de nombres institucional ec.edu.monster en tres capas fundamentales:"));

            list.Add(TableCaption(5, "Estructura de Namespaces y Responsabilidades del Servidor RESTful (MVC Tradicional)"));
            list.Add(TechnicalTable(
                new[] { "Namespace", "Archivo Principal", "Responsabilidad en el Patrón MVC Tradicional" },
                new[] { 2200, 2400, 3904 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.model", "EurekaBankBddModel.cs", "Modelo relacional ACID: conexión MySQL, bloqueo SELECT FOR UPDATE, transacciones y BCrypt." },
                    new[] { "ec.edu.monster.model", "ConcurrenciaMashupModel.cs", "Gestión de exclusión mutua y persistencia del semáforo MashUp (VERDE / ROJO) en base de datos." },
                    new[] { "ec.edu.monster.model", "ModelosDominio.cs", "Entidades de dominio para serialización JSON: CuentaModel, MovimientoModel, OperacionModel, UsuarioModel." },
                    new[] { "ec.edu.monster.model", "RestPeticionesModel.cs", "DTOs de peticiones RESTful: LoginRestRequest, TransaccionRestRequest, TransferenciaRestRequest, BloqueoRestRequest." },
                    new[] { "ec.edu.monster.controller", "EurekaBankRestController.cs", "Controlador RESTful (ControllerBase): mapea verbos semánticos HTTP ([HttpGet], [HttpPost]) y códigos de estado." },
                    new[] { "ec.edu.monster.view", "EstadoServidorView.cs", "Vista web HTML generada en el endpoint raíz / para diagnóstico y monitoreo de estado en Kestrel." }
                }
            ));

            list.Add(FigureCaption(3, "Explorador de Soluciones y Organización Modular MVC del Servidor en Visual Studio"));
            list.Add(ScreenshotBox(
                3,
                "Explorador de Soluciones y Organización Modular MVC del Servidor en Visual Studio",
                "Entorno de Desarrollo Visual Studio 2026 / 2022 Community.",
                "Ventana 'Explorador de soluciones' con el proyecto WS_EurekaBank_DotNet_REST expandido.",
                "Desplegar las carpetas ec.edu.monster.controller, ec.edu.monster.model y ec.edu.monster.view.",
                "Visualización ordenada de los namespaces con todos los archivos .cs compilados sin errores."
            ));

            // 4.4 Modelos de Dominio y Persistencia
            list.Add(Heading2("4.4 Modelos de Dominio y Capa de Persistencia Relacional"));
            list.Add(Paragraph("Los modelos de datos representan las entidades del dominio bancario y los DTOs de solicitud para las peticiones RESTful. Al utilizar ASP.NET Core Web API, estas clases se serializan y deserializan de forma transparente a objetos JSON utilizando System.Text.Json:"));

            list.Add(TableCaption(6, "Modelos de Dominio y DTOs de Peticiones RESTful ModelosDominio.cs y RestPeticionesModel.cs"));
            list.Add(CodeBox("""
namespace ec.edu.monster.model
{
    public class UsuarioModel
    {
        public bool Autenticado { get; set; }
        public int IdEmpleado { get; set; }
        public string Usuario { get; set; } = string.Empty;
        public string NombreCompleto { get; set; } = string.Empty;
        public string Rol { get; set; } = string.Empty;
        public int IdSucursal { get; set; }
        public int Ventanilla { get; set; }
        public string Token { get; set; } = string.Empty;
        public string Mensaje { get; set; } = string.Empty;
    }

    public class CuentaModel
    {
        public string NumeroCuenta { get; set; } = string.Empty;
        public string Moneda { get; set; } = "USD";
        public double Saldo { get; set; }
        public string EstadoCuenta { get; set; } = "Activa";
        public int IdCliente { get; set; }
        public string DniCliente { get; set; } = string.Empty;
        public string NombreCliente { get; set; } = string.Empty;
        public string ApellidoCliente { get; set; } = string.Empty;
        public string TelefonoCliente { get; set; } = string.Empty;
        public string EmailCliente { get; set; } = string.Empty;
        public int IdSucursal { get; set; }
        public string NombreSucursal { get; set; } = string.Empty;
        public string EstadoMashup { get; set; } = "VERDE";
        public int VentanillaOcupada { get; set; }
        public string BloqueadoPor { get; set; } = string.Empty;

        public string TitularCompleto => $"{NombreCliente} {ApellidoCliente}".Trim();
    }

    public class OperacionModel
    {
        public bool Exito { get; set; }
        public string Codigo { get; set; } = string.Empty;
        public string Mensaje { get; set; } = string.Empty;
        public string NumeroOperacion { get; set; } = string.Empty;
        public string NumeroCuenta { get; set; } = string.Empty;
        public double SaldoAnterior { get; set; }
        public double MontoOperacion { get; set; }
        public double SaldoActual { get; set; }
        public string EstadoMashup { get; set; } = "VERDE";
        public int Ventanilla { get; set; }
        public string FechaHora { get; set; } = string.Empty;

        public static OperacionModel Error(string codigo, string mensaje, string estadoMashup = "VERDE", int ventanilla = 0) =>
            new() { Exito = false, Codigo = codigo, Mensaje = mensaje, EstadoMashup = estadoMashup, Ventanilla = ventanilla, FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") };
    }

    // DTOs de Entrada para Endpoints RESTful
    public class LoginRestRequest
    {
        public string Usuario { get; set; } = string.Empty;
        public string Clave { get; set; } = string.Empty;
    }

    public class TransaccionRestRequest
    {
        public string NumeroCuenta { get; set; } = string.Empty;
        public double Monto { get; set; }
        public int IdEmpleado { get; set; } = 1;
        public int Ventanilla { get; set; } = 1;
    }

    public class TransferenciaRestRequest
    {
        public string CuentaOrigen { get; set; } = string.Empty;
        public string CuentaDestino { get; set; } = string.Empty;
        public double Monto { get; set; }
        public int IdEmpleado { get; set; } = 1;
        public int Ventanilla { get; set; } = 1;
    }

    public class BloqueoRestRequest
    {
        public int Ventanilla { get; set; } = 1;
        public string Usuario { get; set; } = "Monster";
    }
}
"""));

            list.Add(Paragraph("La clase EurekaBankBddModel.cs encapsula toda la lógica de persistencia ACID mediante el driver MySqlConnector. Implementa transacciones con MySqlTransaction y bloqueo pesimista en sentencias críticas:"));

            list.Add(TableCaption(7, "Capa de Persistencia Relacional y Transacciones ACID EurekaBankBddModel.cs"));
            list.Add(CodeBox("""
using System.Data;
using MySqlConnector;

namespace ec.edu.monster.model
{
    public class EurekaBankBddModel
    {
        public static string ConnectionString { get; set; } =
            "Server=127.0.0.1;Port=3306;Database=eurekabank_db;User ID=root;Password=admin;SslMode=None;AllowPublicKeyRetrieval=True;";
        public static int LockDelayMs { get; set; } = 4500;

        public UsuarioModel AutenticarUsuario(string usuario, string clave)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    SELECT id_empleado, usuario, password, nombre_completo, rol, id_sucursal, COALESCE(ventanilla, 1)
                    FROM empleado WHERE LOWER(usuario) = LOWER(@usr) LIMIT 1;";
                using var cmd = new MySqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@usr", usuario.Trim());
                using var rd = cmd.ExecuteReader();
                if (!rd.Read())
                    return new UsuarioModel { Autenticado = false, Mensaje = "Credenciales inválidas." };

                string hash = rd.GetString(2);
                bool ok = hash.StartsWith("$2") ? BCrypt.Net.BCrypt.Verify(clave, hash) : clave == hash;
                if (!ok)
                    return new UsuarioModel { Autenticado = false, Mensaje = "Contraseña incorrecta." };

                return new UsuarioModel
                {
                    Autenticado = true,
                    IdEmpleado = rd.GetInt32(0),
                    Usuario = rd.GetString(1),
                    NombreCompleto = rd.GetString(3),
                    Rol = rd.GetString(4),
                    IdSucursal = rd.GetInt32(5),
                    Ventanilla = rd.GetInt32(6),
                    Token = $"MONSTER-REST-{Guid.NewGuid():N}",
                    Mensaje = "Autenticación exitosa en EurekaBank RESTful (.NET)."
                };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error de BDD: " + ex.Message };
            }
        }

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla)
        {
            if (monto <= 0)
                return OperacionModel.Error("MONTO_INVALIDO", "El monto a depositar debe ser mayor a cero.");

            // Adquirir bloqueo en semáforo MashUp
            if (!ConcurrenciaMashupModel.BloquearCuenta(numeroCuenta, ventanilla, $"Cajero {idEmpleado}"))
                return OperacionModel.Error("CONCURRENCIA_BLOQUEADA", $"La cuenta {numeroCuenta} está en uso (ROJO) por otra ventanilla.", "ROJO", ventanilla);

            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var tran = con.BeginTransaction(IsolationLevel.ReadCommitted);

                // Bloqueo pesimista a nivel de fila InnoDB
                const string sqlCta = "SELECT saldo, estado FROM cuenta WHERE numero_cuenta = @cta FOR UPDATE;";
                using var cmdCta = new MySqlCommand(sqlCta, con, tran);
                cmdCta.Parameters.AddWithValue("@cta", numeroCuenta);
                using var rd = cmdCta.ExecuteReader();
                if (!rd.Read())
                    return OperacionModel.Error("CUENTA_NO_EXISTE", "La cuenta bancaria no existe.");

                decimal saldoActual = rd.GetDecimal(0);
                string estado = rd.GetString(1);
                rd.Close();

                if (!string.Equals(estado, "Activa", StringComparison.OrdinalIgnoreCase))
                    return OperacionModel.Error("CUENTA_BLOQUEADA", "La cuenta se encuentra inactiva o bloqueada.");

                // Retardo pedagógico para evidenciar la exclusión mutua en tiempo real
                if (LockDelayMs > 0) Thread.Sleep(LockDelayMs);

                decimal nuevoSaldo = saldoActual + (decimal)monto;
                const string sqlUpd = "UPDATE cuenta SET saldo = @sal WHERE numero_cuenta = @cta;";
                using var cmdUpd = new MySqlCommand(sqlUpd, con, tran);
                cmdUpd.Parameters.AddWithValue("@sal", nuevoSaldo);
                cmdUpd.Parameters.AddWithValue("@cta", numeroCuenta);
                cmdUpd.ExecuteNonQuery();

                string opNum = $"DEP-{DateTimeOffset.UtcNow.ToUnixTimeSeconds()}";
                const string sqlMov = @"INSERT INTO movimiento (numero_operacion, numero_cuenta, id_empleado, ventanilla, tipo, monto, saldo_resultante, descripcion)
                                        VALUES (@op, @cta, @emp, @ven, 'Deposito', @mon, @res, 'Depósito en efectivo por ventanilla');";
                using var cmdMov = new MySqlCommand(sqlMov, con, tran);
                cmdMov.Parameters.AddWithValue("@op", opNum);
                cmdMov.Parameters.AddWithValue("@cta", numeroCuenta);
                cmdMov.Parameters.AddWithValue("@emp", idEmpleado);
                cmdMov.Parameters.AddWithValue("@ven", ventanilla);
                cmdMov.Parameters.AddWithValue("@mon", monto);
                cmdMov.Parameters.AddWithValue("@res", nuevoSaldo);
                cmdMov.ExecuteNonQuery();

                tran.Commit();
                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = "Depósito acreditado exitosamente.",
                    NumeroOperacion = opNum,
                    NumeroCuenta = numeroCuenta,
                    SaldoAnterior = (double)saldoActual,
                    MontoOperacion = monto,
                    SaldoActual = (double)nuevoSaldo,
                    Ventanilla = ventanilla,
                    EstadoMashup = "VERDE",
                    FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                };
            }
            finally
            {
                ConcurrenciaMashupModel.LiberarCuenta(numeroCuenta);
            }
        }
    }
}
"""));

            list.Add(Paragraph("El gestor de concurrencia ConcurrenciaMashupModel.cs administra el semáforo y la exclusión mutua de ventanillas bancarias:"));

            list.Add(TableCaption(8, "Gestor de Exclusión Mutua y Semáforo ConcurrenciaMashupModel.cs"));
            list.Add(CodeBox("""
using System.Collections.Concurrent;
using MySqlConnector;

namespace ec.edu.monster.model
{
    public class ConcurrenciaMashupModel
    {
        public class InfoBloqueo
        {
            public string NumeroCuenta { get; set; } = string.Empty;
            public int Ventanilla { get; set; }
            public string Usuario { get; set; } = string.Empty;
            public DateTime Inicio { get; set; } = DateTime.Now;
        }

        private static readonly ConcurrentDictionary<string, InfoBloqueo> BloqueosActivos = new(StringComparer.OrdinalIgnoreCase);
        private static readonly object SyncLock = new();
        public static string ConnectionString { get; set; } =
            "Server=127.0.0.1;Port=3306;Database=eurekabank_db;User ID=root;Password=admin;SslMode=None;AllowPublicKeyRetrieval=True;";

        public static bool BloquearCuenta(string numeroCuenta, int ventanilla, string usuario)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return false;

            lock (SyncLock)
            {
                if (BloqueosActivos.ContainsKey(cta) || EstaRojoEnBdd(cta))
                {
                    return false;
                }

                var info = new InfoBloqueo
                {
                    NumeroCuenta = cta,
                    Ventanilla = ventanilla <= 0 ? 1 : ventanilla,
                    Usuario = string.IsNullOrWhiteSpace(usuario) ? "Monster" : usuario.Trim(),
                    Inicio = DateTime.Now
                };
                BloqueosActivos[cta] = info;
                PersistirEstadoMashup(cta, "ROJO", info.Ventanilla, info.Usuario);
                return true;
            }
        }

        public static void LiberarCuenta(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return;

            lock (SyncLock)
            {
                BloqueosActivos.TryRemove(cta, out _);
                PersistirEstadoMashup(cta, "VERDE", null, null);
            }
        }

        private static bool EstaRojoEnBdd(string numeroCuenta)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                const string sql = "SELECT estado_mashup FROM control_mashup WHERE numero_cuenta = @cta LIMIT 1;";
                using var cmd = new MySqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@cta", numeroCuenta);
                var val = cmd.ExecuteScalar()?.ToString();
                return string.Equals(val, "ROJO", StringComparison.OrdinalIgnoreCase);
            }
            catch { return false; }
        }

        private static void PersistirEstadoMashup(string numeroCuenta, string estado, int? ventanilla, string? usuario)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por)
                    VALUES (@cta, @est, @ven, @usr)
                    ON DUPLICATE KEY UPDATE estado_mashup = @est, ventanilla = @ven, bloqueado_por = @usr;";
                using var cmd = new MySqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@cta", numeroCuenta);
                cmd.Parameters.AddWithValue("@est", estado);
                cmd.Parameters.AddWithValue("@ven", (object?)ventanilla ?? DBNull.Value);
                cmd.Parameters.AddWithValue("@usr", (object?)usuario ?? DBNull.Value);
                cmd.ExecuteNonQuery();
            }
            catch { }
        }
    }
}
"""));

            // 4.5 Controlador RESTful y Enrutamiento HTTP Semántico
            list.Add(Heading2("4.5 Controlador RESTful Central y Enrutamiento Semántico ASP.NET Core Web API"));
            list.Add(Paragraph("En ASP.NET Core Web API, el controlador central hereda de ControllerBase y se decora con los atributos [ApiController] y [Route(\"api\")]. Cada método mapea un verbo HTTP semántico ([HttpGet], [HttpPost]), procesando solicitudes estructuradas en formato JSON y retornando códigos de estado estandarizados (200 OK, 400 Bad Request, 401 Unauthorized, 404 Not Found, 409 Conflict):"));

            list.Add(TableCaption(9, "Controlador RESTful Central EurekaBankRestController.cs"));
            list.Add(CodeBox("""
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    /// <summary>
    /// Capa Controlador RESTful (MVC Tradicional) de EurekaBank en ASP.NET Core Web API.
    /// Retorna códigos HTTP semánticos (200 OK, 400 Bad Request, 401 Unauthorized, 404 Not Found, 409 Conflict).
    /// </summary>
    [ApiController]
    [Route("api")]
    public class EurekaBankRestController : ControllerBase
    {
        private readonly EurekaBankBddModel _modelo = new();

        [HttpPost("auth/login")]
        public IActionResult Login([FromBody] LoginRestRequest req)
        {
            var res = _modelo.AutenticarUsuario(req.Usuario, req.Clave);
            if (!res.Autenticado) return Unauthorized(res);
            return Ok(res);
        }

        [HttpGet("sucursales")]
        public IActionResult ListarSucursales() =>
            Ok(_modelo.ListarSucursales());

        [HttpGet("cuentas")]
        public IActionResult ListarCuentas([FromQuery] int sucursal = 0) =>
            Ok(_modelo.ListarCuentasPorSucursal(sucursal));

        [HttpGet("cuentas/{numeroCuenta}")]
        public IActionResult ObtenerCuenta(string numeroCuenta)
        {
            var cta = _modelo.ObtenerCuentaPorNumero(numeroCuenta);
            if (cta == null) return NotFound(OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {numeroCuenta} no existe."));
            return Ok(cta);
        }

        [HttpGet("clientes/{dni}/cuentas")]
        public IActionResult ConsultarPorCliente(string dni) =>
            Ok(_modelo.ConsultarCuentasPorCliente(dni));

        [HttpGet("cuentas/{numeroCuenta}/movimientos")]
        public IActionResult ConsultarExtracto(string numeroCuenta) =>
            Ok(_modelo.ConsultarExtracto(numeroCuenta));

        [HttpGet("cuentas/{numeroCuenta}/estado")]
        public IActionResult ConsultarEstadoMashup(string numeroCuenta)
        {
            var res = _modelo.ConsultarEstadoMashUp(numeroCuenta);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/deposito")]
        public IActionResult Depositar([FromBody] TransaccionRestRequest req)
        {
            var res = _modelo.Depositar(req.NumeroCuenta, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/retiro")]
        public IActionResult Retirar([FromBody] TransaccionRestRequest req)
        {
            var res = _modelo.Retirar(req.NumeroCuenta, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("movimientos/transferencia")]
        public IActionResult Transferir([FromBody] TransferenciaRestRequest req)
        {
            var res = _modelo.Transferir(req.CuentaOrigen, req.CuentaDestino, req.Monto, req.IdEmpleado, req.Ventanilla);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("cuentas/{numeroCuenta}/bloquear")]
        public IActionResult BloquearManual(string numeroCuenta, [FromBody] BloqueoRestRequest req)
        {
            var res = _modelo.BloquearCuentaManual(numeroCuenta, req.Ventanilla, req.Usuario);
            return ResolverCodigoHttp(res);
        }

        [HttpPost("cuentas/{numeroCuenta}/desbloquear")]
        public IActionResult Desbloquear(string numeroCuenta, [FromBody] BloqueoRestRequest req)
        {
            var res = _modelo.DesbloquearCuenta(numeroCuenta, req.Usuario);
            return ResolverCodigoHttp(res);
        }

        private IActionResult ResolverCodigoHttp(OperacionModel res)
        {
            if (res.Exito) return Ok(res);
            return res.Codigo switch
            {
                "CONCURRENCIA_BLOQUEADA" => Conflict(res), // HTTP 409 Conflict
                "CUENTA_NO_EXISTE" => NotFound(res),       // HTTP 404 Not Found
                "NO_AUTORIZADO" => StatusCode(403, res),   // HTTP 403 Forbidden
                _ => BadRequest(res)                       // HTTP 400 Bad Request
            };
        }
    }
}
"""));

            // 4.6 Configuración de Kestrel y Endpoints
            list.Add(Heading2("4.6 Configuración del Host Kestrel y Endpoints ASP.NET Core Web API"));
            list.Add(Paragraph("El archivo appsettings.json configura la URL de escucha en el puerto TCP 5140, los parámetros de conexión a MySQL y el retardo pedagógico del semáforo:"));

            list.Add(TableCaption(10, "Archivo de Configuración de Entorno appsettings.json"));
            list.Add(CodeBox("""
{
  "Logging": {
    "LogLevel": {
      "Default": "Information",
      "Microsoft.AspNetCore": "Warning"
    }
  },
  "AllowedHosts": "*",
  "Urls": "http://0.0.0.0:5140",
  "EurekaBank": {
    "ConnectionString": "Server=127.0.0.1;Port=3306;Database=eurekabank_db;User ID=root;Password=admin;SslMode=None;AllowPublicKeyRetrieval=True;",
    "LockDelayMs": 4500
  }
}
"""));

            list.Add(Paragraph("El punto de entrada Program.cs inicializa el host Kestrel, registra los controladores de ASP.NET Core Web API, habilita el explorador de endpoints, activa la generación de documentación OpenAPI con SwaggerGen, configura políticas de CORS abiertas y mapea la ruta raíz / hacia la vista diagnóstica HTML:"));

            list.Add(TableCaption(11, "Configuración de Kestrel, Swagger OpenAPI y Enrutamiento RESTful Program.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;
using ec.edu.monster.view;

var builder = WebApplication.CreateBuilder(args);

string? connStr = builder.Configuration["EurekaBank:ConnectionString"];
if (!string.IsNullOrWhiteSpace(connStr))
{
    EurekaBankBddModel.ConnectionString = connStr;
    ConcurrenciaMashupModel.ConnectionString = connStr;
}
if (int.TryParse(builder.Configuration["EurekaBank:LockDelayMs"], out int delayMs))
{
    EurekaBankBddModel.LockDelayMs = delayMs;
}

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddCors(opt => opt.AddDefaultPolicy(p => p.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod()));

var app = builder.Build();

app.UseCors();
app.UseSwagger();
app.UseSwaggerUI();
app.MapControllers();

app.MapGet("/", () =>
{
    var bdd = new EurekaBankBddModel();
    var cuentas = bdd.ListarCuentasPorSucursal(0);
    return Results.Content(EstadoServidorView.RenderizarHtml(cuentas), "text/html; charset=utf-8");
});

app.Run();
"""));

            list.Add(TableCaption(12, "Vista HTML de Diagnóstico y Estado del Servidor EstadoServidorView.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public static class EstadoServidorView
    {
        public static string RenderizarHtml(List<CuentaModel> cuentas)
        {
            int verdes = cuentas.Count(c => c.EstadoMashup != "ROJO");
            int rojas = cuentas.Count(c => c.EstadoMashup == "ROJO");

            var filas = string.Join("", cuentas.Select(c =>
            {
                string badge = c.EstadoMashup == "ROJO"
                    ? $"<span style='background:#fee2e2;color:#991b1b;padding:4px 10px;border-radius:999px;font-weight:700;'>ROJO (Ventanilla {c.VentanillaOcupada} - {c.BloqueadoPor})</span>"
                    : "<span style='background:#dcfce7;color:#166534;padding:4px 10px;border-radius:999px;font-weight:700;'>VERDE (Libre)</span>";
                return $"<tr><td><b>{c.NumeroCuenta}</b></td><td>{c.NombreCliente} {c.ApellidoCliente}</td><td>${c.Saldo:F2}</td><td>{c.NombreSucursal}</td><td>{badge}</td></tr>";
            }));

            return $@"<!DOCTYPE html>
<html lang='es'>
<head>
  <meta charset='utf-8'/>
  <meta http-equiv='refresh' content='2'/>
  <title>WS_EurekaBank_DotNet_REST - ec.edu.monster</title>
  <style>
    body {{ font-family:'Segoe UI',sans-serif; background:#0f172a; color:#f8fafc; margin:0; padding:28px; }}
    .box {{ max-width:980px; margin:auto; background:#1e293b; border:1px solid #334155; border-radius:12px; padding:24px; }}
    h1 {{ margin-top:0; color:#38bdf8; font-size:22px; }}
    a {{ color:#38bdf8; font-weight:700; }}
    table {{ width:100%; border-collapse:collapse; margin-top:16px; background:#0f172a; border-radius:8px; overflow:hidden; }}
    th, td {{ padding:10px 12px; border-bottom:1px solid #334155; text-align:left; font-size:14px; }}
    th {{ background:#334155; color:#e2e8f0; }}
  </style>
</head>
<body>
  <div class='box'>
    <h1>Servidor RESTful .NET (ASP.NET Core Web API) — EurekaBank GR04 (ec.edu.monster)</h1>
    <p>Documentación OpenAPI / Swagger UI: <a href='/swagger'>http://localhost:5140/swagger</a> | Endpoint Cuentas: <a href='/api/cuentas'>/api/cuentas</a></p>
    <p>Cuentas en VERDE: <b>{verdes}</b> | Cuentas en ROJO (Ocupadas): <b>{rojas}</b></p>
    <table>
      <thead><tr><th>Cuenta</th><th>Titular</th><th>Saldo</th><th>Sucursal</th><th>Semáforo MashUp</th></tr></thead>
      <tbody>{filas}</tbody>
    </table>
  </div>
</body>
</html>";
        }
    }
}
"""));

            list.Add(FigureCaption(4, "Contrato Swagger / OpenAPI Publicado en Kestrel (http://localhost:5140/swagger)"));
            list.Add(ScreenshotBox(
                4,
                "Contrato Swagger / OpenAPI Publicado en Kestrel (http://localhost:5140/swagger)",
                "Navegador web Google Chrome o Microsoft Edge.",
                "Navegar a la URL: http://localhost:5140/swagger con el servidor Kestrel en ejecución.",
                "Inspeccionar la interfaz Swagger UI y la especificación OpenAPI 3.0 generada por ASP.NET Core Web API.",
                "Interfaz interactiva Swagger UI renderizando la documentación de endpoints RESTful, esquemas JSON de entidades y operaciones transaccionales."
            ));

            list.Add(FigureCaption(5, "Interfaz Web de Diagnóstico del Servidor en el Puerto 5140 (http://localhost:5140/)"));
            list.Add(ScreenshotBox(
                5,
                "Interfaz Web de Diagnóstico del Servidor en el Puerto 5140 (http://localhost:5140/)",
                "Navegador web Google Chrome o Microsoft Edge.",
                "Navegar a la URL raíz del servidor: http://localhost:5140/.",
                "Verificar la tabla de cuentas bancarias y el enlace directo al Swagger / OpenAPI.",
                "Página web de diagnóstico renderizando el estado de Kestrel, el listado de cuentas con saldo inicial y los semáforos en VERDE."
            ));

            // 4.7 Desarrollo del Cliente Consola
            list.Add(Heading2("4.7 Desarrollo del Cliente de Consola (WSEurekaBankConsoleClient)"));
            list.Add(Paragraph("El Cliente Consola es una aplicación ejecutable en .NET 10 diseñada para terminales bancarias y tareas de diagnóstico. Consume el servicio RESTful mediante HttpClient y System.Text.Json sin dependencias externas pesadas, operando de forma ultrarrápida bajo el patrón MVC Tradicional:"));

            list.Add(FigureCaption(6, "Creación del Proyecto Cliente Consola en Visual Studio / CLI"));
            list.Add(ScreenshotBox(
                6,
                "Creación del Proyecto Cliente Consola en Visual Studio / CLI",
                "Terminal PowerShell con .NET 10 SDK o Visual Studio 2026/2022.",
                "Comando de creación: dotnet new console -n WSEurekaBankConsoleClient.",
                "Compilar el proyecto para verificar target framework net10.0.",
                "Proyecto creado con target framework net10.0 y dependencias estándar resueltas."
            ));

            list.Add(TableCaption(13, "Descriptor de Proyecto Cliente Consola WSEurekaBankConsoleClient.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <OutputType>Exe</OutputType>
    <TargetFramework>net10.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WSEurekaBankConsoleClient</AssemblyName>
  </PropertyGroup>
</Project>
"""));

            list.Add(TableCaption(14, "Modelo de Consumo RESTful con HttpClient EurekaBankRestModel.cs (Consola)"));
            list.Add(CodeBox("""
using System.Text;
using System.Text.Json;

namespace ec.edu.monster.model
{
    /// <summary>
    /// Capa Modelo (MVC Tradicional) de comunicación HTTP/JSON con el Servidor RESTful .NET.
    /// Cliente delgado: cero lógica de negocio ni acceso a BDD en el cliente.
    /// </summary>
    public class EurekaBankRestModel
    {
        private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(30) };
        private static readonly JsonSerializerOptions JsonOpts = new() { PropertyNameCaseInsensitive = true };

        public string EndpointUrl { get; set; } = "http://localhost:5140/api";
        private string BaseUrl => EndpointUrl.TrimEnd('/');

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                using var content = CrearJson(new { usuario, clave });
                using var resp = Http.PostAsync($"{BaseUrl}/auth/login", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<UsuarioModel>(raw, JsonOpts)
                    ?? new UsuarioModel { Autenticado = false, Mensaje = "Respuesta inválida del servidor REST." };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error conectando al servidor REST: " + ex.Message };
            }
        }

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId) =>
            GetJson($"{BaseUrl}/cuentas?sucursal={sucursalId}", new List<CuentaModel>());

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/deposito", new { numeroCuenta, monto, idEmpleado, ventanilla });

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/transferencia", new { cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla });

        private static StringContent CrearJson(object payload) =>
            new(JsonSerializer.Serialize(payload), Encoding.UTF8, "application/json");

        private static T GetJson<T>(string url, T fallback)
        {
            try
            {
                using var resp = Http.GetAsync(url).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<T>(raw, JsonOpts) ?? fallback;
            }
            catch { return fallback; }
        }

        private static OperacionModel PostOperacion(string url, object payload)
        {
            try
            {
                using var content = CrearJson(payload);
                using var resp = Http.PostAsync(url, content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<OperacionModel>(raw, JsonOpts)
                    ?? new OperacionModel { Exito = false, Codigo = $"HTTP_{(int)resp.StatusCode}", Mensaje = raw };
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Codigo = "ERROR_RED", Mensaje = "Fallo REST: " + ex.Message };
            }
        }
    }
}
"""));

            list.Add(TableCaption(15, "Controlador de Flujo de la Aplicación Consola AppControlador.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class AppControlador
    {
        private readonly EurekaBankRestModel _modelo = new();
        public UsuarioModel? SesionActual { get; private set; }
        public int VentanillaActiva { get; set; } = 1;

        public UsuarioModel IniciarSesion(string usuario, string clave)
        {
            var res = _modelo.Autenticar(usuario, clave);
            if (res.Autenticado)
            {
                SesionActual = res;
                if (res.Ventanilla >= 1 && res.Ventanilla <= 4)
                    VentanillaActiva = res.Ventanilla;
            }
            return res;
        }

        public void CerrarSesion() => SesionActual = null;
        public List<CuentaModel> ObtenerCuentasPorSucursal(int sucId) => _modelo.ListarCuentasPorSucursal(sucId);
        public OperacionModel Depositar(string cuenta, double monto) =>
            _modelo.Depositar(cuenta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);
        public OperacionModel Transferir(string origen, string destino, double monto) =>
            _modelo.Transferir(origen, destino, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);
    }
}
"""));

            list.Add(TableCaption(16, "Vista de Interfaz de Consola y Semáforo ANSI ConsolaView.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.controller;
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public class ConsolaView
    {
        private readonly AppControlador _controlador = new();

        public void Iniciar(string[] args)
        {
            Console.OutputEncoding = System.Text.Encoding.UTF8;
            Console.Clear();
            Console.WriteLine("=====================================================================");
            Console.WriteLine("  EUREKABANK - CLIENTE CONSOLA RESTful .NET 10 (GR04)  ");
            Console.WriteLine("=====================================================================");
            Console.Write("Usuario: ");
            string? usr = Console.ReadLine();
            Console.Write("Contraseña: ");
            string? pwd = Console.ReadLine();

            var login = _controlador.IniciarSesion(usr ?? "", pwd ?? "");
            if (!login.Autenticado)
            {
                Console.WriteLine("\u001b[31m[ERROR] " + login.Mensaje + "\u001b[0m");
                return;
            }

            Console.WriteLine($"\u001b[32m[OK] Bienvenido {login.NombreCompleto} ({login.Rol}) | Ventanilla: {_controlador.VentanillaActiva}\u001b[0m");
            MostrarMenuPrincipal();
        }

        private void MostrarMenuPrincipal()
        {
            while (true)
            {
                Console.WriteLine("\n--- MENU TRANSACCIONAL RESTful ---");
                Console.WriteLine("1. Listar Cuentas Bancarias (Semáforo MashUp)");
                Console.WriteLine("2. Realizar Depósito en Efectivo");
                Console.WriteLine("3. Realizar Transferencia entre Cuentas");
                Console.WriteLine("4. Simulación Concurrente de 3 Ventanillas");
                Console.WriteLine("5. Salir");
                Console.Write("Seleccione opción: ");
                string? opc = Console.ReadLine();
                if (opc == "5") break;

                switch (opc)
                {
                    case "1": ListarCuentas(); break;
                    case "2": RealizarDeposito(); break;
                    case "4": SimularVentanillasConcurrentes(); break;
                }
            }
        }

        private void ListarCuentas()
        {
            var cuentas = _controlador.ObtenerCuentasPorSucursal(0);
            Console.WriteLine("\n----------------------------------------------------------------------");
            Console.WriteLine("CUENTA    | TITULAR                 | SALDO      | ESTADO SEMAFORO");
            Console.WriteLine("----------------------------------------------------------------------");
            foreach (var c in cuentas)
            {
                string tag = c.EstadoMashup == "ROJO"
                    ? $"\u001b[31mROJO (Vent. {c.VentanillaOcupada})\u001b[0m"
                    : "\u001b[32mVERDE (Libre)\u001b[0m";
                Console.WriteLine($"{c.NumeroCuenta,-9} | {c.TitularCompleto,-23} | ${c.Saldo,9:F2} | {tag}");
            }
        }

        private void RealizarDeposito()
        {
            Console.Write("Número de Cuenta: ");
            string cta = Console.ReadLine() ?? "";
            Console.Write("Monto USD: ");
            double.TryParse(Console.ReadLine(), out double monto);

            var res = _controlador.Depositar(cta, monto);
            if (res.Exito)
                Console.WriteLine($"\u001b[32m[EXITO] Operación: {res.NumeroOperacion} | Nuevo Saldo: ${res.SaldoActual:F2}\u001b[0m");
            else
                Console.WriteLine($"\u001b[31m[FALLO] {res.Mensaje}\u001b[0m");
        }

        private void SimularVentanillasConcurrentes()
        {
            Console.WriteLine("\n[SIMULACION] Lanzando 3 ventanillas simultáneas sobre la cuenta 001-0001...");
            Parallel.For(1, 4, v =>
            {
                var ctrl = new AppControlador();
                ctrl.VentanillaActiva = v;
                var res = ctrl.Depositar("001-0001", 10.0 * v);
                Console.WriteLine($"Ventanilla {v}: {(res.Exito ? "PROCESADA" : "BLOQUEADA (ROJO) - " + res.Mensaje)}");
            });
        }
    }
}
"""));

            list.Add(TableCaption(17, "Punto de Entrada del Cliente Consola Program.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.view;

var vista = new ConsolaView();
vista.Iniciar(args);
"""));

            list.Add(FigureCaption(7, "Ejecución del Cliente Consola e Inicio de Sesión de Operador (Monster)"));
            list.Add(ScreenshotBox(
                7,
                "Ejecución del Cliente Consola e Inicio de Sesión de Operador (Monster)",
                "Consola de comandos PowerShell o Terminal de Windows.",
                "Ejecutar 'dotnet run' dentro de 04. CLICON/WSEurekaBankConsoleClient.",
                "Ingresar credenciales: Usuario 'Monster' y Contraseña 'Monster9'.",
                "Autenticación exitosa mostrando mensaje de bienvenida con el nombre del operador y rol Superadmin Monster."
            ));

            list.Add(FigureCaption(8, "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola"));
            list.Add(ScreenshotBox(
                8,
                "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola",
                "Consola de comandos en ejecución.",
                "Seleccionar la opción 1 del menú principal 'Listar Cuentas Bancarias'.",
                "Observar la salida formateada con secuencias de escape ANSI.",
                "Tabla con las cuentas (001-0001 a 003-0001) mostrando titulares, saldos en USD y estado VERDE (Libre)."
            ));

            list.Add(FigureCaption(9, "Simulación de 3 Ventanillas Simultáneas en Cliente Consola"));
            list.Add(ScreenshotBox(
                9,
                "Simulación de 3 Ventanillas Simultáneas en Cliente Consola",
                "Consola de comandos en ejecución.",
                "Seleccionar la opción 4 del menú 'Simulación Concurrente de 3 Ventanillas'.",
                "Disparar peticiones simultáneas sobre la cuenta 001-0001.",
                "Una ventanilla adquiere el bloqueo (estado ROJO) y procesa la transacción, mientras las otras dos reciben mensaje de exclusión mutua."
            ));

            // 4.8 Desarrollo del Cliente Escritorio Windows Forms
            list.Add(Heading2("4.8 Desarrollo del Cliente de Escritorio (WSEurekaBankDesktopClient)"));
            list.Add(Paragraph("El Cliente de Escritorio se desarrolla sobre Windows Forms en .NET 10 (TargetFramework: net10.0-windows). Incorpora formularios interactivos, validaciones numéricas, un DataGridView con renderizado de colores en tiempo real y temporizadores asíncronos para refresco periódico, consumiendo el servicio RESTful mediante HttpClient:"));

            list.Add(TableCaption(18, "Descriptor de Proyecto Cliente Escritorio WSEurekaBankDesktopClient.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <OutputType>WinExe</OutputType>
    <TargetFramework>net10.0-windows</TargetFramework>
    <Nullable>enable</Nullable>
    <UseWindowsForms>true</UseWindowsForms>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WSEurekaBankDesktopClient</AssemblyName>
  </PropertyGroup>
  <ItemGroup>
    <None Update="images\**\*">
      <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
    </None>
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(19, "Modelo de Consumo RESTful en Cliente Escritorio EurekaBankRestModel.cs (WinForms)"));
            list.Add(CodeBox("""
using System.Text;
using System.Text.Json;

namespace ec.edu.monster.model
{
    public class EurekaBankRestModel
    {
        private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(30) };
        private static readonly JsonSerializerOptions JsonOpts = new() { PropertyNameCaseInsensitive = true };

        public string EndpointUrl { get; set; } = "http://localhost:5140/api";
        private string BaseUrl => EndpointUrl.TrimEnd('/');

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(new { usuario, clave }), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync($"{BaseUrl}/auth/login", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<UsuarioModel>(raw, JsonOpts) ?? new UsuarioModel { Autenticado = false, Mensaje = "Respuesta inválida." };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error conectando a REST: " + ex.Message };
            }
        }

        public List<SucursalModel> ListarSucursales() => GetJson($"{BaseUrl}/sucursales", new List<SucursalModel>());
        public List<CuentaModel> ListarCuentasPorSucursal(int sucId) => GetJson($"{BaseUrl}/cuentas?sucursal={sucId}", new List<CuentaModel>());

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/deposito", new { numeroCuenta, monto, idEmpleado, ventanilla });

        public OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla) =>
            PostOperacion($"{BaseUrl}/movimientos/transferencia", new { cuentaOrigen, cuentaDestino, monto, idEmpleado, ventanilla });

        private static T GetJson<T>(string url, T fallback)
        {
            try
            {
                using var resp = Http.GetAsync(url).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<T>(raw, JsonOpts) ?? fallback;
            }
            catch { return fallback; }
        }

        private static OperacionModel PostOperacion(string url, object payload)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(payload), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync(url, content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<OperacionModel>(raw, JsonOpts)
                    ?? new OperacionModel { Exito = false, Mensaje = raw };
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Mensaje = "Fallo REST: " + ex.Message };
            }
        }
    }
}
"""));

            list.Add(TableCaption(20, "Controlador de Escritorio AppControlador.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class AppControlador
    {
        private readonly EurekaBankRestModel _modelo = new();
        public UsuarioModel? SesionActual { get; private set; }
        public int VentanillaActiva { get; set; } = 1;

        public string EndpointActual
        {
            get => _modelo.EndpointUrl;
            set => _modelo.EndpointUrl = value;
        }

        public UsuarioModel IniciarSesion(string usuario, string clave)
        {
            var res = _modelo.Autenticar(usuario, clave);
            if (res.Autenticado)
            {
                SesionActual = res;
                if (res.Ventanilla >= 1 && res.Ventanilla <= 4)
                    VentanillaActiva = res.Ventanilla;
            }
            return res;
        }

        public List<SucursalModel> ObtenerSucursales() => _modelo.ListarSucursales();
        public List<CuentaModel> ObtenerCuentasPorSucursal(int sucId) => _modelo.ListarCuentasPorSucursal(sucId);
        public OperacionModel Depositar(string cta, double monto) =>
            _modelo.Depositar(cta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);
        public OperacionModel Transferir(string ori, string des, double monto) =>
            _modelo.Transferir(ori, des, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);
    }
}
"""));

            list.Add(TableCaption(21, "Formulario de Autenticación de Ventanilla LoginView.cs"));
            list.Add(CodeBox("""
using System.Drawing;
using System.Windows.Forms;
using ec.edu.monster.controller;

namespace ec.edu.monster.view
{
    public class LoginView : Form
    {
        private readonly AppControlador _controlador = new();
        private TextBox _txtUsuario = new();
        private TextBox _txtClave = new();
        private ComboBox _cboVentanilla = new();
        private Button _btnLogin = new();

        public LoginView()
        {
            Text = "EurekaBank RESTful .NET - Inicio de Sesión (GR04)";
            Size = new Size(420, 360);
            StartPosition = FormStartPosition.CenterScreen;
            FormBorderStyle = FormBorderStyle.FixedDialog;
            MaximizeBox = false;

            var pnl = new Panel { Dock = DockStyle.Fill, Padding = new Padding(24) };
            var lblTitulo = new Label { Text = "SISTEMA BANCARIO EUREKABANK", Font = new Font("Segoe UI", 12, FontStyle.Bold), AutoSize = true, Top = 16, Left = 24 };
            
            var lblUsr = new Label { Text = "Usuario Operador:", Top = 64, Left = 24, AutoSize = true };
            _txtUsuario = new TextBox { Top = 88, Left = 24, Width = 350, Text = "Monster" };

            var lblPwd = new Label { Text = "Contraseña:", Top = 124, Left = 24, AutoSize = true };
            _txtClave = new TextBox { Top = 148, Left = 24, Width = 350, PasswordChar = '•', Text = "Monster9" };

            var lblVen = new Label { Text = "Ventanilla:", Top = 184, Left = 24, AutoSize = true };
            _cboVentanilla = new ComboBox { Top = 208, Left = 24, Width = 350, DropDownStyle = ComboBoxStyle.DropDownList };
            _cboVentanilla.Items.AddRange(new object[] { "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4" });
            _cboVentanilla.SelectedIndex = 0;

            _btnLogin = new Button { Text = "INGRESAR AL SISTEMA", Top = 252, Left = 24, Width = 350, Height = 36, BackColor = Color.FromArgb(30, 58, 138), ForeColor = Color.White };
            _btnLogin.Click += BtnLogin_Click;

            pnl.Controls.AddRange(new Control[] { lblTitulo, lblUsr, _txtUsuario, lblPwd, _txtClave, lblVen, _cboVentanilla, _btnLogin });
            Controls.Add(pnl);
        }

        private void BtnLogin_Click(object? sender, EventArgs e)
        {
            var res = _controlador.IniciarSesion(_txtUsuario.Text.Trim(), _txtClave.Text.Trim());
            if (res.Autenticado)
            {
                _controlador.VentanillaActiva = _cboVentanilla.SelectedIndex + 1;
                Hide();
                var dash = new DashboardView(_controlador);
                dash.FormClosed += (s, ev) => Close();
                dash.Show();
            }
            else
            {
                MessageBox.Show(res.Mensaje, "Error de Acceso", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }
}
"""));

            list.Add(TableCaption(22, "Formulario Principal con DataGridView Cromático DashboardView.cs"));
            list.Add(CodeBox("""
using System.Drawing;
using System.Windows.Forms;
using ec.edu.monster.controller;
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public class DashboardView : Form
    {
        private readonly AppControlador _controlador;
        private DataGridView _gridCuentas = new();
        private System.Windows.Forms.Timer _timer = new();

        public DashboardView(AppControlador controlador)
        {
            _controlador = controlador;
            Text = $"EurekaBank Dashboard RESTful - Ventanilla {_controlador.VentanillaActiva} ({_controlador.SesionActual?.NombreCompleto})";
            Size = new Size(1000, 650);
            StartPosition = FormStartPosition.CenterScreen;

            ConfigurarColumnasCuentas();
            RefrescarCuentas();

            _timer.Interval = 1000;
            _timer.Tick += (s, e) => RefrescarCuentas();
            _timer.Start();
        }

        private void ConfigurarColumnasCuentas()
        {
            _gridCuentas.Dock = DockStyle.Fill;
            _gridCuentas.Columns.Add("NumeroCuenta", "Cuenta");
            _gridCuentas.Columns.Add("Titular", "Titular");
            _gridCuentas.Columns.Add("Dni", "DNI");
            _gridCuentas.Columns.Add("Sucursal", "Sucursal");
            _gridCuentas.Columns.Add("Saldo", "Saldo USD");
            _gridCuentas.Columns.Add("Semaforo", "Semáforo MashUp");
            _gridCuentas.ReadOnly = true;
            _gridCuentas.SelectionMode = DataGridViewSelectionMode.FullRowSelect;
            Controls.Add(_gridCuentas);
        }

        private void RefrescarCuentas()
        {
            var cuentas = _controlador.ObtenerCuentasPorSucursal(0);
            _gridCuentas.Rows.Clear();
            foreach (var c in cuentas)
            {
                bool esRojo = string.Equals(c.EstadoMashup, "ROJO", StringComparison.OrdinalIgnoreCase);
                int idx = _gridCuentas.Rows.Add(
                    c.NumeroCuenta, c.TitularCompleto, c.DniCliente, c.NombreSucursal,
                    $"${c.Saldo:F2}", esRojo ? $"🔴 ROJO (Vent. {c.VentanillaOcupada})" : "🟢 VERDE (Libre)");

                var fila = _gridCuentas.Rows[idx];
                fila.DefaultCellStyle.BackColor = esRojo ? Color.FromArgb(254, 226, 226) : Color.FromArgb(240, 253, 244);
                fila.DefaultCellStyle.ForeColor = esRojo ? Color.FromArgb(153, 27, 27) : Color.FromArgb(20, 83, 45);
            }
        }
    }
}
"""));

            list.Add(FigureCaption(10, "Interfaz Gráfica de Autenticación de Ventanilla en Windows Forms (LoginView)"));
            list.Add(ScreenshotBox(
                10,
                "Interfaz Gráfica de Autenticación de Ventanilla en Windows Forms (LoginView)",
                "Cliente de Escritorio Windows Forms (net10.0-windows).",
                "Formulario modal LoginView con campos de usuario, clave y selección de ventanilla.",
                "Digitar 'Monster', contraseña 'Monster9', seleccionar Ventanilla 1 y presionar 'INGRESAR AL SISTEMA'.",
                "Cierre del formulario de inicio de sesión y apertura inmediata del Dashboard principal bancario."
            ));

            list.Add(FigureCaption(11, "Panel Principal del Dashboard Bancario con DataGridView y Semáforo Cromático en Tiempo Real"));
            list.Add(ScreenshotBox(
                11,
                "Panel Principal del Dashboard Bancario con DataGridView y Semáforo Cromático en Tiempo Real",
                "Formulario principal DashboardView.",
                "Cuadrícula DataGridView mostrando todas las cuentas bancarias con datos sincronizados.",
                "Comprobar las filas de cuentas resaltadas en verde claro (#F0FDF4) con insignia '🟢 VERDE (Libre)'.",
                "Visualización fluida con actualización periódica de saldos y semáforos sin parpadeo de interfaz."
            ));

            list.Add(FigureCaption(12, "Ejecución de Transacción de Depósito en Cliente Escritorio"));
            list.Add(ScreenshotBox(
                12,
                "Ejecución de Transacción de Depósito en Cliente Escritorio",
                "Panel lateral de Operaciones Transaccionales en DashboardView.",
                "Cuenta: '001-0001', Monto: '150.00' USD.",
                "Hacer clic en el botón 'ACREDITAR DEPÓSITO (+)'.",
                "La fila cambia temporalmente a rojo durante la transacción y al finalizar muestra el saldo incrementado con mensaje de confirmación."
            ));

            list.Add(FigureCaption(13, "Ejecución de Operación de Transferencia con Verificación ACID en Escritorio"));
            list.Add(ScreenshotBox(
                13,
                "Ejecución de Operación de Transferencia con Verificación ACID en Escritorio",
                "Formulario de Transferencia entre Cuentas en DashboardView.",
                "Origen: '001-0001', Destino: '001-0002', Monto: '100.00' USD.",
                "Hacer clic en 'EJECUTAR TRANSFERENCIA' verificando la atomicidad de débito y crédito.",
                "Actualización simultánea de ambas cuentas con reducción de saldo en la cuenta origen e incremento en la cuenta destino."
            ));

            // 4.9 Desarrollo del Cliente Móvil .NET MAUI
            list.Add(Heading2("4.9 Desarrollo del Cliente Móvil (.NET MAUI)"));
            list.Add(Paragraph("El Cliente Móvil se desarrolla sobre .NET MAUI (Multi-platform App UI) en .NET 10, optimizado para ejecutarse nativamente en el emulador y dispositivos físicos Android. Utiliza vistas XAML declarativas, enlace de datos en C# puro bajo MVC y manejo de la dirección IP de loopback del emulador Android (10.0.2.2:5140/api) para la comunicación RESTful:"));

            list.Add(FigureCaption(14, "Creación y Estructura del Proyecto Móvil .NET MAUI en Visual Studio"));
            list.Add(ScreenshotBox(
                14,
                "Creación y Estructura del Proyecto Móvil .NET MAUI en Visual Studio",
                "Visual Studio 2026/2022 con la carga de trabajo de desarrollo móvil .NET MAUI.",
                "Proyecto 'WSEurekaBankMobileClient' con las carpetas ec.edu.monster.controller, ec.edu.monster.model y ec.edu.monster.view.",
                "Compilación del proyecto para la plataforma net10.0-android.",
                "Estructura de archivos XAML y .xaml.cs compilando sin errores para la plataforma Android."
            ));

            list.Add(TableCaption(23, "Descriptor de Proyecto Cliente Móvil WSEurekaBankMobileClient.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk">
  <PropertyGroup>
    <TargetFrameworks>net10.0-android</TargetFrameworks>
    <TargetFrameworks Condition="$([MSBuild]::IsOSPlatform('windows'))">$(TargetFrameworks);net10.0-windows10.0.19041.0</TargetFrameworks>
    <OutputType>Exe</OutputType>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WSEurekaBankMobileClient</AssemblyName>
    <UseMaui>true</UseMaui>
    <SingleProject>true</SingleProject>
    <ImplicitUsings>enable</ImplicitUsings>
    <Nullable>enable</Nullable>
    <ApplicationTitle>EurekaBank RESTful .NET</ApplicationTitle>
    <ApplicationId>ec.edu.monster.climov.rest</ApplicationId>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Microsoft.Maui.Controls" Version="$(MauiVersion)" />
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(24, "Configuración de Arranque de la Aplicación Móvil MauiProgram.cs"));
            list.Add(CodeBox("""
namespace ec.edu.monster
{
    public static class MauiProgram
    {
        public static MauiApp CreateMauiApp()
        {
            var builder = MauiApp.CreateBuilder();
            builder
                .UseMauiApp<App>()
                .ConfigureFonts(fonts =>
                {
                    fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
                    fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
                });

            return builder.Build();
        }
    }
}
"""));

            list.Add(TableCaption(25, "Modelo de Invocación RESTful en Dispositivo Móvil EurekaBankRestModel.cs (MAUI)"));
            list.Add(CodeBox("""
using System.Text;
using System.Text.Json;
using Microsoft.Maui.Devices;

namespace ec.edu.monster.model
{
    /// <summary>
    /// Capa Modelo (MVC Tradicional puro) para el Cliente Móvil .NET MAUI (RESTful JSON).
    /// </summary>
    public class EurekaBankRestModel
    {
        private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(30) };
        private static readonly JsonSerializerOptions JsonOpts = new() { PropertyNameCaseInsensitive = true };

        public string EndpointUrl { get; set; } =
            DeviceInfo.Platform == DevicePlatform.Android
                ? "http://10.0.2.2:5140/api"
                : "http://localhost:5140/api";

        private string BaseUrl => EndpointUrl.TrimEnd('/');

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(new { usuario, clave }), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync($"{BaseUrl}/auth/login", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<UsuarioModel>(raw, JsonOpts) ?? new UsuarioModel { Autenticado = false, Mensaje = "Respuesta inválida." };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error REST: " + ex.Message };
            }
        }

        public List<CuentaModel> ListarCuentas()
        {
            try
            {
                using var resp = Http.GetAsync($"{BaseUrl}/cuentas").GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<List<CuentaModel>>(raw, JsonOpts) ?? new List<CuentaModel>();
            }
            catch { return new List<CuentaModel>(); }
        }

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(new { numeroCuenta, monto, idEmpleado, ventanilla }), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync($"{BaseUrl}/movimientos/deposito", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<OperacionModel>(raw, JsonOpts) ?? new OperacionModel { Exito = false, Mensaje = raw };
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Mensaje = "Fallo REST: " + ex.Message };
            }
        }
    }
}
"""));

            list.Add(TableCaption(26, "Controlador de la Aplicación Móvil AppControlador.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class AppControlador
    {
        private readonly EurekaBankRestModel _modelo = new();
        public UsuarioModel? SesionActual { get; private set; }
        public int VentanillaActiva { get; set; } = 1;

        public string EndpointActual
        {
            get => _modelo.EndpointUrl;
            set => _modelo.EndpointUrl = value;
        }

        public UsuarioModel IniciarSesion(string usuario, string clave)
        {
            var res = _modelo.Autenticar(usuario, clave);
            if (res.Autenticado)
            {
                SesionActual = res;
                if (res.Ventanilla >= 1 && res.Ventanilla <= 4)
                    VentanillaActiva = res.Ventanilla;
            }
            return res;
        }

        public List<CuentaModel> ObtenerCuentas() => _modelo.ListarCuentas();
        public OperacionModel Depositar(string cta, double monto) =>
            _modelo.Depositar(cta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);
    }
}
"""));

            list.Add(TableCaption(27, "Vista XAML de Inicio de Sesión Móvil LoginView.xaml y LoginView.xaml.cs"));
            list.Add(CodeBox("""
<!-- LoginView.xaml -->
<ContentPage xmlns="http://schemas.microsoft.com/dotnet/2021/maui"
             xmlns:x="http://schemas.microsoft.com/winfx/2009/xaml"
             x:Class="ec.edu.monster.view.LoginView"
             Title="EurekaBank Móvil - RESTful .NET"
             BackgroundColor="#0F172A">
    <ScrollView>
        <VerticalStackLayout Padding="24" Spacing="14" VerticalOptions="Center" MaximumWidthRequest="440">
            <Border BackgroundColor="White" StrokeThickness="0" Padding="24">
                <Border.StrokeShape>
                    <RoundRectangle CornerRadius="16" />
                </Border.StrokeShape>
                <VerticalStackLayout Spacing="12">
                    <Label Text="EUREKABANK - BANCA MOVIL" FontSize="19" FontAttributes="Bold" TextColor="#0F172A" HorizontalOptions="Center" />
                    <Label Text="Cliente .NET MAUI (RESTful) | ec.edu.monster" FontSize="12" TextColor="#64748B" HorizontalOptions="Center" />

                    <Label Text="Usuario Operador:" FontSize="12" FontAttributes="Bold" TextColor="#334155" />
                    <Entry x:Name="TxtUsuario" Text="Monster" TextColor="#0F172A" BackgroundColor="#F8FAFC" />

                    <Label Text="Contraseña:" FontSize="12" FontAttributes="Bold" TextColor="#334155" />
                    <Entry x:Name="TxtClave" Text="Monster9" IsPassword="True" TextColor="#0F172A" BackgroundColor="#F8FAFC" />

                    <Label Text="Ventanilla Móvil:" FontSize="12" FontAttributes="Bold" TextColor="#334155" />
                    <Picker x:Name="PckVentanilla" TextColor="#0F172A" BackgroundColor="#F8FAFC">
                        <Picker.Items>
                            <x:String>Ventanilla 1</x:String>
                            <x:String>Ventanilla 2</x:String>
                            <x:String>Ventanilla 3</x:String>
                            <x:String>Ventanilla 4</x:String>
                        </Picker.Items>
                    </Picker>

                    <Button x:Name="BtnIngresar"
                            Text="INGRESAR A BANCA MOVIL"
                            BackgroundColor="#2563EB"
                            TextColor="White"
                            FontAttributes="Bold"
                            CornerRadius="10"
                            HeightRequest="46"
                            Clicked="OnIngresarClicked" />

                    <Label x:Name="LblEstado" Text="" FontSize="12" FontAttributes="Bold" TextColor="#DC2626" HorizontalTextAlignment="Center" />
                </VerticalStackLayout>
            </Border>
        </VerticalStackLayout>
    </ScrollView>
</ContentPage>
"""));

            list.Add(TableCaption(28, "Vista XAML del Monitor de Cuentas Móvil DashboardView.xaml y DashboardView.xaml.cs"));
            list.Add(CodeBox("""
<!-- DashboardView.xaml -->
<ContentPage xmlns="http://schemas.microsoft.com/dotnet/2021/maui"
             xmlns:x="http://schemas.microsoft.com/winfx/2009/xaml"
             x:Class="ec.edu.monster.view.DashboardView"
             Title="Dashboard Móvil RESTful">
    <Grid RowDefinitions="Auto,*">
        <VerticalStackLayout Grid.Row="0" Padding="16" BackgroundColor="#F1F5F9">
            <Label x:Name="LblInfo" Text="Operador: Monster" FontAttributes="Bold" />
        </VerticalStackLayout>
        <CollectionView Grid.Row="1" x:Name="ListCuentas" SelectionMode="Single">
            <CollectionView.ItemTemplate>
                <DataTemplate>
                    <Frame Margin="8,4" Padding="12" CornerRadius="8" BorderColor="#CBD5E1">
                        <Grid ColumnDefinitions="*,Auto">
                            <VerticalStackLayout Grid.Column="0">
                                <Label Text="{Binding NumeroCuenta}" FontAttributes="Bold" />
                                <Label Text="{Binding TitularCompleto}" />
                                <Label Text="{Binding Saldo, StringFormat='Saldo: ${0:F2}'}" TextColor="#16A34A" />
                            </VerticalStackLayout>
                            <Frame Grid.Column="1" BackgroundColor="{Binding ColorBadge}" Padding="8,4" CornerRadius="4">
                                <Label Text="{Binding EstadoMashup}" TextColor="White" FontSize="12" />
                            </Frame>
                        </Grid>
                    </Frame>
                </DataTemplate>
            </CollectionView.ItemTemplate>
        </CollectionView>
    </Grid>
</ContentPage>
"""));

            list.Add(FigureCaption(15, "Pantalla de Autenticación y Ventanilla en Emulador Android (.NET MAUI)"));
            list.Add(ScreenshotBox(
                15,
                "Pantalla de Autenticación y Ventanilla en Emulador Android (.NET MAUI)",
                "Emulador Android (Pixel 7 / Android 14 API 34).",
                "Pantalla de login de la app WSEurekaBankMobileClient.",
                "Ingresar 'Monster', 'Monster9', seleccionar 'Ventanilla 1' y pulsar 'INGRESAR A BANCA MOVIL'.",
                "Validación de credenciales con el host RESTful (10.0.2.2:5140/api) y transición hacia el Dashboard de cuentas."
            ));

            list.Add(FigureCaption(16, "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil"));
            list.Add(ScreenshotBox(
                16,
                "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil",
                "Pantalla DashboardView en el emulador Android.",
                "Lista CollectionView mostrando las cuentas bancarias con tarjetas formateadas.",
                "Verificar la tarjeta con insignia verde 'VERDE' y actualización tras ejecutar un depósito.",
                "Presentación clara de saldos y confirmación de exclusión mutua cuando otra ventanilla opera la cuenta."
            ));

            // 4.10 Desarrollo del Cliente Web ASP.NET Core MVC
            list.Add(Heading2("4.10 Desarrollo del Cliente Web (ASP.NET Core MVC)"));
            list.Add(Paragraph("El Cliente Web se construye con ASP.NET Core MVC en .NET 10, implementando controladores desacoplados (LoginController y DashboardController), vistas Razor y sondeo reactivo en tiempo real mediante JavaScript (fetch periódico cada 0.9 segundos) para consultar el estado del semáforo MashUp:"));

            list.Add(TableCaption(29, "Descriptor de Proyecto Cliente Web WSEurekaBankWebClient.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WSEurekaBankWebClient</AssemblyName>
  </PropertyGroup>
</Project>
"""));

            list.Add(TableCaption(30, "Configuración del Pipeline Web y Middleware ASP.NET Core Program.cs"));
            list.Add(CodeBox("""
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using ec.edu.monster.controller;

var builder = WebApplication.CreateBuilder(args);

string? ep = builder.Configuration["EurekaBank:EndpointUrl"];
if (!string.IsNullOrWhiteSpace(ep))
{
    AppControlador.EndpointGlobal = ep;
}

// Configuración MVC Tradicional ubicando las vistas en ec.edu.monster.view
builder.Services.AddControllersWithViews()
    .AddRazorOptions(options =>
    {
        options.ViewLocationFormats.Clear();
        options.ViewLocationFormats.Add("/ec.edu.monster.view/{1}/{0}.cshtml");
        options.ViewLocationFormats.Add("/ec.edu.monster.view/{0}.cshtml");
    });

var app = builder.Build();

app.UseStaticFiles();
app.UseRouting();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Login}/{action=Index}/{id?}");

app.Run();
"""));

            list.Add(TableCaption(31, "Modelo RESTful en Cliente Web EurekaBankRestModel.cs (MVC)"));
            list.Add(CodeBox("""
using System.Text;
using System.Text.Json;

namespace ec.edu.monster.model
{
    public class EurekaBankRestModel
    {
        private static readonly HttpClient Http = new() { Timeout = TimeSpan.FromSeconds(30) };
        private static readonly JsonSerializerOptions JsonOpts = new() { PropertyNameCaseInsensitive = true };

        public string EndpointUrl { get; set; } = "http://localhost:5140/api";
        private string BaseUrl => EndpointUrl.TrimEnd('/');

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(new { usuario, clave }), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync($"{BaseUrl}/auth/login", content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<UsuarioModel>(raw, JsonOpts) ?? new UsuarioModel { Autenticado = false, Mensaje = "Respuesta inválida." };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error REST: " + ex.Message };
            }
        }

        public List<CuentaModel> ListarCuentasPorSucursal(int sucId) => GetJson($"{BaseUrl}/cuentas?sucursal={sucId}", new List<CuentaModel>());
        public List<CuentaModel> ConsultarCuentasPorCliente(string dni) => GetJson($"{BaseUrl}/clientes/{Uri.EscapeDataString(dni)}/cuentas", new List<CuentaModel>());
        public List<MovimientoModel> ConsultarExtracto(string cta) => GetJson($"{BaseUrl}/cuentas/{Uri.EscapeDataString(cta)}/movimientos", new List<MovimientoModel>());

        public OperacionModel Depositar(string cta, double monto, int emp, int ven) =>
            PostOperacion($"{BaseUrl}/movimientos/deposito", new { numeroCuenta = cta, monto, idEmpleado = emp, ventanilla = ven });

        public OperacionModel Transferir(string ori, string des, double monto, int emp, int ven) =>
            PostOperacion($"{BaseUrl}/movimientos/transferencia", new { cuentaOrigen = ori, cuentaDestino = des, monto, idEmpleado = emp, ventanilla = ven });

        private static T GetJson<T>(string url, T fallback)
        {
            try
            {
                using var resp = Http.GetAsync(url).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<T>(raw, JsonOpts) ?? fallback;
            }
            catch { return fallback; }
        }

        private static OperacionModel PostOperacion(string url, object payload)
        {
            try
            {
                using var content = new StringContent(JsonSerializer.Serialize(payload), Encoding.UTF8, "application/json");
                using var resp = Http.PostAsync(url, content).GetAwaiter().GetResult();
                string raw = resp.Content.ReadAsStringAsync().GetAwaiter().GetResult();
                return JsonSerializer.Deserialize<OperacionModel>(raw, JsonOpts) ?? new OperacionModel { Exito = false, Mensaje = raw };
            }
            catch (Exception ex)
            {
                return new OperacionModel { Exito = false, Mensaje = "Fallo REST: " + ex.Message };
            }
        }
    }
}
"""));

            list.Add(TableCaption(32, "Controlador Web de Autenticación LoginController.cs"));
            list.Add(CodeBox("""
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.controller;

namespace ec.edu.monster.controller
{
    public class LoginController : Controller
    {
        private readonly AppControlador _controlador = new();

        [HttpGet]
        public IActionResult Index() => View("~/ec.edu.monster.view/Login/Index.cshtml");

        [HttpPost]
        public IActionResult Autenticar(string usuario, string clave, int ventanilla = 1)
        {
            var res = _controlador.IniciarSesion(usuario, clave);
            if (res.Autenticado)
            {
                res.Ventanilla = ventanilla;
                AppControlador.GuardarSesionCookie(Response, res, ventanilla);
                return RedirectToAction("Index", "Dashboard");
            }
            ViewBag.Error = res.Mensaje;
            return View("~/ec.edu.monster.view/Login/Index.cshtml");
        }

        [HttpGet]
        public IActionResult Salir()
        {
            Response.Cookies.Delete("EurekaBank_Sesion_REST");
            return RedirectToAction("Index");
        }
    }
}
"""));

            list.Add(TableCaption(33, "Controlador Web del Dashboard Transaccional DashboardController.cs"));
            list.Add(CodeBox("""
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class DashboardController : Controller
    {
        private readonly AppControlador _controlador = new();

        [HttpGet]
        public IActionResult Index(int sucursalId = 0, string filtro = "", string cuenta = "001-0001")
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return RedirectToAction("Index", "Login");

            var cuentas = _controlador.ObtenerCuentasPorSucursal(sucursalId);
            var vm = new DashboardWebModel
            {
                Sesion = sesion,
                VentanillaActiva = sesion.Ventanilla,
                Cuentas = cuentas,
                Extracto = _controlador.ObtenerExtracto(cuenta),
                CuentaSeleccionada = cuenta
            };
            return View("~/ec.edu.monster.view/Dashboard/Index.cshtml", vm);
        }

        [HttpGet]
        public IActionResult EstadoMashup(int sucursalId = 0)
        {
            var cuentas = _controlador.ObtenerCuentasPorSucursal(sucursalId);
            return Json(new
            {
                verdes = cuentas.Count(c => c.EstadoMashup != "ROJO"),
                rojas = cuentas.Count(c => c.EstadoMashup == "ROJO"),
                cuentas = cuentas.Select(c => new
                {
                    c.NumeroCuenta, c.TitularCompleto, c.DniCliente, c.NombreSucursal,
                    c.Saldo, c.EstadoMashup, c.VentanillaOcupada, c.BloqueadoPor
                })
            });
        }

        [HttpPost]
        public IActionResult Depositar(string cuenta, double monto)
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return Unauthorized();
            var res = _controlador.Depositar(cuenta, monto, sesion.IdEmpleado, sesion.Ventanilla);
            return Json(res);
        }
    }
}
"""));

            list.Add(TableCaption(34, "ViewModel de Pantallas y Filtros Web WebPantallasModel.cs"));
            list.Add(CodeBox("""
using ec.edu.monster.model;

namespace ec.edu.monster.model
{
    public class DashboardWebModel
    {
        public UsuarioModel Sesion { get; set; } = new();
        public int VentanillaActiva { get; set; } = 1;
        public int SucursalSeleccionada { get; set; }
        public string Filtro { get; set; } = string.Empty;
        public string CuentaSeleccionada { get; set; } = "001-0001";
        public List<SucursalModel> Sucursales { get; set; } = new();
        public List<CuentaModel> Cuentas { get; set; } = new();
        public List<MovimientoModel> Extracto { get; set; } = new();
    }
}
"""));

            list.Add(TableCaption(35, "Plantilla Razor de Inicio de Sesión Login/Index.cshtml"));
            list.Add(CodeBox("""
@model ec.edu.monster.model.UsuarioModel
@{
    Layout = null;
}
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <title>EurekaBank Web - Inicio de Sesión</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light d-flex align-items-center min-vh-100">
    <div class="container" style="max-width: 420px;">
        <div class="card shadow border-0 p-4">
            <h4 class="text-center font-weight-bold text-primary mb-3">EUREKABANK WEB</h4>
            @if (ViewBag.Error != null)
            {
                <div class="alert alert-danger py-2">@ViewBag.Error</div>
            }
            <form method="post" action="/Login/Autenticar">
                <div class="mb-3">
                    <label class="form-label">Usuario Operador:</label>
                    <input type="text" name="usuario" class="form-control" value="Monster" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Contraseña:</label>
                    <input type="password" name="clave" class="form-control" value="Monster9" required />
                </div>
                <div class="mb-3">
                    <label class="form-label">Ventanilla de Atención:</label>
                    <select name="ventanilla" class="form-select">
                        <option value="1">Ventanilla 1</option>
                        <option value="2">Ventanilla 2</option>
                        <option value="3">Ventanilla 3</option>
                        <option value="4">Ventanilla 4</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary w-100 py-2">INGRESAR AL SISTEMA</button>
            </form>
        </div>
    </div>
</body>
</html>
"""));

            list.Add(TableCaption(36, "Plantilla Razor del Dashboard Reactivo con Polling Dashboard/Index.cshtml"));
            list.Add(CodeBox("""
@model ec.edu.monster.model.DashboardWebModel
@{
    Layout = null;
}
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <title>EurekaBank - Dashboard Transaccional</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <style>
        .badge-rojo { background-color: #DC2626; color: white; padding: 6px 12px; border-radius: 4px; font-weight: bold; }
        .badge-verde { background-color: #16A34A; color: white; padding: 6px 12px; border-radius: 4px; font-weight: bold; }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-dark bg-primary px-4">
        <span class="navbar-brand font-weight-bold">EUREKABANK - SUPERVISIÓN MASHUP & BANCA WEB (.NET 10)</span>
        <span class="text-white">Operador: @Model.Sesion.NombreCompleto | Ventanilla: @Model.VentanillaActiva</span>
    </nav>
    <div class="container-fluid py-4 px-4">
        <div class="row">
            <div class="col-md-8">
                <div class="card shadow-sm p-3 mb-4">
                    <h5>Semáforo de Concurrencia MashUp en Tiempo Real</h5>
                    <table class="table table-hover mt-3" id="tablaCuentas">
                        <thead class="table-light">
                            <tr><th>Cuenta</th><th>Titular</th><th>Saldo USD</th><th>Estado MashUp</th><th>Acción</th></tr>
                        </thead>
                        <tbody>
                            @foreach (var c in Model.Cuentas)
                            {
                                <tr>
                                    <td>@c.NumeroCuenta</td>
                                    <td>@c.TitularCompleto</td>
                                    <td>$@c.Saldo.ToString("F2")</td>
                                    <td>
                                        <span class="@(c.EstadoMashup == "ROJO" ? "badge-rojo" : "badge-verde")">
                                            @(c.EstadoMashup == "ROJO" ? $"ROJO (Vent. {c.VentanillaOcupada})" : "VERDE (Libre)")
                                        </span>
                                    </td>
                                    <td><button class="btn btn-sm btn-outline-primary" onclick="seleccionarCuenta('@c.NumeroCuenta')">Extracto</button></td>
                                </tr>
                            }
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm p-3">
                    <h5>Operaciones Transaccionales ACID</h5>
                    <div class="mb-3 mt-2">
                        <label>Cuenta Destino/Origen:</label>
                        <input type="text" id="txtCuenta" class="form-control" value="001-0001" />
                    </div>
                    <div class="mb-3">
                        <label>Monto USD ($):</label>
                        <input type="number" id="txtMonto" class="form-control" value="50.00" />
                    </div>
                    <button class="btn btn-success w-100 mb-2" onclick="ejecutarDeposito()">ACREDITAR DEPÓSITO (+)</button>
                </div>
            </div>
        </div>
    </div>
    <script>
        // Polling reactivo cada 900 ms para actualización en tiempo real del semáforo
        setInterval(async () => {
            const resp = await fetch('/Dashboard/EstadoMashup');
            if (resp.ok) {
                const data = await resp.json();
                actualizarTablaCuentas(data.cuentas);
            }
        }, 900);

        function actualizarTablaCuentas(cuentas) {
            const tbody = document.querySelector('#tablaCuentas tbody');
            tbody.innerHTML = '';
            cuentas.forEach(c => {
                const esRojo = c.estadoMashup === 'ROJO';
                const badge = esRojo
                    ? `<span class="badge-rojo">ROJO (Vent. ${c.ventanillaOcupada} - ${c.bloqueadoPor})</span>`
                    : `<span class="badge-verde">VERDE (Libre)</span>`;
                tbody.innerHTML += `<tr><td>${c.numeroCuenta}</td><td>${c.titularCompleto}</td><td>$${c.saldo.toFixed(2)}</td><td>${badge}</td><td><button class="btn btn-sm btn-outline-primary" onclick="seleccionarCuenta('${c.numeroCuenta}')">Extracto</button></td></tr>`;
            });
        }

        async function ejecutarDeposito() {
            const cuenta = document.getElementById('txtCuenta').value;
            const monto = document.getElementById('txtMonto').value;
            const res = await fetch(`/Dashboard/Depositar?cuenta=${cuenta}&monto=${monto}`, { method: 'POST' });
            const data = await res.json();
            alert(data.mensaje);
        }
    </script>
</body>
</html>
"""));

            list.Add(FigureCaption(17, "Interfaz Web de Inicio de Sesión de Ventanilla (Login/Index.cshtml)"));
            list.Add(ScreenshotBox(
                17,
                "Interfaz Web de Inicio de Sesión de Ventanilla (Login/Index.cshtml)",
                "Navegador Google Chrome en http://localhost:5141/ o http://localhost:5000/.",
                "Formulario de autenticación web de EurekaBank.",
                "Ingresar 'Monster', contraseña 'Monster9', seleccionar Ventanilla 1 y pulsar 'INGRESAR AL SISTEMA'.",
                "Autenticación válida y redirección hacia el Dashboard principal de monitoreo en /Dashboard."
            ));

            list.Add(FigureCaption(18, "Monitor MashUp y Banca Web en Tiempo Real (Dashboard/Index.cshtml)"));
            list.Add(ScreenshotBox(
                18,
                "Monitor MashUp y Banca Web en Tiempo Real (Dashboard/Index.cshtml)",
                "Dashboard web interactivo en el navegador.",
                "Página principal mostrando la tabla reactiva con todas las insignias de cuenta en VERDE (Libre).",
                "Observar la actualización periódica cada 0.9 segundos sin parpadeo de pantalla.",
                "Panel de operaciones transaccionales activo a la derecha y tabla de cuentas con saldos actualizados."
            ));

            list.Add(FigureCaption(19, "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web"));
            list.Add(ScreenshotBox(
                19,
                "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web",
                "Dashboard web durante la ejecución de un depósito sobre la cuenta 001-0001.",
                "Hacer clic en 'ACREDITAR DEPÓSITO (+)' desde la Ventanilla 1.",
                "La fila correspondiente cambia de inmediato a color rojo y texto 'ROJO (Vent. 1 - Monster Admin)' durante los 4.5 segundos del proceso.",
                "Retorno automático al estado VERDE al confirmarse el COMMIT en la base de datos."
            ));

            // 4.11 Matriz de Pruebas y Troubleshooting
            list.Add(Heading2("4.11 Matriz de Casos de Prueba Transaccionales y de Concurrencia"));
            list.Add(Paragraph("Para validar la robustez técnica del sistema EurekaBank en .NET 10 y el cumplimiento riguroso de las propiedades ACID y exclusión mutua, se ejecutó la siguiente batería de pruebas funcionales y de concurrencia:"));

            list.Add(TableCaption(37, "Matriz de Casos de Prueba Transaccionales y de Concurrencia MashUp"));
            list.Add(TechnicalTable(
                new[] { "ID", "Escenario de Prueba", "Procedimiento Técnico Ejecutado", "Resultado Esperado", "Estado" },
                new[] { 1000, 2200, 2400, 2404, 1000 },
                new List<string[]>
                {
                    new[] { "CP-01", "Autenticación BCrypt Operador Monster", "Ingreso de 'Monster' y clave 'Monster9' vía POST /api/auth/login.", "Código HTTP 200 OK con token y rol Superadmin asignado.", "EXITOSO" },
                    new[] { "CP-02", "Rechazo de Credenciales Inválidas", "Ingreso de clave errónea para operador cajero1 vía POST /api/auth/login.", "Código HTTP 401 Unauthorized sin emitir token.", "EXITOSO" },
                    new[] { "CP-03", "Depósito Atómico con Bloqueo Pesimista", "Depósito de $150.00 sobre 001-0001 con retardo simulado de 4.5s.", "Saldo incrementa, registro en movimiento y HTTP 200 OK.", "EXITOSO" },
                    new[] { "CP-04", "Exclusión Mutua Concurrente (ROJO)", "Ventanilla 2 intenta operar mientras Ventanilla 1 procesa depósito.", "Código HTTP 409 Conflict con mensaje de cuenta en uso.", "EXITOSO" },
                    new[] { "CP-05", "Transferencia ACID entre Cuentas", "Transferencia de $100.00 de 001-0001 a 001-0002 vía POST.", "Débito y crédito aplicados atómicamente con HTTP 200 OK.", "EXITOSO" },
                    new[] { "CP-06", "Rollback por Saldo Insuficiente", "Intento de retiro de $99,999.00 sobre cuenta activa vía POST.", "Código HTTP 400 Bad Request y saldo inalterado.", "EXITOSO" },
                    new[] { "CP-07", "Consumo Móvil en Loopback Android", "Conexión de emulador Android a http://10.0.2.2:5140/api.", "Listado y depósito exitoso desde .NET MAUI.", "EXITOSO" },
                    new[] { "CP-08", "Sondeo Reactivo MashUp en Web", "Actualización AJAX cada 0.9s en Dashboard/Index.cshtml.", "Cambio cromático visible sin recargar la página.", "EXITOSO" }
                }
            ));

            list.Add(TableCaption(38, "Guía de Diagnóstico y Resolución de Errores Frecuentes (Troubleshooting .NET REST)"));
            list.Add(TechnicalTable(
                new[] { "Síntoma / Error", "Causa Técnica Principal", "Solución Recomendada y Comprobada" },
                new[] { 2500, 3000, 3504 },
                new List<string[]>
                {
                    new[] { "Error de conexión con la base de datos (Port 3306)", "El contenedor Podman/Docker de MySQL no está iniciado.", "Ejecutar 'podman compose up -d' dentro del directorio 03. BDD." },
                    new[] { "Timeout o conexión rechazada en emulador Android", "Se configuró 'localhost' en lugar de la IP de loopback.", "Utilizar '10.0.2.2:5140/api' en el archivo EurekaBankRestModel.cs de MAUI." },
                    new[] { "Error HTTP 409 Conflict en depósito o retiro", "La cuenta bancaria se encuentra en estado ROJO por otra ventanilla.", "Esperar a que finalice la transacción en curso o desbloquear la cuenta con el usuario Superadmin Monster." },
                    new[] { "Error HTTP 401 Unauthorized en autenticación", "Las credenciales no coinciden con el hash BCrypt almacenado.", "Verificar que el usuario sea 'Monster' y la clave 'Monster9', o reiniciar la base con eureka_bank.sql." }
                }
            ));

            // =========================================================================
            // SECCIÓN 5: CONCLUSIONES
            // =========================================================================
            list.Add(Heading1("5 CONCLUSIONES"));
            list.Add(BulletParagraph("1.", "La implementación de servicios web RESTful sobre la plataforma moderna .NET 10 mediante ASP.NET Core Web API demuestra que es factible alcanzar un rendimiento óptimo, baja latencia y mínimo consumo de memoria en comparación con arquitecturas legadas basadas en SOAP, prescindiendo de pesadas envolturas XML y aprovechando de forma nativa la semántica del protocolo HTTP y la serialización JSON con System.Text.Json."));
            list.Add(BulletParagraph("2.", "La aplicación estricta del patrón arquitectónico MVC Tradicional Puro (Model, View, Controller) en todos los proyectos clientes (Consola, Escritorio Windows Forms, Móvil .NET MAUI y Web ASP.NET Core) garantiza un desacoplamiento absoluto de la lógica de presentación respecto a la comunicación RESTful mediante HttpClient, facilitando el mantenimiento, las pruebas unitarias y la evolución independiente de cada interfaz de usuario."));
            list.Add(BulletParagraph("3.", "El mecanismo de Semáforo MashUp de Concurrencia en tiempo real, respaldado por la tabla control_mashup y bloqueos pesimistas SELECT ... FOR UPDATE a nivel del motor InnoDB de MySQL, resuelve satisfactoriamente las condiciones de carrera y previene la inconsistencia de saldos en operaciones bancarias simultáneas entre múltiples ventanillas, retornando códigos semánticos HTTP 409 Conflict ante intentos de acceso concurrente."));
            list.Add(BulletParagraph("4.", "La integración de cuatro tecnologías cliente heterogéneas bajo el lenguaje unificado C# (.NET 10) evidencia la versatilidad del ecosistema .NET contemporáneo para abordar escenarios de terminales ligeras de consola, aplicaciones de escritorio ricas, dispositivos móviles nativos y aplicaciones web reactivas con sincronización continua vía sondeo AJAX."));

            // =========================================================================
            // SECCIÓN 6: RECOMENDACIONES
            // =========================================================================
            list.Add(Heading1("6 RECOMENDACIONES"));
            list.Add(BulletParagraph("1.", "En entornos de producción bancaria, se recomienda habilitar transporte seguro TLS/HTTPS obligatorio en Kestrel y utilizar tokens de acceso firmados digitalmente (JSON Web Tokens - JWT) o OAuth2 en el encabezado Authorization para garantizar la confidencialidad de las cargas útiles JSON y la autenticación sin estado de las peticiones."));
            list.Add(BulletParagraph("2.", "Para la sincronización reactiva en clientes web y de escritorio, se sugiere evaluar la adopción de WebSockets o SignalR Core en reemplazo del sondeo periódico (polling), reduciendo la sobrecarga de conexiones HTTP y la latencia de red en escenarios con cientos de ventanillas concurrentes."));
            list.Add(BulletParagraph("3.", "Se recomienda configurar políticas de reintento automático con retroceso exponencial (utilizando la biblioteca Polly en .NET) en la capa Modelo de los clientes, gestionando de forma transparente caídas temporales de conectividad de red o sobrecargas transitorias del servidor RESTful."));
            list.Add(BulletParagraph("4.", "Para la gestión de secretos y credenciales de base de datos en entornos empresariales, es aconsejable delegar la cadena de conexión ConnectionString a variables de entorno protegidas o sistemas de gestión de claves como Azure Key Vault o HashiCorp Vault, evitando contraseñas en texto plano en appsettings.json."));

            // =========================================================================
            // SECCIÓN 7: REFERENCIAS BIBLIOGRÁFICAS
            // =========================================================================
            list.Add(Heading1("7 REFERENCIAS BIBLIOGRÁFICAS"));
            list.Add(Paragraph("[1] Microsoft Corporation, «Overview of ASP.NET Core Web APIs», Microsoft Learn, 2024. [En línea]. Disponible en: https://learn.microsoft.com/aspnet/core/web-api/."));
            list.Add(Paragraph("[2] R. T. Fielding, «Architectural Styles and the Design of Network-based Software Architectures», Ph.D. dissertation, Department of Information and Computer Science, University of California, Irvine, 2000."));
            list.Add(Paragraph("[3] OpenAPI Initiative, «OpenAPI Specification v3.0.3», Linux Foundation, 2020. [En línea]. Disponible en: https://spec.openapis.org/oas/v3.0.3."));
            list.Add(Paragraph("[4] A. Troelsen y P. Japikse, Pro C# 10 with .NET 6: Foundational Principles and Practices in Programming, 11a ed. Nueva York, EE. UU.: Apress, 2022."));
            list.Add(Paragraph("[5] E. Gamma, R. Helm, R. Johnson, y J. Vlissides, Design Patterns: Elements of Reusable Object-Oriented Software. Boston, MA: Addison-Wesley, 1994."));
            list.Add(Paragraph("[6] Oracle Corporation, «MySQL 8.0 Reference Manual: InnoDB Locking and Transaction Model», Oracle Docs, 2026. [En línea]. Disponible en: https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html."));
            list.Add(Paragraph("[7] Microsoft Corporation, «.NET Multi-platform App UI (.NET MAUI) Documentation», Microsoft Learn, 2026. [En línea]. Disponible en: https://learn.microsoft.com/dotnet/maui/."));
            list.Add(Paragraph("[8] C. Anderson, Pro ASP.NET Core MVC, 8a ed. Berkeley, CA: Apress, 2020."));

            return list;
        }
    }
}
