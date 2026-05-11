package com.sun.kmpstartertemplaterefined.feature_auth_domain.logics

import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository

class SendOtpLogic(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email 不能為空"))
        return repository.sendOtp(email)
    }
}