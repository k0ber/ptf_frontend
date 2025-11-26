package org.patifiner.client.topics.ui.logic

import TopicViewModel


sealed interface TreeAction {
    object OpenDraft : TreeAction
    data class ChangeOpened(val openedTopic: TopicViewModel?) : TreeAction

}

object TopicClickDelegate {

    fun updateStateAfterClick(
        topic: TopicViewModel,
        flatById: Map<Long, TopicViewModel>,
        openedTopic: TopicViewModel?
    ): TreeAction {
        // 1. Получаем полную информацию о топике из дерева
        // Это гарантирует, что мы видим список 'children' из полного дерева.
        val fullTopicInfo = flatById[topic.id] ?: topic

        // 2. Проверяем, является ли он листом в полном дереве
        if (fullTopicInfo.isLeaf) {
            return TreeAction.OpenDraft
        }

        // --- Обработка родительских топиков --- (остальная логика не меняется)
        // 3. Клик по открытому -> Закрыть или Подняться
        if (openedTopic?.id == topic.id) {
            val parent = fullTopicInfo.parentId?.let { flatById[it] }
            return TreeAction.ChangeOpened(parent)
        }

        // 4. Клик по предку -> Откатиться
        val isAncestor = openedTopic?.pathToRoot(flatById)?.any { it.id == topic.id } == true
        if (isAncestor) {
            return TreeAction.ChangeOpened(topic)
        }

        // 5. Клик по новому родителю -> Открыть
        return TreeAction.ChangeOpened(topic)
    }
}
