@echo off
title Servidor Web RESTful .NET - Grupo 4 (NRC 36884)
echo ========================================================
echo    Iniciando Servidor Web RESTful (IIS Express)
echo    Puerto: 63281
echo    URL Base: http://localhost:63281/ServicioWebRest.svc
echo ========================================================
echo.
echo Presione Ctrl + C para detener el servidor en cualquier momento.
echo.

if exist "C:\Program Files\IIS Express\iisexpress.exe" (
    "C:\Program Files\IIS Express\iisexpress.exe" /path:"%~dp0." /port:63281
) else (
    if exist "C:\Program Files (x86)\IIS Express\iisexpress.exe" (
        "C:\Program Files (x86)\IIS Express\iisexpress.exe" /path:"%~dp0." /port:63281
    ) else (
        echo [ERROR] No se encontro IIS Express instalado en el equipo.
        echo Por favor instale IIS Express o ejecute el proyecto desde Visual Studio.
        pause
    )
)
