package com.sun.kmpstartertemplaterefined.feature_auth_domain.logics

import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository

class VerifyOtpLogic(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit> {
        if (code.length != 6) return Result.failure(IllegalArgumentException("請輸入 6 位數驗證碼"))
        return repository.verifyOtp(email = email, code = code)
    }
}