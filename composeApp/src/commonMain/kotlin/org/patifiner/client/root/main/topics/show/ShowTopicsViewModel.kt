package org.patifiner.client.root.main.topics.show

import org.patifiner.client.core.PtfViewModel
import org.patifiner.client.root.main.MainNavigator
import org.patifiner.client.root.main.MainTabRoute

class ShowTopicsViewModel(
    store: ShowTopicsStore,
    private val mainNavigator: MainNavigator
) : PtfViewModel<ShowTopicsIntent, ShowTopicsState, ShowTopicsEvent>(store) {

    fun onAddClick() {
        mainNavigator.switchTab(MainTabRoute.AddUserTopic)
    }

}
