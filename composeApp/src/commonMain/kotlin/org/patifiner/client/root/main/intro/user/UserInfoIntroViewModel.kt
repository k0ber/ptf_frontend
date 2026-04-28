package org.patifiner.client.root.main.intro.user

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import io.github.vinceglb.filekit.PlatformFile
import kotlinx.datetime.LocalDate
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.core.Gender
import org.patifiner.client.core.UserDto
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.intro.IntroViewModel

@Stable
class UserInfoIntroViewModel(
    private val userInteractor: UserInteractor,
    private val introViewModel: IntroViewModel
) : ViewModel(), ContainerHost<UserInfoIntroState, UserInfoSideEffect> {

    override val container: Container<UserInfoIntroState, UserInfoSideEffect> = container(UserInfoIntroState()) {
        val draft = userInteractor.getDraftSync() ?: UserDto.empty()
        reduce { state.copy(user = draft) }
        refreshProfile()
    }

    fun refreshProfile() = intent {
        execute(
            block = { userInteractor.loadProfile() },
            onSuccess = { newUser -> updateAndSaveDraft { newUser } },
            errorFactory = UserInfoSideEffect::Error
        )
    }

    fun saveProfile() = intent {
        execute(
            block = { userInteractor.updateProfile(state.user.toUpdateProfileRequest()) },
            onSuccess = { postSideEffect(UserInfoSideEffect.Saved) },
            errorFactory = UserInfoSideEffect::Error
        )
    }

    fun uploadAvatar(file: PlatformFile) = intent {
        execute(
            block = { userInteractor.uploadAvatar(file) },
            onSuccess = { newUser ->
                updateAndSaveDraft { newUser }
                reduce { state.copy(selectedLocalFile = file) }
            },
            errorFactory = UserInfoSideEffect::Error
        )
    }

    fun deletePhoto(url: String) = intent {
        execute(
            block = { userInteractor.deletePhoto(url) },
            onSuccess = { newUser -> updateAndSaveDraft { newUser } },
            errorFactory = UserInfoSideEffect::Error
        )
    }

    fun changeName(name: String) = intent {
        updateAndSaveDraft { copy(name = name) }
    }

    fun changeBirthDate(date: LocalDate?) = intent {
        updateAndSaveDraft { copy(birthDate = date) }
    }

    fun changeGender(gender: Gender) = intent {
        updateAndSaveDraft { copy(gender = gender) }
    }

    fun onNext() = introViewModel.nextStep()

    // todo: double check
    private suspend fun Syntax<UserInfoIntroState, UserInfoSideEffect>.updateAndSaveDraft(
        update: UserDto.() -> UserDto
    ) {
        val newUser = state.user.update()
        reduce { state.copy(user = newUser) }
        userInteractor.saveDraft(newUser)
    }
}
