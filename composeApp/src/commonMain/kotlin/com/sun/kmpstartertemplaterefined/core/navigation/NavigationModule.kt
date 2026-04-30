package com.sun.kmpstartertemplaterefined.core.navigation

import com.sun.kmpstartertemplaterefined.core.ui.screens.LoginScreen
import com.sun.kmpstartertemplaterefined.core.ui.screens.WelcomeScreen
import com.sun.kmpstartertemplaterefined.feature_core_presentation.screens.OnboardingV1Screen
import com.sun.kmpstartertemplaterefined.feature_core_presentation.screens.SplashScreen
import com.sun.kmpstartertemplaterefined.feature_navigation.StarterNavigator
import com.sun.kmpstartertemplaterefined.feature_navigation.di.navigationCoreModule
import com.sun.kmpstartertemplaterefined.feature_navigation.screens.StarterScreens
import com.sun.kmpstartertemplaterefined.feature_purchases_presentation.ui.screens.PurchasesScreen
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {
    includes(navigationCoreModule)
    navigation<StarterScreens.Welcome> { route ->
        val navigator = StarterNavigator.getCurrent()
        WelcomeScreen(
            onGetStartedClick = {
                navigator.navigateTo(
                    route = StarterScreens.Purchases
                )
            }
        )
    }
    navigation<StarterScreens.Splash> { route ->
        val navigator = StarterNavigator.getCurrent()
        SplashScreen(
            onNavigate = {
                navigator.popAndNavigate(
                    route = StarterScreens.Login
                )
            },
            onNavigateToOnboarding = {
                navigator.popAndNavigate(
                    route = StarterScreens.Onboarding
                )
            }
        )
    }
    navigation<StarterScreens.Onboarding> { route ->
        val navigator = StarterNavigator.getCurrent()
        OnboardingV1Screen(
            onNavigate = {
                navigator.popAndNavigate(
                    route = StarterScreens.Welcome
                )
            }
        )
    }
    navigation<StarterScreens.Purchases> { route ->
        val navigator = StarterNavigator.getCurrent()
        PurchasesScreen(
            onNavigate = {
                navigator.navigateUp()
            }
        )
    }
    navigation<StarterScreens.Login> { route ->
        val navigator = StarterNavigator.getCurrent()
        LoginScreen(
            onGetStartedClick = {
                navigator.navigateTo(route = StarterScreens.Purchases)
            }
        )
    }
}