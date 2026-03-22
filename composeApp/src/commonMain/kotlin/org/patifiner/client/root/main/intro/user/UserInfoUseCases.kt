package org.patifiner.client.root.main.intro.user

import org.patifiner.client.core.UserDto
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.signup.UpdateUserRequest

class UpdateProfileUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(request: UpdateUserRequest): Result<UserDto> =
        repo.updateProfile(request)
}

class UploadAvatarUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(bytes: ByteArray, fileName: String): Result<UserDto> =
        repo.uploadAvatar(bytes, fileName)
}
