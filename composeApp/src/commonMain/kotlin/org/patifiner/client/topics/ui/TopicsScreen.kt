package org.patifiner.client.topics.ui

import TopicViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.screen
import org.patifiner.client.base.showError
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.topics.AddTopicEvents
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.topics.AddUserTopicState
import org.patifiner.client.topics.UserTopicInfo
import org.patifiner.client.topics.ui.bottom.TopicDraftBottomSheet
import org.patifiner.client.topics.ui.topics.TopicsTree

@Composable
fun AddUserTopicScreen(
    component: AddUserTopicComponent,
    snackbarHostState: SnackbarHostState
) {
    val state: AddUserTopicState by component.state.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        component.events.collect { event ->
            when (event) {
                is AddTopicEvents.Error -> snackbarHostState.showError(event.message)
                is AddTopicEvents.CloseKeyboard -> {
//                    focusManager.clearFocus()
//                    keyboardController?.hide()
                }
            }
        }
    }

    AddUserTopicContent(
        state = state,
        onQueryChange = component::onQueryChange,
        onTopicClick = component::onTopicClick,


        onDraftChange = component::onDraftChange,
        onDraftDismiss = component::onDraftDismiss,
        onDraftConfirm = component::onDraftConfirm,
    )
}

@Composable
fun AddUserTopicContent(
    state: AddUserTopicState,
    onQueryChange: (String) -> Unit,
    onTopicClick: (TopicViewModel) -> Unit,
    onDraftChange: (UserTopicInfo) -> Unit,
    onDraftDismiss: () -> Unit,
    onDraftConfirm: (UserTopicInfo) -> Unit,
) {
    Box(modifier = Modifier.screen()) {
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
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), focusedBorderColor = MaterialTheme.colorScheme.primary),
                leadingIcon = { Icon(imageVector = PtfIcons.IcEmail, contentDescription = null, tint = MaterialTheme.colorScheme.primary) })
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
}

@Preview
@Composable
fun TopicsScreenPreview() {
    AppTheme {
        val fakeTree = fakeTopicsTree()
        val state = AddUserTopicState(userTopicsTree = fakeTree)
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