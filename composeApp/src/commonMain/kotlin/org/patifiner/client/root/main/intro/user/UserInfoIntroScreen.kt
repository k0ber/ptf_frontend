package org.patifiner.client.root.main.intro.user

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.core.showError
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfAvatar
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfScreen
import org.patifiner.client.design.views.PtfShadowedText
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.design.views.PtfTextField
import org.patifiner.client.root.RootSnackbarHost
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.name_label

@Composable
fun UserInfoIntroScreen(viewModel: UserInfoIntroViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = RootSnackbarHost.current

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is UserInfoLabel.Error -> snackbarHost.showError(label.message)
                UserInfoLabel.Saved -> viewModel.onNext()
            }
        }
    }

    UserInfoContent(
        state = state,
        onNameChange = { viewModel.onIntent(UserInfoIntent.ChangeName(it)) },
        onSaveProfile = { viewModel.onIntent(UserInfoIntent.SaveProfile) }
    )
}

@Composable
fun UserInfoContent(
    state: UserInfoState,
    onNameChange: (String) -> Unit,
    onSaveProfile: () -> Unit
) {
    PtfScreen {
        PtfLinearProgress(isLoading = state.isLoading)

        Spacer(Modifier.weight(1f))

        PtfShadowedText("ABOUT YOU")
        Spacer(Modifier.height(8.dp))
        PtfText("How should we call you?")

        Spacer(Modifier.height(32.dp))

        PtfAvatar(
            url = state.avatarUrl,
            onClick = { /** image picker */ }
        )

        Spacer(Modifier.height(24.dp))

        PtfTextField(
            modifier = centeredField(),
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(Res.string.name_label)
        )

        Spacer(Modifier.height(32.dp))

        PrimaryButton(
            text = "Continue",
            enabled = state.name.isNotBlank() && !state.isLoading,
            onClick = onSaveProfile
        )

        Spacer(Modifier.weight(1.5f))
    }
}

@Composable
fun UserInfoPreview() {
    UserInfoContent(
        state = UserInfoState(name = "Oleg"),
        onNameChange = {},
        onSaveProfile = {}
    )
}

@Preview
@Composable
fun UserInfoPreviewLight() {
    PtfPreview { UserInfoPreview() }
}

@Preview
@Composable
fun UserInfoPreviewDark() {
    PtfPreview(forceDarkMode = true) { UserInfoPreview() }
}
