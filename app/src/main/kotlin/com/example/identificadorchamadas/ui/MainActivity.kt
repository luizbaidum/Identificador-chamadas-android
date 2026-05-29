package com.example.identificadorchamadas.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.identificadorchamadas.R
import com.example.identificadorchamadas.databinding.ActivityMainBinding
import com.example.identificadorchamadas.service.CallMonitorService
import com.example.identificadorchamadas.utils.PreferenceManager

/**
 * Activity Principal
 * Gerencia permissões e controla o serviço de monitoramento
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PreferenceManager

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            checkOverlayPermission()
        } else {
            Toast.makeText(this, "Permissões necessárias negadas", Toast.LENGTH_LONG).show()
        }
    }

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "Permissão de sobreposição concedida", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissão de sobreposição negada", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PreferenceManager(this)

        setupUI()
        updateServiceStatus()
    }

    private fun setupUI() {
        binding.switchService.isChecked = prefManager.isServiceEnabled()
        
        binding.switchService.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (hasAllPermissions()) {
                    startMonitoringService()
                } else {
                    binding.switchService.isChecked = false
                    requestAllPermissions()
                }
            } else {
                stopMonitoringService()
            }
        }

        binding.btnPermissions.setOnClickListener {
            requestAllPermissions()
        }

        binding.btnOverlaySettings.setOnClickListener {
            openOverlaySettings()
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        updatePermissionsStatus()
    }

    private fun hasAllPermissions(): Boolean {
        val requiredPermissions = mutableListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val allGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        return allGranted && Settings.canDrawOverlays(this)
    }

    private fun requestAllPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        requestPermissionsLauncher.launch(permissions.toTypedArray())
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            // Tentar abrir a configuração específica do app
            try {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                overlayPermissionLauncher.launch(intent)
                
                // Mostrar instruções detalhadas
                Toast.makeText(
                    this,
                    "Ative 'Exibir sobre outros apps' nas configurações",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                // Fallback: abrir configurações gerais do app
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                    Toast.makeText(
                        this,
                        "Vá em Permissões > Apps especiais > Exibir sobre outros apps",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (ex: Exception) {
                    Toast.makeText(
                        this,
                        "Abra Configurações > Apps > Permissões especiais > Exibir sobre outros apps",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } else {
            Toast.makeText(this, "Todas as permissões concedidas!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMonitoringService() {
        prefManager.setServiceEnabled(true)
        val intent = Intent(this, CallMonitorService::class.java)
        ContextCompat.startForegroundService(this, intent)
        updateServiceStatus()
        Toast.makeText(this, "Serviço iniciado", Toast.LENGTH_SHORT).show()
    }

    private fun stopMonitoringService() {
        prefManager.setServiceEnabled(false)
        val intent = Intent(this, CallMonitorService::class.java)
        stopService(intent)
        updateServiceStatus()
        Toast.makeText(this, "Serviço parado", Toast.LENGTH_SHORT).show()
    }

    private fun updateServiceStatus() {
        val isRunning = prefManager.isServiceEnabled()
        binding.tvStatus.text = if (isRunning) {
            getString(R.string.service_running)
        } else {
            getString(R.string.service_stopped)
        }
    }

    private fun openOverlaySettings() {
        try {
            // Tentar abrir diretamente a configuração de overlay do app
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
            Toast.makeText(
                this,
                "Ative a opção 'Permitir exibição sobre outros apps'",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            // Fallback: abrir configurações do app
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
                Toast.makeText(
                    this,
                    "Vá em Permissões > Avançado > Exibir sobre outros apps",
                    Toast.LENGTH_LONG
                ).show()
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Erro ao abrir configurações: ${ex.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updatePermissionsStatus() {
        val overlayGranted = Settings.canDrawOverlays(this)
        val phoneGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
        val callLogGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CALL_LOG
        ) == PackageManager.PERMISSION_GRANTED
        val audioGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        
        val statusText = StringBuilder()
        statusText.append("✓ Overlay: ${if (overlayGranted) "Permitido" else "NEGADO"}\n")
        statusText.append("✓ Telefone: ${if (phoneGranted) "Permitido" else "NEGADO"}\n")
        statusText.append("✓ Registro: ${if (callLogGranted) "Permitido" else "NEGADO"}\n")
        statusText.append("✓ Áudio: ${if (audioGranted) "Permitido" else "NEGADO"}")
        
        binding.tvPermissionDetails.text = statusText.toString()
        
        // Destacar overlay se não estiver concedido
        if (!overlayGranted) {
            binding.btnOverlaySettings.setBackgroundColor(
                resources.getColor(android.R.color.holo_red_light, theme)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switchService.isChecked = prefManager.isServiceEnabled()
        updateServiceStatus()
        updatePermissionsStatus()
    }
}
