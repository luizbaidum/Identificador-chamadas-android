package com.example.identificadorchamadas.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.identificadorchamadas.databinding.OverlayCallScreenBinding
import com.example.identificadorchamadas.utils.CallManager
import com.example.identificadorchamadas.utils.PreferenceManager
import com.example.identificadorchamadas.utils.SpeechManager

/**
 * Gerenciador da tela de overlay que aparece sobre a chamada
 */
object CallOverlayManager {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var binding: OverlayCallScreenBinding? = null
    private var callManager: CallManager? = null
    private var speechManager: SpeechManager? = null
    private var prefManager: PreferenceManager? = null
    private var currentPhoneNumber: String? = null

    @SuppressLint("InflateParams")
    fun showOverlay(context: Context, phoneNumber: String) {
        if (overlayView != null) {
            // Overlay já está sendo exibido
            return
        }

        currentPhoneNumber = phoneNumber
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        callManager = CallManager(context)
        speechManager = SpeechManager(context)
        prefManager = PreferenceManager(context)

        binding = OverlayCallScreenBinding.inflate(LayoutInflater.from(context))
        overlayView = binding?.root

        val layoutType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        setupButtons(context)
        binding?.tvPhoneNumber?.text = phoneNumber

        try {
            windowManager?.addView(overlayView, params)
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao exibir overlay: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons(context: Context) {
        binding?.apply {
            btnAnswerWithBot.setOnClickListener {
                answerWithBot(context)
            }

            btnAnswerNormal.setOnClickListener {
                callManager?.answerCall()
                hideOverlay()
            }

            btnReject.setOnClickListener {
                callManager?.rejectCall()
                hideOverlay()
            }

            btnTakeCall.setOnClickListener {
                // Desabilitar o bot e continuar normalmente
                speechManager?.stopListening()
                hideOverlay()
            }

            btnHangUp.setOnClickListener {
                callManager?.endCall()
                hideOverlay()
            }
        }
    }

    private fun answerWithBot(context: Context) {
        // Atender a chamada
        callManager?.answerCall()

        // Esconder botões iniciais e mostrar área de transcrição
        binding?.apply {
            layoutInitialButtons.visibility = View.GONE
            layoutTranscription.visibility = View.VISIBLE
        }

        // Aguardar um momento para a chamada se estabelecer
        overlayView?.postDelayed({
            // Reproduzir mensagem do bot (carregada das preferências)
            val greetingMessage = prefManager?.getGreetingMessage()
                ?: "Olá, sou o assistente virtual. Qual o motivo da ligação?"
            
            speechManager?.speak(greetingMessage) {
                // Após falar, começar a ouvir
                startListening(context)
            }
        }, 1000)
    }

    private fun startListening(context: Context) {
        speechManager?.startListening { transcription ->
            // Atualizar a transcrição na tela
            binding?.tvTranscription?.text = transcription
        }
    }

    fun hideOverlay() {
        try {
            speechManager?.stopListening()
            speechManager?.shutdown()
            overlayView?.let {
                windowManager?.removeView(it)
            }
            overlayView = null
            binding = null
            windowManager = null
            callManager = null
            speechManager = null
            prefManager = null
            currentPhoneNumber = null
        } catch (e: Exception) {
            // Ignora erros ao remover view
        }
    }
}
