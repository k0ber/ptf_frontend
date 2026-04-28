package org.patifiner.client.root.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.compose.stability.runtime.TraceRecomposition
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.patifiner.client.core.showError
import org.patifiner.client.core.takeIfOrEmpty
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfLinkHint
import org.patifiner.client.design.views.PtfScreen
import org.patifiner.client.design.views.PtfTextField
import org.patifiner.client.root.RootSnackbarHost
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.already_have_account
import patifinerclient.composeapp.generated.resources.create_account_button
import patifinerclient.composeapp.generated.resources.creating_button
import patifinerclient.composeapp.generated.resources.email_incorrect
import patifinerclient.composeapp.generated.resources.login_link
import patifinerclient.composeapp.generated.resources.name_label
import patifinerclient.composeapp.generated.resources.passwords_dont_match
import patifinerclient.composeapp.generated.resources.pwd_to_short

const val LOGIN_LINK_TAG = "LOGIN_LINK"

@Composable
fun SignupScreen(viewModel: SignupViewModel) {
    val snackbarHost = RootSnackbarHost.current
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SignupSideEffect.Error -> snackbarHost.showError(sideEffect.message)
        }
    }

    SignupContent(
        state = state,
        onNameChange = viewModel::changeName,
        onEmailChange = viewModel::changeEmail,
        onPasswordChange = viewModel::changePassword,
        onConfirmPasswordChange = viewModel::changeConfirm,
        onSignup = viewModel::signup,
        onBackToLogin = viewModel::onBack
    )
}

@Composable
@TraceRecomposition(tag = "SignupContent")
fun SignupContent(
    state: SignupState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignup: () -> Unit,
    onBackToLogin: () -> Unit
) {
    PtfScreen {
        PtfLinearProgress(isLoading = state.status.isLoading)
        Spacer(Modifier.weight(1f))
        PtfIntro()
        Spacer(Modifier.height(16.dp))
        PtfTextField(
            modifier = centeredField(),
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(Res.string.name_label),
            isError = state.name.isNotEmpty() && !state.nameValid,
            supportingText = stringResource(Res.string.email_incorrect).takeIfOrEmpty(state.name.isNotEmpty() && !state.nameValid),
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(8.dp))
        EmailField(
            modifier = centeredField(),
            value = state.email,
            onValueChange = onEmailChange,
            isError = state.email.isNotEmpty() && !state.emailValid,
            supportingText = stringResource(Res.string.email_incorrect).takeIfOrEmpty(state.email.isNotEmpty() && !state.emailValid),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(8.dp))
        PasswordField(
            modifier = centeredField(),
            value = state.password,
            onValueChange = onPasswordChange,
            isError = state.password.isNotEmpty() && !state.passwordValid,
            supportingText = stringResource(Res.string.pwd_to_short).takeIfOrEmpty(state.password.isNotEmpty() && !state.passwordValid),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(8.dp))
        PasswordField(
            modifier = centeredField(),
            value = state.confirm,
            onValueChange = onConfirmPasswordChange,
            isError = state.confirm.isNotEmpty() && !state.confirmValid,
            supportingText = stringResource(Res.string.passwords_dont_match).takeIfOrEmpty(state.confirm.isNotEmpty() && !state.confirmValid),
            imeAction = ImeAction.Done,
            onImeAction = onSignup
        )
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = if (state.status.isLoading) stringResource(Res.string.creating_button) else stringResource(Res.string.create_account_button),
            enabled = state.canSubmit,
            onClick = onSignup
        )
        Spacer(Modifier.height(4.dp))
        PtfLinkHint(
            modifier = Modifier.testTag(LOGIN_LINK_TAG),
            text = stringResource(Res.string.already_have_account),
            linkText = stringResource(Res.string.login_link),
            onClick = onBackToLogin
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun SignupPreview() {
    SignupContent(
        state = SignupState(email = "preview@email.com", password = "password"),
        onEmailChange = {},
        onPasswordChange = {},
        onConfirmPasswordChange = {},
        onNameChange = {},
        onSignup = {},
        onBackToLogin = {}
    )
}

@Preview
@Composable
fun LoginPreviewLight() {
    PtfPreview { SignupPreview() }
}

@Preview
@Composable
fun LoginPreviewDark() {
    PtfPreview(forceDarkMode = true) { SignupPreview() }
}
