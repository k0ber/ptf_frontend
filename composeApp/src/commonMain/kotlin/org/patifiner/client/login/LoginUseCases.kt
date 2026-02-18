package org.patifiner.client.login

import org.patifiner.client.base.UserDto
import org.patifiner.client.login.data.AuthRepository

// todo: разнести

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
