package org.patifiner.client.topics.ui

import androidx.compose.runtime.Immutable
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.UserTopicDto

@Immutable
data class TopicViewModel(
    val id: Long,
    val name: String,
    val slug: String,
    val description: String?,
    val tags: List<String>?,
    val icon: String?,
    val parentId: Long?,
    val children: List<TopicViewModel> = emptyList(),

    val userInfo: UserTopicInfo? = null,
) {
    fun hasChild(target: TopicViewModel?): Boolean {
        if (target == null) return false
        return children.any { it.id == target.id || it.hasChild(target) }
    }
}

fun mapTreeToViewModel(tree: List<TopicDto>, userTopics: List<UserTopicDto>): List<TopicViewModel> {
    val byId = userTopics.associateBy { it.topic.id }

    fun mapNode(dto: TopicDto): TopicViewModel {
        val userInfo = byId[dto.id]?.let { UserTopicInfo(level = it.level, description = it.description ?: "") }
        return TopicViewModel(
            id = dto.id,
            name = dto.name,
            slug = dto.slug,
            description = dto.description,
            tags = dto.tags,
            icon = dto.icon,
            parentId = dto.parentId,
            userInfo = userInfo,
            children = dto.children.map { mapNode(it) }
        )
    }
    return tree.map { mapNode(it) }
}

fun List<TopicDto>.toViewModels(userTopicsById: Map<Long, TopicViewModel>): List<TopicViewModel> =
    map { it.toViewModel(userTopicsById) }

fun TopicDto.toViewModel(userTopicsById: Map<Long, TopicViewModel>): TopicViewModel {
    val userInfo = userTopicsById[this.id]?.userInfo
    return TopicViewModel(
        id = this.id,
        name = this.name,
        slug = this.slug,
        description = this.description,
        tags = this.tags,
        icon = this.icon,
        parentId = this.parentId,
        children = emptyList(),
        userInfo = userInfo
    )
}
