package org.patifiner.client.root.main.topics.show

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import org.patifiner.client.core.BaseState
import org.patifiner.client.root.main.topics.UserTopicDto

interface ShowTopicsStore : Store<ShowTopicsIntent, ShowTopicsState, ShowTopicsEvent>

@Immutable
data class ShowTopicsState(
    val topics: List<UserTopicDto> = emptyList(),
    val error: String? = null,
    override val isLoading: Boolean = false
) : BaseState<ShowTopicsState> {
    override fun withLoading(isLoading: Boolean): ShowTopicsState = copy(isLoading = isLoading)
}

sealed interface ShowTopicsIntent {
    data object Refresh : ShowTopicsIntent
    data class Remove(val id: Long) : ShowTopicsIntent
}

sealed interface ShowTopicsEvent {
    data class Error(val message: String) : ShowTopicsEvent
}
