package org.patifiner.client.root.signup

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.core.execute
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.login.data.AuthRepository

@Stable
class SignupViewModel(
    private val repo: AuthRepository,
    private val navigator: RootNavigator
) : ViewModel(), ContainerHost<SignupState, SignupSideEffect> {

    override val container: Container<SignupState, SignupSideEffect> = container(SignupState())

    fun changeName(name: String) = intent { reduce { state.copy(name = name) } }
    fun changeEmail(email: String) = intent { reduce { state.copy(email = email) } }
    fun changePassword(password: String) = intent { reduce { state.copy(password = password) } }
    fun changeConfirm(confirm: String) = intent { reduce { state.copy(confirm = confirm) } }

    fun signup() = intent {
        if (!state.canSubmit) return@intent

        execute(
            block = { repo.signup(state.toRequest()) },
            onSuccess = { /* handles via SessionManager */ },
            errorFactory = SignupSideEffect::Error,
        )
    }

    fun onBack() = navigator.pop()
}
