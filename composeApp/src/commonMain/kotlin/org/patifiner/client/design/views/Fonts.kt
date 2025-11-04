package org.patifiner.client.design.views

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import patifinerclient.composeapp.generated.resources.Nunito_Bold
import patifinerclient.composeapp.generated.resources.Nunito_ExtraBold
import patifinerclient.composeapp.generated.resources.Nunito_Light
import patifinerclient.composeapp.generated.resources.Nunito_Medium
import patifinerclient.composeapp.generated.resources.Nunito_Regular
import patifinerclient.composeapp.generated.resources.Nunito_SemiBold
import patifinerclient.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ptfFontFamily() = FontFamily(
    Font(Res.font.Nunito_Light, weight = FontWeight.Light),
    Font(Res.font.Nunito_Regular, weight = FontWeight.Normal),
    Font(Res.font.Nunito_Medium, weight = FontWeight.Medium),
    Font(Res.font.Nunito_SemiBold, weight = FontWeight.SemiBold),
    Font(Res.font.Nunito_Bold, weight = FontWeight.Bold),
    Font(Res.font.Nunito_ExtraBold, weight = FontWeight.ExtraBold)
)

@Composable
fun ptfTypography() = Typography().run {
    val fontFamily = ptfFontFamily()
    copy(
        displayLarge = displayLarge.copy(fontFamily = fontFamily),
        displayMedium = displayMedium.copy(fontFamily = fontFamily),
        displaySmall = displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = titleLarge.copy(fontFamily = fontFamily),
        titleMedium = titleMedium.copy(fontFamily = fontFamily),
        titleSmall = titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = bodyLarge.copy(fontFamily =  fontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = bodySmall.copy(fontFamily = fontFamily),
        labelLarge = labelLarge.copy(fontFamily = fontFamily),
        labelMedium = labelMedium.copy(fontFamily = fontFamily),
        labelSmall = labelSmall.copy(fontFamily = fontFamily)
    )
}