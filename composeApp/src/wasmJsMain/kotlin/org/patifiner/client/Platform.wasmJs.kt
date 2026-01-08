package org.patifiner.client

import com.russhwolf.settings.Settings
import com.russhwolf.settings.StorageSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = Js
    actual fun initNapier() = Napier.base(DebugAntilog())
    actual fun settings(): Settings = StorageSettings()
    actual fun appMainScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    actual fun apiConfig(): ApiConfig = ApiConfig(baseUrl = "https://api.patifiner.ru", port = 443)

    actual fun networkObserver(): NetworkObserver = object : NetworkObserver {
        override val isOnline: StateFlow<Boolean>
            get() = MutableStateFlow(true)
    }
}
