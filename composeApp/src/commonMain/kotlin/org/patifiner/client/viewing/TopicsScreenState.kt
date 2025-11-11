package org.patifiner.client.viewing

import androidx.compose.runtime.Immutable
import org.patifiner.client.topics.UserTopicDto

sealed interface UserTopicsEvent {
    data class Error(val message: String) : UserTopicsEvent
    data class Removed(val count: Int) : UserTopicsEvent
    data class Updated(val topic: UserTopicDto) : UserTopicsEvent
}

@Immutable
data class UserTopicsState(
    val loading: Boolean = false,
    val topics: List<UserTopicDto> = emptyList(),
    val error: String? = null
)
