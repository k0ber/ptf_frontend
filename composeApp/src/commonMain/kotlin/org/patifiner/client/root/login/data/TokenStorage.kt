package org.patifiner.client.root.login.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.patifiner.client.core.PtfLog

private const val ACCESS_KEY = "access_token"
private const val REFRESH_KEY = "refresh_token"

interface TokenStorage {
    val tokenFlow: StateFlow<String?>
    var accessToken: String?
    var refreshToken: String?
    fun clear()
}

class TokenStorageImpl(private val settings: Settings) : TokenStorage {

    private val _tokenFlow: MutableStateFlow<String?> get() {
        PtfLog.d { "Read Settings for initial token check" }
        return MutableStateFlow(settings.getStringOrNull(ACCESS_KEY))
    }

    override val tokenFlow: StateFlow<String?> = _tokenFlow

    override var accessToken: String?
        get() = _tokenFlow.value
        set(value) {
            _tokenFlow.value = value
            if (value == null) settings.remove(ACCESS_KEY) else settings.putString(ACCESS_KEY, value)
        }

    override var refreshToken: String?
        get() = settings.getStringOrNull(REFRESH_KEY)
        set(value) {
            if (value == null) settings.remove(REFRESH_KEY) else settings.putString(REFRESH_KEY, value)
        }

    override fun clear() {
        accessToken = null
        refreshToken = null
    }
}
