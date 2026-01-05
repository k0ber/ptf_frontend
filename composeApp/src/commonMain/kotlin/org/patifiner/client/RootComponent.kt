package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import org.patifiner.client.common.componentScope
import org.patifiner.client.design.BackableComponent
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.main.MainComponent
import org.patifiner.client.signup.SignupComponent

@Serializable sealed interface RootConfig {
    @Serializable data object Main : RootConfig
    @Serializable data object Login : RootConfig
    @Serializable data object Signup : RootConfig
}

sealed interface RootChild {
    data class Login(val component: LoginComponent) : RootChild
    data class Signup(val component: SignupComponent) : RootChild
    data class Main(val component: MainComponent) : RootChild
}

@Inject
class RootComponent(componentContext: ComponentContext, private val rootGraph: RootGraph) : ComponentContext by componentContext, BackableComponent {
    private val authRepo = rootGraph.authRepo
    private val mainGraphFactory = rootGraph.mainGraphFactory
    private val navigation = StackNavigation<RootConfig>()
    private val scope = componentScope()

    val isOnline: StateFlow<Boolean> = rootGraph.networkObserver.isOnline

    val stack: Value<ChildStack<RootConfig, RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = if (authRepo.tokenFlow.value != null) RootConfig.Main else RootConfig.Login,
        handleBackButton = true,
        childFactory = ::createChild
    )

    init {
        authRepo.tokenFlow.onEach { token ->
            val showMain = token != null && stack.value.active.configuration !is RootConfig.Main
            navigation.replaceAll(if (showMain) RootConfig.Main else RootConfig.Login)
        }.launchIn(scope)
    }

    private fun createChild(config: RootConfig, componentContext: ComponentContext): RootChild {
        return when (config) {
            is RootConfig.Login -> RootChild.Login(
                component = LoginComponent(
                    componentContext = componentContext,
                    login = { req -> rootGraph.loginUseCase(req) },
                    navToSignup = { navigation.replaceAll(RootConfig.Signup) }
                )
            )

            is RootConfig.Signup -> RootChild.Signup(
                SignupComponent(
                    componentContext = componentContext,
                    createUser = { req -> rootGraph.signupUseCase(req) },
                    navigateBackToLogin = { navigation.replaceAll(RootConfig.Login) }
                )
            )

            is RootConfig.Main -> {
                val mainGraph = mainGraphFactory.create(common = rootGraph.commonBinds)
                val mainComponent = mainGraph.mainComponentFactory().create(
                    componentContext = componentContext,
                )
                RootChild.Main(mainComponent)
            }
        }
    }

    override fun back() = navigation.pop()

}
