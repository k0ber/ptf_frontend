package org.patifiner.client

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Platform {
    private lateinit var appContext: Context

    fun setup(context: Context) {
        appContext = context
    }

    actual fun engineFactory(): HttpClientEngineFactory<*> = OkHttp
    actual fun initNapier() = Napier.base(DebugAntilog())
    actual fun settings(): Settings = SharedPreferencesSettings(appContext.getSharedPreferences("ptf_settings", Context.MODE_PRIVATE))
    actual fun networkObserver(): NetworkObserver = AndroidNetworkObserver(appContext)

    actual fun apiConfig(): ApiConfig = ApiConfig(
        baseUrl = "https://api.patifiner.ru/",
        port = 443
    )

    actual fun appMainScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
