package org.patifiner.client.root.main.intro.user

import org.patifiner.client.core.UserDto
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.signup.UpdateUserRequest

class UserInteractor(private val authRepository: AuthRepository) {
    suspend fun updateProfile(request: UpdateUserRequest): Result<UserDto> =
        authRepository.updateProfile(request)

    suspend fun uploadAvatar(bytes: ByteArray, fileName: String): Result<UserDto> =
        authRepository.uploadAvatar(bytes, fileName)
}
