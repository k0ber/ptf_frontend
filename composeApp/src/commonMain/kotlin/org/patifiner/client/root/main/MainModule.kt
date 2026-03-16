@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root.main

import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.patifiner.client.root.login.LoadProfileUseCase
import org.patifiner.client.root.login.LogoutUseCase
import org.patifiner.client.root.main.profile.ProfileScreen
import org.patifiner.client.root.main.profile.ProfileStoreFactory
import org.patifiner.client.root.main.profile.ProfileViewModel
import org.patifiner.client.root.main.topics.AddUserTopicUseCase
import org.patifiner.client.root.main.topics.LoadUserTopicsTreeUseCase
import org.patifiner.client.root.main.topics.SearchTopicsUseCase
import org.patifiner.client.root.main.topics.add.AddTopicsScreen
import org.patifiner.client.root.main.topics.add.AddTopicsStoreFactory
import org.patifiner.client.root.main.topics.add.AddTopicsViewModel
import org.patifiner.client.root.main.topics.data.TopicsRepository
import org.patifiner.client.root.main.topics.show.ShowTopicsScreen
import org.patifiner.client.root.main.topics.show.ShowTopicsStoreFactory
import org.patifiner.client.root.main.topics.show.ShowTopicsViewModel

const val SESSION_SCOPE = "SessionScope"

val mainModule = module {
    scope(named(SESSION_SCOPE)) {
        scopedOf(::MainNavigator)
        scopedOf(::TopicsRepository)

        scopedOf(::LoadProfileUseCase)
        scopedOf(::LogoutUseCase)
        scopedOf(::LoadUserTopicsTreeUseCase)
        scopedOf(::SearchTopicsUseCase)
        scopedOf(::AddUserTopicUseCase)

        viewModelOf(::MainViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::ShowTopicsViewModel)
        viewModelOf(::AddTopicsViewModel)

        factory {
            ProfileStoreFactory(
                factory = get(),
                loadProfile = get(),
                logoutUseCase = get()
            ).create()
        }

        factory {
            AddTopicsStoreFactory(
                factory = get(),
                loadTree = get(),
                search = get(),
                addTopic = get(),
            ).create()
        }

        factory { ShowTopicsStoreFactory(factory = get(), repo = get()).create() }
    }

    navigation<MainTabRoute.Profile> {
        ProfileScreen(koinViewModel())
    }

    navigation<MainTabRoute.UserTopics> {
        ShowTopicsScreen(koinViewModel())
    }

    navigation<MainTabRoute.AddUserTopic> {
        AddTopicsScreen(koinViewModel())
    }
}
