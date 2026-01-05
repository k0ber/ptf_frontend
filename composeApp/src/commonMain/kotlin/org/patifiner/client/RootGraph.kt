package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.russhwolf.settings.Settings
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import org.patifiner.client.binds.BindsCommon
import org.patifiner.client.binds.BindsNetwork
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.login.SignupUseCase
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.main.MainGraph

object AppScope
object LoggedInScope

@DependencyGraph(AppScope::class)
interface RootGraph {
    val commonBinds: BindsCommon
    val networkObserver: NetworkObserver
    val signupUseCase: SignupUseCase
    val loginUseCase: LoginUseCase
    val logoutUseCase: LogoutUseCase
    val authRepo: AuthRepository
    val http: HttpClient

    val mainGraphFactory: MainGraph.Factory

    fun rootComponent(): RootComponent

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Includes common: BindsCommon,
            @Includes network: BindsNetwork,
            @Provides networkObserver: NetworkObserver,
            @Provides apiConfig: ApiConfig,
            @Provides engine: HttpClientEngineFactory<*>,
            @Provides settings: Settings,
            @Provides appMainScope: CoroutineScope,
            @Provides componentContext: ComponentContext,
        ): RootGraph
    }
}
