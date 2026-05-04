package org.patifiner.client.design.views

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.patifiner.client.design.PtfPreview
import org.patifiner.client.design.icons.IcExplore
import org.patifiner.client.design.icons.IcGroup
import org.patifiner.client.design.icons.IcPerson
import org.patifiner.client.root.main.MainTabRoute
import patifinerclient.composeapp.generated.resources.Res
import patifinerclient.composeapp.generated.resources.tab_explore
import patifinerclient.composeapp.generated.resources.tab_groups
import patifinerclient.composeapp.generated.resources.tab_profile

val mainTabs: List<MainTabRoute> = listOf(
    MainTabRoute.Explore,
    MainTabRoute.Groups,
    MainTabRoute.Profile
)

fun MainTabRoute.getDisplayData(): Pair<StringResource, ImageVector> = when (this) {
    is MainTabRoute.Explore -> Res.string.tab_explore to IcExplore
    is MainTabRoute.Groups -> Res.string.tab_groups to IcGroup
    is MainTabRoute.Profile -> Res.string.tab_profile to IcPerson
}

@Composable
fun MainBottomBar(
    activeTab: MainTabRoute,
    onTabClick: (MainTabRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        tonalElevation = 0.dp,
        containerColor = colorScheme.surface.copy(alpha = 0.94f),
    ) {
        mainTabs.forEach { tab ->
            val selected = activeTab == tab
            val (tabStringRes, tabIcon) = tab.getDisplayData()
            val tabLabel = stringResource(tabStringRes)
            NavigationBarItem(
                selected = selected,
                onClick = { onTabClick(tab) },
                label = {
                    Text(
                        text = tabLabel,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                icon = {
                    Icon(
                        imageVector = tabIcon,
                        contentDescription = tabLabel
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
                    indicatorColor = colorScheme.primaryContainer.copy(alpha = 0.5f),
                    unselectedIconColor = colorScheme.outline,
                    unselectedTextColor = colorScheme.outline
                )
            )
        }
    }
}

@Composable
fun MainBottomBarPreview() {
    Scaffold(
        bottomBar = { MainBottomBar(activeTab = MainTabRoute.Profile, onTabClick = {}) },
        containerColor = Color.Transparent
    ) { }
}

@Preview
@Composable
fun MainBottomBarLight() {
    PtfPreview { MainBottomBarPreview() }
}

@Preview
@Composable
fun MainBottomBarDark() {
    PtfPreview(forceDarkMode = true) { MainBottomBarPreview() }
}
