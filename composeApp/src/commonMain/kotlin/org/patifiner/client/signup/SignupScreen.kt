package org.patifiner.client.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.base.showError
import org.patifiner.client.base.takeIfOrEmpty
import org.patifiner.client.base.trackCompositions
import org.patifiner.client.design.PtfScreen
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfLinkHint
import org.patifiner.client.design.views.PtfTextField
import org.patifiner.client.signup.SignupIntent.ChangeConfirm
import org.patifiner.client.signup.SignupIntent.ChangeEmail
import org.patifiner.client.signup.SignupIntent.ChangeName
import org.patifiner.client.signup.SignupIntent.ChangePassword
import org.patifiner.client.signup.SignupIntent.Signup
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.already_have_account
import patifinerclient.composeapp.generated.resources.create_account_button
import patifinerclient.composeapp.generated.resources.creating_button
import patifinerclient.composeapp.generated.resources.email_incorrect
import patifinerclient.composeapp.generated.resources.login_link
import patifinerclient.composeapp.generated.resources.name_label
import patifinerclient.composeapp.generated.resources.passwords_dont_match
import patifinerclient.composeapp.generated.resources.pwd_to_short

@Composable
fun SignupScreen(
    component: SignupComponent,
    snackbarHostState: SnackbarHostState
) {
    val state by component.state.subscribeAsState()

    LaunchedEffect(Unit) {
        component.labels.collect { label ->
            when (label) {
                is SignupLabel.Error -> snackbarHostState.showError(label.message)
                SignupLabel.Success -> {
                    /** handled in @RootComponent via repo */
                }
            }
        }
    }

    SignupContent(
        state = state,
        onNameChange = { component.onIntent(ChangeName(it)) },
        onEmailChange = { component.onIntent(ChangeEmail(it)) },
        onPasswordChange = { component.onIntent(ChangePassword(it)) },
        onConfirmPasswordChange = { component.onIntent(ChangeConfirm(it)) },
        onSignup = { component.onIntent(Signup) },
        onBackToLogin = { component.onBack() }
    )
}

@Composable
fun SignupContent(
    state: SignupState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSignup: () -> Unit,
    onBackToLogin: () -> Unit
) {
    PtfScreen(name = "Signup") {
        PtfLinearProgress(isLoading = state.isLoading)
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
            modifier = centeredField().trackCompositions("SignupEmail"),
            value = state.email,
            onValueChange = onEmailChange,
            isError = state.email.isNotEmpty() && !state.emailValid,
            supportingText = stringResource(Res.string.email_incorrect).takeIfOrEmpty(state.email.isNotEmpty() && !state.emailValid),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(8.dp))
        PasswordField(
            modifier = centeredField().trackCompositions("SignupPassword"),
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
            text = if (state.isLoading) stringResource(Res.string.creating_button) else stringResource(Res.string.create_account_button),
            enabled = state.canSubmit,
            onClick = onSignup
        )
        Spacer(Modifier.height(4.dp))
        PtfLinkHint(
            text = stringResource(Res.string.already_have_account),
            linkText = stringResource(Res.string.login_link),
            onClick = onBackToLogin
        )
    }
}

@Composable
fun SignupPreview() {
    SignupContent(
        state = SignupState(isLoading = false, email = "preview@email.com", password = "password"),
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
    PtfTheme { SignupPreview() }
}

@Preview
@Composable
fun LoginPreviewDark() {
    PtfTheme(forceDarkMode = true) { SignupPreview() }
}