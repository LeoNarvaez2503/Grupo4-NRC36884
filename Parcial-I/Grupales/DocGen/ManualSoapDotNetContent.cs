using System;
using System.Collections.Generic;
using System.Xml.Linq;
using static DocGen.DocHelpers;

namespace DocGen
{
    public static class ManualSoapDotNetContent
    {
        public static List<XElement> BuildElements()
        {
            var list = new List<XElement>();

            // =========================================================================
            // SECCIÓN 1: INTRODUCCIÓN
            // =========================================================================
            list.Add(Heading1("1 INTRODUCCIÓN"));
            list.Add(Paragraph("Un servicio web fundamentado en SOAP (Simple Object Access Protocol) dentro del ecosistema moderno de Microsoft .NET (.NET 10) representa una solución empresarial estandarizada, de tipado rígido y alta confiabilidad para la interoperabilidad de datos entre aplicaciones corporativas distribuidas. Con la evolución de la plataforma .NET unificada hacia versiones modernas y multiplataforma, la tecnología tradicional Windows Communication Foundation (WCF) fue adaptada y continuada a través de CoreWCF, una implementación oficial de código abierto respaldada por Microsoft y la comunidad técnica que permite hospedar servicios web SOAP sobre el servidor web modular Kestrel y ASP.NET Core, conservando la compatibilidad de contratos de servicio ([ServiceContract]), contratos de datos ([DataContract]) y enlaces de transporte estandarizados como BasicHttpBinding sobre HTTP/1.1 y HTTP/2."));
            list.Add(Paragraph("En el ámbito financiero transaccional (Core Banking), las operaciones bancarias ejecutadas sobre cuentas corrientes y de ahorros (tales como la recepción y acreditación de depósitos en efectivo, retiros en ventanilla y transferencias interbancarias o intrabancarias) demandan el estricto cumplimiento de las propiedades ACID (Atomicidad, Consistencia, Aislamiento y Durabilidad). Cuando múltiples ventanillas de atención física o canales de autoservicio digital interactúan de forma simultánea sobre los mismos registros de cuenta, pueden surgir graves condiciones de carrera (race conditions), lecturas sucias (dirty reads) y anomalías contables que ponen en riesgo la solidez financiera de la institución. Para prevenir de raíz estas incidencias, el presente proyecto implementa el sistema EurekaBank con soporte de bloqueo pesimista (SELECT ... FOR UPDATE) a nivel del motor relacional InnoDB en MySQL/MariaDB y un módulo reactivo de exclusión mutua denominado Semáforo MashUp de Concurrencia en tiempo real."));
            list.Add(Paragraph("El componente servidor central se hospeda de forma ligera y eficiente sobre el runtime de .NET 10 utilizando CoreWCF 1.8.0 y Kestrel en el puerto TCP 5120, publicando su contrato WSDL en la ruta estándar /EurekaBankWS.svc?wsdl. Por su parte, la persistencia de la información bancaria se gestiona a través de un contenedor local de base de datos MySQL 8.0 orquestado mediante Podman o Docker Compose, asegurando total portabilidad, aislamiento del entorno anfitrión e inicialización determinista mediante el script relacional eureka_bank.sql."));
            list.Add(Paragraph("Para el consumo y presentación de los servicios SOAP, se aplica con máxima rigurosidad el patrón arquitectónico Modelo-Vista-Controlador (MVC Tradicional Puro), implementando exclusivamente las capas Model, View y Controller sin capas intermedias superfluas ni frameworks de inyección externa innecesarios. Se desarrollan cuatro clientes totalmente heterogéneos bajo el lenguaje C#: un Cliente de Consola interactivo con renderizado de secuencias ANSI y simulación multi-hilo de 3 ventanillas simultáneas, un Cliente de Escritorio de alto rendimiento con Windows Forms y cuadrícula cromática DataGridView, un Cliente Móvil nativo para la plataforma Android desarrollado con .NET MAUI y un Cliente Web responsivo desarrollado sobre ASP.NET Core MVC con vistas Razor y sondeo reactivo en tiempo real (polling a 0.9 segundos). Todo el sistema opera bajo el dominio institucional unificado ec.edu.monster y respeta las credenciales maestras autorizadas (usuario: Monster, contraseña: Monster9), satisfaciendo los requerimientos de ingeniería de software establecidos para el Grupo 04 (GR04)."));

            // =========================================================================
            // SECCIÓN 2: OBJETIVOS
            // =========================================================================
            list.Add(Heading1("2 OBJETIVOS"));
            list.Add(Heading2("2.1 Objetivo General"));
            list.Add(Paragraph("Diseñar, implementar, desplegar y documentar un sistema bancario distribuido y transaccional denominado EurekaBank, fundamentado en servicios web SOAP hospedados en ASP.NET Core Kestrel mediante CoreWCF bajo la plataforma .NET 10, con persistencia relacional ACID en MySQL sobre contenedores locales (Docker/Podman), desacoplado mediante el patrón arquitectónico MVC Tradicional puro en todos sus niveles y consumido concurrentemente por cuatro clientes heterogéneos (Consola, Escritorio Windows Forms, Móvil .NET MAUI y Web ASP.NET Core MVC) con soporte de monitoreo reactivo mediante Semáforo MashUp en tiempo real."));

            list.Add(Heading2("2.2 Objetivos Específicos"));
            list.Add(BulletParagraph("1.", "Configurar y desplegar el motor relacional de base de datos MySQL 8.0 en un contenedor local mediante Podman/Docker Compose, inicializando el esquema completo de tablas bancarias, restricciones de integridad referencial, registros de auditoría y la tabla control_mashup de concurrencia mediante el script eureka_bank.sql."));
            list.Add(BulletParagraph("2.", "Construir la capa de persistencia relacional y control transaccional del servidor SOAP en C# (EurekaBankBddModel.cs), utilizando el driver MySqlConnector para ejecutar transacciones con aislamiento ReadCommitted y bloqueo de filas pesimista (SELECT ... FOR UPDATE), verificando credenciales con encriptación BCrypt ($2a$10$)."));
            list.Add(BulletParagraph("3.", "Diseñar e implementar el componente Gestor de Concurrencia MashUp (ConcurrenciaMashupModel.cs), administrando la alternancia del semáforo cromático (VERDE para cuentas libres y ROJO para cuentas en operación transaccional activa en ventanilla) con persistencia directa en base de datos."));
            list.Add(BulletParagraph("4.", "Implementar y publicar el endpoint SOAP (WS_EurekaBank_DotNet_SOAP) mediante CoreWCF en .NET 10, exponiendo la interfaz de contrato IEurekaBankSoapContrato con BasicHttpBinding y publicando el contrato WSDL accesible bajo el puerto 5120 y dominio institucional ec.edu.monster."));
            list.Add(BulletParagraph("5.", "Desarrollar el Cliente de Consola (WSEurekaBankConsoleClient) bajo el patrón MVC Tradicional, consumiendo el servicio SOAP mediante ChannelFactory<IEurekaBankSoapContrato>, incorporando interfaces de terminal estructuradas con colores ANSI y una rutina multi-hilo de simulación de 3 ventanillas simultáneas para verificar la exclusión mutua."));
            list.Add(BulletParagraph("6.", "Construir el Cliente de Escritorio (WSEurekaBankDesktopClient) mediante Windows Forms en C#, presentando un DataGridView con colores dinámicos (verde claro para cuentas disponibles y rojo suave para cuentas ocupadas), temporizador de refresco periódico y ejecución de transacciones bancarias asíncronas con async/await."));
            list.Add(BulletParagraph("7.", "Implementar el Cliente Móvil (WSEurekaBankMobileClient) utilizando la plataforma multiplataforma .NET MAUI en C#, consumiendo el endpoint SOAP a través de ChannelFactory con resolución de red para emulador Android (10.0.2.2:5120) y presentando vistas XAML reactivas con tarjetas cromáticas de semáforo."));
            list.Add(BulletParagraph("8.", "Desarrollar el Cliente Web (WSEurekaBankWebClient) sobre ASP.NET Core MVC con vistas Razor, implementando controladores desacoplados, autenticación mediante cookies seguras y sondeo asíncrono AJAX en JavaScript cada 0.9 segundos para reflejar en tiempo real la concurrencia entre ventanillas sin recargar la página."));

            // =========================================================================
            // SECCIÓN 3: MARCO TEÓRICO
            // =========================================================================
            list.Add(Heading1("3 MARCO TEÓRICO"));
            list.Add(Heading2("3.1 Servicios Web SOAP en .NET Moderno y Proyecto CoreWCF"));
            list.Add(Paragraph("El protocolo SOAP (Simple Object Access Protocol) es una especificación estándar de mensajería basada en XML aprobada por el consorcio W3C para habilitar la comunicación entre aplicaciones heterogéneas. En las versiones clásicas de Microsoft .NET Framework (versiones 3.0 a 4.8), Windows Communication Foundation (WCF) constituía el marco de trabajo principal para construir arquitecturas orientadas a servicios (SOA). Sin embargo, con el advenimiento de .NET Core y su posterior unificación a partir de .NET 6 hasta .NET 10, los componentes de servidor de WCF no fueron incorporados en el runtime estándar, el cual priorizó APIs HTTP/REST y gRPC."));
            list.Add(Paragraph("Para solventar esta limitación en organizaciones financieras y bancarias con inversiones sustanciales en contratos WSDL y clientes SOAP, la fundación .NET Foundation lanzó CoreWCF. CoreWCF es una reimplementación moderna y de código abierto de los servicios WCF compilada sobre el pipeline de ASP.NET Core y el servidor Kestrel. CoreWCF permite utilizar las anotaciones declarativas estándar de WCF como [ServiceContract], [OperationContract], [DataContract] y [DataMember], proporcionando enlaces modernos como BasicHttpBinding, WSHttpBinding y NetTcpBinding. CoreWCF traslada las ventajas de rendimiento, bajo consumo de memoria y ejecución multiplataforma (Linux, macOS, Windows) de .NET 10 a los servicios web SOAP empresariales."));

            list.Add(Heading2("3.2 Protocolo SOAP, Envelope XML y Contrato WSDL"));
            list.Add(Paragraph("La comunicación en SOAP se realiza mediante documentos XML altamente estructurados denominados SOAP Envelopes. La anatomía formal de un sobre SOAP se compone de:"));
            list.Add(BulletParagraph("• Envelope:", "Elemento raíz que define el espacio de nombres estándar (http://schemas.xmlsoap.org/soap/envelope/ para SOAP 1.1) y encapsula todos los elementos del mensaje."));
            list.Add(BulletParagraph("• Header (Cabecera):", "Elemento opcional utilizado para transmitir metadatos de infraestructura y contexto de seguridad (tales como tokens de autenticación WS-Security, identificadores de correlación transaccional o directivas mustUnderstand)."));
            list.Add(BulletParagraph("• Body (Cuerpo):", "Contenedor obligatorio que aloja la carga útil (payload) de negocio, conteniendo los parámetros de la operación invocada o la respuesta generada por el servidor. En caso de suscitarse un fallo interno, el Body contiene una estructura tipada denominada SOAP Fault (con faultcode, faultstring y detail)."));
            list.Add(Paragraph("El contrato del servicio se describe formalmente mediante el Lenguaje de Descripción de Servicios Web (WSDL). El WSDL expone los esquemas XSD de los tipos de datos, los mensajes de solicitud y respuesta, las interfaces de operaciones (portType), los protocolos de serialización y enlace (binding) y la dirección de red o URL del endpoint (service/port)."));

            list.Add(Heading2("3.3 Patrón Arquitectónico MVC Tradicional en el Ecosistema .NET"));
            list.Add(Paragraph("El patrón Modelo-Vista-Controlador (MVC Tradicional) divide la responsabilidad de un sistema de software en tres componentes fundamentales y desacoplados:"));
            list.Add(BulletParagraph("• Modelo (Model):", "Encapsula la estructura de datos del dominio, las entidades de negocio y la persistencia o interacción con el servicio SOAP remoto (EurekaBankSoapModel.cs). En la arquitectura de cliente delgado, el modelo no contiene reglas de negocio financieras ni conexión directa a bases de datos, sino que actúa como proxy cliente de comunicación remota."));
            list.Add(BulletParagraph("• Vista (View):", "Es la encargada de presentar los datos de forma visual al usuario y capturar los eventos o entradas del operador (interfaces de consola, formularios de Windows Forms, páginas Razor en ASP.NET Core o páginas XAML en .NET MAUI). La vista no procesa datos ni realiza llamadas de red directamente; delega toda acción al controlador."));
            list.Add(BulletParagraph("• Controlador (Controller):", "Coordina el flujo de ejecución entre la vista y el modelo. Recibe las interacciones del operador desde la vista, valida los parámetros de entrada, invoca las operaciones del modelo y actualiza la vista con el resultado de la operación."));
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
            list.Add(BulletParagraph("• Estado ROJO (Ocupada en Ventanilla):", "Indica que un cajero u operador bancario está procesando una transacción activa de depósito, retiro o transferencia sobre la cuenta. Durante el intervalo transaccional (delimitado por un retardo simulado de 4.5 segundos para propósitos pedagógicos), ninguna otra ventanilla puede operar sobre dicha cuenta, retornando un mensaje de advertencia y exclusión mutua."));

            list.Add(Heading2("3.6 Tecnologías de Interfaz de Usuario en C# (.NET 10)"));
            list.Add(Paragraph("Para demostrar la interoperabilidad del servicio SOAP en .NET, se implementan cuatro tecnologías de cliente heterogéneas:"));
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
                "Directorio: 03. BDD | Archivo: docker-compose.yml | Puerto: 3306 | Usuario: root | Base: eurekabank_db.",
                "Ejecutar 'podman compose up -d' o 'docker compose up -d', luego abrir DBeaver y verificar las 6 tablas creadas.",
                "Contenedor 'mysql-db' en estado Up (puerto 3306 activo) y las tablas pobladas con datos de prueba iniciales."
            ));

