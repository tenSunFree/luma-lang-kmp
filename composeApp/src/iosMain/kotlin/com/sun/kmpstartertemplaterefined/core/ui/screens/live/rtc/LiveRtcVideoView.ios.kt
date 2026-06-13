package com.sun.kmpstartertemplaterefined.core.ui.screens.live.rtc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun LiveRtcVideoView(
    modifier: Modifier,
    session: LiveRtcSession,
    showLocalPreview: Boolean,
) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "iOS RTC placeholder", color = Color.White)
    }
}