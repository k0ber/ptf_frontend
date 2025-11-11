package org.patifiner.client.topics.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDraftBottomSheet(
    state: AddUserTopicState,
    onDraftChange: (UserTopicInfo) -> Unit,
    onDraftDismiss: () -> Unit,
    onDraftConfirm: (UserTopicInfo) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (state.isDraftOpened && state.draft != null) {
        ModalBottomSheet(
            onDismissRequest = { onDraftDismiss() },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            modifier = Modifier
                .imePadding()
                .navigationBarsPadding()
        ) {
            val draft = state.draft
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                TopicDraftCard(
                    modifier = Modifier.fillMaxWidth(),
                    draft = draft,
                    onLevelChange = { lvl -> onDraftChange(draft.copy(level = lvl)) },
                    onDescChange = { desc -> onDraftChange(draft.copy(description = desc)) },
                    onDismiss = {
                        scope.launch { sheetState.hide() }
                        onDraftDismiss()
                    },
                    onConfirm = { onDraftConfirm(draft) }
                )
            }
        }
    }
}