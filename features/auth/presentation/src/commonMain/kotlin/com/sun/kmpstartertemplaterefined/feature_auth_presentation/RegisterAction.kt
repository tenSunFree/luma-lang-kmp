package com.sun.kmpstartertemplaterefined.feature_auth_presentation

sealed interface RegisterAction {
    data class EmailChanged(val value: String) : RegisterAction
    data class UsernameChanged(val value: String) : RegisterAction
    data class PasswordChanged(val value: String) : RegisterAction
    data class PhoneChanged(val value: String) : RegisterAction
    data class FullNameChanged(val value: String) : RegisterAction
    data class GenderChanged(val value: String) : RegisterAction
    data class OtpCodeChanged(val value: String) : RegisterAction
    data object TogglePasswordVisible : RegisterAction
    data object SubmitClicked : RegisterAction
    data object VerifyOtpClicked : RegisterAction
    data object ResendOtpClicked : RegisterAction
    data object ErrorShown : RegisterAction
}