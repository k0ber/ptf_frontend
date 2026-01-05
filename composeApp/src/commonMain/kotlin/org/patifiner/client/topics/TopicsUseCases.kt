package org.patifiner.client.topics

import TopicViewModel
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import mapTreeToViewModel
import org.patifiner.client.topics.data.AddUserTopicRequest
import org.patifiner.client.topics.data.AddUserTopicsRequest
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository

@Inject
class LoadUserTopicsTreeUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(): Result<List<TopicViewModel>> = runCatching {
        coroutineScope {
            val topicsDeferred = async { repo.loadTopicsTree().getOrThrow() }
            val userTopicsDeferred = async { repo.getUserTopics().getOrThrow() }

            val topics = topicsDeferred.await()
            val userTopics = userTopicsDeferred.await()

            mapTreeToViewModel(topics, userTopics)
        }
    }
}

@Inject
class SearchTopicsUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(query: String, userTopicsTree: List<TopicViewModel>): Result<List<TopicViewModel>> = runCatching {
        fun flatten(node: TopicViewModel): List<TopicViewModel> = listOf(node) + node.children.flatMap(::flatten)
        val fullTopicMap: Map<Long, TopicViewModel> = userTopicsTree.flatMap(::flatten).associateBy { it.id }
        val searchResultsDto: List<TopicDto> = repo.searchTopics(query).getOrThrow()
        return@runCatching searchResultsDto.mapNotNull { dto -> fullTopicMap[dto.id] }
    }
}

@Inject
class AddUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(topic: TopicViewModel, draft: UserTopicInfo): Result<List<UserTopicDto>> = runCatching {
        val description = draft.description.takeIf { it.isNotBlank() }
        val req = AddUserTopicsRequest(
            topics = listOf(
                AddUserTopicRequest(
                    topicId = topic.id,
                    level = draft.level,
                    description = description
                )
            )
        )
        return repo.addUserTopics(req)
    }
}

@Inject
class RemoveUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(req: RemoveUserTopicsRequest): Result<Int> = repo.removeUserTopics(req)
}
