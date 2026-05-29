# Guia Rápido de Instalação - Identificador de Chamadas

## 🚀 Instalação Rápida

### 1. Gerar o APK

```bash
# No diretório do projeto
gradlew.bat assembleDebug
```

O APK será gerado em: `app/build/outputs/apk/debug/app-debug.apk`

### 2. Instalar no Dispositivo

#### Via USB:
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Via Transferência:
1. Copie o APK para o celular
2. Abra o gerenciador de arquivos
3. Toque no APK
4. Permita instalação de fontes desconhecidas
5. Instale o app

### 3. Configurar Permissões (IMPORTANTE!)

#### Automático (Recomendado):
```bash
# Execute com o celular conectado via USB
configurar_permissoes.bat
```

#### Manual:
1. Abra o app **Identificador de Chamadas**
2. Clique em **"Conceder Permissões"**
3. Aceite todas as permissões solicitadas
4. Clique em **"Configurar Overlay"**
5. Ative **"Permitir exibir sobre outros apps"**

### 4. Ativar o Serviço

1. Na tela principal do app
2. Ative o switch **"Ativar Serviço"**
3. Verifique que o status mostra: **"Serviço ativo"**

### 5. Testar

1. Ligue para o seu número de outro telefone
2. O overlay deve aparecer automaticamente
3. Teste as opções:
   - ✅ **Atender com Robô**
   - ✅ **Atender Normal**
   - ✅ **Recusar**

## ⚠️ Problemas Comuns

### Overlay não aparece?

**Verifique na tela principal:**
- ✓ Overlay: **Permitido** ✅ ou **NEGADO** ❌

**Se estiver NEGADO:**
1. Clique no botão **"Configurar Overlay"** (deve estar vermelho)
2. Nas configurações que abrirem, ative a permissão
3. Volte ao app e verifique se mudou para "Permitido"

### App fecha sozinho?

**Desative otimização de bateria:**

**Xiaomi/MIUI:**
1. Segurança → Gerenciar apps → Identificador de Chamadas
2. Economia de energia: **Sem restrições**
3. Inicialização automática: **Ativado**

**Samsung:**
1. Configurações → Bateria → Uso em segundo plano
2. Adicionar exceção: Identificador de Chamadas

**Android Puro:**
1. Configurações → Bateria → Uso de bateria do app
2. Identificador de Chamadas → **Não restrito**

### Permissões aparecem como negadas?

Execute o diagnóstico:
```bash
diagnostico_permissoes.bat
```

Isso mostrará:
- Versão do Android
- Status de todas as permissões
- Logs de erros (se houver)

### Android 14/15/16 - Permissão bloqueada?

**Via ADB (mais confiável):**
```bash
# Forçar permissão de overlay
adb shell appops set com.example.identificadorchamadas SYSTEM_ALERT_WINDOW allow

# Verificar se funcionou
adb shell appops get com.example.identificadorchamadas SYSTEM_ALERT_WINDOW
```

**Manualmente nas Configurações:**
- Android → Configurações → Apps
- Acesso especial de apps
- Exibir sobre outros apps
- Encontre "Identificador de Chamadas"
- Ative a permissão

## 📋 Checklist de Configuração

Antes de testar, verifique:

- [ ] App instalado com sucesso
- [ ] USB Debugging ativado (se usando ADB)
- [ ] Todas as permissões concedidas
- [ ] Overlay ativado (verificar status no app)
- [ ] Serviço ativado (switch ligado)
- [ ] Status mostra: "Serviço ativo"
- [ ] Otimização de bateria desativada para o app

## 🔄 Reinstalação Limpa

Se algo deu errado, reinstale do zero:

```bash
# 1. Desinstalar
adb uninstall com.example.identificadorchamadas

# 2. Limpar cache do Android
adb shell pm clear com.android.settings

# 3. Reinstalar
adb install app/build/outputs/apk/debug/app-debug.apk

# 4. Configurar permissões
configurar_permissoes.bat
```

## 📞 Teste Final

Faça uma chamada de teste:

1. Use outro celular para ligar
2. O overlay deve aparecer em ~2 segundos
3. Se aparecer: **✅ Tudo funcionando!**
4. Se não aparecer: Execute `diagnostico_permissoes.bat`

## 🆘 Suporte

Se nada funcionar:

1. Execute: `diagnostico_permissoes.bat`
2. Salve o resultado
3. Consulte: [PERMISSOES_ANDROID.md](PERMISSOES_ANDROID.md)
4. Abra uma issue no GitHub com:
   - Modelo do celular
   - Versão do Android
   - Resultado do diagnóstico

---

**Dica:** Mantenha o app aberto durante o primeiro teste para facilitar o debug!
