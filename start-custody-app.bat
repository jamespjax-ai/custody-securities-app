@echo off
title Custody Securities App
echo ==========================================
echo   Custody Securities Application
echo   Starting on http://localhost:8080
echo ==========================================
echo.

:: Use local JDK 25 if it exists, otherwise use JAVA_HOME or PATH
if exist "%~dp0..\jdk-25.0.2\bin\java.exe" (
    set "JAVA_HOME=%~dp0..\jdk-25.0.2"
    set "PATH=%~dp0..\jdk-25.0.2\bin;%PATH%"
) else if not "%JAVA_HOME%"=="" (
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

cd /d "%~dp0"
java -jar target\custody-app-0.0.1-SNAPSHOT.jar

pause
