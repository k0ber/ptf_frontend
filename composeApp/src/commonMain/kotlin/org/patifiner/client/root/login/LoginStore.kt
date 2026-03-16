package org.patifiner.client.root.login

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.Constants
import org.patifiner.client.core.Constants.emailRegex
import org.patifiner.client.core.TokenRequest
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.login.data.AuthRepository

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
    private val repo: AuthRepository
) {
    fun create(): LoginStore =
        object : LoginStore, Store<LoginIntent, LoginState, LoginLabel>
        by factory.createDefault(
            initialState = LoginState(),
            executorFactory = coroutineExecutorFactory {

                onIntent<LoginIntent.ChangeEmail> { intent ->
                    if (state().email == intent.email) return@onIntent

                    dispatch {
                        val isEmailError = intent.email.isNotEmpty() && !emailRegex.matches(intent.email)
                        copy(email = intent.email, isEmailError = isEmailError)
                    }
                }

                onIntent<LoginIntent.ChangePassword> { intent ->
                    dispatch {
                        val isPwdError = intent.password.isNotEmpty() && intent.password.length < Constants.MIN_PASS_LNG
                        copy(password = intent.password, isPasswordError = isPwdError)
                    }
                }

                onIntent<LoginIntent.Login> {
                    execute(
                        useCase = { repo.requestToken(TokenRequest(state().email, state().password)) },
                        errorFactory = LoginLabel::Error
                    )
                }
            }
        ) {}
}
