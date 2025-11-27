package org.patifiner.client.topics

import kotlinx.serialization.Serializable
import org.patifiner.client.topics.TopicLevel.ADVANCED
import org.patifiner.client.topics.TopicLevel.INTERMEDIATE
import org.patifiner.client.topics.TopicLevel.NEWBIE

@Serializable
data class TopicDto(
    val locale: String,
    val id: Long,
    val name: String,
    val slug: String,
    val description: String?,
    val tags: List<String>?,
    val icon: String?,
    val parentId: Long?,
    val childrenIds: List<Long> = emptyList()
)

@Serializable
data class UserTopicDto(
    val id: Long,
    val userId: Long,
    val topic: TopicDto,
    val level: TopicLevel,
    val description: String?,
)

@Serializable
enum class TopicLevel(val mark: String) {
    NONE(""), NEWBIE("★"), INTERMEDIATE("★★"), ADVANCED("★★★");
}

fun presentableTopicLevels(): List<TopicLevel> = listOf(NEWBIE, INTERMEDIATE, ADVANCED)
