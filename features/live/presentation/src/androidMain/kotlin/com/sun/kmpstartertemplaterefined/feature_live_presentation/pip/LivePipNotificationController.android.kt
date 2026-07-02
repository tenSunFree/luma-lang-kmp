package com.sun.kmpstartertemplaterefined.feature_live_presentation.pip

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/**
 * Crash fix record
 *
 * This file originally called rememberLauncherForActivityResult directly
 * inside [rememberPipNotificationPermissionGranted], which led to:
 *
 *     IllegalStateException: No ActivityResultRegistryOwner was
 *     provided via LocalActivityResultRegistryOwner
 *
 * Reason: LiveRoomScreen is mounted via Navigation3's SceneSetupNavEntryDecorator,
 * which is implemented internally using movableContentOf (allowing content to be moved
 * in the Composition tree without losing state). However, movableContentOf has a known
 * subtle pitfall with CompositionLocal inheritance semantics—it may bind to the location
 * where content was first created, rather than where it is currently being moved to.
 * This causes Composables in this path to sometimes fail to receive the
 * LocalActivityResultRegistryOwner that should be inherited from the Activity.
 *
 * Fix: never call rememberLauncherForActivityResult / registerForActivityResult
 * inside any Composable function. Move the actual permission request to an Activity-level
 * property in MainActivity (not inside a Composable function). This approach uses
 * ComponentActivity's own ActivityResultRegistry implementation, which does not depend
 * on CompositionLocal resolution, so it is not affected by Navigation3 / movableContentOf
 * and is guaranteed not to crash.
 *
 * What remains here is just "reading" the result written by MainActivity, which is pure
 * data reading without any launcher logic, so this file will not have similar issues.
 */
actual object LivePipNotificationController {
    actual fun registerActions(
        onToggleMuteRequested: () -> Unit,
        onStopRequested: () -> Unit,
    ) {
        AndroidLivePipNotificationBridge.onToggleMuteRequested = onToggleMuteRequested
        AndroidLivePipNotificationBridge.onStopRequested = onStopRequested
    }

    actual fun unregisterActions() {
        AndroidLivePipNotificationBridge.onToggleMuteRequested = null
        AndroidLivePipNotificationBridge.onStopRequested = null
    }

    actual fun reportMuteState(muted: Boolean) {
        AndroidLivePipNotificationBridge.reportMuteState(muted)
    }
}

/**
 * Pure read-only access to the MutableState inside AndroidLivePipState, using the exact same
 * technique as [isInPipMode]. No remember, no launcher, no CompositionLocal dependencies—as long as
 * the AndroidLivePipState.notificationPermissionGrantedState object exists (it's a top-level object
 * that exists when the app launches), calling this function is absolutely safe at any Composition location.
 *
 * The actual "request permission" action is triggered by MainActivity when the user enters the live room
 * (see MainActivity.ensureNotificationPermission()) and is completely decoupled from here.
 */
@Composable
actual fun rememberPipNotificationPermissionGranted(): Boolean {
    val granted by AndroidLivePipState.notificationPermissionGrantedState
    return granted
}