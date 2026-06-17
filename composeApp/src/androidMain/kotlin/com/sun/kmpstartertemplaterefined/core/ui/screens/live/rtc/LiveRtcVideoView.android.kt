package com.sun.kmpstartertemplaterefined.core.ui.screens.live.rtc

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import android.widget.FrameLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.doOnAttach
import io.agora.rtc2.*
import io.agora.rtc2.video.VideoCanvas

private val mainHandler = Handler(Looper.getMainLooper())

// Use container.tag to remember the bound SurfaceView,
// to avoid cutting and rebuilding it every time you DECODING.
private data class RemoteViewBinding(
    val uid: Int,
    val surfaceView: SurfaceView,
)

// Introducing "Stable SurfaceView Reuse": Remembers the bound (uid, SurfaceView) using container.tag. DECODING no longer rebuilds it; it's only rebuilt during FROZEN/FAILED or the first time it's created (forceRecreate).
// Uses the standard SurfaceView(context) (CreateRendererView has been removed in Agora 4.x).
// Changes renderMode for screen sharing to RENDER_MODE_FIT (does not crop desktop content).
// Changes AndroidView.update{} to post{} to avoid conflicts caused by synchronous requestLayout triggered during layout pass.
// Performs a reuse bind onFirstRemoteVideoFrame to ensure the first frame is stable.
// Clears container.tag onUserOffline to avoid misjudgments during subsequent reconnections.
// Main Composable:One engine, one join, two views
@Composable
actual fun LiveRtcClassroomView(
    modifier: Modifier,
    session: LiveRtcSession,
    screenUid: Int,
    cameraUid: Int,
    showCamera: Boolean,
    speakerEnabled: Boolean,
) {
    val context = LocalContext.current
    // Two stable containers, created only once throughout the entire Composable lifecycle.
    val screenContainer = remember { FrameLayout(context) }
    val cameraContainer = remember { FrameLayout(context) }
    // Only one RtcEngine is created; it is rebuilt only if the channelName changes.
    val rtcEngine = remember(session.channelName) {
        val config = RtcEngineConfig().apply {
            mContext = context.applicationContext
            mAppId = session.appId
            mEventHandler = object : IRtcEngineEventHandler() {
                // Successfully joined the channel
                override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
                    android.util.Log.d(
                        "AgoraRTC[classroom]", "加入頻道成功 channel=$channel localUid=$uid"
                    )
                }

                // Triggered when the App enters the channel later than the teacher's app.
                override fun onUserJoined(uid: Int, elapsed: Int) {
                    android.util.Log.d("AgoraRTC[classroom]", "遠端用戶加入 uid=$uid")
                    val engine = RtcEngineHolder.engine ?: return
                    when (uid) {
                        screenUid -> setupRemoteView(
                            uid = uid,
                            container = screenContainer,
                            context = context,
                            engine = engine,
                            overlay = false,
                            label = "screen",
                            renderMode = VideoCanvas.RENDER_MODE_FIT, // Screen sharing without cropping
                            forceRecreate = false,
                        )

                        cameraUid -> setupRemoteView(
                            uid = uid,
                            container = cameraContainer,
                            context = context,
                            engine = engine,
                            overlay = true,
                            label = "camera",
                            renderMode = VideoCanvas.RENDER_MODE_HIDDEN, // Camera window can be cropped
                            forceRecreate = false,
                        )
                    }
                }

                // Changes in remote video status
                // DECODING: Reuse only, do not rebuild
                // FROZEN / FAILED: Force rebuild only (forceRecreate=true)
                override fun onRemoteVideoStateChanged(
                    uid: Int, state: Int, reason: Int, elapsed: Int
                ) {
                    android.util.Log.d(
                        "AgoraRTC[classroom]", "遠端視訊狀態 uid=$uid state=$state reason=$reason"
                    )
                    val engine = RtcEngineHolder.engine ?: return
                    when (uid) {
                        screenUid -> when (state) {
                            Constants.REMOTE_VIDEO_STATE_DECODING -> {
                                // Only reuse, do not rebuild SurfaceView
                                setupRemoteView(
                                    uid = uid,
                                    container = screenContainer,
                                    context = context,
                                    engine = engine,
                                    overlay = false,
                                    label = "screen",
                                    renderMode = VideoCanvas.RENDER_MODE_FIT,
                                    forceRecreate = false,
                                )
                            }

                            Constants.REMOTE_VIDEO_STATE_FROZEN -> {
                                // FROZEN = Network jitter, Agora will recover on its own, do not touch the SurfaceView
                                // reason=1(NETWORK) Most common, will automatically return to DECODING after 1~2 seconds.
                                // If forceRecreate is used here, rebuilding the SurfaceView will trigger a new
                                // FROZEN, causing a "black→bright→black" loop every second.
                                android.util.Log.w(
                                    "AgoraRTC[classroom]",
                                    "screen FROZEN uid=$uid reason=$reason → 等 Agora 自動恢復，不重建"
                                )
                                // Do nothing and let Agora buffer back on its own.
                            }

                            Constants.REMOTE_VIDEO_STATE_FAILED -> {
                                // Only when it's truly dead does it require a forced rebuild.
                                android.util.Log.e(
                                    "AgoraRTC[classroom]",
                                    "screen FAILED uid=$uid reason=$reason → force rebind"
                                )
                                setupRemoteView(
                                    uid = uid,
                                    container = screenContainer,
                                    context = context,
                                    engine = engine,
                                    overlay = false,
                                    label = "screen",
                                    renderMode = VideoCanvas.RENDER_MODE_FIT,
                                    forceRecreate = true,
                                )
                            }
                        }

                        cameraUid -> {
                            if (state == Constants.REMOTE_VIDEO_STATE_DECODING) {
                                setupRemoteView(
                                    uid = uid,
                                    container = cameraContainer,
                                    context = context,
                                    engine = engine,
                                    overlay = true,
                                    label = "camera",
                                    renderMode = VideoCanvas.RENDER_MODE_HIDDEN,
                                    forceRecreate = false,
                                )
                            }
                        }
                    }
                }

                // First frame arrives (for safety, to ensure stable binding of the first frame)
                override fun onFirstRemoteVideoFrame(
                    uid: Int, width: Int, height: Int, elapsed: Int
                ) {
                    android.util.Log.d(
                        "AgoraRTC[classroom]", "first remote frame uid=$uid ${width}x${height}"
                    )
                    val engine = RtcEngineHolder.engine ?: return
                    when (uid) {
                        screenUid -> setupRemoteView(
                            uid = uid,
                            container = screenContainer,
                            context = context,
                            engine = engine,
                            overlay = false,
                            label = "screen-first-frame",
                            renderMode = VideoCanvas.RENDER_MODE_FIT,
                            forceRecreate = false,  // reuse, do not rebuild
                        )

                        cameraUid -> setupRemoteView(
                            uid = uid,
                            container = cameraContainer,
                            context = context,
                            engine = engine,
                            overlay = true,
                            label = "camera-first-frame",
                            renderMode = VideoCanvas.RENDER_MODE_HIDDEN,
                            forceRecreate = false,
                        )
                    }
                }

                // User offline: Clear the tag so that it can be correctly rebuilt on the next reconnection.
                override fun onUserOffline(uid: Int, reason: Int) {
                    android.util.Log.d(
                        "AgoraRTC[classroom]", "用戶離線 uid=$uid reason=$reason"
                    )
                    mainHandler.post {
                        when (uid) {
                            screenUid -> {
                                screenContainer.removeAllViews()
                                screenContainer.tag = null  // Clear old bindings
                            }

                            cameraUid -> {
                                cameraContainer.removeAllViews()
                                cameraContainer.tag = null
                            }
                        }
                    }
                }

                override fun onError(err: Int) {
                    android.util.Log.e("AgoraRTC[classroom]", "錯誤碼 err=$err")
                }
            }
        }
        RtcEngine.create(config).also { engine ->
            RtcEngineHolder.engine = engine
            android.util.Log.d("AgoraRTC[classroom]", "RtcEngine created")
        }
    }
    // UI layer: main screen + small window in the upper right corner
    Box(modifier = modifier) {
        // Main screen: Teacher's screen sharing (uid=2000)
        // Update{} is replaced with post{} to avoid conflicts
        // or blackouts caused by triggering synchronous requestLayout during Compose layout pass.
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { screenContainer },
            update = { container ->
                container.post {
                    container.requestLayout()
                    container.invalidate()
                    (container.getChildAt(0) as? SurfaceView)?.invalidate()
                }
            },
        )
        // Top right corner small window: Teacher's camera (uid=1000)
        if (showCamera) {
            AndroidView(
                modifier = Modifier.width(120.dp).height(90.dp).align(Alignment.TopEnd),
                factory = { cameraContainer },
                update = { container ->
                    container.post {
                        container.requestLayout()
                        container.invalidate()
                        (container.getChildAt(0) as? SurfaceView)?.invalidate()
                    }
                },
            )
        }
    }
    // Join Channel / Leave Channel
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
        val result = rtcEngine.joinChannel(
            session.token,
            session.channelName,
            session.uid,
            options,
        )
        android.util.Log.d(
            "AgoraRTC[classroom]",
            "joinChannel result=$result (0=成功) channel=${session.channelName} uid=${session.uid}"
        )
        rtcEngine.muteAllRemoteAudioStreams(!speakerEnabled)
        rtcEngine.adjustPlaybackSignalVolume(if (speakerEnabled) 100 else 0)
        onDispose {
            // Clear all binding tags before leaving to avoid residue.
            screenContainer.removeAllViews()
            screenContainer.tag = null
            cameraContainer.removeAllViews()
            cameraContainer.tag = null
            rtcEngine.leaveChannel()
            RtcEngine.destroy()
            RtcEngineHolder.engine = null
            android.util.Log.d("AgoraRTC[classroom]", "engine destroyed")
        }
    }
    // The horn switch takes effect immediately
    LaunchedEffect(speakerEnabled) {
        rtcEngine.muteAllRemoteAudioStreams(!speakerEnabled)
        rtcEngine.adjustPlaybackSignalVolume(if (speakerEnabled) 100 else 0)
    }
}

