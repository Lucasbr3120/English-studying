package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.data.model.CefrLevel
import com.example.ui.theme.CinemaAmber
import com.example.ui.theme.CinemaBorder
import com.example.ui.theme.CinemaCyan
import com.example.ui.theme.CinemaPurple
import com.example.ui.theme.CinemaSurface
import com.example.ui.theme.CinemaSurfaceElevated
import com.example.ui.theme.CinemaSurfaceLight
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoseCoral
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LevelBadge(
    level: CefrLevel,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    prefix: String = ""
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) CinemaAmber else CinemaBorder,
                shape = RoundedCornerShape(8.dp)
            ),
        color = if (isSelected) CinemaAmber else CinemaSurfaceElevated,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = "$prefix${level.code}",
            color = if (isSelected) Color(0xFF381E72) else CinemaAmber,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AudioPlayButton(
    onPlay: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = CinemaAmber,
    size: Int = 44
) {
    Surface(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .clickable { onPlay() }
            .testTag("audio_play_button"),
        color = tint.copy(alpha = 0.15f),
        shape = CircleShape,
        border = androidx.compose.foundation.BorderStroke(1.dp, tint.copy(alpha = 0.35f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Ouvir pronúncia nativa",
                tint = tint,
                modifier = Modifier.size((size * 0.55).dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ContractionQuickChips(
    onInsertContraction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val quickTokens = listOf(
        "I'm", "don't", "can't", "didn't", "won't", "you're", "it's", "they're",
        "we're", "doesn't", "would've", "couldn't", "shouldn't", "gonna", "wanna", "gotta",
        "'ll", "'ve", "'d", "'s", "'re", "n't"
    )

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        quickTokens.forEach { token ->
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onInsertContraction(token) }
                    .testTag("chip_$token"),
                color = CinemaSurfaceElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CinemaBorder)
            ) {
                Text(
                    text = token,
                    color = CinemaAmber,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                )
            }
        }
    }
}
