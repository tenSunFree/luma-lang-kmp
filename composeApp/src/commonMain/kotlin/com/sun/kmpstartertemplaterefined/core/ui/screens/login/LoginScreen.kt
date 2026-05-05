package com.sun.kmpstartertemplaterefined.core.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LumaLangBlue
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LumaLangLoginBackground
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LumaLangPink
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LoginCard
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LoginMode
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.LoginTopBar
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.RegisterCard
import com.sun.kmpstartertemplaterefined.core.ui.screens.login.components.SwitchLoginModeButton

@Composable
fun LoginScreen(
    onGetStartedClick: () -> Unit,
    onRegisterSubmitClick: () -> Unit = {},
) {
    // Screen Status
    var overlayState by remember { mutableStateOf(LoginOverlayState.None) }
    // LoginCard status
    var loginMode by remember { mutableStateOf(LoginMode.Normal) }
    var companyNo by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    // RegisterCard status
    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("男") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LumaLangBlue),
    ) {
        // Background image
        LumaLangLoginBackground()
        // Main content layer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 26.dp),
        ) {
            // Always displayed in the TopBar
            LoginTopBar(
                modifier = Modifier.align(Alignment.TopCenter),
                onMemberLoginClick = {
                    // Resets to normal mode every time you click member login
                    loginMode = LoginMode.Normal
                    overlayState = LoginOverlayState.Login
                },
            )
            // Display corresponding content based on status
            when (overlayState) {
                // Initial screen: Only the "Join for Free" button is displayed at the bottom.
                LoginOverlayState.None -> {
                    Button(
                        onClick = { overlayState = LoginOverlayState.Register },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 32.dp)
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LumaLangPink,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text = "免費加入",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                // After clicking "Join for Free": RegisterCard is displayed.
                LoginOverlayState.Register -> {
                    RegisterCard(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        phone = phone,
                        name = name,
                        selectedGender = gender,
                        onPhoneChange = { phone = it },
                        onNameChange = { name = it },
                        onGenderSelect = { gender = it },
                        onCloseClick = { overlayState = LoginOverlayState.None },
                        onSubmitClick = onRegisterSubmitClick,
                    )
                }
                // After clicking "Member Login": LoginCard + SwitchLoginModeButton will be displayed.
                LoginOverlayState.Login -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
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
                            onCloseClick = { overlayState = LoginOverlayState.None },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SwitchLoginModeButton(
                            loginMode = loginMode,
                            onClick = {
                                loginMode = when (loginMode) {
                                    LoginMode.Normal -> LoginMode.Enterprise
                                    LoginMode.Enterprise -> LoginMode.Normal
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}