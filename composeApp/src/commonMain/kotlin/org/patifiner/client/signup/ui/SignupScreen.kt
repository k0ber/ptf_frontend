package org.patifiner.client.signup.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.common.showError
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.scrollableScreen
import org.patifiner.client.design.views.EmailField
import org.patifiner.client.design.views.IndeterminateGradientProgress
import org.patifiner.client.design.views.PasswordField
import org.patifiner.client.design.views.PrimaryButton
import org.patifiner.client.design.views.PtfIntro
import org.patifiner.client.signup.SignupComponent
import org.patifiner.client.signup.SignupEvent

@Composable
fun SignupScreen(
    component: SignupComponent,
    snackbarHostState: SnackbarHostState
) {
    val state by component.state.collectAsState()

    LaunchedEffect(Unit) {
        component.events.collect { e ->
            when (e) {
                is SignupEvent.Error -> snackbarHostState.showError(e.message)
            }
        }
    }

    SignupContent(
        state = state,
        onName = component::onName,
        onSurname = component::onSurname,
        onYear = component::onYear,
        onMonth = component::onMonth,
        onDay = component::onDay,
        onEmail = component::onEmail,
        onPassword = component::onPassword,
        onConfirm = component::onConfirm,
        onSignup = component::onSignup,
        onBack = component::onBack
    )
}

@Composable
fun SignupContent(
    state: SignupUiState,
    onName: (String) -> Unit = {},
    onSurname: (String) -> Unit = {},
    onYear: (String) -> Unit = {},
    onMonth: (String) -> Unit = {},
    onDay: (String) -> Unit = {},
    onEmail: (String) -> Unit = {},
    onPassword: (String) -> Unit = {},
    onConfirm: (String) -> Unit = {},
    onSignup: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val screenScroll = rememberScrollState()

    Column(
        modifier = Modifier.scrollableScreen(screenScroll)
    ) {
        PtfIntro()

        SignupForm(
            modifier = Modifier.padding(horizontal = 20.dp),
            state = state,
            onName = onName,
            onSurname = onSurname,
            onYear = onYear,
            onMonth = onMonth,
            onDay = onDay,
            onEmail = onEmail,
            onPassword = onPassword,
            onConfirm = onConfirm,
            onSignup = onSignup,
            onBack = onBack
        )

        if (state.loading) {
            IndeterminateGradientProgress()
        }
    }
}

@Composable
private fun SignupForm(
    modifier: Modifier,
    state: SignupUiState,
    onName: (String) -> Unit,
    onSurname: (String) -> Unit,
    onYear: (String) -> Unit,
    onMonth: (String) -> Unit,
    onDay: (String) -> Unit,
    onEmail: (String) -> Unit,
    onPassword: (String) -> Unit,
    onConfirm: (String) -> Unit,
    onSignup: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Name / Surname
        AppTextField(
            value = state.name,
            onValueChange = onName,
            label = "First name",
            isError = state.name.isNotEmpty() && !state.nameValid,
            supportingText = if (state.name.isNotEmpty() && !state.nameValid) "Минимум 2 символа" else "",
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(8.dp))
        AppTextField(
            value = state.surname,
            onValueChange = onSurname,
            label = "Last name",
            isError = state.surname.isNotEmpty() && !state.surnameValid,
            supportingText = if (state.surname.isNotEmpty() && !state.surnameValid) "Минимум 2 символа" else "",
            imeAction = ImeAction.Next,
        )
        Spacer(Modifier.height(8.dp))

        // Birth date (DD / MM / YYYY)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTextField(
                modifier = Modifier.weight(1f),
                value = state.day,
                onValueChange = onDay,
                label = "DD",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                isError = state.day.isNotEmpty() && !state.dateValid,
            )
            Spacer(Modifier.width(8.dp))
            AppTextField(
                modifier = Modifier.weight(1f),
                value = state.month,
                onValueChange = onMonth,
                label = "MM",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                isError = state.month.isNotEmpty() && !state.dateValid,
            )
            Spacer(Modifier.width(8.dp))
            AppTextField(
                modifier = Modifier.weight(1.5f),
                value = state.year,
                onValueChange = onYear,
                label = "YYYY",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                isError = state.year.isNotEmpty() && !state.dateValid,
                supportingText = if ((state.day + state.month + state.year).isNotEmpty() && !state.dateValid) "Некорректная дата" else "",
            )
        }
        Spacer(Modifier.height(8.dp))

        // Email / Password / Confirm
        EmailField(
            value = state.email,
            onValueChange = onEmail,
            placeholder = "name@example.com",
            isError = state.email.isNotEmpty() && !state.emailValid,
            supportingText = if (state.email.isNotEmpty() && !state.emailValid) "Введите корректный e-mail" else "",
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(8.dp))
        PasswordField(
            value = state.password,
            label = "Password",
            onValueChange = onPassword,
            isError = state.password.isNotEmpty() && !state.passwordValid,
            supportingText = if (state.password.isNotEmpty() && !state.passwordValid) "Минимум 8 символов" else "",
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(8.dp))
        PasswordField(
            value = state.confirm,
            label = "Confirm password",
            onValueChange = onConfirm,
            isError = state.confirm.isNotEmpty() && !state.confirmValid,
            supportingText = if (state.confirm.isNotEmpty() && !state.confirmValid) "Пароли не совпадают" else "",
            imeAction = ImeAction.Done,
            onImeAction = onSignup
        )

        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = if (state.loading) "Creating..." else "Create account",
            enabled = state.canSubmit,
            onClick = onSignup
        )

        TextButton(onClick = onBack) {
            Text("Already have an account? Log in")
        }
    }
}

/**
 * Универсальный полевой компонент, если нет своего TextField
 * (Можно заменить на твой EmailField/PasswordField, тут — как запасной вариант)
 */
@Composable
private fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    supportingText: String = "",
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    imeAction: ImeAction = ImeAction.Next,
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        supportingText = { if (supportingText.isNotEmpty()) Text(supportingText) },
        modifier = modifier,
        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        singleLine = true
    )
}

@Preview
@Composable
fun SignupPreview() {
    AppTheme {
        SignupContent(SignupUiState())
    }
}
