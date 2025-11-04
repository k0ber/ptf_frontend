package org.patifiner.client.topics.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.UserTopicDto


class TopicsRepository(
    private val client: HttpClient,
) {
    suspend fun getUserTopics(): Result<List<UserTopicDto>> = runCatching {
        client.get("/topics/me").body<List<UserTopicDto>>()
    }

    suspend fun loadTopicsTree(): Result<List<TopicDto>> = runCatching {
        client.get("/topics/tree").body<List<TopicDto>>()
    }

    suspend fun searchTopics(q: String): Result<List<TopicDto>> = runCatching {
        client.get("/topics/search") {
            url { parameters.append("q", q) }
        }.body<List<TopicDto>>()
    }

    suspend fun addUserTopics(req: AddUserTopicsRequest): Result<List<UserTopicDto>> = runCatching {
        client.post("/topics/me") { setBody(req) }.body<List<UserTopicDto>>()
    }

    suspend fun removeUserTopics(req: RemoveUserTopicsRequest): Result<Int> = runCatching {
        client.delete("/topics/me") { setBody(req) }
            .body<Map<String, Int>>()["removed"] ?: 0
    }
}

//fun List<TopicDto>.toTree(): List<TopicItem> {
//    val byParent = groupBy { it.parentId }
//    fun build(parentId: Long?, depth: Int): List<TopicItem> =
//        (byParent[parentId] ?: emptyList()).map { dto ->
//            val kids = build(dto.id, depth + 1)
//            TopicItem(
//                id = dto.id,
//                name = dto.name,
//                parentId = dto.parentId,
//                depth = depth,
//                hasChildren = kids.isNotEmpty(),
//                children = kids
//            )
//        }
//    return build(parentId = null, depth = 0)
//}