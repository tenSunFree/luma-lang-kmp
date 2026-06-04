package com.sun.kmpstartertemplaterefined.feature_lessons_presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.models.Caption
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonPlayerAction
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonPlayerViewModel
import com.sun.kmpstartertemplaterefined.feature_navigation.StarterNavigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LessonPlayerScreen(
    lessonId: String,
    viewModel: LessonPlayerViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val navigator = StarterNavigator.getCurrent()
    LaunchedEffect(lessonId) {
        viewModel.onAction(LessonPlayerAction.Load(lessonId))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navigator.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "返回",
                    tint = Color.White,
                )
            }
            Text(
                text = "LumaLang",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            )
        }
        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            state.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage ?: "發生錯誤",
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.onAction(LessonPlayerAction.Load(lessonId)) }) {
                            Text("重試")
                        }
                    }
                }
            }

            state.lessonDetail != null -> {
                val detail = state.lessonDetail!!
                // YouTube playback area (16:9)
                YouTubePlayerBox(
                    youtubeVideoId = detail.playback.youtubeVideoId,
                    isPlaying = state.isPlaying,
                    seekToMs = state.seekToMs,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
                // Subtitle List
                val listState = rememberLazyListState()
                // Automatically scroll to the current subtitle
                LaunchedEffect(state.currentMs) {
                    val idx = detail.captions.indexOfFirst {
                        state.currentMs in it.startMs until it.endMs
                    }
                    if (idx != -1) {
                        listState.animateScrollToItem(idx)
                    }
                }
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(items = detail.captions, key = { it.id }) { caption ->
                        val isCurrent = state.currentMs in caption.startMs until caption.endMs
                        CaptionRow(caption = caption, isCurrent = isCurrent)
                    }
                }
                // Bottom control column
                PlayerControls(
                    currentMs = state.currentMs,
                    durationMs = detail.playback.durationMs,
                    isPlaying = state.isPlaying,
                    allowSeek = detail.playback.allowSeek,
                    onPlayPause = { viewModel.onAction(LessonPlayerAction.PlayPauseClicked) },
                    onSeek = { viewModel.onAction(LessonPlayerAction.SeekTo(it)) },
                )
            }
        }
    }
}

// Subtitle list items
@Composable
private fun CaptionRow(caption: Caption, isCurrent: Boolean) {
    val bgColor = if (isCurrent) Color.White else Color(0xFF111111)
    val enColor = if (isCurrent) Color.Black else Color.White
    val zhColor = if (isCurrent) Color(0xFF444444) else Color(0xFF888888)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(horizontal = 20.dp, vertical = if (isCurrent) 18.dp else 14.dp),
    ) {
        Text(
            text = caption.textEn,
            color = enColor,
            fontSize = if (isCurrent) 20.sp else 17.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            lineHeight = 28.sp,
        )
        if (isCurrent) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = caption.textZhTw,
                color = zhColor,
                fontSize = 15.sp,
                lineHeight = 22.sp,
            )
        }
    }
}

// Playback Controls
@Composable
private fun PlayerControls(
    currentMs: Long,
    durationMs: Long,
    isPlaying: Boolean,
    allowSeek: Boolean,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1C))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = currentMs.toTimeText(),
                color = Color.White,
                fontSize = 13.sp,
            )
            Slider(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                value = currentMs.toFloat(),
                onValueChange = { if (allowSeek) onSeek(it.toLong()) },
                valueRange = 0f..durationMs.toFloat().coerceAtLeast(1f),
                enabled = allowSeek,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color(0xFF555555),
                ),
            )
            Text(
                text = durationMs.toTimeText(),
                color = Color(0xFF999999),
                fontSize = 13.sp,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        IconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "暫停" else "播放",
                tint = Color.White,
                modifier = Modifier.size(42.dp),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text("中", color = Color.White, fontSize = 18.sp)
            Text("AA", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("1x", color = Color.White, fontSize = 18.sp)
            Text("↻", color = Color.White, fontSize = 20.sp)
            Text("☾", color = Color.White, fontSize = 18.sp)
        }
    }
}

private fun Long.toTimeText(): String {
    val totalSec = this / 1000
    return "${totalSec / 60}:${(totalSec % 60).toString().padStart(2, '0')}"
}