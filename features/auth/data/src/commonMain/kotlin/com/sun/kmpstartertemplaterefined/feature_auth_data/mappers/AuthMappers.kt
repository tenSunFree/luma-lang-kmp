package com.sun.kmpstartertemplaterefined.feature_auth_data.mappers

import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.LoginDataDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.LoginResult
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterParams
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterResult
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.UserSession

fun RegisterParams.toDto() = RegisterRequestDto(
    email = email,
    fullName = fullName,
    gender = gender,
    password = password,
    phone = phone,
    username = username,
)

fun RegisterResponseDto.toDomain(): RegisterResult {
    val user = data?.user ?: error("註冊成功但沒有回傳使用者資料")
    return RegisterResult(
        userId = user.id,
        username = user.username,
        email = user.email,
        fullName = user.fullName,
        phone = user.phone,
        gender = user.gender,
        message = message,
    )
}

fun LoginDataDto.toDomain() = LoginResult(
    userId = id,
    username = username,
    fullName = fullName,
    email = email,
    token = token,
    refreshToken = refreshToken,
)

fun LoginDataDto.toUserSession() = UserSession(
    id = id,
    username = username,
    fullName = fullName,
    email = email,
    phone = phone,
    gender = gender,
    roleId = roleId,
    token = token,
    refreshToken = refreshToken,
    createdAt = createdAt,
    updatedAt = updatedAt,
)