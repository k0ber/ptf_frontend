package org.patifiner.client.topics.ui.topics

import TopicViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import org.patifiner.client.topics.TopicLevel
import org.patifiner.client.topics.UserTopicInfo


@Composable
fun TopicCard(
    topic: TopicViewModel,
    isOpened: Boolean,
    isInBreadcrumb: Boolean,
    onClick: (TopicViewModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = (if (isOpened) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary).copy(alpha = 0.5f)
    val backgroundColor = MaterialTheme.colorScheme.surface

    if (isInBreadcrumb || isOpened) MaterialTheme.colorScheme.primary.copy(alpha = 0.7f) else MaterialTheme.colorScheme.secondary
    // todo: indications and colors
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .clickable { onClick(topic) },
        shape = RoundedCornerShape(40.dp),
        border = BorderStroke(1.dp, borderColor),
        color = backgroundColor,
    ) {
        val textColor = when {
            isInBreadcrumb -> MaterialTheme.colorScheme.primary
            isInBreadcrumb -> MaterialTheme.colorScheme.secondary
            else -> MaterialTheme.colorScheme.onSurface
        }
        Text(
            text = topic.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = if (isInBreadcrumb) FontWeight.SemiBold else FontWeight.Normal,
                color = textColor
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
    topic: TopicViewModel,
    isInBreadcrumb: Boolean,
    isSelected: Boolean,
    isExpanded: Boolean,
    onClick: (TopicViewModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val hasChildren = topic.children.isNotEmpty()
    val level = topic.userInfo?.level
    val hasComment = topic.userInfo?.description?.isNotBlank() ?: false

    // Цветовая логика
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        isInBreadcrumb -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.08f)
    }

    val borderColor = when {
        level != null -> MaterialTheme.colorScheme.primary
        hasChildren -> MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
        else -> MaterialTheme.colorScheme.outline
    }

    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(topic) }
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = if (isSelected) 3.dp else 0.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = topic.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (hasComment) {
                Text(text = "💬", modifier = Modifier.padding(start = 6.dp))
            }

            if (hasChildren) {
                Text(
                    text = "..",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            if (level != null) {
                Text(
                    text = when (level) {
                        TopicLevel.NEWBIE -> "★"
                        TopicLevel.INTERMEDIATE -> "★★"
                        TopicLevel.ADVANCED -> "★★★"
                    },
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun TopicCardPreview() {
    AppTheme {
        val topic = TopicViewModel(
            id = 1,
            name = "Kotlin",
            slug = "kotlin",
            description = "A modern language",
            tags = listOf("android", "jvm"),
            icon = null,
            parentId = null,
            children = emptyList(),
            userInfo = UserTopicInfo(level = TopicLevel.INTERMEDIATE, description = "I like it")
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