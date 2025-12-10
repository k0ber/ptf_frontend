package org.patifiner.client

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import org.patifiner.client.AppShellComponent
import org.patifiner.client.AppShellComponent.Child
import org.patifiner.client.Tab
import org.patifiner.client.design.childrenAnimation
import org.patifiner.client.profile.ui.ProfileScreen
import org.patifiner.client.topics.ui.AddUserTopicScreen
import org.patifiner.client.viewing.UserTopicsScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppShellScreen(
    component: AppComponent,
    snackbarHost: SnackbarHostState
) {
    val stack by component.stack.collectAsState()
    val activeTab = component.activeTab

    // Используем Scaffold для размещения BottomAppBar
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
                                    Tab.TOPICS -> Icons.Default.List
                                    Tab.ADD_TOPIC -> Icons.Default.AddCircle
                                    Tab.PROFILE -> Icons.Default.AccountCircle
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
        // Рендеринг активного экрана вкладки
        Children(
            stack = stack,
            modifier = Modifier.padding(padding),
            animation = stackAnimation() // Анимация внутри Shell - простая
        ) { child ->
            Box(Modifier.fillMaxSize()) {
                when (val instance = child.instance) {
                    is Child.Topics -> UserTopicsScreen(instance.component, snackbarHost)
                    is Child.AddTopic -> AddUserTopicScreen(instance.component, snackbarHost)
                    is Child.Profile -> ProfileScreen(instance.component, snackbarHost)
                }
            }
        }
    }
}