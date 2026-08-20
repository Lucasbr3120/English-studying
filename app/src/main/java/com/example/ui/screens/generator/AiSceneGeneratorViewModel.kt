package com.example.ui.screens.generator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CefrLevel
import com.example.data.model.Scene
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GeneratorUiState(
    val userPrompt: String = "",
    val selectedLevel: CefrLevel = CefrLevel.A2,
    val isGenerating: Boolean = false,
    val generatedScene: Scene? = null,
    val errorMessage: String? = null
)

class AiSceneGeneratorViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(GeneratorUiState())
    val uiState: StateFlow<GeneratorUiState> = _uiState.asStateFlow()

    fun onPromptChanged(prompt: String) {
        _uiState.update { it.copy(userPrompt = prompt, errorMessage = null) }
    }

    fun onLevelSelected(level: CefrLevel) {
        _uiState.update { it.copy(selectedLevel = level) }
    }

    fun generateScene(onSuccess: (String) -> Unit) {
        val prompt = _uiState.value.userPrompt.trim()
        if (prompt.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, digite o tema ou situação da cena desejada.") }
            return
        }

        _uiState.update { it.copy(isGenerating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val scene = repository.aiService.generateCustomScene(prompt, _uiState.value.selectedLevel)
                repository.addCustomScene(scene)
                _uiState.update { it.copy(isGenerating = false, generatedScene = scene) }
                onSuccess(scene.id)
            } catch (e: Exception) {
                _uiState.update { it.copy(isGenerating = false, errorMessage = "Erro ao gerar cena: ${e.message}") }
            }
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AiSceneGeneratorViewModel(repository) as T
        }
    }
}
