package com.sun.kmpstartertemplaterefined.feature_auth_presentation

sealed interface LoginAction {
    data class EmailChanged(val value: String) : LoginAction
    data class PasswordChanged(val value: String) : LoginAction
    data object TogglePasswordVisible : LoginAction
    data object SubmitClicked : LoginAction
    data object ErrorShown : LoginAction
}