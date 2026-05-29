package com.example.identificadorchamadas.utils

import android.content.Context
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log

/**
 * Gerenciador de chamadas
 * Controla atender, recusar e encerrar chamadas
 */
class CallManager(private val context: Context) {

    private val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager

    /**
     * Atende uma chamada recebida
     */
    fun answerCall(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telecomManager.acceptRingingCall()
                true
            } else {
                Log.w(TAG, "Answer call not supported in this Android version")
                false
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to answer call", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error answering call", e)
            false
        }
    }

    /**
     * Recusa uma chamada recebida
     */
    fun rejectCall(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall()
                true
            } else {
                Log.w(TAG, "Reject call not supported in this Android version")
                false
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to reject call", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error rejecting call", e)
            false
        }
    }

    /**
     * Encerra uma chamada em andamento
     */
    fun endCall(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall()
                true
            } else {
                Log.w(TAG, "End call not supported in this Android version")
                false
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to end call", e)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error ending call", e)
            false
        }
    }

    companion object {
        private const val TAG = "CallManager"
    }
}
