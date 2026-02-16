package org.patifiner.client.base

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import io.ktor.client.plugins.ResponseException
import io.ktor.http.HttpStatusCode

@kotlinx.serialization.Serializable
data class ErrorResponse(val code: String, val message: String)

open class ApiException(val status: HttpStatusCode, val error: ErrorResponse) : Exception(error.message)

//fun Throwable.toUserMessage(default: String? = null): String = when (this) {
//    is ConnectTimeoutException -> "Server is not responding. Check your connection"
//    is ApiException -> message ?: default ?: "Server error: $status"
//    else -> default ?: "Something went wrong.. Try again later"
//}

fun Throwable.toUserMessage(fallback: String = "An error occurred"): String {
    return when (this) {
        is ApiException -> this.error.message
        is ResponseException -> "Network error: ${this.response.status.value}"
        else -> this.message ?: fallback
    }
}

suspend fun SnackbarHostState.showError(message: String) =
    showSnackbar(message = message, duration = SnackbarDuration.Long)

class UnauthorizedException(error: ErrorResponse) : ApiException(HttpStatusCode.Unauthorized, error)
class NotFoundException(error: ErrorResponse) : ApiException(HttpStatusCode.NotFound, error)
class ServerErrorException(status: HttpStatusCode, error: ErrorResponse) : ApiException(status, error)
