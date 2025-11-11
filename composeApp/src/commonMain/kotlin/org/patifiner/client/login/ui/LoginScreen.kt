package org.patifiner.client.login.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.aakira.napier.Napier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.common.Constants
import org.patifiner.client.common.centeredField
import org.patifiner.client.common.scrollableScreen
import org.patifiner.client.common.showError
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.IndeterminateGradientProgress
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.login.LoginComponent

@Composable
fun LoginScreen(component: LoginComponent, snackbarHostState: SnackbarHostState) {
    val state by component.state.collectAsState()
    val passwordFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        component.events.collect { event ->
            when (event) {
                is LoginScreenEvent.Error -> snackbarHostState.showError(event.message)
                is LoginScreenEvent.FocusOnPassword -> passwordFocusRequester.requestFocus()
            }
        }
    }

    LoginContent(
        state = state,
        passwordFocusRequester = passwordFocusRequester,
        onEmailChange = { component.onEmailChange(it) },
        onPasswordChange = { component.onPasswordChange(it) },
        onEmailConfirm = { component.onEmailConfirm() },
        onPasswordConfirm = { component.onPasswordConfirm() },
        onSignupClick = { component.onSignup() }
    )
}

@Composable
fun LoginContent(
    state: LoginScreenState,
    passwordFocusRequester: FocusRequester,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onEmailConfirm: () -> Unit = {},
    onPasswordConfirm: () -> Unit = {},
    onSignupClick: () -> Unit = {},
) {
    val email = state.email
    val pass = state.password
    val emailValid = remember(email) { Regex(Constants.EMAIL_REGEX).matches(email) }
    val passValid = pass.length >= Constants.MIN_PASS_LNG
    val screenScroll = rememberScrollState()

    Column(
        modifier = Modifier.scrollableScreen(screenScroll)
    ) {
        Spacer(Modifier.weight(1f))
        PtfIntro(modifier = Modifier.fillMaxWidth().clickable(onClick = { Napier.d { "Click" } }))
        Spacer(Modifier.height(20.dp))
        EmailField(
            modifier = centeredField(),
            value = email,
            placeholder = "name@example.com",
            onValueChange = onEmailChange,
            isError = email.isNotEmpty() && !emailValid,
            supportingText = if (email.isNotEmpty() && !emailValid) "Введите корректный e-mail" else "",
            imeAction = ImeAction.Next,
            onImeAction = { onEmailConfirm() }
        )
        Spacer(Modifier.height(4.dp))
        PasswordField(
            modifier = centeredField().focusRequester(passwordFocusRequester),
            value = pass,
            label = "Password",
            onValueChange = onPasswordChange,
            isError = pass.isNotEmpty() && !passValid,
            supportingText = if (pass.isNotEmpty() && !passValid) "Минимум 8 символов" else "",
            imeAction = ImeAction.Done,
            onImeAction = { onPasswordConfirm() }
        )
        PrimaryButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = if (state.loading) "Loading..." else "Login",
            enabled = emailValid && passValid && !state.loading,
            onClick = { onPasswordConfirm() },
        )
        SignUpHint(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onSignupClick = onSignupClick,
        )
        Spacer(Modifier.weight(1f))
        if (state.loading) {
            IndeterminateGradientProgress(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SignUpHint(
    modifier: Modifier = Modifier, onSignupClick: () -> Unit
) {
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

// ================================================================================================================
@Preview()
@Composable
fun LoginPreview() {
    val focusRequester = remember { FocusRequester() }
    AppTheme {
        LoginContent(state = LoginScreenState(loading = true), passwordFocusRequester = focusRequester, onEmailChange = {}, onPasswordChange = {})
    }
}

@Preview()
@Composable
fun LoginPreviewDark() {
    val focusRequester = remember { FocusRequester() }
    AppTheme(forceDarkMode = true) {
        LoginContent(state = LoginScreenState(), passwordFocusRequester = focusRequester, onEmailChange = {}, onPasswordChange = {})
    }
}
