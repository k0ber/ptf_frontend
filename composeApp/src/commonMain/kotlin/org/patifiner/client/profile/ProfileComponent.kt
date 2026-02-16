package org.patifiner.client.profile

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.scope.Scope
import org.patifiner.client.base.componentScope
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.profile.ui.ProfileUiState

// todo: rework with bootstraper
class ProfileComponent(
    componentContext: ComponentContext,
    private val navMyTopics: () -> Unit,
    private val navAddTopic: () -> Unit,
    private val koinScope: Scope
) : ComponentContext by componentContext {

    private val loadProfile: LoadProfileUseCase = koinScope.get()
    private val logout: LogoutUseCase = koinScope.get()

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        refresh()
    }

    fun refresh() {
        if (_state.value.loading) return
        componentScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            loadProfile()
                .onSuccess { info -> _state.update { it.copy(userDto = info, error = null) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Failed to load profile") } }
            _state.update { it.copy(loading = false) }
        }
    }

    fun onLogout() = logout()

    fun onNavToAddTopic() = navAddTopic()

    fun onNavToMyTopics() = navMyTopics()
}