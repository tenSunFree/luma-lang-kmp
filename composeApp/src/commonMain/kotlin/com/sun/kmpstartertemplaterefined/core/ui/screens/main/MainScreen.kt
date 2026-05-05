package com.sun.kmpstartertemplaterefined.core.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.sun.kmpstartertemplaterefined.core.ui.screens.main.components.MainBottomBar
import com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs.EasyLearningScreen
import com.sun.kmpstartertemplaterefined.core.ui.system.SetStatusBarStyle

@Composable
fun MainScreen() {
    SetStatusBarStyle(darkIcons = true)
    var selectedBottomIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            MainBottomBar(
                selectedIndex = selectedBottomIndex,
                onTabSelected = { selectedBottomIndex = it },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            when (selectedBottomIndex) {
                0 -> EasyLearningScreen()
                1 -> EmptyPlaceholderScreen("認真學")
                2 -> EmptyPlaceholderScreen("複習")
                3 -> EmptyPlaceholderScreen("精讀收錄")
                4 -> EmptyPlaceholderScreen("更多")
            }
        }
    }
}

@Composable
private fun EmptyPlaceholderScreen(title: String) {
}