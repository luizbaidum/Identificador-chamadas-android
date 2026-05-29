package com.example.identificadorchamadas.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.identificadorchamadas.R
import com.example.identificadorchamadas.databinding.ActivitySettingsBinding
import com.example.identificadorchamadas.utils.PreferenceManager

/**
 * Activity de Configurações
 * Permite personalizar mensagens e comportamento do bot
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.settings)

        prefManager = PreferenceManager(this)

        loadSettings()
        setupListeners()
    }

    private fun loadSettings() {
        // Carregar mensagem de saudação salva
        val greetingMessage = prefManager.getGreetingMessage()
        binding.etGreetingMessage.setText(greetingMessage)

        // Carregar configuração de auto-resposta
        val autoAnswer = prefManager.isAutoAnswerEnabled()
        binding.switchAutoAnswer.isChecked = autoAnswer
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            saveSettings()
        }
    }

    private fun saveSettings() {
        // Desabilitar botão temporariamente para evitar múltiplos cliques
        binding.btnSave.isEnabled = false
        binding.btnSave.text = "Salvando..."

        // Obter texto da mensagem
        val greetingMessage = binding.etGreetingMessage.text.toString().trim()

        // Validar que não está vazio
        if (greetingMessage.isEmpty()) {
            binding.etGreetingMessage.error = "A mensagem não pode estar vazia"
            Toast.makeText(this, "Digite uma mensagem de saudação", Toast.LENGTH_SHORT).show()
            
            // Reabilitar botão
            binding.btnSave.isEnabled = true
            binding.btnSave.text = getString(R.string.save)
            return
        }

        // Salvar mensagem de saudação
        prefManager.setGreetingMessage(greetingMessage)

        // Salvar configuração de auto-resposta
        val autoAnswer = binding.switchAutoAnswer.isChecked
        prefManager.setAutoAnswerEnabled(autoAnswer)

        // Mostrar feedback de sucesso
        Toast.makeText(this, "✅ Configurações salvas com sucesso!", Toast.LENGTH_SHORT).show()

        // Reabilitar botão após 1 segundo
        binding.btnSave.postDelayed({
            binding.btnSave.isEnabled = true
            binding.btnSave.text = getString(R.string.save)
        }, 1000)

        // Opcionalmente, voltar para a tela anterior após salvar
        // finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
