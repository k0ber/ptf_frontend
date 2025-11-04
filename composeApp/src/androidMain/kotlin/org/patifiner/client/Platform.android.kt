package org.patifiner.client

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual object Platform {
    lateinit var appContext: Context

    actual fun engineFactory(): HttpClientEngineFactory<*> = OkHttp
    actual fun initNapier() {
        Napier.base(DebugAntilog())
    }

    actual fun settings(): Settings {
        val prefs = appContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return SharedPreferencesSettings(prefs)
    }
}
