package org.patifiner.client.root.signup

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.Constants
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.ScreenStatus.Idle.isLoading
import org.patifiner.client.core.StatusState
import org.patifiner.client.root.main.data.CreateUserRequest

@Immutable
data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    override val status: ScreenStatus = ScreenStatus.Idle,
) : StatusState {

    override fun copyWithStatus(status: ScreenStatus): SignupState = copy(status = status)

    val nameValid get() = name.trim().length >= 2
    val emailValid get() = Regex(Constants.EMAIL_REGEX).matches(email.trim())
    val passwordValid get() = password.length >= 8
    val confirmValid get() = confirm == password && confirm.isNotEmpty()

    val canSubmit get() = nameValid && emailValid && passwordValid && confirmValid && !isLoading

    fun toRequest() = CreateUserRequest(
        name = name.trim(),
        email = email.trim(),
        password = password
    )
}

sealed interface SignupSideEffect {
    data class Error(val message: String) : SignupSideEffect
}
