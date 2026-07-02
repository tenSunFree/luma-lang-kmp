package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable

/**
 * iOS no-op implementation.
 *
 * The current project uses Agora's own rendering for the live room, and there is no
 * corresponding system-level PiP integration on iOS wired up yet (AVKit-based PiP would
 * require a different rendering pipeline). All methods are no-op so that commonMain calls
 * (LiveRoomScreen) do not encounter errors when compiled for iOS.
 */
actual object LivePipController {
    actual fun setLiveRoomActive(active: Boolean) {
        // iOS no-op
    }

    actual fun setVideoPlaying(playing: Boolean) {
        // iOS no-op
    }

    actual fun setAspectRatio(width: Int, height: Int) {
        // iOS no-op
    }

    actual fun setCourseTitle(title: String) {
        // iOS no-op
    }
}

@Composable
actual fun isInPipMode(): Boolean = false