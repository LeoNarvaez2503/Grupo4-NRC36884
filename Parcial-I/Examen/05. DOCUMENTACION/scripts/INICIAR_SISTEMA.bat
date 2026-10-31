@echo off
chcp 65001 > nul
echo ======================================================================
echo    EXAMEN PRACTICO - GRUPO 04 (NRC: 36884)
echo    INICIANDO ARQUITECTURA DISTRIBUIDA JAVA RESTFUL
echo ======================================================================
echo.

echo [1/3] Verificando Base de Datos MySQL (Contenedor mysql-db)...
podman start mysql-db 2>nul || docker start mysql-db 2>nul
timeout /t 2 /nobreak > nul

echo [2/3] Levantando Servidor BanQuito Core RESTful (Puerto 8080)...
start "BanQuito RESTful API (Puerto 8080)" cmd /k "chcp 65001 > nul && cd /d "%~dp004. SERVIDOR\WS_BanQuito_Java_REST" && "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java"
timeout /t 5 /nobreak > nul

echo [3/3] Levantando Cliente Web Comercializadora (Puerto 8090)...
start "Comercializadora Web App (Puerto 8090)" cmd /k "chcp 65001 > nul && cd /d "%~dp004. CLIWEB\WS_Comercializadora_WebClient" && "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java"
timeout /t 4 /nobreak > nul

echo.
echo ======================================================================
echo    SERVICIOS BACKEND INICIADOS
echo    - BanQuito REST API:      http://localhost:8080/api/credito
echo    - Comercializadora Web:   http://localhost:8090/
echo ======================================================================
echo.

echo Abriendo aplicacion Web en el navegador...
start http://localhost:8090/

:MENU
echo.
echo ----------------------------------------------------------------------
echo  LANZADOR DE CLIENTES ADICIONALES:
echo    [1] Abrir Cliente Escritorio Swing (04. CLIESC)
echo    [2] Abrir Cliente Consola Interactivo (04. CLICON)
echo    [3] Ejecutar Test Automatizado de los 4 Clientes de Evaluacion
echo    [4] Volver a abrir Cliente Web en el Navegador
echo    [0] Salir de este lanzador
echo ----------------------------------------------------------------------
set /p opcion="Seleccione una opcion (0-4): "

if "%opcion%"=="1" (
    start "Comercializadora Desktop Client" cmd /k "chcp 65001 > nul && cd /d "%~dp004. CLIESC\WS_Comercializadora_DesktopClient" && "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java"
    goto MENU
)
if "%opcion%"=="2" (
    start "Comercializadora Console Client" cmd /k "chcp 65001 > nul && cd /d "%~dp004. CLICON\WS_Comercializadora_ConsoleClient" && "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java"
    goto MENU
)
if "%opcion%"=="3" (
    cd /d "%~dp004. CLICON\WS_Comercializadora_ConsoleClient"
    "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java "-Dexec.args=--demo"
    goto MENU
)
if "%opcion%"=="4" (
    start http://localhost:8090/
    goto MENU
)
if "%opcion%"=="0" exit
goto MENU
