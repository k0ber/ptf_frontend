package org.patifiner.client

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

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

    val canvas = document.getElementById("ComposeTarget") as HTMLCanvasElement

    ComposeViewport(canvas) {
        val rootComponent = RootComponent(componentContext = componentContext)

        LaunchedEffect(Unit) {
            hideAppLoader()
        }

        RootScreen(rootComponent)
    }
}

private fun hideAppLoader() {
    js("window.hideAppLoader()")
}
