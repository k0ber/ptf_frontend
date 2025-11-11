package org.patifiner.client.topics

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.Dispatchers
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
import org.patifiner.client.topics.ui.AddTopicEvents
import org.patifiner.client.topics.ui.AddUserTopicState
import org.patifiner.client.topics.ui.TopicClickDelegate
import org.patifiner.client.topics.ui.TopicViewModel
import org.patifiner.client.topics.ui.UserTopicInfo

class AddUserTopicComponent(
    componentContext: ComponentContext,
    private val loadUserTopicsTree: suspend () -> Result<List<TopicViewModel>>,
    private val searchTopics: suspend (String, List<TopicViewModel>) -> Result<List<TopicViewModel>>,
    private val addUserTopic: suspend (TopicViewModel, UserTopicInfo) -> Result<List<UserTopicDto>>,
    private val onDone: () -> Unit
) : ComponentContext by componentContext {

    private val scope = componentScope()

    private val _state = MutableStateFlow(AddUserTopicState())
    val state: StateFlow<AddUserTopicState> = _state.asStateFlow()

    private val _events: MutableSharedFlow<AddTopicEvents> = MutableSharedFlow()
    val events = _events.asSharedFlow()

    init {
        loadTree()
        observeSearch()
    }

    private fun observeSearch() {
        scope.launch(Dispatchers.Default) {
            state
                .map { it.query }
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _state.update { it.copy(searchResult = emptyList()) }
                        return@collectLatest
                    }

                    val userTree = _state.value.userTopicsTree
                    searchTopics(query, userTree)
                        .onSuccess { topics -> _state.update { it.copy(searchResult = topics) } }
                        .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage())) }
                }
        }
    }

    private fun loadTree() {
        scope.launch(Dispatchers.Default) {
            loadUserTopicsTree()
                .onSuccess { tree -> _state.update { it.copy(userTopicsTree = tree) } }
                .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage())) }
        }
    }

    fun onQueryChange(value: String) = _state.update { it.copy(query = value) }

    fun onTopicClick(topic: TopicViewModel) {
        _events.tryEmit(AddTopicEvents.CloseKeyboard)
        TopicClickDelegate.updateStateAfterClick(topic, _state)
    }

    fun onDraftConfirm(draft: UserTopicInfo) {
        scope.launch {
            val topic = _state.value.openedTopic ?: return@launch
            addUserTopic(topic, draft)
                .onSuccess { loadTree() }
                .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage())) }

            _state.update { it.copy(isDraftOpened = false, draft = null) }
        }
    }

    fun onBreadcrumbLongClick(topic: TopicViewModel) {
        _state.update {
            it.copy(
                draft = UserTopicInfo(level = TopicLevel.NEWBIE, description = ""),
                isDraftOpened = true
            )
        }
    }

    fun onDraftDismiss() {
        _state.update { it.copy(isDraftOpened = false, draft = null) }
    }

    fun onDraftChange(newDraft: UserTopicInfo) {
        _state.update { it.copy(draft = newDraft) }
    }

}