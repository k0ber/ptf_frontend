package org.patifiner.client.root.main.intro.topics

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState

@Immutable
data class TopicsIntroState(
    val q: String = "",
    override val status: ScreenStatus = ScreenStatus.Idle
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)
}

sealed interface TopicsIntroSideEffect {
    data class Error(val message: String) : TopicsIntroSideEffect
}
