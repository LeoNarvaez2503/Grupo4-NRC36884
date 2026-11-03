-- =============================================================================
-- SISTEMA BANCARIO TRANSACCIONAL EUREKABANK - GRUPO 04 (GR04)
-- DOMINIO: ec.edu.monster | MOTOR: Microsoft SQL Server 2022 (Docker / Podman Local :1433)
-- CREDENCIAL MAESTRA DE APLICACION: Usuario = Monster | Contraseña = Monster9
-- CREDENCIAL ADMINISTRADOR BDD: sa | Contraseña = Monster9#Admin
-- =============================================================================

IF DB_ID('eurekabank_db') IS NOT NULL
BEGIN
    ALTER DATABASE eurekabank_db SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE eurekabank_db;
END;
GO

CREATE DATABASE eurekabank_db;
GO

USE eurekabank_db;
GO

-- 1. Tabla: sucursal
CREATE TABLE sucursal (
    id_sucursal INT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL
);
GO

-- 2. Tabla: empleado (Autenticación y Auditoría de 4 Ventanillas + Gerencia + Superadmin Monster)
CREATE TABLE empleado (
    id_empleado INT IDENTITY(1,1) PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    rol VARCHAR(50) NOT NULL CHECK (rol IN ('Superadmin Monster', 'Gerente', 'Cajero')),
    id_sucursal INT NOT NULL DEFAULT 1,
    ventanilla INT NULL,
    CONSTRAINT FK_empleado_sucursal FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);
GO

-- 3. Tabla: cliente
CREATE TABLE cliente (
    id_cliente INT IDENTITY(1,1) PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NULL,
    telefono VARCHAR(20) NULL,
    email VARCHAR(100) NULL
);
GO

-- 4. Tabla: cuenta (Soporta bloqueo pesimista con WITH (UPDLOCK, ROWLOCK))
CREATE TABLE cuenta (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    moneda VARCHAR(10) NOT NULL DEFAULT 'USD',
    saldo DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    id_cliente INT NOT NULL,
    id_sucursal INT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Activa' CHECK (estado IN ('Activa', 'Bloqueada')),
    fecha_apertura DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_cuenta_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT FK_cuenta_sucursal FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);
GO

-- 5. Tabla: control_mashup (Semáforo Cromático en Tiempo Real: VERDE = Libre, ROJO = Ocupada en Ventanilla)
CREATE TABLE control_mashup (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    estado_mashup VARCHAR(10) NOT NULL DEFAULT 'VERDE' CHECK (estado_mashup IN ('VERDE', 'ROJO')),
    ventanilla INT NULL,
    bloqueado_por VARCHAR(100) NULL,
    actualizado_en DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_mashup_cuenta FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta) ON DELETE CASCADE
);
GO

-- 6. Tabla: movimiento (Auditoría e Historial Cronológico ACID)
CREATE TABLE movimiento (
    id_movimiento INT IDENTITY(1,1) PRIMARY KEY,
    numero_operacion VARCHAR(50) NOT NULL UNIQUE,
    numero_cuenta VARCHAR(20) NOT NULL,
    id_empleado INT NOT NULL,
    ventanilla INT DEFAULT 1,
    tipo VARCHAR(40) NOT NULL,
    monto DECIMAL(12, 2) NOT NULL,
    saldo_resultante DECIMAL(12, 2) NOT NULL,
    cuenta_referencia VARCHAR(20) NULL,
    descripcion VARCHAR(200) NULL,
    fecha_hora DATETIME2 NOT NULL DEFAULT GETDATE(),
    CONSTRAINT FK_movimiento_cuenta FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    CONSTRAINT FK_movimiento_empleado FOREIGN KEY (id_empleado) REFERENCES empleado(id_empleado)
);
GO

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
('APE-100001', '001-0001', 1, 1, 'Deposito', 2000.00, 2000.00, NULL, 'Apertura de cuenta ahorro Monster', DATEADD(DAY, -3, GETDATE())),
('DEP-100002', '001-0001', 2, 1, 'Deposito',  500.00, 2500.00, NULL, 'Depósito en efectivo Ventanilla 1', DATEADD(DAY, -1, GETDATE())),
('APE-100003', '001-0002', 1, 1, 'Deposito', 2000.00, 2000.00, NULL, 'Apertura de cuenta ahorro',         DATEADD(DAY, -2, GETDATE())),
('RET-100004', '001-0002', 3, 2, 'Retiro',    200.00, 1800.00, NULL, 'Retiro por ventanilla 2',           DATEADD(HOUR, -12, GETDATE())),
('APE-100005', '001-0003', 1, 1, 'Deposito', 3200.50, 3200.50, NULL, 'Apertura de cuenta ahorro',         DATEADD(DAY, -2, GETDATE())),
('APE-100006', '001-0004', 4, 3, 'Deposito',  950.00,  950.00, NULL, 'Apertura de cuenta ahorro',         DATEADD(DAY, -1, GETDATE())),
('APE-100007', '002-0001', 5, 4, 'Deposito', 4100.00, 4100.00, NULL, 'Apertura Sucursal Norte',           DATEADD(DAY, -1, GETDATE())),
('APE-100008', '003-0001', 1, 1, 'Deposito', 1500.00, 1500.00, NULL, 'Apertura Sucursal Guayaquil',       DATEADD(DAY, -1, GETDATE()));
GO
