package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun GradientBackground2(
    modifier: Modifier = Modifier,
    angleDeg: Float = -45f,
    strength: Float = 1f, // общая "интенсивность" эффекта
    content: @Composable BoxScope.() -> Unit
) {
    val c = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    // 🎨 Основные градиентные оттенки
    val colors = listOf(
        if (isDark) c.surface else Color.White.copy(alpha = 0.6f),
        c.primaryContainer.copy(alpha = if (isDark) 0.25f else 0.15f * strength),
        c.secondaryContainer.copy(alpha = if (isDark) 0.20f else 0.10f * strength),
        c.tertiaryContainer.copy(alpha = if (isDark) 0.10f else 0.08f * strength),
    )

    val blurOverlay =
        if (isDark) Color.White.copy(alpha = 0.03f)
        else Color.Black.copy(alpha = 0.02f)

    Box(
        modifier = modifier
            .background(c.surface)
            .fillMaxSize()
            .drawWithCache {
                val rad = angleDeg * PI.toFloat() / 180f
                val vx = cos(rad)
                val vy = sin(rad)
                val hw = size.width * 0.5f
                val hh = size.height * 0.5f
                val start = Offset(hw - vx * hw * 1.2f, hh - vy * hh * 1.2f)
                val end = Offset(hw + vx * hw * 1.2f, hh + vy * hh * 1.2f)

                val brush = Brush.linearGradient(
                    colors = colors,
                    start = start,
                    end = end
                )

                onDrawBehind {
                    drawRect(brush)
                    drawRect(blurOverlay, blendMode = BlendMode.Overlay)
                }
            },
        content = content
    )
}

// ============================================================================================================
@Preview
@Composable
fun GradientBackgroundLightPreview2() {
    AppTheme(forceDarkMode = false) {}
}

@Preview
@Composable
fun GradientBackgroundDarkPreview2() {
    AppTheme(forceDarkMode = true) {}
}
