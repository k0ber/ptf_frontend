package org.patifiner.client.root.main.intro.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
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
    val state by viewModel.collectAsState()
    val snackbarHost = RootSnackbarHost.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            UserInfoSideEffect.Saved -> viewModel.onNext()
            is UserInfoSideEffect.Error -> snackbarHost.showError(sideEffect.message)
        }
    }

    val launcher = rememberFilePickerLauncher(type = FileKitType.Image) { file ->
        file?.let { viewModel.uploadAvatar(it) } // Прямой вызов метода вместо Intent
    }

    UserInfoContent(
        state = state,
        onNameChange = viewModel::changeName,
        onSaveProfile = viewModel::saveProfile,
        onPickAvatar = { launcher.launch() },
        onDeletePhoto = viewModel::deletePhoto
    )
}

@Composable
fun UserInfoContent(
    state: UserInfoIntroState,
    onNameChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onPickAvatar: () -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    val user = state.user
    val isLoading = state.status.isLoading

    PtfScreen {
        val avatarSource = remember(user.avatarUrl, state.selectedLocalFile) {
            val remoteUrl = user.avatarUrl
            when {
                state.selectedLocalFile != null -> AvatarSource.Local(state.selectedLocalFile)
                remoteUrl != null -> AvatarSource.Remote(remoteUrl)
                else -> null
            }
        }

        val placeholderText = remember(user.name) {
            user.name.take(1).uppercase().ifBlank { EMPTY_NAME_CHAR }
        }

        PtfLinearProgress(isLoading = isLoading)

        Spacer(Modifier.weight(1f))

        PtfShadowedText("ABOUT YOU")
        Spacer(Modifier.height(8.dp))
        PtfText("Would you mine to describe yourself?")

        Spacer(Modifier.height(32.dp))

        PtfAvatar(
            source = avatarSource,
            placeholderText = placeholderText,
            onClick = onPickAvatar,
            isUploading = isLoading
        )

        Spacer(Modifier.height(24.dp))

        // Список фото
        if (user.photos.isNotEmpty()) {
            PtfText("Your photos (${user.photos.size}/10)", fontSize = 14)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    PrimaryButton(text = "Add", onClick = onPickAvatar, enabled = !isLoading)
                }
                items(user.photos) { url ->
                    PhotoThumbnail(
                        url = url,
                        onDelete = { onDeletePhoto(url) }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // todo
//        birthDate
//        gender
//        cityId
//        languages

//        Spacer(Modifier.height(24.dp))

        PtfTextField(
            modifier = centeredField(),
            value = user.name,
            onValueChange = onNameChange,
            label = stringResource(Res.string.name_label),
            enabled = !isLoading
        )

        Spacer(Modifier.height(32.dp))

        PrimaryButton(
            text = "Continue",
            enabled = user.name.isNotBlank() && !isLoading,
            onClick = onSaveProfile
        )

        Spacer(Modifier.weight(1.5f))
    }
}

@Composable
fun UserInfoPreview() {
    UserInfoContent(
        state = UserInfoIntroState(
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
