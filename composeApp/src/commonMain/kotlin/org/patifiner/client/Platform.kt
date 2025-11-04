package org.patifiner.client

import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngineFactory

expect object Platform {
    fun engineFactory(): HttpClientEngineFactory<*>
    fun initNapier()
    fun settings(): Settings
}
