package org.patifiner.client.di.binds

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import org.patifiner.client.di.LoggedInScope
import org.patifiner.client.topics.data.TopicsRepository


@ContributesTo(LoggedInScope::class)
interface BindsAuth {

    @Provides
    @SingleIn(LoggedInScope::class)
    fun provideTopicsRepository(http: HttpClient): TopicsRepository = TopicsRepository(http)

}
