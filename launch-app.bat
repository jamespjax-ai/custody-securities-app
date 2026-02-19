@echo off
setlocal EnableDelayedExpansion
title Custody Securities One-Click Launcher

:: Log all output to a file for diagnostics
set "LOG_FILE=%~dp0launcher_debug.log"
echo [%DATE% %TIME%] Starting Launcher > "%LOG_FILE%"

echo ==========================================
echo   Custody Securities Starter
echo ==========================================
echo.

:: Set environment - Try to use system JAVA_HOME if not defined
if "%JAVA_HOME%"=="" (
    set "JAVA_EXE=java"
) else (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)
set "APP_JAR=target\custody-app-0.0.1-SNAPSHOT.jar"

cd /d "%~dp0"
echo [%DATE% %TIME%] Working Directory: %CD% >> "%LOG_FILE%"

:: Check if JAVA_HOME is valid
if not exist "%JAVA_EXE%" (
    echo [ERROR] Java executable not found at: %JAVA_EXE%
    echo [%DATE% %TIME%] ERROR: Java not found >> "%LOG_FILE%"
    pause
    exit /b 1
)

:: Check if jar exists
if not exist "%APP_JAR%" (
    echo [ERROR] Application JAR not found: %APP_JAR%
    echo [%DATE% %TIME%] ERROR: JAR not found >> "%LOG_FILE%"
    pause
    exit /b 1
)

echo [1/3] Starting Backend Server...
echo [%DATE% %TIME%] Launching Backend Server >> "%LOG_FILE%"
start "Custody Securities Backend" "%JAVA_EXE%" -jar "%APP_JAR%"

echo [2/3] Waiting for port 8080 to be ready...
echo [%DATE% %TIME%] Waiting for port 8080 >> "%LOG_FILE%"

set /a "retry=0"
:check_port
set /a "retry+=1"
if !retry! GTR 30 (
    echo.
    echo [ERROR] Server failed to start within 60 seconds.
    echo [%DATE% %TIME%] ERROR: Timeout waiting for port 8080 >> "%LOG_FILE%"
    pause
    exit /b 1
)

netstat -ano | findstr :8080 | findstr LISTENING >nul
if errorlevel 1 (
    <nul set /p "=."
    timeout /t 2 /nobreak >nul
    goto check_port
)

echo.
echo [%DATE% %TIME%] Port 8080 is live >> "%LOG_FILE%"
echo [3/3] Launching User Interface in your browser...
echo [%DATE% %TIME%] Opening browser to http://localhost:8080 >> "%LOG_FILE%"
start http://localhost:8080

echo.
echo ==========================================
echo   Application started successfully!
echo   The backend is running in the other window.
echo ==========================================
echo [%DATE% %TIME%] Launcher finished successfully >> "%LOG_FILE%"
timeout /t 5
exit
