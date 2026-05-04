package org.patifiner.client.design

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.patifiner.client.design.views.ptfTypography
import org.patifiner.client.root.RootSnackbarHost

@Composable
fun PtfTheme(forceDarkMode: Boolean? = null, content: @Composable () -> Unit) {

    val isDark = forceDarkMode ?: isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (isDark) darkScheme() else lightScheme(),
        typography = ptfTypography(),
        shapes = PtfShapes,
        content = {
            CompositionLocalProvider(
                LocalSpacing provides PtfSpacing(), LocalIndication provides ripple(
                    color = colorScheme.primary.copy(
                        alpha = if (isDark) 0.22f else 0.18f
                    ),
                )
            ) { content() }
        })
}

// region Preview
@Composable
fun PtfPreview(
    forceDarkMode: Boolean? = null,
    content: @Composable () -> Unit
) {
    val hostState = remember { SnackbarHostState() }
    PtfTheme(forceDarkMode = forceDarkMode) {
        CompositionLocalProvider(RootSnackbarHost provides hostState) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorScheme.background,
                content = content
            )
        }
    }
}

private data class Swatch(val name: String, val bg: Color, val fg: Color)

@Composable
fun ThemeColorsGrid(modifier: Modifier = Modifier) {
    val scheme = colorScheme
    val swatches = listOf(
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
        Swatch("outline", colorScheme.outline, scheme.outline),
        Swatch("inverseSurface", colorScheme.inverseSurface, scheme.inverseSurface),
        Swatch("inversePrimary", colorScheme.inversePrimary, scheme.inversePrimary),
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().background(colorScheme.background),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(swatches, key = { it.name }) { swatch ->
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp).background(swatch.bg).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(swatch.name, color = swatch.fg, maxLines = 2, fontSize = 16.sp)
                Box(
                    Modifier.width(48.dp).height(24.dp).background(swatch.fg)
                )
            }
        }
    }
}

@Preview
@Composable
fun ThemeColorsLight() {
    PtfTheme { ThemeColorsGrid() }
}

@Preview
@Composable
fun ThemeColorsDark() {
    PtfTheme(forceDarkMode = true) { ThemeColorsGrid() }
}
// endregion
