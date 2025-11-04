package org.patifiner.client.topics.ui.adding

import org.patifiner.client.topics.TopicDto
import kotlin.random.Random

private val random = Random(42)

// Наборы эмодзи для генерации разных категорий
private val emojis = listOf("🎮", "🎬", "🏀", "📚", "🌍", "💻", "🎵", "🏕️", "⚽", "🍔", "🚀", "🧠")

/**
 * Создаёт фейковое дерево тем.
 *
 * @param depth глубина дерева (например, 2 или 3)
 * @param breadth количество дочерних элементов на каждом уровне
 * @param parentId идентификатор родителя
 * @param level текущий уровень (внутренний параметр)
 */
fun TopicDto.Companion.fake(
    depth: Int = 2,
    breadth: Int = 3,
    parentId: Long? = null,
    level: Int = 0
): List<TopicDto> {
    if (depth <= 0) return emptyList()

    return List(breadth) { index ->
        val id = (parentId ?: 0L) * 10 + index + 1
        val emoji = emojis.random(random)
        TopicDto(
            locale = "en",
            id = id,
            name = "$emoji Topic L${level + 1}-$index",
            slug = "topic_${id}",
            description = if (level < 2) "Description for topic ${id}" else null,
            tags = listOf("tag$index", "level$level"),
            icon = emoji,
            parentId = parentId,
            children = fake(depth - 1, breadth - 2, parentId = id, level = level + 1)
        )
    }
}
