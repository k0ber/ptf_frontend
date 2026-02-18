package org.patifiner.client.signup

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.patifiner.client.base.asValue
import org.patifiner.client.signup.ui.SignupState

class SignupComponent(
    componentContext: ComponentContext,
    private val navigateBackToLogin: () -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    private val store: SignupStore = instanceKeeper.getStore { get() }

    val state: Value<SignupState> = store.asValue()
    val labels: Flow<SignupLabel> = store.labels

    fun onIntent(intent: SignupIntent) = store.accept(intent)
    fun onBack() = navigateBackToLogin()
}
