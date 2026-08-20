package com.example.ui.screens.youtube

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.TtsManager
import com.example.data.model.AiCorrectionResult
import com.example.data.model.YouTubeStudyPhrase
import com.example.data.model.YouTubeVocabularyItem
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.CorrectionFeedbackCard
import com.example.ui.components.LevelBadge
import com.example.ui.components.PronunciationPracticeSheet
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaBorderHighlight
import com.example.ui.theme.CinemaCyan
import com.example.ui.theme.CinemaPeach
import com.example.ui.theme.CinemaPurple
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.CinemaSurfaceLight
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.EmeraldSuccessBg
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RoseError
import com.example.ui.theme.RoseErrorBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun YouTubeStudyScreen(
    viewModel: YouTubeStudyViewModel,
    ttsManager: TtsManager,
    onNavigateBack: () -> Unit,
    onNavigateToScenes: () -> Unit,
    onNavigateToMistakes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val video = uiState.video
    var showPronunciationSheet by remember { mutableStateOf(false) }
    var currentSpokenPhrase by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = video?.title ?: "Estudo de Vídeo",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1
                        )
                        Text(
                            text = video?.channelTitle ?: "YouTube",
                            color = CinemaCyan,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("youtube_study_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CinemaSurface)
            )
        },
        containerColor = ObsidianBg
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CinemaAmber)
            }
            return@Scaffold
        }

        if (video == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Vídeo não encontrado.", color = TextSecondary)
            }
            return@Scaffold
        }

        val studySet = video.authorizedStudySet
        val phrases = studySet?.phrases.orEmpty()
        val currentPhrase = phrases.getOrNull(uiState.currentPhraseIndex)

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Official YouTube Player
            item {
                YouTubePlayerView(
                    videoId = video.id,
                    modifier = Modifier.testTag("official_youtube_player")
                )
            }

            // 2. Video Badges & Metadata
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LevelBadge(level = video.suggestedLevel, prefix = "Nível sugerido: ")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFF334155))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ClosedCaption,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Legenda Ativa no Player", color = Color(0xFFCBD5E1), fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // 3. If No Authorized Transcripts / Study Set Available
            if (studySet == null || phrases.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
                        border = BorderStroke(1.dp, CinemaBorder)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = CinemaAmber,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Legendas & Direitos de Transcrição",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Este vídeo possui legendas no YouTube, mas não temos acesso autorizado ao texto necessário para gerar os exercícios automaticamente.",
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Você pode assistir ao vídeo normalmente pelo player oficial com legendas nativas, ou escolher uma das opções abaixo para praticar exercícios:",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onNavigateBack,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, CinemaAmber)
                                ) {
                                    Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Outro Vídeo", fontSize = 12.sp, color = CinemaAmber)
                                }
                                Button(
                                    onClick = onNavigateToScenes,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = CinemaAmber, contentColor = Color(0xFF1E1B4B))
                                ) {
                                    Icon(imageVector = Icons.Default.TheaterComedy, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cenas do App", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
                // 4. Study Progress Bar
                item {
                    val progress = (uiState.currentPhraseIndex.toFloat() + 1f) / phrases.size.toFloat()
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = CinemaSurfaceElevated,
                        border = BorderStroke(1.dp, CinemaBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Frase ${uiState.currentPhraseIndex + 1} de ${phrases.size}",
                                    color = CinemaAmber,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = when (uiState.currentStep) {
                                        YouTubeStudyStep.OVERVIEW -> "Visão Geral"
                                        YouTubeStudyStep.CONTRACTION -> "1/5 Contrações"
                                        YouTubeStudyStep.TRANSLATION -> "2/5 Tradução"
                                        YouTubeStudyStep.SPOKEN_ENGLISH -> "3/5 Inglês Falado"
                                        YouTubeStudyStep.VOCABULARY -> "4/5 Vocabulário"
                                        YouTubeStudyStep.COMPREHENSION -> "5/5 Compreensão"
                                        YouTubeStudyStep.SUMMARY -> "Concluído 🎉"
                                    },
                                    color = CinemaCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = CinemaAmber,
                                trackColor = CinemaBorder
                            )
                        }
                    }
                }

                // 5. Interactive Exercise Flow
                currentPhrase?.let { phrase ->
                    when (uiState.currentStep) {
                        YouTubeStudyStep.OVERVIEW,
                        YouTubeStudyStep.CONTRACTION -> {
                            item {
                                ContractionExerciseCard(
                                    phrase = phrase,
                                    input = uiState.contractionInput,
                                    evaluation = uiState.contractionEvaluation,
                                    isEvaluating = uiState.isEvaluatingContraction,
                                    onInputChange = { viewModel.onContractionInputChanged(it) },
                                    onEvaluate = {
                                        focusManager.clearFocus()
                                        viewModel.evaluateContraction()
                                    },
                                    onContinue = { viewModel.proceedToTranslation() },
                                    onPlayAudio = { ttsManager.speak(phrase.contractedForm) },
                                    onOpenPronunciation = {
                                        currentSpokenPhrase = phrase.contractedForm
                                        showPronunciationSheet = true
                                    }
                                )
                            }
                        }

                        YouTubeStudyStep.TRANSLATION -> {
                            item {
                                TranslationExerciseCard(
                                    phrase = phrase,
                                    input = uiState.translationInput,
                                    evaluation = uiState.translationEvaluation,
                                    isEvaluating = uiState.isEvaluatingTranslation,
                                    onInputChange = { viewModel.onTranslationInputChanged(it) },
                                    onEvaluate = {
                                        focusManager.clearFocus()
                                        viewModel.evaluateTranslation()
                                    },
                                    onContinue = { viewModel.proceedToSpokenEnglish() },
                                    onPlayAudio = { ttsManager.speak(phrase.contractedForm) },
                                    onOpenPronunciation = {
                                        currentSpokenPhrase = phrase.contractedForm
                                        showPronunciationSheet = true
                                    }
                                )
                            }
                        }

                        YouTubeStudyStep.SPOKEN_ENGLISH -> {
                            item {
                                SpokenEnglishCard(
                                    phrase = phrase,
                                    ttsManager = ttsManager,
                                    onContinue = { viewModel.proceedToVocabulary() },
                                    onOpenPronunciation = {
                                        currentSpokenPhrase = phrase.informalSpokenForm ?: phrase.contractedForm
                                        showPronunciationSheet = true
                                    }
                                )
                            }
                        }

                        YouTubeStudyStep.VOCABULARY -> {
                            item {
                                VocabularyCard(
                                    phrase = phrase,
                                    ttsManager = ttsManager,
                                    onContinue = { viewModel.proceedToComprehension() }
                                )
                            }
                        }

                        YouTubeStudyStep.COMPREHENSION -> {
                            item {
                                ComprehensionQuizCard(
                                    phrase = phrase,
                                    selectedOption = uiState.selectedQuizOption,
                                    isAnswered = uiState.isQuizAnswered,
                                    isCorrect = uiState.isQuizCorrect,
                                    onSelectOption = { viewModel.onSelectQuizOption(it) },
                                    onNext = { viewModel.nextPhrase() }
                                )
                            }
                        }

                        YouTubeStudyStep.SUMMARY -> {
                            item {
                                YouTubeStudySummaryCard(
                                    totalPhrases = phrases.size,
                                    onRestart = { viewModel.restartStudy() },
                                    onMistakes = onNavigateToMistakes,
                                    onExploreMore = onNavigateBack
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showPronunciationSheet) {
        PronunciationPracticeSheet(
            targetPhrase = currentSpokenPhrase,
            portugueseTranslation = "Prática de fala da frase do YouTube",
            ttsManager = ttsManager,
            onDismiss = { showPronunciationSheet = false }
        )
    }
}

@Composable
fun ContractionExerciseCard(
    phrase: YouTubeStudyPhrase,
    input: String,
    evaluation: AiCorrectionResult?,
    isEvaluating: Boolean,
    onInputChange: (String) -> Unit,
    onEvaluate: () -> Unit,
    onContinue: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("youtube_contraction_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CinemaAmber, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Transforme para a forma natural",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AudioPlayButton(onPlay = onPlayAudio, size = 36)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CinemaSurfaceElevated,
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Forma Completa (Formal):", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${phrase.fullForm}\"",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("youtube_contraction_input"),
                placeholder = { Text("Escreva a frase com as contrações naturais...", color = TextMuted, fontSize = 13.sp) },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (input.isNotBlank() && !isEvaluating) onEvaluate() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CinemaAmber,
                    unfocusedBorderColor = CinemaBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = CinemaAmber
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenPronunciation,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CinemaAmber),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaAmber),
                    modifier = Modifier.testTag("youtube_speak_button")
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Falar Frase", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        if (evaluation?.isCorrect == true) {
                            onContinue()
                        } else {
                            onEvaluate()
                        }
                    },
                    enabled = input.isNotBlank() && !isEvaluating,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("youtube_submit_contraction_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (evaluation?.isCorrect == true) EmeraldSuccess else CinemaAmber,
                        contentColor = Color(0xFF1E1B4B)
                    )
                ) {
                    if (isEvaluating) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else if (evaluation?.isCorrect == true) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Avançar para Tradução", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Text("Avaliar Contração", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Evaluation Feedback Card
            evaluation?.let { eval ->
                Spacer(modifier = Modifier.height(14.dp))
                CorrectionFeedbackCard(
                    result = eval,
                    userAnswer = input,
                    expectedAnswer = phrase.contractedForm,
                    onContinue = onContinue,
                    onRetry = onEvaluate,
                    onOpenPronunciation = onOpenPronunciation
                )
            }
        }
    }
}

