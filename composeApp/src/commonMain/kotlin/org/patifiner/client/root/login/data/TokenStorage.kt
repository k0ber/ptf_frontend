package org.patifiner.client.root.login.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val ACCESS_KEY = "access_token"
private const val REFRESH_KEY = "refresh_token"
private const val INTRO_REQUIRED_KEY = "intro_required"

interface TokenStorage {
    var isIntroRequired: Boolean // todo: use state flow instead?
    val tokenFlow: StateFlow<String?>
    var accessToken: String?
    var refreshToken: String?
    fun clear()
}

class TokenStorageImpl(private val settings: Settings) : TokenStorage {

    private val _tokenFlow: MutableStateFlow<String?> = MutableStateFlow(settings.getStringOrNull(ACCESS_KEY))

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

    override var isIntroRequired: Boolean // not related to tokens, shouldn't be here
        get() = settings.getBoolean(INTRO_REQUIRED_KEY, false)
        set(value) = settings.putBoolean(INTRO_REQUIRED_KEY, value)


    override fun clear() {
        accessToken = null
        refreshToken = null
        isIntroRequired = false
    }
}
