package com.sun.kmpstartertemplaterefined.feature_auth_domain.di

import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.LoginLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.RegisterUserLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.SendOtpLogic
import com.sun.kmpstartertemplaterefined.feature_auth_domain.logics.VerifyOtpLogic
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authDomainModule = module {
    singleOf(::LoginLogic)
    singleOf(::RegisterUserLogic)
    singleOf(::SendOtpLogic)
    singleOf(::VerifyOtpLogic)
}