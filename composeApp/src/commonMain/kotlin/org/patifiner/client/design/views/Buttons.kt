package org.patifiner.client.design.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.patifiner.client.design.PtfTheme

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp,
            disabledElevation = 2.dp
        ),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer,

            disabledContainerColor = colorScheme.surfaceVariant.copy(alpha = 0.75f),
            disabledContentColor = colorScheme.onSurfaceVariant.copy(alpha = 0.65f),
        ),
    ) {
        Text(
            text = text,
            style = typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            fontSize = 15.sp
        )
    }
}

// region Preview
@Composable
fun ButtonsPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(text = "Login with E-mail", enabled = true, modifier = Modifier, onClick = {})
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryButton(text = "Login with E-mail", enabled = false, modifier = Modifier, onClick = {})
    }
}

@Preview
@Composable
fun ButtonsPreviewLight() {
    PtfTheme(forceDarkMode = false) { ButtonsPreview() }
}

@Preview
@Composable
fun ButtonsPreviewDark() {
    PtfTheme(forceDarkMode = true) { ButtonsPreview() }
}
// endregion