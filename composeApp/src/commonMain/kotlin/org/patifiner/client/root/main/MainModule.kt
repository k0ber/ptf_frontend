@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.koin.compose.LocalKoinScope
import org.koin.compose.koinInject
import org.koin.compose.scope.rememberKoinScope
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.onClose
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation
import org.patifiner.client.core.authScopedViewModel
import org.patifiner.client.core.createHttpClient
import org.patifiner.client.root.PtfRoute
import org.patifiner.client.root.login.data.SessionManager
import org.patifiner.client.root.main.data.UserRepository
import org.patifiner.client.root.main.data.UserStorage
import org.patifiner.client.root.main.intro.IntroNavigator
import org.patifiner.client.root.main.intro.IntroRoute
import org.patifiner.client.root.main.intro.IntroScreen
import org.patifiner.client.root.main.intro.IntroViewModel
import org.patifiner.client.root.main.intro.events.EventsIntroScreen
import org.patifiner.client.root.main.intro.events.EventsIntroStoreFactory
import org.patifiner.client.root.main.intro.events.EventsIntroViewModel
import org.patifiner.client.root.main.intro.topics.TopicsIntroScreen
import org.patifiner.client.root.main.intro.topics.TopicsIntroStoreFactory
import org.patifiner.client.root.main.intro.topics.TopicsIntroViewModel
import org.patifiner.client.root.main.intro.user.UserInfoIntroScreen
import org.patifiner.client.root.main.intro.user.UserInfoIntroStoreFactory
import org.patifiner.client.root.main.intro.user.UserInfoIntroViewModel
import org.patifiner.client.root.main.intro.user.UserInteractor
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
        scoped { createHttpClient(engine = get(), json = get(), config = get(), sessionManager = get()) }

        scopedOf(::MainNavigator)
        scopedOf(::IntroNavigator)

        scopedOf(::UserStorage) { onClose { it?.clear() } } // todo: clear draft when app close ?
        scopedOf(::UserRepository)
        scopedOf(::TopicsRepository)

        // rework use cases to interactors - main idea is to gather business logic by data it works with
        scopedOf(::UserInteractor)
        scopedOf(::LoadUserTopicsTreeUseCase)
        scopedOf(::SearchTopicsUseCase)
        scopedOf(::AddUserTopicUseCase)

        // ViewModels
        viewModelOf(::MainViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::ShowTopicsViewModel)
        viewModelOf(::AddTopicsViewModel)
        viewModelOf(::IntroViewModel)
        viewModelOf(::UserInfoIntroViewModel)
        viewModelOf(::TopicsIntroViewModel)
        viewModelOf(::EventsIntroViewModel)

        factory { ProfileStoreFactory(factory = get(), userInteractor = get()).create() }
        factory { AddTopicsStoreFactory(factory = get(), loadTree = get(), search = get(), addTopic = get()).create() }
        factory { ShowTopicsStoreFactory(factory = get(), repo = get()).create() }

        factory { UserInfoIntroStoreFactory(factory = get(), userInteractor = get()).create() }
        factory { TopicsIntroStoreFactory(factory = get()).create() }
        factory { EventsIntroStoreFactory(factory = get()).create() }
    }

    // Main tabs
    navigation<PtfRoute.Main> { MainScreen(authScopedViewModel()) }
    navigation<MainTabRoute.Profile> { ProfileScreen(authScopedViewModel()) }
    navigation<MainTabRoute.UserTopics> { ShowTopicsScreen(authScopedViewModel()) }
    navigation<MainTabRoute.AddUserTopic> { AddTopicsScreen(authScopedViewModel()) }

    // Intro
    navigation<PtfRoute.Intro> { IntroScreen(authScopedViewModel()) }
    navigation<IntroRoute.UserInfo> { UserInfoIntroScreen(authScopedViewModel()) }
    navigation<IntroRoute.Topics> { TopicsIntroScreen(authScopedViewModel()) }
    navigation<IntroRoute.Events> { EventsIntroScreen(authScopedViewModel()) }

}
