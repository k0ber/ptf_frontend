package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.patifiner.client.design.PtfPreview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    angleDeg: Float = 38f,
    colors: ImmutableList<Color> = persistentListOf(
        colorScheme.primary.copy(alpha = 0.07f),
        colorScheme.primaryContainer.copy(alpha = 0.14f),
        colorScheme.tertiary.copy(alpha = 0.09f)
    ),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(colorScheme.background)
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
fun GradientBackgroundDarkPreview() {
    PtfPreview {
        GradientBackground {}
    }
}

@Preview
@Composable
fun GradientBackgroundLightPreview() {
    PtfPreview(forceDarkMode = true) {
        GradientBackground {}
    }
}
