package com.example.identificadorchamadas.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gerenciador de preferências do app
 */
class PreferenceManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun isServiceEnabled(): Boolean {
        return prefs.getBoolean(KEY_SERVICE_ENABLED, false)
    }

    fun setServiceEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
    }

    fun getGreetingMessage(): String {
        return prefs.getString(
            KEY_GREETING_MESSAGE,
            "Olá, sou o assistente virtual. Qual o motivo da ligação?"
        ) ?: "Olá, sou o assistente virtual. Qual o motivo da ligação?"
    }

    fun setGreetingMessage(message: String) {
        prefs.edit().putString(KEY_GREETING_MESSAGE, message).apply()
    }

    fun isAutoAnswerEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_ANSWER, false)
    }

    fun setAutoAnswerEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_ANSWER, enabled).apply()
    }

    companion object {
        private const val PREFS_NAME = "call_screen_prefs"
        private const val KEY_SERVICE_ENABLED = "service_enabled"
        private const val KEY_GREETING_MESSAGE = "greeting_message"
        private const val KEY_AUTO_ANSWER = "auto_answer"
    }
}
