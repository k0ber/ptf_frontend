package org.patifiner.client.root.login.data

import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.patifiner.client.core.PtfLog
import org.patifiner.client.root.main.SESSION_SCOPE

class SessionManager(private val sessionStorage: SessionStorage) : KoinComponent {

    val accessTokenFlow: StateFlow<String?> = sessionStorage.tokenFlow
    val isIntroRequired: Boolean get() = sessionStorage.isIntroRequired
    val accessToken: String? get() = sessionStorage.accessToken
    val refreshToken: String? get() = sessionStorage.refreshToken

    val userId: String? get() = sessionStorage.userId

    val authScope: Scope?
        get() = userId?.let { getKoin().getScopeOrNull(it) }

    fun startSession(accessToken: String, refreshToken: String, userId: String, isIntroRequired: Boolean = false) {
        PtfLog.d { "Close session before starting new" } // to be sure it's safe
        closeSession()

        PtfLog.d { "Session start for ${accessToken.firstOrNull()}**, intro: $isIntroRequired" }
        sessionStorage.userId = userId
        sessionStorage.accessToken = accessToken
        sessionStorage.refreshToken = refreshToken
        sessionStorage.isIntroRequired = isIntroRequired
        openAuthScope()
    }

    fun openAuthScope() {
        val userId = userId ?: "guest" // ??
        getKoin().createScope(userId, named(SESSION_SCOPE))
        PtfLog.d { "Auth scope created for $userId" }
    }

    fun closeSession() {
        userId?.let { userId ->
            getKoin().getScopeOrNull(userId)?.close()
            PtfLog.d { "Auth scope closed for $userId" }
        }
        sessionStorage.clear()
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        sessionStorage.accessToken = accessToken
        sessionStorage.refreshToken = refreshToken
    }

    fun onIntroCompleted() {
        sessionStorage.isIntroRequired = false
    }
}
