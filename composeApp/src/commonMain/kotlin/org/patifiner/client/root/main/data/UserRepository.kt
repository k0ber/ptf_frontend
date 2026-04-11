package org.patifiner.client.root.main.data

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.delete
import org.patifiner.client.core.get
import org.patifiner.client.core.post
import org.patifiner.client.core.put

class UserRepository(
    private val authClient: HttpClient,
) {
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