            // 4.2 Servidor CoreWCF
            list.Add(Heading2("4.2 Creación del Proyecto Servidor SOAP CoreWCF en .NET 10"));
            list.Add(Paragraph("Para la construcción del servidor SOAP central, se utiliza el SDK de .NET 10 y el framework web Kestrel. Desde la terminal de comandos de PowerShell o Visual Studio 2026/2022, se genera un proyecto web vacío mediante el comando:"));
            list.Add(CodeBox("dotnet new web -n WS_EurekaBank_DotNet_SOAP\ncd WS_EurekaBank_DotNet_SOAP"));

            list.Add(Paragraph("A continuación, se instalan los paquetes NuGet necesarios para el soporte de servicios WCF, conectividad nativa a MySQL y validación criptográfica BCrypt:"));
            list.Add(CodeBox("dotnet add package CoreWCF.Http --version 1.8.0\ndotnet add package CoreWCF.Primitives --version 1.8.0\ndotnet add package MySqlConnector --version 2.5.0\ndotnet add package BCrypt.Net-Next --version 4.0.3"));

            list.Add(TableCaption(4, "Descriptor de Proyecto Servidor CoreWCF WS_EurekaBank_DotNet_SOAP.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WS_EurekaBank_DotNet_SOAP</AssemblyName>
    <NoWarn>$(NoWarn);NU1902;NU1903;NU1904</NoWarn>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="CoreWCF.Http" Version="1.8.0" />
    <PackageReference Include="CoreWCF.Primitives" Version="1.8.0" />
    <PackageReference Include="MySqlConnector" Version="2.5.0" />
    <PackageReference Include="BCrypt.Net-Next" Version="4.0.3" />
  </ItemGroup>
</Project>
"""));

            list.Add(FigureCaption(2, "Creación y Estructura del Proyecto Servidor CoreWCF en Visual Studio / CLI"));
            list.Add(ScreenshotBox(
                2,
                "Creación y Estructura del Proyecto Servidor CoreWCF en Visual Studio / CLI",
                "Visual Studio 2026 / Visual Studio 2022 Community o terminal PowerShell con .NET 10 SDK.",
                "Ruta: 04. SERVIDOR/WS_EurekaBank_DotNet_SOAP | Proyecto: WS_EurekaBank_DotNet_SOAP.csproj | TargetFramework: net10.0.",
                "Ejecutar 'dotnet build' para compilar el proyecto servidor y verificar la resolución de paquetes NuGet.",
                "Compilación limpia sin errores ni advertencias, generando el ensamblado WS_EurekaBank_DotNet_SOAP.dll en bin/Debug/net10.0."
            ));

            // 4.3 Organización Modular MVC
            list.Add(Heading2("4.3 Organización Modular de Namespaces del Servidor (MVC Tradicional)"));
            list.Add(Paragraph("En estricta observancia del patrón arquitectónico MVC Tradicional Puro, el proyecto servidor organiza su código fuente bajo el espacio de nombres institucional ec.edu.monster en tres capas fundamentales:"));

            list.Add(TableCaption(5, "Estructura de Namespaces y Responsabilidades del Servidor SOAP (MVC Tradicional)"));
            list.Add(TechnicalTable(
                new[] { "Namespace", "Archivo Principal", "Responsabilidad en el Patrón MVC Tradicional" },
                new[] { 2200, 2400, 3904 },
                new List<string[]>
                {
                    new[] { "ec.edu.monster.model", "EurekaBankBddModel.cs", "Modelo relacional ACID: conexión MySQL, bloqueo SELECT FOR UPDATE, transacciones y BCrypt." },
                    new[] { "ec.edu.monster.model", "ConcurrenciaMashupModel.cs", "Gestión de exclusión mutua y persistencia del semáforo MashUp (VERDE / ROJO) en base de datos." },
                    new[] { "ec.edu.monster.model", "ModelosDominio.cs", "Contratos de datos ([DataContract]): CuentaModel, MovimientoModel, OperacionModel, UsuarioModel." },
                    new[] { "ec.edu.monster.controller", "EurekaBankSoapController.cs", "Controlador SOAP: define IEurekaBankSoapContrato ([ServiceContract]) y atiende peticiones WCF." },
                    new[] { "ec.edu.monster.view", "EstadoServidorView.cs", "Vista web HTML generada en el endpoint raíz / para diagnóstico y monitoreo de estado." }
                }
            ));

            list.Add(FigureCaption(3, "Explorador de Soluciones y Organización Modular MVC del Servidor en Visual Studio"));
            list.Add(ScreenshotBox(
                3,
                "Explorador de Soluciones y Organización Modular MVC del Servidor en Visual Studio",
                "Entorno de Desarrollo Visual Studio 2026 / 2022 Community.",
                "Solución WS_EurekaBank_DotNet_SOAP.sln con árbol de directorios expandido en el Explorador de Soluciones.",
                "Desplegar las carpetas ec.edu.monster.controller, ec.edu.monster.model y ec.edu.monster.view.",
                "Visualización ordenada de los namespaces con todos los archivos .cs compilados sin errores."
            ));

            // 4.4 Modelos de Dominio y Persistencia
            list.Add(Heading2("4.4 Modelos de Dominio y Capa de Persistencia Relacional"));
            list.Add(Paragraph("Los modelos de datos que viajan a través del protocolo SOAP se decoran con los atributos de serialización [DataContract] y [DataMember] para asegurar su transformación exacta en tipos de esquema XSD dentro del WSDL:"));

            list.Add(TableCaption(6, "Modelos de Dominio y Contratos de Datos ModelosDominio.cs"));
            list.Add(CodeBox("""
using System.Runtime.Serialization;

namespace ec.edu.monster.model
{
    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class UsuarioModel
    {
        [DataMember(Order = 1)] public bool Autenticado { get; set; }
        [DataMember(Order = 2)] public int IdEmpleado { get; set; }
        [DataMember(Order = 3)] public string Usuario { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string NombreCompleto { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string Rol { get; set; } = string.Empty;
        [DataMember(Order = 6)] public int IdSucursal { get; set; }
        [DataMember(Order = 7)] public int Ventanilla { get; set; }
        [DataMember(Order = 8)] public string Token { get; set; } = string.Empty;
        [DataMember(Order = 9)] public string Mensaje { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class SucursalModel
    {
        [DataMember(Order = 1)] public int IdSucursal { get; set; }
        [DataMember(Order = 2)] public string Codigo { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string Nombre { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string Ciudad { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string Direccion { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class CuentaModel
    {
        [DataMember(Order = 1)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 2)] public string Moneda { get; set; } = "USD";
        [DataMember(Order = 3)] public double Saldo { get; set; }
        [DataMember(Order = 4)] public string EstadoCuenta { get; set; } = "Activa";
        [DataMember(Order = 5)] public int IdCliente { get; set; }
        [DataMember(Order = 6)] public string DniCliente { get; set; } = string.Empty;
        [DataMember(Order = 7)] public string NombreCliente { get; set; } = string.Empty;
        [DataMember(Order = 8)] public string ApellidoCliente { get; set; } = string.Empty;
        [DataMember(Order = 9)] public string TelefonoCliente { get; set; } = string.Empty;
        [DataMember(Order = 10)] public string EmailCliente { get; set; } = string.Empty;
        [DataMember(Order = 11)] public int IdSucursal { get; set; }
        [DataMember(Order = 12)] public string NombreSucursal { get; set; } = string.Empty;
        [DataMember(Order = 13)] public string EstadoMashup { get; set; } = "VERDE";
        [DataMember(Order = 14)] public int VentanillaOcupada { get; set; }
        [DataMember(Order = 15)] public string BloqueadoPor { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class MovimientoModel
    {
        [DataMember(Order = 1)] public int IdMovimiento { get; set; }
        [DataMember(Order = 2)] public string NumeroOperacion { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 4)] public int IdEmpleado { get; set; }
        [DataMember(Order = 5)] public string NombreEmpleado { get; set; } = string.Empty;
        [DataMember(Order = 6)] public int Ventanilla { get; set; }
        [DataMember(Order = 7)] public string Tipo { get; set; } = string.Empty;
        [DataMember(Order = 8)] public double Monto { get; set; }
        [DataMember(Order = 9)] public double SaldoResultante { get; set; }
        [DataMember(Order = 10)] public string CuentaReferencia { get; set; } = string.Empty;
        [DataMember(Order = 11)] public string Descripcion { get; set; } = string.Empty;
        [DataMember(Order = 12)] public string FechaHora { get; set; } = string.Empty;
    }

    [DataContract(Namespace = "http://ws.monster.edu.ec/")]
    public class OperacionModel
    {
        [DataMember(Order = 1)] public bool Exito { get; set; }
        [DataMember(Order = 2)] public string Codigo { get; set; } = string.Empty;
        [DataMember(Order = 3)] public string Mensaje { get; set; } = string.Empty;
        [DataMember(Order = 4)] public string NumeroOperacion { get; set; } = string.Empty;
        [DataMember(Order = 5)] public string NumeroCuenta { get; set; } = string.Empty;
        [DataMember(Order = 6)] public double SaldoAnterior { get; set; }
        [DataMember(Order = 7)] public double MontoOperacion { get; set; }
        [DataMember(Order = 8)] public double SaldoActual { get; set; }
        [DataMember(Order = 9)] public string EstadoMashup { get; set; } = "VERDE";
        [DataMember(Order = 10)] public int Ventanilla { get; set; }
        [DataMember(Order = 11)] public string FechaHora { get; set; } = string.Empty;

        public static OperacionModel Error(string codigo, string mensaje, string estadoMashup = "VERDE", int ventanilla = 0)
        {
            return new OperacionModel
            {
                Exito = false,
                Codigo = codigo,
                Mensaje = mensaje,
                EstadoMashup = estadoMashup,
                Ventanilla = ventanilla,
                FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
            };
        }
    }
}
"""));

            list.Add(Paragraph("El gestor de concurrencia ConcurrenciaMashupModel.cs administra la alternancia del semáforo (VERDE / ROJO) mediante exclusión mutua respaldada por la tabla control_mashup de MySQL:"));

            list.Add(TableCaption(7, "Gestor de Concurrencia MashUp ConcurrenciaMashupModel.cs"));
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

        public static void DesbloquearAdministrativo(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (string.IsNullOrEmpty(cta)) return;

            lock (SyncLock)
            {
                BloqueosActivos.TryRemove(cta, out _);
                PersistirEstadoMashup(cta, "VERDE", null, null);
                try
                {
                    using var con = new MySqlConnection(ConnectionString);
                    con.Open();
                    using var cmd = new MySqlCommand("UPDATE cuenta SET estado = 'Activa' WHERE numero_cuenta = @cta", con);
                    cmd.Parameters.AddWithValue("@cta", cta);
                    cmd.ExecuteNonQuery();
                }
                catch { }
            }
        }

        public static InfoBloqueo ObtenerInfoBloqueo(string numeroCuenta)
        {
            string cta = (numeroCuenta ?? string.Empty).Trim();
            if (BloqueosActivos.TryGetValue(cta, out var local)) return local;

            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var cmd = new MySqlCommand(
                    "SELECT estado_mashup, COALESCE(ventanilla, 1), COALESCE(bloqueado_por, 'Operador') FROM control_mashup WHERE numero_cuenta = @cta",
                    con);
                cmd.Parameters.AddWithValue("@cta", cta);
                using var rd = cmd.ExecuteReader();
                if (rd.Read() && string.Equals(rd.GetString(0), "ROJO", StringComparison.OrdinalIgnoreCase))
                {
                    return new InfoBloqueo { NumeroCuenta = cta, Ventanilla = rd.GetInt32(1), Usuario = rd.GetString(2) };
                }
            }
            catch { }

            return new InfoBloqueo { NumeroCuenta = cta, Ventanilla = 1, Usuario = "Ventanilla Activa" };
        }

        private static bool EstaRojoEnBdd(string cta)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var cmd = new MySqlCommand("SELECT estado_mashup FROM control_mashup WHERE numero_cuenta = @cta", con);
                cmd.Parameters.AddWithValue("@cta", cta);
                var val = cmd.ExecuteScalar()?.ToString();
                return string.Equals(val, "ROJO", StringComparison.OrdinalIgnoreCase);
            }
            catch { return false; }
        }

        private static void PersistirEstadoMashup(string cta, string estado, int? ventanilla, string? usuario)
        {
            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                const string sql = @"
                    INSERT INTO control_mashup (numero_cuenta, estado_mashup, ventanilla, bloqueado_por)
                    VALUES (@cta, @est, @ven, @usr)
                    ON DUPLICATE KEY UPDATE
                        estado_mashup = VALUES(estado_mashup),
                        ventanilla = VALUES(ventanilla),
                        bloqueado_por = VALUES(bloqueado_por),
                        actualizado_en = CURRENT_TIMESTAMP;";
                using var cmd = new MySqlCommand(sql, con);
                cmd.Parameters.AddWithValue("@cta", cta);
                cmd.Parameters.AddWithValue("@est", estado);
                cmd.Parameters.AddWithValue("@ven", ventanilla.HasValue ? ventanilla.Value : DBNull.Value);
                cmd.Parameters.AddWithValue("@usr", !string.IsNullOrEmpty(usuario) ? usuario : DBNull.Value);
                cmd.ExecuteNonQuery();
            }
            catch { }
        }
    }
}
"""));

            list.Add(Paragraph("La clase EurekaBankBddModel.cs implementa las transacciones con aislamiento ReadCommitted, bloqueo de filas pesimista (SELECT ... FOR UPDATE) y control de retardo (LockDelayMs = 4500):"));

            list.Add(TableCaption(8, "Capa de Persistencia Relacional y Transacciones ACID EurekaBankBddModel.cs"));
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
            if (string.IsNullOrWhiteSpace(usuario) || string.IsNullOrWhiteSpace(clave))
                return new UsuarioModel { Autenticado = false, Mensaje = "Debe ingresar usuario y contraseña." };

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
                    return new UsuarioModel { Autenticado = false, Mensaje = "Credenciales inválidas. Verifique usuario y contraseña." };

                string hash = rd.GetString(2);
                bool ok = hash.StartsWith("$2") ? BCrypt.Net.BCrypt.Verify(clave, hash) : string.Equals(clave, hash, StringComparison.Ordinal);
                if (!ok)
                    return new UsuarioModel { Autenticado = false, Mensaje = "Contraseña incorrecta para el operador indicado." };

                return new UsuarioModel
                {
                    Autenticado = true,
                    IdEmpleado = rd.GetInt32(0),
                    Usuario = rd.GetString(1),
                    NombreCompleto = rd.GetString(3),
                    Rol = rd.GetString(4),
                    IdSucursal = rd.GetInt32(5),
                    Ventanilla = rd.GetInt32(6),
                    Token = $"MONSTER-DOTNET-{Guid.NewGuid():N}",
                    Mensaje = "Autenticación exitosa en EurekaBank (.NET)."
                };
            }
            catch (Exception ex)
            {
                return new UsuarioModel { Autenticado = false, Mensaje = "Error de conexión con la base de datos: " + ex.Message };
            }
        }

        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla)
        {
            if (string.IsNullOrWhiteSpace(numeroCuenta))
                return OperacionModel.Error("ERROR_VALIDACION", "Debe especificar el número de cuenta.");
            if (monto <= 0)
                return OperacionModel.Error("ERROR_VALIDACION", "El monto del depósito debe ser mayor a $0.00.");

            string cta = numeroCuenta.Trim();
            int nroVentanilla = ventanilla <= 0 ? 1 : ventanilla;
            int empId = idEmpleado <= 0 ? 1 : idEmpleado;
            string nombreUsuario = ObtenerUsuarioEmpleado(empId);

            if (!ConcurrenciaMashupModel.BloquearCuenta(cta, nroVentanilla, nombreUsuario))
            {
                var info = ConcurrenciaMashupModel.ObtenerInfoBloqueo(cta);
                return OperacionModel.Error(
                    "CONCURRENCIA_BLOQUEADA",
                    $"CONCURRENCIA [ROJO]: La cuenta {cta} está siendo operada actualmente en Ventanilla {info.Ventanilla} ({info.Usuario}). Intente en unos segundos.",
                    "ROJO",
                    info.Ventanilla);
            }

            try
            {
                using var con = new MySqlConnection(ConnectionString);
                con.Open();
                using var tx = con.BeginTransaction(IsolationLevel.ReadCommitted);

                var infoCuenta = LeerCuentaForUpdate(con, tx, cta);
                if (infoCuenta == null) { tx.Rollback(); return OperacionModel.Error("CUENTA_NO_EXISTE", $"La cuenta {cta} no existe."); }
                if (string.Equals(infoCuenta.Value.Estado, "Bloqueada", StringComparison.OrdinalIgnoreCase))
                {
                    tx.Rollback();
                    return OperacionModel.Error("CUENTA_BLOQUEADA", $"La cuenta {cta} tiene bloqueo administrativo. Solicite desbloqueo a Monster.");
                }

                if (LockDelayMs > 0) Thread.Sleep(LockDelayMs);

                decimal saldoAnterior = infoCuenta.Value.Saldo;
                decimal montoDec = Math.Round((decimal)monto, 2);
                decimal nuevoSaldo = Math.Round(saldoAnterior + montoDec, 2);

                ActualizarSaldoCuenta(con, tx, cta, nuevoSaldo);
                string nroOp = GenerarNumeroOperacion("DEP");
                InsertarMovimiento(con, tx, nroOp, cta, empId, nroVentanilla, "Deposito", montoDec, nuevoSaldo, null,
                    $"Depósito en efectivo Ventanilla {nroVentanilla}");

                tx.Commit();
                return new OperacionModel
                {
                    Exito = true,
                    Codigo = "OK",
                    Mensaje = $"Depósito de ${montoDec:F2} acreditado exitosamente en cuenta {cta} (Ventanilla {nroVentanilla}).",
                    NumeroOperacion = nroOp,
                    NumeroCuenta = cta,
                    SaldoAnterior = (double)saldoAnterior,
                    MontoOperacion = (double)montoDec,
                    SaldoActual = (double)nuevoSaldo,
                    EstadoMashup = "VERDE",
                    Ventanilla = nroVentanilla,
                    FechaHora = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                };
            }
            catch (Exception ex)
            {
                return OperacionModel.Error("ERROR_TRANSACCION", "Transacción revertida (ROLLBACK): " + ex.Message);
            }
            finally
            {
                ConcurrenciaMashupModel.LiberarCuenta(cta);
            }
        }
    }
}
"""));

            // 4.5 Contrato e Implementación del Servicio SOAP
            list.Add(Heading2("4.5 Contrato e Implementación del Servicio SOAP CoreWCF"));
            list.Add(Paragraph("En CoreWCF, el contrato del servicio se define a través de una interfaz de C# decorada con el atributo [ServiceContract] y cada método expuesto con [OperationContract]. La clase controladora EurekaBankSoapController implementa dicho contrato delegando las operaciones al modelo relacional:"));

            list.Add(TableCaption(9, "Contrato e Implementación del Servicio SOAP EurekaBankSoapController.cs"));
            list.Add(CodeBox("""
using CoreWCF;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    [ServiceContract(Namespace = "http://ws.monster.edu.ec/", Name = "EurekaBankWS")]
    public interface IEurekaBankSoapContrato
    {
        [OperationContract] UsuarioModel AutenticarUsuario(string usuario, string clave);
        [OperationContract] List<SucursalModel> ListarSucursales();
        [OperationContract] List<CuentaModel> ListarCuentasPorSucursal(int sucursalId);
        [OperationContract] CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta);
        [OperationContract] List<CuentaModel> ConsultarCuentasPorCliente(string dni);
        [OperationContract] List<MovimientoModel> ConsultarExtracto(string numeroCuenta);
        [OperationContract] OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel Transferir(string cuentaOrigen, string cuentaDestino, double monto, int idEmpleado, int ventanilla);
        [OperationContract] OperacionModel ConsultarEstadoMashUp(string numeroCuenta);
        [OperationContract] OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin);
        [OperationContract] OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario);
    }

    [ServiceBehavior(
        IncludeExceptionDetailInFaults = true,
        InstanceContextMode = InstanceContextMode.Single,
        ConcurrencyMode = ConcurrencyMode.Multiple)]
    public class EurekaBankSoapController : IEurekaBankSoapContrato
    {
        private readonly EurekaBankBddModel _modelo = new();

        public UsuarioModel AutenticarUsuario(string usuario, string clave) => _modelo.AutenticarUsuario(usuario, clave);
        public List<SucursalModel> ListarSucursales() => _modelo.ListarSucursales();
        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId) => _modelo.ListarCuentasPorSucursal(sucursalId);
        public CuentaModel? ObtenerCuentaPorNumero(string numeroCuenta) => _modelo.ObtenerCuentaPorNumero(numeroCuenta);
        public List<CuentaModel> ConsultarCuentasPorCliente(string dni) => _modelo.ConsultarCuentasPorCliente(dni);
        public List<MovimientoModel> ConsultarExtracto(string numeroCuenta) => _modelo.ConsultarExtracto(numeroCuenta);
        public OperacionModel Depositar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Depositar(numeroCuenta, monto, idEmpleado, ventanilla);
        public OperacionModel Retirar(string numeroCuenta, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Retirar(numeroCuenta, monto, idEmpleado, ventanilla);
        public OperacionModel Transferir(string origen, string destino, double monto, int idEmpleado, int ventanilla) =>
            _modelo.Transferir(origen, destino, monto, idEmpleado, ventanilla);
        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta) => _modelo.ConsultarEstadoMashUp(numeroCuenta);
        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin) => _modelo.DesbloquearCuenta(numeroCuenta, usuarioAdmin);
        public OperacionModel BloquearCuentaManual(string numeroCuenta, int ventanilla, string usuario) => _modelo.BloquearCuentaManual(numeroCuenta, ventanilla, usuario);
    }
}
"""));

            // 4.6 Configuración de Kestrel y Endpoints
            list.Add(Heading2("4.6 Configuración del Host Kestrel y Endpoints CoreWCF"));
            list.Add(Paragraph("El archivo appsettings.json configura la URL de escucha en el puerto TCP 5120, los parámetros de conexión a MySQL y el retardo pedagógico del semáforo:"));

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
  "Urls": "http://0.0.0.0:5120",
  "EurekaBank": {
    "ConnectionString": "Server=127.0.0.1;Port=3306;Database=eurekabank_db;User ID=root;Password=admin;SslMode=None;AllowPublicKeyRetrieval=True;",
    "LockDelayMs": 4500
  }
}
"""));

            list.Add(Paragraph("El punto de entrada Program.cs inicializa el contenedor de inyección de dependencias, registra los servicios de CoreWCF, habilita la publicación de metadatos WSDL mediante ServiceMetadataBehavior y mapea el endpoint SOAP /EurekaBankWS.svc:"));

            list.Add(TableCaption(11, "Configuración de Kestrel, Metadatos y Endpoints CoreWCF Program.cs"));
            list.Add(CodeBox("""
using CoreWCF;
using CoreWCF.Channels;
using CoreWCF.Configuration;
using CoreWCF.Description;
using ec.edu.monster.controller;
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

builder.Services.AddServiceModelServices();
builder.Services.AddServiceModelMetadata();
builder.Services.AddSingleton<EurekaBankSoapController>();

var app = builder.Build();

app.UseServiceModel(serviceBuilder =>
{
    serviceBuilder.AddService<EurekaBankSoapController>(options =>
    {
        options.DebugBehavior.IncludeExceptionDetailInFaults = true;
    });

    var binding = new BasicHttpBinding(BasicHttpSecurityMode.None)
    {
        MaxReceivedMessageSize = 10 * 1024 * 1024
    };

    serviceBuilder.AddServiceEndpoint<EurekaBankSoapController, IEurekaBankSoapContrato>(
        binding, "/EurekaBankWS.svc");

    var metadataBehavior = app.Services.GetRequiredService<ServiceMetadataBehavior>();
    metadataBehavior.HttpGetEnabled = true;
});

app.MapGet("/", () =>
{
    var bdd = new EurekaBankBddModel();
    var cuentas = bdd.ListarCuentasPorSucursal(0);
    return Results.Content(EstadoServidorView.RenderizarHtml(cuentas), "text/html; charset=utf-8");
});

app.Run();
"""));

            list.Add(TableCaption(12, "Vista HTML de Diagnóstico del Servidor EstadoServidorView.cs"));
            list.Add(CodeBox("""
using System.Text;
using ec.edu.monster.model;

namespace ec.edu.monster.view
{
    public static class EstadoServidorView
    {
        public static string RenderizarHtml(List<CuentaModel> cuentas)
        {
            var sb = new StringBuilder();
            sb.Append("<!DOCTYPE html><html><head><meta charset='utf-8'/><title>EurekaBank SOAP .NET 10</title>");
            sb.Append("<style>body{font-family:sans-serif;background:#0f172a;color:#f8fafc;padding:24px;}");
            sb.Append(".card{background:#1e293b;padding:20px;border-radius:8px;max-width:900px;margin:auto;}");
            sb.Append("table{width:100%;border-collapse:collapse;margin-top:16px;}th,td{padding:8px 12px;border:1px solid #334155;}");
            sb.Append(".badge-green{background:#166534;color:#dcfce7;padding:4px 8px;border-radius:4px;}");
            sb.Append(".badge-red{background:#991b1b;color:#fee2e2;padding:4px 8px;border-radius:4px;}</style></head><body>");
            sb.Append("<div class='card'><h2>EurekaBank CoreWCF SOAP Server (.NET 10)</h2>");
            sb.Append("<p>Dominio: <b>ec.edu.monster</b> | WSDL: <a style='color:#38bdf8' href='/EurekaBankWS.svc?wsdl'>/EurekaBankWS.svc?wsdl</a></p>");
            sb.Append("<table><tr><th>Cuenta</th><th>Titular</th><th>Saldo</th><th>Semáforo MashUp</th></tr>");
            foreach (var c in cuentas)
            {
                string cls = c.EstadoMashup == "ROJO" ? "badge-red" : "badge-green";
                sb.Append($"<tr><td>{c.NumeroCuenta}</td><td>{c.NombreCliente} {c.ApellidoCliente}</td><td>${c.Saldo:F2}</td><td><span class='{cls}'>{c.EstadoMashup}</span></td></tr>");
            }
            sb.Append("</table></div></body></html>");
            return sb.ToString();
        }
    }
}
"""));

            list.Add(FigureCaption(4, "Contrato WSDL Publicado en Kestrel (http://localhost:5120/EurekaBankWS.svc?wsdl)"));
            list.Add(ScreenshotBox(
                4,
                "Contrato WSDL Publicado en Kestrel (http://localhost:5120/EurekaBankWS.svc?wsdl)",
                "Navegador web Google Chrome o Microsoft Edge.",
                "Servidor SOAP en ejecución en http://localhost:5120 | Ruta WSDL: /EurekaBankWS.svc?wsdl.",
                "Navegar a la URL del contrato WSDL e inspeccionar las operaciones y tipos complejos expuestos.",
                "Estructura XML conteniendo wsdl:definitions, el targetNamespace http://ws.monster.edu.ec/ y los contratos transaccionales."
            ));

            list.Add(FigureCaption(5, "Interfaz Web de Diagnóstico del Servidor en el Puerto 5120"));
            list.Add(ScreenshotBox(
                5,
                "Interfaz Web de Diagnóstico del Servidor en el Puerto 5120",
                "Navegador web Google Chrome o Microsoft Edge.",
                "URL raíz del host SOAP: http://localhost:5120/.",
                "Abrir la página de inicio del servidor Kestrel para verificar la conexión directa a MySQL y el listado de cuentas.",
                "Página web oscura renderizando el estado del servidor CoreWCF, el listado de cuentas con saldo y semáforos en VERDE."
            ));

            // 4.7 Desarrollo del Cliente Consola
            list.Add(Heading2("4.7 Desarrollo del Cliente de Consola (WSEurekaBankConsoleClient)"));
            list.Add(Paragraph("El Cliente Consola es una aplicación ejecutable en .NET 10 diseñada para terminales bancarias y tareas de diagnóstico. Consume el servicio SOAP mediante ChannelFactory<IEurekaBankSoapContrato> sin generar stubs pesados, utilizando la librería estándar System.ServiceModel.Primitives:"));

            list.Add(FigureCaption(6, "Creación del Proyecto Cliente Consola en Visual Studio / CLI"));
            list.Add(ScreenshotBox(
                6,
                "Creación del Proyecto Cliente Consola en Visual Studio / CLI",
                "Terminal PowerShell con .NET 10 SDK o Visual Studio 2026/2022.",
                "Directorio: 04. CLICON/WSEurekaBankConsoleClient | Proyecto: WSEurekaBankConsoleClient.csproj | OutputType: Exe.",
                "Ejecutar 'dotnet new console -n WSEurekaBankConsoleClient' e instalar paquetes System.ServiceModel.Http y Primitives 8.1.2.",
                "Proyecto creado con target framework net10.0 y referencias de NuGet resueltas."
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
    <NoWarn>$(NoWarn);NU1901;NU1902;NU1903;NU1904</NoWarn>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="System.ServiceModel.Http" Version="8.1.2" />
    <PackageReference Include="System.ServiceModel.Primitives" Version="8.1.2" />
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(14, "Modelo de Consumo SOAP ChannelFactory EurekaBankSoapModel.cs (Consola)"));
            list.Add(CodeBox("""
using System.ServiceModel;

namespace ec.edu.monster.model
{
    public class EurekaBankSoapModel
    {
        public string EndpointUrl { get; set; } = "http://localhost:5120/EurekaBankWS.svc";

        private T Ejecutar<T>(Func<IEurekaBankSoapContrato, T> accion)
        {
            var binding = new BasicHttpBinding(BasicHttpSecurityMode.None)
            {
                MaxReceivedMessageSize = 10 * 1024 * 1024,
                SendTimeout = TimeSpan.FromSeconds(30),
                ReceiveTimeout = TimeSpan.FromSeconds(30)
            };
            using var factory = new ChannelFactory<IEurekaBankSoapContrato>(binding, new EndpointAddress(EndpointUrl));
            var canal = factory.CreateChannel();
            try
            {
                var res = accion(canal);
                ((IClientChannel)canal).Close();
                return res;
            }
            catch
            {
                ((IClientChannel)canal).Abort();
                throw;
            }
        }

        public UsuarioModel Autenticar(string usuario, string clave)
        {
            try { return Ejecutar(c => c.AutenticarUsuario(usuario, clave)); }
            catch (Exception ex) { return new UsuarioModel { Autenticado = false, Mensaje = "Error SOAP: " + ex.Message }; }
        }

        public List<CuentaModel> ListarCuentasPorSucursal(int sucursalId)
        {
            try { return Ejecutar(c => c.ListarCuentasPorSucursal(sucursalId)); }
            catch { return new List<CuentaModel>(); }
        }

        public OperacionModel Depositar(string cuenta, double monto, int idEmpleado, int ventanilla)
        {
            try { return Ejecutar(c => c.Depositar(cuenta, monto, idEmpleado, ventanilla)); }
            catch (Exception ex) { return new OperacionModel { Exito = false, Mensaje = "Error SOAP: " + ex.Message }; }
        }

        public OperacionModel Retirar(string cuenta, double monto, int idEmpleado, int ventanilla)
        {
            try { return Ejecutar(c => c.Retirar(cuenta, monto, idEmpleado, ventanilla)); }
            catch (Exception ex) { return new OperacionModel { Exito = false, Mensaje = "Error SOAP: " + ex.Message }; }
        }

        public OperacionModel Transferir(string origen, string destino, double monto, int idEmpleado, int ventanilla)
        {
            try { return Ejecutar(c => c.Transferir(origen, destino, monto, idEmpleado, ventanilla)); }
            catch (Exception ex) { return new OperacionModel { Exito = false, Mensaje = "Error SOAP: " + ex.Message }; }
        }

        public OperacionModel ConsultarEstadoMashUp(string numeroCuenta)
        {
            try { return Ejecutar(c => c.ConsultarEstadoMashUp(numeroCuenta)); }
            catch (Exception ex) { return new OperacionModel { Exito = false, Mensaje = "Error SOAP: " + ex.Message }; }
        }

        public OperacionModel DesbloquearCuenta(string numeroCuenta, string usuarioAdmin)
        {
            try { return Ejecutar(c => c.DesbloquearCuenta(numeroCuenta, usuarioAdmin)); }
            catch (Exception ex) { return new OperacionModel { Exito = false, Mensaje = "Error SOAP: " + ex.Message }; }
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
        private readonly EurekaBankSoapModel _modelo = new();
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
        public List<CuentaModel> BuscarCuentasPorCliente(string dni) => _modelo.ConsultarCuentasPorCliente(dni);
        public List<MovimientoModel> ObtenerExtracto(string cuenta) => _modelo.ConsultarExtracto(cuenta);

        public OperacionModel Depositar(string cuenta, double monto) =>
            _modelo.Depositar(cuenta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel DepositarEnVentanilla(string cuenta, double monto, int ventanilla) =>
            _modelo.Depositar(cuenta, monto, SesionActual?.IdEmpleado ?? 1, ventanilla);

        public OperacionModel Retirar(string cuenta, double monto) =>
            _modelo.Retirar(cuenta, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel Transferir(string origen, string destino, double monto) =>
            _modelo.Transferir(origen, destino, monto, SesionActual?.IdEmpleado ?? 1, VentanillaActiva);

        public OperacionModel ConsultarEstadoMashUp(string cuenta) =>
            _modelo.ConsultarEstadoMashUp(cuenta);

        public OperacionModel DesbloquearCuenta(string cuenta) =>
            _modelo.DesbloquearCuenta(cuenta, SesionActual?.Usuario ?? "Monster");
    }
}
"""));

            list.Add(TableCaption(16, "Vista de Interfaz de Consola y Semáforo ANSI ConsolaView.cs"));
            list.Add(CodeBox("""
using System.Globalization;
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
            if (args.Any(a => string.Equals(a, "--demo", StringComparison.OrdinalIgnoreCase)))
            {
                EjecutarDemostracionAutomatica();
                return;
            }

            MostrarBanner();
            while (_controlador.SesionActual == null)
            {
                Console.Write("\nUsuario (ej. Monster): ");
                string usr = Console.ReadLine()?.Trim() ?? "";
                Console.Write("Contraseña: ");
                string pwd = Console.ReadLine()?.Trim() ?? "";

                var login = _controlador.IniciarSesion(usr, pwd);
                if (login.Autenticado)
                {
                    Console.ForegroundColor = ConsoleColor.Green;
                    Console.WriteLine($"[OK] Bienvenido {login.NombreCompleto} | Rol: {login.Rol} | Ventanilla: {_controlador.VentanillaActiva}");
                    Console.ResetColor();
                }
                else
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine($"[ERROR] {login.Mensaje}");
                    Console.ResetColor();
                }
            }

            bool salir = false;
            while (!salir)
            {
                Console.WriteLine("\n=====================================================================");
                Console.WriteLine($" EUREKABANK CONSOLA SOAP .NET | Ventanilla {_controlador.VentanillaActiva} | Operador: {_controlador.SesionActual.Usuario}");
                Console.WriteLine("=====================================================================");
                Console.WriteLine(" 1. Monitorear Semáforo MashUp de Cuentas (VERDE / ROJO)");
                Console.WriteLine(" 2. Consultar Cuenta por Número o DNI");
                Console.WriteLine(" 3. Consultar Extracto Bancario");
                Console.WriteLine(" 4. Realizar Depósito");
                Console.WriteLine(" 5. Realizar Retiro");
                Console.WriteLine(" 6. Realizar Transferencia entre Cuentas");
                Console.WriteLine(" 7. Simular Concurrencia Crítica (3 Ventanillas Simultáneas)");
                Console.WriteLine(" 8. Cambiar Ventanilla Activa (1..4)");
                Console.WriteLine(" 9. Desbloquear Cuenta (Superadmin Monster / Gerencia)");
                Console.WriteLine(" 0. Salir");
                Console.Write("Seleccione una opción: ");

                switch (Console.ReadLine()?.Trim())
                {
                    case "1": ImprimirCuentas(_controlador.ObtenerCuentasPorSucursal(0)); break;
                    case "4": RealizarDeposito(); break;
                    case "6": RealizarTransferencia(); break;
                    case "7": SimularTresVentanillas("001-0001"); break;
                    case "0": salir = true; break;
                }
            }
        }

        private void SimularTresVentanillas(string cuenta)
        {
            Console.WriteLine($"\n[SIMULACIÓN] Disparando 3 ventanillas concurrentes sobre la cuenta {cuenta}...");
            var t1 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 50.00, 1));
            Thread.Sleep(350);
            var estadoDurante = _controlador.ConsultarEstadoMashUp(cuenta);
            Console.WriteLine($"  -> [MONITOR MASHUP EN VIVO] Cuenta {cuenta}: Estado={estadoDurante.EstadoMashup} | {estadoDurante.Mensaje}");

            var t2 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 30.00, 2));
            var t3 = Task.Run(() => _controlador.DepositarEnVentanilla(cuenta, 20.00, 3));

            Task.WaitAll(t1, t2, t3);
            Console.WriteLine("  -> [Ventanilla 1]: " + (t1.Result.Exito ? "PROCESADA" : t1.Result.Mensaje));
            Console.WriteLine("  -> [Ventanilla 2]: " + (t2.Result.Exito ? "PROCESADA" : t2.Result.Mensaje));
            Console.WriteLine("  -> [Ventanilla 3]: " + (t3.Result.Exito ? "PROCESADA" : t3.Result.Mensaje));
        }
    }
}
"""));

            list.Add(FigureCaption(7, "Ejecución del Cliente Consola e Inicio de Sesión de Operador (Monster)"));
            list.Add(ScreenshotBox(
                7,
                "Ejecución del Cliente Consola e Inicio de Sesión de Operador (Monster)",
                "Consola de comandos PowerShell o Terminal de Windows.",
                "Ruta: 04. CLICON/WSEurekaBankConsoleClient | Usuario: Monster | Contraseña: Monster9 | Puesto: Superadmin Monster.",
                "Ejecutar 'dotnet run' y suministrar las credenciales maestras autorizadas del sistema.",
                "Mensaje '[OK] Bienvenido James P. Sullivan | Rol: Superadmin Monster | Ventanilla: 1' y despliegue del menú interactivo."
            ));

            list.Add(FigureCaption(8, "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola"));
            list.Add(ScreenshotBox(
                8,
                "Consulta de Cuentas Bancarias con Semáforo MashUp en Consola",
                "Consola de comandos en ejecución.",
                "Opción seleccionada: 1 (Monitorear Semáforo MashUp de Cuentas).",
                "Ingresar '1' para consultar en tiempo real las 6 cuentas bancarias desde el servicio SOAP.",
                "Tabla con columnas CUENTA, TITULAR, SALDO USD y semáforo cromático con etiquetas 'VERDE (Libre)'."
            ));

            list.Add(FigureCaption(9, "Simulación de 3 Ventanillas Simultáneas en Cliente Consola"));
            list.Add(ScreenshotBox(
                9,
                "Simulación de 3 Ventanillas Simultáneas en Cliente Consola",
                "Consola de comandos en ejecución.",
                "Opción seleccionada: 7 | Cuenta objetivo: 001-0001 | Ventanillas: 1, 2 y 3 ejecutando depósitos en paralelo.",
                "Seleccionar la opción 7 del menú para disparar tareas asíncronas simultáneas mediante Task.Run.",
                "Ventanilla 1 adquiere el bloqueo (estado ROJO) y procesa la transacción, mientras Ventanilla 2 y 3 son rechazadas por exclusión mutua."
            ));

            // 4.8 Desarrollo del Cliente Escritorio Windows Forms
            list.Add(Heading2("4.8 Desarrollo del Cliente de Escritorio (WSEurekaBankDesktopClient)"));
            list.Add(Paragraph("El Cliente de Escritorio se desarrolla sobre Windows Forms en .NET 10 (TargetFramework: net10.0-windows). Incorpora formularios interactivos, validaciones numéricas, un DataGridView con renderizado de colores en tiempo real y temporizadores asíncronos para refresco periódico:"));

            list.Add(TableCaption(17, "Descriptor de Proyecto Cliente Escritorio WSEurekaBankDesktopClient.csproj"));
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
    <NoWarn>$(NoWarn);NU1901;NU1902;NU1903;NU1904</NoWarn>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="System.ServiceModel.Http" Version="8.1.2" />
    <PackageReference Include="System.ServiceModel.Primitives" Version="8.1.2" />
  </ItemGroup>

  <ItemGroup>
    <None Update="images\**\*">
      <CopyToOutputDirectory>PreserveNewest</CopyToOutputDirectory>
    </None>
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(18, "Formulario de Autenticación de Ventanilla LoginView.cs"));
            list.Add(CodeBox("""
using System.Drawing;
using System.IO;
using System.Windows.Forms;
using ec.edu.monster.controller;

namespace ec.edu.monster.view
{
    public class LoginView : Form
    {
        private readonly AppControlador _controlador = new();
        private readonly TextBox _txtUsuario = new() { Text = "Monster", Width = 280, Font = new Font("Segoe UI", 10.5f) };
        private readonly TextBox _txtClave = new() { Text = "Monster9", Width = 280, UseSystemPasswordChar = true, Font = new Font("Segoe UI", 10.5f) };
        private readonly ComboBox _cmbVentanilla = new() { DropDownStyle = ComboBoxStyle.DropDownList, Width = 280, Font = new Font("Segoe UI", 10f) };
        private readonly TextBox _txtEndpoint = new() { Width = 280, Font = new Font("Segoe UI", 9f) };
        private readonly Button _btnIngresar = new() { Text = "INGRESAR A ESTACIÓN BANCARIA", Width = 280, Height = 40, BackColor = Color.FromArgb(37, 99, 235), ForeColor = Color.White };

        public LoginView()
        {
            Text = "EurekaBank SOAP .NET - Autenticación de Ventanilla (GR04)";
            Size = new Size(440, 560);
            StartPosition = FormStartPosition.CenterScreen;
            BackColor = Color.FromArgb(15, 23, 42);

            _txtEndpoint.Text = _controlador.EndpointActual;
            _cmbVentanilla.Items.AddRange(new object[] { "Ventanilla 1", "Ventanilla 2", "Ventanilla 3", "Ventanilla 4" });
            _cmbVentanilla.SelectedIndex = 0;

            _btnIngresar.Click += async (_, _) =>
            {
                var res = _controlador.IniciarSesion(_txtUsuario.Text.Trim(), _txtClave.Text.Trim());
                if (res.Autenticado)
                {
                    _controlador.VentanillaActiva = _cmbVentanilla.SelectedIndex + 1;
                    Hide();
                    var dash = new DashboardView(_controlador);
                    dash.FormClosed += (s, ev) => Close();
                    dash.Show();
                }
                else
                {
                    MessageBox.Show(res.Mensaje, "Error de Acceso", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
            };
        }
    }
}
"""));

            list.Add(TableCaption(19, "Formulario Principal con DataGridView Cromático DashboardView.cs"));
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
        private readonly System.Windows.Forms.Timer _timerMashup;
        private readonly DataGridView _gridCuentas = new() { Dock = DockStyle.Fill, ReadOnly = true, SelectionMode = DataGridViewSelectionMode.FullRowSelect };

        public DashboardView(AppControlador controlador)
        {
            _controlador = controlador;
            Text = $"EurekaBank Estación de Ventanilla (SOAP .NET) — Operador: {_controlador.SesionActual?.NombreCompleto}";
            Size = new Size(1320, 780);
            StartPosition = FormStartPosition.CenterScreen;

            _timerMashup = new System.Windows.Forms.Timer { Interval = 1200 };
            _timerMashup.Tick += async (_, _) => await RefrescarCuentasAsync();
            _timerMashup.Start();
        }

        private async Task RefrescarCuentasAsync()
        {
            var cuentas = await Task.Run(() => _controlador.ObtenerCuentasPorSucursal(0));
            _gridCuentas.Rows.Clear();
            foreach (var c in cuentas)
            {
                bool esRojo = string.Equals(c.EstadoMashup, "ROJO", StringComparison.OrdinalIgnoreCase);
                int idx = _gridCuentas.Rows.Add(c.NumeroCuenta, c.TitularCompleto, c.DniCliente, c.NombreSucursal, $"${c.Saldo:F2}", c.EstadoMashup);
                var row = _gridCuentas.Rows[idx];
                row.DefaultCellStyle.BackColor = esRojo ? Color.FromArgb(254, 226, 226) : Color.FromArgb(220, 252, 231);
                row.DefaultCellStyle.ForeColor = esRojo ? Color.FromArgb(153, 27, 27) : Color.FromArgb(22, 101, 52);
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
                "Directorio: 04. CLIESC/WSEurekaBankDesktopClient | Usuario: Monster | Contraseña: Monster9 | Ventanilla: 1 | Endpoint: http://localhost:5120/EurekaBankWS.svc.",
                "Digitar credenciales, seleccionar la Ventanilla 1 física y presionar el botón 'INGRESAR A ESTACIÓN BANCARIA'.",
                "Autenticación exitosa, cierre automático de LoginView y apertura inmediata del panel bancario DashboardView."
            ));

            list.Add(FigureCaption(11, "Panel Principal del Dashboard Bancario con DataGridView y Semáforo Cromático en Tiempo Real"));
            list.Add(ScreenshotBox(
                11,
                "Panel Principal del Dashboard Bancario con DataGridView y Semáforo Cromático en Tiempo Real",
                "Formulario principal DashboardView.",
                "Cuadrícula DataGridView mostrando todas las cuentas bancarias con temporizador a 1.2 segundos.",
                "Verificar la carga inicial de cuentas sincronizadas con saldos en USD y filas resaltadas en verde suave (#DCFCE7).",
                "Visualización fluida de cuentas con semáforos VERDE (Libre) y panel de operaciones transaccionales listo."
            ));

            list.Add(FigureCaption(12, "Ejecución de Transacción de Depósito en Cliente Escritorio"));
            list.Add(ScreenshotBox(
                12,
                "Ejecución de Transacción de Depósito en Cliente Escritorio",
                "Panel lateral de Operaciones Transaccionales en DashboardView.",
                "Cuenta: 001-0001 | Monto: 150.00 USD | Ventanilla: 1.",
                "Hacer clic en el botón 'ACREDITAR DEPÓSITO (+)' y observar el bloqueo de cuenta.",
                "La fila cambia temporalmente a rojo durante la operación (4.5 segundos) y se acredita el nuevo saldo con confirmación en pantalla."
            ));

            list.Add(FigureCaption(13, "Ejecución de Operación de Transferencia con Verificación ACID en Escritorio"));
            list.Add(ScreenshotBox(
                13,
                "Ejecución de Operación de Transferencia con Verificación ACID en Escritorio",
                "Formulario de Transferencia entre Cuentas en DashboardView.",
                "Cuenta Origen: 001-0001 | Cuenta Destino: 001-0002 | Monto: 100.00 USD.",
                "Presionar 'TRANSFERIR ENTRE CUENTAS (⇄)' verificando el orden canónico para evitar interbloqueos.",
                "Débito y crédito aplicados atómicamente, actualizando los saldos de ambas cuentas de forma sincronizada."
            ));

            // 4.9 Desarrollo del Cliente Móvil .NET MAUI
            list.Add(Heading2("4.9 Desarrollo del Cliente Móvil (.NET MAUI)"));
            list.Add(Paragraph("El Cliente Móvil se desarrolla sobre .NET MAUI (Multi-platform App UI) en .NET 10, optimizado para ejecutarse nativamente en el emulador y dispositivos físicos Android. Utiliza vistas XAML declarativas, enlace de datos en C# puro bajo MVC y manejo de la dirección IP de loopback del emulador Android (10.0.2.2:5120):"));

            list.Add(FigureCaption(14, "Creación y Estructura del Proyecto Móvil .NET MAUI en Visual Studio"));
            list.Add(ScreenshotBox(
                14,
                "Creación y Estructura del Proyecto Móvil .NET MAUI en Visual Studio",
                "Visual Studio 2026/2022 con la carga de trabajo de desarrollo móvil .NET MAUI.",
                "Directorio: 04 CLIMOV/WSEurekaBankMobileClient | Proyecto: WSEurekaBankMobileClient.csproj | TargetFramework: net10.0-android.",
                "Compilar el proyecto móvil y verificar la resolución de paquetes System.ServiceModel.Http y Primitives 8.1.2.",
                "Proyecto compilando satisfactoriamente para la plataforma Android con intermediarios redirigidos en Directory.Build.props."
            ));

            list.Add(TableCaption(20, "Descriptor de Proyecto Cliente Móvil WSEurekaBankMobileClient.csproj"));
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
    <ApplicationTitle>EurekaBank SOAP .NET</ApplicationTitle>
    <ApplicationId>ec.edu.monster.climov.soap</ApplicationId>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Microsoft.Maui.Controls" Version="$(MauiVersion)" />
    <PackageReference Include="System.ServiceModel.Http" Version="8.1.2" />
    <PackageReference Include="System.ServiceModel.Primitives" Version="8.1.2" />
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(21, "Configuración de Arranque de la Aplicación Móvil MauiProgram.cs"));
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

            list.Add(TableCaption(22, "Modelo de Invocación SOAP en Dispositivo Móvil EurekaBankSoapModel.cs (MAUI)"));
            list.Add(CodeBox("""
using System.ServiceModel;
using Microsoft.Maui.Devices;

namespace ec.edu.monster.model
{
    public class EurekaBankSoapModel
    {
        public string EndpointUrl { get; set; } =
            DeviceInfo.Platform == DevicePlatform.Android
                ? "http://10.0.2.2:5120/EurekaBankWS.svc"
                : "http://localhost:5120/EurekaBankWS.svc";

        private T Ejecutar<T>(Func<IEurekaBankSoapContrato, T> accion)
        {
            var binding = new BasicHttpBinding(BasicHttpSecurityMode.None)
            {
                MaxReceivedMessageSize = 10 * 1024 * 1024,
                SendTimeout = TimeSpan.FromSeconds(30),
                ReceiveTimeout = TimeSpan.FromSeconds(30)
            };
            using var factory = new ChannelFactory<IEurekaBankSoapContrato>(binding, new EndpointAddress(EndpointUrl));
            var canal = factory.CreateChannel();
            try
            {
                var res = accion(canal);
                ((IClientChannel)canal).Close();
                return res;
            }
            catch
            {
                ((IClientChannel)canal).Abort();
                throw;
            }
        }

        public UsuarioModel Autenticar(string usr, string pwd) => Ejecutar(c => c.AutenticarUsuario(usr, pwd));
        public List<CuentaModel> ListarCuentas() => Ejecutar(c => c.ListarCuentasPorSucursal(0));
        public OperacionModel Depositar(string cta, double mon, int emp, int ven) => Ejecutar(c => c.Depositar(cta, mon, emp, ven));
    }
}
"""));

            list.Add(TableCaption(23, "Vista XAML de Inicio de Sesión Móvil LoginView.xaml"));
            list.Add(CodeBox("""
<ContentPage xmlns="http://schemas.microsoft.com/dotnet/2021/maui"
             xmlns:x="http://schemas.microsoft.com/winfx/2009/xaml"
             x:Class="ec.edu.monster.view.LoginView"
             Title="EurekaBank Móvil — SOAP .NET"
             BackgroundColor="#0F172A">
    <ScrollView>
        <VerticalStackLayout Padding="24" Spacing="14" VerticalOptions="Center">
            <Border BackgroundColor="White" Padding="24">
                <VerticalStackLayout Spacing="12">
                    <Image Source="sullivan.png" HeightRequest="84" WidthRequest="84" HorizontalOptions="Center" />
                    <Label Text="EUREKABANK — BANCA MÓVIL" FontSize="19" FontAttributes="Bold" HorizontalOptions="Center" />
                    <Entry x:Name="TxtUsuario" Text="Monster" />
                    <Entry x:Name="TxtClave" Text="Monster9" IsPassword="True" />
                    <Picker x:Name="PckVentanilla" SelectedIndex="0">
                        <Picker.Items>
                            <x:String>Ventanilla 1</x:String>
                            <x:String>Ventanilla 2</x:String>
                            <x:String>Ventanilla 3</x:String>
                            <x:String>Ventanilla 4</x:String>
                        </Picker.Items>
                    </Picker>
                    <Entry x:Name="TxtEndpoint" Text="http://10.0.2.2:5120/EurekaBankWS.svc" />
                    <Button Text="INGRESAR A BANCA MÓVIL" Clicked="OnIngresarClicked" BackgroundColor="#2563EB" TextColor="White" />
                </VerticalStackLayout>
            </Border>
        </VerticalStackLayout>
    </ScrollView>
</ContentPage>
"""));

            list.Add(FigureCaption(15, "Pantalla de Autenticación y Ventanilla en Emulador Android (.NET MAUI)"));
            list.Add(ScreenshotBox(
                15,
                "Pantalla de Autenticación y Ventanilla en Emulador Android (.NET MAUI)",
                "Emulador Android (Pixel 7 / Android 14 API 34).",
                "App: WSEurekaBankMobileClient | Host SOAP: http://10.0.2.2:5120/EurekaBankWS.svc | Operador: Monster | Ventanilla: 1.",
                "Iniciar la app en el emulador, verificar la resolución de red hacia el servidor anfitrión y presionar 'INGRESAR A BANCA MÓVIL'.",
                "Autenticación confirmada por el servidor SOAP y transición hacia el Dashboard móvil de cuentas bancarias."
            ));

            list.Add(FigureCaption(16, "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil"));
            list.Add(ScreenshotBox(
                16,
                "Monitoreo de Cuentas y Operaciones Transaccionales en Cliente Móvil",
                "Pantalla DashboardView en el emulador Android.",
                "Vista de cuentas bancarias en CollectionView con tarjetas cromáticas y panel de operaciones.",
                "Seleccionar la cuenta 001-0001, ingresar un monto de 50.00 USD y presionar 'DEPOSITAR (+)'.",
                "Tarjeta de cuenta actualizándose en tiempo real con insignia verde y confirmación transaccional exitosa."
            ));

            // 4.10 Desarrollo del Cliente Web ASP.NET Core MVC
            list.Add(Heading2("4.10 Desarrollo del Cliente Web (ASP.NET Core MVC)"));
            list.Add(Paragraph("El Cliente Web se construye con ASP.NET Core MVC en .NET 10 (puerto TCP 5121), implementando controladores dedicados (LoginController y DashboardController), vistas Razor organizadas en ec.edu.monster.view y sondeo reactivo en tiempo real mediante JavaScript (fetch periódico cada 0.9 segundos):"));

            list.Add(TableCaption(24, "Descriptor de Proyecto Cliente Web WSEurekaBankWebClient.csproj"));
            list.Add(CodeBox("""
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net10.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
    <RootNamespace>ec.edu.monster</RootNamespace>
    <AssemblyName>WSEurekaBankWebClient</AssemblyName>
    <NoWarn>$(NoWarn);NU1901;NU1902;NU1903;NU1904</NoWarn>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="System.ServiceModel.Http" Version="8.1.2" />
    <PackageReference Include="System.ServiceModel.Primitives" Version="8.1.2" />
  </ItemGroup>
</Project>
"""));

            list.Add(TableCaption(25, "Configuración del Host Web y Enrutamiento Razor Program.cs"));
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

            list.Add(TableCaption(26, "Controlador Web de Autenticación LoginController.cs"));
            list.Add(CodeBox("""
using Microsoft.AspNetCore.Mvc;
using ec.edu.monster.model;

namespace ec.edu.monster.controller
{
    public class LoginController : Controller
    {
        private readonly AppControlador _controlador = new();

        [HttpGet]
        public IActionResult Index()
        {
            if (AppControlador.LeerSesionCookie(Request) != null)
                return RedirectToAction("Index", "Dashboard");

            return View("~/ec.edu.monster.view/Login/Index.cshtml", new LoginWebModel());
        }

        [HttpPost]
        public IActionResult Index(LoginWebModel model)
        {
            var res = _controlador.IniciarSesion(model.Usuario, model.Clave);
            if (!res.Autenticado)
            {
                model.MensajeError = res.Mensaje;
                return View("~/ec.edu.monster.view/Login/Index.cshtml", model);
            }

            int vent = model.Ventanilla is >= 1 and <= 4 ? model.Ventanilla : 1;
            AppControlador.GuardarSesionCookie(Response, res, vent);
            return RedirectToAction("Index", "Dashboard");
        }

        [HttpGet]
        public IActionResult Logout()
        {
            AppControlador.CerrarSesionCookie(Response);
            return RedirectToAction("Index", "Login");
        }
    }
}
"""));

            list.Add(TableCaption(27, "Controlador Web del Dashboard Transaccional DashboardController.cs"));
            list.Add(CodeBox("""
using System.Globalization;
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

            var cuentas = string.IsNullOrWhiteSpace(filtro)
                ? _controlador.ObtenerCuentasPorSucursal(sucursalId)
                : _controlador.BuscarCuentasPorCliente(filtro);

            string ctaSel = !string.IsNullOrWhiteSpace(cuenta)
                ? cuenta
                : (cuentas.FirstOrDefault()?.NumeroCuenta ?? "001-0001");

            var vm = new DashboardWebModel
            {
                Sesion = sesion,
                VentanillaActiva = sesion.Ventanilla is >= 1 and <= 4 ? sesion.Ventanilla : 1,
                SucursalSeleccionada = sucursalId,
                Filtro = filtro,
                CuentaSeleccionada = ctaSel,
                Sucursales = _controlador.ObtenerSucursales(),
                Cuentas = cuentas,
                Extracto = _controlador.ObtenerExtracto(ctaSel)
            };

            return View("~/ec.edu.monster.view/Dashboard/Index.cshtml", vm);
        }

        [HttpGet]
        public IActionResult EstadoMashup(int sucursalId = 0, string filtro = "", string cuentaExtracto = "001-0001")
        {
            var sesion = AppControlador.LeerSesionCookie(Request);
            if (sesion == null) return Unauthorized();

            var cuentas = string.IsNullOrWhiteSpace(filtro)
                ? _controlador.ObtenerCuentasPorSucursal(sucursalId)
                : _controlador.BuscarCuentasPorCliente(filtro);

            var extracto = string.IsNullOrWhiteSpace(cuentaExtracto)
                ? new List<MovimientoModel>()
                : _controlador.ObtenerExtracto(cuentaExtracto);

            return Json(new
            {
                verdes = cuentas.Count(c => c.EstadoMashup != "ROJO"),
                rojas = cuentas.Count(c => c.EstadoMashup == "ROJO"),
                cuentas = cuentas.Select(c => new
                {
                    numeroCuenta = c.NumeroCuenta,
                    titular = c.TitularCompleto,
                    dni = c.DniCliente,
                    sucursal = c.NombreSucursal,
                    saldo = c.Saldo.ToString("F2", CultureInfo.InvariantCulture),
                    estadoMashup = c.EstadoMashup,
                    etiqueta = c.EstadoMashup == "ROJO"
                        ? $"ROJO (Vent. {c.VentanillaOcupada} - {c.BloqueadoPor})"
                        : "VERDE (Libre)"
                }),
                extracto = extracto.Take(15).Select(m => new
                {
                    fechaHora = m.FechaHora,
                    numeroOperacion = m.NumeroOperacion,
                    tipo = m.Tipo,
                    ventanilla = m.Ventanilla,
                    monto = m.Monto.ToString("F2", CultureInfo.InvariantCulture),
                    saldoResultante = m.SaldoResultante.ToString("F2", CultureInfo.InvariantCulture),
                    descripcion = m.Descripcion
                })
            });
        }
    }
}
"""));

            list.Add(TableCaption(28, "Plantilla Razor de Inicio de Sesión Login/Index.cshtml"));
            list.Add(CodeBox("""
@model ec.edu.monster.model.LoginWebModel
@{
    Layout = null;
}
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <title>Acceso &mdash; EurekaBank Web SOAP .NET (GR04)</title>
    <link rel="stylesheet" href="/css/site.css" />
</head>
<body style="background:#0f172a; display:flex; align-items:center; justify-content:center; min-height:100vh;">
    <div style="background:#fff; width:390px; border-radius:14px; padding:32px 30px; text-align:center;">
        <img src="/images/sullivan.png" width="84" height="84" alt="Monster" />
        <h2>EUREKABANK &mdash; BANCA WEB</h2>
        <div style="color:#64748b; font-size:12px; margin-bottom:20px;">Cliente ASP.NET Core MVC (SOAP .NET) | ec.edu.monster</div>

        @if (!string.IsNullOrEmpty(Model.MensajeError))
        {
            <div style="background:#fee2e2; color:#991b1b; padding:10px; border-radius:8px; margin-bottom:14px;">@Model.MensajeError</div>
        }

        <form method="post" action="/Login/Index">
            <div style="text-align:left; margin-bottom:14px;">
                <label style="font-size:12px; font-weight:700;">Usuario Operador:</label>
                <input type="text" name="Usuario" value="@Model.Usuario" required style="width:100%; padding:10px;" />
            </div>
            <div style="text-align:left; margin-bottom:14px;">
                <label style="font-size:12px; font-weight:700;">Contraseña:</label>
                <input type="password" name="Clave" value="@Model.Clave" required style="width:100%; padding:10px;" />
            </div>
            <div style="text-align:left; margin-bottom:14px;">
                <label style="font-size:12px; font-weight:700;">Ventanilla Asignada:</label>
                <select name="Ventanilla" style="width:100%; padding:10px;">
                    <option value="1">Ventanilla 1</option>
                    <option value="2">Ventanilla 2</option>
                    <option value="3">Ventanilla 3</option>
                    <option value="4">Ventanilla 4</option>
                </select>
            </div>
            <button type="submit" style="width:100%; padding:12px; background:#2563eb; color:#fff; border:none; border-radius:8px; font-weight:700;">INGRESAR AL SISTEMA</button>
        </form>
    </div>
</body>
</html>
"""));

            list.Add(TableCaption(29, "Plantilla Razor del Dashboard Reactivo con Polling Dashboard/Index.cshtml"));
            list.Add(CodeBox("""
@model ec.edu.monster.model.DashboardWebModel
@{
    Layout = null;
}
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="utf-8" />
    <title>Monitor MashUp &amp; Operaciones &mdash; EurekaBank Web SOAP .NET (GR04)</title>
    <style>
        body { margin:0; font-family:'Segoe UI',sans-serif; background:#f1f5f9; }
        .badge-verde { background:#dcfce7; color:#166534; padding:4px 11px; border-radius:999px; font-weight:700; }
        .badge-rojo { background:#dc2626; color:#ffffff; padding:4px 11px; border-radius:999px; font-weight:700; }
    </style>
</head>
<body>
    <div style="background:#0f172a; color:#fff; padding:14px 26px; display:flex; justify-content:space-between;">
        <div><b>EUREKABANK — SUPERVISIÓN MASHUP & BANCA WEB (SOAP .NET - GR04)</b></div>
        <div>Operador: @Model.Sesion.NombreCompleto | Ventanilla: @Model.VentanillaActiva</div>
    </div>
    <div style="padding:20px;">
        <h3>Semáforo de Concurrencia MashUp en Tiempo Real</h3>
        <table style="width:100%; border-collapse:collapse; background:#fff;">
            <thead>
                <tr><th>Cuenta</th><th>Titular</th><th>Saldo USD</th><th>Estado MashUp</th></tr>
            </thead>
            <tbody id="tablaCuentas">
                @foreach (var c in Model.Cuentas)
                {
                    <tr>
                        <td>@c.NumeroCuenta</td>
                        <td>@c.TitularCompleto</td>
                        <td>$@c.Saldo.ToString("F2")</td>
                        <td><span class="@(c.EstadoMashup == "ROJO" ? "badge-rojo" : "badge-verde")">@c.EtiquetaSemaforo</span></td>
                    </tr>
                }
            </tbody>
        </table>
    </div>
    <script>
        setInterval(async () => {
            const resp = await fetch('/Dashboard/EstadoMashup');
            if (resp.ok) {
                const data = await resp.json();
                actualizarTabla(data.cuentas);
            }
        }, 900);

        function actualizarTabla(cuentas) {
            const tbody = document.getElementById('tablaCuentas');
            tbody.innerHTML = '';
            cuentas.forEach(c => {
                const cls = c.estadoMashup === 'ROJO' ? 'badge-rojo' : 'badge-verde';
                tbody.innerHTML += `<tr><td>${c.numeroCuenta}</td><td>${c.titular}</td><td>$${c.saldo}</td><td><span class="${cls}">${c.etiqueta}</span></td></tr>`;
            });
        }
    </script>
</body>
</html>
"""));

            list.Add(FigureCaption(17, "Interfaz Web de Inicio de Sesión de Ventanilla (Login/Index.cshtml)"));
            list.Add(ScreenshotBox(
                17,
                "Interfaz Web de Inicio de Sesión de Ventanilla (Login/Index.cshtml)",
                "Navegador Google Chrome o Microsoft Edge en http://localhost:5121/.",
                "Ruta: 04. CLIWEB/WSEurekaBankWebClient | Puerto: 5121 | Usuario: Monster | Contraseña: Monster9 | Ventanilla: 1.",
                "Cargar la URL http://localhost:5121/ en el navegador e iniciar sesión con las credenciales maestras.",
                "Página de autenticación web con tarjeta blanca, logotipo institucional de Sullivan y redirección inmediata a /Dashboard."
            ));

            list.Add(FigureCaption(18, "Monitor MashUp y Banca Web en Tiempo Real (Dashboard/Index.cshtml)"));
            list.Add(ScreenshotBox(
                18,
                "Monitor MashUp y Banca Web en Tiempo Real (Dashboard/Index.cshtml)",
                "Navegador web en http://localhost:5121/Dashboard.",
                "Dashboard web interactivo con sondeo reactivo en tiempo real (polling cada 0.9 segundos).",
                "Observar la cuadrícula de cuentas bancarias con indicadores cromáticos VERDE y tabla de extracto sincronizada.",
                "Actualización reactiva sin recargar la página, reflejando saldos bancarios y estado del semáforo MashUp."
            ));

            list.Add(FigureCaption(19, "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web"));
            list.Add(ScreenshotBox(
                19,
                "Reflejo del Semáforo en ROJO durante Operación Transaccional Activa en Web",
                "Dashboard web durante la ejecución de un depósito en la cuenta 001-0001.",
                "Operación: Depósito de $100.00 USD | Ventanilla: 1 | Tiempo de retención: 4.5 segundos.",
                "Pulsar 'ACREDITAR DEPÓSITO (+)' y verificar la alternancia visual en la cuadrícula compartida.",
                "La fila cambia instantáneamente a badge rojo 'ROJO (Vent. 1 - Monster)' durante la transacción y retorna a VERDE tras el COMMIT."
            ));

            // 4.11 Matriz de Pruebas y Troubleshooting
            list.Add(Heading2("4.11 Matriz de Casos de Prueba Transaccionales y de Concurrencia"));
            list.Add(Paragraph("Para validar la robustez técnica del sistema EurekaBank en .NET 10 y el cumplimiento riguroso de las propiedades ACID y exclusión mutua, se ejecutó la siguiente batería de pruebas funcionales y de concurrencia:"));

            list.Add(TableCaption(30, "Matriz de Casos de Prueba Transaccionales y de Concurrencia MashUp"));
            list.Add(TechnicalTable(
                new[] { "ID", "Escenario de Prueba", "Procedimiento Técnico Ejecutado", "Resultado Esperado", "Estado" },
                new[] { 1000, 2200, 2400, 2404, 1000 },
                new List<string[]>
                {
                    new[] { "CP-01", "Autenticación BCrypt Operador Monster", "Ingreso de 'Monster' y clave 'Monster9' vía SOAP.", "Token generado y rol Superadmin asignado.", "EXITOSO" },
                    new[] { "CP-02", "Rechazo de Credenciales Inválidas", "Ingreso de clave errónea para operador cajero1.", "Mensaje de rechazo sin emitir token.", "EXITOSO" },
                    new[] { "CP-03", "Depósito Atómico con Bloqueo Pesimista", "Depósito de $150.00 sobre 001-0001 con delay de 4.5s.", "Saldo incrementa y se genera registro en movimiento.", "EXITOSO" },
                    new[] { "CP-04", "Exclusión Mutua Concurrente (ROJO)", "Ventanilla 2 intenta operar mientras Ventanilla 1 procesa.", "Rechazo de concurrencia con mensaje de cuenta en uso.", "EXITOSO" },
                    new[] { "CP-05", "Transferencia ACID entre Cuentas", "Transferencia de $100.00 de 001-0001 a 001-0002.", "Débito y crédito aplicados atómicamente.", "EXITOSO" },
                    new[] { "CP-06", "Rollback por Saldo Insuficiente", "Intento de retiro de $99,999.00 sobre cuenta activa.", "Transacción cancelada y saldo inalterado.", "EXITOSO" },
                    new[] { "CP-07", "Consumo Móvil en Loopback Android", "Conexión de emulador a http://10.0.2.2:5120/.", "Listado y depósito exitoso desde .NET MAUI.", "EXITOSO" },
                    new[] { "CP-08", "Sondeo Reactivo MashUp en Web", "Actualización AJAX cada 0.9s en Dashboard/Index.cshtml.", "Cambio cromático visible sin recargar la página.", "EXITOSO" }
                }
            ));

            list.Add(TableCaption(31, "Guía de Diagnóstico y Resolución de Errores Frecuentes (Troubleshooting .NET)"));
            list.Add(TechnicalTable(
                new[] { "Síntoma / Error", "Causa Técnica Principal", "Solución Recomendada y Comprobada" },
                new[] { 2500, 3000, 3504 },
                new List<string[]>
                {
                    new[] { "Error de conexión con la base de datos (Port 3306)", "El contenedor Podman/Docker de MySQL no está iniciado.", "Ejecutar 'podman compose up -d' dentro del directorio 03. BDD." },
                    new[] { "Timeout o conexión rechazada en emulador Android", "Se configuró 'localhost' en lugar de la IP de loopback.", "Utilizar '10.0.2.2:5120' en el archivo EurekaBankSoapModel.cs de MAUI." },
                    new[] { "Error 'No se puede agregar fila sin columnas' en WinForms", "La cuadrícula DataGridView se refrescó antes de inicializarse.", "Llamar a ConfigurarColumnasCuentas() si Columns.Count == 0." },
                    new[] { "Advertencia de tamaño de mensaje SOAP excedido", "La respuesta WCF supera el tamaño por defecto (64 KB).", "Configurar MaxReceivedMessageSize = 10 * 1024 * 1024 en BasicHttpBinding." }
                }
            ));

            // =========================================================================
            // SECCIÓN 5: CONCLUSIONES
            // =========================================================================
            list.Add(Heading1("5 CONCLUSIONES"));
            list.Add(BulletParagraph("1.", "La implementación de servicios web SOAP sobre la plataforma moderna .NET 10 mediante CoreWCF demuestra que es factible mantener la compatibilidad con estándares industriales rigurosos (WSDL, SOAP 1.1, BasicHttpBinding) sin renunciar al rendimiento de Kestrel y a la portabilidad multiplataforma sobre contenedores Linux y Windows."));
            list.Add(BulletParagraph("2.", "La aplicación estricta del patrón arquitectónico MVC Tradicional Puro (Model, View, Controller) en todos los proyectos clientes (Consola, Escritorio Windows Forms, Móvil .NET MAUI y Web ASP.NET Core) garantiza un desacoplamiento absoluto de la lógica de presentación respecto a la comunicación SOAP, facilitando el mantenimiento y la evolución independiente de cada interfaz de usuario."));
            list.Add(BulletParagraph("3.", "El mecanismo de Semáforo MashUp de Concurrencia en tiempo real, respaldado por la tabla control_mashup y bloqueos pesimistas SELECT ... FOR UPDATE a nivel del motor InnoDB de MySQL, resuelve satisfactoriamente las condiciones de carrera y previene la inconsistencia de saldos en operaciones bancarias simultáneas entre múltiples ventanillas."));
            list.Add(BulletParagraph("4.", "La integración de cuatro tecnologías cliente heterogéneas bajo el lenguaje unificado C# evidencia la versatilidad del ecosistema .NET contemporáneo para abordar escenarios de terminales ligeras de consola, aplicaciones de escritorio ricas, dispositivos móviles táctiles y aplicaciones web reactivas con sincronización continua."));

            // =========================================================================
            // SECCIÓN 6: RECOMENDACIONES
            // =========================================================================
            list.Add(Heading1("6 RECOMENDACIONES"));
            list.Add(BulletParagraph("1.", "En entornos de producción bancaria, se recomienda habilitar transporte seguro TLS/HTTPS obligatorio en Kestrel (BasicHttpSecurityMode.Transport) y utilizar certificados digitales institucionales para prevenir la interceptación de mensajes XML en tránsito."));
            list.Add(BulletParagraph("2.", "Para la sincronización reactiva en clientes web y de escritorio, se sugiere evaluar la adopción de WebSockets o SignalR Core en reemplazo del sondeo periódico (polling), reduciendo la sobrecarga de red en escenarios con cientos de ventanillas concurrentes."));
            list.Add(BulletParagraph("3.", "Se recomienda configurar políticas de reintento automático con retroceso exponencial (Polly en .NET) en la capa Modelo de los clientes, gestionando de forma transparente caídas temporales de conectividad de red o sobrecargas transitorias del servidor."));
            list.Add(BulletParagraph("4.", "Para la gestión de secretos y credenciales de base de datos en entornos empresariales, es aconsejable delegar el ConnectionString a variables de entorno protegidas o sistemas de gestión de claves como Azure Key Vault o HashiCorp Vault."));

            // =========================================================================
            // SECCIÓN 7: REFERENCIAS BIBLIOGRÁFICAS
            // =========================================================================
            list.Add(Heading1("7 REFERENCIAS BIBLIOGRÁFICAS"));
            list.Add(Paragraph("[1] Microsoft Corporation, «CoreWCF: WCF for .NET Core and .NET 5+», Microsoft Docs, 2026. [En línea]. Disponible en: https://github.com/CoreWCF/CoreWCF."));
            list.Add(Paragraph("[2] W3C, «Simple Object Access Protocol (SOAP) 1.1», World Wide Web Consortium Recommendation, 2000. [En línea]. Disponible en: https://www.w3.org/TR/2000/NOTE-SOAP-20000508/."));
            list.Add(Paragraph("[3] W3C, «Web Services Description Language (WSDL) 1.1», W3C Note, 2001. [En línea]. Disponible en: https://www.w3.org/TR/wsdl."));
            list.Add(Paragraph("[4] A. Troelsen y P. Japikse, Pro C# 10 with .NET 6: Foundational Principles and Practices in Programming, 11a ed. Nueva York, EE. UU.: Apress, 2022."));
            list.Add(Paragraph("[5] E. Gamma, R. Helm, R. Johnson, y J. Vlissides, Design Patterns: Elements of Reusable Object-Oriented Software. Boston, MA: Addison-Wesley, 1994."));
            list.Add(Paragraph("[6] Oracle Corporation, «MySQL 8.0 Reference Manual: InnoDB Locking and Transaction Model», Oracle Docs, 2026. [En línea]. Disponible en: https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html."));
            list.Add(Paragraph("[7] Microsoft Corporation, «.NET Multi-platform App UI (.NET MAUI) Documentation», Microsoft Learn, 2026. [En línea]. Disponible en: https://learn.microsoft.com/dotnet/maui/."));
            list.Add(Paragraph("[8] C. Anderson, Pro ASP.NET Core MVC, 8a ed. Berkeley, CA: Apress, 2020."));

            return list;
        }
    }
}
