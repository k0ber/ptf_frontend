package org.patifiner.client

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.patifiner.client.base.createHttpClient
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.login.LoginStore
import org.patifiner.client.login.LoginStoreFactory
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.login.data.TokenStorage
import org.patifiner.client.login.data.TokenStorageImpl
import org.patifiner.client.signup.SignupStore
import org.patifiner.client.signup.SignupStoreFactory
import org.patifiner.client.signup.SignupUseCase
import org.patifiner.client.topics.AddUserTopicUseCase
import org.patifiner.client.topics.LoadUserTopicsTreeUseCase
import org.patifiner.client.topics.SearchTopicsUseCase
import org.patifiner.client.topics.data.TopicsRepository

data class KoinAppConfig(
    val engine: HttpClientEngineFactory<*>,
    val apiConfig: ApiConfig,
    val appScope: CoroutineScope
)

fun initKoin(config: KoinAppConfig, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()

        Platform.initNapier()

        modules(appModule, module {
            single { config.engine }
            single { config.apiConfig }
            single { config.appScope }
            single { Platform.settings() }
            single { Platform.networkObserver() }
        })
    }

private val appModule = module {
    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            explicitNulls = false
            encodeDefaults = true
            isLenient = true
        }
    }

    single<TokenStorage> { TokenStorageImpl(settings = get()) }
    single<StoreFactory> { DefaultStoreFactory() }

    single {
        createHttpClient(
            engine = get(),
            json = get(),
            config = get(),
            tokenStorage = get(),
            authRepositoryProvider = { get() }
        )
    }

    single { AuthRepository(client = get(), tokenStorage = get()) }

    factoryOf(::LoginUseCase)
    factory<LoginStore> { LoginStoreFactory(factory = get(), loginUseCase = get()).create() }

    factory<SignupStore> { SignupStoreFactory(factory = get(), signupUseCase = get()).create() }
    factoryOf(::SignupUseCase)

    factoryOf(::LogoutUseCase)


    // Session scope
    scope(named("LoggedInScope")) {
        scopedOf(::TopicsRepository)
        scopedOf(::LoadProfileUseCase)
        scopedOf(::LoadUserTopicsTreeUseCase)
        scopedOf(::SearchTopicsUseCase)
        scopedOf(::AddUserTopicUseCase)
    }
}
