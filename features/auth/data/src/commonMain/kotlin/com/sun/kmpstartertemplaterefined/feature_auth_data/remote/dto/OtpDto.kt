package com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendOtpRequestDto(
    val email: String,
)

@Serializable
data class VerifyOtpRequestDto(
    val email: String,
    val code: String,
)

// send-otp and verify-otp return the same format and share a single DTO.
@Serializable
data class SimpleAuthResponseDto(
    val status: Boolean,
    val message: String,
    val data: String? = null,
)