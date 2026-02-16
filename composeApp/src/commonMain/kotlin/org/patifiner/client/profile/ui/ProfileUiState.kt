package org.patifiner.client.profile.ui

import androidx.compose.runtime.Immutable
import org.patifiner.client.login.UserDto

@Immutable
data class ProfileUiState(
    val loading: Boolean = false,
    val userDto: UserDto? = null,
    val error: String? = null
)
