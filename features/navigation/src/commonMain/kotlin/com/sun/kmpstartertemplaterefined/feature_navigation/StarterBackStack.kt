package com.sun.kmpstartertemplaterefined.feature_navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.sun.kmpstartertemplaterefined.feature_navigation.screens.StarterScreens
import com.sun.kmpstartertemplaterefined.feature_navigation.utils.rememberNavBackStack
import kotlinx.serialization.modules.subclass

@Composable
fun rememberStarterBackStack(vararg initialScreens: NavKey): NavBackStack<NavKey> {
    val backstack = rememberNavBackStack(
        elements = initialScreens
    ) {
        subclass(StarterScreens.Welcome::class)
        subclass(StarterScreens.Purchases::class)
        subclass(StarterScreens.Onboarding::class)
        subclass(StarterScreens.Splash::class)
        subclass(StarterScreens.Login::class)
        // add other screens here
    }
    return backstack
}