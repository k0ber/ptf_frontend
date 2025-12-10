package org.patifiner.client

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import org.patifiner.client.di.AppGraph

class AppShellComponent(
    componentContext: ComponentContext,
    private val appGraph: AppGraph,
    private val onLogout: () -> Unit,
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    val stack: Value<StackNavigation<Config>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Topics, // Начальная вкладка
            handleBackButton = false,
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.Topics -> Child.Topics(
                appGraph.userTopicsComponentFactory.create(componentContext)
            )
            Config.AddTopic -> Child.AddTopic(
                appGraph.addUserTopicComponentFactory.create(componentContext)
            )
            Config.Profile -> Child.Profile(
                appGraph.profileComponentFactory.create(componentContext, onLogout)
            )
        }
    }

    // Методы для переключения вкладок
    fun onTabClicked(tab: Tab) {
        when (tab) {
            Tab.TOPICS -> navigation.navigate(Config.Topics) { it.copy() }
            Tab.ADD_TOPIC -> navigation.navigate(Config.AddTopic) { it.copy() }
            Tab.PROFILE -> navigation.navigate(Config.Profile) { it.copy() }
        }
    }

    // Получение текущей активной вкладки
    val activeTab: Tab
        get() = when (stack.value.active.configuration) {
            Config.Topics -> Tab.TOPICS
            Config.AddTopic -> Tab.ADD_TOPIC
            Config.Profile -> Tab.PROFILE
        }

    sealed interface Child {
        data class Topics(val component: UserTopicsComponent) : Child
        data class AddTopic(val component: AddUserTopicComponent) : Child
        data class Profile(val component: ProfileComponent) : Child
    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Topics : Config
        @Parcelize
        data object AddTopic : Config
        @Parcelize
        data object Profile : Config
    }
}

enum class Tab {
    TOPICS, ADD_TOPIC, PROFILE
}