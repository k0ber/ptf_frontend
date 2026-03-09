package org.patifiner.client.login

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.base.BaseState
import org.patifiner.client.base.Constants
import org.patifiner.client.base.PtfLog
import org.patifiner.client.base.TokenRequest
import org.patifiner.client.base.createDefault
import org.patifiner.client.base.execute
import org.patifiner.client.login.data.AuthRepository

interface LoginStore : Store<LoginIntent, LoginState, LoginLabel>

@Immutable
data class LoginState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val password: String = "",
    val isPasswordError: Boolean = false,
    override val isLoading: Boolean = false
) : BaseState<LoginState> {
    override fun withLoading(isLoading: Boolean): LoginState = copy(isLoading = isLoading)
}

sealed interface LoginLabel {
    data class Error(val message: String) : LoginLabel
    data object FocusOnPassword : LoginLabel
}

sealed interface LoginIntent {
    data class ChangeEmail(val email: String) : LoginIntent
    data class ChangePassword(val password: String) : LoginIntent
    data object Login : LoginIntent
}

class LoginStoreFactory(
    private val factory: StoreFactory,
    private val loginUseCase: LoginUseCase
) {
    fun create(): LoginStore =
        object : LoginStore, Store<LoginIntent, LoginState, LoginLabel>
        by factory.createDefault(
            initialState = LoginState(),
            executorFactory = coroutineExecutorFactory {

                onIntent<LoginIntent.ChangeEmail> { intent ->
                    dispatch {
                        PtfLog.d { "ChangeEmail: ${intent.email}" }
                        val isEmailError = !Constants.emailRegex.matches(email)
                        copy(email = intent.email, isEmailError = isEmailError)
                    }
                }

                onIntent<LoginIntent.ChangePassword> { intent ->
                    dispatch {
                        PtfLog.d { "ChangePwf: ${intent.password}" }
                        val isPwdError = intent.password.length >= Constants.MIN_PASS_LNG
                        copy(password = intent.password, isPasswordError = isPwdError)
                    }
                }

                onIntent<LoginIntent.Login> {
                    execute(
                        useCase = { loginUseCase(TokenRequest(state().email, state().password)) },
                        errorFactory = LoginLabel::Error
                    )
                }
            }
        ) {}
}

class LoginUseCase(private val repo: AuthRepository) { // redundant class
    suspend operator fun invoke(tokenRequest: TokenRequest): Result<Unit> =
        repo.requestToken(tokenRequest)
}
