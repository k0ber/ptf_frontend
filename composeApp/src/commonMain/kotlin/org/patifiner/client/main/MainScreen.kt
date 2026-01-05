package org.patifiner.client.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.patifiner.client.design.PtfAnim
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.design.icons.ptficons.IcArrowDown
import org.patifiner.client.design.icons.ptficons.IcArrowUp
import org.patifiner.client.design.icons.ptficons.IcEmail
import org.patifiner.client.profile.ui.ProfileScreen
import org.patifiner.client.topics.ui.AddUserTopicScreen
import org.patifiner.client.viewing.UserTopicsScreen

enum class Tab { TOPICS, ADD_TOPIC, PROFILE }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(component: MainComponent, snackbarHost: SnackbarHostState) {
    val activeTab by component.activeTab.subscribeAsState()
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = activeTab == tab,
                        onClick = { component.onTabClicked(tab) },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    Tab.PROFILE -> PtfIcons.IcArrowUp
                                    Tab.TOPICS -> PtfIcons.IcArrowDown
                                    Tab.ADD_TOPIC -> PtfIcons.IcEmail
                                },
                                contentDescription = tab.name
                            )
                        },
                        label = { Text(tab.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { padding ->
        Children(
            stack = component.stack,
            modifier = Modifier.padding(padding),
            animation = PtfAnim.changingTabs()
        ) { child ->
            when (val instance = child.instance) {
                is MainChild.Profile -> ProfileScreen(instance.component, snackbarHost)
                is MainChild.UserTopics -> UserTopicsScreen(instance.component, snackbarHost)
                is MainChild.AddUserTopic -> AddUserTopicScreen(instance.component, snackbarHost)
            }
        }
    }
}
