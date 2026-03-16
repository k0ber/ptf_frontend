package org.patifiner.client.root.main.topics

import kotlinx.serialization.Serializable

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

fun presentableTopicLevels(): List<TopicLevel> = listOf(
    TopicLevel.NEWBIE,
    TopicLevel.INTERMEDIATE,
    TopicLevel.ADVANCED
)
