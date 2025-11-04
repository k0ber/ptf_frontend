package org.patifiner.client.di

import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Includes
import org.patifiner.client.di.binds.BindsCommon
import org.patifiner.client.login.LoadProfileUseCase
import org.patifiner.client.topics.data.TopicsRepository


@GraphExtension(LoggedInScope::class)
interface LoggedInGraph {
    val loadProfile: LoadProfileUseCase
    val topicsRepository: TopicsRepository

    @GraphExtension.Factory
    interface Factory {
        fun createLoggedInGraph(@Includes common: BindsCommon): LoggedInGraph
    }
}
