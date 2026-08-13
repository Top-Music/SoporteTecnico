@echo off
setlocal

echo Starting microservices and frontend...

cd /d "%~dp0"

if not exist "ms-users-main\mvnw.cmd" (
  echo [ERROR] ms-users-main\mvnw.cmd not found.
  exit /b 1
)

if not exist "ms-solicitudes\mvnw.cmd" (
  echo [ERROR] ms-solicitudes\mvnw.cmd not found.
  exit /b 1
)

if not exist "frontend\index.html" (
  echo [ERROR] frontend\index.html not found.
  exit /b 1
)

start "ms-users-main" cmd /k "cd /d \"%~dp0ms-users-main\" && call mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak >nul

start "ms-solicitudes" cmd /k "cd /d \"%~dp0ms-solicitudes\" && call mvnw.cmd spring-boot:run"
timeout /t 8 /nobreak >nul

where python >nul 2>nul
if %errorlevel%==0 (
  start "frontend" cmd /k "cd /d \"%~dp0frontend\" && python -m http.server 5500"
) else (
  echo [WARN] Python was not found. Open frontend\index.html manually.
)

start "" http://localhost:5500
echo.
echo Backend users:  http://localhost:8083
echo Backend requests: http://localhost:8084
echo Frontend:      http://localhost:5500
echo.
echo Press any key to close this launcher window.
pause >nul
