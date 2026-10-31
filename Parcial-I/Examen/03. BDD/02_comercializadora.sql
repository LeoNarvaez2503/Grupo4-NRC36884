-- =======================================================================
-- SISTEMA DE LA COMERCIALIZADORA DE TELÉFONOS CELULARES
-- Base de Datos: comercializadora_db
-- NRC: 36884 - Grupo 04
-- =======================================================================

CREATE DATABASE IF NOT EXISTS comercializadora_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE comercializadora_db;

DROP TABLE IF EXISTS FACTURA;
DROP TABLE IF EXISTS TELEFONO_CELULAR;

-- 1. Catálogo de Teléfonos Celulares (Sin inventario de stock físico, solo entidad catálogo)
CREATE TABLE TELEFONO_CELULAR (
    COD_TELEFONO INT NOT NULL AUTO_INCREMENT,
    MARCA VARCHAR(50) NOT NULL,
    MODELO VARCHAR(100) NOT NULL,
    PRECIO NUMERIC(10,2) NOT NULL,
    CARACTERISTICAS TEXT NOT NULL,
    IMAGEN_URL VARCHAR(255) NULL,
    ESTADO VARCHAR(20) NOT NULL DEFAULT 'DISPONIBLE', -- 'DISPONIBLE', 'INACTIVO'
    PRIMARY KEY (COD_TELEFONO)
) ENGINE=InnoDB;

-- 2. Facturación de Ventas (Efectivo con 42% desc. o Crédito Directo BanQuito)
CREATE TABLE FACTURA (
    NUM_FACTURA INT NOT NULL AUTO_INCREMENT,
    NUMERO_COMPROBANTE VARCHAR(50) NOT NULL UNIQUE,
    FECHA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    COD_CLIENTE_BANCO INT NULL,
    CEDULA_CLIENTE VARCHAR(20) NOT NULL,
    NOMBRE_CLIENTE VARCHAR(100) NOT NULL,
    COD_TELEFONO INT NOT NULL,
    MODELO_TELEFONO VARCHAR(100) NOT NULL,
    PRECIO_UNITARIO NUMERIC(10,2) NOT NULL,
    FORMA_PAGO VARCHAR(20) NOT NULL, -- 'EFECTIVO' o 'CREDITO_DIRECTO'
    PORCENTAJE_DESCUENTO NUMERIC(5,2) NOT NULL DEFAULT 0.00,
    VALOR_DESCUENTO NUMERIC(10,2) NOT NULL DEFAULT 0.00,
    TOTAL_PAGAR NUMERIC(10,2) NOT NULL,
    COD_CREDITO_BANCO INT NULL,
    NUM_CUOTAS INT NULL,
    VALOR_CUOTA NUMERIC(10,2) NULL,
    OBSERVACION VARCHAR(255) NULL,
    PRIMARY KEY (NUM_FACTURA),
    CONSTRAINT FK_FACTURA_TELEFONO FOREIGN KEY (COD_TELEFONO) 
        REFERENCES TELEFONO_CELULAR (COD_TELEFONO) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =======================================================================
-- POBLAMIENTO INICIAL DEL CATÁLOGO DE TELÉFONOS CELULARES
-- =======================================================================
INSERT INTO TELEFONO_CELULAR (COD_TELEFONO, MARCA, MODELO, PRECIO, CARACTERISTICAS, IMAGEN_URL, ESTADO) VALUES
(1, 'Motorola', 'Edge 40 Neo 5G', 450.00, 'Pantalla pOLED 6.55" 144Hz, 256GB Almacenamiento, 12GB RAM, Cámara 50MP OIS, Carga rápida 68W, Certificación IP68', 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=300', 'DISPONIBLE'),
(2, 'Samsung', 'Galaxy S24 5G', 850.00, 'Pantalla Dynamic AMOLED 2X 6.2" 120Hz, 256GB, 8GB RAM, Procesador Exynos 2400 / Snapdragon 8 Gen 3, Galaxy AI', 'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=300', 'DISPONIBLE'),
(3, 'Apple', 'iPhone 15 128GB', 950.00, 'Pantalla Super Retina XDR 6.1", Chip A16 Bionic, Dynamic Island, Cámara principal 48MP, Puerto USB-C', 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=300', 'DISPONIBLE'),
(4, 'Xiaomi', 'Redmi Note 13 Pro', 280.00, 'Pantalla AMOLED 6.67" 120Hz, 256GB, 8GB RAM, Cámara 200MP con OIS, Batería 5000mAh Turbo Charging 67W', 'https://images.unsplash.com/photo-1511707171634-5f897ff02560?w=300', 'DISPONIBLE'),
(5, 'Honor', 'Magic 6 Lite 5G', 340.00, 'Pantalla curva AMOLED 6.78" 1.5K, 256GB, 8GB+8GB RAM Turbo, Pantalla ultrarresistente Honor Ultra-Bounce, Batería 5800mAh', 'https://images.unsplash.com/photo-1565849904461-04a58ad377e0?w=300', 'DISPONIBLE');
