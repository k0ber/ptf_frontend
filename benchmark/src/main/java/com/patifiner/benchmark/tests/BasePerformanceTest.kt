package com.patifiner.benchmark.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.patifiner.client.NetworkObserver
import org.patifiner.client.appModule
import org.patifiner.client.login.data.AuthRepository

@RunWith(AndroidJUnit4::class)
abstract class BasePerformanceTest {

    @get:Rule
    val composeRule =
        createAndroidComposeRule<ComponentActivity>()

    val authRepo = mockk<AuthRepository>(relaxed = true)
    val networkObserver = mockk<NetworkObserver>(relaxed = true)
    val httpClient = mockk<HttpClient>(relaxed = true)

    @Before
    fun baseSetup() {
        isAssertOnMainThreadEnabled = false

        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            allowOverride(true)
            modules(appModule, module {
                single { authRepo }
                single { networkObserver }
                single { httpClient }
                single<Settings> { MapSettings() }
            })
        }

        every { authRepo.tokenFlow } returns MutableStateFlow(null)
        every { networkObserver.isOnline } returns MutableStateFlow(true)
    }

    @After
    fun baseTearDown() {
        stopKoin()
        isAssertOnMainThreadEnabled = true
    }
}
