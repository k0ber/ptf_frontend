package org.patifiner.client

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.patifiner.client.base.ApiException
import org.patifiner.client.base.ErrorResponse
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.login.LoginStore
import org.patifiner.client.login.LoginStoreFactory
import org.patifiner.client.login.LoginUseCase
import org.patifiner.client.login.LogoutUseCase
import org.patifiner.client.login.SignupUseCase
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.login.data.TokenStorage
import org.patifiner.client.login.data.TokenStorageImpl
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
        val engine: HttpClientEngineFactory<*> = get()
        val json: Json = get()
        val config: ApiConfig = get()
        val tokenStorage: TokenStorage = get()

        HttpClient(engine) {
            expectSuccess = false
            install(ContentNegotiation) { json(json) }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) = Napier.d(message, tag = "Ktor")
                }
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                url(config.baseUrl)
                url.port = config.port
                tokenStorage.token?.let { headers.append(HttpHeaders.Authorization, "Bearer $it") }
                headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
                accept(ContentType.Application.Json)
            }

            HttpResponseValidator {
                validateResponse { resp ->
                    if (!resp.status.isSuccess()) {
                        if (resp.status == HttpStatusCode.Unauthorized) {
                            tokenStorage.clear()
                        }
                        val raw = resp.runCatching { bodyAsText() }.getOrDefault("")
                        val parsed = runCatching { json.decodeFromString(ErrorResponse.serializer(), raw) }.getOrNull()
                        val error = parsed ?: ErrorResponse(resp.status.value.toString(), "Unrecognized server error")
                        throw ApiException(resp.status, error)
                    }
                }
            }
        }
    }

    single { AuthRepository(client = get(), tokenStorage = get()) }

    factoryOf(::LoginUseCase)
    factoryOf(::SignupUseCase)
    factoryOf(::LogoutUseCase)

    factory<LoginStore> { LoginStoreFactory(factory = get(), loginUseCase = get()).create() }

    // Session scope
    scope(named("LoggedInScope")) {
        scopedOf(::TopicsRepository)
        scopedOf(::LoadProfileUseCase)
        scopedOf(::LoadUserTopicsTreeUseCase)
        scopedOf(::SearchTopicsUseCase)
        scopedOf(::AddUserTopicUseCase)
    }
}
