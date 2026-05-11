package com.sun.kmpstartertemplaterefined.feature_auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.RegisterUserLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.SendOtpLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.VerifyOtpLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserLogic: RegisterUserLogic,
    private val sendOtpLogic: SendOtpLogic,
    private val verifyOtpLogic: VerifyOtpLogic,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.EmailChanged ->
                _state.value = _state.value.copy(email = action.value)

            is RegisterAction.UsernameChanged ->
                _state.value = _state.value.copy(username = action.value)

            is RegisterAction.PasswordChanged ->
                _state.value = _state.value.copy(password = action.value)

            is RegisterAction.PhoneChanged ->
                _state.value = _state.value.copy(phone = action.value)

            is RegisterAction.FullNameChanged ->
                _state.value = _state.value.copy(fullName = action.value)

            is RegisterAction.GenderChanged ->
                _state.value = _state.value.copy(gender = action.value)

            is RegisterAction.OtpCodeChanged -> {
                // Keep only the numbers, maximum 6 digits
                val filtered = action.value.filter { it.isDigit() }.take(6)
                _state.value = _state.value.copy(otpCode = filtered)
            }

            RegisterAction.TogglePasswordVisible ->
                _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)

            RegisterAction.SubmitClicked -> registerAndSendOtp()
            RegisterAction.VerifyOtpClicked -> verifyOtp()
            RegisterAction.ResendOtpClicked -> resendOtp()
            RegisterAction.ErrorShown ->
                _state.value = _state.value.copy(errorMessage = null)
        }
    }

    // Registration → OTP will be automatically sent upon successful registration
    private fun registerAndSendOtp() {
        val s = _state.value
        _state.value = s.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val email = s.email.trim()
            val params = RegisterParams(
                email = email,
                username = s.username.trim(),
                password = s.password,
                fullName = s.fullName.trim(),
                phone = s.phone.trim(),
                gender = if (s.gender == "男") "male" else "female",
            )
            registerUserLogic(params)
                .onSuccess {
                    // Registration successful → Send OTP immediately
                    sendOtpLogic(email)
                        .onSuccess {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                step = RegisterStep.OTP,
                                otpCode = "",
                                errorMessage = null,
                            )
                        }
                        .onFailure { error ->
                            _state.value = _state.value.copy(
                                isLoading = false,
                                errorMessage = error.message ?: "驗證碼發送失敗，請稍後再試",
                            )
                        }
                }
                .onFailure { error ->
                    _state.value = s.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "註冊失敗，請稍後再試",
                    )
                }
        }
    }

    // Resend OTP
    private fun resendOtp() {
        val email = _state.value.email.trim()
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            sendOtpLogic(email)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        otpCode = "",
                        errorMessage = null,
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "重新發送驗證碼失敗",
                    )
                }
        }
    }

    // Verify OTP
    private fun verifyOtp() {
        val s = _state.value
        _state.value = s.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            verifyOtpLogic(email = s.email.trim(), code = s.otpCode)
                .onSuccess {
                    // isSuccess = true → Switch to LoginCard after LoginScreen detects success.
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true,
                    )
                }
                .onFailure { error ->
                    _state.value = s.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "驗證碼錯誤，請重新確認",
                    )
                }
        }
    }

    // LoginScreen is called after the switch is complete to reset the state.
    fun reset() {
        _state.value = RegisterState()
    }
}