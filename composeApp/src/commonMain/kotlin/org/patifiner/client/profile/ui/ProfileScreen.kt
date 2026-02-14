package org.patifiner.client.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.AppTheme
import org.patifiner.client.profile.ProfileComponent

// TODO: use Coil3 for show and Peekaboo for peeking images

@Composable
fun ProfileScreen(component: ProfileComponent, snackbarHost: SnackbarHostState) {
    val state by component.state.collectAsState()

    ProfileContent(
        state = state,
        onRefresh = component::refresh,
        onLogout = component::onLogout,
        onNavToAddTopic = component::onNavToAddTopic,
        onNavToMyTopics = component::onNavToMyTopics
    )
}

@Composable
fun ProfileContent(
    state: ProfileUiState,
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
        state.userInfoDto?.let {
            Text("ID: ${it.id}")
            Text("Name: ${it.name}")
            Text("Email: ${it.email}")
        }
        state.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRefresh, enabled = !state.loading) {
            Text(if (state.loading) "Loading..." else "Refresh profile")
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
    AppTheme {
        ProfileContent(ProfileUiState())
    }
}