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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false) // for keyboard ?

        val componentContext = defaultComponentContext()
        val rootComponent = RootComponent(componentContext = componentContext)

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
