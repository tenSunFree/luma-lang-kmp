package com.sun.kmpstartertemplaterefined.feature_auth_data.repositories

import com.sun.kmpstartertemplaterefined.feature_auth_data.mappers.toDomain
import com.sun.kmpstartertemplaterefined.feature_auth_data.mappers.toDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.AuthRemoteDataSource
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.SendOtpRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.VerifyOtpRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterParams
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterResult
import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource,
) : AuthRepository {

    override suspend fun register(params: RegisterParams): Result<RegisterResult> =
        runCatching {
            val response = remoteDataSource.register(params.toDto())
            if (!response.status) error(response.message.ifBlank { "註冊失敗" })
            response.toDomain()
        }

    override suspend fun sendOtp(email: String): Result<Unit> =
        runCatching {
            val response = remoteDataSource.sendOtp(SendOtpRequestDto(email = email))
            if (!response.status) error(response.message.ifBlank { "驗證碼發送失敗" })
        }

    override suspend fun verifyOtp(email: String, code: String): Result<Unit> =
        runCatching {
            val response = remoteDataSource.verifyOtp(
                VerifyOtpRequestDto(email = email, code = code)
            )
            if (!response.status) error(response.message.ifBlank { "驗證碼驗證失敗" })
        }
}