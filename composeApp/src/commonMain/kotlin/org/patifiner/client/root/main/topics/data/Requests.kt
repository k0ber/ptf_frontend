package org.patifiner.client.root.main.topics.data

import kotlinx.serialization.Serializable
import org.patifiner.client.root.main.topics.TopicLevel


@Serializable
data class CreateTopicRequest(
    val name: String,
    val parentId: Long? = null
)

@Serializable
data class AddUserTopicRequest(
    val topicId: Long,
    val level: TopicLevel,
    val description: String? = null
)

@Serializable
data class AddUserTopicsRequest(val topics: List<org.patifiner.client.root.main.topics.data.AddUserTopicRequest>)

@Serializable
data class RemoveUserTopicsRequest(val topicIds: List<Long>)
