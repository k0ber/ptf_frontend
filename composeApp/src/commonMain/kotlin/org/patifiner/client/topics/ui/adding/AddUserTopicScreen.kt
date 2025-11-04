package org.patifiner.client.topics.ui.adding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.common.scrollableScreen
import org.patifiner.client.common.showError
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.topics.TopicDto
import org.patifiner.client.topics.TopicLevel

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
        onTopicClick = {
            // Закрываем клавиатуру, чтобы показать карточку драфта
//            focusManager.clearFocus()
//            keyboardController?.hide()
            component.onTopicClick(it)
        },
        onDraftChange = component::onDraftChange,
        onDraftDismiss = component::onDraftDismiss,
        onDraftConfirm = component::onDraftConfirm
    )
}

@Composable
fun AddUserTopicContent(
    state: AddUserTopicState,
    onQueryChange: (String) -> Unit,
    onTopicClick: (TopicDto) -> Unit,
    onDraftChange: (UserTopicDraft) -> Unit,
    onDraftDismiss: () -> Unit,
    onDraftConfirm: (UserTopicDraft) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.scrollableScreen(scrollState)
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            text = "Add Your Interests !",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary), modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "What is your best topic ?",
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            value = state.query,
            onValueChange = onQueryChange,
            singleLine = true,
            placeholder = { Text("Search topics...") },
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), focusedBorderColor = MaterialTheme.colorScheme.primary),
            leadingIcon = { Icon(imageVector = PtfIcons.IcEmail, contentDescription = null, tint = MaterialTheme.colorScheme.primary) })

        val searchResults = state.searchResults
        TopicsTree(
            modifier = Modifier.scrollableScreen(scrollState),
            singleColumnMode = searchResults.isEmpty(),
            breadcrumb = state.breadcrumb,
            tree = searchResults.takeIf { it.isNotEmpty() } ?: state.tree,
            openedTopic = state.openedTopic,
            onTopicClick = onTopicClick
        )

        Spacer(Modifier.weight(1f))

        AnimatedVisibility(
            visible = state.isDraftOpened && state.draft != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
        ) {
            if (state.draft != null) {
                TopicDraftCard(
                    modifier = Modifier.padding(8.dp),
                    draft = state.draft,
                    onLevelChange = { lvl -> onDraftChange(state.draft.copy(level = lvl)) },
                    onDescChange = { desc -> onDraftChange(state.draft.copy(description = desc)) },
                    onDismiss = onDraftDismiss,
                    onConfirm = { onDraftConfirm(state.draft) }
                )
            }
        }
    }
}

// ======================================================================================================
@Preview
@Composable
fun AddUserTopicContentPreview() {
    val fakeTopics = TopicDto.fake(depth = 2, breadth = 4)
    val state = AddUserTopicState(
        tree = fakeTopics,
        query = "",
        openedTopic = fakeTopics.first(),
        isDraftOpened = true,
        draft = UserTopicDraft(fakeTopics.first(), TopicLevel.NEWBIE)
    )

    AppTheme(forceDarkMode = false) {
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