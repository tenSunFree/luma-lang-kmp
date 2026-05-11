package com.sun.kmpstartertemplaterefined.feature_auth_presentation

enum class RegisterStep {
    FORM,   // Display registration form
    OTP,    // Display OTP verification screen
}

data class RegisterState(
    // Form fields
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val phone: String = "",
    val fullName: String = "",
    val gender: String = "男",
    val passwordVisible: Boolean = false,
    // OTP
    val otpCode: String = "",
    val step: RegisterStep = RegisterStep.FORM,
    // Shared
    val isLoading: Boolean = false,
    // OTP verification successful, notify LoginScreen to switch
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
)