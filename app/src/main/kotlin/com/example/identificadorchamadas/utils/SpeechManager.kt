package com.example.identificadorchamadas.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

/**
 * Gerenciador de reconhecimento de fala e síntese de voz
 */
class SpeechManager(private val context: Context) {

    private var textToSpeech: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var transcriptionCallback: ((String) -> Unit)? = null
    private var fullTranscription = StringBuilder()

    init {
        initTextToSpeech()
        initSpeechRecognizer()
    }

    private fun initTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.getDefault()
            } else {
                Log.e(TAG, "TTS initialization failed")
            }
        }
    }

    private fun initSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        } else {
            Log.e(TAG, "Speech recognition not available")
        }
    }

    /**
     * Fala um texto usando Text-to-Speech
     */
    fun speak(text: String, onComplete: (() -> Unit)? = null) {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Log.d(TAG, "TTS started")
            }

            override fun onDone(utteranceId: String?) {
                Log.d(TAG, "TTS completed")
                onComplete?.invoke()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                Log.e(TAG, "TTS error")
            }
        })

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId")
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId")
    }

    /**
     * Inicia o reconhecimento contínuo de fala
     */
    fun startListening(onTranscription: (String) -> Unit) {
        transcriptionCallback = onTranscription
        fullTranscription.clear()
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
                Log.d(TAG, "Ready for speech")
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "Speech started")
            }

            override fun onRmsChanged(rmsdB: Float) {
                // Volume do áudio
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Buffer de áudio
            }

            override fun onEndOfSpeech() {
                Log.d(TAG, "Speech ended")
            }

            override fun onError(error: Int) {
                Log.e(TAG, "Recognition error: $error")
                isListening = false
                
                // Reiniciar o reconhecimento se ainda estivermos ouvindo
                if (transcriptionCallback != null) {
                    speechRecognizer?.startListening(intent)
                }
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { result ->
                    fullTranscription.append(" ").append(result)
                    transcriptionCallback?.invoke(fullTranscription.toString().trim())
                }
                
                // Continuar ouvindo
                if (transcriptionCallback != null) {
                    speechRecognizer?.startListening(intent)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { result ->
                    val preview = "${fullTranscription} $result"
                    transcriptionCallback?.invoke(preview.trim())
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Eventos customizados
            }
        })

        speechRecognizer?.startListening(intent)
    }

    /**
     * Para o reconhecimento de fala
     */
    fun stopListening() {
        isListening = false
        transcriptionCallback = null
        speechRecognizer?.stopListening()
    }

    /**
     * Libera os recursos
     */
    fun shutdown() {
        stopListening()
        speechRecognizer?.destroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    companion object {
        private const val TAG = "SpeechManager"
    }
}
