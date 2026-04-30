package com.sun.kmpstartertemplaterefined.core.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SwitchLoginModeButton(
    loginMode: LoginMode,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.72f)
                .height(48.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            color = FundaySwitchBg,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (loginMode == LoginMode.Normal) {
                        "切換為企業用戶登入"
                    } else {
                        "切換為一般用戶登入"
                    },
                    color = Color(0xFF0C8FE8),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}