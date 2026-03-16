package org.patifiner.client.design

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


fun lightScheme(): ColorScheme = lightColorScheme(
    // Брендовые
    primary = Color(0xFF6C4CF5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB9A6FF),
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
    background = Color(0xFFFDF7FD),
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

fun darkScheme(): ColorScheme = darkColorScheme(
    // Брендовые
    primary = Color(0xFFB9A6FF),
    onPrimary = Color(0xFF2E0A91),
    primaryContainer = Color(0xFF6C4CF5),
    onPrimaryContainer = Color.White,

    secondary = Color(0xFFCEC2FF),
    onSecondary = Color(0xFF241055),
    secondaryContainer = Color(0xFF5840C7),
    onSecondaryContainer = Color.White,

    tertiary = Color(0xFF00E6C0),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF00665A),
    onTertiaryContainer = Color.White,

    // Поверхности
    background = Color(0xFF1B1B23),
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
