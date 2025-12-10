package org.patifiner.client.design

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.arkivanov.decompose.extensions.compose.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import org.patifiner.client.RootComponent

fun childrenAnimation(root: RootComponent) = predictiveBackAnimation(
    backHandler = root.backHandler,
    onBack = root::pop, // stackAnimation(slide() + scale()) // Material-ish
    fallbackAnimation = stackAnimation(slide() + iosParallaxScale) // iOS-ish
)

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