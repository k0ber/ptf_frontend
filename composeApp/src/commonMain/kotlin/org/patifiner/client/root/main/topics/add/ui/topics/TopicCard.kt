package org.patifiner.client.root.main.topics.add.ui.topics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.root.main.topics.TopicLevel
import org.patifiner.client.root.main.topics.add.UserTopicInfo
import org.patifiner.client.root.main.topics.add.ui.TopicViewModel
import org.patifiner.client.root.main.topics.add.ui.fakeTopicsRow


@Composable
fun TopicCard(
    modifier: Modifier = Modifier,
    topic: TopicViewModel,
    isOpened: Boolean,
    isInBreadcrumb: Boolean,
    onClick: (TopicViewModel) -> Unit,
) {
    val borderColor = (if (isOpened) colorScheme.tertiary else colorScheme.secondary).copy(alpha = 0.5f)
    val backgroundColor = colorScheme.surface

    if (isInBreadcrumb || isOpened) colorScheme.primary.copy(alpha = 0.7f) else colorScheme.secondary
    // todo: indications and colors
    Surface(
        modifier = modifier.wrapContentHeight().clickable { onClick(topic) },
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, borderColor),
        color = backgroundColor,
    ) {
        val textColor = when {
            isInBreadcrumb -> colorScheme.primary
            isInBreadcrumb -> colorScheme.secondary
            else -> colorScheme.onSurface
        }
        Text(
            text = topic.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = typography.titleSmall.copy(
                fontWeight = if (isInBreadcrumb) FontWeight.SemiBold
                else FontWeight.Normal, color = textColor
            )
        )
    }
}

/**
 * isOpened -> AppTheme.colors.primary.copy(alpha = 0.25f)
 * isBreadcrumb -> AppTheme.colors.primary.copy(alpha = 0.12f)
 * else -> AppTheme.colors.surfaceVariant
 */
@Composable
fun TopicCardColored(
    topic: TopicViewModel, isInBreadcrumb: Boolean, isSelected: Boolean, isExpanded: Boolean, onClick: (TopicViewModel) -> Unit, modifier: Modifier = Modifier
) {
    val hasChildren = topic.children.isNotEmpty()
    val level = topic.userInfo?.level
    val hasComment = topic.userInfo?.description?.isNotBlank() ?: false

    // Цветовая логика
    val backgroundColor = when {
        isSelected -> colorScheme.primary.copy(alpha = 0.15f)
        isInBreadcrumb -> colorScheme.secondary.copy(alpha = 0.12f)
        else -> colorScheme.surfaceVariant.copy(alpha = 0.08f)
    }

    val borderColor = when {
        level != null -> colorScheme.primary
        hasChildren -> colorScheme.outline.copy(alpha = 0.7f)
        else -> colorScheme.outline
    }

    Surface(
        modifier = modifier.clip(shapes.medium).clickable { onClick(topic) }.background(backgroundColor).padding(horizontal = 10.dp, vertical = 6.dp),
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = if (isSelected) 3.dp else 0.dp,
        shape = shapes.medium
    ) {
        Row(
            modifier = Modifier.wrapContentSize(), horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = topic.name, style = typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
            if (hasComment) {
                Text(text = "💬", modifier = Modifier.padding(start = 6.dp))
            }
            if (hasChildren) {
                Text(text = "..", color = colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 6.dp))
            }
            if (level != null) {
                PtfShadowedText(
                    fontSize = 12, text = level.mark, modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun TopicCardPreview() {
    val fakeTopic = fakeTopicsRow().first()
    PtfPreview {
        val topic = TopicViewModel(
            id = 1,
            name = "Kotlin",
            slug = "kotlin",
            description = "A modern language",
            tags = listOf("android", "jvm"),
            icon = null,
            parentId = null,
            children = emptyList(),
            userInfo = UserTopicInfo(
                topicId = fakeTopic.id,
                level = TopicLevel.INTERMEDIATE,
                description = "I like it"
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "isInBreadcrumb = false, isOpened = false")
            TopicCard(topic = topic, isInBreadcrumb = false, isOpened = false, onClick = {})
            Text(text = "isInBreadcrumb = false, isOpened = true")
            TopicCard(topic = topic, isInBreadcrumb = false, isOpened = true, onClick = {})
            Text(text = "isInBreadcrumb = true, isOpened = false")
            TopicCard(topic = topic, isInBreadcrumb = true, isOpened = false, onClick = {})
            Text(text = "isInBreadcrumb = true, isOpened = true")
            TopicCard(topic = topic, isInBreadcrumb = true, isOpened = true, onClick = {})
        }
    }
}