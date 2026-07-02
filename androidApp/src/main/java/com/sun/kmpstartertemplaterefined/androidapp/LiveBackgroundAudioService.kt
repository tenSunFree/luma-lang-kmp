package com.sun.kmpstartertemplaterefined.androidapp

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.sun.kmpstartertemplaterefined.feature_live_presentation.rtc.RtcEngineHolder

/**
 * Foreground service for live "audio-only background playback."
 *
 * Usage scenario
 * This service is started only when the user is in the live room, explicitly chooses to
 * keep audio playing in the background (instead of using the system PIP), and sends the
 * entire app to the background. If the only goal is to use a PIP floating window, this
 * service is completely unnecessary — in PIP mode the Activity is still within the
 * foreground lifecycle and is not treated as background by the system, so Android 8+
 * background execution limits do not apply.
 *
 * Design principles
 * - Do not create a separate Agora RtcEngine instance; reuse the shared
 *   RtcEngineHolder.engine instance to avoid conflicts or duplicate billing from both
 *   sides joining the channel independently.
 * - When entering the background, call muteLocalVideoStream / stop subscribing to video
 *   (keep audio only) to save power and bandwidth, because the user cannot see the screen
 *   in the background and sending video data is pointless.
 * - START_STICKY: if the system kills the Service because of low memory, it will try to
 *   restart it, but the Intent received by onStartCommand will be null, so you must decide
 *   whether playback should really resume (here we choose not to auto-resume, to avoid the
 *   Service strangely restarting after the user has already ended the live session).
 */
class LiveBackgroundAudioService : Service() {

    companion object {
        const val ACTION_START = "com.sun.kmpstartertemplaterefined.action.START_BG_AUDIO"
        const val ACTION_STOP = "com.sun.kmpstartertemplaterefined.action.STOP_BG_AUDIO"
        const val EXTRA_COURSE_TITLE = "extra_course_title"

        /**
         * Called externally (from the Compose layer) to start background playback.
         *
         * Context.startForegroundService() was added to the Context class only in API 26
         * (Android 8.0). This project has minSdk=24, so on API 24/25 devices this method
         * does not exist at all; calling it directly would throw NoSuchMethodError and crash
         * the app on those older devices.
         *
         * Below API 26 (24/25): the foreground-service concept and its restrictions were only
         * introduced in Android 8.0. Older versions do not have the problem that background
         * service starts are restricted, so calling the normal startService() is sufficient.
         * Inside the Service, startForeground() can still be called to show a notification
         * (startForeground() itself has existed since API 5, so it is not affected; the only
         * limitation here is the Context.startForegroundService() "start method" itself).
         */
        fun start(context: android.content.Context, courseTitle: String) {
            val intent = Intent(context, LiveBackgroundAudioService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_COURSE_TITLE, courseTitle)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        /** Called externally to stop background playback (for example, when the user returns to the app and video should be restored). */
        fun stop(context: android.content.Context) {
            val intent = Intent(context, LiveBackgroundAudioService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> {
                stopPlaybackAndSelf()
                return START_NOT_STICKY
            }

            ACTION_START -> {
                val courseTitle = intent.getStringExtra(EXTRA_COURSE_TITLE) ?: "直播課程"
                LiveNotificationChannel.ensureCreated(this)
                val notification = buildNotification(courseTitle)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    // Android 14+ requires the service type to be specified when starting a foreground service,
                    // and that type must match the foregroundServiceType declared in the Manifest.
                    startForeground(
                        LiveNotificationChannel.NOTIFICATION_ID_BACKGROUND_PLAYBACK,
                        notification,
                        ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK,
                    )
                } else {
                    startForeground(
                        LiveNotificationChannel.NOTIFICATION_ID_BACKGROUND_PLAYBACK,
                        notification
                    )
                }
                muteVideoKeepAudio()
            }
        }
        return START_STICKY
    }

    /**
     * Keep audio only and stop video decoding.
     * Note: this calls the shared engine instance from RtcEngineHolder,
     * so it will not affect the join-channel state created by LiveRtcClassroomView in the foreground,
     * because both are already the same engine and the same channel.
     */
    private fun muteVideoKeepAudio() {
        RtcEngineHolder.engine?.muteLocalVideoStream(true)
        RtcEngineHolder.engine?.muteAllRemoteVideoStreams(true)
        // Keep audio in its original mute state (controlled by LiveRoomScreen's speakerEnabled).
        // There is no need, and it would be wrong, to force audio to unmute here.
    }

    private fun stopPlaybackAndSelf() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    private fun buildNotification(courseTitle: String): Notification {
        val openAppIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val stopIntent = Intent(this, LiveBackgroundAudioService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        return NotificationCompat.Builder(
            this,
            LiveNotificationChannel.CHANNEL_ID_BACKGROUND_PLAYBACK
        )
            .setSmallIcon(android.R.drawable.ic_btn_speak_now) // Recommended to replace with androidApp/res/drawable/ic_notification.xml
            .setContentTitle(courseTitle)
            .setContentText("直播聲音正在背景播放中")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentPendingIntent)
            .addAction(
                android.R.drawable.ic_media_pause,
                "停止播放",
                stopPendingIntent,
            )
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Restore video when the service is destroyed (to avoid the screen still being muted the next time it returns to the foreground).
        RtcEngineHolder.engine?.muteLocalVideoStream(false)
        RtcEngineHolder.engine?.muteAllRemoteVideoStreams(false)
    }
}