package org.patifiner.client.root.main.topics.add

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.topics.AddUserTopicUseCase
import org.patifiner.client.root.main.topics.LoadUserTopicsTreeUseCase
import org.patifiner.client.root.main.topics.SearchTopicsUseCase
import org.patifiner.client.root.main.topics.TopicLevel
import org.patifiner.client.root.main.topics.add.ui.logic.TopicClickDelegate
import org.patifiner.client.root.main.topics.add.ui.logic.TreeAction

internal sealed interface AddUserTopicsAction {
    data object Init : AddUserTopicsAction
}

class AddTopicsStoreFactory(
    private val factory: StoreFactory,
    private val loadTree: LoadUserTopicsTreeUseCase,
    private val search: SearchTopicsUseCase,
    private val addTopic: AddUserTopicUseCase
) {
    fun create(): AddTopicsStore =
        object : AddTopicsStore, Store<AddTopicsIntent, AddTopicsState, AddTopicsEvent>
        by factory.createDefault(
            initialState = AddTopicsState(),
            bootstrapper = coroutineBootstrapper { dispatch(AddUserTopicsAction.Init) },
            executorFactory = coroutineExecutorFactory {

                onAction<AddUserTopicsAction.Init> {
                    execute(
                        useCase = { loadTree() },
                        loading = { withLoading(it) },
                        onSuccessData = { data -> copy(userTopicsTree = data) },
                        errorFactory = AddTopicsEvent::Error
                    )
                }

                onIntent<AddTopicsIntent.QueryChange> { intent ->
                    dispatch { copy(query = intent.query) }
                    if (intent.query.isBlank()) dispatch { copy(searchResult = emptyList()) }
                    else execute(
                        useCase = { search(intent.query, state().userTopicsTree) },
                        onSuccessData = { data -> copy(searchResult = data) },
                        errorFactory = AddTopicsEvent::Error
                    )
                }

                onIntent<AddTopicsIntent.ClickTopic> { intent ->
                    val dlgAction = TopicClickDelegate.updateStateAfterClick(
                        topic = intent.topic,
                        flatById = state().flatById,
                        openedTopic = state().openedTopic
                    )
                    when (dlgAction) {
                        is TreeAction.OpenDraft -> {
                            dispatch {
                                copy(
                                    draft = UserTopicInfo(
                                        topicId = intent.topic.id,
                                        level = intent.topic.userInfo?.level ?: TopicLevel.NONE,
                                        description = intent.topic.userInfo?.description ?: ""
                                    )
                                )
                            }
                        }

                        is TreeAction.ChangeOpened -> {
                            dispatch { copy(openedTopic = dlgAction.openedTopic, draft = null) }
                        }
                    }
                }

                onIntent<AddTopicsIntent.ConfirmDraft> { intent ->
                    val topic = state().openedTopic ?: return@onIntent
                    execute(
                        useCase = { addTopic(topic, intent.info) },
                        onSuccessData = { _ -> copy(draft = null) },
                        errorFactory = AddTopicsEvent::Error
                    )
                }

                onIntent<AddTopicsIntent.DismissDraft> {
                    dispatch { copy(draft = null) }
                }
            }
        ) {}
}

// you can add breadcrumb by long click (?)
//    fun onBreadcrumbLongClick(topic: TopicViewModel) {
//        _state.update { it.copy(draft = UserTopicInfo(level = TopicLevel.NEWBIE, description = "")) }
//    }
