package org.patifiner.client.base

import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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
import kotlinx.serialization.json.Json
import org.patifiner.client.ApiConfig
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.login.data.TokenStorage

fun createHttpClient(
    engine: HttpClientEngineFactory<*>,
    json: Json,
    config: ApiConfig,
    tokenStorage: TokenStorage,
    authRepositoryProvider: () -> AuthRepository // Используем лямбду, чтобы избежать циклической зависимости в Koin
): HttpClient {
    return HttpClient(engine) {
        expectSuccess = false

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) = Napier.d(message, tag = "Ktor")
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    tokenStorage.accessToken?.let { BearerTokens(it, tokenStorage.refreshToken ?: "") }
                }
                refreshTokens {
                    val result = authRepositoryProvider().refreshTokens()
                    result.getOrNull()?.let { BearerTokens(it.accessToken, it.refreshToken) }
                }
                sendWithoutRequest { request ->
                    request.attributes.getOrNull(AuthRequiredKey) ?: true
                }
            }
        }

        install(DefaultRequest) {
            url(config.baseUrl)
            url.port = config.port
            headers.append(HttpHeaders.ContentType, ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        HttpResponseValidator {
            validateResponse { resp ->
                if (!resp.status.isSuccess() && resp.status != HttpStatusCode.Unauthorized) {
                    val raw = resp.runCatching { bodyAsText() }.getOrDefault("")
                    val parsed = runCatching {
                        json.decodeFromString(ErrorResponse.serializer(), raw)
                    }.getOrNull()

                    val error = parsed ?: ErrorResponse(resp.status.value.toString(), "Unrecognized error")
                    throw ApiException(resp.status, error)
                }
            }
        }
    }
}
