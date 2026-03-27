package org.patifiner.client.root.main.intro.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.core.Gender
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.showError
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.views.AvatarSource
import org.patifiner.client.design.views.PhotoThumbnail
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

const val EMPTY_NAME_CHAR = "?"

@Composable
fun UserInfoIntroScreen(viewModel: UserInfoIntroViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHost = RootSnackbarHost.current

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                UserInfoLabel.Saved -> viewModel.onNext()
                is UserInfoLabel.Error -> snackbarHost.showError(label.message)
            }
        }
    }

    val launcher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        file?.let { viewModel.onIntent(UserInfoIntent.UploadPhoto(it)) }
    }

    UserInfoContent(
        state = state,
        onNameChange = { viewModel.onIntent(UserInfoIntent.ChangeName(it)) },
        onSaveProfile = { viewModel.onIntent(UserInfoIntent.SaveProfile) },
        onPickAvatar = { launcher.launch() },
        onDeletePhoto = { viewModel.onIntent(UserInfoIntent.DeletePhoto(it)) }
    )
}

@Composable
fun UserInfoContent(
    state: UserInfoState,
    onNameChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onPickAvatar: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    PtfScreen {
        val avatarSource = remember(state.avatarUrl, state.selectedLocalFile) {
            val avatarUrl = state.avatarUrl
            when {
                state.selectedLocalFile != null -> AvatarSource.Local(state.selectedLocalFile)
                avatarUrl != null -> AvatarSource.Remote(avatarUrl)
                else -> null
            }
        }

        val placeholderText = remember(state.name) {
            state.name.take(1).uppercase().ifBlank { EMPTY_NAME_CHAR }
        }

        PtfLinearProgress(isLoading = state.isLoading)

        Spacer(Modifier.weight(1f))

        PtfShadowedText("ABOUT YOU")
        Spacer(Modifier.height(8.dp))
        PtfText("Would you mine to describe yourself?")

        Spacer(Modifier.height(32.dp))

        PtfAvatar(
            source = avatarSource,
            placeholderText = placeholderText,
            onClick = onPickAvatar,
            isUploading = state.isLoading
        )

        Spacer(Modifier.height(24.dp))

        // photos
        if (state.photos.isNotEmpty()) {
            PtfText("Your photos (${state.photos.size}/10)", fontSize = 14)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    PrimaryButton(text = "Add", onClick = onPickAvatar)
                }
                items(state.photos) { url ->
                    PhotoThumbnail(
                        url = url,
                        onDelete = { onDeletePhoto(url) }
                    )
                }
            }
        }

        // todo
//        birthDate
//        gender
//        cityId
//        languages

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
        state = UserInfoState(
            user = UserDto(
                1, "Oleg", emptyList(), null, "woody@.com",
                null, null, Gender.NOT_SPECIFIED, emptyList(), "ru"
            )
        ),
        onNameChange = {},
        onSaveProfile = {},
        onPickAvatar = {},
        onDeletePhoto = {},
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
