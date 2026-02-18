package org.patifiner.client.design.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.patifiner.client.design.PtfTheme

@Composable
fun PtfIntro(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PtfShadowedText("PATIFINER")
        Spacer(modifier = Modifier.height(8.dp))
        PtfText("Let’s find something interesting..")
    }
}

@Composable
fun PtfShadowedText(text: String, fontSize: Int = 32, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize.sp,
            letterSpacing = 2.sp,
            color = MaterialTheme.colorScheme.primary,
            shadow = Shadow(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                offset = Offset(0f, 0f),   // смещение (0,0) = ровное свечение
                blurRadius = 24f           // радиус размытия
            )
        )
    )
}

@Composable
fun PtfText(text: String, fontSize: Int = 16, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    )
}

@Composable
fun PtfLinkHint(
    text: String,
    linkText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            append(text)
            append(" ")
        }
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(linkText)
        }
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    )
}

@Composable
fun PtfInputExampleText(text: String, fontSize: Int = 14, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
fun PtfWarningText(text: String, fontSize: Int = 14, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.error
        )
    )
}

// ===============================================================================================
@Composable
fun TextsPreview() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
    ) {
        PtfIntro()
        Spacer(modifier = Modifier.height(42.dp))
        PtfWarningText("Warning !")
        PtfText("Regular text. Just text")
        PtfShadowedText("Shadowed Text")
        PtfInputExampleText("Input example...")
        PtfLinkHint(text = "Don't have an account?", linkText = "Sign up", onClick = {})
    }
}

@Preview
@Composable
fun TextPreviewLight() {
    PtfTheme { TextsPreview() }
}

@Preview
@Composable
fun TextsPreviewDark() {
    PtfTheme(forceDarkMode = true) { TextsPreview() }
}
