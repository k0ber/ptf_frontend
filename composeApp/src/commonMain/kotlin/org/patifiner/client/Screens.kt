package org.patifiner.client

import kotlinx.serialization.Serializable

@Serializable
enum class ScreenSource { LOAD, LOGIN, SIGNUP }

@Serializable
enum class TopicAction { ADD }

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen

    @Serializable
    data object Signup : Screen

    @Serializable
    data class Profile(val source: ScreenSource) : Screen

    @Serializable
    data class UserTopics(val action: TopicAction) : Screen

    @Serializable
    data object AddUserTopic : Screen
}
