package com.example.ui.screens.youtube

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.IntelligentCorrectionEngine
import com.example.data.model.AiCorrectionResult
import com.example.data.model.ScenePhrase
import com.example.data.model.YouTubeStudyPhrase
import com.example.data.model.YouTubeVideoItem
import com.example.data.repository.YouTubeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class YouTubeStudyStep {
    OVERVIEW,
    CONTRACTION,
    TRANSLATION,
    SPOKEN_ENGLISH,
    VOCABULARY,
    COMPREHENSION,
    SUMMARY
}

data class YouTubeStudyUiState(
    val video: YouTubeVideoItem? = null,
    val isLoading: Boolean = true,
    val currentPhraseIndex: Int = 0,
    val currentStep: YouTubeStudyStep = YouTubeStudyStep.OVERVIEW,
    val contractionInput: String = "",
    val contractionEvaluation: AiCorrectionResult? = null,
    val isEvaluatingContraction: Boolean = false,
    val contractionAttempts: Int = 0,
    val translationInput: String = "",
    val translationEvaluation: AiCorrectionResult? = null,
    val isEvaluatingTranslation: Boolean = false,
    val translationAttempts: Int = 0,
    val selectedQuizOption: String? = null,
    val isQuizAnswered: Boolean = false,
    val isQuizCorrect: Boolean = false,
    val totalPhrasesCompleted: Int = 0,
    val totalCorrect: Int = 0,
    val totalErrors: Int = 0
)

fun YouTubeStudyPhrase.toScenePhrase(): ScenePhrase {
    return ScenePhrase(
        id = id,
        characterName = "Speaker",
        fullForm = fullForm,
        naturalForm = contractedForm,
        portugueseTranslation = portugueseTranslation,
        acceptableTranslations = acceptableTranslations,
        contractionsUsed = contractionsList,
        vocabularyNotes = vocabularyNotes.joinToString("; ") { "${it.word}: ${it.translation} (${it.meaning})" },
        grammarTip = grammarExplanation,
        additionalExample = spokenTip ?: "",
        additionalExampleTranslation = ""
    )
}

class YouTubeStudyViewModel(
    private val videoId: String,
    private val repository: YouTubeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(YouTubeStudyUiState())
    val uiState: StateFlow<YouTubeStudyUiState> = _uiState.asStateFlow()

    init {
        loadVideo()
    }

    private fun loadVideo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val video = repository.getVideoById(videoId)
            _uiState.update {
                it.copy(
                    video = video,
                    isLoading = false
                )
            }
        }
    }

    fun startStudy() {
        _uiState.update {
            it.copy(
                currentPhraseIndex = 0,
                currentStep = YouTubeStudyStep.CONTRACTION,
                contractionInput = "",
                contractionEvaluation = null,
                contractionAttempts = 0
            )
        }
    }

    fun onContractionInputChanged(input: String) {
        _uiState.update { it.copy(contractionInput = input) }
    }

    fun onTranslationInputChanged(input: String) {
        _uiState.update { it.copy(translationInput = input) }
    }

    fun evaluateContraction() {
        val state = _uiState.value
        val phrases = state.video?.authorizedStudySet?.phrases ?: return
        val phrase = phrases.getOrNull(state.currentPhraseIndex) ?: return

        _uiState.update { it.copy(isEvaluatingContraction = true) }

        viewModelScope.launch {
            val (eval, _) = IntelligentCorrectionEngine.evaluateContractions(
                userInput = state.contractionInput,
                phrase = phrase.toScenePhrase()
            )

            _uiState.update {
                it.copy(
                    isEvaluatingContraction = false,
                    contractionEvaluation = eval,
                    contractionAttempts = it.contractionAttempts + 1
                )
            }
        }
    }

    fun proceedToTranslation() {
        _uiState.update {
            it.copy(
                currentStep = YouTubeStudyStep.TRANSLATION,
                translationInput = "",
                translationEvaluation = null,
                translationAttempts = 0
            )
        }
    }

    fun evaluateTranslation() {
        val state = _uiState.value
        val phrases = state.video?.authorizedStudySet?.phrases ?: return
        val phrase = phrases.getOrNull(state.currentPhraseIndex) ?: return

        _uiState.update { it.copy(isEvaluatingTranslation = true) }

        viewModelScope.launch {
            val eval = IntelligentCorrectionEngine.evaluateTranslation(
                userInput = state.translationInput,
                phrase = phrase.toScenePhrase()
            )

            _uiState.update {
                it.copy(
                    isEvaluatingTranslation = false,
                    translationEvaluation = eval,
                    translationAttempts = it.translationAttempts + 1
                )
            }
        }
    }

    fun proceedToSpokenEnglish() {
        _uiState.update { it.copy(currentStep = YouTubeStudyStep.SPOKEN_ENGLISH) }
    }

    fun proceedToVocabulary() {
        _uiState.update { it.copy(currentStep = YouTubeStudyStep.VOCABULARY) }
    }

    fun proceedToComprehension() {
        _uiState.update {
            it.copy(
                currentStep = YouTubeStudyStep.COMPREHENSION,
                selectedQuizOption = null,
                isQuizAnswered = false,
                isQuizCorrect = false
            )
        }
    }

    fun onSelectQuizOption(option: String) {
        val state = _uiState.value
        val phrases = state.video?.authorizedStudySet?.phrases ?: return
        val phrase = phrases.getOrNull(state.currentPhraseIndex) ?: return

        val isCorrect = option.trim().equals(phrase.comprehensionCorrectAnswer.trim(), ignoreCase = true)

        _uiState.update {
            it.copy(
                selectedQuizOption = option,
                isQuizAnswered = true,
                isQuizCorrect = isCorrect
            )
        }

        // Record progress in repository
        viewModelScope.launch {
            repository.recordYouTubeStudyResult(
                videoId = videoId,
                phrase = phrase,
                wasContractionCorrect = state.contractionEvaluation?.isCorrect == true,
                wasTranslationCorrect = state.translationEvaluation?.isCorrect == true,
                wasComprehensionCorrect = isCorrect
            )
        }
    }

    fun nextPhrase() {
        val state = _uiState.value
        val phrases = state.video?.authorizedStudySet?.phrases ?: return

        val nextIndex = state.currentPhraseIndex + 1
        if (nextIndex < phrases.size) {
            _uiState.update {
                it.copy(
                    currentPhraseIndex = nextIndex,
                    currentStep = YouTubeStudyStep.CONTRACTION,
                    contractionInput = "",
                    contractionEvaluation = null,
                    contractionAttempts = 0,
                    translationInput = "",
                    translationEvaluation = null,
                    translationAttempts = 0,
                    selectedQuizOption = null,
                    isQuizAnswered = false,
                    totalPhrasesCompleted = it.totalPhrasesCompleted + 1
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    currentStep = YouTubeStudyStep.SUMMARY,
                    totalPhrasesCompleted = phrases.size
                )
            }
        }
    }

    fun restartStudy() {
        _uiState.update {
            it.copy(
                currentPhraseIndex = 0,
                currentStep = YouTubeStudyStep.CONTRACTION,
                contractionInput = "",
                contractionEvaluation = null,
                contractionAttempts = 0,
                translationInput = "",
                translationEvaluation = null,
                translationAttempts = 0,
                selectedQuizOption = null,
                isQuizAnswered = false
            )
        }
    }

    class Factory(
        private val videoId: String,
        private val repository: YouTubeRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return YouTubeStudyViewModel(videoId, repository) as T
        }
    }
}
