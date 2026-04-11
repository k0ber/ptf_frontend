package org.patifiner.client.root.main.profile

import org.patifiner.client.core.PtfLog
import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.MainTabRoute

class ProfileViewModel(
    store: ProfileStore,
    private val mainNavigator: MainNavigator,
) : PtfViewModel<ProfileIntent, ProfileState, ProfileLabel>(store) {

    init {
        PtfLog.d { "ProfileViewModel init, hc = ${this.hashCode()}" }
    }

    fun onNavToMyTopics() {
        mainNavigator.navigateTo(MainTabRoute.UserTopics)
    }

    fun onNavToAddTopic() {
        mainNavigator.navigateTo(MainTabRoute.AddUserTopic)
    }
}
