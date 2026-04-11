package org.patifiner.client.root.login.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.patifiner.client.root.main.data.USER_DRAFT_TAG

private const val ACCESS_KEY = "access_token"
private const val REFRESH_KEY = "refresh_token"
private const val INTRO_REQUIRED_KEY = "intro_required"
private const val USER_ID_KEY = "user_id"

class SessionStorage(private val settings: Settings) {
    private val _tokenFlow = MutableStateFlow(settings.getStringOrNull(ACCESS_KEY))
    val tokenFlow: StateFlow<String?> = _tokenFlow

    var accessToken: String?
        get() = _tokenFlow.value
        set(value) {
            _tokenFlow.value = value
            if (value == null) settings.remove(ACCESS_KEY) else settings.putString(ACCESS_KEY, value)
        }

    var refreshToken: String?
        get() = settings.getStringOrNull(REFRESH_KEY)
        set(value) {
            if (value == null) settings.remove(REFRESH_KEY) else settings.putString(REFRESH_KEY, value)
        }

    var userId: String?
        get() = settings.getStringOrNull(USER_ID_KEY)
        set(value) {
            if (value == null) settings.remove(USER_ID_KEY) else settings.putString(USER_ID_KEY, value)
        }

    var isIntroRequired: Boolean
        get() = settings.getBoolean(INTRO_REQUIRED_KEY, false)
        set(value) = settings.putBoolean(INTRO_REQUIRED_KEY, value)

    fun clear() {
        accessToken = null
        refreshToken = null
        userId = null
        isIntroRequired = false
    }
}
