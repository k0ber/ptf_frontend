package org.patifiner.client

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.zacsweers.metro.createGraphFactory
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patifiner.client.common.iosParallaxScale
import org.patifiner.client.design.AppTheme
import org.patifiner.client.design.views.AppScaffold
import org.patifiner.client.di.AppGraph
import org.patifiner.client.di.binds.BindsCommon
import org.patifiner.client.di.binds.BindsNetwork
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.login.ui.LoginScreen
import org.patifiner.client.profile.ProfileComponent
import org.patifiner.client.profile.ui.ProfileScreen
import org.patifiner.client.viewing.UserTopicsScreen
import org.patifiner.client.signup.SignupComponent
import org.patifiner.client.signup.ui.SignupScreen
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.viewing.UserTopicsComponent
import org.patifiner.client.topics.ui.AddUserTopicScreen

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
@Composable
@Preview
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
        AppScaffold(snackbarHostState = snackbarHost) {
            Children(
                stack = root.stack,
                animation = predictiveBackAnimation(
                    backHandler = root.backHandler,
                    onBack = root::pop,
                    fallbackAnimation = stackAnimation(slide() + iosParallaxScale) // iOS-ish
                ),                   // stackAnimation(slide() + scale()) // Material-ish
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
