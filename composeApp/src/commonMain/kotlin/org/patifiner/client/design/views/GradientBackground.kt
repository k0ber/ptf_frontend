package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.PtfTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 *     colors: List<Color> = listOf(
 *         MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
 *         MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.27f),
 *         MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f)
 *     )
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    angleDeg: Float = 38f,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.065f),
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.16f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.055f)
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val brush = Brush.linearGradient(
                    colors = colors,
                    start = calculateStart(size, angleDeg),
                    end = calculateEnd(size, angleDeg)
                )
                onDrawBehind { drawRect(brush = brush) }
            },
        content = content
    )
}

private fun calculateStart(size: Size, angle: Float): Offset {
    val rad = angle * PI.toFloat() / 180f
    val vx = cos(rad)
    val vy = sin(rad)
    return Offset(
        size.width * 0.5f - vx * size.width * 0.65f,
        size.height * 0.5f - vy * size.height * 0.65f
    )
}

private fun calculateEnd(size: Size, angle: Float): Offset {
    val rad = angle * PI.toFloat() / 180f
    val vx = cos(rad)
    val vy = sin(rad)
    return Offset(
        size.width * 0.5f + vx * size.width * 0.65f,
        size.height * 0.5f + vy * size.height * 0.65f
    )
}

// ============================================================================================================
@Preview
@Composable
fun GradientBackgroundLightPreview() {
    PtfTheme(forceDarkMode = false) { }
}

@Preview
@Composable
fun GradientBackgroundDarkPreview() {
    PtfTheme(forceDarkMode = true) { }
}
