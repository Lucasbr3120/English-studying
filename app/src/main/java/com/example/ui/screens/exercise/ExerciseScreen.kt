package com.example.ui.screens.exercise

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.TtsManager
import com.example.data.ai.ProgressiveHint
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.ContractionQuickChips
import com.example.ui.components.CorrectionFeedbackCard
import com.example.ui.components.LevelBadge
import com.example.ui.components.PronunciationPracticeSheet
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.CinemaAmberLight
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaCyan
import com.example.ui.theme.CinemaPurple
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.CinemaSurfaceLight
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    viewModel: ExerciseViewModel,
    ttsManager: TtsManager,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scene = state.scene
    val phrase = state.currentPhrase
    var showPronunciationSheet by remember { mutableStateOf(false) }

    if (scene == null || (phrase == null && !state.isSceneFinished)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBg),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CinemaAmber)
        }
        return
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = scene.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (state.isSceneFinished) "Cena Finalizada" else "Frase ${state.currentPhraseIndex + 1} de ${scene.phrases.size}",
                            color = CinemaAmber,
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("exercise_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar treino",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    LevelBadge(level = scene.level, modifier = Modifier.padding(end = 12.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CinemaSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { state.progressRatio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp),
                color = CinemaAmber,
                trackColor = CinemaBorder
            )

            // Main Interactive Content Area
            if (state.isSceneFinished) {
                SceneFinishedView(
                    sceneTitle = scene.title,
                    totalPhrases = scene.phrases.size,
                    correctCount = state.totalCorrectCount,
                    mistakesCount = state.totalMistakesCount,
                    onFinish = onNavigateBack
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        when (state.currentStepType) {
                            ExerciseStepType.CONTRACTION_INPUT, ExerciseStepType.CONTRACTION_FEEDBACK -> {
                                ContractionStepView(
                                    phrase = phrase!!,
                                    characterName = phrase.characterName,
                                    inputText = state.contractionInputText,
                                    activeHint = state.activeHint,
                                    isHintVisible = state.isHintVisible,
                                    onRequestHint = { viewModel.requestHint() },
                                    onApplyPartial = { viewModel.applyPartialHint() },
                                    onTextChanged = { viewModel.onContractionInputChanged(it) },
                                    onInsertToken = { viewModel.appendContractionToken(it) },
                                    onSubmit = { viewModel.submitContraction() },
                                    isEvaluating = state.isEvaluating,
                                    onPlayAudio = { ttsManager.speak(phrase.fullForm) },
                                    onOpenPronunciation = { showPronunciationSheet = true }
                                )
                            }
                            ExerciseStepType.TRANSLATION_INPUT, ExerciseStepType.TRANSLATION_FEEDBACK -> {
                                TranslationStepView(
                                    phrase = phrase!!,
                                    characterName = phrase.characterName,
                                    inputText = state.translationInputText,
                                    activeHint = state.activeHint,
                                    isHintVisible = state.isHintVisible,
                                    onRequestHint = { viewModel.requestHint() },
                                    onApplyPartial = { viewModel.applyPartialHint() },
                                    onTextChanged = { viewModel.onTranslationInputChanged(it) },
                                    onSubmit = { viewModel.submitTranslation() },
                                    isEvaluating = state.isEvaluating,
                                    onPlayAudio = { ttsManager.speak(phrase.naturalForm) },
                                    onOpenPronunciation = { showPronunciationSheet = true }
                                )
                            }
                            ExerciseStepType.GAP_FILL_INPUT, ExerciseStepType.GAP_FILL_FEEDBACK -> {
                                GapFillStepView(
                                    phrase = phrase!!,
                                    selectedOption = state.selectedGapOption,
                                    onSelectOption = { viewModel.onSelectGapOption(it) },
                                    onSubmit = { viewModel.submitGapFill() },
                                    onPlayAudio = { ttsManager.speak(phrase.naturalForm) },
                                    onOpenPronunciation = { showPronunciationSheet = true }
                                )
                            }
                            ExerciseStepType.QUIZ_INPUT, ExerciseStepType.QUIZ_FEEDBACK -> {
                                QuizStepView(
                                    phrase = phrase!!,
                                    selectedOption = state.selectedQuizOption,
                                    onSelectOption = { viewModel.onSelectQuizOption(it) },
                                    onSubmit = { viewModel.submitQuiz() },
                                    onPlayAudio = { ttsManager.speak(phrase.naturalForm) },
                                    onOpenPronunciation = { showPronunciationSheet = true }
                                )
                            }
                            else -> {}
                        }

                        Spacer(modifier = Modifier.height(120.dp))
                    }

                    // Bottom Sheet / Card Feedback Overlay
                    state.currentFeedback?.let { feedback ->
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                        ) {
                            CorrectionFeedbackCard(
                                result = feedback,
                                userAnswer = when (state.currentStepType) {
                                    ExerciseStepType.CONTRACTION_FEEDBACK -> state.contractionInputText
                                    ExerciseStepType.TRANSLATION_FEEDBACK -> state.translationInputText
                                    ExerciseStepType.GAP_FILL_FEEDBACK -> state.selectedGapOption ?: ""
                                    ExerciseStepType.QUIZ_FEEDBACK -> state.selectedQuizOption ?: ""
                                    else -> ""
                                },
                                expectedAnswer = when (state.currentStepType) {
                                    ExerciseStepType.CONTRACTION_FEEDBACK -> phrase?.naturalForm ?: ""
                                    ExerciseStepType.TRANSLATION_FEEDBACK -> phrase?.portugueseTranslation ?: ""
                                    ExerciseStepType.GAP_FILL_FEEDBACK -> phrase?.blankCorrectAnswer ?: ""
                                    ExerciseStepType.QUIZ_FEEDBACK -> phrase?.quizCorrectAnswer ?: ""
                                    else -> ""
                                },
                                onContinue = { viewModel.advanceStep() },
                                onRetry = { viewModel.retryStepWithHint() },
                                activeHint = state.activeHint,
                                onPlayExampleAudio = { textToPlay -> ttsManager.speak(textToPlay) },
                                onOpenPronunciation = { showPronunciationSheet = true }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showPronunciationSheet && phrase != null) {
        PronunciationPracticeSheet(
            targetPhrase = phrase.naturalForm,
            portugueseTranslation = phrase.portugueseTranslation,
            ttsManager = ttsManager,
            onDismiss = { showPronunciationSheet = false }
        )
    }
}

// ----------------- SUB-STEP VIEWS -----------------

@Composable
fun ContractionStepView(
    phrase: com.example.data.model.ScenePhrase,
    characterName: String,
    inputText: String,
    activeHint: ProgressiveHint?,
    isHintVisible: Boolean,
    onRequestHint: () -> Unit,
    onApplyPartial: () -> Unit,
    onTextChanged: (String) -> Unit,
    onInsertToken: (String) -> Unit,
    onSubmit: () -> Unit,
    isEvaluating: Boolean,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Step Banner
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF78350F).copy(alpha = 0.4f),
            border = BorderStroke(1.dp, CinemaAmber)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FlashOn,
                    contentDescription = null,
                    tint = CinemaAmber,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ETAPA 1: TRANSFORMAÇÃO PARA INGLÊS NATURAL",
                    color = CinemaAmberLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // Target Phrase (Full uncontracted form)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
            border = BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Frase Completa (sem contração):",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = onOpenPronunciation,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, CinemaAmber),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaAmber),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("step_falar_frase_button")
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Falar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        AudioPlayButton(onPlay = onPlayAudio, size = 36)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "\"${phrase.fullForm}\"",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Personagem: $characterName",
                    color = CinemaCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Progressive Hint Box (if active)
        if (isHintVisible && activeHint != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF3B2D1D),
                border = BorderStroke(1.dp, CinemaAmber)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = CinemaAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeHint.hintTitle,
                                color = CinemaAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        if (activeHint.partialAnswer != null) {
                            OutlinedButton(
                                onClick = onApplyPartial,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, CinemaAmber),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Usar início 🧩", color = CinemaAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = activeHint.hintText,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transforme para o inglês falado natural:",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "💡 Pedir Dica",
                color = CinemaAmber,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onRequestHint() }
                    .padding(4.dp)
            )
        }

        // Text Input
        OutlinedTextField(
            value = inputText,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("contraction_text_input"),
            placeholder = {
                Text("Digite a frase contraída (ex: I'm, don't, gonna...)", color = TextMuted, fontSize = 14.sp)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CinemaSurfaceLight,
                unfocusedContainerColor = CinemaSurfaceLight,
                focusedBorderColor = CinemaAmber,
                unfocusedBorderColor = CinemaBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            minLines = 2
        )

        // Quick Contraction Chips
        Column {
            Text(
                text = "Atalhos rápidos:",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            ContractionQuickChips(onInsertContraction = onInsertToken)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Submit Button
        Button(
            onClick = onSubmit,
            enabled = inputText.isNotBlank() && !isEvaluating,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_contraction_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = CinemaAmber,
                contentColor = Color.Black,
                disabledContainerColor = CinemaSurfaceLight
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isEvaluating) {
                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    text = "Verificar Contração com IA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun TranslationStepView(
    phrase: com.example.data.model.ScenePhrase,
    characterName: String,
    inputText: String,
    activeHint: ProgressiveHint?,
    isHintVisible: Boolean,
    onRequestHint: () -> Unit,
    onApplyPartial: () -> Unit,
    onTextChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    isEvaluating: Boolean,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Step Banner
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF065F46).copy(alpha = 0.4f),
            border = BorderStroke(1.dp, EmeraldSuccess)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = EmeraldSuccess,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ETAPA 2: TRADUÇÃO CONTEXTUAL",
                    color = Color(0xFF6EE7B7),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        // Natural Spoken Phrase
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
            border = BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Frase falada na cena:",
                        color = CinemaCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = onOpenPronunciation,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, CinemaCyan),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaCyan),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.testTag("translation_falar_frase_button")
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Falar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        AudioPlayButton(onPlay = onPlayAudio, size = 36)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "\"${phrase.naturalForm}\"",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 26.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$characterName em diálogo",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
        }

        // Progressive Translation Hint
        if (isHintVisible && activeHint != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0F3E32),
                border = BorderStroke(1.dp, EmeraldSuccess)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = activeHint.hintTitle,
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        if (activeHint.partialAnswer != null) {
                            OutlinedButton(
                                onClick = onApplyPartial,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, EmeraldSuccess),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Usar início 🧩", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = activeHint.hintText,
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Traduza pelo sentido real no Brasil (não literal):",
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "💡 Pedir Dica",
                color = EmeraldSuccess,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onRequestHint() }
                    .padding(4.dp)
            )
        }

        // Translation Input
        OutlinedTextField(
            value = inputText,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("translation_text_input"),
            placeholder = {
                Text("Digite o significado em português...", color = TextMuted, fontSize = 14.sp)
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CinemaSurfaceLight,
                unfocusedContainerColor = CinemaSurfaceLight,
                focusedBorderColor = EmeraldSuccess,
                unfocusedBorderColor = CinemaBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSubmit,
            enabled = inputText.isNotBlank() && !isEvaluating,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_translation_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = EmeraldSuccess,
                contentColor = Color.Black,
                disabledContainerColor = CinemaSurfaceLight
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (isEvaluating) {
                CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(
                    text = "Avaliar Tradução com IA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun GapFillStepView(
    phrase: com.example.data.model.ScenePhrase,
    selectedOption: String?,
    onSelectOption: (String) -> Unit,
    onSubmit: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF1E1B4B).copy(alpha = 0.5f),
            border = BorderStroke(1.dp, CinemaPurple)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TaskAlt,
                    contentDescription = null,
                    tint = CinemaPurple,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ETAPA 3: COMPLETE A LACUNA",
                    color = Color(0xFFA5B4FC),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
            border = BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "Complete com a forma contraída correta:",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "\"${phrase.blankSentence}\"",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    lineHeight = 26.sp
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            phrase.blankOptions.forEach { option ->
                val isSelected = selectedOption == option
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectOption(option) }
                        .testTag("gap_option_$option"),
                    color = if (isSelected) CinemaPurple.copy(alpha = 0.25f) else CinemaSurfaceLight,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (isSelected) CinemaPurple else CinemaBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            color = if (isSelected) CinemaAmberLight else TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CinemaPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSubmit,
            enabled = selectedOption != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_gap_fill_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = CinemaPurple,
                contentColor = Color.White,
                disabledContainerColor = CinemaSurfaceLight
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Confirmar Resposta",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun QuizStepView(
    phrase: com.example.data.model.ScenePhrase,
    selectedOption: String?,
    onSelectOption: (String) -> Unit,
    onSubmit: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF164E63).copy(alpha = 0.5f),
            border = BorderStroke(1.dp, CinemaCyan)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = CinemaCyan,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ETAPA 4: COMPREENSÃO DA EXPRESSÃO",
                    color = Color(0xFF67E8F9),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
            border = BorderStroke(1.dp, CinemaBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = phrase.quizQuestion,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = CinemaSurfaceLight,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "\"${phrase.naturalForm}\"",
                        color = CinemaAmber,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            phrase.quizOptions.forEach { option ->
                val isSelected = selectedOption == option
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectOption(option) }
                        .testTag("quiz_option_${option.take(10)}"),
                    color = if (isSelected) CinemaCyan.copy(alpha = 0.25f) else CinemaSurfaceLight,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.5.dp,
                        if (isSelected) CinemaCyan else CinemaBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CinemaCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onSubmit,
            enabled = selectedOption != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_quiz_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = CinemaCyan,
                contentColor = Color.Black,
                disabledContainerColor = CinemaSurfaceLight
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Concluir Pergunta",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun SceneFinishedView(
    sceneTitle: String,
    totalPhrases: Int,
    correctCount: Int,
    mistakesCount: Int,
    onFinish: () -> Unit
) {
    val totalAttempts = correctCount + mistakesCount
    val accuracy = if (totalAttempts > 0) ((correctCount.toFloat() / totalAttempts.toFloat()) * 100).toInt() else 100

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(EmeraldSuccess.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = CinemaAmber,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Cena Concluída!",
            color = TextPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Você dominou as contrações e falas naturais de \"$sceneTitle\"!",
            color = TextSecondary,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 16.dp),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Stats summary card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
            border = BorderStroke(1.dp, CinemaBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$totalPhrases", color = TextPrimary, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text(text = "Frases", color = TextMuted, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$accuracy%", color = EmeraldSuccess, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text(text = "Precisão", color = TextMuted, fontSize = 12.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$correctCount", color = CinemaAmber, fontWeight = FontWeight.Black, fontSize = 22.sp)
                    Text(text = "Acertos", color = TextMuted, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("finish_scene_button"),
            colors = ButtonDefaults.buttonColors(containerColor = CinemaAmber, contentColor = Color.Black),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Voltar ao Início",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
