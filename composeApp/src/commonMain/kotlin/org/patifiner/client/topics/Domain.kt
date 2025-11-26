package org.patifiner.client.topics

import TopicViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import mapTreeToViewModel
import org.patifiner.client.topics.data.AddUserTopicRequest
import org.patifiner.client.topics.data.AddUserTopicsRequest
import org.patifiner.client.topics.data.RemoveUserTopicsRequest
import org.patifiner.client.topics.data.TopicsRepository

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

class SearchTopicsUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(query: String, userTopicsTree: List<TopicViewModel>): Result<List<TopicViewModel>> = runCatching {
        // 1. Создаем Map для доступа ко ВСЕМ элементам дерева (включая детей)
        // 🔑 Добавляем функцию сплющивания, так как userTopicsTree — это список корней.
        fun flatten(node: TopicViewModel): List<TopicViewModel> = listOf(node) + node.children.flatMap(::flatten)
        val fullTopicMap: Map<Long, TopicViewModel> = userTopicsTree.flatMap(::flatten).associateBy { it.id }

        // 2. Получаем DTO результатов поиска
        val searchResultsDto: List<TopicDto> = repo.searchTopics(query).getOrThrow()

        // 3. Используем уже сформированные TopicViewModel из полного дерева
        return@runCatching searchResultsDto.mapNotNull { dto ->
            // 🔑 Если ID найден в полном дереве, мы используем ГОТОВУЮ ViewModel (с детьми, userInfo и т.д.)
            fullTopicMap[dto.id]
        }
    }
}

class AddUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(topic: TopicViewModel, draft: UserTopicInfo): Result<List<UserTopicDto>> = runCatching {
        val description = draft.description.takeIf { it.isNotBlank() }
        val req = AddUserTopicsRequest(topics = listOf(
            AddUserTopicRequest(
                topicId = topic.id,
                level = draft.level,
                description = description
            )
        ))
        return repo.addUserTopics(req)
    }
}

class RemoveUserTopicUseCase(private val repo: TopicsRepository) {
    suspend operator fun invoke(req: RemoveUserTopicsRequest): Result<Int> = repo.removeUserTopics(req)
}
