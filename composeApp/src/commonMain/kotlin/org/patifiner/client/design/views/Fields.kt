package org.patifiner.client.design.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.icons.IcEmail
import org.patifiner.client.design.icons.IcPassword
import org.patifiner.client.design.icons.IcVisibilityOff
import org.patifiner.client.design.icons.IcVisibilityOn
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.email_label
import patifinerclient.composeapp.generated.resources.email_placeholder
import patifinerclient.composeapp.generated.resources.hide_password
import patifinerclient.composeapp.generated.resources.password_label
import patifinerclient.composeapp.generated.resources.show_password

@Composable
fun PtfTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,

    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,

    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {}
) {
    val labelComposable: (@Composable () -> Unit)? = remember(label) {
        if (label != null) {
            { FieldLabel(label) }
        } else null
    }

    val placeholderComposable: (@Composable () -> Unit)? = remember(placeholder) {
        if (placeholder != null) {
            { FieldPlaceholder(placeholder) }
        } else null
    }

    val supportingComposable: (@Composable () -> Unit)? = remember(supportingText, isError) {
        if (supportingText != null) {
            { FieldSupportingText(supportingText, isError) }
        } else null
    }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        isError = isError,

        shape = RoundedCornerShape(14.dp),

        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = colorScheme.primary.copy(alpha = 0.6f),
            focusedBorderColor = colorScheme.primary,
            errorBorderColor = colorScheme.error,
        ),

        textStyle = typography.bodyLarge,

        label = labelComposable,
        placeholder = placeholderComposable,
        supportingText = supportingComposable,

        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,

        keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() },
            onNext = { onImeAction() },
            onSearch = { onImeAction() },
            onSend = { onImeAction() },
            onGo = { onImeAction() }
        ),
        visualTransformation = visualTransformation
    )
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = stringResource(Res.string.email_label),
    placeholder: String = stringResource(Res.string.email_placeholder),
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    PtfTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        isError = isError,
        supportingText = supportingText,
        imeAction = imeAction,
        onImeAction = onImeAction,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = {
            Icon(
                imageVector = IcEmail,
                contentDescription = null,
                tint = colorScheme.primary
            )
        }
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = stringResource(Res.string.password_label),
    placeholder: String = "",
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String = "",
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }

    PtfTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        isError = isError,
        supportingText = supportingText,
        imeAction = imeAction,
        onImeAction = onImeAction,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                imageVector = IcPassword,
                contentDescription = null,
                tint = colorScheme.primary
            )
        },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) IcVisibilityOn else IcVisibilityOff,
                    contentDescription = stringResource(if (visible) Res.string.hide_password else Res.string.show_password),
                    tint = colorScheme.primary
                )
            }
        }
    )
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = colorScheme.secondary,
        style = typography.labelLarge,
        fontSize = 14.sp
    )
}

@Composable
private fun FieldPlaceholder(text: String) {
    Text(
        text = text,
        color = colorScheme.outline.copy(alpha = 0.7f),
        style = typography.bodyMedium,
        fontSize = 14.sp,
        maxLines = 1,
        softWrap = false
    )
}

@Composable
private fun FieldSupportingText(text: String, isError: Boolean) {
    Text(
        text = text,
        color = if (isError) colorScheme.error else colorScheme.outline,
        style = typography.bodySmall,
        fontSize = 12.sp,
        maxLines = 2
    )
}

//================================================================================================================

@Composable
fun FieldsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PtfText("Normal state")
        PtfTextField(
            value = "",
            onValueChange = {},
            label = "Username",
            placeholder = "Enter your username"
        )
        PtfText("Filled state")
        PtfTextField(
            value = "John Doe",
            onValueChange = {},
            label = "Full Name"
        )
        PtfText("Error state")
        PtfTextField(
            value = "invalid_input",
            onValueChange = {},
            label = "Username",
            isError = true,
            supportingText = "Username is already taken"
        )
        PtfText("Disabled state")
        PtfTextField(
            value = "Cannot edit this",
            onValueChange = {},
            label = "ID",
            enabled = false
        )
        PtfText("Email Field")
        EmailField(
            value = "test@example.com",
            onValueChange = {}
        )
        PtfText("Password Field")
        PasswordField(
            value = "password123",
            onValueChange = {},
            label = "Password"
        )
        Spacer(Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun FieldsPreviewLight() {
    PtfPreview { FieldsPreview() }
}

@Preview
@Composable
fun FieldsPreviewDark() {
    PtfPreview(forceDarkMode = true) { FieldsPreview() }
}
