package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable

/**
 * Cross-platform PiP (picture-in-picture) controller.
 *
 * LiveRoomScreen in commonMain reports through this object whether the user is currently in the live room,
 * whether video has started playing, and the course title, so the Android platform knows when system PiP
 * is available and what title the PiP notification should display.
 *
 * - Android: actually calls Activity's enterPictureInPictureMode() (see androidMain implementation)
 *   and syncs the state to the PiP floating notification (see LivePipNotificationController).
 * - iOS: no-op, because the current project uses Agora's own rendering, with no corresponding system-level PiP integration.
 */
expect object LivePipController {
    /** Whether the user is currently in the live room screen (should be set to false when leaving). */
    fun setLiveRoomActive(active: Boolean)

    /** Whether the live video has actually started displaying frames (recommended to hook when receiving the first remote frame). */
    fun setVideoPlaying(playing: Boolean)

    /** Set the video aspect ratio for use by the PiP window (for example, screen sharing is typically 16:9). */
    fun setAspectRatio(width: Int, height: Int)

    /**
     * Set the current live room course title. The Android platform will use this value as the title
     * in the PiP floating notification; it is recommended to call this once when entering LiveRoomScreen.
     */
    fun setCourseTitle(title: String)
}

/**
 * Read whether the app is currently in system PiP floating window mode with Compose observability
 * (state changes trigger recomposition). Android returns the real state; iOS always returns false.
 */
@Composable
expect fun isInPipMode(): Boolean