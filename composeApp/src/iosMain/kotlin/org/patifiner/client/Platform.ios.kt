package org.patifiner.client

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Platform {
    actual val os: Os = Os.IOS

    actual fun apiConfig(): ApiConfig = ApiConfig("https://api.patifiner.ru/", 443)
    actual fun engineFactory(): HttpClientEngineFactory<*> = Darwin
    actual fun networkObserver(): NetworkObserver = object : NetworkObserver {
        override val isOnline = MutableStateFlow(true)
    }

    @OptIn(ExperimentalSettingsImplementation::class)
    actual fun settings(): Settings = KeychainSettings("patifiner.app")
    actual fun initNapier() = Napier.base(DebugAntilog())
    actual fun appMainScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
