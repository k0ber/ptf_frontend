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
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.patifiner.client.core.Gender
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.showError
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.root.RootSnackbarHost

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val state by viewModel.collectAsState()
    val snackbarHost = RootSnackbarHost.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ProfileSideEffect.Error -> snackbarHost.showError(sideEffect.message)
            ProfileSideEffect.ProfileUpdated -> { /* todo ? */
            }
        }
    }

    ProfileContent(
        state = state,
        onRefresh = viewModel::refreshProfile,
        onLogout = viewModel::logout,
        onNavToAddTopic = viewModel::onNavToAddTopic,
        onNavToMyTopics = viewModel::onNavToMyTopics,
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
    val user = state.user
    val isLoading = state.status.isLoading

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PtfLinearProgress(isLoading = isLoading)

        Spacer(Modifier.height(16.dp))

        PtfText("ID: ${user.id}")
        PtfText("Name: ${user.name}")
        PtfText("Email: ${user.email}")

        Spacer(Modifier.height(16.dp))

        Button(onClick = onRefresh, enabled = !isLoading) {
            Text(if (isLoading) "Loading..." else "Refresh profile")
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
        ProfileContent(
            ProfileState(
                UserDto(
                    id = 1,
                    name = "Mikle",
                    photos = emptyList(),
                    birthDate = null,
                    email = "test@email",
                    cityId = 1,
                    cityName = "Pkh",
                    gender = Gender.MALE,
                    languages = emptyList(),
                    locale = "en",
                )
            )
        )
    }
}
