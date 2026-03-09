package org.patifiner.client

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import org.patifiner.client.base.PtfLog
import org.patifiner.client.base.componentScope
import org.patifiner.client.design.BackableComponent
import org.patifiner.client.design.PtfAnim
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.main.MainComponent
import org.patifiner.client.signup.SignupComponent

@Serializable sealed interface RootConfig {
    @Serializable data object Empty : RootConfig
    @Serializable data object Main : RootConfig
    @Serializable data object Login : RootConfig
    @Serializable data object Signup : RootConfig
}

@Stable sealed interface RootChild {
    data object Empty : RootChild
    data class Login(val component: LoginComponent) : RootChild
    data class Signup(val component: SignupComponent) : RootChild
    data class Main(val component: MainComponent) : RootChild
}

class RootComponent(componentContext: ComponentContext) : ComponentContext by componentContext, BackableComponent, KoinComponent {
//    private val authRepo: AuthRepository by inject()
//    private val networkObserver: NetworkObserver by inject()
//    private val navigation = StackNavigation<RootConfig>()
//    val navigationAnimation = PtfAnim.provideStackAnimation(this)
//
//    val isOnline: StateFlow<Boolean> = MutableStateFlow(true) //networkObserver.isOnline
//
//    @OptIn(DelicateDecomposeApi::class)
//    val stack: Value<ChildStack<RootConfig, RootChild>> = childStack(
//        source = navigation,
//        serializer = RootConfig.serializer(),
//        initialConfiguration = RootConfig.Empty,
//        handleBackButton = true,
//        childFactory = { config, componentContext ->
//            return@childStack when (config) {
//                RootConfig.Empty -> RootChild.Empty
//
//                RootConfig.Login -> RootChild.Login(
//                    LoginComponent(
//                        componentContext = componentContext,
//                        navToSignup = { navigation.push(RootConfig.Signup) }
//                    )
//                )
//
//                RootConfig.Signup -> RootChild.Signup(
//                    SignupComponent(
//                        componentContext = componentContext,
//                        onNavBack = { navigation.pop() }
//                    )
//                )
//
//                RootConfig.Main -> {
//                    val sessionScope = getKoin().getOrCreateScope(
//                        "session_id",
//                        named("LoggedInScope")
//                    )
//                    componentContext.lifecycle.doOnDestroy { sessionScope.close() }
//                    RootChild.Main(
//                        MainComponent(
//                            componentContext = componentContext,
//                            koinScope = sessionScope
//                        )
//                    )
//                }
//            }
//        }
//    )

    init {
//        authRepo.tokenFlow.onEach { token ->
//            PtfLog.d { "Root handles token change event" }
//            navigation.replaceAll(
//                if (token != null) RootConfig.Main else RootConfig.Login
//            )
//        }.launchIn(componentScope)
    }

    override fun back() {}// = navigation.pop()

}
