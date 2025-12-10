package org.patifiner.client

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.zacsweers.metro.createGraphFactory
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.childrenAnimation
import org.patifiner.client.design.views.AppScaffold
import org.patifiner.client.di.AppGraph
import org.patifiner.client.di.binds.BindsCommon
import org.patifiner.client.di.binds.BindsNetwork
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.login.ui.LoginScreen
import org.patifiner.client.profile.ProfileComponent
import org.patifiner.client.profile.ui.ProfileScreen
import org.patifiner.client.signup.SignupComponent
import org.patifiner.client.signup.ui.SignupScreen
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.topics.ui.AddUserTopicScreen
import org.patifiner.client.viewing.UserTopicsComponent
import org.patifiner.client.viewing.UserTopicsScreen

// TODO ITERATION [2]
// TODO: BOTTOM NAV BAR with navigation
// TODO: ACTION BAR (for some screens might be needed
// TODO: PROFILE WITH IMAGE UPLOAD

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class, ExperimentalSharedTransitionApi::class)
@Composable
//@Preview
fun App(componentContext: DefaultComponentContext) {
    AppTheme {
        val appGraph = remember {
            createGraphFactory<AppGraph.Factory>().create(
                engine = Platform.engineFactory(),
                settings = Platform.settings(),
                common = object : BindsCommon {},
                network = object : BindsNetwork {},
            )
        }
//        val lifecycle = remember { LifecycleRegistry() }
//        val componentContext = remember { DefaultComponentContext(lifecycle) }
        val root = remember { RootComponent(componentContext, appGraph) }
        val snackbarHost = remember { SnackbarHostState() }

        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {

                AppScaffold(snackbarHostState = snackbarHost) {

                    Children(
                        stack = root.stack,
                        animation = childrenAnimation(root),
                    ) { child ->
                        when (val instance = child.instance) {
                            is ProfileComponent -> ProfileScreen(instance, snackbarHost)
                            is LoginComponent -> LoginScreen(instance, snackbarHost)
                            is SignupComponent -> SignupScreen(instance, snackbarHost)

                            is UserTopicsComponent -> UserTopicsScreen(instance, snackbarHost)
                            is AddUserTopicComponent -> AddUserTopicScreen(instance, snackbarHost)
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
