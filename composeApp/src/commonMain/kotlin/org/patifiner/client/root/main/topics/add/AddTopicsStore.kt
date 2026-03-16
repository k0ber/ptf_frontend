package org.patifiner.client.root.main.topics.add

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import org.patifiner.client.core.BaseState
import org.patifiner.client.root.main.topics.TopicLevel
import org.patifiner.client.root.main.topics.add.ui.TopicViewModel

@Immutable
data class UserTopicInfo(
    val topicId: Long,
    val level: TopicLevel = TopicLevel.NONE,
    val description: String = ""
)

interface AddTopicsStore : Store<AddTopicsIntent, AddTopicsState, AddTopicsEvent>

@Immutable
data class AddTopicsState(
    val query: String = "",
    val searchResult: List<TopicViewModel> = emptyList(),
    override val isLoading: Boolean = false,

    val userTopicsTree: List<TopicViewModel> = emptyList(),
    val openedTopic: TopicViewModel? = null,

    val draft: UserTopicInfo? = null,

    val error: String? = null,
) : BaseState<AddTopicsState> {
    override fun withLoading(isLoading: Boolean) = copy(isLoading = isLoading)
    val isDraftOpened: Boolean get() = draft != null
    val flatById: Map<Long, TopicViewModel> by lazy {
        fun flatten(node: TopicViewModel): List<TopicViewModel> = listOf(node) + node.children.flatMap(::flatten)
        userTopicsTree.flatMap(::flatten).associateBy { it.id }
    }
    val breadcrumbs: List<TopicViewModel> get() = openedTopic?.pathToRoot(flatById).orEmpty()
}

sealed interface AddTopicsEvent {
    data class Error(val message: String) : AddTopicsEvent
    object CloseKeyboard : AddTopicsEvent
}

sealed interface AddTopicsIntent {
    data class QueryChange(val query: String) : AddTopicsIntent
    data class ClickTopic(val topic: TopicViewModel) : AddTopicsIntent
    data class ChangeDraft(val info: UserTopicInfo) : AddTopicsIntent
    data class ConfirmDraft(val info: UserTopicInfo) : AddTopicsIntent
    data object DismissDraft : AddTopicsIntent
}
