@echo off
chcp 65001 > nul
echo ======================================================================
echo    COMERCIALIZADORA DE CELULARES - CLIENTE CONSOLA (GRUPO 04)
echo ======================================================================
cd /d "%~dp004. CLICON\WS_Comercializadora_ConsoleClient"
"C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java
pause
