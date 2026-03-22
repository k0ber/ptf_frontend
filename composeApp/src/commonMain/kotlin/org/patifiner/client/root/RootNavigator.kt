package org.patifiner.client.root

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.patifiner.client.root.login.data.SessionManager

class RootNavigator(
    appScope: CoroutineScope,
    private val sessionManager: SessionManager,
) : KoinComponent {

    val backStack = mutableStateListOf<PtfRoute>()
        .apply {
            val hasToken = sessionManager.accessTokenFlow.value != null
            if (hasToken) {
                sessionManager.openAuthScope()
                add(chooseStartRoute())
            } else {
                add(PtfRoute.Login)
            }
        }

    init {
        sessionManager.accessTokenFlow.drop(1) // first read on backStack init
            .onEach { token ->
                val currentRoot = backStack.lastOrNull()
                if (token != null && currentRoot !is PtfRoute.Main) {
                    backStack.clear()
                    backStack.add(chooseStartRoute())
                } else if (token == null && currentRoot !is PtfRoute.Login) {
                    backStack.clear()
                    backStack.add(PtfRoute.Login)
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

    private fun chooseStartRoute(): PtfRoute =
        if (sessionManager.isIntroRequired) PtfRoute.Intro else PtfRoute.Main

}
