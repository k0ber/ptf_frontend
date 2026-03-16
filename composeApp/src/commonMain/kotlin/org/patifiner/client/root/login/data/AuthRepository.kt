package org.patifiner.client.root.login.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow
import org.patifiner.client.core.RefreshTokenRequest
import org.patifiner.client.core.TokenRequest
import org.patifiner.client.core.TokenResponse
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.get
import org.patifiner.client.core.post
import org.patifiner.client.root.signup.CreateUserRequest
import org.patifiner.client.root.signup.UserCreatedResponse


class AuthRepository(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
) {
    val tokenFlow: StateFlow<String?> = tokenStorage.tokenFlow

    suspend fun requestToken(loginRequest: TokenRequest): Result<Unit> = runCatching {
        val response: TokenResponse = client.post("/user/login", loginRequest, false)
        tokenStorage.accessToken = response.accessToken
        tokenStorage.refreshToken = response.refreshToken
    }

    suspend fun refreshTokens(): Result<TokenResponse> = runCatching {
        val currentRefresh = tokenStorage.refreshToken ?: throw Exception("No refresh token")
        val response: TokenResponse = client.post("/user/refresh", RefreshTokenRequest(currentRefresh), false)

        tokenStorage.accessToken = response.accessToken
        tokenStorage.refreshToken = response.refreshToken
        response
    }

    suspend fun loadProfile(): Result<UserDto> = runCatching { client.get("/user/me") }

    suspend fun signup(req: CreateUserRequest): Result<Unit> = runCatching {
        val response: UserCreatedResponse = client.post("/user/create", req, false)
        tokenStorage.accessToken = response.token.accessToken
        tokenStorage.refreshToken = response.token.refreshToken
    }

    fun logout() {
        tokenStorage.clear()
    }
}
