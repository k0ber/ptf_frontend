package org.patifiner.client.viewing

import com.arkivanov.decompose.ComponentContext
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
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

@Inject
class UserTopicsComponent(
    @Assisted private val componentContext: ComponentContext,
    @Assisted private val navigateToAdd: () -> Unit,
    private val repo: TopicsRepository,
) : ComponentContext by componentContext {

    @AssistedFactory fun interface Factory {
        fun create(
            componentContext: ComponentContext,
            naviAddTopic: () -> Unit
        ): UserTopicsComponent
    }

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

    fun onAddClick() {
        navigateToAdd()
    }
}