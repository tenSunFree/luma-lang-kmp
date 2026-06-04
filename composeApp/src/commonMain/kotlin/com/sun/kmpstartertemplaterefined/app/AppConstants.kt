package com.sun.kmpstartertemplaterefined.app

import com.sun.kmpstartertemplaterefined.core.platform.Platform
import com.sun.kmpstartertemplaterefined.core.platform.platform

object AppConstants {
    private const val RC_TEST_STORE_API_KEY = "test_afivZbbPFDRKdXEZBSgJGuYCHMt"
    private val RC_PROD_API_KEY = when (platform) {
        is Platform.Android -> "goog_android_api_key"
        is Platform.Ios -> "apple_api_key"
    }
    val REVENUE_CAT_API_KEY = if (platform.debug) RC_TEST_STORE_API_KEY else RC_PROD_API_KEY
    const val GOOGLE_WEB_CLIENT_ID =
        "xxx"

    const val MIXPANEL_API_TOKEN = "add-your-mixpanel-token-here"
}