package org.patifiner.client.profile

import com.arkivanov.decompose.ComponentContext
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.profile.ui.ProfileUiState

@Inject
class ProfileComponent(
    @Assisted componentContext: ComponentContext,
    @Assisted("nav_my_topics") private val navMyTopics: () -> Unit,
    @Assisted("nav_add_topic") private val navAddTopic: () -> Unit,
    private val loadProfile: LoadProfileUseCase,
    private val logout: LogoutUseCase,
) : ComponentContext by componentContext {

    @AssistedFactory
    fun interface Factory {
        fun create(
            @Assisted componentContext: ComponentContext,
            @Assisted("nav_my_topics") navMyTopics: () -> Unit,
            @Assisted("nav_add_topic") navAddTopic: () -> Unit
        ): ProfileComponent
    }

    private val scope = componentScope()
    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        refresh()
    }

    fun refresh() {
        if (_state.value.loading) return
        scope.launch {
            _state.update { it.copy(loading = true, error = null) }
            loadProfile()
                .onSuccess { info -> _state.update { it.copy(userInfoDto = info, error = null) } }
                .onFailure { e -> _state.update { it.copy(error = e.message ?: "Failed to load profile") } }
            _state.update { it.copy(loading = false) }
        }
    }

    fun onLogout() = logout()

    fun onNavToAddTopic() = navAddTopic()

    fun onNavToMyTopics() = navMyTopics()
}