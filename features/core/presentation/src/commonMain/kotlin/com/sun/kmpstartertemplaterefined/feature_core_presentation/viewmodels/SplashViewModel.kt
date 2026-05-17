package com.sun.kmpstartertemplaterefined.feature_core_presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.sun.kmpstartertemplaterefined.feature_core_domain.logics.OnboardingLogics
import com.sun.kmpstartertemplaterefined.feature_core_domain.session.SessionChecker
import com.sun.kmpstartertemplaterefined.ui_utils.viewmodels.DummyActions
import com.sun.kmpstartertemplaterefined.ui_utils.viewmodels.MviViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
internal data class SplashState(
    val isOnboarded: Boolean = false,
    val duration: Long = Random.nextLong(1500, 2500),
)

internal sealed class SplashEvents {
    data object NavigateToMain : SplashEvents()
    data object NavigateToLogin : SplashEvents()
    data object NavigateToOnboarding : SplashEvents()
}

internal class SplashViewModel(
    private val onboardingLogics: OnboardingLogics,
    private val sessionChecker: SessionChecker,
) : MviViewModel<SplashState, DummyActions, SplashEvents>() {

    override val initialState get() = SplashState()

    override fun onAction(action: DummyActions) {}

    // Remove onStateStart() and use an explicit start() instead
    private var hasStarted = false

    fun start() {
        // Prevent recomposition from triggering repeatedly
        if (hasStarted) return
        hasStarted = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(_state.value.duration)
            checkAndNavigate()
        }
    }

    private suspend fun checkAndNavigate() {
        if (!onboardingLogics.checkIsOnboarded()) {
            emitEvent(SplashEvents.NavigateToOnboarding)
            return
        }
        if (!sessionChecker.isLoggedIn()) {
            emitEvent(SplashEvents.NavigateToLogin)
            return
        }
        val refreshSuccess = sessionChecker.tryRefreshToken()
        if (refreshSuccess) {
            emitEvent(SplashEvents.NavigateToMain)
        } else {
            sessionChecker.clearSession()
            emitEvent(SplashEvents.NavigateToLogin)
        }
    }
}