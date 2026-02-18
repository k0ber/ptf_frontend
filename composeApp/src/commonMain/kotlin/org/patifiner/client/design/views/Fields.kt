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
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.design.icons.ptficons.IcPassword
import org.patifiner.client.design.icons.ptficons.IcVisibilityOff
import org.patifiner.client.design.icons.ptficons.IcVisibilityOn

@Composable
fun PtfTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    autofocus: Boolean = false,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(autofocus) { if (autofocus) focusRequester.requestFocus() }

    OutlinedTextField(
        modifier = modifier.focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        singleLine = singleLine,
        isError = isError,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = MaterialTheme.colorScheme.error,
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = if (label.isNotEmpty()) { { FieldLabel(label) } } else null,
        placeholder = if (placeholder.isNotEmpty()) { { FieldPlaceholder(placeholder) } } else null,
        supportingText = if (supportingText.isNotEmpty()) { { FieldSupportingText(supportingText) } } else null,
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
    placeholder: String = "name@example.com",
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    label: String = "E-mail",
    autofocus: Boolean = false,
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
        autofocus = autofocus,
        isError = isError,
        supportingText = supportingText,
        imeAction = imeAction,
        onImeAction = onImeAction,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(PtfIcons.IcEmail),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
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

    PtfTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        label = label,
        placeholder = placeholder,
        autofocus = autofocus,
        isError = isError,
        supportingText = supportingText,
        imeAction = imeAction,
        onImeAction = onImeAction,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
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
        }
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
        Text("Normal state", style = MaterialTheme.typography.titleMedium)
        PtfTextField(
            value = "",
            onValueChange = {},
            label = "Username",
            placeholder = "Enter your username"
        )

        Text("Filled state", style = MaterialTheme.typography.titleMedium)
        PtfTextField(
            value = "John Doe",
            onValueChange = {},
            label = "Full Name"
        )

        Text("Error state", style = MaterialTheme.typography.titleMedium)
        PtfTextField(
            value = "invalid_input",
            onValueChange = {},
            label = "Username",
            isError = true,
            supportingText = "Username is already taken"
        )

        Text("Disabled state", style = MaterialTheme.typography.titleMedium)
        PtfTextField(
            value = "Cannot edit this",
            onValueChange = {},
            label = "ID",
            enabled = false
        )

        Text("Email Field", style = MaterialTheme.typography.titleMedium)
        EmailField(
            value = "test@example.com",
            onValueChange = {}
        )

        Text("Password Field", style = MaterialTheme.typography.titleMedium)
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
    PtfTheme { FieldsPreview() }
}

@Preview
@Composable
fun FieldsPreviewDark() {
    PtfTheme(forceDarkMode = true) { FieldsPreview() }
}
