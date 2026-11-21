-- Script SQL Server - Taller 2: Consumo de APIs Públicas en .NET con SQL Server (Patrón MVC)
IF DB_ID(N'ApiPaisesDB') IS NULL
BEGIN
    CREATE DATABASE ApiPaisesDB;
END
GO

USE ApiPaisesDB;
GO

IF OBJECT_ID(N'dbo.Paises', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Paises (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        Nombre NVARCHAR(100) NOT NULL,
        Capital NVARCHAR(100) NULL,
        Region NVARCHAR(100) NULL,
        SubRegion NVARCHAR(100) NULL,
        Poblacion BIGINT NOT NULL DEFAULT 0,
        Area FLOAT NOT NULL DEFAULT 0,
        Moneda NVARCHAR(100) NULL
    );
END
GO

IF OBJECT_ID(N'dbo.Universidades', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Universidades (
        Id INT IDENTITY(1,1) PRIMARY KEY,
        Nombre NVARCHAR(200) NOT NULL,
        Dominio NVARCHAR(150) NULL,
        SitioWeb NVARCHAR(300) NULL,
        Pais NVARCHAR(100) NULL
    );
END
GO
