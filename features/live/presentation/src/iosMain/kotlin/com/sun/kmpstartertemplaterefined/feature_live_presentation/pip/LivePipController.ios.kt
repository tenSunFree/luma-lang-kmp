package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable

/**
 * iOS does not have a corresponding "System PiP Notification" concept—iOS's picture-in-picture functionality is AVKit-level.
 * The system UI already provides back/control gestures; apps do not need, and should not, add additional gestures.
 * One notification. All methods are no-op to ensure that commonMain calls do not encounter errors.
 */
actual object LivePipNotificationController {
    actual fun registerActions(
        onToggleMuteRequested: () -> Unit,
        onStopRequested: () -> Unit,
    ) {
        // iOS no-op
    }

    actual fun unregisterActions() {
        // iOS no-op
    }

    actual fun reportMuteState(muted: Boolean) {
        // iOS no-op
    }
}

@Composable
actual fun rememberPipNotificationPermissionGranted(): Boolean = true