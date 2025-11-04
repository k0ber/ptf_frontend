package org.patifiner.client.topics.ui.adding

import androidx.compose.runtime.Immutable
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.TopicLevel

sealed interface AddTopicEvents {
    data class Error(val message: String): AddTopicEvents
    object CloseKeyboard: AddTopicEvents
}

data class AddUserTopicState(
    val query: String = "",
    val searchResults: List<TopicDto> = emptyList(),
    val searchLoading: Boolean = false,

    val tree: List<TopicDto> = emptyList(),
    val breadcrumb: List<TopicDto> = emptyList(),
    val openedTopic: TopicDto? = null,

    val level: TopicLevel = TopicLevel.NEWBIE,
    val description: String = "",

    val draft: UserTopicDraft? = null,
    val isDraftOpened: Boolean = false,

    val error: String? = null
)


@Immutable
data class UserTopicDraft(
    val topic: TopicDto,
    val level: TopicLevel = TopicLevel.NEWBIE,
    val description: String = ""
)
