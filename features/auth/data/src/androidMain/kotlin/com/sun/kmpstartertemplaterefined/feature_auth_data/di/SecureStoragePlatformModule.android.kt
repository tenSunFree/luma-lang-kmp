package com.sun.kmpstartertemplaterefined.feature_auth_data.di

import com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage.AndroidSecureStorage
import com.sun.kmpstartertemplaterefined.feature_auth_data.secure_storage.SecureStorage
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

actual fun secureStoragePlatformModule(): Module = module {
    single<SecureStorage> { AndroidSecureStorage(context = androidContext()) }
}