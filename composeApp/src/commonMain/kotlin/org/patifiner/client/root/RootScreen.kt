package org.patifiner.client.root

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.patifiner.client.Platform
import org.patifiner.client.design.PtfTheme
import org.patifiner.client.design.views.PtfAlert
import org.patifiner.client.design.views.PtfScaffold
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.no_connection

private const val animDurationMs = 400

@Composable
@OptIn(KoinExperimentalAPI::class)
fun RootScreen() {

    val navigator: RootNavigator = koinInject()
    val isOnline by Platform.networkObserver().isOnline.collectAsStateWithLifecycle()
    val snackbarHost = remember { SnackbarHostState() }

    CompositionLocalProvider(RootSnackbarHost provides snackbarHost) {
        PtfTheme {
            PtfScaffold(snackbarHostState = snackbarHost) {
                Column {
                    if (!isOnline) {
                        PtfAlert(stringResource(Res.string.no_connection))
                    }
                    NavDisplay(
                        backStack = navigator.backStack,
                        onBack = { navigator.pop() },
                        entryProvider = koinEntryProvider(),
                        transitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(animDurationMs)
                            ) + fadeIn() togetherWith slideOutHorizontally(
                                targetOffsetX = { -it / 3 },
                                animationSpec = tween(animDurationMs)
                            ) + fadeOut()
                        },
                        popTransitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { -it / 3 },
                                animationSpec = tween(animDurationMs)
                            ) + fadeIn() togetherWith slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(animDurationMs)
                            ) + fadeOut()
                        },
                        predictivePopTransitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { -it / 3 },
                                animationSpec = tween(animDurationMs)
                            ) + fadeIn() togetherWith slideOutHorizontally(
                                targetOffsetX = { it },
                                animationSpec = tween(animDurationMs)
                            ) + fadeOut()
                        },
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                        )
                    )
                }
            }
        }
    }
}

val RootSnackbarHost =
    staticCompositionLocalOf<SnackbarHostState> { error("No SnackbarHostState provided") }
