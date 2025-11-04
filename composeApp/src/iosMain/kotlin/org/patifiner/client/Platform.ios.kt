package org.patifiner.client

import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = Darwin
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    actual fun settings(): Settings {
        val serviceName = "patifiner.app"
        return KeychainSettings(serviceName)
    }
}
