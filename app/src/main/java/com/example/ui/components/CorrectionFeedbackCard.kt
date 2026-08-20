package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.ProgressiveHint
import com.example.data.model.AiCorrectionResult
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaCyan
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.EmeraldSuccessBg
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.RoseCoralBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CorrectionFeedbackCard(
    result: AiCorrectionResult,
    userAnswer: String,
    expectedAnswer: String,
    onContinue: () -> Unit,
    onRetry: (() -> Unit)? = null,
    activeHint: ProgressiveHint? = null,
    onPlayExampleAudio: ((String) -> Unit)? = null,
    onOpenPronunciation: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isCorrect = result.isCorrect
    val statusBg = if (isCorrect) EmeraldSuccessBg.copy(alpha = 0.85f) else RoseCoralBg.copy(alpha = 0.85f)
    val statusBorder = if (isCorrect) EmeraldSuccess else RoseCoral
    val statusText = if (isCorrect) "✅ Correto!" else "❌ Quase!"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .border(1.dp, CinemaBorder, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Status Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(statusBg)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (isCorrect) EmeraldSuccess else RoseCoral),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect) Color(0xFF003822) else Color(0xFF690005),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = statusText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = result.feedbackTitle,
                        color = TextPrimary.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User vs Expected Answer Comparison
            if (userAnswer.isNotBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CinemaSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Sua resposta:",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = userAnswer,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isCorrect) "Forma correta:" else "Forma natural esperada:",
                            color = CinemaAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = expectedAnswer,
                            color = CinemaAmber,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Detailed Diagnostic Explanation
            Text(
                text = result.feedbackMessage,
                color = TextSecondary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            result.suggestedImprovement?.let { suggestion ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = suggestion,
                    color = CinemaAmber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Progressive Hint Box (if available and incorrect)
            if (!isCorrect && activeHint != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF3B2D1D),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaAmber)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeHint.hintText,
                            color = Color.White,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Contraction Explanation (Quais palavras foram contraídas e por quê)
            result.contractionAnalysis?.let { analysis ->
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF4A4458).copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CinemaAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Como as contrações funcionam nesta frase:",
                                color = CinemaAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = analysis,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Grammar & Vocabulary Tip
            result.grammarExplanation?.let { tip ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = EmeraldSuccessBg.copy(alpha = 0.5f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = EmeraldSuccess,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Dica de Pronúncia & Gramática:",
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tip,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Additional Example
            result.additionalExample?.let { example ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CinemaSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Exemplo do cinema:",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = example,
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        onPlayExampleAudio?.let { play ->
                            Spacer(modifier = Modifier.width(8.dp))
                            AudioPlayButton(
                                onPlay = { play(example.substringBefore("(").trim()) },
                                size = 36
                            )
                        }
                    }
                }
            }

            // Pronunciation Training Action Button
            if (onOpenPronunciation != null) {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onOpenPronunciation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("feedback_speak_phrase_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaAmber),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CinemaAmber)
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = CinemaAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🎤 Falar Frase (Treinar Pronúncia)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Action Buttons: If not correct and retry is available, allow Try Again with hint!
            if (!isCorrect && onRetry != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("correction_retry_button"),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CinemaAmber),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CinemaAmber)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tentar com Dica ✍️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onContinue,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("correction_continue_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CinemaSurfaceElevated,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Avançar ➔",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("correction_continue_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CinemaAmber,
                        contentColor = Color(0xFF381E72)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (isCorrect) "Continuar para a próxima etapa →" else "Entendi, vamos continuar →",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
