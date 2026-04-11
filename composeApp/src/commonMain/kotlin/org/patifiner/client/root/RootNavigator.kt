package org.patifiner.client.root

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.patifiner.client.core.PtfLog
import org.patifiner.client.root.login.data.SessionManager

class RootNavigator(appScope: CoroutineScope, private val sessionManager: SessionManager) {

    val backStack = mutableStateListOf<PtfRoute>()
        .apply {
            val hasToken = sessionManager.accessTokenFlow.value != null
            if (hasToken) {
                PtfLog.d { "RootNavigator on new token" }
                sessionManager.openAuthScope()
                add(chooseAuthStartRoute())
            } else {
                add(PtfRoute.Login)
            }
        }

    init {
        sessionManager.accessTokenFlow.drop(1) // first read on backStack init
            .onEach { token ->
                if (token == null) {
                    PtfLog.d { "RootNavigator on empty token" }
                    sessionManager.closeSession()
                    backStack.clear()
                    backStack.add(PtfRoute.Login)
                } else {
                    backStack.clear()
                    backStack.add(chooseAuthStartRoute())
                }
            }
            .launchIn(appScope)
    }

    fun navigateTo(route: PtfRoute) {
        backStack.add(route)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }

    fun completeIntro() {
        sessionManager.onIntroCompleted()
        backStack.clear()
        backStack.add(PtfRoute.Main)
    }

    private fun chooseAuthStartRoute(): PtfRoute =
        if (sessionManager.isIntroRequired) PtfRoute.Intro else PtfRoute.Main

}