@Composable
fun TranslationExerciseCard(
    phrase: YouTubeStudyPhrase,
    input: String,
    evaluation: AiCorrectionResult?,
    isEvaluating: Boolean,
    onInputChange: (String) -> Unit,
    onEvaluate: () -> Unit,
    onContinue: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenPronunciation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("youtube_translation_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = CinemaCyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Traduza para português",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                AudioPlayButton(onPlay = onPlayAudio, size = 36)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = CinemaSurfaceElevated,
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Inglês Falado:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "\"${phrase.contractedForm}\"",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("youtube_translation_input"),
                placeholder = { Text("Escreva o significado em português (avaliação semântica)...", color = TextMuted, fontSize = 13.sp) },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (input.isNotBlank() && !isEvaluating) onEvaluate() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CinemaCyan,
                    unfocusedBorderColor = CinemaBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = CinemaCyan
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (evaluation?.isCorrect == true) {
                        onContinue()
                    } else {
                        onEvaluate()
                    }
                },
                enabled = input.isNotBlank() && !isEvaluating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("youtube_submit_translation_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (evaluation?.isCorrect == true) EmeraldSuccess else CinemaCyan,
                    contentColor = Color(0xFF003822)
                )
            ) {
                if (isEvaluating) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else if (evaluation?.isCorrect == true) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Avançar para Inglês Falado", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Text("Avaliar Tradução com IA", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            evaluation?.let { eval ->
                Spacer(modifier = Modifier.height(14.dp))
                CorrectionFeedbackCard(
                    result = eval,
                    userAnswer = input,
                    expectedAnswer = phrase.portugueseTranslation,
                    onContinue = onContinue,
                    onRetry = onEvaluate,
                    onOpenPronunciation = onOpenPronunciation
                )
            }
        }
    }
}

