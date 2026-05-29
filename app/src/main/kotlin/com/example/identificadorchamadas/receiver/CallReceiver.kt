package com.example.identificadorchamadas.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.identificadorchamadas.overlay.CallOverlayManager
import com.example.identificadorchamadas.utils.PreferenceManager

/**
 * Receiver para detectar mudanças no estado das chamadas
 */
class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prefManager = PreferenceManager(context)
        
        if (!prefManager.isServiceEnabled()) {
            return
        }

        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.d(TAG, "Phone state changed: $state, number: $phoneNumber")

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Chamada recebida - mostrar overlay
                    phoneNumber?.let {
                        CallOverlayManager.showOverlay(context, it)
                    }
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Chamada atendida
                    Log.d(TAG, "Call answered")
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Chamada encerrada - remover overlay
                    CallOverlayManager.hideOverlay()
                }
            }
        }
    }

    companion object {
        private const val TAG = "CallReceiver"
    }
}
