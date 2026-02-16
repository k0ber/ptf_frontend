package org.patifiner.client.login.data

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow
import org.patifiner.client.base.get
import org.patifiner.client.base.post
import org.patifiner.client.login.RefreshTokenRequest
import org.patifiner.client.login.TokenRequest
import org.patifiner.client.login.TokenResponse
import org.patifiner.client.login.UserDto
import org.patifiner.client.signup.SignupRequest


class AuthRepository(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage,
) {
    val tokenFlow: StateFlow<String?> = tokenStorage.tokenFlow

    suspend fun requestToken(loginRequest: TokenRequest): Result<Unit> = runCatching {
        val response: TokenResponse = client.post("/user/login", loginRequest, false)
        tokenStorage.accessToken = response.accessToken
    }

    suspend fun refreshTokens(): Result<TokenResponse> = runCatching {
        val currentRefresh = tokenStorage.refreshToken ?: throw Exception("No refresh token")
        val response: TokenResponse = client.post("/user/refresh", RefreshTokenRequest(currentRefresh), false)

        tokenStorage.accessToken = response.accessToken
        tokenStorage.refreshToken = response.refreshToken
        response
    }

    suspend fun loadProfile(): Result<UserDto> = runCatching { client.get("/user/me") }

    suspend fun signup(req: SignupRequest): Result<Unit> = runCatching {
        val response: TokenResponse = client.post("/user/create", req, false)
        tokenStorage.accessToken = response.accessToken
        tokenStorage.refreshToken = response.refreshToken
    }

    fun logout() {
        tokenStorage.clear()
    }
}
