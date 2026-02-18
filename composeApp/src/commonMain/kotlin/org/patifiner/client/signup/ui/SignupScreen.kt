package org.patifiner.client.signup.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.patifiner.client.base.showError
import org.patifiner.client.design.PtfScreen
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.design.views.PtfLinearProgress
import org.patifiner.client.design.views.PtfLinkHint
import org.patifiner.client.design.views.PtfTextField
import org.patifiner.client.signup.SignupComponent
import org.patifiner.client.signup.SignupIntent
import org.patifiner.client.signup.SignupIntent.ChangeEmail
import org.patifiner.client.signup.SignupIntent.ChangeName
import org.patifiner.client.signup.SignupIntent.Signup
import org.patifiner.client.signup.SignupLabel

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
                SignupLabel.Success -> { /* Navigate or show success */
                }
            }
        }
    }

    SignupContent(
        state = state,
        onNameChange = { component.onIntent(ChangeName(it)) },
        onEmailChange = { component.onIntent(ChangeEmail(it)) },
        onPasswordChange = { component.onIntent(SignupIntent.ChangeConfirm(it)) },
        onConfirmPasswordChange = { component.onIntent(ChangeName(it)) },
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
    PtfScreen {
        PtfLinearProgress(isLoading = state.isLoading)
        PtfIntro()
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PtfTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = "Name",
                isError = state.name.isNotEmpty() && !state.nameValid,
                supportingText = if (state.name.isNotEmpty() && !state.nameValid) "At least 2 characters" else "",
                imeAction = ImeAction.Next,
            )
            Spacer(Modifier.height(8.dp))

            EmailField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "name@example.com",
                isError = state.email.isNotEmpty() && !state.emailValid,
                supportingText = if (state.email.isNotEmpty() && !state.emailValid) "Enter valid email" else "",
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(8.dp))

            PasswordField(
                value = state.password,
                label = "Password",
                onValueChange = onPasswordChange,
                isError = state.password.isNotEmpty() && !state.passwordValid,
                supportingText = if (state.password.isNotEmpty() && !state.passwordValid) "At least 8 characters" else "",
                imeAction = ImeAction.Next
            )
            Spacer(Modifier.height(8.dp))

            PasswordField(
                value = state.confirm,
                label = "Confirm password",
                onValueChange = onConfirmPasswordChange,
                isError = state.confirm.isNotEmpty() && !state.confirmValid,
                supportingText = if (state.confirm.isNotEmpty() && !state.confirmValid) "Passwords do not match" else "",
                imeAction = ImeAction.Done,
                onImeAction = onSignup
            )

            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = if (state.isLoading) "Creating..." else "Create account",
                enabled = state.canSubmit,
                onClick = onSignup
            )

            PtfLinkHint(text = "Already have an account?", linkText = "Log in", onClick = onBackToLogin)
        }
    }
}
