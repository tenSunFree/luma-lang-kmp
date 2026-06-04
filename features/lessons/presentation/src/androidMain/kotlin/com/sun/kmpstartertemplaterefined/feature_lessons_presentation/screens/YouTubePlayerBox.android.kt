package com.sun.kmpstartertemplaterefined.feature_lessons_presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
actual fun YouTubePlayerBox(
    youtubeVideoId: String,
    isPlaying: Boolean,
    seekToMs: Long?,
    modifier: Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var ytPlayer by remember { mutableStateOf<YouTubePlayer?>(null) }
    var ytPlayerView by remember { mutableStateOf<YouTubePlayerView?>(null) }
    var isReady by remember { mutableStateOf(false) }
    Box(modifier = modifier.background(Color.Black)) {
        AndroidView(
            modifier = Modifier.matchParentSize(),
            factory = { context ->
                val options = IFramePlayerOptions.Builder(context)
                    .controls(0) // Hide YouTube native control bar
                    .rel(0).ivLoadPolicy(3).ccLoadPolicy(0).build()
                YouTubePlayerView(context).apply {
                    enableAutomaticInitialization = false
                    initialize(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                ytPlayer = youTubePlayer
                                isReady = true
                                // cueVideo = Loads a video but does not play it automatically
                                youTubePlayer.cueVideo(youtubeVideoId, 0f)
                            }

                            override fun onStateChange(
                                youTubePlayer: YouTubePlayer,
                                state: PlayerConstants.PlayerState,
                            ) {
                                // Reserved: can synchronize the actual playback progress here later.
                            }
                        },
                        options,
                    )
                    lifecycleOwner.lifecycle.addObserver(this)
                    ytPlayerView = this
                }
            },
            update = { view ->
                // Re-cue the video when the youtubeVideoId changes (in the case of changing classes)
                if (isReady) {
                    ytPlayer?.cueVideo(youtubeVideoId, 0f)
                }
            },
        )
        // Transparent overlay: Blocks touch events from the YouTube screen
        // This prevents users from triggering YouTube's native behavior when tapping the video area at the top.
        Box(
            modifier = Modifier.matchParentSize().pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            },
        )
    }
    // Play/Pause Synchronization
    LaunchedEffect(isPlaying, isReady) {
        if (!isReady) return@LaunchedEffect
        if (isPlaying) ytPlayer?.play() else ytPlayer?.pause()
    }
    // Seek synchronization (triggered only when the user drags the slider)
    LaunchedEffect(seekToMs, isReady) {
        if (!isReady) return@LaunchedEffect
        seekToMs?.let { ytPlayer?.seekTo(it / 1000f) }
    }
    // Release resources when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            ytPlayerView?.let { view ->
                lifecycleOwner.lifecycle.removeObserver(view)
                view.release()
            }
            ytPlayerView = null
            ytPlayer = null
        }
    }
}