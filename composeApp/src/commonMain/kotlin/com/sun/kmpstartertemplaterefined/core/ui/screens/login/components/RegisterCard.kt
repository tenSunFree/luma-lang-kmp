package com.sun.kmpstartertemplaterefined.core.ui.screens.login.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sun.kmpstartertemplaterefined.feature_auth_presentation.RegisterStep

@Composable
internal fun RegisterCard(
    modifier: Modifier = Modifier,
    // Form fields
    email: String,
    username: String,
    password: String,
    passwordVisible: Boolean,
    phone: String,
    fullName: String,
    selectedGender: String,
    // OTP
    otpCode: String,
    step: RegisterStep,
    // Shared
    isLoading: Boolean,
    // Form callbacks
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisible: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onGenderSelect: (String) -> Unit,
    // OTP callbacks
    onOtpCodeChange: (String) -> Unit,
    onVerifyOtpClick: () -> Unit,
    onResendOtpClick: () -> Unit,
    // shared callbacks
    onCloseClick: () -> Unit,
    onSubmitClick: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header column (shared by FORM / OTP)
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (step == RegisterStep.FORM) "免費加入" else "驗證信箱",
                    modifier = Modifier.align(Alignment.Center),
                    color = LumaLangTextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.align(Alignment.CenterEnd).size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "關閉",
                        modifier = Modifier.size(24.dp),
                        tint = Color(0xFFAAAAAA),
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Switch content based on step
            when (step) {
                RegisterStep.FORM -> RegisterFormContent(
                    email = email,
                    username = username,
                    password = password,
                    passwordVisible = passwordVisible,
                    phone = phone,
                    fullName = fullName,
                    selectedGender = selectedGender,
                    isLoading = isLoading,
                    onEmailChange = onEmailChange,
                    onUsernameChange = onUsernameChange,
                    onPasswordChange = onPasswordChange,
                    onTogglePasswordVisible = onTogglePasswordVisible,
                    onPhoneChange = onPhoneChange,
                    onFullNameChange = onFullNameChange,
                    onGenderSelect = onGenderSelect,
                    onSubmitClick = onSubmitClick,
                )

                RegisterStep.OTP -> RegisterOtpContent(
                    email = email,
                    otpCode = otpCode,
                    isLoading = isLoading,
                    onOtpCodeChange = onOtpCodeChange,
                    onVerifyOtpClick = onVerifyOtpClick,
                    onResendOtpClick = onResendOtpClick,
                )
            }
        }
    }
}

// ── 表單內容（原本的欄位） ──────────────────────────────────────────────────

@Composable
private fun RegisterFormContent(
    email: String,
    username: String,
    password: String,
    passwordVisible: Boolean,
    phone: String,
    fullName: String,
    selectedGender: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisible: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onGenderSelect: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    LoginTextField(
        label = "Email",
        value = email,
        placeholder = "請輸入 Email",
        onValueChange = onEmailChange,
    )

    Spacer(modifier = Modifier.height(16.dp))

    LoginTextField(
        label = "帳號",
        value = username,
        placeholder = "請輸入帳號",
        onValueChange = onUsernameChange,
    )

    Spacer(modifier = Modifier.height(16.dp))

    LoginTextField(
        label = "密碼",
        value = password,
        placeholder = "請輸入密碼",
        onValueChange = onPasswordChange,
        isPassword = true,
        passwordVisible = passwordVisible,
        onPasswordVisibleChange = { onTogglePasswordVisible() },
    )

    Spacer(modifier = Modifier.height(16.dp))

    LoginTextField(
        label = "手機號碼",
        value = phone,
        placeholder = "請輸入手機號碼",
        onValueChange = onPhoneChange,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Box(modifier = Modifier.weight(1f)) {
            LoginTextField(
                label = "姓名",
                value = fullName,
                placeholder = "請輸入姓名",
                onValueChange = onFullNameChange,
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "性別",
                color = LumaLangTextDark,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedGender == "男",
                    onClick = { onGenderSelect("男") },
                    colors = RadioButtonDefaults.colors(selectedColor = LumaLangPink),
                )
                Text(text = "男", fontSize = 15.sp, color = LumaLangTextDark)
                Spacer(modifier = Modifier.width(8.dp))
                RadioButton(
                    selected = selectedGender == "女",
                    onClick = { onGenderSelect("女") },
                    colors = RadioButtonDefaults.colors(selectedColor = LumaLangPink),
                )
                Text(text = "女", fontSize = 15.sp, color = LumaLangTextDark)
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    ThirdPartySection(label = "其他加入方式")

    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = onSubmitClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LumaLangPink,
            contentColor = Color.White,
            disabledContainerColor = LumaLangPink.copy(alpha = 0.6f),
            disabledContentColor = Color.White,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(text = "送出", fontSize = 17.sp, fontWeight = FontWeight.Medium)
        }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
        text = "加入即代表同意LumaLang的服務條款與隱私權",
        color = Color(0xFFB8B8B8),
        fontSize = 13.sp,
    )
}

// ── OTP 驗證內容 ───────────────────────────────────────────────────────────

@Composable
private fun RegisterOtpContent(
    email: String,
    otpCode: String,
    isLoading: Boolean,
    onOtpCodeChange: (String) -> Unit,
    onVerifyOtpClick: () -> Unit,
    onResendOtpClick: () -> Unit,
) {
    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "驗證碼已寄送至",
        color = Color(0xFF777777),
        fontSize = 14.sp,
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = email,
        color = LumaLangPink,
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(24.dp))

    LoginTextField(
        label = "驗證碼",
        value = otpCode,
        placeholder = "請輸入 6 位數驗證碼",
        onValueChange = onOtpCodeChange,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onVerifyOtpClick,
        enabled = !isLoading && otpCode.length == 6,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LumaLangPink,
            contentColor = Color.White,
            disabledContainerColor = LumaLangPink.copy(alpha = 0.6f),
            disabledContentColor = Color.White,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(text = "確認驗證", fontSize = 17.sp, fontWeight = FontWeight.Medium)
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    TextButton(
        onClick = onResendOtpClick,
        enabled = !isLoading,
    ) {
        Text(
            text = "重新發送驗證碼",
            color = if (isLoading) Color(0xFFCCCCCC) else LumaLangPink,
            fontSize = 14.sp,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
}