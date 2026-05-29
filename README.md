# Identificador de Chamadas Android

App Android nativo em Kotlin que intercepta chamadas recebidas, permite atender com um assistente virtual que fala uma mensagem pré-definida e transcreve a resposta do interlocutor em tempo real.

## 🎯 Funcionalidades

- ✅ Detecta chamadas recebidas automaticamente
- ✅ Exibe overlay customizado sobre a tela de chamada nativa
- ✅ Opção de atender com robô assistente virtual
- ✅ Síntese de voz (Text-to-Speech) para mensagem inicial
- ✅ Reconhecimento de fala em tempo real (Speech-to-Text)
- ✅ Transcrição ao vivo da conversa
- ✅ Controles para pegar a ligação manualmente ou desligar
- ✅ Serviço em foreground para monitoramento contínuo
- ✅ Configurações personalizáveis

## 🏗️ Arquitetura

O projeto segue uma estrutura organizada e modular:

```
app/
├── src/main/
│   ├── kotlin/com/example/identificadorchamadas/
│   │   ├── CallScreenApp.kt              # Application class
│   │   ├── ui/
│   │   │   ├── MainActivity.kt           # Tela principal
│   │   │   └── SettingsActivity.kt       # Configurações
│   │   ├── service/
│   │   │   └── CallMonitorService.kt     # Foreground service
│   │   ├── receiver/
│   │   │   └── CallReceiver.kt           # BroadcastReceiver para chamadas
│   │   ├── overlay/
│   │   │   └── CallOverlayManager.kt     # Gerenciador do overlay
│   │   └── utils/
│   │       ├── PreferenceManager.kt      # Gerenciamento de preferências
│   │       ├── CallManager.kt            # Controle de chamadas
│   │       └── SpeechManager.kt          # TTS e STT
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── activity_settings.xml
│   │   │   └── overlay_call_screen.xml
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   └── xml/
│   │       ├── backup_rules.xml
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml
```

## 🔧 Tecnologias Utilizadas

- **Linguagem**: Kotlin 1.9.22
- **SDK Mínimo**: Android 8.0 (API 26)
- **SDK Alvo**: Android 15 (API 35)
- **Gradle**: 8.3.0
- **ViewBinding**: Habilitado
- **Bibliotecas principais**:
  - AndroidX Core KTX
  - Material Design Components
  - Coroutines
  - Lifecycle Components
  - WorkManager

## 📋 Componentes Principais

### 1. **CallMonitorService**
Serviço em foreground que mantém o app ativo em segundo plano para detectar chamadas.

### 2. **CallReceiver**
BroadcastReceiver que escuta mudanças no estado do telefone (`PHONE_STATE`).

### 3. **CallOverlayManager**
Gerencia a exibição da tela flutuante sobre a interface de chamada nativa usando `WindowManager`.

### 4. **CallManager**
Controla as ações de atender, recusar e encerrar chamadas usando `TelecomManager`.

### 5. **SpeechManager**
Gerencia:
- **Text-to-Speech (TTS)**: Para o robô falar a mensagem inicial
- **Speech-to-Text (STT)**: Para reconhecer e transcrever a fala do interlocutor

## 🔐 Permissões Necessárias

O app requer as seguintes permissões (solicitadas em tempo de execução):

```xml
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.ANSWER_PHONE_CALLS" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## 🚀 Como Executar

### Pré-requisitos
- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Dispositivo físico Android (recomendado) ou emulador

### Passos

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/Identificador-chamadas-android.git
cd Identificador-chamadas-android
```

2. **Abra no Android Studio**
   - File → Open → Selecione a pasta do projeto

3. **Sincronize as dependências**
   - O Gradle irá baixar automaticamente as dependências

4. **Execute no dispositivo**
   - Conecte um dispositivo Android via USB (com depuração USB ativada)
   - Ou inicie um emulador
   - Clique em "Run" (▶️)

5. **Conceda as permissões**
   - Ao abrir o app, clique em "Conceder Permissões"
   - Aceite todas as permissões solicitadas
   - Habilite a permissão de sobreposição nas configurações do sistema

6. **Ative o serviço**
   - Na tela principal, ative o switch "Ativar Serviço"

## � Solução de Problemas - Permissões

### ⚠️ Problema: Não consegue ativar "Exibir sobre outros apps" (Android 14+/16)

Se você instalou o APK e não consegue habilitar a permissão de overlay:

#### Método 1: Scripts Automáticos (Recomendado)
Execute os scripts na raiz do projeto:

