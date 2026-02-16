package org.patifiner.client.login

import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.signup.SignupRequest

class LoginUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(tokenRequest: TokenRequest): Result<Unit> =
        repo.requestToken(tokenRequest)
}

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

class SignupUseCase(val repo: AuthRepository) {
    suspend operator fun invoke(signupRequest: SignupRequest): Result<Unit> =
        repo.signup(signupRequest)
}
