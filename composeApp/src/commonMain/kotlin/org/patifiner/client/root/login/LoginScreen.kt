package org.patifiner.client.root.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.skydoves.compose.stability.runtime.TraceRecomposition
import org.jetbrains.compose.resources.stringResource
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
import org.patifiner.client.root.RootSnackbarHost
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.dont_have_account
import patifinerclient.composeapp.generated.resources.email_incorrect
import patifinerclient.composeapp.generated.resources.login_button
import patifinerclient.composeapp.generated.resources.login_loading
import patifinerclient.composeapp.generated.resources.pwd_to_short
import patifinerclient.composeapp.generated.resources.signup_link

const val LOGIN_EMAIL_FIELD_TAG: String = "LOGIN_EMAIL_FIELD"
const val SIGNUP_LINK_TAG: String = "SIGNUP_LINK"

@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    val snackbarHost = RootSnackbarHost.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.labels.collect { label ->
            when (label) {
                is LoginLabel.Error -> snackbarHost.showError(label.message)
                LoginLabel.FocusOnPassword -> passwordFocusRequester.requestFocus()
            }
        }
    }

    LoginContent(
        state = state,
        passwordFocusRequester = passwordFocusRequester,
        onEmailChange = { viewModel.onIntent(LoginIntent.ChangeEmail(it)) },
        onPasswordChange = { viewModel.onIntent(LoginIntent.ChangePassword(it)) },
        onLogin = { viewModel.onIntent(LoginIntent.Login) },
        onSignup = { viewModel.onNavToSignup() },
    )
}

@Composable
@TraceRecomposition(tag = "LoginContent")
fun LoginContent(
    state: LoginState,
    passwordFocusRequester: FocusRequester,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onSignup: () -> Unit
) {
    val email = state.email
    val pass = state.password
    val isEmailError = state.isEmailError
    val isPassError = state.isPasswordError

    PtfScreen {
        PtfLinearProgress(isLoading = state.isLoading)
        Spacer(Modifier.weight(1f))
        PtfIntro(modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        EmailField(
            modifier = centeredField().testTag(LOGIN_EMAIL_FIELD_TAG),
            value = email,
            onValueChange = onEmailChange,
            isError = isEmailError,
            supportingText = stringResource(Res.string.email_incorrect)
                .takeIfOrEmpty(isEmailError),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(4.dp))

        PasswordField(
            modifier = centeredField().focusRequester(passwordFocusRequester),
            value = pass,
            onValueChange = onPasswordChange,
            isError = isPassError,
            supportingText = stringResource(Res.string.pwd_to_short)
                .takeIfOrEmpty(isPassError),
            imeAction = ImeAction.Done,
            onImeAction = onLogin
        )
        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text =
                if (state.isLoading) stringResource(Res.string.login_loading)
                else stringResource(Res.string.login_button),
            enabled = email.isNotEmpty() && !isEmailError
                    && pass.isNotEmpty() && !isPassError,
            onClick = onLogin,
        )
        Spacer(Modifier.height(12.dp))

        PtfLinkHint(
            modifier = Modifier.testTag(SIGNUP_LINK_TAG),
            text = stringResource(Res.string.dont_have_account),
            linkText = stringResource(Res.string.signup_link),
            onClick = onSignup
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun LoginPreview() {
    LoginContent(
        state = LoginState(isLoading = false, email = "preview@email.com", password = "password"),
        passwordFocusRequester = remember { FocusRequester() },
        onEmailChange = {},
        onPasswordChange = {},
        onLogin = {},
        onSignup = {}
    )
}

@Preview
@Composable
fun LoginPreviewLight() {
    PtfPreview { LoginPreview() }
}

@Preview
@Composable
fun LoginPreviewDark() {
    PtfPreview(forceDarkMode = true) { LoginPreview() }
}
