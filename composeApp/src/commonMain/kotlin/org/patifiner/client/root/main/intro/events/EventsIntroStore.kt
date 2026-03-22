package org.patifiner.client.root.main.intro.events

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.createDefault

interface EventsIntroStore : Store<EventsIntroIntent, EventsIntroState, EventsIntroLabel>

data class EventsIntroState(
    val q: String = "",
    override val isLoading: Boolean = false
) : BaseState<EventsIntroState> {
    override fun withLoading(isLoading: Boolean): EventsIntroState = copy(isLoading = isLoading)
}

sealed interface EventsIntroIntent {
    data class ChangeSearch(val q: String) : EventsIntroIntent
}

sealed interface EventsIntroLabel {
    data class Error(val message: String) : EventsIntroLabel
}

internal class EventsIntroStoreFactory(
    private val factory: StoreFactory
) {
    fun create(): EventsIntroStore =
        object : EventsIntroStore, Store<EventsIntroIntent, EventsIntroState, EventsIntroLabel>
        by factory.createDefault(
            initialState = EventsIntroState(),
            executorFactory = coroutineExecutorFactory {
                onIntent<EventsIntroIntent.ChangeSearch> { intent ->
                    dispatch { copy(q = intent.q) }
                }
            }
        ) {}
}
