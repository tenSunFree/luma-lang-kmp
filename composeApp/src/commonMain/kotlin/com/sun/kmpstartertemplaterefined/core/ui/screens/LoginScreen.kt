package com.sun.kmpstartertemplaterefined.core.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sun.kmpstartertemplaterefined.core.ui.login.FundayBlue
import com.sun.kmpstartertemplaterefined.core.ui.login.FundayLoginBackground
import com.sun.kmpstartertemplaterefined.core.ui.login.LoginCard
import com.sun.kmpstartertemplaterefined.core.ui.login.LoginMode
import com.sun.kmpstartertemplaterefined.core.ui.login.LoginTopBar
import com.sun.kmpstartertemplaterefined.core.ui.login.SwitchLoginModeButton

@Composable
fun LoginScreen(
    onGetStartedClick: () -> Unit,
) {
    var loginMode by remember { mutableStateOf(LoginMode.Normal) }
    var companyNo by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundayBlue),
    ) {
        FundayLoginBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 26.dp),
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            LoginTopBar()
            Spacer(modifier = Modifier.weight(0.6f))
            LoginCard(
                loginMode = loginMode,
                companyNo = companyNo,
                account = account,
                password = password,
                rememberPassword = rememberPassword,
                passwordVisible = passwordVisible,
                onCompanyNoChange = { companyNo = it },
                onAccountChange = { account = it },
                onPasswordChange = { password = it },
                onRememberPasswordChange = { rememberPassword = it },
                onPasswordVisibleChange = { passwordVisible = it },
                onLoginClick = onGetStartedClick,
            )
            Spacer(modifier = Modifier.weight(0.35f))
            SwitchLoginModeButton(
                loginMode = loginMode,
                onClick = {
                    loginMode = when (loginMode) {
                        LoginMode.Normal -> LoginMode.Enterprise
                        LoginMode.Enterprise -> LoginMode.Normal
                    }
                },
            )
            Spacer(modifier = Modifier.weight(0.35f))
        }
    }
}