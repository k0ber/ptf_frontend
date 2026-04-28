package org.patifiner.client

import com.skydoves.compose.stability.runtime.ComposeStabilityAnalyzer
import com.skydoves.compose.stability.runtime.RecompositionEvent
import com.skydoves.compose.stability.runtime.RecompositionLogger
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.patifiner.client.core.createHttpClient
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.login.data.SessionManager
import org.patifiner.client.root.login.data.SessionStorage
import org.patifiner.client.root.main.mainModule
import org.patifiner.client.root.main.mainNavigationModule
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

        // default logs are verbose
//        ComposeStabilityAnalyzer.setLogger(object : RecompositionLogger {
//            override fun log(event: RecompositionEvent) { }
//        })

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
            mainNavigationModule,
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

    single(named(UNAUTH_CLIENT), createdAtStart = true) {
        createHttpClient(engine = get(), json = get(), config = get(), sessionManager = null)
    }

    single<SessionStorage>(createdAtStart = true) { SessionStorage(settings = get()) }
    single(createdAtStart = true) { SessionManager(sessionStorage = get()) }
    single(createdAtStart = true) {
        AuthRepository(
            unauthClient = get(named(UNAUTH_CLIENT)),
            sessionManager = get()
        )
    }
}
