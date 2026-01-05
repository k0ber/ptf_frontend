package org.patifiner.client

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import java.util.prefs.Preferences

actual object Platform {
    actual fun engineFactory(): HttpClientEngineFactory<*> = OkHttp
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    actual fun settings(): Settings {
        val prefs = Preferences.userRoot().node("org.patifiner.client")
        return PreferencesSettings(prefs)
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
