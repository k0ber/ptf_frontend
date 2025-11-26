import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.patifiner.client.topics.ui.topics.TopicCard


@Composable
fun ChildrenFlowRow(
    children: List<TopicViewModel>,
    openedTopic: TopicViewModel?,
    breadcrumbs: List<TopicViewModel>,
    onTopicClick: (TopicViewModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .animateContentSize(tween(250)),
        color = bg,
        shape = RoundedCornerShape(14.dp)
    ) {
        FlowRow(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = Int.MAX_VALUE
        ) {
            children.forEach { topic ->
                TopicCard(
                    topic = topic,
                    isInBreadcrumb = topic.isInBreadcrumb(breadcrumbs),
                    isOpened = topic.isOpened(openedTopic),
                    onClick = onTopicClick,
                )
            }
        }
    }
}