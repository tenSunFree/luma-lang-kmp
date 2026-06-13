package com.sun.kmpstartertemplaterefined.core.ui.screens.live.rtc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Temporary tokens are used in the session; the token will be obtained via API after backend completion.
data class LiveRtcSession(
    val appId: String,
    val token: String,
    // Corresponds to course.roomId, for example, "funday_room_001"
    val channelName: String,
    val uid: Int,
)

@Composable
expect fun LiveRtcVideoView(
    modifier: Modifier = Modifier,
    session: LiveRtcSession,
    showLocalPreview: Boolean = false,
)