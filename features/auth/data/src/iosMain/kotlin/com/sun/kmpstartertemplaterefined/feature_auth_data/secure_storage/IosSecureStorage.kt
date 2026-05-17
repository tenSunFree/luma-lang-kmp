package com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRefVar
import platform.Foundation.CFBridgingRelease
import platform.Foundation.NSData
import platform.Foundation.NSMutableDictionary
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.SecItemUpdate
import platform.Security.errSecItemNotFound
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

@OptIn(ExperimentalForeignApi::class)
class IosSecureStorage : SecureStorage {

    // Use your Bundle ID as a service to isolate Keychain data from different apps.
    private val service = "com.sun.kmpstartertemplaterefined.auth"

    override suspend fun putString(key: String, value: String) {
        val data = value.toNSData() ?: return
        // Try updating (an existing key) first.
        val query = buildQuery(key)
        val attributes = NSMutableDictionary()
        attributes.setObject(data, forKey = kSecValueData)
        val updateStatus = SecItemUpdate(query as CFDictionaryRef, attributes as CFDictionaryRef)
        if (updateStatus == errSecItemNotFound) {
            // Add if it doesn't exist
            val addQuery = buildQuery(key).also {
                it[kSecValueData] = data
                it[kSecAttrAccessible] = kSecAttrAccessibleAfterFirstUnlockThisDeviceOnly
            }
            SecItemAdd(addQuery as CFDictionaryRef, null)
        }
    }

    override suspend fun getString(key: String): String? = memScoped {
        val query = buildQuery(key).also {
            it[kSecReturnData] = true
            it[kSecMatchLimit] = kSecMatchLimitOne
        }
        val result = alloc<CFTypeRefVar>()
        val status = SecItemCopyMatching(query as CFDictionaryRef, result.ptr)
        if (status != errSecSuccess) return@memScoped null
        val nsData = CFBridgingRelease(result.value) as? NSData ?: return@memScoped null
        NSString.create(data = nsData, encoding = NSUTF8StringEncoding) as? String
    }

    override suspend fun remove(key: String) {
        SecItemDelete(buildQuery(key) as CFDictionaryRef)
    }

    override suspend fun clear() {
        val query = NSMutableDictionary()
        query[kSecClass] = kSecClassGenericPassword
        query[kSecAttrService] = service
        SecItemDelete(query as CFDictionaryRef)
    }

    private fun buildQuery(key: String): NSMutableDictionary {
        val q = NSMutableDictionary()
        q[kSecClass] = kSecClassGenericPassword
        q[kSecAttrService] = service
        q[kSecAttrAccount] = key
        return q
    }

    private fun String.toNSData(): NSData? =
        (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)
}