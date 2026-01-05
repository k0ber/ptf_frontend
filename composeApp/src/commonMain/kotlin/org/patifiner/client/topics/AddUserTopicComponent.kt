package org.patifiner.client.topics

import TopicViewModel
import com.arkivanov.decompose.ComponentContext
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.patifiner.client.common.componentScope
import org.patifiner.client.common.toUserMessage
import org.patifiner.client.topics.ui.logic.TopicClickDelegate
import org.patifiner.client.topics.ui.logic.TreeAction

@Inject
class AddUserTopicComponent(
    @Assisted componentContext: ComponentContext,
    private val loadWholeTopicsTree: LoadUserTopicsTreeUseCase,
    private val searchTopics: SearchTopicsUseCase,
    private val addUserTopic: AddUserTopicUseCase,
) : ComponentContext by componentContext {

    @AssistedFactory
    fun interface Factory {
        fun create(componentContext: ComponentContext): AddUserTopicComponent
    }

    private val scope = componentScope()

    private val _state = MutableStateFlow(AddUserTopicState())
    val state = _state.asStateFlow()

    private val _events: MutableSharedFlow<AddTopicEvents> = MutableSharedFlow()
    val events = _events.asSharedFlow()

    init {
        // todo: suspend functions launched in one scope - we need to subscribe on query after user date loading successfully completed
        loadUserTopics()
        observeSearch()
    }

    private fun loadUserTopics() {
        scope.launch(Dispatchers.Default) {
            loadWholeTopicsTree()
                .onSuccess { userTopics -> _state.update { it.copy(userTopicsTree = userTopics) } }
                .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage("Can't load user topics"))) } // todo: how user can retry?
        }
    }

    private fun observeSearch() {
        scope.launch(Dispatchers.Default) {
            state.map { it.query }
                .distinctUntilChanged()
                .combine(_state.map { it.userTopicsTree }) { query, allTopics -> Pair(query, allTopics) }
                .collectLatest { (query, allTopics) ->
                    if (query.isBlank()) {
                        _state.update { it.copy(searchResult = emptyList()) }
                    } else if (allTopics.isNotEmpty()) {
                        searchTopics(query, allTopics)
                            .onSuccess { found -> _state.update { it.copy(searchResult = found) } }
                            .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage("Search failed"))) }
                    } // else if (allTopics.isNotEmpty() мы ничего не делаем, пока loadUserTopics не завершится
                }
        }
    }

    fun onTopicClick(topic: TopicViewModel) {
        val dlgAction = TopicClickDelegate.updateStateAfterClick(
            topic = topic,
            flatById = _state.value.flatById,
            openedTopic = _state.value.openedTopic
        )
        when (dlgAction) {
            is TreeAction.OpenDraft -> {
                val initialDraft = UserTopicInfo(
                    topicId = topic.id,
                    level = topic.userInfo?.level ?: TopicLevel.NONE,
                    description = topic.userInfo?.description ?: ""
                )
                _state.update { it.copy(draft = initialDraft) }
            }

            is TreeAction.ChangeOpened -> _state.update { it.copy(openedTopic = dlgAction.openedTopic, draft = null) }
        }
    }

    // todo: you can add breadcrumb by long click (?)
//    fun onBreadcrumbLongClick(topic: TopicViewModel) {
//        _state.update { it.copy(draft = UserTopicInfo(level = TopicLevel.NEWBIE, description = "")) }
//    }

    fun onDraftConfirm(draft: UserTopicInfo) {
        scope.launch {
            val topic = _state.value.openedTopic ?: return@launch
            addUserTopic(topic, draft)
                .onSuccess { loadUserTopics() }
                .onFailure { e -> _events.emit(AddTopicEvents.Error(e.toUserMessage())) }

            _state.update { it.copy(draft = null) }
        }
    }

    fun onQueryChange(value: String) = _state.update { it.copy(query = value) }
    fun onDraftChange(newDraft: UserTopicInfo) = _state.update { it.copy(draft = newDraft) }
    fun onDraftDismiss() = _state.update { it.copy(draft = null) }

}