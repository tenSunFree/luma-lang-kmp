package com.sun.kmpstartertemplaterefined.system

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun SetStatusBarStyle(
    color: Color,
    darkIcons: Boolean,
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as ComponentActivity).window
        WindowCompat.getInsetsController(window, view)
            .isAppearanceLightStatusBars = darkIcons
    }
}