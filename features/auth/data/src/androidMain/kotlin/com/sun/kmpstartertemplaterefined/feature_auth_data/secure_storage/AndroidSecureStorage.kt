package com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage

import android.content.Context
import android.util.Base64
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import kotlinx.coroutines.flow.first

// The DataStore name is isolated from the settings.preferences_pb used by TokenDataStore.
private val Context.secureDataStore by preferencesDataStore(name = "secure_auth_storage")

class AndroidSecureStorage(
    private val context: Context,
) : SecureStorage {

    /**
     * Tink AEAD: Manages encryption keys using the Android Keystore.
     * AES256_GCM provides authentication encryption to prevent tampering and unauthorized reading.
     */
    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(
                context,
                "auth_keyset",          // keyset name
                "auth_keyset_prefs"    // SharedPreferences filename (stores keyset metadata)
            )
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri("android-keystore://kmp_auth_master_key")
            .build()
            .keysetHandle
            .getPrimitive(RegistryConfiguration.get(), Aead::class.java)
    }

    override suspend fun putString(key: String, value: String) {
        val encrypted = Base64.encodeToString(
            aead.encrypt(value.toByteArray(Charsets.UTF_8), null),
            Base64.NO_WRAP
        )
        context.secureDataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = encrypted
        }
    }

    override suspend fun getString(key: String): String? {
        val prefs = context.secureDataStore.data.first()
        val encrypted = prefs[stringPreferencesKey(key)] ?: return null
        return runCatching {
            val bytes = aead.decrypt(Base64.decode(encrypted, Base64.NO_WRAP), null)
            bytes.toString(Charsets.UTF_8)
        }.getOrNull()
    }

    override suspend fun remove(key: String) {
        context.secureDataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey(key))
        }
    }

    override suspend fun clear() {
        context.secureDataStore.edit { it.clear() }
    }
}