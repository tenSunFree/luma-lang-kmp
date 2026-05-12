package com.sun.kmpstartertemplaterefined.feature_auth_presentation.di

import com.sun.kmpstartertemplaterefined.feature_auth_presentation.LoginViewModel
import com.sun.kmpstartertemplaterefined.feature_auth_presentation.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}