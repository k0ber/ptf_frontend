package org.patifiner.client.root.login

import androidx.compose.runtime.Stable
import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.PtfRoute

@Stable
class LoginViewModel(
    val store: LoginStore,
    private val navigator: RootNavigator,
) : PtfViewModel<LoginIntent, LoginState, LoginLabel>(store) {

    fun onNavToSignup() = navigator.navigateTo(PtfRoute.Signup)

}
