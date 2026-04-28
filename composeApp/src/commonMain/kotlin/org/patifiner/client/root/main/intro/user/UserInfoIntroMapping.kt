package org.patifiner.client.root.main.intro.user

import org.patifiner.client.core.Gender
import org.patifiner.client.core.UserDto
import org.patifiner.client.root.main.data.UpdateUserRequest

fun UserDto.toUpdateProfileRequest() = UpdateUserRequest(
    name = name.takeIf { it.isNotBlank() },
    birthDate = birthDate?.toString(),
    gender = gender,
    cityId = cityId,
    languages = languages
)

val UserDto.avatarUrl: String?
    get() = photos.firstOrNull()

fun UserDto.Companion.empty() = UserDto(
    id = 0,
    name = "",
    photos = emptyList(),
    birthDate = null,
    email = "",
    gender = Gender.NOT_SPECIFIED,
    locale = "en"
)
