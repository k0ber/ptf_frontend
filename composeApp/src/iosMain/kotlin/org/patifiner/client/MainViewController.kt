package org.patifiner.client

import androidx.compose.ui.window.ComposeUIViewController
import org.patifiner.client.root.RootScreen
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {

    initKoin(
        KoinAppConfig(
            isDev = false,
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope(),
        )
    )

    return ComposeUIViewController {
        RootScreen()
    }

}
