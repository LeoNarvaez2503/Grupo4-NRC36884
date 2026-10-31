@echo off
chcp 65001 > nul
echo ======================================================================
echo    COMERCIALIZADORA DE CELULARES - CLIENTE ESCRITORIO SWING (GRUPO 04)
echo ======================================================================
cd /d "%~dp004. CLIESC\WS_Comercializadora_DesktopClient"
"C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" exec:java
