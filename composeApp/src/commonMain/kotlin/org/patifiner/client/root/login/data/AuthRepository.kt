package org.patifiner.client.root.login.data

import io.ktor.client.HttpClient
import org.patifiner.client.core.RefreshTokenRequest
import org.patifiner.client.core.TokenRequest
import org.patifiner.client.core.TokenResponse
import org.patifiner.client.core.post
import org.patifiner.client.root.main.data.CreateUserRequest
import org.patifiner.client.root.main.data.UserCreatedResponse


class AuthRepository(
    private val unauthClient: HttpClient,
    private val sessionManager: SessionManager,
) {
    suspend fun refreshTokens(): Result<TokenResponse> = runCatching {
        val currentRefresh = sessionManager.refreshToken ?: throw Exception("No refresh token")

        val response: TokenResponse = unauthClient.post("/user/refresh", RefreshTokenRequest(currentRefresh), false)

        sessionManager.updateTokens(response.accessToken, response.refreshToken)
        response
    }

    suspend fun login(request: TokenRequest): Result<Unit> = runCatching {
        val response: TokenResponse = unauthClient.post("/user/login", request, false)
        sessionManager.startSession(
            userId = request.email,
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            isIntroRequired = false
        )
    }

    suspend fun signup(request: CreateUserRequest): Result<Unit> = runCatching {
        val response: UserCreatedResponse = unauthClient.post("/user/create", request, false)
        sessionManager.startSession(
            userId = request.email,
            accessToken = response.token.accessToken,
            refreshToken = response.token.refreshToken,
            isIntroRequired = true
        )
    }

    fun logout() = runCatching { sessionManager.closeSession() }

}
