package org.patifiner.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false) // for keyboard ?

        val componentContext = defaultComponentContext()
        val rootComponent = RootComponent(componentContext = componentContext)

        setContent {
            RootScreen(rootComponent)
        }
    }

}
