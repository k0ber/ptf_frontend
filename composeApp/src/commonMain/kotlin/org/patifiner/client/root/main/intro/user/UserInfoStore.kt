package org.patifiner.client.root.main.intro.user

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.datetime.LocalDate
import org.patifiner.client.core.BaseState
import org.patifiner.client.core.Gender
import org.patifiner.client.core.UserLanguage
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.createDefault
import org.patifiner.client.core.execute
import org.patifiner.client.root.signup.UpdateUserRequest

interface UserInfoStore : Store<UserInfoIntent, UserInfoState, UserInfoLabel>

data class UserInfoState(
    val name: String = "",
    val avatarUrl: String? = null,
    val birthDate: LocalDate? = null,
    val gender: Gender = Gender.NOT_SPECIFIED,
    val cityId: Long? = null,
    val languages: List<UserLanguage> = emptyList(),
    override val isLoading: Boolean = false
) : BaseState<UserInfoState> {
    override fun withLoading(isLoading: Boolean): UserInfoState = copy(isLoading = isLoading)

    fun toRequest() = UpdateUserRequest(
        name = name.takeIf { it.isNotBlank() },
        birthDate = birthDate?.toString(),
        gender = gender,
        cityId = cityId,
        languages = languages
    )
}

sealed interface UserInfoIntent {
    data class ChangeName(val name: String) : UserInfoIntent
    data class ChangeBirthDate(val date: LocalDate?) : UserInfoIntent
    data class ChangeGender(val gender: Gender) : UserInfoIntent
    data class UploadAvatar(val bytes: ByteArray, val fileName: String) : UserInfoIntent
    data object SaveProfile : UserInfoIntent
}

sealed interface UserInfoLabel {
    data class Error(val message: String) : UserInfoLabel
    data object Saved : UserInfoLabel
}

internal class UserInfoIntroStoreFactory(
    private val factory: StoreFactory,
    private val userInteractor: UserInteractor
) {
    fun create(): UserInfoStore =
        object : UserInfoStore, Store<UserInfoIntent, UserInfoState, UserInfoLabel>
        by factory.createDefault(
            initialState = UserInfoState(),
            executorFactory = coroutineExecutorFactory {
                onIntent<UserInfoIntent.ChangeName> { intent -> dispatch { copy(name = intent.name) } }
                onIntent<UserInfoIntent.ChangeBirthDate> { intent -> dispatch { copy(birthDate = intent.date) } }
                onIntent<UserInfoIntent.ChangeGender> { intent -> dispatch { copy(gender = intent.gender) } }

                onIntent<UserInfoIntent.UploadAvatar> { intent ->
                    execute(
                        useCase = { userInteractor.uploadAvatar(intent.bytes, intent.fileName) },
                        onSuccessData = { user: UserDto -> copy(avatarUrl = user.avatarUrl) },
                        errorFactory = { msg: String -> UserInfoLabel.Error(msg) }
                    )
                }

                onIntent<UserInfoIntent.SaveProfile> {
                    execute(
                        useCase = { userInteractor.updateProfile(state().toRequest()) },
                        onSuccess = { _: UserDto -> publish(UserInfoLabel.Saved) },
                        errorFactory = { msg: String -> UserInfoLabel.Error(msg) }
                    )
                }
            }
        ) {}
}
