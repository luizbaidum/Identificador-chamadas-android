@echo off
REM Script para configurar permissões do Identificador de Chamadas no Android
REM Execute este script com o dispositivo conectado via USB

echo ====================================
echo Configurador de Permissões
echo Identificador de Chamadas - Android
echo ====================================
echo.

set PACKAGE=com.example.identificadorchamadas

echo [1/7] Verificando conexão ADB...
adb devices
if %errorlevel% neq 0 (
    echo ERRO: ADB não encontrado ou dispositivo não conectado
    pause
    exit /b 1
)
echo OK!
echo.

echo [2/7] Concedendo permissão SYSTEM_ALERT_WINDOW (Overlay)...
adb shell appops set %PACKAGE% SYSTEM_ALERT_WINDOW allow
echo OK!
echo.

echo [3/7] Concedendo permissão READ_PHONE_STATE...
adb shell pm grant %PACKAGE% android.permission.READ_PHONE_STATE
echo OK!
echo.

echo [4/7] Concedendo permissão READ_CALL_LOG...
adb shell pm grant %PACKAGE% android.permission.READ_CALL_LOG
echo OK!
echo.

echo [5/7] Concedendo permissão ANSWER_PHONE_CALLS...
adb shell pm grant %PACKAGE% android.permission.ANSWER_PHONE_CALLS
echo OK!
echo.

echo [6/7] Concedendo permissão RECORD_AUDIO...
adb shell pm grant %PACKAGE% android.permission.RECORD_AUDIO
echo OK!
echo.

echo [7/7] Concedendo permissão CALL_PHONE...
adb shell pm grant %PACKAGE% android.permission.CALL_PHONE
echo OK!
echo.

echo ====================================
echo Verificando permissões concedidas...
echo ====================================
echo.

echo Verificando SYSTEM_ALERT_WINDOW:
adb shell appops get %PACKAGE% SYSTEM_ALERT_WINDOW
echo.

echo ====================================
echo Abrindo configurações do app...
echo ====================================
adb shell am start -a android.settings.ACTION_MANAGE_OVERLAY_PERMISSION -d package:%PACKAGE%
echo.

echo ====================================
echo CONCLUÍDO!
echo ====================================
echo.
echo Se a permissão de Overlay ainda não estiver ativa,
echo ative manualmente nas configurações que acabaram de abrir.
echo.
echo Caminho: Permitir exibição sobre outros apps
echo.
pause
