package com.example.ui.screens.pronunciation

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.audio.TtsManager
import com.example.data.local.PrepopulatedPronunciation
import com.example.data.model.PronunciationCategory
import com.example.data.model.PronunciationExerciseItem
import com.example.ui.components.AudioPlayButton
import com.example.ui.components.LevelBadge
import com.example.ui.components.PronunciationPracticeSheet
import com.example.ui.theme.CinemaAmber
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PronunciationScreen(
    ttsManager: TtsManager,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<PronunciationCategory?>(null) }
    var activePracticeItem by remember { mutableStateOf<PronunciationExerciseItem?>(null) }

    val allExercises = remember { PrepopulatedPronunciation.allExercises }
    val filteredExercises = remember(selectedCategory) {
        if (selectedCategory == null) allExercises
        else allExercises.filter { it.category == selectedCategory }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = ObsidianBg,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(CinemaAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = CinemaAmber,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Laboratório de Pronúncia",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                            Text(
                                text = "Foco em comunicação & fluidez real",
                                color = CinemaAmber,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CinemaSurface)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Hero Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CinemaSurfaceElevated),
                    border = BorderStroke(1.dp, CinemaBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CinemaAmber.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "IA RECONHECEDORA DE VOZ",
                                    color = CinemaAmber,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = CinemaCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Fale naturalmente sem medo de errar",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "O sistema avalia fluidez, omissões e conexões reais da fala. Não exigimos sotaque nativo perfeito: o foco é falar e ser compreendido.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Category Filter Chips
            item {
                Text(
                    text = "MÓDULOS DE TREINAMENTO:",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("Todos (${allExercises.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CinemaAmber,
                            selectedLabelColor = Color.Black,
                            containerColor = CinemaSurfaceLight,
                            labelColor = TextPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = selectedCategory == null,
                            borderColor = CinemaBorder,
                            selectedBorderColor = CinemaAmber
                        )
                    )

                    PronunciationCategory.values().forEach { cat ->
                        val count = allExercises.count { it.category == cat }
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = if (isSelected) null else cat },
                            label = { Text("${cat.icon} ${cat.displayName} ($count)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CinemaAmber,
                                selectedLabelColor = Color.Black,
                                containerColor = CinemaSurfaceLight,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = CinemaBorder,
                                selectedBorderColor = CinemaAmber
                            )
                        )
                    }
                }
            }

            // Exercises List
            items(filteredExercises, key = { it.id }) { item ->
                PronunciationExerciseCard(
                    exercise = item,
                    onPlayModelAudio = { ttsManager.speak(item.naturalSpokenForm) },
                    onStartPractice = { activePracticeItem = item }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    activePracticeItem?.let { item ->
        PronunciationPracticeSheet(
            targetPhrase = item.naturalSpokenForm,
            portugueseTranslation = item.portugueseTranslation,
            ttsManager = ttsManager,
            onDismiss = { activePracticeItem = null }
        )
    }
}

@Composable
fun PronunciationExerciseCard(
    exercise: PronunciationExerciseItem,
    onPlayModelAudio: () -> Unit,
    onStartPractice: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onStartPractice() }
            .testTag("pron_card_${exercise.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CinemaSurface),
        border = BorderStroke(1.dp, CinemaBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Category & Level header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${exercise.category.icon} ${exercise.category.displayName.uppercase()}",
                        color = CinemaAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    LevelBadge(level = exercise.difficulty)
                    Spacer(modifier = Modifier.width(8.dp))
                    AudioPlayButton(onPlay = onPlayModelAudio, size = 32)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${exercise.naturalSpokenForm}\"",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = exercise.portugueseTranslation,
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = CinemaSurfaceLight,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Foco: ",
                        color = CinemaCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = exercise.focusConcept,
                        color = TextPrimary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Button: Falar frase
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onStartPractice() }
                    .testTag("start_pron_button_${exercise.id}"),
                color = CinemaAmber.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, CinemaAmber.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = CinemaAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Falar frase & Analisar Pronúncia",
                            color = CinemaAmber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = CinemaAmber,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
