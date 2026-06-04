package com.sun.kmpstartertemplaterefined.core.di

import android.content.Context
import com.sun.kmpstartertemplaterefined.app.di.initKoin
import org.koin.android.ext.koin.androidContext

fun initKoinAndroid(
    context: Context,
    authBaseUrl: String,
) {
    initKoin(authBaseUrl = authBaseUrl) {
        androidContext(context)
    }
}