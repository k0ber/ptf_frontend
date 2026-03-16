package org.patifiner.client.root.login

import org.patifiner.client.core.UserDto
import org.patifiner.client.root.login.data.AuthRepository

class LogoutUseCase(private val repo: AuthRepository) {
    operator fun invoke() {
        repo.logout()
    }
}

class LoadProfileUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(): Result<UserDto> {
        // get from database or load ??
        return repo.loadProfile()
    }
}
