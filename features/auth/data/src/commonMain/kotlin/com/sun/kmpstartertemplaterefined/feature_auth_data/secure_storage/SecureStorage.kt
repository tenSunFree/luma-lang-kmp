package com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage

/**
 * A shared interface for secure cross-platform storage.
 * - Android：DataStore + Tink AEAD + Android Keystore
 * - iOS：Keychain (platform.Security.*)
 */
interface SecureStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun remove(key: String)
    suspend fun clear()
}