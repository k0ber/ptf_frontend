package com.patifiner.benchmark.tests

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.patifiner.client.KoinAppConfig
import org.patifiner.client.NetworkObserver
import org.patifiner.client.UNAUTH_CLIENT
import org.patifiner.client.appModule
import org.patifiner.client.root.login.data.AuthRepository
import org.patifiner.client.root.login.data.SessionManager
import org.patifiner.client.root.main.mainModule
import org.patifiner.client.root.rootModule

@RunWith(AndroidJUnit4::class)
abstract class BasePerformanceTest {

    private val testDispatcher = StandardTestDispatcher()
    protected val testScope = TestScope(testDispatcher + Job())

    @get:Rule
    val composeRule =
        createAndroidComposeRule<ComponentActivity>()

    val authRepo = mockk<AuthRepository>(relaxed = true)
    val networkObserver = mockk<NetworkObserver>(relaxed = true)
    val httpClient = mockk<HttpClient>(relaxed = true)
    val sessionManager = mockk<SessionManager>(relaxed = true)

    @Before
    fun baseSetup() {
        stopKoin()

        startKoin {
            androidContext(InstrumentationRegistry.getInstrumentation().targetContext)
            allowOverride(true)
            modules(
                appModule,
                rootModule,
                mainModule,
                module {
                    single {
                        KoinAppConfig(
                            isDev = true,
                            engine = mockk(),
                            apiConfig = mockk(),
                            appScope = testScope
                        )
                    }
                    single { authRepo }
                    single { networkObserver }
                    single { httpClient }
                    single(named(UNAUTH_CLIENT)) { httpClient }
                    single { sessionManager }
                    single<CoroutineScope> { testScope }
                    single<Settings> { MapSettings() }
                })
        }

        every { networkObserver.isOnline } returns MutableStateFlow(true)

        every { sessionManager.accessTokenFlow } returns MutableStateFlow(null)
        every { sessionManager.isIntroRequired } returns false
    }

    @After
    fun baseTearDown() {
        stopKoin()
    }
}
