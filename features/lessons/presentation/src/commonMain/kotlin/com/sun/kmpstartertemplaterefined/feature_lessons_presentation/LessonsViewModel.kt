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

    private var currentType: String = "video"

    fun onAction(action: LessonsAction) {
        when (action) {
            is LessonsAction.LoadLessons -> {
                currentType = action.type
                loadLessons(type = action.type)
            }

            LessonsAction.RetryClicked -> loadLessons(type = currentType)
            LessonsAction.ErrorShown -> _state.value = _state.value.copy(errorMessage = null)
        }
    }

    private fun loadLessons(type: String) {
        if (_state.value.isLoading) return
        _state.value = _state.value.copy(
            isLoading = true,
            errorMessage = null,
            lessons = emptyList(),
        )
        viewModelScope.launch {
            getLessonsLogic(type = type, page = 1, limit = 20)
                .onSuccess { lessons ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        lessons = lessons,
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "取得內容列表失敗",
                    )
                }
        }
    }
}