package com.sun.kmpstartertemplaterefined.feature_auth_domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val id: String,
    val username: String,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val gender: String? = null,
    val roleId: Int,
    val token: String,
    val refreshToken: String,
    val createdAt: String,
    val updatedAt: String,
)