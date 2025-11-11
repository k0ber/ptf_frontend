package org.patifiner.client.topics.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import org.patifiner.client.common.centeredField
import org.patifiner.client.common.screen
import org.patifiner.client.common.showError
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.topics.AddUserTopicComponent

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
        onDraftConfirm = component::onDraftConfirm
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
    isPreview: Boolean = false,
) {
    Box(modifier = Modifier.screen()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(32.dp))
            PtfShadowedText(
                text = "Add Your Interests !", fontSize = 24,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(64.dp))
            PtfText(
                text = "What is your best topic ?",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                modifier = centeredField(),
                value = state.query,
                onValueChange = onQueryChange,
                singleLine = true,
                placeholder = { Text("Search topics...") },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), focusedBorderColor = MaterialTheme.colorScheme.primary),
                leadingIcon = { Icon(imageVector = PtfIcons.IcEmail, contentDescription = null, tint = MaterialTheme.colorScheme.primary) })
            Spacer(Modifier.height(12.dp))

            val topicsScroll = rememberScrollState()
            AddingTopicsTree(
                state = state, onTopicClick = onTopicClick,
                modifier = Modifier.weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(topicsScroll)
                    .padding(horizontal = 8.dp),
            )
            Spacer(Modifier.height(16.dp))
        }

        if (!isPreview) {
            TopicDraftBottomSheet(
                state = state,
                onDraftChange = onDraftChange,
                onDraftDismiss = onDraftDismiss,
                onDraftConfirm = onDraftConfirm
            )
        }
    }
}
