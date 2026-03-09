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

        val signupLink = "Don’t have an account?"   // stringResource(Res.string.already_have_account)
        val loginLink = "Already have an account?"  // find a way to use resources in benchmark tests

        device.wait( // simplify syntax
            Until.findObject(By.textContains(signupLink)),
            1_000
        )?.click() ?: throw IllegalStateException("Signup link not found")

        device.wait(
            Until.findObject(By.textContains(loginLink)), 1_000
        ) ?: throw IllegalStateException("Login link not found")
    }
}
