package org.patifiner.client

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initKoin(
        KoinAppConfig(
            engine = Platform.engineFactory(),
            apiConfig = Platform.apiConfig(),
            appScope = Platform.appMainScope()
        )
    )

    val lifecycle = LifecycleRegistry()
    val rootComponent = RootComponent(DefaultComponentContext(lifecycle))

    return ComposeUIViewController {
        RootScreen(rootComponent)
    }
}
