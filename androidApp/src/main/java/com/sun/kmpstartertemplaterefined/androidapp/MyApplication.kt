package com.sun.kmpstartertemplaterefined.androidapp

import android.app.Application
import com.sun.kmpstartertemplaterefined.app.initKmpApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MyApplication : Application() {

    companion object {
        private const val TAG = "MyApplication"
    }

    override fun onCreate() {
        super.onCreate()
        initKmpApp(
            authBaseUrl = BuildConfig.AUTH_BASE_URL,
            koinConfig = {
                androidLogger()
                androidContext(this@MyApplication)
            }
        )
    }
}

