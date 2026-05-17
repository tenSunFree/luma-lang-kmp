package com.sun.kmpstartertemplaterefined.feature_auth_data.remote

import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String,
) : AuthRemoteDataSource {

    override suspend fun login(request: LoginRequestDto): LoginResponseDto =
        httpClient.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun refreshToken(request: RefreshTokenRequestDto): LoginResponseDto =
        httpClient.post("$baseUrl/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun register(request: RegisterRequestDto): RegisterResponseDto =
        httpClient.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun sendOtp(request: SendOtpRequestDto): SimpleAuthResponseDto =
        httpClient.post("$baseUrl/auth/send-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    override suspend fun verifyOtp(request: VerifyOtpRequestDto): SimpleAuthResponseDto =
        httpClient.post("$baseUrl/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}