package org.patifiner.client.root.main.topics.show

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState
import org.patifiner.client.root.main.topics.UserTopicDto

@Immutable
data class ShowTopicsState(
    val topics: List<UserTopicDto> = emptyList(),
    override val status: ScreenStatus = ScreenStatus.Idle
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)
}

sealed interface ShowTopicsSideEffect {
    data class Error(val message: String) : ShowTopicsSideEffect
}
