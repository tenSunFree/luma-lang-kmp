package com.sun.kmpstartertemplaterefined.feature_auth_data.datastore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sun.kmpstartertemplaterefined.utils.datastore.AppDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenDataStore(appDataStore: AppDataStore) {

    private val dataStore = appDataStore.dataStore

    companion object {
        private val KEY_TOKEN = stringPreferencesKey("auth_token")
        private val KEY_REFRESH_TOKEN = stringPreferencesKey("auth_refresh_token")
        private val KEY_USER_ID = stringPreferencesKey("auth_user_id")
    }

    suspend fun saveTokens(token: String, refreshToken: String, userId: String) {
        dataStore.edit { prefs ->
            prefs[KEY_TOKEN] = token
            prefs[KEY_REFRESH_TOKEN] = refreshToken
            prefs[KEY_USER_ID] = userId
        }
    }

    suspend fun getToken(): String? =
        dataStore.data.map { it[KEY_TOKEN] }.firstOrNull()

    suspend fun getRefreshToken(): String? =
        dataStore.data.map { it[KEY_REFRESH_TOKEN] }.firstOrNull()

    suspend fun isLoggedIn(): Boolean = getToken() != null

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}