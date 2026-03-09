package com.patifiner.benchmark.tests

import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.arkivanov.decompose.defaultComponentContext
import com.patifiner.benchmark.utils.assertRecompositionCount
import com.patifiner.benchmark.utils.assertRecompositionCountInRange
import org.junit.Test
import org.patifiner.client.ROOT_CHILDREN_TAG
import org.patifiner.client.RootComponent
import org.patifiner.client.RootScreen
import org.patifiner.client.login.LOGIN_EMAIL_FIELD_TAG

// recomposition count depends on animation duration and device refresh rate
// further investigation is needed
class ChangeTabsTest : BasePerformanceTest() {

    @Test
    fun testNavigationRecompositions() {
        val rootComponent = composeRule.runOnUiThread { // ui thread is required for lifecycle init
            RootComponent(composeRule.activity.defaultComponentContext())
        }

        composeRule.setContent {
            RootScreen(rootComponent)
        }

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule
                .onAllNodesWithTag(ROOT_CHILDREN_TAG)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag(ROOT_CHILDREN_TAG)
            .assertRecompositionCount(1)

        composeRule.onNodeWithTag(LOGIN_EMAIL_FIELD_TAG)
            .assertRecompositionCount(1)

        composeRule.onNodeWithText("Don’t have an account?", substring = true).performClick()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag(ROOT_CHILDREN_TAG)
            .assertRecompositionCountInRange(30..40)
    }

}
