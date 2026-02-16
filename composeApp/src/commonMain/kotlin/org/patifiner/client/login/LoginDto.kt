package org.patifiner.client.login

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

// todo: update from backend
@Serializable
data class TokenRequest(val email: String, val password: String)

@Serializable
data class TokenResponse(val token: String)

@Serializable
class UserInfoDto(
    val id: Long,
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val email: String,
)
