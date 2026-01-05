package org.patifiner.client

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js
import io.ktor.http.URLProtocol
import kotlinx.browser.window

actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = Js
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    actual fun settings(): Settings = StorageSettings()
}

actual class ApiConfig {
    actual val protocol: URLProtocol = URLProtocol.HTTPS
    actual val host: String = window.location.hostname
    actual val port: Int = if (window.location.protocol == "https:") 443 else 80
    val apiBasePath: String = "/api"
    actual val baseUrl: String get() = "${protocol.name.lowercase()}://$host:$port$apiBasePath"
}
