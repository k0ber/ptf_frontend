package org.patifiner.client.design

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV1
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.essenty.backhandler.BackEvent
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import org.patifiner.client.Os
import org.patifiner.client.Platform

interface BackableComponent : BackHandlerOwner {
    fun back()
}

object PtfAnim {
    @OptIn(ExperimentalDecomposeApi::class)
    fun provideStackAnimation(root: BackableComponent): StackAnimation<Any, Any> =
        when (Platform.os) {
            Os.ANDROID ->
                // todo: perf test
                stackAnimation(slide(mediumTween) + fade(mediumTween))
//                predictiveBackAnimation(
//                backHandler = root.backHandler,
//                onBack = root::back,
//                selector = { initialBackEvent, _, _ -> androidPredictiveBackAnimatableV1(initialBackEvent) },
//                fallbackAnimation = stackAnimation(slide(mediumTween) + fade(mediumTween))
//            )

            Os.IOS, Os.DESC -> stackAnimation(slide(mediumTween) + fade(mediumTween))
            Os.WEB -> stackAnimation(fade(mediumTween))
        }

    private val mediumTween: FiniteAnimationSpec<Float> = tween(
        durationMillis = 450,
        easing = FastOutSlowInEasing
    )

    private val fastTween: FiniteAnimationSpec<Float> = tween(
        durationMillis = 250,
        easing = FastOutSlowInEasing
    )

    fun <C : Any, T : Any> changingTabs(): StackAnimation<C, T> =
        stackAnimation(fade(fastTween))
}
