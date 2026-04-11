package org.patifiner.client.root.main.intro

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.patifiner.client.core.PtfLog

class IntroNavigator {

    init {
        PtfLog.d { "IntroNavigator init" }
    }

    val backStack = mutableStateListOf<IntroRoute>(IntroRoute.UserInfo)

    fun next(route: IntroRoute) = backStack.add(route)
    fun pop() = backStack.removeLastOrNull()
}

@Serializable
sealed interface IntroRoute : NavKey {
    @Serializable data object UserInfo : IntroRoute
    @Serializable data object Topics : IntroRoute
    @Serializable data object Events : IntroRoute
}
