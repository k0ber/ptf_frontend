package org.patifiner.client.design.views

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme


@Composable
fun IndeterminateGradientProgress(
    modifier: Modifier = Modifier,
    height: Dp = 2.dp,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
    headColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
    ),
    barFraction: Float = 0.35f,     // длина «шайбы» (доля ширины)
    durationMillis: Int = 1200
) {
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
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(trackColor)
            .drawWithCache {
                val barW = size.width * barFraction
                val x = (size.width * anim).coerceIn(-barW, size.width)
                val brush = Brush.linearGradient(headColors)
                onDrawBehind {
                    // трек уже залит фоном; рисуем «шайбу»
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

@Preview
@Composable
fun ProgressPreview() {
    AppTheme {
    IndeterminateGradientProgress()
    }
}