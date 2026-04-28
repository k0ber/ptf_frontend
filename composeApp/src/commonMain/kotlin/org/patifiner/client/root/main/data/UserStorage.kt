package org.patifiner.client.root.main.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.patifiner.client.core.PtfDispatchers
import org.patifiner.client.core.PtfLog
import org.patifiner.client.core.UserDto

const val USER_DRAFT_TAG = "USER_DRAFT"

class UserStorage(private val settings: Settings, private val json: Json, private val dispatchers: PtfDispatchers) {

    init {
        PtfLog.d { "User Storage init ${this.hashCode()}" }
    }

    suspend fun saveDraft(user: UserDto) {
        withContext(dispatchers.io) {
            runCatching { settings.putString(USER_DRAFT_TAG, json.encodeToString(user)) }
        }
    }

    suspend fun getDraft(): UserDto? {
        return withContext(dispatchers.io) {
            val data = settings.getStringOrNull(USER_DRAFT_TAG) ?: return@withContext null
            runCatching { json.decodeFromString<UserDto>(data) }.getOrNull()
        }
    }

    fun getDraftSync(): UserDto? {
        val data = settings.getStringOrNull(USER_DRAFT_TAG) ?: return null
        return runCatching { json.decodeFromString<UserDto>(data) }.getOrNull()
    }

    fun clear() {
        settings.remove(USER_DRAFT_TAG)
        PtfLog.d { "AUTH: UserStorage CLEARED for hc: ${this.hashCode()}" }
    }
}