@Composable
fun SpokenEnglishCard(
    phrase: YouTubeStudyPhrase,
    ttsManager: TtsManager,
    onContinue: () -> Unit,
    onOpenPronunciation: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("youtube_spoken_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.School, contentDescription = null, tint = CinemaPeach, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Inglês Falado & Contrações",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contractions Breakdown
            Text(text = "Contrações Utilizadas:", color = TextMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            phrase.contractionsList.forEach { contraction ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E1B4B),
                    border = BorderStroke(1.dp, Color(0xFF4338CA))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${contraction.fullForm}  →  ${contraction.contractedForm}",
                                color = Color(0xFFA5B4FC),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(text = contraction.ruleExplanation, color = Color(0xFFC7D2FE), fontSize = 11.sp)
                        }
                        AudioPlayButton(onPlay = { ttsManager.speak(contraction.contractedForm) }, size = 32)
                    }
                }
            }

            // Informal Reductions (gonna / wanna / gotta)
            phrase.informalSpokenForm?.let { informal ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Forma Informal & Reduções na Fala Rápida:", color = WarningAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF3B1E08),
                    border = BorderStroke(1.dp, WarningAmber.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "\"$informal\"",
                                color = WarningAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            AudioPlayButton(onPlay = { ttsManager.speak(informal) }, size = 32)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💡 'going to' → gonna, 'want to' → wanna, 'got to' → gotta são contrações informais extremamente comuns em conversas e vídeos.",
                            color = Color(0xFFFFEDD5),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Pedagogical Grammar Note
            Spacer(modifier = Modifier.height(12.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = CinemaSurfaceElevated,
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Explicação da Estrutura:", color = CinemaCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = phrase.grammarExplanation, color = TextSecondary, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenPronunciation,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, CinemaAmber),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaAmber)
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Falar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("continue_to_vocabulary_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CinemaAmber, contentColor = Color(0xFF1E1B4B))
                ) {
                    Text("Ver Vocabulário", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun VocabularyCard(
    phrase: YouTubeStudyPhrase,
    ttsManager: TtsManager,
    onContinue: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("youtube_vocabulary_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = CinemaCyan, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Vocabulário & Expressões",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            phrase.vocabularyNotes.forEach { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = CinemaSurfaceElevated,
                    border = BorderStroke(1.dp, CinemaBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = item.word,
                                    color = CinemaAmber,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                if (item.isInformal) {
                                    Surface(
                                        color = Color(0xFF451A03),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, WarningAmber)
                                    ) {
                                        Text(
                                            text = "Informal",
                                            color = WarningAmber,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        color = Color(0xFF064E3B),
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, EmeraldSuccess)
                                    ) {
                                        Text(
                                            text = "Formal",
                                            color = EmeraldSuccess,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            AudioPlayButton(onPlay = { ttsManager.speak(item.word) }, size = 30)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Tradução: ${item.translation}", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text(text = "Significado: ${item.meaning}", color = TextSecondary, fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = CinemaSurfaceLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Exemplo: \"${item.example}\"",
                                color = CinemaCyan,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("continue_to_comprehension_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CinemaAmber, contentColor = Color(0xFF1E1B4B))
            ) {
                Text("Testar Compreensão", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun ComprehensionQuizCard(
    phrase: YouTubeStudyPhrase,
    selectedOption: String?,
    isAnswered: Boolean,
    isCorrect: Boolean,
    onSelectOption: (String) -> Unit,
    onNext: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("youtube_comprehension_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorderHighlight)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.QuestionAnswer, contentDescription = null, tint = CinemaPurple, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Compreensão do Diálogo",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = CinemaSurfaceElevated,
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Text(
                    text = phrase.comprehensionQuestion,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            phrase.comprehensionOptions.forEach { option ->
                val isSelected = selectedOption == option
                val isTarget = option.trim().equals(phrase.comprehensionCorrectAnswer.trim(), ignoreCase = true)

                val (containerColor, borderColor, textColor) = when {
                    !isAnswered -> if (isSelected) Triple(CinemaAmber.copy(alpha = 0.2f), CinemaAmber, TextPrimary) else Triple(CinemaSurfaceLight, CinemaBorder, TextSecondary)
                    isTarget -> Triple(EmeraldSuccessBg, EmeraldSuccess, Color(0xFFD8FCE8))
                    isSelected && !isCorrect -> Triple(RoseErrorBg, RoseError, Color(0xFFFFDAD6))
                    else -> Triple(CinemaSurfaceLight.copy(alpha = 0.5f), CinemaBorder, TextMuted)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isAnswered) { onSelectOption(option) },
                    shape = RoundedCornerShape(10.dp),
                    color = containerColor,
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            color = textColor,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected || isTarget) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        if (isAnswered && isTarget) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            if (isAnswered) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("youtube_next_phrase_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCorrect) EmeraldSuccess else CinemaAmber,
                        contentColor = Color(0xFF1E1B4B)
                    )
                ) {
                    Text("Avançar para Próxima Atividade", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun YouTubeStudySummaryCard(
    totalPhrases: Int,
    onRestart: () -> Unit,
    onMistakes: () -> Unit,
    onExploreMore: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, EmeraldSuccess)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = EmeraldSuccess.copy(alpha = 0.2f),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "🎉 Vídeo Concluído com Sucesso!",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Você praticou $totalPhrases frases completas, absorveu contrações do inglês falado, vocabulário contextual e teste de compreensão.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onMistakes,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaSurfaceElevated,
                    contentColor = CinemaAmber
                ),
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Icon(imageVector = Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Revisar Meus Erros & Estruturas", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onExploreMore,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaAmber,
                    contentColor = Color(0xFF1E1B4B)
                )
            ) {
                Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Pesquisar Outro Vídeo no YouTube", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
