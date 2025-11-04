package org.patifiner.client.di

import com.russhwolf.settings.Settings
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import org.patifiner.client.di.binds.BindsCommon
import org.patifiner.client.di.binds.BindsNetwork
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.login.SignupUseCase
import org.patifiner.client.login.data.AuthRepository

object AppScope
object LoggedInScope

@DependencyGraph(AppScope::class)
interface AppGraph : LoggedInGraph.Factory {
    val signupUseCase: SignupUseCase
    val loginUseCase: LoginUseCase
    val logoutUseCase: LogoutUseCase
    val authRepo: AuthRepository
    val http: HttpClient

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides engine: HttpClientEngineFactory<*>,
            @Provides settings: Settings,
            @Includes common: BindsCommon,
            @Includes network: BindsNetwork,
        ): AppGraph
    }
}
