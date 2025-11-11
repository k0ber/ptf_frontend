package org.patifiner.client.topics.ui

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.patifiner.client.topics.TopicLevel
import kotlin.collections.plus

sealed interface AddTopicEvents {
    data class Error(val message: String) : AddTopicEvents
    object CloseKeyboard : AddTopicEvents
}

data class AddUserTopicState(
    val query: String = "",
    val searchResult: List<TopicViewModel> = emptyList(),
    val searchLoading: Boolean = false,

    val userTopicsTree: List<TopicViewModel> = emptyList(),
    val breadcrumb: List<TopicViewModel> = emptyList(),
    val openedTopic: TopicViewModel? = null,

    val draft: UserTopicInfo? = null,
    val isDraftOpened: Boolean = false,

    val error: String? = null
)

@Immutable
data class UserTopicInfo(
    val level: TopicLevel = TopicLevel.NEWBIE,
    val description: String = ""
)

object TopicClickDelegate {
    fun updateStateAfterClick(topic: TopicViewModel, _state: MutableStateFlow<AddUserTopicState>) {
        val stateValue = _state.value
        val opened = stateValue.openedTopic
        val breadcrumb = stateValue.breadcrumb

        // --- 1. если это лист, просто открываем драфт
        if (topic.children.isEmpty()) {
            _state.update {
                it.copy(
                    draft = UserTopicInfo(level = TopicLevel.NEWBIE, description = ""),
                    isDraftOpened = true
                )
            }
            return
        }

        // --- 2. если кликнули по уже открытому — "поднимаемся" на уровень выше
        if (opened?.id == topic.id) {
            val newBreadcrumb = if (breadcrumb.size > 1) breadcrumb.dropLast(1) else emptyList()
            _state.update {
                it.copy(
                    openedTopic = newBreadcrumb.lastOrNull(),
                    breadcrumb = newBreadcrumb
                )
            }
            return
        }

        // --- 3. если клик по breadcrumb → возвращаемся на этот уровень
        if (breadcrumb.any { it.id == topic.id }) {
            val index = breadcrumb.indexOfFirst { it.id == topic.id }
            val newBreadcrumb = breadcrumb.take(index + 1)
            _state.update {
                it.copy(openedTopic = topic, breadcrumb = newBreadcrumb)
            }
            return
        }

        // --- 4. если у openedTopic есть дети и среди них кликнутый
        val isChildOfOpened = opened?.children?.any { it.id == topic.id } == true
        if (isChildOfOpened) {
            _state.update {
                it.copy(
                    openedTopic = topic,
                    breadcrumb = breadcrumb + topic
                )
            }
            return
        }

        // --- 5. клик по новому корню (сбрасываем breadcrumb)
        _state.update {
            it.copy(
                openedTopic = topic,
                breadcrumb = listOf(topic)
            )
        }
    }
}