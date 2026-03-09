package org.patifiner.client

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import org.patifiner.client.base.PtfLog

fun main() {

    initKoin(
        KoinAppConfig(
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope()
        )
    )

    application {
        val lifecycle = remember { LifecycleRegistry() }
        val windowState = rememberWindowState()
        val root = remember {
            setMainThreadId(Thread.currentThread().threadId())
            RootComponent(DefaultComponentContext(lifecycle))
        }

        LifecycleController(lifecycle, windowState)

        remember {
            lifecycle.resume()
            PtfLog.d { "Patifiner started" }
        }

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Patifiner"
        ) {
            RootScreen(root)
        }
    }
}
