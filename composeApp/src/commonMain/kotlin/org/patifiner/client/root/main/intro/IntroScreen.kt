package org.patifiner.client.root.main.intro

import androidx.compose.runtime.Composable
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
@OptIn(KoinExperimentalAPI::class)
fun IntroScreen(viewModel: IntroViewModel) {
    val navigator = viewModel.navigator

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.pop() },
        entryProvider = koinEntryProvider(),
        // Добавь анимации
    )
}
