-- Script SQL - Taller Práctico: Consumo de APIs Públicas en Java con MySQL (Patrón MVC)
CREATE DATABASE IF NOT EXISTS api_paises
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE api_paises;

CREATE TABLE IF NOT EXISTS pais (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    capital VARCHAR(100),
    region VARCHAR(100),
    poblacion BIGINT,
    moneda VARCHAR(100)
);
