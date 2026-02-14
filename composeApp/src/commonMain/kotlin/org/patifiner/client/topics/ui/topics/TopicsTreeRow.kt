package org.patifiner.client.topics.ui.topics

import TopicViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.topics.ui.fakeTopicsRow

@Composable
fun TopicsTreeRow(
    topics: List<TopicViewModel>,
    openedTopic: TopicViewModel?,
    breadcrumbs: List<TopicViewModel>,
    onTopicClick: (TopicViewModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        topics.forEach { topic ->

            val isOpened = topic.id == openedTopic?.id
            val isCrumb = breadcrumbs.any { it.id == topic.id }

            TopicCard(
                topic = topic,
                isOpened = isOpened,
                isInBreadcrumb = isCrumb,
                onClick = { onTopicClick(topic) }
            )
        }
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Preview
@Composable
fun Preview_TopicsTreeRow() {
    val mockTopics = fakeTopicsRow()
    val opened = null//mockTopics[1]
    val breadcrumbs = listOf(mockTopics[0])

    MaterialTheme {
        TopicsTreeRow(
            topics = mockTopics,
            openedTopic = opened,
            breadcrumbs = breadcrumbs,
            onTopicClick = {},
//            isChildRow = false
        )
    }
}

//@Preview
//@Composable
//fun Preview_BreadcrumbRow() {
//    val breadcrumbs = listOf(
//        TopicViewModel(1, "Programming", "prog", null, null, null, null),
//        TopicViewModel(2, "Kotlin", "kotlin", null, null, null, null),
//        TopicViewModel(3, "Compose", "compose", null, null, null, null)
//    )
//
//    MaterialTheme {
//        BreadcrumbRow(
//            breadcrumbs = breadcrumbs,
//            onTopicClick = {}
//        )
//    }
//}