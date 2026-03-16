package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
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
import org.patifiner.client.design.PtfPreview

/**
 * todo: change params order according to official guideline https://android.googlesource.com/platform/frameworks/support/+/androidx-main/compose/docs/compose-component-api-guidelines.md
 * Required parameters. Parameters that don’t have default values and the user is required to pass the values for those parameters in order to use the components. Coming first, they allow users to set them without using named parameters.
 * modifier: Modifier = Modifier. Modifiers should come as a first optional parameter in a @composable function. It must be named modifier and have a default value of Modifier. There should be only one modifier parameter and it should be applied to the root-most layout in the implementation. See “modifier parameter” section for more information.
 * Optional parameters. Parameters that have default values that will be used if not overridden by the user of the component. Coming after required parameters and a modifier parameter, they do not require the user to make an immediate choice and allow one-by-one override using named parameters.
 * (optional) trailing @Composable lambda representing the main content of the component, usually named content. It can have a default value. Having non-@composable trailing lambda (e.g. onClick) might be misleading as it is a user expectation to have a trailing lambda in a component to be @Composable. For LazyColumn and other DSL-like exceptions, it is ok to have non-@composable lambda since it still represents the main content.
 */

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
        style = typography.headlineLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize.sp,
            letterSpacing = 2.sp,
            color = colorScheme.tertiary,
            shadow = Shadow(
                color = colorScheme.tertiary.copy(alpha = 0.6f),
                offset = Offset(0f, 0f),
                blurRadius = 24f
            )
        )
    )
}

@Composable
fun PtfText(text: String, fontSize: Int = 16, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = fontSize.sp,
            color = colorScheme.secondary
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
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = colorScheme.secondary)) {
                append(text)
                append(" ")
            }
            withStyle(
                style = SpanStyle(
                    color = colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
            ) { append(linkText) }
        },
        style = typography.bodyMedium,
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
        style = typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = fontSize.sp,
            color = colorScheme.outline
        )
    )
}

@Composable
fun PtfAlert(text: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxWidth()
            .height(32.dp)
            .background(colorScheme.errorContainer)
    ) {
        PtfWarningText(text)
    }
}

@Composable
fun PtfWarningText(text: String, fontSize: Int = 14, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            fontSize = fontSize.sp,
            color = colorScheme.error
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
    PtfPreview { TextsPreview() }
}

@Preview
@Composable
fun TextsPreviewDark() {
    PtfPreview(forceDarkMode = true) { TextsPreview() }
}
