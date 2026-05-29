package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.runtime.*
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun VideoTab(
    viewModel: LessonsViewModel = koinViewModel(key = "vm-video"),
) {
    ContentListTab(
        contentType = "video",
        emptyText = "目前沒有影片",
        viewModel = viewModel,
    )
}