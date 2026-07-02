package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable

/**
 * Cross-platform "PiP notification control" bridge interface.
 *
 * Design principles
 * This notification **completely follows the system PiP state**, not the "app enters background" state:
 * - When watching live in full screen: do not show any notification to avoid disturbing the user.
 * - When shrunk into a system PiP floating window: show a low-intrusion persistent notification
 *   that provides three operations: "Return to live," "mute/unmute," and "stop playback."
 * - When the user returns to full-screen live, leaves the live room, or the live session ends:
 *   update or remove the notification according to the state.
 *
 * Because the Activity is still within the foreground lifecycle in PiP mode (not truly in the background),
 * no ForegroundService is needed to protect the process from termination. Here we simply use
 * NotificationManager to show/update/dismiss, which is much simpler than the background audio
 * playback feature (see LiveBackgroundPlayback)—the two are different concepts and should not
 * share the same code.
 *
 * LiveRoomScreen registers through [registerActions] the logic for "what to do when the user
 * presses the mute/stop button on the notification," and synchronizes the current mute state through
 * [reportMuteState] (whether the change comes from the in-app UI or from the notification button,
 * the notification display must stay in sync with the real state).
 *
 * - Android: actually create/update/dismiss system notifications.
 * - iOS: no-op, because iOS has no corresponding system-level PiP notification concept
 *   (iOS's PiP is a system UI at the AVKit level, and the app does not need, and cannot,
 *   overlay an additional notification).
 */
expect object LivePipNotificationController {
    /**
     * Register the handler logic for notification buttons. LiveRoomScreen should call this once
     * when entering the screen (for example, wrapped in DisposableEffect) and call [unregisterActions]
     * when leaving the screen.
     *
     * @param onToggleMuteRequested The user pressed the "mute/unmute" button on the notification.
     * @param onStopRequested The user pressed the "stop playback" button on the notification;
     *   the implementation should end the live session (equivalent to the user pressing Back to leave).
     */
    fun registerActions(
        onToggleMuteRequested: () -> Unit,
        onStopRequested: () -> Unit,
    )

    /** Unregister (call when the screen leaves to avoid the callback holding a reference to a destroyed screen). */
    fun unregisterActions()

    /**
     * Synchronize the current mute state. This method should be called regardless of whether the state
     * changed from the in-app UI or from the notification button, to ensure that the button text/icon
     * in the currently displayed notification stays in sync with the real state. If no notification is
     * currently shown (for example, still in full screen), this call should only update the internally
     * recorded state and will not actually display a notification.
     */
    fun reportMuteState(muted: Boolean)
}

/**
 * Call early when the screen loads to proactively request notification permission (required on Android 13+).
 *
 * This is intentionally designed to request permission when entering the live room, not when
 * shrinking into PiP: Android's system permission-request dialog needs a foreground UI to pop up.
 * At the moment the user shrinks into PiP (or has already switched to the background), the system
 * has no way to display the permission dialog, so we must request permission early while the user
 * is still in full-screen mode.
 *
 * Not having permission does not affect the live session itself (video and audio work normally);
 * it only means the notification will not be shown when shrinking—this is a reasonable graceful
 * degradation and should not block or interfere with the user watching live.
 */
@Composable
expect fun rememberPipNotificationPermissionGranted(): Boolean
