package com.sun.kmpstartertemplaterefined.feature_auth_data.di

import com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage.IosSecureStorage
import com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage.SecureStorage
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun secureStoragePlatformModule(): Module = module {
    single<SecureStorage> { IosSecureStorage() }
}