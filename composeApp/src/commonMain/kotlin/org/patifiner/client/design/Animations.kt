package org.patifiner.client.design

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV1
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import org.patifiner.client.Os
import org.patifiner.client.Platform

interface BackableComponent : BackHandlerOwner {
    fun back()
}

object PtfAnim {
    private val springSpec: FiniteAnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessMedium,
        dampingRatio = Spring.DampingRatioNoBouncy
    )

    private val mediumTween: FiniteAnimationSpec<Float> = tween(
        durationMillis = 450,
        easing = FastOutSlowInEasing
    )

    private val fastTween: FiniteAnimationSpec<Float> = tween(
        durationMillis = 300,
        easing = FastOutSlowInEasing
    )

    @OptIn(ExperimentalDecomposeApi::class)
    fun provideStackAnimation(root: BackableComponent): StackAnimation<Any, Any> =
        when (Platform.os) {
            Os.ANDROID -> predictiveBackAnimation(
                backHandler = root.backHandler,
                onBack = root::back,
                selector = { initialBackEvent, _, _ ->
                    androidPredictiveBackAnimatableV1(initialBackEvent)
                },
                fallbackAnimation = fallbackMatchingV1()
            )

            Os.WEB -> stackAnimation(fade(mediumTween))
            Os.IOS, Os.DESC -> stackAnimation(slide(springSpec) + fade(springSpec))
        }

    private fun fallbackMatchingV1(): StackAnimation<Any, Any> =
        stackAnimation(
            fade(springSpec) +
                    scale(
                        springSpec,
                        frontFactor = 1.0f,
                        backFactor = 0.9f
                    ) +
                    slide(springSpec)
        )

    fun <C : Any, T : Any> changingTabs(): StackAnimation<C, T> =
        stackAnimation(fade(fastTween))
}
