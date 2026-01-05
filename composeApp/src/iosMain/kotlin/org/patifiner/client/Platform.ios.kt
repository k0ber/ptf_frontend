package org.patifiner.client

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineScope

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = Darwin
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    @OptIn(ExperimentalSettingsImplementation::class)
    actual fun settings(): Settings {
        val serviceName = "patifiner.app"
        return KeychainSettings(serviceName)
    }

    actual fun appMainScope(): CoroutineScope {
        TODO("Not yet implemented")
    }

    actual fun apiConfig(): ApiConfig {
        TODO("Not yet implemented")
    }

    actual fun networkObserver(): NetworkObserver {
        TODO("Not yet implemented")
    }
}
