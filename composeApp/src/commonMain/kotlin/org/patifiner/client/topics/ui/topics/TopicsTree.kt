package org.patifiner.client.topics.ui.topics

import TopicViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppShapes
import org.patifiner.client.design.AppTheme
import org.patifiner.client.topics.ui.logic.LayoutItem
import org.patifiner.client.topics.ui.logic.MeasuredTopic
import org.patifiner.client.topics.ui.logic.buildTopicTreeRows
import org.patifiner.client.topics.ui.fakeTopicsTree
import kotlin.math.max

@Composable
fun TopicsTree(
    tree: List<TopicViewModel>,
    breadcrumbs: List<TopicViewModel>,
    openedTopic: TopicViewModel?,
    onTopicClick: (TopicViewModel) -> Unit,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 8.dp,
) {
    SubcomposeLayout(modifier) { constraints ->
        val hSpacingPx = horizontalSpacing.roundToPx()
        val vSpacingPx = verticalSpacing.roundToPx()

        var breadcrumbPlaceable: Placeable? = null
        var backgroundPlaceable: Placeable? = null
        var measuredChildren: List<MeasuredTopic> = emptyList()

        val measuredTopics = tree.mapIndexed { index, topic ->
            val placeable = subcompose("TOPIC_$index") {
                TopicCard(
                    topic = topic, isOpened = topic.isOpened(openedTopic), isInBreadcrumb = topic.isInBreadcrumb(breadcrumbs), onClick = onTopicClick
                )
            }.first().measure(constraints.copy(minWidth = 0, minHeight = 0))
            MeasuredTopic(topic, placeable)
        }

        //region [MEASURE]
        if (openedTopic != null) {
            if (breadcrumbs.size > 1) {
                breadcrumbPlaceable = subcompose("BREADCRUMB_TAIL") {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        breadcrumbs.forEachIndexed { index, crumb ->
                            if (index >= 0) {
                                Text( // todo icon
                                    text = "›", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                            TopicCard(
                                topic = crumb, isInBreadcrumb = true, isOpened = false, onClick = onTopicClick
                            )
                        }
                    }
                }.first().measure(constraints.copy(minHeight = 0))
            }

            if (openedTopic.children.isNotEmpty()) {
                measuredChildren = openedTopic.children.mapIndexed { index, childTopic ->
                    val placeable = subcompose("CHILD_TOPIC_$index") {
                        TopicCard(
                            topic = childTopic, isOpened = false, isInBreadcrumb = false, onClick = onTopicClick
                        )
                    }.first().measure(constraints.copy(minWidth = 0, minHeight = 0))
                    MeasuredTopic(childTopic, placeable)
                }
            }
        }
        //endregion

        // region [ROW BUILDING]
        val (layoutItems, bgSizes) = buildTopicTreeRows(
            measuredTopics = measuredTopics,
            measuredChildren = measuredChildren,
            maxWidth = constraints.maxWidth,
            hSpacingPx = hSpacingPx,
            rowBaseHeight = measuredTopics.firstOrNull()?.placeable?.height ?: 0,
            verticalSpacingPx = vSpacingPx,
            openedTopic = openedTopic,
            breadcrumbPlaceable = breadcrumbPlaceable,
        )
        // endregion

        // region [CHILD BACKGROUND]
        bgSizes?.let { sizes ->
            backgroundPlaceable = subcompose("ACTIVE_BACKGROUND") {
                Surface(
                    tonalElevation = 4.dp,
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.14f),
                    shape = AppShapes.medium,
                ) {
                }
            }.first().measure(
                constraints.copy(
                    maxHeight = sizes.height, minHeight = sizes.height,
                    maxWidth = sizes.width, minWidth = sizes.width
                )
            )
        }
        // endregion

        // region [LAYOUT]
        val totalContentHeight = layoutItems.sumOf { it.height }
        val totalSpacingHeight = max(0, (layoutItems.size - 1) * vSpacingPx)
        val totalHeight = totalContentHeight + totalSpacingHeight
        val maxWidth = constraints.maxWidth
        layout(maxWidth, totalHeight) {

            if (backgroundPlaceable != null && bgSizes != null) {
                val backgroundYStart = bgSizes.startY
                val backgroundXStart = bgSizes.startX
                backgroundPlaceable.placeRelativeWithLayer(x = backgroundXStart, y = backgroundYStart, zIndex = -1f)
            }

            // --- 2. РАЗМЕЩЕНИЕ ВСЕХ ЭЛЕМЕНТОВ (Используем layoutItems) ---
            var y = 0
            layoutItems.forEachIndexed { index, layoutItem ->
                val itemHeight = layoutItem.height // LayoutItem теперь всегда TopicsRow

                if (index > 0) {
                    y += vSpacingPx
                }

                // Мы знаем, что layoutItem — это LayoutItem.TopicsRow
                val row = layoutItem as LayoutItem.TopicsRow

                if (row.selected != null) {
                    // Размещение активной строки (с двумя секциями и Breadcrumbs)
                    placeActiveRow(
                        row = row, y = y, maxWidth = maxWidth, hSpacingPx = hSpacingPx, vSpacingPx = vSpacingPx
                    )
                } else {
                    // Размещение обычной строки ИЛИ дочерней строки
                    val offsetX = max(0, (maxWidth - row.rowWidth) / 2)
                    var x = offsetX

                    row.topics.forEach { measuredItem ->
                        // Центрирование топика внутри высоты строки
                        val offsetYY = (row.height - measuredItem.placeable.height) / 2
                        measuredItem.placeable.placeRelative(x, y + offsetYY)
                        x += measuredItem.placeable.width + hSpacingPx
                    }
                }
                y += itemHeight
            }
        }
        // endregion
    }
}

