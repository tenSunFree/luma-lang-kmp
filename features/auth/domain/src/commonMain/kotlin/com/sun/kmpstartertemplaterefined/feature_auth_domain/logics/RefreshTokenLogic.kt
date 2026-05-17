package com.sun.kmpstartertemplaterefined.feature_auth_domain.logics

import com.sun.kmpstartertemplaterefined.feature_auth_domain.models.UserSession
import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository

class RefreshTokenLogic(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<UserSession> = repository.refreshToken()
}