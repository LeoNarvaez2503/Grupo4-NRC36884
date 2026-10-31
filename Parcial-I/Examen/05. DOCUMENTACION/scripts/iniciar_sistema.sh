#!/bin/bash
echo "======================================================================"
echo "   EXAMEN PRACTICO - GRUPO 04 (NRC: 36884)"
echo "   INICIANDO ARQUITECTURA DISTRIBUIDA JAVA RESTFUL"
echo "======================================================================"
echo ""

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo "[1/3] Verificando Base de Datos MySQL (Contenedor mysql-db)..."
docker start mysql-db 2>/dev/null || podman start mysql-db 2>/dev/null
sleep 2

echo "[2/3] El servidor Payara / REST API está activo en:"
echo "      http://localhost:8080/WS_BanQuito_Java_REST/"
echo "      http://localhost:8080/WS_BanQuito_Java_REST/api/credito"
sleep 1

echo "[3/3] Levantando Cliente Web Comercializadora (Puerto 8090)..."
cd "$ROOT_DIR/04. CLIWEB/WS_Comercializadora_WebClient"
mvn exec:java
