package com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories

import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterParams
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.RegisterResult

interface AuthRepository {
    suspend fun register(params: RegisterParams): Result<RegisterResult>
    suspend fun sendOtp(email: String): Result<Unit>
    suspend fun verifyOtp(email: String, code: String): Result<Unit>
}