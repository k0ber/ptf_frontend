package org.patifiner.client.root.main

import androidx.compose.runtime.mutableStateListOf

class MainNavigator {
    val tabBackStack =
        mutableStateListOf<MainTabRoute>(MainTabRoute.Profile)

    val activeTab: MainTabRoute get() = tabBackStack.last()

    fun switchTab(route: MainTabRoute) {
        if (activeTab == route) return
        tabBackStack.clear()
        tabBackStack.add(route)
    }

    fun navigateTo(route: MainTabRoute) { // for navigation inside tabs
        tabBackStack.add(route)
    }

    fun pop() {
        tabBackStack.removeLastOrNull()
    }
}
