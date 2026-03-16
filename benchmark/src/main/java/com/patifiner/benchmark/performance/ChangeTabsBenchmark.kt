package com.patifiner.benchmark.performance

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.patifiner.client.root.login.SIGNUP_LINK_TAG
import org.patifiner.client.root.signup.LOGIN_LINK_TAG

@RunWith(AndroidJUnit4::class)
class ChangeTabsBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun measureLoginToSignup() = benchmarkRule.measureRepeated(
        packageName = "org.patifiner.client",
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.Partial(),
        iterations = 5,
        startupMode = StartupMode.WARM
    ) {
        startActivityAndWait()

        device.wait(
            Until.findObject(By.res(SIGNUP_LINK_TAG)),
            2_000
        )?.click() ?: throw IllegalStateException("Signup link not found")

        device.wait(
            Until.findObject(By.res(LOGIN_LINK_TAG)),
            2_000
        )?.click() ?: throw IllegalStateException("Login link not found - navigation failed")
    }
}
