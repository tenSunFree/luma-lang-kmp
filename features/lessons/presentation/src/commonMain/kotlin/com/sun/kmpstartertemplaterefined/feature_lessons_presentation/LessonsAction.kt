package com.sun.kmpstartertemplaterefined.feature_lessons_presentation

sealed interface LessonsAction {
    data class LoadLessons(val type: String) : LessonsAction
    data object RetryClicked : LessonsAction
    data object ErrorShown : LessonsAction
}