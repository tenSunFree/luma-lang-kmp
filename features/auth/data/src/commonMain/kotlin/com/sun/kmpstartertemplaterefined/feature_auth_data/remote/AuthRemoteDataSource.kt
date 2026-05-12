package com.sun.kmpstartertemplaterefined.feature_auth_data.remote

import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.LoginRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.LoginResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.SendOtpRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.SimpleAuthResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.VerifyOtpRequestDto

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): LoginResponseDto
    suspend fun register(request: RegisterRequestDto): RegisterResponseDto
    suspend fun sendOtp(request: SendOtpRequestDto): SimpleAuthResponseDto
    suspend fun verifyOtp(request: VerifyOtpRequestDto): SimpleAuthResponseDto
}