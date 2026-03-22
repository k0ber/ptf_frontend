package org.patifiner.client.root.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainTabRoute : NavKey {
    @Serializable data object Profile : MainTabRoute
    @Serializable data object UserTopics : MainTabRoute
    @Serializable data object AddUserTopic : MainTabRoute
}
