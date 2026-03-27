package org.patifiner.client.root.login.data

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.patifiner.client.core.RefreshTokenRequest
import org.patifiner.client.core.TokenRequest
import org.patifiner.client.core.TokenResponse
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.delete
import org.patifiner.client.core.get
import org.patifiner.client.core.post
import org.patifiner.client.core.put
import org.patifiner.client.root.main.data.CreateUserRequest
import org.patifiner.client.root.main.data.DeletePhotoRequest
import org.patifiner.client.root.main.data.UpdateUserRequest
import org.patifiner.client.root.main.data.UserCreatedResponse


class AuthRepository(
    private val unauthClient: HttpClient,
    private val authClient: HttpClient,
    private val sessionManager: SessionManager,
) {
    suspend fun refreshTokens(): Result<TokenResponse> = runCatching {
        val currentRefresh = sessionManager.refreshToken ?: throw Exception("No refresh token")

        val response: TokenResponse = unauthClient.post("/user/refresh", RefreshTokenRequest(currentRefresh), false)

        sessionManager.updateTokens(response.accessToken, response.refreshToken)
        response
    }

    suspend fun login(loginRequest: TokenRequest): Result<Unit> = runCatching {
        val response: TokenResponse = unauthClient.post("/user/login", loginRequest, false)
        sessionManager.startSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            isIntroRequired = false
        )
    }

    fun logout() = runCatching { sessionManager.closeSession() }

    suspend fun signup(req: CreateUserRequest): Result<Unit> = runCatching {
        val response: UserCreatedResponse = unauthClient.post("/user/create", req, false)
        sessionManager.startSession(
            accessToken = response.token.accessToken,
            refreshToken = response.token.refreshToken,
            isIntroRequired = true
        )
    }

    suspend fun loadProfile(): Result<UserDto> = runCatching { authClient.get("/user/me") }

    suspend fun updateProfile(request: UpdateUserRequest): Result<UserDto> = runCatching {
        authClient.put("/user/update", request)
    }

    suspend fun uploadAvatar(imageBytes: ByteArray, fileName: String): Result<UserDto> = runCatching {
        authClient.post(
            "/user/me/photo", MultiPartFormDataContent(
                formData {
                    append("image", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            ))
    }

    suspend fun deletePhoto(url: String): Result<UserDto> = runCatching {
        authClient.delete("/user/me/photo", DeletePhotoRequest(url))
    }
}
