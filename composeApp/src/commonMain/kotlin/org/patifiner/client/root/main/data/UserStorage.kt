package org.patifiner.client.root.main.data

import com.russhwolf.settings.Settings
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.patifiner.client.core.PtfDispatchers
import org.patifiner.client.core.UserDto

class UserStorage(private val settings: Settings, private val json: Json, private val dispatchers: PtfDispatchers) {
    suspend fun saveDraft(user: UserDto) {
        withContext(dispatchers.io) {
            runCatching { settings.putString("user_draft", json.encodeToString(user)) }
        }
    }

    suspend fun getDraft(): UserDto? {
        return withContext(dispatchers.io) {
            val data = settings.getStringOrNull("user_draft") ?: return@withContext null
            runCatching { json.decodeFromString<UserDto>(data) }.getOrNull()
        }
    }

    fun getDraftSync(): UserDto? {
        val data = settings.getStringOrNull("user_draft") ?: return null
        return runCatching { json.decodeFromString<UserDto>(data) }.getOrNull()
    }
}
