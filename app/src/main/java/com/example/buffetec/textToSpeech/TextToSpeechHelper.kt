package com.example.buffetec.textToSpeech


import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*

class TextToSpeechHelper(context: Context) {
    private lateinit var textToSpeech: TextToSpeech
    private var selectedLanguage: Locale = Locale.US  // Idioma por defecto

    init {
        // Inicializa TextToSpeech
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Configura el idioma por defecto
                textToSpeech.language = selectedLanguage

                // Ajusta la voz masculina si está disponible
                val maleVoice = textToSpeech.voices.find {
                    it.name.contains("male", ignoreCase = true) || it.name.contains("hombre", ignoreCase = true)
                }
                if (maleVoice != null) {
                    textToSpeech.voice = maleVoice
                } else {
                    Log.e("TTS", "No se encontró una voz masculina en el dispositivo.")
                }
            }
        }
    }

    // Método para cambiar el idioma
    fun setLanguage(language: String) {
        selectedLanguage = when (language) {
            "es" -> Locale("es", "MX") // Español (México)
            else -> Locale.US // Inglés por defecto
        }
        textToSpeech.language = selectedLanguage
    }

    // Método para hablar
    fun speak(text: String) {
        if (::textToSpeech.isInitialized) {
            adjustVoice(pitch = 0.7f, rate = 1.0f)  // Ajustar según lo que prefieras
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }
    fun adjustVoice(pitch: Float, rate: Float) {
        if (::textToSpeech.isInitialized) {
            textToSpeech.setPitch(pitch) // Valores menores de 1.0 harán la voz más grave
            textToSpeech.setSpeechRate(rate) // Ajusta la velocidad de la voz
        }
    }

    // Método para detener y liberar el TTS
    fun shutdown() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
}