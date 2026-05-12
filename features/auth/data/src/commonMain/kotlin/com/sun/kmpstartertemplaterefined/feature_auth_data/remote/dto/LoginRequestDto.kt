package com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
)