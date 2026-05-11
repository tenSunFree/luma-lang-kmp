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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.sun.kmpstartertemplaterefined.feature_auth_presentation.RegisterAction
import com.sun.kmpstartertemplaterefined.feature_auth_presentation.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onGetStartedClick: () -> Unit,
    onRegisterSuccess: () -> Unit = {},
    registerViewModel: RegisterViewModel = koinViewModel(),
) {
    val registerState by registerViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var overlayState by remember { mutableStateOf(LoginOverlayState.None) }
    var loginMode by remember { mutableStateOf(LoginMode.Normal) }
    var companyNo by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    LaunchedEffect(registerState.isSuccess) {
        if (registerState.isSuccess) {
            overlayState = LoginOverlayState.Login
            loginMode = LoginMode.Normal
            registerViewModel.reset()
        }
    }
    LaunchedEffect(registerState.errorMessage) {
        registerState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            registerViewModel.onAction(RegisterAction.ErrorShown)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LumaLangBlue),
    ) {
        LumaLangLoginBackground()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 26.dp),
        ) {
            LoginTopBar(
                modifier = Modifier.align(Alignment.TopCenter),
                onMemberLoginClick = {
                    loginMode = LoginMode.Normal
                    overlayState = LoginOverlayState.Login
                },
            )
            when (overlayState) {
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

                LoginOverlayState.Register -> {
                    RegisterCard(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(),
                        // Form fields
                        email = registerState.email,
                        username = registerState.username,
                        password = registerState.password,
                        passwordVisible = registerState.passwordVisible,
                        phone = registerState.phone,
                        fullName = registerState.fullName,
                        selectedGender = registerState.gender,
                        // OTP
                        otpCode = registerState.otpCode,
                        step = registerState.step,
                        // Shared
                        isLoading = registerState.isLoading,
                        // Form callbacks
                        onEmailChange = { registerViewModel.onAction(RegisterAction.EmailChanged(it)) },
                        onUsernameChange = {
                            registerViewModel.onAction(
                                RegisterAction.UsernameChanged(
                                    it
                                )
                            )
                        },
                        onPasswordChange = {
                            registerViewModel.onAction(
                                RegisterAction.PasswordChanged(
                                    it
                                )
                            )
                        },
                        onTogglePasswordVisible = { registerViewModel.onAction(RegisterAction.TogglePasswordVisible) },
                        onPhoneChange = { registerViewModel.onAction(RegisterAction.PhoneChanged(it)) },
                        onFullNameChange = {
                            registerViewModel.onAction(
                                RegisterAction.FullNameChanged(
                                    it
                                )
                            )
                        },
                        onGenderSelect = {
                            registerViewModel.onAction(
                                RegisterAction.GenderChanged(
                                    it
                                )
                            )
                        },
                        // OTP callbacks
                        onOtpCodeChange = {
                            registerViewModel.onAction(
                                RegisterAction.OtpCodeChanged(
                                    it
                                )
                            )
                        },
                        onVerifyOtpClick = { registerViewModel.onAction(RegisterAction.VerifyOtpClicked) },
                        onResendOtpClick = { registerViewModel.onAction(RegisterAction.ResendOtpClicked) },
                        // shared callbacks
                        onCloseClick = { overlayState = LoginOverlayState.None },
                        onSubmitClick = { registerViewModel.onAction(RegisterAction.SubmitClicked) },
                    )
                }

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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}