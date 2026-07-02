package com.sun.kmpstartertemplaterefined.feature_live_presentation

import LiveCourseUi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.DisposableEffect
import com.sun.kmpstartertemplaterefined.feature_live_presentation.pip.LivePipController
import com.sun.kmpstartertemplaterefined.feature_live_presentation.pip.LivePipNotificationController
import com.sun.kmpstartertemplaterefined.feature_live_presentation.pip.isInPipMode
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.feature_live_presentation.rtc.AgoraLocalConfig
import com.sun.kmpstartertemplaterefined.feature_live_presentation.rtc.LiveRtcClassroomView
import com.sun.kmpstartertemplaterefined.feature_live_presentation.rtc.LiveRtcSession
import com.sun.kmpstartertemplaterefined.utils.logging.Log

private val LivePink = Color(0xFFFF3F68)
private val LiveBg = Color.Black
private val PanelBg = Color(0xFF1B1B1B)
private val ControlBg = Color(0xFF3A3A3A)
private val MutedBadge = Color(0xFF555555)

// The UID shared by the teacher's screen (agreed upon with the teacher)
private const val TEACHER_SCREEN_UID = 2000

// The teacher's camera UID (agreed upon with the teacher)
private const val TEACHER_CAMERA_UID = 1000

data class LiveParticipantUi(
    val id: String,
    val name: String,
    val avatarEmoji: String? = null,
    val isMuted: Boolean = true,
    val isTeacher: Boolean = false,
)

data class LiveChatMessageUi(
    val id: String,
    val userName: String,
    val message: String,
)

enum class LiveRoomTab { Chat, Participants }

private val mockParticipants = listOf(
    LiveParticipantUi(
        id = "teacher", name = "KarolChin", avatarEmoji = "👩🏻", isMuted = false, isTeacher = true
    ),
    LiveParticipantUi(id = "jeffery", name = "Jeffery", avatarEmoji = "🌿", isMuted = true),
    LiveParticipantUi(id = "sun", name = "Sun", avatarEmoji = null, isMuted = true),
    LiveParticipantUi(id = "evan", name = "Evan", avatarEmoji = "👦🏻", isMuted = true),
    LiveParticipantUi(id = "jack", name = "Jack", avatarEmoji = null, isMuted = true),
)

private val mockChatMessages = listOf(
    LiveChatMessageUi(id = "1", userName = "Sun", message = "has joined the stream"),
    LiveChatMessageUi(id = "2", userName = "Jack", message = "has joined the stream"),
)

/**
 * Single call-site principle
 *
 * LiveVideoArea appears only once in the codebase (the same call site).
 * Whether or not we are in PiP only changes the appearance through parameters
 * (modifier size and whether other UI is shown), rather than switching the whole
 * Composable branch. This keeps remember state, the RtcEngine, and the FrameLayout
 * all as the same instance, so the view tree is not torn down and rebuilt when PiP changes.
 */
