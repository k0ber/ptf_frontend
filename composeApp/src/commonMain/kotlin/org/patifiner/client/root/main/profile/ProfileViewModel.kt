package org.patifiner.client.root.main.profile

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import org.patifiner.client.core.PtfLog
import org.patifiner.client.core.execute
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.MainTabRoute
import org.patifiner.client.root.main.intro.user.UserInteractor

@Stable class ProfileViewModel(
    private val userInteractor: UserInteractor,
    private val mainNavigator: MainNavigator,
) : ViewModel(), ContainerHost<ProfileState, ProfileSideEffect> {

    override val container: Container<ProfileState, ProfileSideEffect> = container(ProfileState()) {
        refreshProfile()
    }

    init {
        PtfLog.d { "ProfileViewModel init, hc = ${this.hashCode()}" }
    }

    fun refreshProfile() = intent {
        execute(
            block = { userInteractor.loadProfile() },
            onSuccess = { user -> reduce { state.copy(user = user) } },
            errorFactory = ProfileSideEffect::Error
        )
    }

    fun logout() {
        userInteractor.logout()
    }

    fun onNavToMyTopics() {
        mainNavigator.switchTab(MainTabRoute.Explore)
    }

    fun onNavToAddTopic() {
        mainNavigator.switchTab(MainTabRoute.Groups)
    }
}
