package org.patifiner.client.common

import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode


fun Throwable.toUserMessage(default: String? = null): String = when (this) {
    is ConnectTimeoutException -> "Server is not responding. Check your connection"
    is ApiException -> message ?: default?: "Server error: $status"
    else -> default ?: "Something went wrong.. Try again later"
}

// server sends this in case of error
@kotlinx.serialization.Serializable
data class ErrorResponse(val code: String, val message: String)

open class ApiException(val status: HttpStatusCode, val error: ErrorResponse) : Exception(error.message)

class UnauthorizedException(error: ErrorResponse) : ApiException(HttpStatusCode.Unauthorized, error)
class NotFoundException(error: ErrorResponse) : ApiException(HttpStatusCode.NotFound, error)
class ServerErrorException(status: HttpStatusCode, error: ErrorResponse) : ApiException(status, error)