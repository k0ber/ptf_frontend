package org.patifiner.client.design.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.icons.ptficons.IcPassword
import org.patifiner.client.design.icons.ptficons.IcVisibilityOff
import org.patifiner.client.design.icons.ptficons.IcVisibilityOn

@Composable
fun FieldsPreview() {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val emailValid = remember(email) {
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(email)
    }
    val passValid = pass.length >= 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        EmailField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            placeholder = "name@example.com",
            onValueChange = { email = it },
            autofocus = true,
            isError = email.isNotEmpty() && !emailValid,
            supportingText = if (email.isNotEmpty() && !emailValid) "Введите корректный e-mail" else "",
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(12.dp))
        PasswordField(
            modifier = Modifier.fillMaxWidth(),
            value = pass,
            label = "Password",
            onValueChange = { pass = it },
            isError = pass.isNotEmpty() && !passValid,
            supportingText = if (pass.isNotEmpty() && !passValid) "Минимум 8 символов" else "",
            imeAction = ImeAction.Done,
        )
        Spacer(Modifier.height(16.dp))
        PrimaryButton(
            text = "login",
            onClick = { },
            enabled = emailValid && passValid,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    label: String = "E-mail",
    autofocus: Boolean = false,
    isError: Boolean = false,
    supportingText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(autofocus) { if (autofocus) focusRequester.requestFocus() }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary),

        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,

        textStyle = MaterialTheme.typography.bodyLarge,

        label = { FieldLabel(label) },
        placeholder = { FieldPlaceholder(placeholder) },
        supportingText = { FieldSupportingText(supportingText) },

        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(PtfIcons.IcEmail),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        isError = isError,

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() },
            onNext = { onImeAction() }
        ),
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    label: String,
    autofocus: Boolean = false,
    isError: Boolean = false,
    supportingText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(autofocus) { if (autofocus) focusRequester.requestFocus() }

    OutlinedTextField(
        modifier = modifier
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary),

        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = true,
        isError = isError,

        textStyle = MaterialTheme.typography.bodyLarge,

        label = { FieldLabel(label) },
        placeholder = { FieldPlaceholder(placeholder) },
        supportingText = { FieldSupportingText(supportingText) },

        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(PtfIcons.IcPassword),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) PtfIcons.IcVisibilityOn else PtfIcons.IcVisibilityOff,
                    contentDescription = if (visible) "Скрыть пароль" else "Показать пароль",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() },
            onNext = { onImeAction() }
        ),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
    )
}


@Composable
fun FieldLabel(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelLarge,
        fontSize = 16.sp
    )
}

@Composable
fun FieldPlaceholder(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 14.sp,
        maxLines = 1,
        softWrap = false
    )
}

@Composable
fun FieldSupportingText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.outline,
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 14.sp,
        maxLines = 1,
        softWrap = false
    )
}

//================================================================================================================
@Preview
@Composable
fun FieldsPreviewLight() {
    AppTheme { FieldsPreview() }
}

@Preview
@Composable
fun FieldsPreviewDark() {
    AppTheme(forceDarkMode = true) { FieldsPreview() }
}
