@echo off
REM Script de diagnóstico para problemas com permissões
REM Identificador de Chamadas - Android

echo ====================================
echo Diagnóstico de Permissões
echo Identificador de Chamadas - Android
echo ====================================
echo.

set PACKAGE=com.example.identificadorchamadas

echo [1] Informações do Dispositivo:
echo ====================================
echo.

echo Modelo:
adb shell getprop ro.product.model
echo.

echo Versão do Android:
adb shell getprop ro.build.version.release
echo.

echo Nível da API:
adb shell getprop ro.build.version.sdk
echo.

echo Fabricante:
adb shell getprop ro.product.manufacturer
echo.

echo ====================================
echo [2] Status do App:
echo ====================================
echo.

echo Verificando se o app está instalado...
adb shell pm list packages | findstr %PACKAGE%
if %errorlevel% neq 0 (
    echo APP NÃO INSTALADO!
    goto :fim
)
echo App está instalado: OK
echo.

echo ====================================
echo [3] Permissões Concedidas:
echo ====================================
echo.

adb shell dumpsys package %PACKAGE% | findstr "permission"
echo.

echo ====================================
echo [4] Status SYSTEM_ALERT_WINDOW:
echo ====================================
echo.

adb shell appops get %PACKAGE% SYSTEM_ALERT_WINDOW
echo.

echo ====================================
echo [5] Logs Recentes do App:
echo ====================================
echo.

echo (Últimas 50 linhas - pressione Ctrl+C para parar)
timeout /t 2 /nobreak >nul
adb logcat -d | findstr %PACKAGE% | more
echo.

:fim
echo ====================================
echo DIAGNÓSTICO CONCLUÍDO
echo ====================================
echo.
echo Salve estas informações se precisar de suporte!
echo.
pause
