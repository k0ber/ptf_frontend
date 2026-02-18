package org.patifiner.client.signup

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.patifiner.client.base.createDefault
import org.patifiner.client.base.execute
import org.patifiner.client.login.data.AuthRepository
import org.patifiner.client.signup.ui.SignupState

interface SignupStore : Store<SignupIntent, SignupState, SignupLabel>

sealed interface SignupLabel {
    data class Error(val message: String) : SignupLabel
    data object Success : SignupLabel
}

sealed interface SignupIntent {
    data class ChangeName(val name: String) : SignupIntent
    data class ChangeEmail(val email: String) : SignupIntent
    data class ChangePassword(val password: String) : SignupIntent
    data class ChangeConfirm(val confirm: String) : SignupIntent
    data object Signup : SignupIntent
}

internal class SignupStoreFactory(
    private val factory: StoreFactory,
    private val signupUseCase: SignupUseCase
) {
    fun create(): SignupStore =
        object : SignupStore, Store<SignupIntent, SignupState, SignupLabel>
        by factory.createDefault(
            initialState = SignupState(),
            executorFactory = coroutineExecutorFactory {
                onIntent<SignupIntent.ChangeName> { dispatch { copy(name = it.name) } }
                onIntent<SignupIntent.ChangeEmail> { dispatch { copy(email = it.email) } }
                onIntent<SignupIntent.ChangePassword> { dispatch { copy(password = it.password) } }
                onIntent<SignupIntent.ChangeConfirm> { dispatch { copy(confirm = it.confirm) } }

                onIntent<SignupIntent.Signup> {
                    val state = state()
                    if (!state.canSubmit) return@onIntent

                    execute(
                        useCase = { signupUseCase(state.toRequest()) },
                        onSuccess = { publish(SignupLabel.Success) },
                        errorFactory = SignupLabel::Error
                    )
                }
            }
        ) {}
}

class SignupUseCase(val repo: AuthRepository) {
    suspend operator fun invoke(signupRequest: CreateUserRequest): Result<Unit> =
        repo.signup(signupRequest)
}
