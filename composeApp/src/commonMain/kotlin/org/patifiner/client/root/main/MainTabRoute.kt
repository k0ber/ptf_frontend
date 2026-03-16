package org.patifiner.client.root.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainTabRoute : NavKey {
    @Serializable data object Profile : org.patifiner.client.root.main.MainTabRoute
    @Serializable data object UserTopics : org.patifiner.client.root.main.MainTabRoute
    @Serializable data object AddUserTopic : org.patifiner.client.root.main.MainTabRoute
}
