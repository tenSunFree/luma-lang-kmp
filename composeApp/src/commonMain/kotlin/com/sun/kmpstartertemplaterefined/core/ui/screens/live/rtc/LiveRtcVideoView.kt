package com.sun.kmpstartertemplaterefined.core.ui.screens.live.rtc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class LiveRtcSession(
    val appId: String,
    val token: String,
    val channelName: String,
    val uid: Int,
)

/**
 * For live classroom use:
 * One RtcEngine joins one channel, rendering two remote feeds simultaneously.
 * screenUid → Main screen (teacher's desktop sharing)
 * cameraUid → Top right corner window (teacher's camera)
 */
@Composable
expect fun LiveRtcClassroomView(
    modifier: Modifier = Modifier,
    session: LiveRtcSession,
    screenUid: Int,
    cameraUid: Int,
    showCamera: Boolean,
    speakerEnabled: Boolean,
)