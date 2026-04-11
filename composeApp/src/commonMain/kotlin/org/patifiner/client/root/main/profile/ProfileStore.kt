package org.patifiner.client.root.main.profile

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.PtfLog
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.intro.user.UserInteractor

interface ProfileStore : Store<ProfileIntent, ProfileState, ProfileLabel>

@Immutable
data class ProfileState(
    val userDto: UserDto? = null,
    override val isLoading: Boolean = false
) : BaseState<ProfileState> {
    override fun withLoading(isLoading: Boolean): ProfileState = copy(isLoading = isLoading)
}

class ProfileStoreFactory(
    private val factory: StoreFactory,
    private val userInteractor: UserInteractor,
) {
    fun create(): ProfileStore =
        object : ProfileStore, Store<ProfileIntent, ProfileState, ProfileLabel>
        by factory.createDefault(
            initialState = ProfileState(),
            executorFactory = coroutineExecutorFactory {
                onAction<ProfileAction.LoadInitial> {
                    PtfLog.d { "Profile Init" }
                    execute(
                        useCase = { userInteractor.loadProfile() },
                        loading = { withLoading(it) },
                        onSuccessData = { data -> copy(userDto = data) },
                        errorFactory = ProfileLabel::Error
                    )
                }

                onIntent<ProfileIntent.Refresh> {
                    execute(
                        useCase = { userInteractor.loadProfile() },
                        loading = { withLoading(it) },
                        onSuccessData = { data -> copy(userDto = data) },
                        errorFactory = ProfileLabel::Error
                    )
                }

                onIntent<ProfileIntent.Logout> {
                    userInteractor.logout()
                }
            },
            bootstrapper = coroutineBootstrapper { dispatch(ProfileAction.LoadInitial) }
        ) {}
}

sealed interface ProfileIntent {
    data object Refresh : ProfileIntent
    data object Logout : ProfileIntent
}

sealed interface ProfileLabel {
    data class Error(val message: String) : ProfileLabel
}

private sealed interface ProfileAction {
    data object LoadInitial : ProfileAction
}
