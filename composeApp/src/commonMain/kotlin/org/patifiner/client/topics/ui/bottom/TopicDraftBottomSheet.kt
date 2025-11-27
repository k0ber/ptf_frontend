package org.patifiner.client.topics.ui.bottom

import TopicViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.views.PtfInputExampleText
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.topics.TopicLevel
import org.patifiner.client.topics.UserTopicInfo
import org.patifiner.client.topics.presentableTopicLevels
import org.patifiner.client.topics.ui.fakeTopicsRow
import org.patifiner.client.topics.ui.topics.TopicCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDraftBottomSheet(
    modifier: Modifier = Modifier,
    topic: TopicViewModel,
    draft: UserTopicInfo,
    onDraftChange: (UserTopicInfo) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
            .navigationBarsPadding()
    ) {
        BottomSheetContent(topic, draft, onDraftChange, onDismiss, onConfirm)
    }
}

@Composable
fun BottomSheetContent(
    topic: TopicViewModel,
    draft: UserTopicInfo,
    onDraftChange: (UserTopicInfo) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        PtfShadowedText(
            text = "How good you are in:",
            fontSize = 20,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopicCard(modifier = Modifier.wrapContentSize(), topic = topic, isOpened = true, isInBreadcrumb = false, onClick = {})
            Spacer(Modifier.width(8.dp))
            PtfShadowedText(text = "?", fontSize = 20, modifier = Modifier)
        }
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = draft.description,
            onValueChange = { desc -> onDraftChange(draft.copy(description = desc)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            label = { PtfInputExampleText("Any comments?") }
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            val list = presentableTopicLevels()
            items(list.size, { list[it].name }) { index ->
                val level = list[index]
                FilterChip(
                    selected = draft.level == level,
                    onClick = { onDraftChange(draft.copy(level = level)) },
                    label = { Text(level.mark) }
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onDismiss) { Text("Cancel") }
            Spacer(Modifier.width(8.dp))
            FilledTonalButton(onClick = onConfirm) { Text("Add") }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun TopicDraftCardPreview() {
    AppTheme(forceDarkMode = false) {
        val fakeTopic = fakeTopicsRow().first()
        val draftInfo = UserTopicInfo(
            topicId = fakeTopic.id,
            level = TopicLevel.NONE,
            description = "This is a dummy description for the preview draft."
        )
        BottomSheetContent(
            topic = fakeTopic,
            draft = draftInfo,
            onDraftChange = {},
            onDismiss = {},
            onConfirm = {},
        )
    }
}