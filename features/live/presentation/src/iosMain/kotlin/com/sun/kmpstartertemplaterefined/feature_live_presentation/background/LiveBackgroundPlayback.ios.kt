package com.sun.kmpstartertemplaterefined.feature_live_presentation.background

import androidx.compose.runtime.Composable

/**
 * iOS currently does not support background audio playback (it currently uses Agora for custom rendering).
 * Future support will require switching to the `.playback` category of `AVAudioSession` +
 * the Audio, AirPlay, and Picture-in-Picture capabilities of `Background Modes`,
 * and the background task mechanism of `AVAudioSession`. This does not directly correspond to the concept of `ForegroundService` in Android and requires separate design.
 * Currently, all methods are no-op to ensure that `commonMain` calls do not encounter errors.
 */
actual object LiveBackgroundPlayback {
    actual fun start(courseTitle: String) {
        // iOS no-op
    }

    actual fun stop() {
        // iOS no-op
    }
}

@Composable
actual fun rememberNotificationPermissionGranted(): Boolean = true
