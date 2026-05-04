package org.patifiner.client.design.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.ptfScrollable

const val AVATAR_SIZE = 120

@Composable
fun PtfScreenContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.ptfScrollable(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.statusBarsPadding())
        content()
        Spacer(Modifier.navigationBarsPadding()) // not needed for tabs
    }
}

@Composable
fun PtfScaffold(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
//    containerColor: Color = Color.Transparent,
    navContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
//  GradientBackground(modifier = modifier.fillMaxSize()) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorScheme.background,  // Color.Transparent,// containerColor,
        contentWindowInsets = WindowInsets(0.dp),
        snackbarHost = { PtfSnackbarHost(snackbarHostState) },
        bottomBar = navContent, //  // if (isWideScreen) Rail else BottomBar ?
        content = content
    )
//  }
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

//region preview
@Composable
fun ContainersPreview() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        PtfAvatar(source = null, onClick = {})
        PhotoThumbnail("") {}
    }
}

@Preview
@Composable
fun PtfScaffoldPreview() {
    PtfTheme {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(
                message = "Preview snackbar message",
                duration = SnackbarDuration.Indefinite
            )
        }
        PtfScaffold(snackbarHostState = hostState) {
            ContainersPreview()
        }
    }
}

@Preview
@Composable
fun PtfScaffoldPreviewDark() {
    PtfTheme(forceDarkMode = true) {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showSnackbar(
                message = "Preview snackbar message",
                duration = SnackbarDuration.Indefinite
            )
        }
        PtfScaffold(modifier = Modifier.fillMaxSize(), snackbarHostState = hostState) {
            ContainersPreview()
        }
    }
}
//endregion
