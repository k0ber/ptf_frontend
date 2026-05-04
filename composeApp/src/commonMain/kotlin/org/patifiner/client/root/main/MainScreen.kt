@file:OptIn(KoinExperimentalAPI::class)

package org.patifiner.client.root.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.patifiner.client.design.PtfAnimations
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.views.MainBottomBar
import org.patifiner.client.design.views.PtfScaffold
import org.patifiner.client.design.views.PtfText
import org.patifiner.client.root.RootSnackbarHost

// TODO: пробегись по всем скринам и проверь превью
// TODO: цвет отличается от эмулятора, на эмуляторе какой-то красноватый оттенок добавляется

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    MainContent(
        activeTab = viewModel.navigator.activeTab,
        backStack = viewModel.navigator.tabBackStack,
        onTabClick = { viewModel.navigator.switchTab(it) }
    )
}

@Composable
fun MainContent(
    activeTab: MainTabRoute,
    backStack: SnapshotStateList<MainTabRoute> = SnapshotStateList(),
    entryProvider: (MainTabRoute) -> NavEntry<MainTabRoute> = koinEntryProvider(),
    onTabClick: (MainTabRoute) -> Unit = {}
) {
    val snackbarHost = RootSnackbarHost.current

    PtfScaffold(
        snackbarHostState = snackbarHost,
        navContent = { MainBottomBar(activeTab = activeTab, onTabClick = onTabClick) },
    ) { padding ->
        Box(Modifier.padding(padding)) {
            NavDisplay(
                backStack = backStack,
                entryProvider = entryProvider,
                transitionSpec = PtfAnimations.tabTransitionSpec,
                popTransitionSpec = PtfAnimations.tabTransitionSpec,
                predictivePopTransitionSpec = PtfAnimations.tabPredictiveSpec(),
                entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            )
        }
    }
}

@Composable
fun MainPreview() {
    MainContent(activeTab = MainTabRoute.Profile, backStack = mutableStateListOf(MainTabRoute.Profile), onTabClick = {}, entryProvider = { route ->
        NavEntry(route) {
            Box(
                modifier = Modifier.fillMaxSize().background(colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                PtfText("Screen: ${route::class.simpleName}")
            }
        }
    })
}

@Preview
@Composable
fun LoginPreviewLight() {
    PtfPreview { MainPreview() }
}

@Preview
@Composable
fun LoginPreviewDark() {
    PtfPreview(forceDarkMode = true) { MainPreview() }
}
