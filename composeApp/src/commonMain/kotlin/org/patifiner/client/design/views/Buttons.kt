package org.patifiner.client.design.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.design.AppTheme


@Composable
fun ButtonsPreview() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryButton(text = "Login with E-mail", enabled = true, modifier = Modifier, onClick = {})
        Spacer(modifier = Modifier.height(8.dp))
        PrimaryButton(text = "Login with E-mail", enabled = false, modifier = Modifier, onClick = {})
    }
}

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
            defaultElevation = 12.dp,
            pressedElevation = 14.dp,
            disabledElevation = 8.dp
        ),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.surface,//).copy(alpha = 0.8f),
            disabledContentColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.9f),
        ),
//        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 14.sp
        )
    }
}

//
////@Composable
////fun GhostButton(
////    onClick: () -> Unit,
////    modifier: Modifier = Modifier,
////    content: @Composable () -> Unit
////) {
////    val sp = LocalSpacing.current
////    OutlinedButton(
////        onClick = onClick,
////        modifier = modifier,
////        shape = MaterialTheme.shapes.medium,
////        contentPadding = PaddingValues(start = sp.lg.dp, end = sp.lg.dp, top = sp.sm.dp, bottom = sp.sm.dp)
////    ) { content() }
////}
//
//@Preview
//@Composable
//fun ButtonsLightPreview() {
//    AppTheme(forceDarkMode = false) {
//        Row(Modifier.padding(20.dp)) {
//            PrimaryButton(onClick = {}) {}
////            GhostButton({}) {}
//        }
//    }
//}
//
//@Preview
//@Composable
//fun ButtonsDarkPreview() {
//    AppTheme(forceDarkMode = true) {
//        Row(Modifier.padding(20.dp)) {
//            PrimaryButton(onClick = {}) {}
////            GhostButton({}) {}
//        }
//    }
//}

@Preview
@Composable
fun ButtonsPreviewLight() {
    Column {
        AppTheme(forceDarkMode = false) { ButtonsPreview() }
    }
}

@Preview
@Composable
fun ButtonsPreviewDark() {
    Column {
        AppTheme(forceDarkMode = true) { ButtonsPreview() }
    }
}
