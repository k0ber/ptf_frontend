package org.patifiner.client.root.login.data

import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.patifiner.client.root.main.SESSION_SCOPE

class SessionManager(private val tokenStorage: TokenStorage) : KoinComponent {
    val accessTokenFlow: StateFlow<String?> = tokenStorage.tokenFlow
    val isIntroRequired: Boolean get() = tokenStorage.isIntroRequired
    val accessToken: String? get() = tokenStorage.accessToken
    val refreshToken: String? get() = tokenStorage.refreshToken

    private var authScope: Scope? = null

    val scope: Scope
        get() = authScope ?: error("authorization required")

    fun startSession(accessToken: String, refreshToken: String, isIntroRequired: Boolean = false) {
        tokenStorage.accessToken = accessToken
        tokenStorage.refreshToken = refreshToken
        tokenStorage.isIntroRequired = isIntroRequired
        openAuthScope()
    }

    fun openAuthScope() {
        if (authScope == null) {
            authScope = getKoin().createScope("session_id", named(SESSION_SCOPE))
        }
    }

    fun closeSession() {
        authScope?.close()
        authScope = null
        tokenStorage.clear()
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        tokenStorage.accessToken = accessToken
        tokenStorage.refreshToken = refreshToken
    }

    fun onIntroCompleted() { // meh
        tokenStorage.isIntroRequired = false
    }
}
