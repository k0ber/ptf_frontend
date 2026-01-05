package org.patifiner.client.login.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.patifiner.client.NetworkObserver
import org.patifiner.client.login.TokenRequest
import org.patifiner.client.login.TokenResponse
import org.patifiner.client.login.UserInfoDto
import org.patifiner.client.main.MainGraph
import org.patifiner.client.signup.SignupRequest


class AuthRepository(
    private val appScope: CoroutineScope,
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
    private val loggedInGraphFactory: MainGraph.Factory
) {

    val tokenFlow: StateFlow<String?> = tokenStorage.tokenFlow

    suspend fun login(loginRequest: TokenRequest): Result<Unit> = runCatching {
        withContext(Dispatchers.Default) {
            val response: TokenResponse = client.post("/user/login") { setBody(loginRequest) }.body()
            tokenStorage.token = response.token
        }
    }

    fun logout() {
        tokenStorage.clear()
    }

    suspend fun loadProfile(): Result<UserInfoDto> = runCatching {
        client.get("/user/me").body()
    }

    suspend fun signup(req: SignupRequest): Result<Unit> = runCatching {
        val response = client.post("/user/create") { setBody(req) }.body<TokenResponse>()
        tokenStorage.token = response.token
    }
}
