package org.patifiner.client.signup.ui

import androidx.compose.runtime.Immutable
import org.patifiner.client.base.BaseState
import org.patifiner.client.base.Constants
import org.patifiner.client.signup.CreateUserRequest

@Immutable
data class SignupState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    override val isLoading: Boolean = false,
) : BaseState<SignupState> {

    override fun withLoading(isLoading: Boolean): SignupState = copy(isLoading = isLoading)

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
