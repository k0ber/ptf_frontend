package org.patifiner.client.root.signup

import androidx.compose.runtime.Stable
import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.RootNavigator

@Stable
class SignupViewModel(
    store: SignupStore,
    private val navigator: RootNavigator
) : PtfViewModel<SignupIntent, SignupState, SignupLabel>(store) {
    fun onBack() = navigator.pop()
}
