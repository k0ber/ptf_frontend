package org.patifiner.client.topics

import org.patifiner.client.topics.data.AddUserTopicsRequest
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository

class GetTopicsTreeUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(): Result<List<TopicDto>> = repo.loadTopicsTree()
}

class SearchTopicsUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(query: String): Result<List<TopicDto>> = repo.searchTopics(query)
}

class GetUserTopicsUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(): Result<List<UserTopicDto>> = repo.getUserTopics()
}

class AddUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(req: AddUserTopicsRequest): Result<List<UserTopicDto>> =
        repo.addUserTopics(req)
}

class RemoveUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(req: RemoveUserTopicsRequest): Result<Int> = repo.removeUserTopics(req)
}
