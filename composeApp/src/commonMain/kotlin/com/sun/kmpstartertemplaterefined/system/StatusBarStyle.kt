package com.sun.kmpstartertemplaterefined.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun SetStatusBarStyle(
    color: Color = Color.Transparent,
    darkIcons: Boolean,
)