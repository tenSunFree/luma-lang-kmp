package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.runtime.Composable
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MusicTab(
    viewModel: LessonsViewModel = koinViewModel(key = "vm-music"),
) {
    ContentListTab(
        contentType = "music",
        emptyText = "目前沒有音樂",
        viewModel = viewModel,
    )
}