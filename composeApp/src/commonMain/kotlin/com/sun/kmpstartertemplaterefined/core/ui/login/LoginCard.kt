package com.sun.kmpstartertemplaterefined.core.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun LoginCard(
    loginMode: LoginMode,
    companyNo: String,
    account: String,
    password: String,
    rememberPassword: Boolean,
    passwordVisible: Boolean,
    onCompanyNoChange: (String) -> Unit,
    onAccountChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberPasswordChange: (Boolean) -> Unit,
    onPasswordVisibleChange: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 22.dp,
                bottom = 20.dp,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title + Close Icon
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (loginMode == LoginMode.Normal) {
                        "歡迎回來"
                    } else {
                        "簽約企業用戶登入"
                    },
                    modifier = Modifier.align(Alignment.Center),
                    color = FundayTextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "關閉",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(24.dp),
                    tint = Color(0xFFAAAAAA),
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Company ID (displayed only in company mode)
            if (loginMode == LoginMode.Enterprise) {
                LoginTextField(
                    label = "企業編號",
                    value = companyNo,
                    placeholder = "請輸入企業編號",
                    onValueChange = onCompanyNoChange,
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            // account number
            LoginTextField(
                label = "帳號",
                value = account,
                placeholder = "請輸入帳號",
                onValueChange = onAccountChange,
            )
            Spacer(modifier = Modifier.height(14.dp))
            // password
            LoginTextField(
                label = "密碼",
                value = password,
                placeholder = "",
                onValueChange = onPasswordChange,
                isPassword = true,
                passwordVisible = passwordVisible,
                onPasswordVisibleChange = onPasswordVisibleChange,
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Remember account password + Forgot link
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = rememberPassword,
                    onCheckedChange = onRememberPasswordChange,
                    modifier = Modifier.size(22.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = FundayPink,
                        uncheckedColor = Color(0xFFC8C8C8),
                        checkmarkColor = Color.White,
                    ),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "記住帳號密碼",
                    color = FundayTextDark,
                    fontSize = 15.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (loginMode == LoginMode.Normal) {
                    Text(text = "忘記帳號", color = FundayPink, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "忘記密碼", color = FundayPink, fontSize = 14.sp)
                } else {
                    Text(text = "忘記帳號/密碼", color = FundayPink, fontSize = 14.sp)
                }
            }
            // Third-party login (only displayed in normal mode)
            if (loginMode == LoginMode.Normal) {
                Spacer(modifier = Modifier.height(16.dp))
                ThirdPartySection()
            }
            Spacer(modifier = Modifier.height(18.dp))
            // Login button
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FundayPink,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = "登入",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}