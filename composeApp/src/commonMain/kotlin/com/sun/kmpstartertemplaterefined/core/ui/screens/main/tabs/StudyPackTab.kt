package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.runtime.Composable
import com.sun.kmpstartertemplaterefined.feature_lessons_presentation.LessonsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudyPackTab(
    viewModel: LessonsViewModel = koinViewModel(key = "vm-supplement"),
) {
    ContentListTab(
        contentType = "supplement",
        emptyText = "目前沒有補充教材",
        viewModel = viewModel,
    )
}