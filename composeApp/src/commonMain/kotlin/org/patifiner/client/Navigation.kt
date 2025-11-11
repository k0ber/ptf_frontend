package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.patifiner.client.common.componentScope
import org.patifiner.client.di.AppGraph
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.profile.ProfileComponent
import org.patifiner.client.signup.SignupComponent
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.viewing.UserTopicsComponent

@Serializable
enum class ScreenSource { LOAD, LOGIN, SIGNUP }

@Serializable
enum class TopicAction { ADD }

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Signup : Screen

    @Serializable
    data class Profile(val source: ScreenSource) : Screen

    @Serializable
    data class UserTopics(val action: TopicAction) : Screen

    @Serializable
    data object AddUserTopic : Screen
}

class RootComponent(
    componentContext: ComponentContext,
    private val appGraph: AppGraph
) : ComponentContext by componentContext {
    private val scope = componentScope()
    private val nav = StackNavigation<Screen>()
    private val authRepo get() = appGraph.authRepo
    private val auth get() = requireNotNull(authRepo.loggedInGraph) // todo danger

    init {
        Napier.d { "RootComponent Init" }
        scope.launch {
            appGraph.authRepo.isLoggedFlow.collect { loggedIn ->
                nav.replaceAll(if (loggedIn) Screen.Profile(ScreenSource.LOGIN) else Screen.Login)
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    val stack = childStack(
        source = nav,
        serializer = Screen.serializer(),
        initialConfiguration = if (authRepo.isLogged) Screen.Profile(ScreenSource.LOAD) else Screen.Login,
        handleBackButton = true
    ) { screen, ctx ->
        when (screen) {
            Screen.Login -> LoginComponent(
                componentContext = ctx,
                login = { req -> appGraph.loginUseCase(req) },
                navToSignup = { nav.push(Screen.Signup) },
            )

            Screen.Signup -> SignupComponent(
                componentContext = ctx,
                createUser = { req -> appGraph.signupUseCase(req) },
                navigateBackToLogin = { nav.pop() },
                navigateToProfile = { nav.replaceAll(Screen.Profile(ScreenSource.SIGNUP)) },
            )

            is Screen.Profile -> ProfileComponent(
                componentContext = ctx,
                source = screen.source,
                loadProfile = { auth.loadProfile() },
                logout = { appGraph.logoutUseCase() },
                showMyTopics = { nav.push(Screen.UserTopics(TopicAction.ADD)) },
                addNewTopic = { nav.push(Screen.AddUserTopic) },
            )

            is Screen.UserTopics -> UserTopicsComponent(
                componentContext = ctx,
                repo = auth.topicsRepository,
                navigateToAdd = {} // TODO
            )

            is Screen.AddUserTopic -> AddUserTopicComponent(
                componentContext = ctx,
                loadUserTopicsTree = { auth.loadUserTopicsTreeUseCase() },
                searchTopics = { q, tree -> auth.searchTopics(q, tree) },
                addUserTopic = { topic, draft -> auth.addUserTopic(topic, draft) },
                onDone = {} // TODO
            )
        }
    }

    fun pop() {
        nav.pop()
    }
}
