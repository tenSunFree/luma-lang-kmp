package com.sun.kmpstartertemplaterefined.core.ui.screens.live.rtc

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnAttach
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas
import androidx.core.view.isEmpty

private val mainHandler = Handler(Looper.getMainLooper())

@Composable
actual fun LiveRtcVideoView(
    modifier: Modifier,
    session: LiveRtcSession,
    showLocalPreview: Boolean,
) {
    val context = LocalContext.current
    val container = remember { FrameLayout(context) }

    val rtcEngine = remember {
        val config = RtcEngineConfig().apply {
            mContext = context.applicationContext
            mAppId = session.appId
            mEventHandler = object : IRtcEngineEventHandler() {
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    android.util.Log.d("AgoraRTC", "加入頻道成功: $channel, uid=$uid")
                }

                override fun onUserJoined(uid: Int, elapsed: Int) {
                    android.util.Log.d("AgoraRTC", "遠端用戶加入(後進): uid=$uid")
                    setupRemoteView(uid, container, context)
                }

                // Handling situations where "you came in later, but the teacher went first"
                override fun onRemoteVideoStateChanged(
                    uid: Int,
                    state: Int,
                    reason: Int,
                    elapsed: Int
                ) {
                    android.util.Log.d(
                        "AgoraRTC",
                        "遠端視訊狀態: uid=$uid, state=$state, reason=$reason"
                    )
                    // state=2 indicates that the remote video signal has started decoding (there is a picture now).
                    if (state == Constants.REMOTE_VIDEO_STATE_DECODING) {
                        setupRemoteView(uid, container, context)
                    }
                }

                override fun onUserOffline(uid: Int, reason: Int) {
                    android.util.Log.d("AgoraRTC", "遠端用戶離線: uid=$uid")
                    // Switch back to the main thread and then operate the View
                    mainHandler.post {
                        container.removeAllViews()
                    }
                }

                override fun onError(err: Int) {
                    android.util.Log.e("AgoraRTC", "Agora 錯誤碼: $err")
                }
            }
        }
        RtcEngine.create(config).also { RtcEngineHolder.engine = it }
    }
    AndroidView(
        modifier = modifier,
        factory = { container },
        update = { view ->
            view.requestLayout()
            view.invalidate()
        }
    )
    DisposableEffect(session.channelName) {
        rtcEngine.enableVideo()
        rtcEngine.setDefaultAudioRoutetoSpeakerphone(true)
        val options = ChannelMediaOptions().apply {
            clientRoleType = Constants.CLIENT_ROLE_AUDIENCE
            channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING
            publishCameraTrack = false
            publishMicrophoneTrack = false
            autoSubscribeAudio = true
            autoSubscribeVideo = true
        }
        val result = rtcEngine.joinChannel(session.token, session.channelName, session.uid, options)
        android.util.Log.d("AgoraRTC", "joinChannel result=$result (0=成功)")
        onDispose {
            rtcEngine.leaveChannel()
            RtcEngine.destroy()
            RtcEngineHolder.engine = null
        }
    }
}

// Both callbacks can be called, and it is ensured that they execute on the main thread.
private fun setupRemoteView(
    uid: Int,
    container: FrameLayout,
    context: android.content.Context
) {
    fun bindVideo() {
        if (container.isEmpty()) {
            val remoteView = SurfaceView(context).apply {
                setZOrderMediaOverlay(false)
            }
            container.addView(
                remoteView,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                )
            )
            RtcEngineHolder.engine?.setupRemoteVideo(
                VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid)
            )
            container.requestLayout()
            container.invalidate()
            android.util.Log.d("AgoraRTC", "setupRemoteVideo 完成: uid=$uid")
        } else {
            val view = container.getChildAt(0)
            RtcEngineHolder.engine?.setupRemoteVideo(
                VideoCanvas(view, VideoCanvas.RENDER_MODE_HIDDEN, uid)
            )
            container.requestLayout()
            container.invalidate()

            android.util.Log.d("AgoraRTC", "重新綁定 remote video: uid=$uid")
        }
    }
    mainHandler.post {
        if (container.isAttachedToWindow) {
            bindVideo()
        } else {
            container.doOnAttach {
                bindVideo()
            }
        }
    }
}

private object RtcEngineHolder {
    var engine: RtcEngine? = null
}