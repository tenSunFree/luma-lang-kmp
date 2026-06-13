package com.sun.kmpstartertemplaterefined.core.events.enums;

enum class ThemeMode(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System");

    fun isInDarkTheme(isSystemInDarkTheme: Boolean) =
        when (this) {
            LIGHT -> false
            DARK -> true
            SYSTEM -> isSystemInDarkTheme
        }
}

