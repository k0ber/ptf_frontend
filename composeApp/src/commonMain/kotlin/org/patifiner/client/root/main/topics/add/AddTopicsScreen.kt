package org.patifiner.client.root.main.topics.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.patifiner.client.core.showError
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.icons.IcEmail
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.root.RootSnackbarHost
import org.patifiner.client.root.main.topics.add.ui.TopicViewModel
import org.patifiner.client.root.main.topics.add.ui.bottom.TopicDraftBottomSheet
import org.patifiner.client.root.main.topics.add.ui.fakeTopicsTree
import org.patifiner.client.root.main.topics.add.ui.topics.TopicsTree

@Composable
fun AddTopicsScreen(viewModel: AddTopicsViewModel) {
    val state by viewModel.collectAsState()
    val snackbarHost = RootSnackbarHost.current
    val focusManager = LocalFocusManager.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is AddTopicsSideEffect.Error -> snackbarHost.showError(event.message)
        }
    }

    AddUserTopicContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onTopicClick = viewModel::clickTopic,
        onDraftChange = viewModel::changeDraft,
        onDraftDismiss = viewModel::dismissDraft,
        onDraftConfirm = viewModel::confirmDraft,
    )
}

@Composable
fun AddUserTopicContent(
    state: AddTopicsState,
    onQueryChange: (String) -> Unit,
    onTopicClick: (TopicViewModel) -> Unit,
    onDraftChange: (UserTopicInfo) -> Unit,
    onDraftDismiss: () -> Unit,
    onDraftConfirm: (UserTopicInfo) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.height(32.dp))
        PtfShadowedText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Add Your Interests !", fontSize = 24
        )
        Spacer(Modifier.height(64.dp))
        PtfText(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "What is your best topic ?"
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            modifier = centeredField(),
            value = state.query,
            onValueChange = onQueryChange,
            singleLine = true,
            placeholder = { Text("Search topics...") },
            shape = RoundedCornerShape(20.dp), // todo: move TextFields colors to Design System
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = colorScheme.primary.copy(alpha = 0.4f), focusedBorderColor = colorScheme.primary),
            leadingIcon = { Icon(imageVector = IcEmail, contentDescription = null, tint = colorScheme.primary) })
        Spacer(Modifier.height(8.dp))
        val showResults = state.query.isNotEmpty() && state.searchResult.isNotEmpty()
        TopicsTree(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            horizontalSpacing = 12.dp,
            verticalSpacing = 8.dp,
            tree = if (showResults) state.searchResult else state.userTopicsTree,
            breadcrumbs = state.breadcrumbs,
            openedTopic = state.openedTopic,
            onTopicClick = onTopicClick,
        )
        Spacer(Modifier.height(16.dp))
    }

    if (state.isDraftOpened && state.draft != null && state.openedTopic != null) {
        val currentDraft = state.draft
        TopicDraftBottomSheet(
            topic = state.openedTopic,
            draft = currentDraft,
            onDraftChange = onDraftChange,
            onDismiss = onDraftDismiss,
            onConfirm = { onDraftConfirm(currentDraft) },
        )
    }
}

@Preview
@Composable
fun TopicsScreenPreview() {
    PtfPreview {
        val fakeTree = fakeTopicsTree()
        val state = AddTopicsState(userTopicsTree = fakeTree)
        AddUserTopicContent(
            state = state,
            onQueryChange = {},
            onTopicClick = {},
            onDraftChange = {},
            onDraftDismiss = {},
            onDraftConfirm = {},
        )
    }
}
