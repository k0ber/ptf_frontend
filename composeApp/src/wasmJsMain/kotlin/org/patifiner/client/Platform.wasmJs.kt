package org.patifiner.client

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = Js
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    actual fun settings(): Settings = StorageSettings()
}
