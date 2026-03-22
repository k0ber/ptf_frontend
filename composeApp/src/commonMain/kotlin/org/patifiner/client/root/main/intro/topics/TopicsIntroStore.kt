package org.patifiner.client.root.main.intro.topics

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.createDefault

interface TopicsIntroStore : Store<TopicsIntroIntent, TopicsIntroState, TopicsIntroLabel>

data class TopicsIntroState(
    val q: String = "",
    override val isLoading: Boolean = false
) : BaseState<TopicsIntroState> {
    override fun withLoading(isLoading: Boolean): TopicsIntroState = copy(isLoading = isLoading)
}

sealed interface TopicsIntroIntent {
    data class ChangeSearch(val q: String) : TopicsIntroIntent
}

sealed interface TopicsIntroLabel {
    data class Error(val message: String) : TopicsIntroLabel
}

internal class TopicsIntroStoreFactory(
    private val factory: StoreFactory
) {
    fun create(): TopicsIntroStore =
        object : TopicsIntroStore, Store<TopicsIntroIntent, TopicsIntroState, TopicsIntroLabel>
        by factory.createDefault(
            initialState = TopicsIntroState(),
            executorFactory = coroutineExecutorFactory {
                onIntent<TopicsIntroIntent.ChangeSearch> { intent ->
                    dispatch { copy(q = intent.q) }
                }
            }
        ) {}
}
