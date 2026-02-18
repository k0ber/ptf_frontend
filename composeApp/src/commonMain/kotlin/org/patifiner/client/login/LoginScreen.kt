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
import org.patifiner.client.base.Constants
import org.patifiner.client.base.showError
import org.patifiner.client.base.takeIfOrEmpty
import org.patifiner.client.design.PtfScreen
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfLinkHint

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
        onSignup = component::onNavToSignup
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
    val emailValid = remember(email) { Regex(Constants.EMAIL_REGEX).matches(email) }
    val passValid = pass.length >= Constants.MIN_PASS_LNG

    PtfScreen {
        PtfLinearProgress(isLoading = state.isLoading)

        Spacer(Modifier.weight(1f))
        PtfIntro(modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(20.dp))
        EmailField(
            modifier = centeredField(),
            value = email,
            placeholder = "name@example.com",
            onValueChange = onEmailChange,
            isError = email.isNotEmpty() && !emailValid,
            supportingText = "Введите корректный e-mail".takeIfOrEmpty(email.isNotEmpty() && !emailValid),
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(4.dp))
        PasswordField(
            modifier = centeredField().focusRequester(passwordFocusRequester),
            value = pass,
            label = "Password",
            onValueChange = onPasswordChange,
            isError = pass.isNotEmpty() && !passValid,
            supportingText = "Минимум 8 символов".takeIfOrEmpty(pass.isNotEmpty() && !passValid),
            imeAction = ImeAction.Done,
            onImeAction = onLogin
        )
        PrimaryButton(
            text = if (state.isLoading) "Loading..." else "Login",
            enabled = emailValid && passValid && !state.isLoading,
            onClick = onLogin,
        )
        PtfLinkHint(text = "Don’t have an account?", linkText = "Sign Up", onClick = onSignup)
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
