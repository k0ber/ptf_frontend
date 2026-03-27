package org.patifiner.client.root.main.intro.user

import org.patifiner.client.root.main.data.UpdateUserRequest

fun UserInfoState.toUpdateProfileRequest() = UpdateUserRequest(
    name = user.name.takeIf { it.isNotBlank() },
    birthDate = user.birthDate?.toString(),
    gender = user.gender,
    cityId = user.cityId,
    languages = user.languages
)
