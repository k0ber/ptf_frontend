package org.patifiner.client.topics.ui.adding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.views.Chip
import org.patifiner.client.topics.TopicDto

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopicsTree(
    modifier: Modifier = Modifier,
    singleColumnMode: Boolean,
    breadcrumb: List<TopicDto>,
    tree: List<TopicDto>,
    openedTopic: TopicDto?,
    onTopicClick: (TopicDto) -> Unit,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(), // плавное изменение высоты, без "прыжков"
        horizontalArrangement = if (singleColumnMode) Arrangement.Start
                                else Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = if (singleColumnMode) 1 else Int.MAX_VALUE
    ) {
        tree.forEach { topic ->

            val isOpened = openedTopic?.id == topic.id
            val isInBreadcrumb = breadcrumb.any { it.id == topic.id }

            // фильтрация: если топик уже есть в breadcrumb, мы не дублируем его в корне
            if (isInBreadcrumb && breadcrumb.firstOrNull()?.id != topic.id) return@forEach

            Column(
                modifier = Modifier
                    .animateContentSize()
                    .background(
                        if (isOpened || isInBreadcrumb)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                        else
                            Color.Transparent,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(4.dp)
            ) {
                val showTopicWithChildren = breadcrumb.isNotEmpty() && topic.hasChild(openedTopic)

                if (showTopicWithChildren) {
                    TopicWithChildren(
                        topic = topic,
                        breadcrumb = breadcrumb,
                        openedTopic = openedTopic,
                        onTopicClick = onTopicClick
                    )
                } else {
                    TopicCard(
                        modifier = Modifier.clickable { onTopicClick(topic) },
                        topic = topic,
                        selected = isOpened || isInBreadcrumb,
                        onClick = onTopicClick
                    )
                }
            }
        }
    }
}

@Composable
fun TopicWithChildren(
    topic: TopicDto,
    breadcrumb: List<TopicDto>,
    openedTopic: TopicDto?,
    onTopicClick: (TopicDto) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Рисуем breadcrumb только один раз — для актуальной ветки
        FlowRow(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 4.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            breadcrumb.forEachIndexed { index, breadcrumbTopic ->
                TopicCard(
                    modifier = Modifier.clickable { onTopicClick(breadcrumbTopic) },
                    topic = breadcrumbTopic,
                    selected = true,
                    onClick = onTopicClick
                )
                if (index < breadcrumb.lastIndex) {
                    Text(
                        ">",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        val shouldShowChildren = openedTopic?.let { topic.hasChild(it) || it.id == topic.id } == true
        AnimatedVisibility(
            visible = shouldShowChildren && openedTopic.children.isNotEmpty(),
            enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
            exit = shrinkVertically(animationSpec = tween(200)) + fadeOut()
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                openedTopic?.children?.forEach { child ->
                    TopicCard(
                        topic = child,
                        selected = false,
                        onClick = onTopicClick
                    )
                }
            }
        }
    }
}


@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    selected: Boolean,
    topic: TopicDto,
    onClick: (TopicDto) -> Unit,
) {
    Chip(
        modifier = modifier,
        text = topic.name,
        selected = selected,
        onClick = { onClick(topic) }
    )
}

// ======================================================================================================
@Preview
@Composable
fun TopicTreePreview() {
    val fakeTopics = listOf(
        TopicDto(
            "", 1, "t1", "", "", emptyList(), null, 0,
            listOf(
                TopicDto("", 2, "t2<-1", "", "", emptyList(), null, 1, emptyList()),
                TopicDto("", 3, "t3<-1", "", "", emptyList(), null, 1, emptyList()),
                TopicDto("", 4, "t4<-1", "", "", emptyList(), null, 1, emptyList())
            )
        ),
        TopicDto(
            "", 44, "t44", "", "", emptyList(), "", 0,
            listOf(
                TopicDto("", 444, "t44<-444", "", "", emptyList(), null, 1, emptyList())
            )
        )
    )
    AppTheme(forceDarkMode = false) {
        TopicsTree(
            singleColumnMode = true,
            breadcrumb = listOf(fakeTopics.first()),
            tree = fakeTopics,
            openedTopic = fakeTopics.first(),
            onTopicClick = {},
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        )
    }
}
