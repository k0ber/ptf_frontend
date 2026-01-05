package org.patifiner.client.main

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable
import org.patifiner.client.profile.ProfileComponent
import org.patifiner.client.topics.AddUserTopicComponent
import org.patifiner.client.viewing.UserTopicsComponent

@Serializable sealed interface MainConfig {
    @Serializable data object Profile : MainConfig
    @Serializable data object UserTopics : MainConfig
    @Serializable data object AddUserTopic : MainConfig
}

sealed interface MainChild {
    data class Profile(val component: ProfileComponent) : MainChild
    data class UserTopics(val component: UserTopicsComponent) : MainChild
    data class AddUserTopic(val component: AddUserTopicComponent) : MainChild
}

@Inject
class MainComponent(
    @Assisted componentContext: ComponentContext,
    private val profileComponentFactory: ProfileComponent.Factory,
    private val userTopicsComponentFactory: UserTopicsComponent.Factory,
    private val addUserTopicComponentFactory: AddUserTopicComponent.Factory
) : ComponentContext by componentContext {

    @AssistedFactory fun interface Factory {
        fun create(componentContext: ComponentContext): MainComponent
    }

    private val tabsNavigation = StackNavigation<MainConfig>()

    val stack: Value<ChildStack<MainConfig, MainChild>> = childStack(
        source = tabsNavigation,
        serializer = MainConfig.serializer(),
        initialConfiguration = MainConfig.Profile,
        handleBackButton = true,
        childFactory = ::createChild
    )

    val activeTab: Value<Tab> = stack.map { it.active.configuration.toTab() }

    private fun createChild(config: MainConfig, componentContext: ComponentContext): MainChild {
        return when (config) {
            is MainConfig.UserTopics -> MainChild.UserTopics(
                component = userTopicsComponentFactory.create(
                    componentContext = componentContext,
                    naviAddTopic = { onTabClicked(Tab.ADD_TOPIC) }
                )
            )

            is MainConfig.AddUserTopic -> MainChild.AddUserTopic(
                component = addUserTopicComponentFactory.create(componentContext)
            )

            is MainConfig.Profile -> MainChild.Profile(
                component = profileComponentFactory.create(
                    componentContext = componentContext,
                    navMyTopics = { onTabClicked(Tab.TOPICS) },
                    navAddTopic = { onTabClicked(Tab.ADD_TOPIC) },
                )
            )
        }
    }

    fun onTabClicked(tab: Tab) = tabsNavigation.bringToFront(tab.toConfig())

    private fun Tab.toConfig(): MainConfig = when (this) {
        Tab.PROFILE -> MainConfig.Profile
        Tab.TOPICS -> MainConfig.UserTopics
        Tab.ADD_TOPIC -> MainConfig.AddUserTopic
    }

    private fun MainConfig.toTab(): Tab = when (this) {
        is MainConfig.Profile -> Tab.PROFILE
        is MainConfig.UserTopics -> Tab.TOPICS
        is MainConfig.AddUserTopic -> Tab.ADD_TOPIC
    }
}
