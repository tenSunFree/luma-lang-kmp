package com.sun.kmpstartertemplaterefined.feature_auth_domain.logics

import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.LoginParams
import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.LoginResult
import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository

class LoginLogic(private val repository: AuthRepository) {
    suspend operator fun invoke(params: LoginParams): Result<LoginResult> {
        if (params.email.isBlank()) return Result.failure(IllegalArgumentException("請輸入 Email"))
        if (params.password.isBlank()) return Result.failure(IllegalArgumentException("請輸入密碼"))
        return repository.login(params)
    }
}