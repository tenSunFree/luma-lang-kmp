package com.sun.kmpstartertemplaterefined.feature_lessons_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.kmpstartertemplaterefined.feature_lessons_domain.logics.GetLessonsLogic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LessonsViewModel(
    private val getLessonsLogic: GetLessonsLogic,
) : ViewModel() {

    private val _state = MutableStateFlow(LessonsState())
    val state: StateFlow<LessonsState> = _state.asStateFlow()

    fun onAction(action: LessonsAction) {
        when (action) {
            LessonsAction.LoadLessons,
            LessonsAction.RetryClicked -> loadLessons()
            LessonsAction.ErrorShown ->
                _state.value = _state.value.copy(errorMessage = null)
        }
    }

    private fun loadLessons() {
        if (_state.value.isLoading) return
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            getLessonsLogic(type = "video", page = 1, limit = 20)
                .onSuccess { lessons ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        lessons = lessons,
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "取得影片列表失敗",
                    )
                }
        }
    }
}