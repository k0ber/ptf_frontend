package org.patifiner.client.design.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.PtfTheme

@Composable
fun PtfLinearProgress(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp,
    trackColor: Color = Color.Transparent,
    headColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    ),
    barFraction: Float = 0.33f,     // доля ширины градиента
    durationMillis: Int = 900
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        if (isLoading) {
            val shape = RoundedCornerShape(percent = 50)
            val infinite = rememberInfiniteTransition(label = "indeterminate")
            val anim by infinite.animateFloat(
                initialValue = -barFraction,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = durationMillis, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "offset"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .clip(shape)
                    .background(trackColor)
                    .drawWithCache {
                        val barW = size.width * barFraction
                        val x = (size.width * anim).coerceIn(-barW, size.width)
                        val brush = Brush.linearGradient(headColors)
                        onDrawBehind {
                            drawRoundRect(
                                brush = brush,
                                topLeft = Offset(x, 0f),
                                size = Size(barW, size.height),
                                cornerRadius = CornerRadius(size.height / 2f, size.height / 2f)
                            )
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun ProgressPreview() {
    PtfTheme {
        Column {
            PtfWarningText("Progress is empty in preview")
            PtfLinearProgress(isLoading = true)
        }
    }
}
