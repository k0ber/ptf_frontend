package org.patifiner.client

import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.flow.StateFlow

enum class Os {
    ANDROID, IOS, WEB, DESC
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Platform {
    val os: Os

    fun apiConfig(): ApiConfig
    fun engineFactory(): HttpClientEngineFactory<*>
    fun networkObserver(): NetworkObserver
    fun settings(): Settings
    fun appMainScope(): kotlinx.coroutines.CoroutineScope

    fun onAppInit()
}

data class ApiConfig(val baseUrl: String, val port: Int)

interface NetworkObserver {
    val isOnline: StateFlow<Boolean>
}
