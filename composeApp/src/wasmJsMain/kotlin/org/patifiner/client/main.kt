package org.patifiner.client

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.patifiner.client.root.RootScreen

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body ?: return

    initKoin(
        KoinAppConfig(
            isDev = false,
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope()
        )
    )

    hideAppLoader()

    ComposeViewport(body) {
        RootScreen()
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun hideAppLoader() {
    js("if (window.hideAppLoader) window.hideAppLoader();")
}
