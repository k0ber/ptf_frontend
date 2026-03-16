package org.patifiner.client.root

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.main.SESSION_SCOPE

class RootNavigator(
    authRepository: AuthRepository,
    appScope: CoroutineScope,
) : KoinComponent {

    val backStack = mutableStateListOf<PtfRoute>()
        .apply {
            val hasToken = authRepository.tokenFlow.value != null
            if (hasToken) {
                openSession()
                add(PtfRoute.Main)
            } else {
                add(PtfRoute.Login)
            }
        }

    private var authScope: Scope? = null

    init {
        authRepository.tokenFlow.drop(1) // first read on backStack init
            .onEach { token ->
                val currentRoot = backStack.lastOrNull()
                if (token != null && currentRoot !is PtfRoute.Main) {
                    openSession()
                    backStack.clear()
                    backStack.add(PtfRoute.Main)
                } else if (token == null && currentRoot !is PtfRoute.Login) {
                    closeSession()
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

    private fun openSession() {
        if (authScope == null) {
            authScope = getKoin().createScope(
                "session_id",
                named(SESSION_SCOPE)
            )
        }
    }

    private fun closeSession() {
        authScope?.close()
        authScope = null
    }
}
