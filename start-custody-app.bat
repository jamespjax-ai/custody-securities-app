@echo off
title Custody Securities App
echo ==========================================
echo   Custody Securities Application
echo   Starting on http://localhost:8080
echo ==========================================
echo.

set JAVA_HOME=C:\tools\java\jdk-17.0.18+8
set PATH=%JAVA_HOME%\bin;%PATH%

cd /d "%~dp0"
java -jar target\custody-app-0.0.1-SNAPSHOT.jar

pause
