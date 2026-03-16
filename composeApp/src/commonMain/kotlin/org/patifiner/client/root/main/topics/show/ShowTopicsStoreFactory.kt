package org.patifiner.client.root.main.topics.show

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.root.main.topics.data.TopicsRepository

internal sealed interface ShowTopicsAction {
    data object Load : ShowTopicsAction
}

class ShowTopicsStoreFactory(
    private val factory: StoreFactory,
    private val repo: TopicsRepository
) {
    fun create(): ShowTopicsStore =
        object : ShowTopicsStore, Store<ShowTopicsIntent, ShowTopicsState, ShowTopicsEvent>
        by factory.createDefault(
            initialState = ShowTopicsState(),
            bootstrapper = coroutineBootstrapper { dispatch(ShowTopicsAction.Load) },
            executorFactory = coroutineExecutorFactory {
                onAction<ShowTopicsAction.Load> {
                    execute(
                        useCase = { repo.getUserTopics() },
                        onSuccessData = { data -> copy(topics = data) },
                        errorFactory = ShowTopicsEvent::Error
                    )
                }
                onIntent<ShowTopicsIntent.Remove> { intent ->
                    execute(
                        useCase = {
                            val removeReq = RemoveUserTopicsRequest(listOf(intent.id))
                            repo.removeUserTopics(removeReq)
                        },
                        onSuccessData = { _ ->
                            copy(topics = topics.filterNot { it.id == intent.id })
                        }
                    )
                }
            }
        ) {}
}
