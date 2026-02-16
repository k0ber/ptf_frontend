package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.patifiner.client.base.componentScope
import org.patifiner.client.design.BackableComponent
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.login.data.AuthRepository
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

class RootComponent(componentContext: ComponentContext) : ComponentContext by componentContext, BackableComponent, KoinComponent {
    private val authRepo: AuthRepository by inject()
    private val networkObserver: NetworkObserver by inject()
    private val navigation = StackNavigation<RootConfig>()

    val isOnline: StateFlow<Boolean> = networkObserver.isOnline

    val stack: Value<ChildStack<RootConfig, RootChild>> = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = if (authRepo.tokenFlow.value != null) RootConfig.Main else RootConfig.Login,
        handleBackButton = true,
        childFactory = { config, componentContext ->
            return@childStack when (config) {
                RootConfig.Login -> RootChild.Login(
                    LoginComponent(
                        componentContext = componentContext,
                        navToSignup = { navigation.replaceAll(RootConfig.Signup) }
                    )
                )

                RootConfig.Signup -> RootChild.Signup(
                    SignupComponent(
                        componentContext = componentContext,
                        navigateBackToLogin = { navigation.replaceAll(RootConfig.Login) }
                    )
                )

                RootConfig.Main -> {
                    val sessionScope = getKoin().getOrCreateScope("session_id", named("LoggedInScope"))
                    componentContext.lifecycle.doOnDestroy { sessionScope.close() }
                    RootChild.Main(MainComponent(componentContext = componentContext, koinScope = sessionScope))
                }

            }
        }
    )

    init {
        authRepo.tokenFlow.onEach { token ->
            val isMainActive = stack.value.active.configuration is RootConfig.Main
            if (token != null && !isMainActive) navigation.replaceAll(RootConfig.Main)
            else if (token == null && isMainActive) navigation.replaceAll(RootConfig.Login)
        }.launchIn(componentScope)
    }

    override fun back() = navigation.pop()

}
