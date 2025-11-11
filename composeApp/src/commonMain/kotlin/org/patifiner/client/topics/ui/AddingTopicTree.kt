package org.patifiner.client.topics.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme

@Composable
fun AddingTopicsTree(state: AddUserTopicState, modifier: Modifier = Modifier, onTopicClick: (TopicViewModel) -> Unit) {
    val singleColumnMode = state.searchResult.isEmpty()
    val actualTree = state.searchResult.ifEmpty { state.userTopicsTree }

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing)) // todo: create common anim spec
            .padding(horizontal = 8.dp),
        horizontalArrangement = if (singleColumnMode) Arrangement.Start else Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = if (singleColumnMode) 1 else Int.MAX_VALUE
    ) {
        actualTree.forEach { rootTopic ->
            val isInBreadcrumb = state.breadcrumb.any { it.id == rootTopic.id }
            val showShadow = isInBreadcrumb || state.openedTopic?.id == rootTopic.id
            val bg = if (showShadow) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent

            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .animateContentSize(animationSpec = tween(250))
                    .background(bg, shape = MaterialTheme.shapes.small)
                    .padding(4.dp)
            ) {
                // breadcrumbs
                if (isInBreadcrumb) {
                    LazyRow(
                        modifier = Modifier.wrapContentSize()
                            .animateContentSize(animationSpec = tween(250))
                            .background(color = bg, shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) { // single scrollable line
                        state.breadcrumb.forEachIndexed { index, topicCrumb ->
                            item(key = topicCrumb.id) {
                                TopicCard(
                                    topic = topicCrumb,
                                    isSelected = topicCrumb.id == state.openedTopic?.id,
                                    isExpanded = true, // EXPANDED ?
                                    isInBreadcrumb = isInBreadcrumb,
                                    onClick = onTopicClick
                                )
                                if (index < state.breadcrumb.lastIndex) {
                                    Text(text = ">", color = MaterialTheme.colorScheme.onSurfaceVariant) // modifier = Modifier.align(Alignment.CenterVertically),
                                }
                            }
                        }
                    }
                }
                // показываем детей только у открытого топика
                val topicIsOpened = state.openedTopic?.parentId == rootTopic.id || rootTopic.id == state.openedTopic?.id
                if (topicIsOpened) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(animationSpec = tween(300, easing = LinearOutSlowInEasing)) // todo: create common anim spec
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.openedTopic.children.forEach { childTopic ->
                            TopicCard(
                                topic = childTopic,
                                isSelected = childTopic.isOpened(state),
                                isExpanded = false,
                                isInBreadcrumb = false,
                                onClick = onTopicClick
                            )
                        }
                    }
                }
                TopicCard(
                    topic = rootTopic,
                    isSelected = rootTopic.isOpened(state),
                    isExpanded = false,
                    isInBreadcrumb = false,
                    onClick = onTopicClick
                )
            }
        }
    }
}

fun TopicViewModel.isInBreadcrumb(state: AddUserTopicState): Boolean = state.breadcrumb.any { breadcrumb -> breadcrumb.id == this.id }
fun TopicViewModel.isOpened(state: AddUserTopicState): Boolean = state.openedTopic?.id == this.id

//@Composable
//fun TopicColumn(
//    topic: TopicViewModel,
//    openedTopic: TopicViewModel?,
//    breadcrumbs: List<TopicViewModel>,
//    onTopicClick: (TopicViewModel) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    val isMagic = state.openedTopic != null && topic.hasChild(state.openedTopic)
//
//        val shouldShowChildren = topic.hasChild(openedTopic)
//        val areChildrenVisible = shouldShowChildren && topic.children.isNotEmpty()
//        PtfAnim(areChildrenVisible) {
//            FlowRow(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                topic.children.forEach { child ->
//                    TopicCard(
//                        topic = child,
//                        isSelected = false, // todo what if it is topic reopening?
//                        isExpanded = false,
//                        isInBreadcrumb = false,
//                        onClick = onTopicClick
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun PtfAnim(isVisible: Boolean, content: @Composable (AnimatedVisibilityScope.() -> Unit)) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(animationSpec = tween(250)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(200)) + fadeOut(),
        content = content
    )
}

// ======================================================================================================
@Preview
@Composable
fun AddingTopicsTreePreview() {
    val fakeTree = listOf(
        TopicViewModel(
            id = 1,
            name = "Спорт",
            slug = "sport",
            description = "Разные виды спорта",
            tags = null,
            icon = null,
            parentId = null,
            children = listOf(
                TopicViewModel(
                    id = 2,
                    name = "Водные",
                    slug = "water",
                    description = "Плавание и сапы",
                    tags = null,
                    icon = null,
                    parentId = 1,
                    children = listOf(
                        TopicViewModel(
                            id = 3,
                            name = "Плавание",
                            slug = "swimming",
                            description = null,
                            tags = null,
                            icon = null,
                            parentId = 2,
                            children = emptyList()
                        ),
                        TopicViewModel(
                            id = 4,
                            name = "Сапы",
                            slug = "sup",
                            description = null,
                            tags = null,
                            icon = null,
                            parentId = 2,
                            children = emptyList()
                        )
                    )
                ),
                TopicViewModel(
                    id = 5,
                    name = "Бег",
                    slug = "running",
                    description = null,
                    tags = null,
                    icon = null,
                    parentId = 1
                ),
                TopicViewModel(
                    id = 6,
                    name = "Футбол",
                    slug = "football",
                    description = null,
                    tags = null,
                    icon = null,
                    parentId = 1
                )
            )
        ),
        TopicViewModel(
            id = 7,
            name = "Путешествия",
            slug = "travel",
            description = "Места, маршруты, отдых",
            tags = null,
            icon = null,
            parentId = null,
            children = emptyList()
        )
    )
    val breadcrumb: List<TopicViewModel> = listOf(
        fakeTree.first(),
        fakeTree.first().children.first(),
    )
    val openedTopic = fakeTree.first().children.first()
    val state = AddUserTopicState(
        userTopicsTree = fakeTree,
        openedTopic = openedTopic,
        breadcrumb = breadcrumb,
        query = "",
        isDraftOpened = false
    )
    AppTheme {
        AddingTopicsTree(modifier = Modifier.fillMaxSize(), state = state, onTopicClick = {})
    }
}
