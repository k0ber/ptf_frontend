package org.patifiner.client.login.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface TokenStorage {
    val tokenFlow: StateFlow<String?>
    var token: String?
    fun clear()
}

class TokenStorageImpl(private val settings: Settings) : TokenStorage {
    private val key = "auth_token"
    private val _tokenFlow = MutableStateFlow(settings.getStringOrNull(key))
    override val tokenFlow: StateFlow<String?> = _tokenFlow

    override var token: String?
        get() = _tokenFlow.value
        set(value) {
            _tokenFlow.value = value
            if (value == null) settings.remove(key) else settings.putString(key, value)
        }

    override fun clear() { token = null }
}
