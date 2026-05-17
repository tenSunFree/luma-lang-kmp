package com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val status: Boolean,
    val message: String,
    val data: LoginDataDto? = null,
)

@Serializable
data class LoginDataDto(
    val id: String,
    val username: String,
    @SerialName("full_name") val fullName: String,
    val email: String,
    val phone: String? = null,
    val gender: String? = null,
    @SerialName("role_id") val roleId: Int,
    val token: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
)