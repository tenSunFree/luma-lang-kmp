package com.sun.kmpstartertemplaterefined.feature_live_presentation.background

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Important: bridge constraint
 * LiveBackgroundPlayback in commonMain is an expect object and cannot directly hold a reference to the
 * LiveBackgroundAudioService class from the androidApp module (to avoid circular dependency:
 * feature_live_presentation should not depend back on androidApp).
 *
 * Solution: use the same trick as LivePipController—in this androidMain file, start the Service using
 * "string + reflection-free explicit component Intent," or the cleaner approach is to have androidApp
 * register a callback at startup, consistent with how AndroidLivePipState.onStateChanged works.
 * Below we use the latter approach.
 */
object AndroidLiveBackgroundPlaybackBridge {
    var onStart: ((courseTitle: String) -> Unit)? = null
    var onStop: (() -> Unit)? = null
}

actual object LiveBackgroundPlayback {
    actual fun start(courseTitle: String) {
        AndroidLiveBackgroundPlaybackBridge.onStart?.invoke(courseTitle)
    }

    actual fun stop() {
        AndroidLiveBackgroundPlaybackBridge.onStop?.invoke()
    }
}

@Composable
actual fun rememberNotificationPermissionGranted(): Boolean {
    val context = LocalContext.current
    var granted by remember {
        mutableStateOf(hasNotificationPermission(context))
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        granted = isGranted
    }
    // If we still don't have permission and it's Android 13+, proactively launch the system permission request.
    // On Android 13 and below, notification permission is open by default and does not need to be requested.
    if (!granted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        androidx.compose.runtime.LaunchedEffect(Unit) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    return granted
}

private fun hasNotificationPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS,
    ) == PackageManager.PERMISSION_GRANTED
}
