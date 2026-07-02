package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

/**
 * Bridge object between the androidApp module (which actually owns the NotificationManager /
 * BroadcastReceiver code) and the feature_live_presentation module (which owns the Compose UI logic).
 *
 * Maintains the same design principle as [AndroidLivePipState]: the feature module does not depend on
 * the androidApp module; instead, it communicates one-way through "callback registration":
 *
 * - LiveRoomScreen (commonMain, via LivePipNotificationController.android.kt)
 *   registers what "mute/stop" actions should do in [onToggleMuteRequested] / [onStopRequested].
 * - When LivePipNotificationActionReceiver in androidApp receives a broadcast from the notification button,
 *   it calls these two callbacks to trigger the actual behavior on the Compose side "across modules,"
 *   without needing androidApp to directly import Compose code from feature_live_presentation.
 *
 * [isMuted] is the cached mute state as currently known, allowing androidApp to perform an "optimistic update"
 * when receiving a "mute/unmute" broadcast: without waiting for the Compose side to actually finish processing
 * and recompose, it can immediately flip this value and update the button text in the notification. The user will
 * not feel any delay after clicking the button. The Compose side will later report the true state via
 * [LivePipNotificationController.reportMuteState], and both sides will eventually be consistent.
 */
object AndroidLivePipNotificationBridge {
    var onToggleMuteRequested: (() -> Unit)? = null
    var onStopRequested: (() -> Unit)? = null

    /** Cached mute state as currently known (used for optimistic updates, not the single source of truth). */
    @Volatile
    var isMuted: Boolean = false

    /**
     * MainActivity in androidApp will register this callback to immediately update the notification content
     * currently being displayed when [isMuted] changes (if no notification is currently shown, the androidApp
     * implementation should simply ignore it).
     */
    var onMutedStateChanged: ((Boolean) -> Unit)? = null

    fun notifyToggleMuteRequested() {
        onToggleMuteRequested?.invoke()
    }

    fun notifyStopRequested() {
        onStopRequested?.invoke()
    }

    fun reportMuteState(muted: Boolean) {
        isMuted = muted
        onMutedStateChanged?.invoke(muted)
    }
}