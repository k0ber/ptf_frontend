package org.patifiner.client.base

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable data class TokenRequest(val email: String, val password: String)
@Serializable data class TokenResponse(val accessToken: String, val refreshToken: String)
@Serializable data class RefreshTokenRequest(val refreshToken: String)

@Serializable class UserDto(
    val id: Long,
    val name: String,
    val avatarUrl: String? = null,
    val photos: List<String> = emptyList(),
    val birthDate: LocalDate?,
    val email: String,
    val cityId: Long? = null,
    val cityName: String? = null,
    val gender: Gender = Gender.NOT_SPECIFIED,
    val languages: List<UserLanguage> = emptyList(),
    val locale: String
)

@Serializable data class UserLanguage(val languageCode: String, val level: LanguageLevel)
@Serializable enum class LanguageLevel { BEGINNER, CONVERSATIONAL, FLUENT, NATIVE }
@Serializable enum class Gender { MALE, FEMALE, OTHER, NOT_SPECIFIED }
