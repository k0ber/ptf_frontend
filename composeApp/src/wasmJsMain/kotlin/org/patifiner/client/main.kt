package org.patifiner.client

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import io.github.aakira.napier.Napier
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()

    val rootComponent = try {
        initKoin(
            KoinAppConfig(
                engine = Platform.engineFactory(),
                apiConfig = Platform.apiConfig(),
                appScope = Platform.appMainScope()
            )
        )
        RootComponent(componentContext = DefaultComponentContext(lifecycle = lifecycle))
    } catch (e: Throwable) {
        Napier.e(tag = "WasmMain", throwable = e) { "Koin/RootComponent initialization failed" }
        hideAppLoader()
        null
    }

    val container = document.getElementById("ComposeTarget") as HTMLElement

    ComposeViewport(container) {
        LaunchedEffect(Unit) {
            container.focus()
            hideAppLoader()
        }

        if (rootComponent != null) {
            lifecycle.resume()
            RootScreen(rootComponent)
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun hideAppLoader() {
    js("if (window.hideAppLoader) window.hideAppLoader();")
}