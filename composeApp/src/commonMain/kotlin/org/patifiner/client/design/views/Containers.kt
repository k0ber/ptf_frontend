package org.patifiner.client.design.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.icons.IcEmail
import org.patifiner.client.design.scrollableScreen

@Composable
fun PtfScreen(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    GradientBackground(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = scrollableScreen(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}

@Composable
fun PtfScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    GradientBackground(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0.dp),
            snackbarHost = { PtfSnackbarHost(snackbarHostState) },
            content = content
        )
    }
}

@Composable
fun PtfSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier.imePadding()
    ) { data ->
        Snackbar(
            shape = RoundedCornerShape(12.dp),
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface,
            actionColor = colorScheme.primary,
            dismissActionContentColor = colorScheme.primary,
            snackbarData = data
        )
    }
}

// todo
@Composable
fun PtfAvatar(
    url: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (url != null) {
            PtfText("Photo")
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = IcEmail,
                    contentDescription = null,
                    tint = colorScheme.primary
                )
                PtfText(
                    text = "Photo",
                    fontSize = 12
                )
            }
        }
    }
}

// =====================================================================================================
@Preview
@Composable
fun PtfScaffoldPreview() {
    PtfPreview {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(
                message = "Preview snackbar message",
                duration = SnackbarDuration.Indefinite
            )
        }
        PtfScaffold(modifier = Modifier.fillMaxSize(), snackbarHostState = hostState) {
        }
    }
}

@Preview
@Composable
fun PtfScaffoldPreviewDark() {
    PtfPreview(forceDarkMode = true) {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(
                message = "Preview snackbar message",
                duration = SnackbarDuration.Indefinite
            )
        }
        PtfScaffold(modifier = Modifier.fillMaxSize(), snackbarHostState = hostState) {
        }
    }
}
