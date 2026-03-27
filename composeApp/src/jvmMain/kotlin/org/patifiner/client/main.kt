package org.patifiner.client

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.github.vinceglb.filekit.FileKit
import org.patifiner.client.root.RootScreen

fun main() {

    initKoin(
        KoinAppConfig(
            isDev = false,
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope()
        )
    )

    FileKit.init(appId = "MyApplication")

    application {
        val windowState = rememberWindowState()

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "Patifiner"
        ) {
            RootScreen()
        }
    }

}