// todo use somewhere as background
/**
 * GradientBackground(
 *                     modifier = Modifier.fillMaxWidth(),
 *                     colors = listOf(
 *                         MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.70f),
 *                         MaterialTheme.colorScheme.tertiary.copy(alpha = 0.85f),
 *                     ),
 *                 ) {}
 */

private fun Placeable.PlacementScope.placeActiveRow(
    row: LayoutItem.TopicsRow, y: Int, // Текущая вертикальная позиция (начало строки)
    maxWidth: Int, hSpacingPx: Int, vSpacingPx: Int // Для внутреннего отступа
) {
    val activeTopic = row.selected ?: return

    // 1. Расчет высоты половин и отступа
    val halfRowHeight = (row.height - vSpacingPx) / 2
    val innerSpacing = vSpacingPx

    // 2. Расчет ширин
    val nonActiveTopics = row.topics.filter { it.topic.id != activeTopic.topic.id }
    val nonActiveWidth = nonActiveTopics.sumOf { it.placeable.width } + max(0, (nonActiveTopics.size - 1) * hSpacingPx)
    val activeRowWidth = activeTopic.placeable.width + (row.breadcrumbPlaceable?.width ?: 0) + (if (row.breadcrumbPlaceable != null) hSpacingPx else 0)

    // 3. Размещение НЕАКТИВНЫХ топиков (Верхняя половина, центрирование)
    var x = max(0, (maxWidth - nonActiveWidth) / 2)
    nonActiveTopics.forEach { measuredItem ->
        val regularOffsetYY = (halfRowHeight - measuredItem.placeable.height) / 2
        measuredItem.placeable.placeRelative(x, y + regularOffsetYY)
        x += measuredItem.placeable.width + hSpacingPx
    }

    // 4. Размещение АКТИВНОГО топика + Breadcrumbs (Нижняя половина, центрирование)
    // todo: choose what to show, breadcrumbs should show instead of active topic, not together
    var activeX = max(0, (maxWidth - activeRowWidth) / 2)
    val activeRowY = y + halfRowHeight + innerSpacing

    val activeOffsetYY = halfRowHeight - activeTopic.placeable.height
    activeTopic.placeable.placeRelative(activeX, activeRowY + activeOffsetYY)
    activeX += activeTopic.placeable.width

    row.breadcrumbPlaceable?.let { bp ->
        val breadcrumbOffsetYY = activeOffsetYY
        val breadcrumbStartX = activeX + hSpacingPx
        bp.placeRelative(breadcrumbStartX, activeRowY + breadcrumbOffsetYY)
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Preview
@Composable
fun Preview_TopicsTreeSimple() {
    val fakeTree = fakeTopicsTree()
    AppTheme(forceDarkMode = true) {
        TopicsTree(
            tree = fakeTree,
            openedTopic = fakeTree.first(),
            breadcrumbs = emptyList(),
            onTopicClick = { },
        )
    }
}