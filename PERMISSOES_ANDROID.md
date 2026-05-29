# Guia de Permissões - Android 14+ (Android 16 Preview)

## Problema: Não consegue ativar "Exibir sobre outros apps"

### Causas Comuns

#### 1. **App Instalado via ADB/Fontes Desconhecidas**
No Android 14+, apps instalados via ADB ou APK podem ter restrições extras:

**Solução:**
- Vá em **Configurações** → **Apps** → **Acesso especial de apps**
- Procure por **"Exibir sobre outros apps"** ou **"Display over other apps"**
- Encontre o app na lista e ative a permissão

#### 2. **Caminho Alternativo nas Configurações**
Diferentes fabricantes podem ter caminhos diferentes:

**Samsung:**
```
Configurações → Apps → Menu (⋮) → Acesso especial → Exibir sobre outros apps
```

**Xiaomi/MIUI:**
```
Configurações → Apps → Gerenciar apps → [Nome do App] → Outras permissões → Exibir sobre outros apps
```

**Android Puro/Google Pixel:**
```
Configurações → Apps → Acesso especial de apps → Exibir sobre outros apps
```

**Motorola/OnePlus:**
```
Configurações → Apps e notificações → Acesso especial → Exibir sobre outros apps
```

#### 3. **Permissões de Desenvolvedor**
Se você instalou o app em modo debug:

1. Vá em **Configurações** → **Sistema** → **Opções do desenvolvedor**
2. Procure por **"Desenhar sobre outros apps"** ou opções similares
3. Certifique-se de que não há restrições para apps em desenvolvimento

#### 4. **Reinstalar o App**
Às vezes, reinstalar o app resolve:

```bash
# Desinstalar
adb uninstall com.example.identificadorchamadas

# Reinstalar
adb install -r app-debug.apk

# Abrir as configurações do app após instalar
adb shell am start -a android.settings.action.MANAGE_OVERLAY_PERMISSION -d package:com.example.identificadorchamadas
```

#### 5. **Conceder Permissão via ADB (Modo Debug)**
Para testes, você pode forçar a permissão via ADB:

```bash
# Conceder permissão de overlay via ADB
adb shell appops set com.example.identificadorchamadas SYSTEM_ALERT_WINDOW allow

# Verificar se foi concedida
adb shell appops get com.example.identificadorchamadas SYSTEM_ALERT_WINDOW
```

⚠️ **Nota:** Isso só funciona para testes. Em produção, o usuário deve conceder manualmente.

### Como Testar se a Permissão Funciona

Depois de conceder a permissão:

1. Abra o app **Identificador de Chamadas**
2. Verifique o status das permissões na tela principal
3. Ative o switch do serviço
4. Faça uma chamada de teste para ver o overlay aparecer

### Logs para Diagnóstico

Para ver se há erros relacionados a permissões:

```bash
# Ver logs do app
adb logcat | grep identificadorchamadas

# Ver logs de permissões negadas
adb logcat | grep "SYSTEM_ALERT_WINDOW"

# Ver logs de WindowManager
adb logcat | grep "WindowManager"
```

### Verificar Permissão Programaticamente

O app já possui verificação automática. Na tela principal, você verá:
- ✓ Overlay: **Permitido** (verde) ou **NEGADO** (vermelho)
- Status das outras permissões

### Android 16 Preview - Considerações Especiais

Se você está usando Android 16 Preview/Beta:

1. **API Level pode ter mudado**: Verifique se o `targetSdk` está correto
2. **Novas restrições**: Versões preview podem ter políticas mais restritivas
3. **Bugs do sistema**: Versões preview podem ter bugs nas configurações
4. **Modo Developer**: Certifique-se de que "USB Debugging" está ativo

### Troubleshooting Final

Se nada funcionar:

1. **Reinicie o dispositivo** após instalar o app
2. **Limpe o cache** das Configurações do Android:
   - Configurações → Apps → Configurações → Armazenamento → Limpar cache
3. **Teste em outro dispositivo** para isolar o problema
4. **Verifique o fabricante**: Alguns fabricantes (Xiaomi, Huawei) têm restrições extras

### Contato e Suporte

Se o problema persistir:
- Verifique a versão do Android: `adb shell getprop ro.build.version.release`
- Verifique o nível da API: `adb shell getprop ro.build.version.sdk`
- Anote o modelo do dispositivo: `adb shell getprop ro.product.model`
