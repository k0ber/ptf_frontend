package org.patifiner.client

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.compose.getKoin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.patifiner.client.core.createHttpClient
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.login.data.SessionManager
import org.patifiner.client.root.login.data.SessionStorage
import org.patifiner.client.root.main.mainModule
import org.patifiner.client.root.rootModule

const val UNAUTH_CLIENT = "unauth_client"

data class KoinAppConfig(
    val engine: HttpClientEngineFactory<*>,
    val apiConfig: ApiConfig,
    val appScope: CoroutineScope,
    val isDev: Boolean,
)

fun initKoin(config: KoinAppConfig, appDeclaration: (KoinApplication.() -> Unit)? = null) =
    startKoin {

        appDeclaration?.invoke(this)

        modules(
            module {
                single { config }
                single { config.engine }
                single { config.apiConfig }
                single { config.appScope }
                single { Platform.settings() }
                single { Platform.networkObserver() }
                single { Platform.dispatchers() }
            },
            appModule,
            rootModule,
            mainModule,
        )
    }.also { Platform.onAppInit() }

val appModule = module {
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
            isLenient = true
        }
    }

    single(named(UNAUTH_CLIENT)) { createHttpClient(engine = get(), json = get(), config = get(), sessionManager = null) }

    single<SessionStorage> { SessionStorage(settings = get()) }
    single { SessionManager(sessionStorage = get()) }
    single {
        AuthRepository(
            unauthClient = get(named(UNAUTH_CLIENT)),
            sessionManager = get()
        )
    }

    // todo: move to view models?
    single<StoreFactory> {
        val base = DefaultStoreFactory()
        val config: KoinAppConfig = get()
        if (config.isDev) LoggingStoreFactory(base) else base
    }
}
