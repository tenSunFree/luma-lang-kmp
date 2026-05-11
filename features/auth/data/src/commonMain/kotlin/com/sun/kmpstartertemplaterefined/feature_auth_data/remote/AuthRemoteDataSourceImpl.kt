package com.sun.kmpstartertemplaterefined.feature_auth_data.remote

import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.RegisterResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.SendOtpRequestDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.SimpleAuthResponseDto
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.dto.VerifyOtpRequestDto
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

    override suspend fun register(request: RegisterRequestDto): RegisterResponseDto {
        return httpClient.post("$baseUrl/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun sendOtp(request: SendOtpRequestDto): SimpleAuthResponseDto {
        return httpClient.post("$baseUrl/auth/send-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    override suspend fun verifyOtp(request: VerifyOtpRequestDto): SimpleAuthResponseDto {
        return httpClient.post("$baseUrl/auth/verify-otp") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}