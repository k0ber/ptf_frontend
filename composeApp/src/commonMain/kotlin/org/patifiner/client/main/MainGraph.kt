package org.patifiner.client.main

import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Includes
import org.patifiner.client.LoggedInScope
import org.patifiner.client.binds.BindsCommon
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.profile.ProfileComponent
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.topics.AddUserTopicUseCase
import org.patifiner.client.topics.LoadUserTopicsTreeUseCase
import org.patifiner.client.topics.SearchTopicsUseCase
import org.patifiner.client.viewing.UserTopicsComponent

@GraphExtension(LoggedInScope::class)
interface MainGraph {
    //region UseCases
    val loadProfile: LoadProfileUseCase
    val loadUserTopicsTreeUseCase: LoadUserTopicsTreeUseCase
    val addUserTopic: AddUserTopicUseCase
    val searchTopics: SearchTopicsUseCase
    //endregion

    fun mainComponentFactory(): MainComponent.Factory

    //region Children
    fun profileComponentFactory(): ProfileComponent.Factory
    fun userTopicsComponentFactory(): UserTopicsComponent.Factory
    fun addUserTopicComponentFactory(): AddUserTopicComponent.Factory
    //endregion

    @GraphExtension.Factory
    interface Factory {
        fun create(@Includes common: BindsCommon): MainGraph
    }
}
