package org.patifiner.client.root.main.topics.add

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.root.main.topics.add.ui.TopicViewModel
import org.patifiner.client.root.main.topics.data.TopicsRepository

@Stable
class AddTopicsViewModel(
    private val repo: TopicsRepository
) : ViewModel(), ContainerHost<AddTopicsState, AddTopicsSideEffect> {

    override val container = container<AddTopicsState, AddTopicsSideEffect>(AddTopicsState()) {
        // todo: load initial
    }

    fun onQueryChange(query: String) = intent {
        reduce { state.copy(query = query) }
        if (query.length > 2) {
            // todo: add search
        }
    }

    fun clickTopic(topic: TopicViewModel) = intent {
        reduce { state.copy(openedTopic = topic, draft = UserTopicInfo(topic.id)) }
    }

    fun changeDraft(info: UserTopicInfo) = intent {
        reduce { state.copy(draft = info) }
    }

    fun dismissDraft() = intent {
        reduce { state.copy(draft = null) }
    }

    fun confirmDraft(info: UserTopicInfo) = intent {
        // todo: add save
        dismissDraft()
    }
}
