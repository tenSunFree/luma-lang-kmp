package com.sun.kmpstartertemplaterefined.feature_auth_domain.models

data class LoginResult(
    val userId: String,
    val username: String,
    val fullName: String,
    val email: String,
    val token: String,
    val refreshToken: String,
)