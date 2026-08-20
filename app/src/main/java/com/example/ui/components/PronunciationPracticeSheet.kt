package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.PronunciationEngine
import com.example.audio.SpeechRecognitionHelper
import com.example.audio.SpeechRecognitionState
import com.example.audio.TtsManager
import com.example.data.model.PronunciationAnalysisResult
import com.example.data.model.WordPronunciationDetail
import com.example.data.model.WordPronunciationStatus
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaCyan
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.CinemaSurfaceLight
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RoseError
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PronunciationPracticeSheet(
    targetPhrase: String,
    portugueseTranslation: String,
    ttsManager: TtsManager,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val speechHelper = remember { SpeechRecognitionHelper(context) }
    val speechState by speechHelper.state.collectAsStateWithLifecycle()

    var analysisResult by remember { mutableStateOf<PronunciationAnalysisResult?>(null) }
    var showManualInput by remember { mutableStateOf(false) }
    var manualText by remember { mutableStateOf("") }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
        if (isGranted) {
            speechHelper.startListening()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechHelper.stopListening()
        }
    }

    // Process speech recognition result
    LaunchedEffect(speechState) {
        when (val state = speechState) {
            is SpeechRecognitionState.Success -> {
                val result = PronunciationEngine.analyzePronunciation(
                    targetPhrase = targetPhrase,
                    spokenPhrase = state.recognizedText,
                    recognitionConfidence = state.confidence
                )
                analysisResult = result
            }
            else -> {}
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Pulsing animation for microphone when listening
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianBg,
        dragHandle = null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with title and close button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CinemaAmber.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = CinemaAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Treino de Pronúncia",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "Fale naturalmente em inglês",
                            color = CinemaCyan,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_pronunciation_sheet")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Target Phrase Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
                border = BorderStroke(1.dp, CinemaBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FRASE ALVO",
                            color = CinemaAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        AudioPlayButton(
                            onPlay = { ttsManager.speak(targetPhrase) },
                            size = 36
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"$targetPhrase\"",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = portugueseTranslation,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Result Display when user has spoken
            if (analysisResult != null) {
                val res = analysisResult!!

                // Score Gauge & Feedback Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pronunciation_result_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CinemaSurface),
                    border = BorderStroke(
                        1.dp,
                        if (res.scorePercentage >= 80) EmeraldSuccess.copy(alpha = 0.6f)
                        else if (res.scorePercentage >= 60) CinemaAmber.copy(alpha = 0.6f)
                        else RoseError.copy(alpha = 0.6f)
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "RESULTADO DO TREINO",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Pronúncia: ${res.scorePercentage}%",
                                    color = if (res.scorePercentage >= 80) EmeraldSuccess
                                    else if (res.scorePercentage >= 60) CinemaAmber
                                    else Color(0xFFFF6B6B),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Surface(
                                color = if (res.scorePercentage >= 80) EmeraldSuccess.copy(alpha = 0.15f)
                                else if (res.scorePercentage >= 60) CinemaAmber.copy(alpha = 0.15f)
                                else RoseError.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (res.scorePercentage >= 80) "Excelente ✨"
                                    else if (res.scorePercentage >= 60) "Bom / Compreensível 👍"
                                    else "Precisa Praticar 🎯",
                                    color = if (res.scorePercentage >= 80) EmeraldSuccess
                                    else if (res.scorePercentage >= 60) CinemaAmber
                                    else Color(0xFFFF6B6B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { res.scorePercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (res.scorePercentage >= 80) EmeraldSuccess
                            else if (res.scorePercentage >= 60) CinemaAmber
                            else Color(0xFFFF6B6B),
                            trackColor = Color.White.copy(alpha = 0.08f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Spoken text transcription
                        Text(
                            text = "O que ouvimos você falar:",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (res.spokenPhrase.isNotBlank()) "\"${res.spokenPhrase}\"" else "(nenhuma palavra reconhecida)",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Word-by-word visual badges
                        Text(
                            text = "Análise Palavra por Palavra:",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            res.words.forEach { wordDetail ->
                                WordStatusBadge(
                                    detail = wordDetail,
                                    onPlayWord = { ttsManager.speak(wordDetail.word) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Detailed specific feedback
                        if (res.omittedWords.isNotEmpty() || res.mispronouncedWords.isNotEmpty()) {
                            Surface(
                                color = CinemaSurfaceLight,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, CinemaBorder)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Palavras para praticar:",
                                        color = CinemaAmber,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    res.omittedWords.forEach { word ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "❌ ", fontSize = 12.sp)
                                            Text(
                                                text = word,
                                                color = Color(0xFFFF6B6B),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "— Você não pronunciou esta palavra.",
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }

                                    res.mispronouncedWords.forEach { (target, spoken) ->
                                        Row(
                                            modifier = Modifier.padding(vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "⚠️ ", fontSize = 12.sp)
                                            Text(
                                                text = target,
                                                color = CinemaAmber,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "— Ouvido como \"$spoken\". Pratique a pronúncia correta.",
                                                color = TextSecondary,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Pedagogy message & rhythm tip
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CinemaCyan,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = res.feedbackMessage,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = res.rhythmAndIntonationTip,
                                    color = CinemaCyan,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Speech Recognition Status & Microphone Trigger
            val isListening = speechState is SpeechRecognitionState.Listening
            val isProcessing = speechState is SpeechRecognitionState.Processing

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Speech State Label
                when (val state = speechState) {
                    is SpeechRecognitionState.Listening -> {
                        Text(
                            text = "🎙️ Ouvindo você... Fale agora!",
                            color = CinemaAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    is SpeechRecognitionState.Processing -> {
                        Text(
                            text = "Analisando sua pronúncia...",
                            color = CinemaCyan,
                            fontSize = 14.sp
                        )
                    }
                    is SpeechRecognitionState.Error -> {
                        Text(
                            text = state.errorMessage,
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    else -> {
                        if (analysisResult == null) {
                            Text(
                                text = "Toque no microfone abaixo e fale a frase em voz alta",
                                color = TextMuted,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Microphone Big Action Button
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .scale(if (isListening) pulseScale else 1.0f)
                        .clip(CircleShape)
                        .background(if (isListening) RoseError else CinemaAmber)
                        .clickable {
                            if (isListening) {
                                speechHelper.stopListening()
                            } else {
                                if (hasAudioPermission) {
                                    speechHelper.startListening()
                                } else {
                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            }
                        }
                        .testTag("record_speech_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isListening) Icons.Default.Mic else Icons.Default.Mic,
                        contentDescription = "Falar frase",
                        tint = if (isListening) Color.White else Color.Black,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (isListening) "Toque para parar" else if (analysisResult != null) "Tentar Novamente (🎤)" else "Falar Frase",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action row: listen correct vs manual simulation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { ttsManager.speak(targetPhrase) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaCyan),
                        border = BorderStroke(1.dp, CinemaBorder),
                        modifier = Modifier.testTag("pronunciation_listen_model_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Escutar Modelo", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = { showManualInput = !showManualInput },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = BorderStroke(1.dp, CinemaBorder)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (showManualInput) "Ocultar Digitação" else "Testar por Texto", fontSize = 12.sp)
                    }
                }

                // Optional Manual Input Drawer for emulator testing
                AnimatedVisibility(visible = showManualInput) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = manualText,
                            onValueChange = { manualText = it },
                            placeholder = { Text("Ex: I'm going to the...", color = TextMuted, fontSize = 13.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("manual_speech_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CinemaSurfaceLight,
                                unfocusedContainerColor = CinemaSurfaceLight,
                                focusedBorderColor = CinemaAmber,
                                unfocusedBorderColor = CinemaBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (manualText.isNotBlank()) {
                                            speechHelper.submitManualTranscript(manualText)
                                        }
                                    },
                                    modifier = Modifier.testTag("submit_manual_speech_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Avaliar",
                                        tint = CinemaAmber
                                    )
                                }
                            },
                            singleLine = true
                        )
                    }
                }

                if (analysisResult != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("finish_pronunciation_practice_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = CinemaAmber, contentColor = Color.Black),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Continuar Estudo", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun WordStatusBadge(
    detail: WordPronunciationDetail,
    onPlayWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (detail.status) {
        WordPronunciationStatus.CORRECT -> Triple(
            Color(0xFF0F3A2A),
            EmeraldSuccess,
            EmeraldSuccess.copy(alpha = 0.6f)
        )
        WordPronunciationStatus.CLOSE_ENOUGH -> Triple(
            Color(0xFF332900),
            Color(0xFFFFD54F),
            Color(0xFFFFD54F).copy(alpha = 0.5f)
        )
        WordPronunciationStatus.MISPRONOUNCED -> Triple(
            Color(0xFF4A1E00),
            Color(0xFFFF9800),
            Color(0xFFFF9800).copy(alpha = 0.6f)
        )
        WordPronunciationStatus.OMITTED -> Triple(
            Color(0xFF421010),
            Color(0xFFFF6B6B),
            Color(0xFFFF6B6B).copy(alpha = 0.6f)
        )
        WordPronunciationStatus.EXTRA -> Triple(
            Color(0xFF281838),
            Color(0xFFD8B4FE),
            Color(0xFFD8B4FE).copy(alpha = 0.4f)
        )
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onPlayWord() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (detail.status) {
                    WordPronunciationStatus.CORRECT -> "✓"
                    WordPronunciationStatus.CLOSE_ENOUGH -> "~"
                    WordPronunciationStatus.MISPRONOUNCED -> "⚠️"
                    WordPronunciationStatus.OMITTED -> "❌"
                    WordPronunciationStatus.EXTRA -> "+"
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = detail.word,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
