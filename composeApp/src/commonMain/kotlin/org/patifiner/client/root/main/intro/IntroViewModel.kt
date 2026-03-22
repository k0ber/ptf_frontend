package org.patifiner.client.root.main.intro

import androidx.lifecycle.ViewModel
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.login.data.AuthRepository

class IntroViewModel(
    val navigator: IntroNavigator,
    private val rootNavigator: RootNavigator,
) : ViewModel() {

    fun nextStep() {
        val current = navigator.backStack.lastOrNull() ?: return
        when (current) {
            is IntroRoute.UserInfo -> navigator.next(IntroRoute.Topics)
            is IntroRoute.Topics -> navigator.next(IntroRoute.Events)
            is IntroRoute.Events -> finishIntro()
        }
    }

    private fun finishIntro() {
        rootNavigator.completeIntro()
    }
}
