package org.patifiner.client.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.patifiner.client.Platform
import org.patifiner.client.design.PtfAnimations
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.views.PtfAlert
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.no_connection

@Composable
@OptIn(KoinExperimentalAPI::class)
fun RootScreen() {

    val navigator: RootNavigator = koinInject()

    val isOnline by Platform.networkObserver().isOnline.collectAsStateWithLifecycle()
    val snackbarHost = remember { SnackbarHostState() }

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { addPlatformFileSupport() }
            .crossfade(true)
            .build()
    }

    CompositionLocalProvider(RootSnackbarHost provides snackbarHost) {
        PtfTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Box(Modifier.fillMaxSize()) {
                    NavDisplay(
                        backStack = navigator.backStack,
                        onBack = { navigator.pop() },
                        entryProvider = koinEntryProvider(),
                        transitionSpec = PtfAnimations.screenForwardSpec,
                        popTransitionSpec = PtfAnimations.screenBackwardSpec,
                        predictivePopTransitionSpec = PtfAnimations.screenPredictiveBackward(),
                        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
                    )

                    if (!isOnline) {
                        PtfAlert(
                            text = stringResource(Res.string.no_connection),
                            modifier = Modifier.statusBarsPadding()
                        )
                    }
                }
            }
        }
    }
}

val RootSnackbarHost =
    staticCompositionLocalOf<SnackbarHostState> { error("No SnackbarHostState provided  [root]") }
