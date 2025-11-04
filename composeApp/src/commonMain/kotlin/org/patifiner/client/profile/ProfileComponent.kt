package org.patifiner.client.profile

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.ScreenSource
import org.patifiner.client.login.UserInfoDto
import org.patifiner.client.profile.ui.ProfileUiState

class ProfileComponent(
    componentContext: ComponentContext,
    private val source: ScreenSource,
    private val loadProfile: suspend () -> Result<UserInfoDto>,
    private val logout: () -> Unit,
    private val addNewTopic: () -> Unit,
    private val showMyTopics: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        .also { lifecycle.doOnDestroy { it.cancel() } }

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        refresh()
    }

    fun refresh() {
        if (_state.value.loading) return
        scope.launch {
            _state.update { it.copy(loading = true, error = null) }
            val result = loadProfile()
            result.onSuccess { info -> _state.update { it.copy(userInfoDto = info, error = null) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Failed to load profile") } }
            _state.update { it.copy(loading = false) }
        }
    }

    fun onLogout() = logout()

    fun onNavToAddTopic() = addNewTopic()

    fun onNavToMyTopics() = showMyTopics()
}