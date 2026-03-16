package com.patifiner.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.patifiner.client.design.icons.PtfIcons
import org.patifiner.client.root.login.SIGNUP_LINK_TAG
import org.patifiner.client.root.signup.LOGIN_LINK_TAG

/**
 * This class generates a Baseline Profile and a Startup Profile for the app.
 *
 * Baseline Profiles are used by the Android Runtime (ART) to pre-compile critical code paths,
 * reducing JIT-related lag and improving overall performance.
 *
 * Startup Profiles optimize the DEX layout to put classes needed for startup in the primary DEX file.
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generateBaselineProfiles() = baselineProfileRule.collect(
        packageName = "org.patifiner.client",
        includeInStartupProfile = true
    ) {
        pressHome()
        startActivityAndWait()

        PtfIcons.warmup()

        device.waitForIdle()

        device.wait(
            Until.findObject(By.res(SIGNUP_LINK_TAG)),
            2_000
        )?.click() ?: throw IllegalStateException("Signup link not found")

        device.wait(
            Until.findObject(By.res(LOGIN_LINK_TAG)),
            2_000
        )?.click() ?: throw IllegalStateException("Login link not found - navigation failed")

        device.wait(
            Until.hasObject(By.res(SIGNUP_LINK_TAG)),
            2_000
        )
    }
}
