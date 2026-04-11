package org.patifiner.client.root.main.intro.user

import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.Gender
import org.patifiner.client.core.PtfLog
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute

interface UserInfoStore : Store<UserInfoIntent, UserInfoState, UserInfoLabel>

@Stable
data class UserInfoState(
    val user: UserDto = UserDto.empty(),
    override val isLoading: Boolean = false,
    val isAvatarUploading: Boolean = false,
    val selectedLocalFile: PlatformFile? = null
) : BaseState<UserInfoState> {

    val name: String get() = user.name
    val photos: List<String> get() = user.photos
    val avatarUrl: String? get() = photos.lastOrNull()

    override fun withLoading(isLoading: Boolean): UserInfoState = copy(isLoading = isLoading)
}

internal class UserInfoIntroStoreFactory(
    private val factory: StoreFactory,
    private val userInteractor: UserInteractor
) {
    fun create(): UserInfoStore {
        PtfLog.d { "UserInfoStore.create hc = ${this.hashCode()}" }
        return object : UserInfoStore, Store<UserInfoIntent, UserInfoState, UserInfoLabel>
        by factory.createDefault(
            initialState = UserInfoState(user = userInteractor.getDraftSync() ?: UserDto.empty()),
            bootstrapper = coroutineBootstrapper { dispatch(UserInfoAction.Init) },
            executorFactory = coroutineExecutorFactory {
                // Load initial
                onAction<UserInfoAction.Init> {
                    execute(
                        useCase = { userInteractor.loadProfile() },
                        loading = { copy(isLoading = it) },
                        onSuccessData = { newUser ->
                            launch { userInteractor.saveDraft(newUser) }
                            dispatch { copy(user = newUser) }
                            copy(user = newUser)
                        },
                        errorFactory = { UserInfoLabel.Error(it) }
                    )
                }
                // Change name
                onIntent<UserInfoIntent.ChangeName> { intent ->
                    val newState = state().updateUser { copy(name = intent.name) }
                    launch { userInteractor.saveDraft(newState.user) }
                    dispatch { newState }
                }
                // Change birthdate
                onIntent<UserInfoIntent.ChangeBirthDate> { intent ->
                    val newState = state().updateUser { copy(birthDate = intent.date) }
                    launch { userInteractor.saveDraft(newState.user) }
                    dispatch { newState }
                }
                // Change gender
                onIntent<UserInfoIntent.ChangeGender> { intent ->
                    val newState = state().updateUser { copy(gender = intent.gender) }
                    launch { userInteractor.saveDraft(newState.user) }
                    dispatch { newState }
                }
                // Photo upload
                onIntent<UserInfoIntent.UploadPhoto> { intent ->
                    execute(
                        useCase = { userInteractor.uploadAvatar(intent.file) },
                        loading = { copy(isAvatarUploading = it, isLoading = it, selectedLocalFile = intent.file) },
                        onSuccessData = { newUser ->
                            launch { userInteractor.saveDraft(newUser) }
                            copy(user = newUser)
                        },
                        errorFactory = { UserInfoLabel.Error(it) }
                    )
                }
                // Photo delete
                onIntent<UserInfoIntent.DeletePhoto> { intent ->
                    execute(
                        useCase = { userInteractor.deletePhoto(intent.url) },
                        loading = { withLoading(it) },
                        onSuccessData = { newUser ->
                            launch { userInteractor.saveDraft(newUser) }
                            copy(user = newUser)
                        },
                        errorFactory = UserInfoLabel::Error
                    )
                }
                // Rearrange Photos
                onIntent<UserInfoIntent.RearrangePhotos> { intent ->
                    val newState = state().updateUser { copy(photos = intent.newList) }
                    launch { userInteractor.saveDraft(newState.user) }
                    dispatch { newState }
                }
                // Update profile
                onIntent<UserInfoIntent.SaveProfile> {
                    execute(
                        useCase = { userInteractor.updateProfile(state().toUpdateProfileRequest()) },
                        loading = { withLoading(it) },
                        onSuccess = { _: UserDto ->
                            publish(UserInfoLabel.Saved)
                        },
                        errorFactory = { msg: String -> UserInfoLabel.Error(msg) }
                    )
                }
            },
        ) {}
    }
}

fun UserInfoState.updateUser(update: UserDto.() -> UserDto): UserInfoState {
    return copy(user = user.update())
}

fun UserDto.Companion.empty() = UserDto( // todo: not cool
    id = 0, name = "", photos = emptyList(), birthDate = null,
    email = "", gender = Gender.NOT_SPECIFIED,
    languages = emptyList(), locale = "en", cityId = null
)

sealed interface UserInfoIntent {
    data class UploadPhoto(val file: PlatformFile) : UserInfoIntent
    data class DeletePhoto(val url: String) : UserInfoIntent
    data class RearrangePhotos(val newList: List<String>) : UserInfoIntent
    data class ChangeName(val name: String) : UserInfoIntent
    data class ChangeBirthDate(val date: LocalDate?) : UserInfoIntent
    data class ChangeGender(val gender: Gender) : UserInfoIntent
    data object SaveProfile : UserInfoIntent
}

sealed interface UserInfoLabel {
    data class Error(val message: String) : UserInfoLabel
    data object Saved : UserInfoLabel
}

sealed interface UserInfoAction {
    data object Init : UserInfoAction
}
