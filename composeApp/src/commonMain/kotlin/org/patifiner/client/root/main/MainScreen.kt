@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.patifiner.client.design.views.PtfBottomBar

enum class Tab { TOPICS, ADD_TOPIC, PROFILE }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    Scaffold(
        bottomBar = {
            PtfBottomBar(
                activeTab = viewModel.navigator.activeTab,
                onTabClick = { viewModel.navigator.switchTab(it) }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) { // is this box necessary?
            NavDisplay(
                backStack = viewModel.navigator.tabBackStack,
                entryProvider = koinEntryProvider()
            )
        }
    }
}
