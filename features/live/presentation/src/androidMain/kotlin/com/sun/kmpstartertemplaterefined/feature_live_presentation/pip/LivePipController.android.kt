package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

/**
 * Android platform's actual PiP state holder.
 *
 * Placed in feature_live_presentation/androidMain (not in the androidApp module) to avoid
 * features/live/presentation depending back on androidApp, maintaining the one-way relationship:
 * "app module depends on feature module, feature module does not depend on app module."
 *
 * MainActivity reads [canEnterPip] to decide whether to call enterPictureInPictureMode(),
 * and writes [isInPipModeState] in the onPictureInPictureModeChanged callback.
 */
object AndroidLivePipState {
    private var isLiveRoomActive: Boolean = false
    private var isVideoPlaying: Boolean = false
    var aspectRatioWidth: Int = 16
        private set
    var aspectRatioHeight: Int = 9
        private set

    /**
     * Current live room course title, set when LiveRoomScreen enters the screen.
     * The PiP notification reads this value to display the title, without needing to pass
     * parameters through MainActivity (avoiding MainActivity having to keep its own copy of course info).
     */
    @Volatile
    var courseTitle: String = "直播課程"
        private set

    /** Whether currently in system PiP mode, wrapped with Compose State to support recomposition. */
    val isInPipModeState: MutableState<Boolean> = mutableStateOf(false)

    /**
     * Notification permission state (fix for LocalActivityResultRegistryOwner crash)
     *
     * This only stores "whether POST_NOTIFICATIONS permission is currently granted," without
     * holding or calling any ActivityResultLauncher.
     *
     * The actual permission request (registerForActivityResult) is placed in an Activity-level property
     * in MainActivity, not called inside any Composable function like rememberLauncherForActivityResult.
     * This is necessary because LiveRoomScreen is mounted through Navigation3's SceneSetupNavEntryDecorator
     * (implemented internally with movableContentOf), and in this path the CompositionLocal inheritance chain
     * (including LocalActivityResultRegistryOwner) is unreliable and previously caused IllegalStateException crashes.
     *
     * After MainActivity gets the permission result, it writes it here. The Compose side is responsible only for
     * "reading" this MutableState (usage identical to [isInPipModeState]), and no other place will call launcher-related
     * APIs inside Composables.
     */
    val notificationPermissionGrantedState: MutableState<Boolean> = mutableStateOf(false)

    /** Called when MainActivity gets the permission result (whether from an initial check or after the user responds to the system dialog). */
    fun setNotificationPermissionGranted(granted: Boolean) {
        notificationPermissionGrantedState.value = granted
    }

    /**
     * MainActivity registers its refreshPipParams in onCreate, so every time the live state changes
     * (entering the room/receiving the first frame/leaving the room), it can immediately sync to the system's
     * PictureInPictureParams (especially autoEnterEnabled).
     */
    var onStateChanged: (() -> Unit)? = null

    /**
     * Black screen fix
     * MainActivity.onResume() calls [notifyResumed] to notify the currently rendering LiveRtcClassroomView:
     * the app has just recovered from background/PIP mode to the foreground, and the underlying SurfaceView
     * Surface has likely been recreated by the system. It must proactively call setupRemoteVideo() on the
     * Agora engine again to restore the video, otherwise the screen will get stuck on black.
     *
     * Only one callback can be registered at a time (LiveRtcClassroomView registers/unregisters it in its
     * own DisposableEffect) to avoid multiple stacking that causes repeated triggers when the live screen
     * Composable recomposes.
     */
    var onResumed: (() -> Unit)? = null

    fun setLiveRoomActive(active: Boolean) {
        isLiveRoomActive = active
        onStateChanged?.invoke()
    }

    fun setVideoPlaying(playing: Boolean) {
        isVideoPlaying = playing
        onStateChanged?.invoke()
    }

    fun setAspectRatio(width: Int, height: Int) {
        if (width > 0 && height > 0) {
            aspectRatioWidth = width
            aspectRatioHeight = height
            onStateChanged?.invoke()
        }
    }

    fun setCourseTitle(title: String) {
        if (title.isNotBlank()) {
            courseTitle = title
        }
    }

    /** MainActivity uses this to decide whether to allow entering PiP: must be in the live room and video must be showing. */
    fun canEnterPip(): Boolean = isLiveRoomActive && isVideoPlaying

    /** Called by MainActivity.onResume() to notify the live screen that SurfaceView may need to be rebound. */
    fun notifyResumed() {
        onResumed?.invoke()
    }
}

actual object LivePipController {
    actual fun setLiveRoomActive(active: Boolean) {
        AndroidLivePipState.setLiveRoomActive(active)
    }

    actual fun setVideoPlaying(playing: Boolean) {
        AndroidLivePipState.setVideoPlaying(playing)
    }

    actual fun setAspectRatio(width: Int, height: Int) {
        AndroidLivePipState.setAspectRatio(width, height)
    }

    actual fun setCourseTitle(title: String) {
        AndroidLivePipState.setCourseTitle(title)
    }
}

@Composable
actual fun isInPipMode(): Boolean {
    val state by AndroidLivePipState.isInPipModeState
    return state
}