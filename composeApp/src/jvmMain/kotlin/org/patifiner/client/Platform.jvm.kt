package org.patifiner.client

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.prefs.Preferences

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Platform {
    actual val os: Os = Os.DESC

    actual fun apiConfig(): ApiConfig = ApiConfig(baseUrl = "https://api.patifiner.ru", port = 443)
    actual fun engineFactory(): HttpClientEngineFactory<*> = OkHttp
    actual fun networkObserver(): NetworkObserver = JvmNetworkObserver()

    actual fun settings(): Settings = PreferencesSettings(Preferences.userRoot().node("patifiner_settings"))
    actual fun initNapier() = Napier.base(DebugAntilog())

    actual fun appMainScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
