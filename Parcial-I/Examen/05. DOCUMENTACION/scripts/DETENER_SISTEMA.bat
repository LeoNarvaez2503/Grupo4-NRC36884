@echo off
chcp 65001 > nul
echo ======================================================================
echo    DETENIENDO SERVICIOS DEL EXAMEN - GRUPO 04
echo ======================================================================
echo.

echo Buscando procesos en puerto 8080 y 8090...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8080 "') do (
    taskkill /F /PID %%a 2>nul
)
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":8090 "') do (
    taskkill /F /PID %%a 2>nul
)

echo.
echo Servicios detenidos correctamente.
pause
