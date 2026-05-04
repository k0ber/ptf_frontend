package org.patifiner.client.root.main

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface MainTabRoute : NavKey {
    @Serializable data object Profile : MainTabRoute
    @Serializable data object Explore : MainTabRoute
    @Serializable data object Groups : MainTabRoute
}
