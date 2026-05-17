package com.sun.kmpstartertemplaterefined.feature_core_presentation.di

import com.sun.kmpstartertemplaterefined.feature_core_presentation.viewmodels.OnboardingViewModel
import com.sun.kmpstartertemplaterefined.feature_core_presentation.viewmodels.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val corePresentationModule = module {
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::SplashViewModel)
}