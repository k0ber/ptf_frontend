package org.patifiner.client.root.main.intro.events

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState

@Immutable
data class EventsIntroState(
    val q: String = "",
    override val status: ScreenStatus = ScreenStatus.Idle
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)
}

sealed interface EventsIntroSideEffect {
    data class Error(val message: String) : EventsIntroSideEffect
}
