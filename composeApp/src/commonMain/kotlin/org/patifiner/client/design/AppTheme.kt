package org.patifiner.client.design

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.views.GradientBackground
import org.patifiner.client.design.views.ptfTypography

@Composable
fun AppTheme(forceDarkMode: Boolean? = null, content: @Composable () -> Unit) {
    val isDark = forceDarkMode ?: isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = if (isDark) darkScheme() else lightScheme(),
        typography = ptfTypography(),
        shapes = AppShapes,
        content = {
            CompositionLocalProvider(LocalSpacing provides Spacing()) {
                GradientBackground {
                    content()
                }
            }
        }
    )
}


private fun lightScheme(): ColorScheme = lightColorScheme(
    // Брендовые
    primary = Color(0xFF6C4CF5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB9A6FF), // светл
    onPrimaryContainer = Color(0xFF1C0062),

    secondary = Color(0xFF5840C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2DEFF),
    onSecondaryContainer = Color(0xFF17004C),

    tertiary = Color(0xFFFF5C8A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD9E2),
    onTertiaryContainer = Color(0xFF370017),

    // Поверхности
    background = Color(0xFFFDF7FD), // нежный светлый розовато-белый
    onBackground = Color(0xFF111113),
    surface = Color(0xFFF9F9FC),
    onSurface = Color(0xFF111113),
    surfaceVariant = Color(0xFFF1F1F6),
    onSurfaceVariant = Color(0xFF44464E),

    // Ошибки и служебные
    error = Color(0xFFFF3B30),
    onError = Color.White,
    outline = Color(0xFF777680),
    inverseSurface = Color(0xFF2E3036),
    inverseOnSurface = Color(0xFFF1F1F1),
    inversePrimary = Color(0xFFB9A6FF),
    scrim = Color(0xFF000000)
)

private fun darkScheme(): ColorScheme = darkColorScheme(
    // Брендовые
    primary = Color(0xFFB9A6FF),
    onPrimary = Color(0xFF2E0A91),
    primaryContainer = Color(0xFF6C4CF5),
    onPrimaryContainer = Color.White,

    secondary = Color(0xFFCEC2FF),
    onSecondary = Color(0xFF241055),
    secondaryContainer = Color(0xFF5840C7),
    onSecondaryContainer = Color.White,

    tertiary = Color(0xFFFFB1C8),
    onTertiary = Color(0xFF5A1E3E),
    tertiaryContainer = Color(0xFFFF5C8A),
    onTertiaryContainer = Color.White,

    // Поверхности
    background = Color(0xFF1B1B23), // лёгкий, не абсолютно чёрный
    onBackground = Color(0xFFEDEDF0),
    surface = Color(0xFF21212A),
    onSurface = Color(0xFFEDEDF0),
    surfaceVariant = Color(0xFF2E2E38),
    onSurfaceVariant = Color(0xFFC4C6D0),

    // Ошибки и служебные
    error = Color(0xFFFF6659),
    onError = Color.Black,
    outline = Color(0xFF8E9099),
    inverseSurface = Color(0xFFEDEDF0),
    inverseOnSurface = Color(0xFF21212A),
    inversePrimary = Color(0xFF6C4CF5),
    scrim = Color(0xFF000000)
)

// ==================================================================================================================
// ==================================================================================================================
private data class Swatch(val name: String, val bg: Color, val fg: Color)

@Composable
fun ThemeColorsGrid(modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    val swatches =
        listOf(
            Swatch("primary", scheme.primary, scheme.onPrimary),
            Swatch("primaryContainer", scheme.primaryContainer, scheme.onPrimaryContainer),

            Swatch("secondary", scheme.secondary, scheme.onSecondary),
            Swatch("secondaryContainer", scheme.secondaryContainer, scheme.onSecondaryContainer),

            Swatch("tertiary", scheme.tertiary, scheme.onTertiary),
            Swatch("tertiaryContainer", scheme.tertiaryContainer, scheme.onTertiaryContainer),

            Swatch("background", scheme.background, scheme.onBackground),
            Swatch("surface", scheme.surface, scheme.onSurface),
            Swatch("surfaceVariant", scheme.surfaceVariant, scheme.onSurfaceVariant),

            Swatch("error", scheme.error, scheme.onError),

            // Добавим несколько служебных/редко-используемых токенов с авто-подбором текста:
            Swatch("outline", MaterialTheme.colorScheme.outline, scheme.outline),
            Swatch("inverseSurface", MaterialTheme.colorScheme.inverseSurface, scheme.inverseSurface),
            Swatch("inversePrimary", MaterialTheme.colorScheme.inversePrimary, scheme.inversePrimary),
        )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(swatches, key = { it.name }) { s ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(s.bg)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(s.name, color = s.fg, maxLines = 2, fontSize = 16.sp)
                Box(
                    Modifier
                        .width(48.dp)
                        .height(24.dp)
                        .background(s.fg)
                )
            }
        }
    }
}

// =============================================================================================
@Preview
@Composable
fun ThemeColorsPreview() {
    Row(Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            AppTheme(forceDarkMode = false) {
                ThemeColorsGrid()
            }
        }
        Column(Modifier.weight(1f)) {
            AppTheme(forceDarkMode = true) {
                ThemeColorsGrid()
            }
        }
    }
}
