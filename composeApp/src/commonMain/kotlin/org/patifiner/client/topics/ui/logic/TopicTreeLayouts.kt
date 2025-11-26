package org.patifiner.client.topics.ui.logic

import TopicViewModel
import androidx.compose.ui.layout.Placeable

interface Sizeable {
    val height: Int
}

data class MeasuredTopic(val topic: TopicViewModel, val placeable: Placeable) : Sizeable {
    override val height = placeable.height
}

sealed class LayoutItem : Sizeable {
    data class TopicsRow(
        val topics: List<MeasuredTopic>,
        val selected: MeasuredTopic?,
        val breadcrumbPlaceable: Placeable?,
        override val height: Int,
        val rowWidth: Int,
        val isChildRow: Boolean
    ) : LayoutItem()

}
