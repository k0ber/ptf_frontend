package org.patifiner.client.root.main.profile

import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.RootNavigator
import org.patifiner.client.root.main.MainNavigator

class ProfileViewModel(
    store: ProfileStore,
    private val rootNavigator: RootNavigator,
    private val navigator: MainNavigator,
) : PtfViewModel<ProfileIntent, ProfileState, ProfileLabel>(store) {

    fun onNavToMyTopics() {
    }

    fun onNavToAddTopic() {
//         navigator.navigateToTab(MainTabRoute.AddUserTopic)
    }
}
