package org.patifiner.client.topics

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.patifiner.client.topics.data.AddUserTopicRequest
import org.patifiner.client.topics.data.AddUserTopicsRequest
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository
import org.patifiner.client.topics.ui.UserTopicInfo
import org.patifiner.client.topics.ui.TopicViewModel
import org.patifiner.client.topics.ui.mapTreeToViewModel
import org.patifiner.client.topics.ui.toViewModels

class SearchTopicsUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(query: String, userTopicsTree: List<TopicViewModel>): Result<List<TopicViewModel>> = runCatching {
        val topics: List<TopicDto> = repo.searchTopics(query).getOrThrow()
        val userTopics: Map<Long, TopicViewModel> = userTopicsTree.associateBy { it.id } // todo ??
        topics.toViewModels(userTopics)
    }
}

class AddUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(topic: TopicViewModel, draft: UserTopicInfo): Result<List<UserTopicDto>> {
        val req = AddUserTopicsRequest(topics = listOf(AddUserTopicRequest(topicId = topic.id, level = draft.level, description = draft.description.ifBlank { null })))
        return repo.addUserTopics(req)
    }
}

class LoadUserTopicsTreeUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(): Result<List<TopicViewModel>> = runCatching {
        coroutineScope {
            val treeDeferred = async { repo.loadTopicsTree().getOrThrow() }
            val userTopicsDeferred = async { repo.getUserTopics().getOrThrow() }

            val tree = treeDeferred.await()
            val userTopics = userTopicsDeferred.await()

            mapTreeToViewModel(tree, userTopics)
        }
    }
}

class RemoveUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(req: RemoveUserTopicsRequest): Result<Int> = repo.removeUserTopics(req)
}
