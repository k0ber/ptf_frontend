package org.patifiner.client.signup

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.patifiner.client.base.Gender
import org.patifiner.client.base.TokenResponse
import org.patifiner.client.base.UserDto
import org.patifiner.client.base.UserLanguage

@Serializable
data class SignupRequest(
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val email: String,
    val password: String
)

@Serializable data class CreateUserRequest(val name: String, val email: String, val password: String)
@Serializable data class UserCreatedResponse(val userInfo: UserDto, val token: TokenResponse)

@Serializable data class UpdateUserRequest(
    val name: String? = null,
    val birthDate: String? = null,
    val gender: Gender = Gender.NOT_SPECIFIED,
    val cityId: Long? = null,
    val languages: List<UserLanguage> = emptyList(),
    val locale: String? = null
)

@Serializable data class SetMainPhotoRequest(val url: String)
@Serializable data class DeletePhotoRequest(val url: String)
