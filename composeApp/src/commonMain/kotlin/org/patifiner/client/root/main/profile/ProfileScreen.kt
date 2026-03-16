package org.patifiner.client.root.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.root.RootSnackbarHost

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = RootSnackbarHost.current

    ProfileContent(
        state = state,
        onRefresh = { viewModel.onIntent(ProfileIntent.Refresh) },
        onLogout = { viewModel.onIntent(ProfileIntent.Logout) },
        onNavToAddTopic = { viewModel.onNavToAddTopic() },
        onNavToMyTopics = { viewModel.onNavToMyTopics() },
    )
}

@Composable
fun ProfileContent(
    state: ProfileState,
    onRefresh: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavToMyTopics: () -> Unit = {},
    onNavToAddTopic: () -> Unit = {},
) {
    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(16.dp))
        state.userDto?.let {
            Text("ID: ${it.id}")
            Text("Name: ${it.name}")
            Text("Email: ${it.email}")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onRefresh, enabled = !state.isLoading) {
            Text(if (state.isLoading) "Loading..." else "Refresh profile")
        }
        Button(onClick = onLogout, colors = ButtonDefaults.buttonColors()) {
            Text("Logout")
        }
        Button(onClick = onNavToMyTopics, colors = ButtonDefaults.buttonColors()) {
            Text("My Topics")
        }
        Button(onClick = onNavToAddTopic, colors = ButtonDefaults.buttonColors()) {
            Text("Add Topic")
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    PtfPreview {
        ProfileContent(ProfileState())
    }
}
