package com.sun.kmpstartertemplaterefined.feature_live_presentation.background

import androidx.compose.runtime.Composable

/**
 * CommonMain-side bridge interface: allows LiveRoomScreen to call "start/stop background audio playback"
 * without directly depending on Android Service.
 *
 * Usage example (add to LiveRoomScreen.kt):
 *
 *     var backgroundPlaybackEnabled by remember { mutableStateOf(false) }
 *
 *     DisposableEffect(Unit) {
 *         onDispose {
 *             if (backgroundPlaybackEnabled) {
 *                 LiveBackgroundPlayback.stop()
 *             }
 *         }
 *     }
 *
 *     // Add a toggle Icon/Switch to the Header:
 *     IconButton(onClick = {
 *         backgroundPlaybackEnabled = !backgroundPlaybackEnabled
 *         if (backgroundPlaybackEnabled) {
 *             LiveBackgroundPlayback.start(courseTitle = course.title)
 *         } else {
 *             LiveBackgroundPlayback.stop()
 *         }
 *     }) { ... }
 *
 * Note: this is intentionally NOT automatically triggered when "the app enters the background"; instead,
 * let the user decide whether to enable background playback. Reason: most live-streaming apps have the
 * expected behavior "shrink = PIP to watch," "leave completely = stop playback." Only a few users want
 * to "listen while doing other things, like a podcast." This should be an explicit user choice, not an
 * implicit behavior, otherwise it can be misunderstood as draining power or secretly using data.
 */
expect object LiveBackgroundPlayback {
    fun start(courseTitle: String)
    fun stop()
}

/**
 * Whether notification permission has been granted (required on Android 13+; always true on iOS/older Android).
 * Should be checked before starting background playback; if not granted, request it once using the system API.
 */
@Composable
expect fun rememberNotificationPermissionGranted(): Boolean
