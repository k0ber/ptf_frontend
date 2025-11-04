package org.patifiner.client.signup

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val email: String,
    val password: String
)
