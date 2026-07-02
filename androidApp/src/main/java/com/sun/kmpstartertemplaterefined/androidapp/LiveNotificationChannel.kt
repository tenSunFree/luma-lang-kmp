package com.sun.kmpstartertemplaterefined.androidapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * Centralized management for live-related notification channels.
 *
 * Why separate channels are needed:
 * - Starting from Android 8.0+, every notification must belong to a channel, and users can
 *   independently enable or disable notifications for a single channel (without affecting other
 *   app notifications, such as push messages).
 * - Here we intentionally split them into two channels because they are two notifications with
 *   different meanings:
 *
 *   1. [CHANNEL_ID_BACKGROUND_PLAYBACK]: shown only when the user explicitly chooses to
 *      "fully go to the background, not enable PiP, and keep audio playing" (together with
 *      LiveBackgroundAudioService). This is a notification that appears only after the user's
 *      choice.
 *
 *   2. [CHANNEL_ID_PIP_CONTROLS]: automatically shown when the user shrinks the app into a
 *      PiP floating window (no ForegroundService is needed, because PiP itself is already a
 *      valid foreground state).
 *      This is a notification that appears as part of the system behavior, and users may see it
 *      more frequently. Therefore, we create a separate channel so users can choose to disable
 *      it independently without affecting background playback or other notification features.
 *
 * IMPORTANCE_LOW: neither of them needs sound or heads-up pop-ups; they only need to stay
 * quietly in the notification drawer.
 */
object LiveNotificationChannel {
    const val CHANNEL_ID_BACKGROUND_PLAYBACK = "live_background_playback"
    const val NOTIFICATION_ID_BACKGROUND_PLAYBACK = 9001
    const val CHANNEL_ID_PIP_CONTROLS = "live_pip_controls"
    const val NOTIFICATION_ID_PIP_CONTROLS = 9002

    fun ensureCreated(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java)
        ensureChannel(
            manager = manager,
            channelId = CHANNEL_ID_BACKGROUND_PLAYBACK,
            name = "直播背景播放",
            description = "切到背景時持續播放直播聲音的通知",
        )
        ensureChannel(
            manager = manager,
            channelId = CHANNEL_ID_PIP_CONTROLS,
            name = "直播懸浮視窗控制",
            description = "縮小成懸浮畫面時顯示的直播控制通知",
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ensureChannel(
        manager: NotificationManager,
        channelId: String,
        name: String,
        description: String,
    ) {
        if (manager.getNotificationChannel(channelId) != null) return
        val channel = NotificationChannel(
            channelId,
            name,
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            this.description = description
            setShowBadge(false)
        }
        manager.createNotificationChannel(channel)
    }
}

