package org.patifiner.client.design

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.navigation3.scene.Scene

object PtfAnimations {
    const val TAB_ANIM_MS = 250
    const val SCREEN_ANIM_MS = 400

    fun <T : Any> tabPredictiveSpec(): AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform = { _ ->
        fadeIn(tween(TAB_ANIM_MS)) togetherWith fadeOut(tween(TAB_ANIM_MS))
    }

    fun <T : Any> screenPredictiveBackward(): AnimatedContentTransitionScope<Scene<T>>.(Int) -> ContentTransform = { _ ->
        slideInHorizontally(tween(TAB_ANIM_MS)) { -it / 3 } + fadeIn() togetherWith
                slideOutHorizontally(tween(TAB_ANIM_MS)) { it } + fadeOut()
    }

    val tabTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        fadeIn(animationSpec = tween(TAB_ANIM_MS)) togetherWith
                fadeOut(animationSpec = tween(TAB_ANIM_MS))
    }

    val screenForwardSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(SCREEN_ANIM_MS)
        ) + fadeIn() togetherWith slideOutHorizontally(
            targetOffsetX = { -it / 3 },
            animationSpec = tween(SCREEN_ANIM_MS)
        ) + fadeOut()
    }

    val screenBackwardSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        slideInHorizontally(
            initialOffsetX = { -it / 3 },
            animationSpec = tween(SCREEN_ANIM_MS)
        ) + fadeIn() togetherWith slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(SCREEN_ANIM_MS)
        ) + fadeOut()
    }
}
