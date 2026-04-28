package org.patifiner.client.root.main.profile

import androidx.compose.runtime.Immutable
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState
import org.patifiner.client.core.UserDto
import org.patifiner.client.root.main.intro.user.empty

@Immutable
data class ProfileState(
    val user: UserDto = UserDto.empty(),
    override val status: ScreenStatus = ScreenStatus.Idle
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus): ProfileState = copy(status = status)
}

sealed interface ProfileSideEffect {
    data class Error(val message: String) : ProfileSideEffect
    data object ProfileUpdated : ProfileSideEffect
}
