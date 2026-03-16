package org.patifiner.client.root

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface PtfRoute : NavKey {

    val requiresAuth: Boolean get() = true

    @Serializable data object Login : PtfRoute {
        override val requiresAuth = false
    }

    @Serializable data object Signup : PtfRoute {
        override val requiresAuth = false
    }

    @Serializable data object Main : PtfRoute

}