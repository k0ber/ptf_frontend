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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    angleDeg: Float = 32f,
    colors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.27f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f)
    ),
    stops: List<Float>? = listOf(0f, 0.55f, 1f),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    val rad = angleDeg * PI.toFloat() / 180f
                    val vx = cos(rad);
                    val vy = sin(rad)
                    val hw = size.width * 0.5f
                    val hh = size.height * 0.5f
                    val start = Offset(hw - vx * hw * 1.2f, hh - vy * hh * 1.2f)
                    val end = Offset(hw + vx * hw * 1.2f, hh + vy * hh * 1.2f)

                    val brush =
                        if (stops != null)
                            Brush.linearGradient(
                                colorStops = stops.zip(colors).toTypedArray(),
                                start = start, end = end
                            )
                        else
                            Brush.linearGradient(
                                colors = colors,
                                start = start, end = end
                            )

                    drawRect(brush = brush)
                }
            },
        content = content
    )
}

// ============================================================================================================
@Preview
@Composable
fun GradientBackgroundLightPreview() {
    AppTheme(forceDarkMode = false) { }
}

@Preview
@Composable
fun GradientBackgroundDarkPreview() {
    AppTheme(forceDarkMode = true) { }
}
