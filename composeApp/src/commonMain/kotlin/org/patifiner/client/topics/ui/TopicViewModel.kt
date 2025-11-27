import androidx.compose.runtime.Immutable
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.UserTopicDto
import org.patifiner.client.topics.UserTopicInfo

@Immutable
data class TopicViewModel(
    val id: Long,
    val name: String,
    val slug: String,
    val description: String?,
    val tags: List<String>?,
    val icon: String?,
    val parentId: Long?,
    val children: List<TopicViewModel>,
    val userInfo: UserTopicInfo? = null,
) {
    val isLeaf: Boolean get() = children.isEmpty()
    val isRoot: Boolean get() = parentId == null

    fun isOpened(opened: TopicViewModel?) = id == opened?.id
    fun isOpenedOrContainsOpened(opened: TopicViewModel?): Boolean = id == opened?.id || (opened != null && containsChild(opened.id))
    fun isInBreadcrumb(crumbs: List<TopicViewModel>) = crumbs.any { it.id == id }
    fun containsChild(targetId: Long): Boolean = children.any { it.id == targetId || it.containsChild(targetId) }

    fun pathToRoot(byId: Map<Long, TopicViewModel>): List<TopicViewModel> {
        var cur: TopicViewModel? = this
        val result = ArrayList<TopicViewModel>()
        while (cur != null) {
            result.add(cur)
            cur = cur.parentId?.let { byId[it] }
        }
        return result.reversed()
    }
}

fun TopicDto.toViewModel(
    userInfo: UserTopicInfo?,
    childrenViewModels: List<TopicViewModel>
) = TopicViewModel(
    id = this.id,
    name = this.name,
    slug = this.slug,
    description = this.description,
    tags = this.tags,
    icon = this.icon,
    parentId = this.parentId,
    children = childrenViewModels,
    userInfo = userInfo,
)

// todo: create test for this
fun mapTreeToViewModel(tree: List<TopicDto>, userTopics: List<UserTopicDto>): List<TopicViewModel> {
    val userTopicsById = userTopics.associateBy { it.topic.id }
    val dtoMap = tree.associateBy { it.id }
    val assembledNodesCache = mutableMapOf<Long, TopicViewModel>()

    fun buildNodeRecursively(topicId: Long): TopicViewModel? {
        // 1. Проверка кэша и ранний выход
        assembledNodesCache[topicId]?.let { return it }
        val dto = dtoMap[topicId] ?: return null

        // 2. Рекурсивно собираем дочерние узлы
        val childrenViewModels = dto.childrenIds
            .mapNotNull { childId -> buildNodeRecursively(childId) }

        // 3. Создаем UserInfo и конечный узел с использованием функции-расширения
        val userInfo = userTopicsById[dto.id]?.let { userDto ->
            UserTopicInfo(
                topicId = userDto.topic.id,
                level = userDto.level,
                description = userDto.description ?: ""
            )
        }

        val finalNode = dto.toViewModel(userInfo, childrenViewModels)
        assembledNodesCache[topicId] = finalNode
        return finalNode
    }

    val rootIds = tree.filter { it.parentId == null }.map { it.id }
    return rootIds.mapNotNull { buildNodeRecursively(it) }
}