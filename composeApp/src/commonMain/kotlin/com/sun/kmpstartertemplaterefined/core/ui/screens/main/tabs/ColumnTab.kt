package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.runtime.Composable
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ColumnTab(
    viewModel: LessonsViewModel = koinViewModel(key = "vm-column"),
) {
    ContentListTab(
        contentType = "column",
        emptyText = "目前沒有專欄",
        viewModel = viewModel,
    )
}