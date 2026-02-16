package org.patifiner.client.login.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TokenStorage {
    val tokenFlow: StateFlow<String?>
    var accessToken: String?
    var refreshToken: String?
    fun clear()
}

class TokenStorageImpl(private val settings: Settings) : TokenStorage {
    private val accessKey = "access_token"
    private val refreshKey = "refresh_token"

    private val _tokenFlow = MutableStateFlow(settings.getStringOrNull(accessKey))
    override val tokenFlow: StateFlow<String?> = _tokenFlow

    override var accessToken: String?
        get() = _tokenFlow.value
        set(value) {
            _tokenFlow.value = value
            if (value == null) settings.remove(accessKey) else settings.putString(accessKey, value)
        }

    override var refreshToken: String?
        get() = settings.getStringOrNull(refreshKey)
        set(value) {
            if (value == null) settings.remove(refreshKey) else settings.putString(refreshKey, value)
        }

    override fun clear() {
        accessToken = null
        refreshToken = null
    }
}
