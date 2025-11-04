package org.patifiner.client.login.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.patifiner.client.di.LoggedInGraph
import org.patifiner.client.di.binds.BindsCommon
import org.patifiner.client.login.TokenRequest
import org.patifiner.client.login.TokenResponse
import org.patifiner.client.login.UserInfoDto
import org.patifiner.client.signup.SignupRequest


class AuthRepository(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
    private val loggedInGraphFactory: LoggedInGraph.Factory
) {
    var loggedInGraph: LoggedInGraph? = tokenStorage.token?.let { createAuthGraph() }
        private set

    val isLogged: Boolean = tokenStorage.token != null

    val isLoggedFlow: Flow<Boolean> = tokenStorage.tokenFlow.map { it != null }

    suspend fun login(loginRequest: TokenRequest): Result<Unit> = runCatching {
        withContext(Dispatchers.Default) {
            val response: TokenResponse = client.post("/user/login") { setBody(loginRequest) }.body()
            tokenStorage.token = response.token
            loggedInGraph = createAuthGraph()
        }
    }

    fun logout() {
        tokenStorage.clear()
        loggedInGraph = null
    }

    suspend fun loadProfile(): Result<UserInfoDto> = runCatching {
        client.get("/user/me").body()
    }

    suspend fun signup(req: SignupRequest): Result<Unit> = runCatching {
        val response = client.post("/user/create") { setBody(req) }.body<TokenResponse>()
        tokenStorage.token = response.token
    }

    private fun createAuthGraph() = loggedInGraphFactory.createLoggedInGraph(common = object : BindsCommon {})
}
