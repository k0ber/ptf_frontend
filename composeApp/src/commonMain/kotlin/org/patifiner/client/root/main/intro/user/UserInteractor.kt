package org.patifiner.client.root.main.intro.user

import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import org.patifiner.client.core.UserDto
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.main.data.UpdateUserRequest
import org.patifiner.client.root.main.data.UserStorage

class UserInteractor(
    private val authRepository: AuthRepository,
    private val userStorage: UserStorage
) {
    suspend fun loadProfile(): Result<UserDto> = authRepository.loadProfile()

    fun logout(): Result<Unit> = authRepository.logout()

    suspend fun uploadAvatar(file: PlatformFile): Result<UserDto> {
        // todo: check file extension and size
        return authRepository.uploadAvatar(file.readBytes(), file.name)
    }

    suspend fun updateProfile(request: UpdateUserRequest): Result<UserDto> =
        authRepository.updateProfile(request)

    suspend fun deletePhoto(url: String): Result<UserDto> = authRepository.deletePhoto(url)

    suspend fun getDraft(): UserDto? = userStorage.getDraft()

    fun getDraftSync(): UserDto? = userStorage.getDraftSync()

    suspend fun saveDraft(user: UserDto) = userStorage.saveDraft(user)

}
