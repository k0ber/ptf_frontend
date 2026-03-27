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
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.patifiner.client.core.PtfDispatchers

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Platform : KoinComponent {
    actual val os: Os = Os.ANDROID

    actual fun apiConfig(): ApiConfig = ApiConfig(baseUrl = "https://api.patifiner.ru/", port = 443)
    actual fun engineFactory(): HttpClientEngineFactory<*> = OkHttp
    actual fun networkObserver(): NetworkObserver {
        val context: Context = get()
        return AndroidNetworkObserver(context)
    }

    actual fun settings(): Settings {
        val context: Context = get()
        return SharedPreferencesSettings(context.getSharedPreferences("ptf_settings", Context.MODE_PRIVATE))
    }

    actual fun appMainScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    actual fun dispatchers(): PtfDispatchers = object : PtfDispatchers {
        override val main = Dispatchers.Main.immediate
        override val default = Dispatchers.Default
        override val io = Dispatchers.IO
    }

    actual fun onAppInit() {
        Napier.base(DebugAntilog())
    }
}
