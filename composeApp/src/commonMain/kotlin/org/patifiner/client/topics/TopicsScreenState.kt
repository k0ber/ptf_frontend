package org.patifiner.client.topics

import TopicViewModel
import androidx.compose.runtime.Immutable

sealed interface AddTopicEvents {
    data class Error(val message: String) : AddTopicEvents
    object CloseKeyboard : AddTopicEvents
}

data class AddUserTopicState(
    val query: String = "",
    val searchResult: List<TopicViewModel> = emptyList(),
    val searchLoading: Boolean = false,

    val userTopicsTree: List<TopicViewModel> = emptyList(),
    val openedTopic: TopicViewModel? = null,

    val draft: UserTopicInfo? = null,

    val error: String? = null,
) {
    val isDraftOpened: Boolean get() = draft != null
    val flatById: Map<Long, TopicViewModel> by lazy {
        fun flatten(node: TopicViewModel): List<TopicViewModel> = listOf(node) + node.children.flatMap(::flatten)
        userTopicsTree.flatMap(::flatten).associateBy { it.id }
    }
    val breadcrumbs: List<TopicViewModel> get() = openedTopic?.pathToRoot(flatById).orEmpty()
}

@Immutable
data class UserTopicInfo(
    val topicId: Long,
    val level: TopicLevel = TopicLevel.NONE,
    val description: String = ""
)
