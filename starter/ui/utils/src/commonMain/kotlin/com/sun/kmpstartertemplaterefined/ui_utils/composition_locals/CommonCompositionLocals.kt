package com.sun.kmpstartertemplaterefined.ui_utils.composition_locals

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.compositionLocalOf
import com.sun.kmpstartertemplaterefined.core.events.enums.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
val LocalThemeMode = compositionLocalOf { ThemeMode.SYSTEM }
