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
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.patifiner.client.Platform.appContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        appContext = this
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val lifecycle = remember { LifecycleRegistry() }
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
            val componentContext = remember { DefaultComponentContext(lifecycle = lifecycle, backHandler = essentyBack) }
            App(componentContext)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val lifecycle = remember { LifecycleRegistry() }
    val back = remember { BackDispatcher() }
    val componentContext = remember { DefaultComponentContext( lifecycle = lifecycle, backHandler = back ) }
    App(componentContext)
}