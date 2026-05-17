package com.sun.kmpstartertemplaterefined.feature_auth_data.di

import com.sun.kmpstartertemplaterefined.feature_auth_data.config.AuthConfig
import com.sun.kmpstartertemplaterefined.feature_auth_data.local.AuthSessionStorage
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.AuthRemoteDataSource
import com.sun.kmpstartertemplaterefined.feature_auth_data.remote.AuthRemoteDataSourceImpl
import com.sun.kmpstartertemplaterefined.feature_auth_data.repositories.AuthRepositoryImpl
import com.sun.kmpstartertemplaterefined.feature_auth_data.util.HttpLogger
import com.sun.kmpstartertemplaterefined.feature_auth_domain.repositories.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import com.sun.kmpstartertemplaterefined.feature_auth_data.session.AuthSessionChecker
import com.sun.kmpstartertemplaterefined.feature_core_domain.session.SessionChecker

fun authDataModule(authConfig: AuthConfig) = module {

    single(named("authHttpClient")) {
        HttpClient {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true; explicitNulls = false })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }
            install(Logging) {
                logger = HttpLogger
                level = LogLevel.BODY
            }
        }
    }

    // SecureStorage (Platform implementation injects via expect/actual)
    includes(secureStoragePlatformModule())

    // JSON (required by AuthSessionStorage)
    single {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    single {
        AuthSessionStorage(secureStorage = get(), json = get())
    }

    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(
            httpClient = get(named("authHttpClient")),
            baseUrl = authConfig.baseUrl,
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            remoteDataSource = get(),
            sessionStorage = get(),
        )
    }

    single<SessionChecker> {
        AuthSessionChecker(
            sessionStorage = get(),
            remoteDataSource = get(),
        )
    }
}