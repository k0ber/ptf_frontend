package org.patifiner.client.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

suspend fun SnackbarHostState.showError(message: String) = showSnackbar(message = message, duration = SnackbarDuration.Long)

fun ComponentContext.componentScope() = CoroutineScope(
    SupervisorJob() + Dispatchers.Main.immediate // todo: Default mb? avoid switching context to often
).also { lifecycle.doOnDestroy { it.cancel() } }

val iosParallaxScale by lazy {
    stackAnimator { factor, direction, content ->
        // factor:
        //   ENTER_FRONT: 1 -> 0
        //   EXIT_FRONT:  0 -> 1
        //   ENTER_BACK: -1 -> 0
        //   EXIT_BACK:   0 -> -1
        val p = kotlin.math.abs(factor)

        // Чуть-чуть «сжимаем» задний/уходящий экран
        val scale = when (direction) {
            Direction.EXIT_FRONT, Direction.ENTER_BACK -> 1f - 0.05f * p
            else -> 1f
        }

        content(
            Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
        )
    }
}