package org.patifiner.client.root.main

import androidx.compose.runtime.mutableStateListOf

class MainNavigator {
    val tabBackStack =
        mutableStateListOf<MainTabRoute>(MainTabRoute.Profile)

    val activeTab: MainTabRoute get() = tabBackStack.last()

    fun switchTab(route: MainTabRoute) {
        if (activeTab == route) {
            return
        }

        tabBackStack.remove(route)
        tabBackStack.add(route)
    }

}
