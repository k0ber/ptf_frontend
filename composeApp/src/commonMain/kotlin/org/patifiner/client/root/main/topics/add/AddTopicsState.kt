package org.patifiner.client.root.main.topics.add

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState
import org.patifiner.client.root.main.topics.TopicLevel
import org.patifiner.client.root.main.topics.add.ui.TopicViewModel

@Immutable
data class UserTopicInfo(
    val topicId: Long,
    val level: TopicLevel = TopicLevel.NONE,
    val description: String = ""
)

@Immutable
data class AddTopicsState(
    val query: String = "",
    val searchResult: List<TopicViewModel> = emptyList(),

    val userTopicsTree: List<TopicViewModel> = emptyList(),
    val openedTopic: TopicViewModel? = null,

    val draft: UserTopicInfo? = null,

    override val status: ScreenStatus = ScreenStatus.Idle,
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)

    val isDraftOpened: Boolean get() = draft != null
    val flatById: Map<Long, TopicViewModel> by lazy {
        fun flatten(node: TopicViewModel): List<TopicViewModel> = listOf(node) + node.children.flatMap(::flatten)
        userTopicsTree.flatMap(::flatten).associateBy { it.id }
    }
    val breadcrumbs: List<TopicViewModel> get() = openedTopic?.pathToRoot(flatById).orEmpty()
}

sealed interface AddTopicsSideEffect {
    data class Error(val message: String) : AddTopicsSideEffect
}
