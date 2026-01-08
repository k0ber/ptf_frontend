package org.patifiner.client

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    initKoin(
        KoinAppConfig(
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope()
        )
    )

    val lifecycle = LifecycleRegistry()
    val componentContext = DefaultComponentContext(lifecycle = lifecycle)
    lifecycle.resume()

    ComposeViewport(document.body!!) {
        val rootComponent = RootComponent(componentContext = componentContext)
        RootScreen(rootComponent)
    }
}
