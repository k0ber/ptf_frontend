package com.patifiner.benchmark.tests

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import org.junit.Test
import org.patifiner.client.root.RootScreen
import org.patifiner.client.root.login.LOGIN_EMAIL_FIELD_TAG

class LoginStateRestorationTest : BasePerformanceTest() {

    @Test
    fun loginEmailIsRestored() {
        val restorationTester = StateRestorationTester(composeRule)
        restorationTester.setContent { RootScreen() }

        val testEmail = "perfectionist@test.com"

        composeRule.onNodeWithTag(LOGIN_EMAIL_FIELD_TAG)
            .performTextInput(testEmail)

        restorationTester.emulateSavedInstanceStateRestore()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag(LOGIN_EMAIL_FIELD_TAG)
            .assertTextContains(testEmail)

    }
}
