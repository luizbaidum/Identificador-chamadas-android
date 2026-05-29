package com.example.identificadorchamadas

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * Classe de aplicação principal
 * Inicializa componentes globais e canais de notificação
 */
class CallScreenApp : Application() {

    companion object {
        const val CHANNEL_ID = "call_monitor_channel"
        const val CHANNEL_NAME = "Call Monitoring Service"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * Cria o canal de notificação necessário para o Foreground Service
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora chamadas recebidas em tempo real"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
