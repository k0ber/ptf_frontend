package org.patifiner.client.root.main.intro.user

import androidx.compose.runtime.Stable
import io.github.vinceglb.filekit.PlatformFile
import org.patifiner.client.core.ScreenStatus
import org.patifiner.client.core.StatusState
import org.patifiner.client.core.UserDto

@Stable
data class UserInfoIntroState(
    val user: UserDto = UserDto.empty(),
    override val status: ScreenStatus = ScreenStatus.Idle,
    val selectedLocalFile: PlatformFile? = null
) : StatusState {
    override fun copyWithStatus(status: ScreenStatus) = copy(status = status)
}

sealed interface UserInfoSideEffect {
    data class Error(val message: String) : UserInfoSideEffect
    data object Saved : UserInfoSideEffect
}
