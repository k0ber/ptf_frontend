package org.patifiner.client.root.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.core.Constants
import org.patifiner.client.core.TokenRequest
import org.patifiner.client.core.execute
import org.patifiner.client.root.PtfRoute
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.login.data.AuthRepository

@Stable
class LoginViewModel(
    private val authRepository: AuthRepository,
    private val navigator: RootNavigator,
) : ViewModel(), ContainerHost<LoginState, LoginSideEffect> {

    override val container: Container<LoginState, LoginSideEffect> = container(LoginState()) {}

    fun changeEmail(email: String) = intent {
        val isError = email.isNotEmpty() && !Constants.emailRegex.matches(email)
        reduce { state.copy(email = email, isEmailError = isError) }
    }

    fun changePassword(password: String) = intent {
        val isError = password.isNotEmpty() && password.length < Constants.MIN_PASS_LNG
        reduce { state.copy(password = password, isPasswordError = isError) }
    }

    fun login() = intent {
        execute(
            block = { authRepository.login(TokenRequest(state.email, state.password)) },
            onSuccess = { /* handles by SessionManager */ },
            errorFactory = LoginSideEffect::Error
        )
    }

    fun navigateToSignup() {
        navigator.navigateTo(PtfRoute.Signup)
    }
}
