package org.patifiner.client.root.main.profile

import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.MainTabRoute

class ProfileViewModel(
    store: ProfileStore,
    private val mainNavigator: MainNavigator,
) : PtfViewModel<ProfileIntent, ProfileState, ProfileLabel>(store) {

    fun onNavToMyTopics() {
        mainNavigator.navigateTo(MainTabRoute.UserTopics)
    }

    fun onNavToAddTopic() {
        mainNavigator.navigateTo(MainTabRoute.AddUserTopic)
    }
}
