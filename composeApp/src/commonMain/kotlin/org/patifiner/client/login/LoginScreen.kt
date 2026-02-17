package org.patifiner.client.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.patifiner.client.base.Constants
import org.patifiner.client.base.showError
import org.patifiner.client.base.takeIfOrEmpty
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.centeredField
import org.patifiner.client.design.scrollableScreen
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.IndeterminateGradientProgress
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro

@Composable
fun LoginScreen(component: LoginComponent, snackbarHostState: SnackbarHostState) {
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
        onSignup = component::onSignup
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
    val screenScroll = rememberScrollState()

    Column(
        modifier = Modifier.scrollableScreen(screenScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically)
    ) {
        if (state.isLoading) IndeterminateGradientProgress(modifier = Modifier.fillMaxWidth())
        else Spacer(Modifier.height(4.dp)) // prevents height changes

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
        SignUpHint(onSignupClick = onSignup)
        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun SignUpHint(modifier: Modifier = Modifier, onSignupClick: () -> Unit) {
    Row(modifier = modifier) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically), text = "Don’t have an account?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline
        )
        TextButton(onClick = onSignupClick, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = "Sign Up", style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview()
@Composable
fun LoginPreview() {
    AppTheme {
        LoginContent(
            state = LoginState(isLoading = true, email = "preview@email.com", password = "password"),
            passwordFocusRequester = remember { FocusRequester() },
            onEmailChange = {},
            onPasswordChange = {},
            onLogin = {},
            onSignup = {}
        )
    }
}
