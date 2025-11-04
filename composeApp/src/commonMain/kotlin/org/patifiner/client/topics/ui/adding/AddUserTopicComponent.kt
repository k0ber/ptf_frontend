package org.patifiner.client.topics.ui.adding

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.common.toUserMessage
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.TopicLevel
import org.patifiner.client.topics.data.AddUserTopicRequest
import org.patifiner.client.topics.data.AddUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository


class AddUserTopicComponent(
    componentContext: ComponentContext,
    private val repo: TopicsRepository,
    private val onDone: () -> Unit
) : ComponentContext by componentContext {

    private val scope = componentScope()

    private val _state = MutableStateFlow(AddUserTopicState())
    val state: StateFlow<AddUserTopicState> = _state.asStateFlow()

    private val _events: MutableSharedFlow<AddTopicEvents> = MutableSharedFlow()
    val events = _events.asSharedFlow()

    init {
        scope.launch {
            repo.loadTopicsTree()   // todo loading
                .onSuccess { tree -> _state.update { it.copy(tree = tree) } }
                .onFailure { e ->
                    _state.update { it.copy(error = e.message) } // todo: two types of error
                    _events.emit(AddTopicEvents.Error(e.toUserMessage()))
                }
        }

        scope.launch {
            state.map { state -> state.query }
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) _state.update { it.copy(searchResults = emptyList()) }
                    else repo.searchTopics(query)   // todo loading
                        .onSuccess { list -> _state.update { it.copy(searchResults = list) } }
                        .onFailure { e -> _state.update { it.copy(error = e.message) } }
                }
        }
    }

    fun onQueryChange(value: String) = _state.update { it.copy(query = value) }

    fun onTopicClick(topic: TopicDto) {
        _events.tryEmit(AddTopicEvents.CloseKeyboard)

        val openedTopic = _state.value.openedTopic
        val breadcrumb = _state.value.breadcrumb

        // нажали по уже открытому топику
        if (openedTopic == topic) {
            if (breadcrumb.size <= 1) {
                _state.update { it.copy(openedTopic = null, breadcrumb = emptyList(), /*draft = null,*/ isDraftOpened = false) } // Если это первый уровень — закрываем всё
            } else {
                val parent = breadcrumb.getOrNull(breadcrumb.lastIndex - 1)
                _state.update { it.copy(openedTopic = parent, breadcrumb = breadcrumb.dropLast(1)) }
            }
            return
        }

        // открыли первый уровень в первый раз
        if (openedTopic == null && topic.children.isNotEmpty()) {
            _state.update { it.copy(openedTopic = topic, breadcrumb = listOf(topic)) }
            return
        }

        // раскрываем вглубь или уходим в другую ветку
        if (openedTopic != null && topic.children.isNotEmpty() && topic != breadcrumb.last()) {
            val isNextLevel = openedTopic.children.contains(topic)
            _state.update { it.copy(openedTopic = topic, breadcrumb = if (isNextLevel) breadcrumb + topic else listOf(topic)) }
            return
        }

        // нажали по бредкрамбу
        if (breadcrumb.contains(topic)) {
            val index = breadcrumb.indexOfFirst { it.id == topic.id }
            val newBreadcrumb = if (index == -1) breadcrumb else breadcrumb.take(index + 1)
            _state.update { it.copy(openedTopic = topic, breadcrumb = newBreadcrumb) }
            return
        }

        // нажали по топику без детей
        if (topic.children.isEmpty()) {
            _state.update {
                it.copy(
                    draft = UserTopicDraft(topic = topic, level = TopicLevel.NEWBIE, description = ""),
                    isDraftOpened = true
                )
            }
            _events.tryEmit(AddTopicEvents.CloseKeyboard)
        }
    }

    fun onBreadcrumbLongClick(topicDto: TopicDto) {
        _state.update {
            it.copy(
                draft = UserTopicDraft(topic = topicDto, level = TopicLevel.NEWBIE, description = ""),
                isDraftOpened = true
            )
        }
    }

    fun onDraftDismiss() {
        _state.update { it.copy(isDraftOpened = false, draft = null) }
    }

    fun onDraftConfirm(draft: UserTopicDraft) {
        scope.launch {
            val req = AddUserTopicsRequest(
                topics = listOf(
                    AddUserTopicRequest(
                        topicId = draft.topic.id,
                        level = draft.level,
                        description = draft.description.ifBlank { null }
                    )
                )
            )
            repo.addUserTopics(req)
//                .onSuccess { _state.update {  } } // todo: mark added topic
                .onFailure { e -> _state.update { it.copy(error = e.message) } }
        }

        _state.update { it.copy(isDraftOpened = false, draft = null) }
    }

    fun onDraftChange(newDraft: UserTopicDraft) {
        _state.update { it.copy(draft = newDraft) }
    }

}