@Composable
fun LiveRoomScreen(
    course: LiveCourseUi,
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableStateOf(LiveRoomTab.Participants) } // Default to Participants for screenshots
    var showTeacherVideo by remember { mutableStateOf(true) }
    var speakerEnabled by remember { mutableStateOf(true) }
    var inputText by remember { mutableStateOf("") }
    // Notification permission handling has already been moved to MainActivity
    // (via Activity-level registerForActivityResult, triggered by
    // AndroidLivePipState.onStateChanged). This screen no longer calls
    // rememberPipNotificationPermissionGranted() or any ActivityResultLauncher-related API.
    //
    // Why: this screen is mounted through Navigation3's SceneSetupNavEntryDecorator
    // (implemented internally with movableContentOf). In this path, the CompositionLocal
    // inheritance chain (including the LocalActivityResultRegistryOwner dependency used by
    // rememberLauncherForActivityResult) is unreliable and previously caused an
    // IllegalStateException crash. Moving the permission request to the Activity level
    // completely avoids this problem; see MainActivity.ensureNotificationPermission().
    //
    // When entering this screen, inform the platform that we are in the live room so
    // system PiP is allowed. When leaving the screen, clear the state to avoid the user
    // being mistakenly treated as entering PiP after leaving the live room.
    DisposableEffect(Unit) {
        LivePipController.setLiveRoomActive(true)
        LivePipController.setCourseTitle(course.title)
        onDispose {
            LivePipController.setLiveRoomActive(false)
            LivePipController.setVideoPlaying(false)
        }
    }
    // Register the actual behavior for the PiP notification's "mute/unmute" and "stop playback" buttons.
    // - Mute/unmute: directly flip the current speakerEnabled state. The subsequent
    //   LaunchedEffect(speakerEnabled) (inside LiveRtcClassroomView)
    //   will automatically handle Agora's muteAllRemoteAudioStreams.
    // - Stop playback: equivalent to the user pressing Back to leave the live room.
    //   It reuses the same onBack logic, which will naturally trigger the onDispose
    //   in the DisposableEffect above and the leaveChannel/destroy cleanup inside
    //   LiveRtcClassroomView.
    DisposableEffect(Unit) {
        LivePipNotificationController.registerActions(
            onToggleMuteRequested = { speakerEnabled = !speakerEnabled },
            onStopRequested = onBack,
        )
        onDispose {
            LivePipNotificationController.unregisterActions()
        }
    }
    // Report the current mute state (muted = !speakerEnabled) to the notification controller
    // every time speakerEnabled changes, so the text on the button in the currently displayed
    // notification stays in sync with the real state—whether the change came from the in-app
    // UI or from the notification button itself.
    LaunchedEffect(speakerEnabled) {
        LivePipNotificationController.reportMuteState(!speakerEnabled)
    }
    // Whether the app is currently in system PiP mode (Android only; iOS is always false).
    val inPip = isInPipMode()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LiveBg)
            .then(if (inPip) Modifier else Modifier.systemBarsPadding()),
    ) {
        // Because the PiP window is very small, hide the Header, chat, and control bar and keep only the video.
        // Wrapping these display-only sections in if is safe—they are unrelated to video rendering, and being
        // disposed/recreated will not affect the Agora engine or video continuity.
        // The only part that must not be affected by an if-return is the LiveVideoArea below.
        if (!inPip) {
            LiveRoomHeader(
                showTeacherVideo = showTeacherVideo,
                speakerEnabled = speakerEnabled,
                onBack = onBack,
                onToggleTeacherVideo = { showTeacherVideo = !showTeacherVideo },
                onToggleSpeaker = { speakerEnabled = !speakerEnabled },
            )
        }
        // Live Stream View Area — there is only this one call site for the entire screen.
        // In PiP mode: fill the entire PiP window and forcibly hide the teacher camera inset.
        // (The PiP space is too small, and stacking two SurfaceViews can easily cause Z-order/
        // black-screen issues; we also adopted the point mentioned in the analysis.)
        LiveVideoArea(
            course = course,
            showTeacherVideo = if (inPip) false else showTeacherVideo,
            speakerEnabled = speakerEnabled,
            modifier = if (inPip) {
                Modifier.fillMaxSize()
            } else {
                Modifier.fillMaxWidth().height(240.dp)
            },
        )
        if (!inPip) {
            // Chat / Participants Tab column
            LiveRoomTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
            // Content Panel
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth().background(PanelBg),
            ) {
                when (selectedTab) {
                    LiveRoomTab.Chat -> LiveChatPanel(messages = mockChatMessages)
                    LiveRoomTab.Participants -> LiveParticipantsPanel(participants = mockParticipants)
                }
            }
            // Bottom Operation Column
            LiveBottomBar(
                selectedTab = selectedTab,
                inputText = inputText,
                onInputTextChange = { inputText = it },
                onSend = { inputText = "" },
            )
        }
    }
}

