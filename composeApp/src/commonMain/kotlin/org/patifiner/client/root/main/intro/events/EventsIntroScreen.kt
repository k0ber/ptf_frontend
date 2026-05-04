package org.patifiner.client.root.main.intro.events

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfScreenContent
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText

@Composable
fun EventsIntroScreen(viewModel: EventsIntroViewModel) {
    EventsIntroContent(
        onNext = { viewModel.onNext() }
    )
}

@Composable
fun EventsIntroContent(
    onNext: () -> Unit
) {
    PtfScreenContent {
        Spacer(Modifier.weight(1f))

        PtfShadowedText("EVENTS")
        Spacer(Modifier.height(8.dp))
        PtfText("Stay tuned for local and online events")

        Spacer(Modifier.height(32.dp))

        PtfText(
            text = "We will notify you about the most interesting happenings in your city and online, " +
                    "and help to find other people for participation.",
            fontSize = 14,
            modifier = Modifier.height(100.dp) // Placeholder height
        )

        Spacer(Modifier.weight(1.5f))

        PrimaryButton(
            text = "Finish",
            onClick = onNext
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun EventsIntroPreview() {
    EventsIntroContent(onNext = {})
}

@Preview
@Composable
fun EventsIntroPreviewLight() {
    PtfTheme { EventsIntroPreview() }
}

@Preview
@Composable
fun EventsIntroPreviewDark() {
    PtfTheme(forceDarkMode = true) { EventsIntroPreview() }
}
