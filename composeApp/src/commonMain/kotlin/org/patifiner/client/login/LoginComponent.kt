package org.patifiner.client.login

import com.arkivanov.decompose.ComponentContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.common.toUserMessage
import org.patifiner.client.login.ui.LoginScreenEvent
import org.patifiner.client.login.ui.LoginScreenState

class LoginComponent(
    componentContext: ComponentContext,
    private val login: suspend (TokenRequest) -> Result<Unit>,
    private val navToSignup: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val _state = MutableStateFlow(LoginScreenState())
    private val _events = MutableSharedFlow<LoginScreenEvent>()

    val state: StateFlow<LoginScreenState> = _state
    val events: SharedFlow<LoginScreenEvent> = _events

    fun onEmailChange(v: String) = _state.update { it.copy(email = v) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v) }
    fun onSignup() = navToSignup()

    fun onEmailConfirm() = scope.launch { _events.emit(LoginScreenEvent.FocusOnPassword)  }

    fun onPasswordConfirm() {
        val state = _state.value
        if (state.loading) return
        _state.update { it.copy(loading = true) }

        val loginRequest = TokenRequest("john.doe@example.com", "Password123")
//        val loginRequest = TokenRequest(state.email, state.password)
        scope.launch {
            Napier.d { "LoginComponent -> login called" }
            login(loginRequest)
                .onSuccess { Napier.d { "LoginComponent -> login succeed" } }
                .onFailure { e ->
                    Napier.e { "LoginComponent -> login failed: $e" }
                    _events.emit(LoginScreenEvent.Error(e.toUserMessage("Login failed")))
                }

            _state.update { it.copy(loading = false) }
        }
    }

}