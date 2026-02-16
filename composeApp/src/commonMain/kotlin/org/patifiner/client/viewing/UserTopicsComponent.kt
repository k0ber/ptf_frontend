package org.patifiner.client.viewing

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.scope.Scope
import org.patifiner.client.base.componentScope
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository

class UserTopicsComponent(
    componentContext: ComponentContext,
    private val navigateToAdd: () -> Unit,
    private val koinScope: Scope
) : ComponentContext by componentContext, KoinComponent {

    private val repo: TopicsRepository = koinScope.get()

    private val _state = MutableStateFlow(UserTopicsState())
    val state: StateFlow<UserTopicsState> = _state

    private val _events = MutableSharedFlow<UserTopicsEvent>()
    val events = _events.asSharedFlow()

    init {
        load()
    }

    fun load() = componentScope.launch {
        _state.update { it.copy(loading = true) }
        repo.getUserTopics()
            .onSuccess { list ->
                _state.update { it.copy(loading = false, topics = list) }
            }
            .onFailure { e ->
                _state.update { it.copy(loading = false) }
                _events.emit(UserTopicsEvent.Error(e.message ?: "Failed to load topics"))
            }
    }

    fun onRemoveTopic(id: Long) = componentScope.launch {
        repo.removeUserTopics(RemoveUserTopicsRequest(listOf(id)))
            .onSuccess { count ->
                _state.update { it.copy(topics = it.topics.filterNot { t -> t.id == id }) }
                _events.emit(UserTopicsEvent.Removed(count))
            }
            .onFailure { e ->
                _events.emit(UserTopicsEvent.Error(e.message ?: "Failed to remove topic"))
            }
    }

    fun onAddClick() {
        navigateToAdd()
    }
}