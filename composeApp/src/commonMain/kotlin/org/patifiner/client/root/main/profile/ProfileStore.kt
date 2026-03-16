package org.patifiner.client.root.main.profile

import androidx.compose.runtime.Immutable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.login.LoadProfileUseCase
import org.patifiner.client.root.login.LogoutUseCase

interface ProfileStore : Store<ProfileIntent, ProfileState, ProfileLabel>

@Immutable
data class ProfileState(
    val userDto: UserDto? = null,
    override val isLoading: Boolean = false
) : BaseState<ProfileState> {
    override fun withLoading(isLoading: Boolean): ProfileState = ProfileState(isLoading = isLoading)
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

class ProfileStoreFactory(
    private val factory: StoreFactory,
    private val loadProfile: LoadProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) {
    fun create(): ProfileStore =
        object : ProfileStore, Store<ProfileIntent, ProfileState, ProfileLabel>
        by factory.createDefault(
            initialState = ProfileState(),
            executorFactory = coroutineExecutorFactory {
                onAction<ProfileAction.LoadInitial> {
                    execute(
                        useCase = { loadProfile() },
                        onSuccessData = { data -> ProfileState(userDto = data) },
                        errorFactory = ProfileLabel::Error
                    )
                }

                onIntent<ProfileIntent.Refresh> {
                    execute(
                        useCase = { loadProfile() },
                        onSuccessData = { data -> ProfileState(userDto = data) },
                        errorFactory = ProfileLabel::Error
                    )
                }

                onIntent<ProfileIntent.Logout> {
                    logoutUseCase()
                }
            },
            bootstrapper = coroutineBootstrapper { dispatch(ProfileAction.LoadInitial) }
        ) {}
}
