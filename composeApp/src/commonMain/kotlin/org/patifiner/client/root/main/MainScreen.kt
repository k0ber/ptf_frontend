@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.compose.scope.rememberKoinScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.qualifier.named
import org.patifiner.client.design.views.PtfBottomBar
import org.patifiner.client.root.login.data.SessionManager

enum class Tab { TOPICS, ADD_TOPIC, PROFILE }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val sessionManager: SessionManager = koinInject()
    val authScope = sessionManager.authScope
        ?: error("MainScreen called without active session scope")

    val koinScope = rememberKoinScope(authScope)

    Scaffold(
        bottomBar = {
            PtfBottomBar(
                activeTab = viewModel.navigator.activeTab,
                onTabClick = { viewModel.navigator.switchTab(it) }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavDisplay(
                backStack = viewModel.navigator.tabBackStack,
                entryProvider = koinEntryProvider(scope = koinScope),
                entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            )
        }
    }
}
