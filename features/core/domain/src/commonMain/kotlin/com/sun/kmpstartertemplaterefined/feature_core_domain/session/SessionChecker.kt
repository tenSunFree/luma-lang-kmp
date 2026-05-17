package com.sun.kmpstartertemplaterefined.feature_core_domain.session

/**
 * core/presentation only depends on this interface, not directly on feature_auth_domain.
 * Implementation is provided by AuthSessionChecker of feature_auth_data.
 */
interface SessionChecker {
    /** Read the local session and determine if the user is logged in */
    suspend fun isLoggedIn(): Boolean

    /**
     * Try exchanging your refresh token for a new token pair.
     * @return true = Refresh successful, you can now access the homepage; false = Refresh failed, you need to return to the login page.
     */
    suspend fun tryRefreshToken(): Boolean

    /** Clear the local session (called if refresh fails) */
    suspend fun clearSession()
}