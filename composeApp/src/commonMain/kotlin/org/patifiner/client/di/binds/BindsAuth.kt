package org.patifiner.client.di.binds

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import org.patifiner.client.di.LoggedInScope
import org.patifiner.client.topics.AddUserTopicUseCase
import org.patifiner.client.topics.LoadUserTopicsTreeUseCase
import org.patifiner.client.topics.SearchTopicsUseCase
import org.patifiner.client.topics.data.TopicsRepository


@ContributesTo(LoggedInScope::class)
interface BindsAuth {

    @Provides
    @SingleIn(LoggedInScope::class)
    fun provideTopicsRepository(http: HttpClient): TopicsRepository = TopicsRepository(http)

    @Provides
    @SingleIn(LoggedInScope::class)
    fun provideLoadUserTopicsTreeUseCase(repo: TopicsRepository): LoadUserTopicsTreeUseCase = LoadUserTopicsTreeUseCase(repo)

    @Provides
    @SingleIn(LoggedInScope::class)
    fun provideSearchTopicsUseCase(repo: TopicsRepository): SearchTopicsUseCase = SearchTopicsUseCase(repo)

    @Provides
    @SingleIn(LoggedInScope::class)
    fun provideAddUserTopicUseCase(repo: TopicsRepository): AddUserTopicUseCase = AddUserTopicUseCase(repo)
}
