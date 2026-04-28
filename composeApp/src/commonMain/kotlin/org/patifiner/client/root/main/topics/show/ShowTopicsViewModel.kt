package org.patifiner.client.root.main.topics.show

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.MainTabRoute
import org.patifiner.client.root.main.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.root.main.topics.data.TopicsRepository

@Stable
class ShowTopicsViewModel(
    private val repo: TopicsRepository,
    private val mainNavigator: MainNavigator
) : ViewModel(), ContainerHost<ShowTopicsState, ShowTopicsSideEffect> {

    override val container = container<ShowTopicsState, ShowTopicsSideEffect>(ShowTopicsState()) {
        loadTopics()
    }

    fun loadTopics() = intent {
        execute(
            block = { repo.getUserTopics() },
            onSuccess = { data -> reduce { state.copy(topics = data) } },
            errorFactory = ShowTopicsSideEffect::Error
        )
    }

    fun removeTopic(id: Long) = intent {
        execute(
            block = { repo.removeUserTopics(RemoveUserTopicsRequest(listOf(id))) },
            onSuccess = { reduce { state.copy(topics = state.topics.filterNot { it.id == id }) } },
            errorFactory = ShowTopicsSideEffect::Error
        )
    }

    fun onAddClick() {
        mainNavigator.switchTab(MainTabRoute.AddUserTopic)
    }

}
