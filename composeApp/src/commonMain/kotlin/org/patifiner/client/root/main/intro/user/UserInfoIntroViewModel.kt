package org.patifiner.client.root.main.intro.user

import androidx.compose.runtime.Stable
import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.intro.IntroViewModel

@Stable
class UserInfoIntroViewModel(
    store: UserInfoStore,
    private val introViewModel: IntroViewModel
) : PtfViewModel<UserInfoIntent, UserInfoState, UserInfoLabel>(store) {
    fun onNext() {
        introViewModel.nextStep()
    }
}
