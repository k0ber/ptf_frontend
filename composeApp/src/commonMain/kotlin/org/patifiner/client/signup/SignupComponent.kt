package org.patifiner.client.signup

import com.arkivanov.decompose.ComponentContext
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.common.toUserMessage
import org.patifiner.client.signup.ui.SignupUiState

sealed interface SignupEvent {
    data class Error(val message: String) : SignupEvent
}

class SignupComponent(
    componentContext: ComponentContext,
    private val createUser: suspend (SignupRequest) -> Result<Unit>,
    private val navigateBackToLogin: () -> Unit,
    private val navigateToProfile: () -> Unit
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val _state = MutableStateFlow(SignupUiState())
    private val _events = MutableSharedFlow<SignupEvent>()

    val state: StateFlow<SignupUiState> = _state.asStateFlow()
    val events: SharedFlow<SignupEvent> = _events.asSharedFlow()

    // todo: rework screen
    fun onName(v: String) = _state.update { it.copy(name = v) }
    fun onSurname(v: String) = _state.update { it.copy(surname = v) }
    fun onYear(v: String) = _state.update { it.copy(year = v.filter { c -> c.isDigit() }.take(4)) }
    fun onMonth(v: String) = _state.update { it.copy(month = v.filter { c -> c.isDigit() }.take(2)) }
    fun onDay(v: String) = _state.update { it.copy(day = v.filter { c -> c.isDigit() }.take(2)) }
    fun onEmail(v: String) = _state.update { it.copy(email = v) }
    fun onPassword(v: String) = _state.update { it.copy(password = v) }
    fun onConfirm(v: String) = _state.update { it.copy(confirm = v) }

    fun onBack() = navigateBackToLogin()

    fun onSignup() {
        val state = _state.value
        if (!state.canSubmit || state.loading) return
        _state.update { it.copy(loading = true) }
        val req = state.toRequest()

        scope.launch {
            Napier.d { "SignupComponent -> createUser called" }
            createUser(state.toRequest())
                .onSuccess {
                    Napier.d { "SignupComponent -> success" }
                    navigateToProfile()
                }
                .onFailure { e ->
                    Napier.e { "SignupComponent -> failed: $e" }
                    _events.emit(SignupEvent.Error(e.toUserMessage("Registration failed")))
                }
            _state.update { it.copy(loading = false) }
        }
    }
}