// setupRemoteView: Stable Core Reuse
// Strategy:
// - container.tag stores RemoteViewBinding(uid, surfaceView)
// - If tag exists && uid is the same && surfaceView is still in container
// → Direct reuse, only rerun setupRemoteVideo() (rebinding Agora)
// - Otherwise (first time / forceRecreate / uid changed) → Rebuild SurfaceView
// Solves the problem of "every DECODING cancels rebuild → transitional black screen".
private fun setupRemoteView(
    uid: Int,
    container: FrameLayout,
    context: android.content.Context,
    engine: RtcEngine,
    overlay: Boolean,
    label: String,
    renderMode: Int,
    forceRecreate: Boolean = false,
) {
    fun bindVideo() {
        val existing = container.tag as? RemoteViewBinding
        val surfaceView: SurfaceView =
            if (!forceRecreate && existing?.uid == uid && existing.surfaceView.parent == container) {
                // Reuse existing SurfaceView without cutting or rebuilding it.
                android.util.Log.d(
                    "AgoraRTC[classroom]", "reuse SurfaceView label=$label uid=$uid"
                )
                existing.surfaceView
            } else {
                // Initial creation / forceRecreate / uid change
                android.util.Log.d(
                    "AgoraRTC[classroom]",
                    "create SurfaceView label=$label uid=$uid force=$forceRecreate"
                )
                container.removeAllViews()
                val newSv = SurfaceView(context).apply {
                    setZOrderMediaOverlay(overlay)
                }
                container.addView(
                    newSv,
                    FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT,
                    ),
                )
                container.tag = RemoteViewBinding(uid, newSv)
                newSv
            }
        // Regardless of whether it's reuse or rebuild, always call setupRemoteVideo again
        // Ensure Agora knows it needs to render to this SurfaceView
        engine.setupRemoteVideo(
            VideoCanvas(surfaceView, renderMode, uid)
        )
        surfaceView.requestLayout()
        surfaceView.invalidate()
        container.requestLayout()
        container.invalidate()
    }

    mainHandler.post {
        if (container.isAttachedToWindow) {
            bindVideo()
        } else {
            container.doOnAttach { bindVideo() }
        }
    }
}

// Single engine owner
internal object RtcEngineHolder {
    var engine: RtcEngine? = null
}