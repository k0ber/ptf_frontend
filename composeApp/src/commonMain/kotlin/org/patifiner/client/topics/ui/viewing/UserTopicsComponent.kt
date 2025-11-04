package org.patifiner.client.topics.ui.viewing

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository

@OptIn(FlowPreview::class)
class UserTopicsComponent(
    componentContext: ComponentContext,
    private val repo: TopicsRepository,
    private val navigateToAdd: () -> Unit,
) : ComponentContext by componentContext {

    private val scope = componentScope()
    private val _state = MutableStateFlow(UserTopicsState())
    val state: StateFlow<UserTopicsState> = _state

    private val _events = MutableSharedFlow<UserTopicsEvent>()
    val events = _events.asSharedFlow()

    init {
        load()
    }

    fun load() = scope.launch {
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

    fun onRemoveTopic(id: Long) = scope.launch {
        repo.removeUserTopics(RemoveUserTopicsRequest(listOf(id)))
            .onSuccess { count ->
                _state.update { it.copy(topics = it.topics.filterNot { t -> t.id == id }) }
                _events.emit(UserTopicsEvent.Removed(count))
            }
            .onFailure { e ->
                _events.emit(UserTopicsEvent.Error(e.message ?: "Failed to remove topic"))
            }
    }

    fun onAddClick() { navigateToAdd() }
}