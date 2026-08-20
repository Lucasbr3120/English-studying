package com.example.ui.screens.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ai.IntelligentCorrectionEngine
import com.example.data.ai.ProgressiveHint
import com.example.data.model.AiCorrectionResult
import com.example.data.model.Scene
import com.example.data.model.ScenePhrase
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ExerciseStepType {
    CONTRACTION_INPUT,
    CONTRACTION_FEEDBACK,
    TRANSLATION_INPUT,
    TRANSLATION_FEEDBACK,
    GAP_FILL_INPUT,
    GAP_FILL_FEEDBACK,
    QUIZ_INPUT,
    QUIZ_FEEDBACK,
    COMPLETED
}

data class ExerciseUiState(
    val scene: Scene? = null,
    val currentPhraseIndex: Int = 0,
    val currentStepType: ExerciseStepType = ExerciseStepType.CONTRACTION_INPUT,
    val contractionInputText: String = "",
    val translationInputText: String = "",
    val selectedGapOption: String? = null,
    val selectedQuizOption: String? = null,
    val currentFeedback: AiCorrectionResult? = null,
    val isEvaluating: Boolean = false,
    val totalCorrectCount: Int = 0,
    val totalMistakesCount: Int = 0,
    val isSceneFinished: Boolean = false,
    // Progressive Hint State
    val contractionMistakeCount: Int = 0,
    val translationMistakeCount: Int = 0,
    val activeHint: ProgressiveHint? = null,
    val isHintVisible: Boolean = false,
    val isRetryMode: Boolean = false
) {
    val currentPhrase: ScenePhrase?
        get() = scene?.phrases?.getOrNull(currentPhraseIndex)

    val totalPhrases: Int
        get() = scene?.phrases?.size ?: 0

    val progressRatio: Float
        get() {
            if (totalPhrases == 0) return 0f
            val base = currentPhraseIndex.toFloat() / totalPhrases.toFloat()
            val stepFraction = when (currentStepType) {
                ExerciseStepType.CONTRACTION_INPUT, ExerciseStepType.CONTRACTION_FEEDBACK -> 0.25f
                ExerciseStepType.TRANSLATION_INPUT, ExerciseStepType.TRANSLATION_FEEDBACK -> 0.50f
                ExerciseStepType.GAP_FILL_INPUT, ExerciseStepType.GAP_FILL_FEEDBACK -> 0.75f
                ExerciseStepType.QUIZ_INPUT, ExerciseStepType.QUIZ_FEEDBACK -> 1.0f
                ExerciseStepType.COMPLETED -> 1.0f
            }
            return (base + (stepFraction / totalPhrases.toFloat())).coerceIn(0f, 1f)
        }
}

