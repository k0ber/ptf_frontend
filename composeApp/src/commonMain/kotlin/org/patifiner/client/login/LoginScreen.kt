package org.patifiner.client.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.dont_have_account
import patifinerclient.composeapp.generated.resources.email_incorrect
import patifinerclient.composeapp.generated.resources.login_button
import patifinerclient.composeapp.generated.resources.login_loading
import patifinerclient.composeapp.generated.resources.pwd_to_short
import patifinerclient.composeapp.generated.resources.signup_link

const val LOGIN_EMAIL_FIELD_TAG: String = "LOGIN_EMAIL_FIELD_TAG"

@Composable
fun LoginScreen(
    component: LoginComponent,
    snackbarHostState: SnackbarHostState
) {
    val state by component.state.subscribeAsState()
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        component.labels.collect { label ->
            when (label) {
                is LoginLabel.Error -> snackbarHostState.showError(label.message)
                LoginLabel.FocusOnPassword -> passwordFocusRequester.requestFocus()
            }
        }
    }

    LoginContent(
        state = state,
        passwordFocusRequester = passwordFocusRequester,
        onEmailChange = { component.onIntent(LoginIntent.ChangeEmail(it)) },
        onPasswordChange = { component.onIntent(LoginIntent.ChangePassword(it)) },
        onLogin = { component.onIntent(LoginIntent.Login) },
        onSignup = { component.onNavToSignup() },
    )
}

@Composable
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

    PtfScreen(name = "Login") {
        PtfLinearProgress(isLoading = state.isLoading)
        Spacer(Modifier.weight(1f))
        PtfIntro(modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        EmailField(
            modifier = centeredField().trackCompositions(LOGIN_EMAIL_FIELD_TAG),
            value = email,
            onValueChange = onEmailChange,
            isError = email.isNotEmpty() && state.isEmailError,
            supportingText = stringResource(Res.string.email_incorrect)
                .takeIfOrEmpty(email.isNotEmpty() && state.isEmailError),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(4.dp))

        PasswordField(
            modifier = centeredField().focusRequester(passwordFocusRequester),
            value = pass,
            onValueChange = onPasswordChange,
            isError = pass.isNotEmpty() && state.isPasswordError,
            supportingText = stringResource(Res.string.pwd_to_short)
                .takeIfOrEmpty(pass.isNotEmpty() && state.isPasswordError),
            imeAction = ImeAction.Done,
            onImeAction = onLogin
        )
        Spacer(Modifier.height(16.dp))

        PrimaryButton(
            text =
                if (state.isLoading) stringResource(Res.string.login_loading)
                else stringResource(Res.string.login_button),
            enabled = !state.isEmailError && !state.isPasswordError,
            onClick = onLogin,
        )
        Spacer(Modifier.height(4.dp))

        PtfLinkHint(
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
    PtfTheme { LoginPreview() }
}

@Preview
@Composable
fun LoginPreviewDark() {
    PtfTheme(forceDarkMode = true) { LoginPreview() }
}
