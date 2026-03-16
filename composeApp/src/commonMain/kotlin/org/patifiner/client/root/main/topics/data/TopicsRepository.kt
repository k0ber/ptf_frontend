package org.patifiner.client.root.main.topics.data

import io.ktor.client.HttpClient
import org.patifiner.client.core.deleteWithCount
import org.patifiner.client.core.get
import org.patifiner.client.core.getSearch
import org.patifiner.client.core.post
import org.patifiner.client.root.main.topics.TopicDto
import org.patifiner.client.root.main.topics.UserTopicDto


class TopicsRepository(
    private val client: HttpClient,
) {
    suspend fun getUserTopics(): Result<List<UserTopicDto>> = runCatching { client.get("/topics/me") }

    suspend fun loadTopicsTree(): Result<List<TopicDto>> = runCatching { client.get("/topics/tree") }

    suspend fun searchTopics(q: String): Result<List<TopicDto>> = runCatching {
        client.getSearch("/topics/search", q)
    }

    suspend fun addUserTopics(req: AddUserTopicsRequest): Result<List<UserTopicDto>> = runCatching {
        client.post("/topics/me", req)
    }

    suspend fun removeUserTopics(req: RemoveUserTopicsRequest): Result<Int> = runCatching {
        client.deleteWithCount("/topics/me", req)
    }
}
