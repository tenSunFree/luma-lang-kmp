package com.sun.kmpstartertemplaterefined.feature_auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.LoginLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.LoginParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginLogic: LoginLogic,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged ->
                _state.value = _state.value.copy(email = action.value)

            is LoginAction.PasswordChanged ->
                _state.value = _state.value.copy(password = action.value)

            LoginAction.TogglePasswordVisible ->
                _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)

            LoginAction.SubmitClicked -> login()
            LoginAction.ErrorShown ->
                _state.value = _state.value.copy(errorMessage = null)
        }
    }

    private fun login() {
        val s = _state.value
        _state.value = s.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            loginLogic(LoginParams(email = s.email.trim(), password = s.password))
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "登入失敗，請確認帳號密碼",
                    )
                }
        }
    }
}