package org.patifiner.client

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.retainedComponent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val app = application as PatifinerApplication
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        splashScreen.setKeepOnScreenCondition { !app.ready.value }

        setContent {
            val start = System.currentTimeMillis()
//            val rootComponent = RootComponent(DefaultComponentContext(LifecycleRegistry()))
            // todo: you aren't need retainedComponent - it's heavy (about 40ms on emulator)
            val rootComponent = retainedComponent { componentContext ->
                RootComponent(componentContext = componentContext)
            }
            Log.w("PTF", "rootComponent init took: ${System.currentTimeMillis() - start}")

            RootScreen(rootComponent)
        }
    }
}
