package org.patifiner.client.topics

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
    val children: List<TopicDto> = emptyList()
) {
    fun hasChild(another: TopicDto?): Boolean = another != null && (this == another || children.any { it.hasChild(another) })
}

@Serializable
data class UserTopicDto(
    val id: Long,
    val userId: Long,
    val topic: TopicDto,
    val level: TopicLevel,
    val description: String?,
)

@Serializable
enum class TopicLevel { NEWBIE, INTERMEDIATE, ADVANCED }
