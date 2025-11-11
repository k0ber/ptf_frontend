package org.patifiner.client.topics.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import org.patifiner.client.topics.TopicLevel

@Composable
fun TopicDraftCard(
    modifier: Modifier = Modifier.fillMaxWidth(),
    draft: UserTopicInfo,
    onLevelChange: (TopicLevel) -> Unit,
    onDescChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            // todo: add TopicViewModel and draw a new card here
//            Text("Add: ${draft.topic.name}", style = MaterialTheme.typography.titleMedium)
            Text("Add Topic", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Text("Level", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TopicLevel.entries.forEach { lvl ->
                    FilterChip(
                        selected = draft.level == lvl,
                        onClick = { onLevelChange(lvl) },
                        label = { Text(lvl.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = draft.description,
                onValueChange = onDescChange,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                label = { Text("Any comments? (not required)") }
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onDismiss) { Text("Cancel") }
                Spacer(Modifier.width(8.dp))
                FilledTonalButton(onClick = onConfirm) { Text("Add") }
            }
        }
    }
}

@Preview
@Composable
fun TopicDraftCardPreview() {
    AppTheme(forceDarkMode = false) {
//        val fakeTopic = TopicDto(locale = "", id = 1, name = "Topic", slug = "slug", description = null, tags = null, icon = null, parentId = -1, children = emptyList())
        TopicDraftCard(
            draft = UserTopicInfo(level = TopicLevel.NEWBIE, description = ""),
            onLevelChange = {},
            onDescChange = {},
            onConfirm = {},
            onDismiss = {}
        )
    }
}
