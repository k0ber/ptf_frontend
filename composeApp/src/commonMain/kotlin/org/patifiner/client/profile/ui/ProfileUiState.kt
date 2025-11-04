package org.patifiner.client.profile.ui

import androidx.compose.runtime.Immutable
import org.patifiner.client.login.UserInfoDto

@Immutable
data class ProfileUiState(
    val loading: Boolean = false,
    val userInfoDto: UserInfoDto? = null,
    val error: String? = null
)
