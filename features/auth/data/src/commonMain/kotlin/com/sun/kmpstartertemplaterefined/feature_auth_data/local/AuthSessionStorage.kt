package com.sun.kmpstartertemplaterefined.feature_auth_data.local

import com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage.SecureStorage
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.UserSession
import kotlinx.serialization.json.Json

/**
 * Encapsulates all read and write operations of UserSession, and the ViewModel does not directly touch SecureStorage.
 */
class AuthSessionStorage(
    private val secureStorage: SecureStorage,
    private val json: Json,
) {
    companion object {
        private const val KEY_SESSION = "user_session"
    }

    suspend fun saveSession(session: UserSession) {
        secureStorage.putString(KEY_SESSION, json.encodeToString(session))
    }

    suspend fun getSession(): UserSession? {
        val raw = secureStorage.getString(KEY_SESSION) ?: return null
        return runCatching { json.decodeFromString<UserSession>(raw) }.getOrNull()
    }

    /**
     * After a token rotate, only the token pair is updated; other user information remains unchanged.
     */
    suspend fun updateTokens(token: String, refreshToken: String) {
        val old = getSession() ?: return
        saveSession(old.copy(token = token, refreshToken = refreshToken))
    }

    suspend fun clearSession() {
        secureStorage.remove(KEY_SESSION)
    }

    suspend fun isLoggedIn(): Boolean = getSession() != null
}