@Composable
private fun LiveRoomHeader(
    showTeacherVideo: Boolean,
    speakerEnabled: Boolean,
    onBack: () -> Unit,
    onToggleTeacherVideo: () -> Unit,
    onToggleSpeaker: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "返回",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "LumaLang",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 32.sp,
            )
            Text(
                text = "AI English Academy",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        // Eyes (Switch to teacher's video)
        CircleIconButton(
            onClick = onToggleTeacherVideo,
            backgroundColor = if (showTeacherVideo) Color.White else ControlBg,
        ) {
            Icon(
                imageVector = if (showTeacherVideo) Icons.Filled.RemoveRedEye else Icons.Filled.VisibilityOff,
                contentDescription = "切換老師視訊",
                tint = if (showTeacherVideo) Color.Black else Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        // trumpet
        CircleIconButton(
            onClick = onToggleSpeaker,
            backgroundColor = ControlBg,
        ) {
            Icon(
                imageVector = if (speakerEnabled) Icons.Filled.VolumeUp else Icons.Filled.VolumeOff,
                contentDescription = "喇叭",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun LiveVideoArea(
    course: LiveCourseUi,
    showTeacherVideo: Boolean,
    speakerEnabled: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth().height(240.dp),
) {
    val session = remember(course.roomId) {
        LiveRtcSession(
            appId = AgoraLocalConfig.appId,
            token = AgoraLocalConfig.token,
            channelName = course.roomId,
            uid = 0,  // 0 = Agora automatically assigns student UIDs
        )
    }
    Log.d(
        "LiveRoomScreen", "channel=${session.channelName}, appIdBlank=${session.appId.isBlank()}"
    )
    // Single Composable, single RTCEngine, dual-view
    //
    // This is the only LiveRtcClassroomView call site in the entire file. Whether or not we are in PiP,
    // it is the same call site, and Compose treats it as the same node.
    // The session/rtcEngine/screenContainer/cameraContainer all stay intact,
    // and are not disposed and rebuilt when PiP changes.
    LiveRtcClassroomView(
        modifier = modifier.background(Color.Black),
        session = session,
        screenUid = TEACHER_SCREEN_UID,   // 2000
        cameraUid = TEACHER_CAMERA_UID,   // 1000
        showCamera = showTeacherVideo,
        speakerEnabled = speakerEnabled,
    )
}

// Chat / Participants Tab column
@Composable
private fun LiveRoomTabs(
    selectedTab: LiveRoomTab,
    onTabSelected: (LiveRoomTab) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).background(PanelBg),
    ) {
        LiveTabItem(
            text = "Chat",
            selected = selectedTab == LiveRoomTab.Chat,
            onClick = { onTabSelected(LiveRoomTab.Chat) },
            modifier = Modifier.weight(1f),
        )
        LiveTabItem(
            text = "Participants",
            selected = selectedTab == LiveRoomTab.Participants,
            onClick = { onTabSelected(LiveRoomTab.Participants) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun LiveTabItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight().clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFF888888),
            fontSize = 20.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier.fillMaxWidth().height(3.dp)
                .background(if (selected) LivePink else Color.Transparent),
        )
    }
}

// Participants panel
@Composable
private fun LiveParticipantsPanel(participants: List<LiveParticipantUi>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(participants) { participant ->
            ParticipantItem(participant = participant)
        }
    }
}

@Composable
private fun ParticipantItem(participant: LiveParticipantUi) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                modifier = Modifier.size(82.dp).clip(CircleShape).background(Color(0xFFEFEFEF))
                    .then(
                        if (participant.isTeacher) Modifier.border(4.dp, LivePink, CircleShape)
                        else Modifier
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = participant.avatarEmoji ?: "👤", fontSize = 38.sp)
            }
            // Mute badge (bottom right corner)
            if (participant.isMuted) {
                Box(
                    modifier = Modifier.size(34.dp).clip(CircleShape).background(MutedBadge)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.MicOff,
                        contentDescription = "已靜音",
                        tint = Color(0xFFCCCCCC),
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = participant.name,
            color = Color(0xFFDDDDDD),
            fontSize = 16.sp,
            maxLines = 1,
        )
    }
}

// Chat panel
@Composable
private fun LiveChatPanel(messages: List<LiveChatMessageUi>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(messages) { message ->
            LiveChatMessageBubble(message = message)
        }
    }
}

@Composable
private fun LiveChatMessageBubble(message: LiveChatMessageUi) {
    Row(
        modifier = Modifier.wrapContentWidth().clip(RoundedCornerShape(22.dp))
            .background(Color(0xFF444444)).padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "👤", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message.userName,
            color = Color(0xFF1E88E5),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = message.message,
            color = Color(0xFFBBBBBB),
            fontSize = 16.sp,
        )
    }
}

// Bottom Operation Column
@Composable
private fun LiveBottomBar(
    selectedTab: LiveRoomTab,
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.Black).navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Hand button
        CircleIconButton(onClick = {}, backgroundColor = ControlBg) {
            Icon(
                imageVector = Icons.Filled.WavingHand,
                contentDescription = "舉手",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        if (selectedTab == LiveRoomTab.Chat) {
            TextField(
                value = inputText,
                onValueChange = onInputTextChange,
                placeholder = { Text(text = "輸入...", color = Color(0xFFAAAAAA)) },
                modifier = Modifier.weight(1f).height(52.dp).clip(RoundedCornerShape(26.dp)),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ControlBg,
                    unfocusedContainerColor = ControlBg,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                ),
                trailingIcon = {
                    IconButton(onClick = onSend) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = "送出",
                            tint = Color.White,
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        CircleIconButton(onClick = {}, backgroundColor = ControlBg) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "愛心",
                tint = LivePink,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun CircleIconButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier.size(52.dp).clip(CircleShape).background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
        content = content,
    )
}