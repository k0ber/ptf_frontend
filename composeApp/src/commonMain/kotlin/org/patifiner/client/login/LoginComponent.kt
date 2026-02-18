package org.patifiner.client.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.patifiner.client.base.asValue

class LoginComponent(
    componentContext: ComponentContext,
    private val navToSignup: () -> Unit,
) : ComponentContext by componentContext, KoinComponent {
    private val store: LoginStore = instanceKeeper.getStore { get() }

    val state: Value<LoginState> = store.asValue()
    val labels: Flow<LoginLabel> = store.labels

    fun onIntent(intent: LoginIntent) = store.accept(intent)
    fun onNavToSignup() = navToSignup()
}