```bash
# Windows - Conceder todas as permissões automaticamente
configurar_permissoes.bat

# Para diagnóstico de problemas
diagnostico_permissoes.bat
```

#### Método 2: ADB Manual
Com o dispositivo conectado via USB:

```bash
# Conceder permissão de overlay via ADB
adb shell appops set com.example.identificadorchamadas SYSTEM_ALERT_WINDOW allow

# Abrir configurações do app
adb shell am start -a android.settings.ACTION_MANAGE_OVERLAY_PERMISSION -d package:com.example.identificadorchamadas
```

#### Método 3: Configurações do Sistema
Caminho manual depende do fabricante:

**Android Puro/Google Pixel:**
```
Configurações → Apps → Acesso especial → Exibir sobre outros apps
```

**Samsung:**
```
Configurações → Apps → ⋮ Menu → Acesso especial → Exibir sobre outros apps
```

**Xiaomi/MIUI:**
```
Configurações → Apps → Gerenciar apps → [App] → Outras permissões → Exibir sobre outros apps
```

#### Método 4: Botão Integrado no App
A partir da versão atual, o app possui:
- ✅ Botão "Configurar Overlay" na tela principal
- ✅ Status das permissões em tempo real
- ✅ Indicador visual vermelho se overlay não estiver ativo

📖 **Mais detalhes:** Consulte [PERMISSOES_ANDROID.md](PERMISSOES_ANDROID.md) para troubleshooting completo.

## �📱 Fluxo de Uso

1. **Chamada recebida** → CallReceiver detecta
2. **Overlay aparece** → Mostra número e opções
3. **Usuário escolhe**:
   - **"Atender com Robô"**: App atende, fala mensagem, inicia transcrição
   - **"Atender Normal"**: Atende normalmente
   - **"Recusar"**: Rejeita a chamada
4. **Durante transcrição**: Usuário pode "Pegar Ligação" ou "Desligar"
5. **Chamada encerra** → Overlay desaparece automaticamente

## ⚠️ Limitações Conhecidas

### 1. **Captura de Áudio da Chamada**
Em dispositivos modernos (Android 10+), capturar o áudio da chamada via operadora é extremamente restrito por questões de privacidade. Este app implementa a interface, mas a transcrição pode não funcionar perfeitamente em todos os dispositivos.

**Soluções alternativas**:
- Usar chamadas VoIP (WhatsApp, Telegram) onde há controle total do áudio
- Testar em diferentes fabricantes (alguns permitem mais acesso que outros)
- Usar em dispositivos com root (não recomendado)

### 2. **Gerenciamento de Energia**
Alguns fabricantes (Xiaomi, Huawei, Samsung) têm gerenciadores de bateria agressivos que podem matar o serviço em segundo plano.

**Solução**: Adicionar o app à lista de exceções de economia de bateria.

### 3. **Android 14+**
As APIs de controle de chamadas estão cada vez mais restritas. Algumas funcionalidades podem requerer que o app seja definido como discador padrão.

## 🎨 Personalização

### Alterar Mensagem do Robô
1. Abra o app
2. Vá em "Configurações"
3. Edite o campo "Mensagem de Saudação"
4. Clique em "Salvar"

### Auto-responder
Ative o switch "Responder Automaticamente" nas configurações para que o robô atenda todas as chamadas automaticamente.

## 🧪 Testando

Para testar o app:

1. **Teste de overlay**: Ligue para o seu número de outro telefone
2. **Teste de permissões**: Verifique se todas foram concedidas
3. **Teste de TTS**: Atenda com o robô e ouça a mensagem
4. **Teste de STT**: Fale algo e veja se aparece na transcrição

## 📚 Referências Técnicas

- [Android TelecomManager](https://developer.android.com/reference/android/telecom/TelecomManager)
- [SpeechRecognizer API](https://developer.android.com/reference/android/speech/SpeechRecognizer)
- [TextToSpeech API](https://developer.android.com/reference/android/speech/tts/TextToSpeech)
- [WindowManager Overlays](https://developer.android.com/reference/android/view/WindowManager)
- [Foreground Services](https://developer.android.com/guide/components/foreground-services)

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é de código aberto para fins educacionais.

## ⚖️ Aviso Legal

Este app foi desenvolvido para fins educacionais e de demonstração. A gravação de chamadas telefônicas sem consentimento pode ser ilegal em algumas jurisdições. Use com responsabilidade e em conformidade com as leis locais.

## 📧 Contato

Para dúvidas ou sugestões, abra uma issue no GitHub.

---

**Desenvolvido com ❤️ usando Kotlin e Android SDK**