class ExerciseViewModel(
    private val sceneId: String,
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExerciseUiState())
    val uiState: StateFlow<ExerciseUiState> = _uiState.asStateFlow()

    init {
        loadScene()
    }

    private fun loadScene() {
        val scene = repository.getSceneById(sceneId)
        _uiState.update { it.copy(scene = scene) }
    }

    fun onContractionInputChanged(text: String) {
        _uiState.update { it.copy(contractionInputText = text) }
    }

    fun appendContractionToken(token: String) {
        val current = _uiState.value.contractionInputText
        val newText = if (current.isBlank() || current.endsWith(" ")) {
            "$current$token "
        } else {
            "$current $token "
        }
        _uiState.update { it.copy(contractionInputText = newText) }
    }

    fun onTranslationInputChanged(text: String) {
        _uiState.update { it.copy(translationInputText = text) }
    }

    fun onSelectGapOption(option: String) {
        _uiState.update { it.copy(selectedGapOption = option) }
    }

    fun onSelectQuizOption(option: String) {
        _uiState.update { it.copy(selectedQuizOption = option) }
    }

    fun requestHint() {
        val phrase = _uiState.value.currentPhrase ?: return
        val isTranslation = _uiState.value.currentStepType in listOf(
            ExerciseStepType.TRANSLATION_INPUT,
            ExerciseStepType.TRANSLATION_FEEDBACK
        )
        val currentCount = if (isTranslation) _uiState.value.translationMistakeCount else _uiState.value.contractionMistakeCount
        val nextLevel = ((_uiState.value.activeHint?.level ?: currentCount) % 3) + 1

        val hint = IntelligentCorrectionEngine.getProgressiveHint(phrase, nextLevel, isTranslation)
        _uiState.update { it.copy(activeHint = hint, isHintVisible = true) }
    }

    fun applyPartialHint() {
        val hint = _uiState.value.activeHint?.partialAnswer ?: return
        val isTranslation = _uiState.value.currentStepType in listOf(
            ExerciseStepType.TRANSLATION_INPUT,
            ExerciseStepType.TRANSLATION_FEEDBACK
        )
        val cleanHint = hint.replace("...", " ").trim()
        if (isTranslation) {
            _uiState.update { it.copy(translationInputText = cleanHint) }
        } else {
            _uiState.update { it.copy(contractionInputText = cleanHint) }
        }
    }

    fun retryStepWithHint() {
        // Return from feedback to input mode, keeping hint active for learning
        _uiState.update {
            val step = when (it.currentStepType) {
                ExerciseStepType.CONTRACTION_FEEDBACK -> ExerciseStepType.CONTRACTION_INPUT
                ExerciseStepType.TRANSLATION_FEEDBACK -> ExerciseStepType.TRANSLATION_INPUT
                ExerciseStepType.GAP_FILL_FEEDBACK -> ExerciseStepType.GAP_FILL_INPUT
                ExerciseStepType.QUIZ_FEEDBACK -> ExerciseStepType.QUIZ_INPUT
                else -> it.currentStepType
            }
            it.copy(
                currentStepType = step,
                currentFeedback = null,
                isRetryMode = true,
                isHintVisible = true
            )
        }
    }

    fun submitContraction() {
        val phrase = _uiState.value.currentPhrase ?: return
        val userInput = _uiState.value.contractionInputText.trim()
        if (userInput.isBlank()) return

        _uiState.update { it.copy(isEvaluating = true) }

        viewModelScope.launch {
            val (result, structureTag) = repository.aiService.evaluateContractionWithAi(userInput, phrase)

            if (!result.isCorrect) {
                val newMistakeCount = _uiState.value.contractionMistakeCount + 1
                val hint = IntelligentCorrectionEngine.getProgressiveHint(
                    phrase = phrase,
                    attemptNumber = newMistakeCount,
                    isTranslationStep = false
                )

                // Track mistake adaptively
                val primaryPair = phrase.contractionsUsed.firstOrNull()
                val tag = structureTag ?: "${primaryPair?.fullForm ?: "Contração"} → ${primaryPair?.contractedForm ?: ""}"
                repository.recordStructureMistake(
                    tag = tag,
                    fullForm = primaryPair?.fullForm ?: phrase.fullForm,
                    contractedForm = primaryPair?.contractedForm ?: phrase.naturalForm,
                    category = "CONTRACTIONS",
                    sampleSentence = phrase.naturalForm,
                    sampleTranslation = phrase.portugueseTranslation,
                    tip = phrase.grammarTip
                )

                _uiState.update {
                    it.copy(
                        isEvaluating = false,
                        currentFeedback = result,
                        currentStepType = ExerciseStepType.CONTRACTION_FEEDBACK,
                        totalMistakesCount = it.totalMistakesCount + 1,
                        contractionMistakeCount = newMistakeCount,
                        activeHint = hint,
                        isHintVisible = true
                    )
                }
            } else {
                // Track success adaptively
                structureTag?.let { repository.recordStructureSuccess(it) }

                _uiState.update {
                    it.copy(
                        isEvaluating = false,
                        currentFeedback = result,
                        currentStepType = ExerciseStepType.CONTRACTION_FEEDBACK,
                        totalCorrectCount = it.totalCorrectCount + 1,
                        isHintVisible = false
                    )
                }
            }
        }
    }

    fun submitTranslation() {
        val phrase = _uiState.value.currentPhrase ?: return
        val userInput = _uiState.value.translationInputText.trim()
        if (userInput.isBlank()) return

        _uiState.update { it.copy(isEvaluating = true) }

        viewModelScope.launch {
            val result = repository.aiService.evaluateTranslationWithAi(userInput, phrase)

            // Record phrase attempt in database
            repository.recordPhraseAttempt(
                sceneId = sceneId,
                phraseId = phrase.id,
                fullSentence = phrase.fullForm,
                naturalSentence = phrase.naturalForm,
                userTranslation = userInput,
                isCorrect = result.isCorrect
            )

            if (!result.isCorrect) {
                val newMistakeCount = _uiState.value.translationMistakeCount + 1
                val hint = IntelligentCorrectionEngine.getProgressiveHint(
                    phrase = phrase,
                    attemptNumber = newMistakeCount,
                    isTranslationStep = true
                )

                _uiState.update {
                    it.copy(
                        isEvaluating = false,
                        currentFeedback = result,
                        currentStepType = ExerciseStepType.TRANSLATION_FEEDBACK,
                        totalMistakesCount = it.totalMistakesCount + 1,
                        translationMistakeCount = newMistakeCount,
                        activeHint = hint,
                        isHintVisible = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isEvaluating = false,
                        currentFeedback = result,
                        currentStepType = ExerciseStepType.TRANSLATION_FEEDBACK,
                        totalCorrectCount = it.totalCorrectCount + 1,
                        isHintVisible = false
                    )
                }
            }
        }
    }

    fun submitGapFill() {
        val phrase = _uiState.value.currentPhrase ?: return
        val selected = _uiState.value.selectedGapOption ?: return
        val isCorrect = selected.trim().equals(phrase.blankCorrectAnswer.trim(), ignoreCase = true)

        val result = AiCorrectionResult(
            isCorrect = isCorrect,
            feedbackTitle = if (isCorrect) "Completou Corretamente! 🎉" else "Opção Incorreta",
            feedbackMessage = if (isCorrect)
                "Você selecionou a forma contraída correta que completa o diálogo naturalmente."
            else
                "A forma correta para preencher a lacuna é \"${phrase.blankCorrectAnswer}\".",
            suggestedImprovement = "Frase completa: \"${phrase.blankSentence.replace("______", phrase.blankCorrectAnswer)}\"",
            grammarExplanation = phrase.grammarTip,
            additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
            scorePercentage = if (isCorrect) 100 else 0
        )

        _uiState.update {
            it.copy(
                currentFeedback = result,
                currentStepType = ExerciseStepType.GAP_FILL_FEEDBACK,
                totalCorrectCount = it.totalCorrectCount + if (isCorrect) 1 else 0,
                totalMistakesCount = it.totalMistakesCount + if (!isCorrect) 1 else 0
            )
        }
    }

    fun submitQuiz() {
        val phrase = _uiState.value.currentPhrase ?: return
        val selected = _uiState.value.selectedQuizOption ?: return
        val isCorrect = selected.trim().equals(phrase.quizCorrectAnswer.trim(), ignoreCase = true)

        val result = AiCorrectionResult(
            isCorrect = isCorrect,
            feedbackTitle = if (isCorrect) "Excelente Compreensão! 🎬" else "Vamos revisar o sentido",
            feedbackMessage = if (isCorrect)
                "Você compreendeu perfeitamente o tom e significado da expressão cinematográfica!"
            else
                "A resposta correta é: \"${phrase.quizCorrectAnswer}\".",
            suggestedImprovement = phrase.quizExplanation,
            grammarExplanation = phrase.grammarTip,
            additionalExample = "${phrase.additionalExample} (${phrase.additionalExampleTranslation})",
            scorePercentage = if (isCorrect) 100 else 0
        )

        _uiState.update {
            it.copy(
                currentFeedback = result,
                currentStepType = ExerciseStepType.QUIZ_FEEDBACK,
                totalCorrectCount = it.totalCorrectCount + if (isCorrect) 1 else 0,
                totalMistakesCount = it.totalMistakesCount + if (!isCorrect) 1 else 0
            )
        }
    }

    fun advanceStep() {
        val state = _uiState.value
        val scene = state.scene ?: return

        when (state.currentStepType) {
            ExerciseStepType.CONTRACTION_FEEDBACK -> {
                _uiState.update {
                    it.copy(
                        currentStepType = ExerciseStepType.TRANSLATION_INPUT,
                        currentFeedback = null,
                        translationInputText = "",
                        activeHint = null,
                        isHintVisible = false,
                        isRetryMode = false
                    )
                }
            }
            ExerciseStepType.TRANSLATION_FEEDBACK -> {
                _uiState.update {
                    it.copy(
                        currentStepType = ExerciseStepType.GAP_FILL_INPUT,
                        currentFeedback = null,
                        selectedGapOption = null,
                        activeHint = null,
                        isHintVisible = false,
                        isRetryMode = false
                    )
                }
            }
            ExerciseStepType.GAP_FILL_FEEDBACK -> {
                _uiState.update {
                    it.copy(
                        currentStepType = ExerciseStepType.QUIZ_INPUT,
                        currentFeedback = null,
                        selectedQuizOption = null,
                        activeHint = null,
                        isHintVisible = false,
                        isRetryMode = false
                    )
                }
            }
            ExerciseStepType.QUIZ_FEEDBACK -> {
                // Completed one full phrase!
                val nextPhraseIndex = state.currentPhraseIndex + 1
                if (nextPhraseIndex < scene.phrases.size) {
                    _uiState.update {
                        it.copy(
                            currentPhraseIndex = nextPhraseIndex,
                            currentStepType = ExerciseStepType.CONTRACTION_INPUT,
                            currentFeedback = null,
                            contractionInputText = "",
                            translationInputText = "",
                            selectedGapOption = null,
                            selectedQuizOption = null,
                            contractionMistakeCount = 0,
                            translationMistakeCount = 0,
                            activeHint = null,
                            isHintVisible = false,
                            isRetryMode = false
                        )
                    }
                } else {
                    // Completed whole scene!
                    viewModelScope.launch {
                        repository.saveSceneProgress(
                            sceneId = scene.id,
                            levelCode = scene.level.code,
                            completedPhrases = scene.phrases.size,
                            totalPhrases = scene.phrases.size
                        )
                    }
                    _uiState.update {
                        it.copy(
                            currentStepType = ExerciseStepType.COMPLETED,
                            isSceneFinished = true,
                            currentFeedback = null
                        )
                    }
                }
            }
            else -> {}
        }
    }

    class Factory(
        private val sceneId: String,
        private val repository: AppRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ExerciseViewModel(sceneId, repository) as T
        }
    }
}
