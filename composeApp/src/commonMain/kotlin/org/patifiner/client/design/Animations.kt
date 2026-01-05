package org.patifiner.client.design

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface BackableComponent : BackHandlerOwner {
    fun back(): Unit
}

object PtfAnim {

    @OptIn(ExperimentalDecomposeApi::class)
    fun predictiveBack(root: BackableComponent) = predictiveBackAnimation(
        backHandler = root.backHandler,
        onBack = root::back, // stackAnimation(slide() + scale()) // Material-ish
        fallbackAnimation = stackAnimation(slide() + iosParallaxScale) // iOS-ish
    )

    fun <C : Any, T : Any> changingTabs(): StackAnimation<C, T> = stackAnimation(fade())

    private val iosParallaxScale by lazy {
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

}
