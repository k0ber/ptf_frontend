package org.patifiner.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.PtfAnim
import org.patifiner.client.design.views.AppScaffold
import org.patifiner.client.design.views.PtfWarningText
import org.patifiner.client.login.ui.LoginScreen
import org.patifiner.client.main.MainScreen
import org.patifiner.client.signup.ui.SignupScreen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun RootScreen(rootComponent: RootComponent) {
    val isOnline by rootComponent.isOnline.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }

    AppTheme {
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                AppScaffold(snackbarHostState = snackbarHost) {
                    Column {
                        AnimatedVisibility(visible = !isOnline) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth()
                                    .height(32.dp)
                                    .background(MaterialTheme.colorScheme.errorContainer)
                            ) {
                                PtfWarningText("No Internet Connection")
                            }
                        }
                        Children(
                            stack = rootComponent.stack,
                            animation = PtfAnim.predictiveBack(rootComponent)
                        ) { child ->
                            when (val instance = child.instance) {
                                is RootChild.Main -> MainScreen(instance.component, snackbarHost)
                                is RootChild.Login -> LoginScreen(instance.component, snackbarHost)
                                is RootChild.Signup -> SignupScreen(instance.component, snackbarHost)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope> {
    error("SharedTransitionScope not provided")
}
