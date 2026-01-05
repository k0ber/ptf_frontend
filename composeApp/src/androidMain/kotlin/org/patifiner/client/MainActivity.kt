package org.patifiner.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import dev.zacsweers.metro.createGraphFactory
import org.patifiner.client.Platform.appContext
import org.patifiner.client.binds.BindsCommon
import org.patifiner.client.binds.BindsNetwork

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appContext = this
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false) // for keyboard ?
        val componentContext = defaultComponentContext()
        val rootComponent = createGraphFactory<RootGraph.Factory>().create(
            common = object : BindsCommon {},
            network = object : BindsNetwork {},
            networkObserver = Platform.networkObserver(),
            apiConfig = Platform.apiConfig(),
            engine = Platform.engineFactory(),
            settings = Platform.settings(),
            appMainScope = Platform.appMainScope(),
            componentContext = componentContext,
        ).rootComponent()

        setContent {
            SetupBackHandle()
            RootScreen(rootComponent)
        }
    }

    @Composable
    private fun SetupBackHandle() { // возможно ненужно
        val essentyBack = remember { BackDispatcher() }
        val androidBack = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher
        DisposableEffect(essentyBack, androidBack) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!essentyBack.back()) {
                        isEnabled = false
                        androidBack.onBackPressed()
                        isEnabled = true
                    }
                }
            }
            androidBack.addCallback(callback)
            onDispose { callback.remove() }
        }
    }
}
