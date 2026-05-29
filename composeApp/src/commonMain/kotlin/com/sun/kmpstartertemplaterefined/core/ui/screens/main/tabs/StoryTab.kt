package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.runtime.Composable
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StoryTab(
    viewModel: LessonsViewModel = koinViewModel(key = "vm-fairy-tale"),
) {
    ContentListTab(
        contentType = "fairy_tale",
        emptyText = "目前沒有故事",
        viewModel = viewModel,
    )
}