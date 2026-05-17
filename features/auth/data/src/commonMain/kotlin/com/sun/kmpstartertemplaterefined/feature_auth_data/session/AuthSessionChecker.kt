package com.sun.kmpstartertemplaterefined.feature_auth_data.session

import com.sun.kmpstartertemplaterefined.feature_auth_data.local.AuthSessionStorage
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.AuthRemoteDataSource
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RefreshTokenRequestDto
import com.sun.kmpstartertemplaterefined.feature_core_domain.session.SessionChecker

class AuthSessionChecker(
    private val sessionStorage: AuthSessionStorage,
    private val remoteDataSource: AuthRemoteDataSource,
) : SessionChecker {

    override suspend fun isLoggedIn(): Boolean =
        sessionStorage.isLoggedIn()

    override suspend fun tryRefreshToken(): Boolean {
        val session = sessionStorage.getSession() ?: return false
        return runCatching {
            val response = remoteDataSource.refreshToken(
                RefreshTokenRequestDto(refreshToken = session.refreshToken)
            )
            if (!response.status) error("refresh failed")
            val data = response.data ?: error("no data")
            // Only the token pair is updated; user information remains unchanged.
            sessionStorage.updateTokens(
                token = data.token,
                refreshToken = data.refreshToken,
            )
        }.isSuccess
    }

    override suspend fun clearSession() {
        sessionStorage.clearSession()
    }
}