package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandler
import org.patifiner.client.di.AppGraph
import org.patifiner.client.login.LoginComponent
import org.patifiner.client.signup.SignupComponent

class AuthComponent(
    componentContext: ComponentContext,
    private val appGraph: AppGraph,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    val stack: Value<com.arkivanov.decompose.router.stack.Stack<Config>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Login, // Начальный экран - Логин
            handleBackButton = true,
            childFactory = ::child,
        )

    val backHandler: BackHandler
        get() = backHandler

    fun push(config: Config) {
        navigation.push(config)
    }

    fun pop() {
        navigation.pop()
    }

    private fun child(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            is Config.Login -> Child.Login(
                appGraph.loginComponentFactory.create(
                    componentContext = componentContext,
                    onLoginSuccess = ::openApp,
                    onNavigateToSignup = { navigation.push(Config.Signup) }
                )
            )
            is Config.Signup -> Child.Signup(
                appGraph.signupComponentFactory.create(
                    componentContext = componentContext,
                    onSignupSuccess = ::openShell, // 🔑 Переход в Shell после успешной регистрации
                    onNavigateToLogin = { navigation.pop() }
                )
            )
            is Config.App -> Child.App( // 🔑 НОВЫЙ Child для AppShellComponent
                AppShellComponent(
                    componentContext = componentContext,
                    appGraph = appGraph,
                    onLogout = { navigation.replaceAll(Config.Login) } // При логауте возвращаемся на Login
                )
            )
        }
    }

    // Приватная функция для перехода к оболочке
    private fun openApp() {
        navigation.replaceAll(Config.App)
    }

    sealed interface Child {
        data class Login(val component: LoginComponent) : Child
        data class Signup(val component: SignupComponent) : Child
        data class App(val component: AppShellComponent) : Child
    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Login : Config
        @Parcelize
        data object Signup : Config
        @Parcelize
        data object App : Config // 🔑
    }
}