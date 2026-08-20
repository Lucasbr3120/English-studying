package com.example.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed class SpeechRecognitionState {
    object Idle : SpeechRecognitionState()
    object Listening : SpeechRecognitionState()
    object Processing : SpeechRecognitionState()
    data class Success(val recognizedText: String, val confidence: Float) : SpeechRecognitionState()
    data class Error(val errorMessage: String) : SpeechRecognitionState()
}

class SpeechRecognitionHelper(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private val _state = MutableStateFlow<SpeechRecognitionState>(SpeechRecognitionState.Idle)
    val state: StateFlow<SpeechRecognitionState> = _state.asStateFlow()

    private val isRecognitionAvailable: Boolean
        get() = SpeechRecognizer.isRecognitionAvailable(context)

    fun startListening() {
        if (!isRecognitionAvailable) {
            _state.value = SpeechRecognitionState.Error(
                "Serviço de reconhecimento de voz indisponível no dispositivo. Você pode digitar sua fala no modo manual."
            )
            return
        }

        try {
            stopListening()

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _state.value = SpeechRecognitionState.Listening
                    }

                    override fun onBeginningOfSpeech() {
                        _state.value = SpeechRecognitionState.Listening
                    }

                    override fun onRmsChanged(rmsdB: Float) {}

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        _state.value = SpeechRecognitionState.Processing
                    }

                    override fun onError(error: Int) {
                        val message = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Erro na gravação de áudio."
                            SpeechRecognizer.ERROR_CLIENT -> "Erro do cliente de voz."
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permissão de microfone necessária."
                            SpeechRecognizer.ERROR_NETWORK -> "Erro de rede ao processar voz."
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tempo de rede esgotado."
                            SpeechRecognizer.ERROR_NO_MATCH -> "Nenhuma fala foi reconhecida. Tente novamente."
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconhecedor ocupado. Tente de novo."
                            SpeechRecognizer.ERROR_SERVER -> "Erro nos servidores de voz."
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Nenhum som detectado. Fale próximo ao microfone."
                            else -> "Não foi possível reconhecer o áudio. Tente novamente."
                        }
                        Log.w("SpeechHelper", "Speech recognition error code: $error ($message)")
                        _state.value = SpeechRecognitionState.Error(message)
                    }

                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val confScores = results?.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES)
                        val text = matches?.firstOrNull() ?: ""
                        val confidence = confScores?.firstOrNull()?.coerceIn(0.5f, 1.0f) ?: 0.9f

                        if (text.isNotBlank()) {
                            _state.value = SpeechRecognitionState.Success(text, confidence)
                        } else {
                            _state.value = SpeechRecognitionState.Error("Nenhuma palavra detectada.")
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val partial = matches?.firstOrNull()
                        if (!partial.isNullOrBlank()) {
                            _state.value = SpeechRecognitionState.Listening
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "en-US")
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale a frase em inglês...")
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e("SpeechHelper", "Failed to start speech recognition: ${e.message}")
            _state.value = SpeechRecognitionState.Error("Erro ao iniciar microfone: ${e.localizedMessage}")
        }
    }

    fun submitManualTranscript(text: String, confidence: Float = 0.95f) {
        if (text.isNotBlank()) {
            _state.value = SpeechRecognitionState.Success(text.trim(), confidence)
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.cancel()
            speechRecognizer?.destroy()
        } catch (e: Exception) {
            Log.w("SpeechHelper", "Error stopping recognizer: ${e.message}")
        } finally {
            speechRecognizer = null
        }
    }

    fun resetState() {
        stopListening()
        _state.value = SpeechRecognitionState.Idle
    }
}
