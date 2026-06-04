package com.sun.kmpstartertemplaterefined.feature_lessons_presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun YouTubePlayerBox(
    youtubeVideoId: String,
    isPlaying: Boolean,
    seekToMs: Long?,
    modifier: Modifier = Modifier,
)