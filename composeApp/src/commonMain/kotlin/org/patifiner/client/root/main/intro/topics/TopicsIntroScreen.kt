package org.patifiner.client.root.main.intro.topics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.patifiner.client.core.showError
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.views.Chip
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfScreenContent
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.root.RootSnackbarHost

@Composable
fun TopicsIntroScreen(viewModel: TopicsIntroViewModel) {
    val state by viewModel.collectAsState()
    val snackbarHost = RootSnackbarHost.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TopicsIntroSideEffect.Error -> snackbarHost.showError(sideEffect.message)
        }
    }

    TopicsIntroContent(
        state = state,
        onNext = viewModel::onNext,
        onSearchChange = viewModel::changeSearch
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicsIntroContent(
    state: TopicsIntroState,
    onNext: () -> Unit,
    onSearchChange: (String) -> Unit,
//    onTopicClick: (String) -> Unit
) {
    val mockTopics = listOf("Music", "Sport", "Art", "Tech", "Food", "Travel", "Games", "Movies", "Books", "Dance")

    PtfScreenContent {
        PtfLinearProgress(isLoading = state.status.isLoading)

        Spacer(Modifier.weight(1f))

        PtfShadowedText("TOPICS")
        Spacer(Modifier.height(8.dp))
        PtfText("Choose what you like")

        Spacer(Modifier.height(32.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, androidx.compose.ui.Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            mockTopics.forEach { topic ->
                Chip(
                    text = topic,
                    selected = false,
                    onClick = { /* TODO */ }
                )
            }
        }

        Spacer(Modifier.height(32.dp))
        PtfText(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = "Tell us about your unique topics"
        )
        Chip(
            text = "Banana",
            selected = false,
            onClick = { /* TODO */ }
        )
        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = "Next",
            onClick = onNext
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun TopicsIntroPreview() {
    TopicsIntroContent(
        state = TopicsIntroState(),
        onNext = {},
        onSearchChange = {}
    )
}

@Preview
@Composable
fun TopicsIntroPreviewLight() {
    PtfPreview { TopicsIntroPreview() }
}

@Preview
@Composable
fun TopicsIntroPreviewDark() {
    PtfPreview(forceDarkMode = true) { TopicsIntroPreview() }
}
