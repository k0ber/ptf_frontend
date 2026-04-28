package org.patifiner.client.root.login

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState

@Immutable
data class LoginState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val password: String = "",
    val isPasswordError: Boolean = false,
    override val status: ScreenStatus = ScreenStatus.Idle
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)

    val isLoginAvailable: Boolean
        get() = email.isNotBlank() &&
                password.isNotBlank() &&
                !isEmailError &&
                !isPasswordError &&
                !status.isLoading
}

sealed interface LoginSideEffect {
    data class Error(val message: String) : LoginSideEffect
}
