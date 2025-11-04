package org.patifiner.client.di.binds

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
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
import io.ktor.http.URLProtocol
import io.ktor.http.append
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.patifiner.client.common.ApiException
import org.patifiner.client.common.ErrorResponse
import org.patifiner.client.di.AppScope
import org.patifiner.client.di.LoggedInScope
import org.patifiner.client.login.data.TokenStorage

@ContributesTo(AppScope::class)
@ContributesTo(LoggedInScope::class)
interface BindsNetwork {

    @Provides
    @SingleIn(AppScope::class)
    fun provideHttp(
        engine: HttpClientEngineFactory<*>,
        json: Json,
        tokenStorage: TokenStorage,
    ): HttpClient = HttpClient(engine) {
        expectSuccess = false
        install(ContentNegotiation) { json(json) }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(message, tag = "Ktor")
                }
            }
            level = LogLevel.ALL
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTP
                host = "10.0.2.2"
                port = 8080
            }
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

    @Provides
    @SingleIn(AppScope::class)
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
        isLenient = true
    }
}