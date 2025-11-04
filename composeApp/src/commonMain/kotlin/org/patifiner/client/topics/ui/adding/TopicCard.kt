package org.patifiner.client.topics.ui.adding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.LocalSpacing
import org.patifiner.client.topics.TopicDto

//@Composable
//fun TopicCard(
//    topic: TopicDto,
//    isExpanded: Boolean,
//    onClick: (TopicDto) -> Unit
//) {
//    val sp = LocalSpacing.current
//    val interaction = remember { MutableInteractionSource() }
//
//    Surface(
//        shape = RoundedCornerShape(40.dp),
//        tonalElevation = if (isExpanded) 6.dp else 1.dp,
//        shadowElevation = if (isExpanded) 4.dp else 0.dp,
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
//        color = if (isExpanded)
//            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
//        else
//            MaterialTheme.colorScheme.surface,
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(
//                interactionSource = interaction,
//                indication = rememberRipple(bounded = true)
//            ) { onClick(topic) }
//            .padding(vertical = sp.xs.dp)
//    ) {
//        Text(
//            text = topic.name,
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
//            style = MaterialTheme.typography.bodyLarge.copy(
//                fontWeight = if (isExpanded) FontWeight.SemiBold else FontWeight.Normal,
//                color = if (isExpanded)
//                    MaterialTheme.colorScheme.primary
//                else
//                    MaterialTheme.colorScheme.onSurface
//            )
//        )
//    }
//}