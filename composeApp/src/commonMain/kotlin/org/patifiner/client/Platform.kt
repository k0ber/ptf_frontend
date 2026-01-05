package org.patifiner.client

import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngineFactory
import kotlinx.coroutines.flow.StateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Platform {
    fun engineFactory(): HttpClientEngineFactory<*>
    fun initNapier()
    fun settings(): Settings

    fun appMainScope(): kotlinx.coroutines.CoroutineScope

    fun apiConfig(): ApiConfig

    fun networkObserver(): NetworkObserver
}

data class ApiConfig(val baseUrl: String, val port: Int)

interface NetworkObserver {
    val isOnline: StateFlow<Boolean>
}
