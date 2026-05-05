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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
internal fun RegisterCard(
    modifier: Modifier = Modifier,
    phone: String,
    name: String,
    selectedGender: String,
    onPhoneChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onGenderSelect: (String) -> Unit,
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
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 22.dp,
                bottom = 20.dp,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Title + X button
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "免費加入",
                    modifier = Modifier.align(Alignment.Center),
                    color = LumaLangTextDark,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(36.dp),
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
            // phone number
            LoginTextField(
                label = "手機號碼",
                value = phone,
                placeholder = "請輸入手機號碼",
                onValueChange = onPhoneChange,
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Name + Gender (side by side)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                // Name
                Box(modifier = Modifier.weight(1f)) {
                    LoginTextField(
                        label = "姓名",
                        value = name,
                        placeholder = "請輸入姓名",
                        onValueChange = onNameChange,
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                // gender
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
                            colors = RadioButtonDefaults.colors(
                                selectedColor = LumaLangPink,
                            ),
                        )
                        Text(text = "男", fontSize = 15.sp, color = LumaLangTextDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = selectedGender == "女",
                            onClick = { onGenderSelect("女") },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = LumaLangPink,
                            ),
                        )
                        Text(text = "女", fontSize = 15.sp, color = LumaLangTextDark)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            // Other joining methods (share ThirdPartySection, change the label to "Other joining methods")
            ThirdPartySection(label = "其他加入方式")
            Spacer(modifier = Modifier.height(20.dp))
            // Send button
            Button(
                onClick = onSubmitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LumaLangPink,
                    contentColor = Color.White,
                ),
            ) {
                Text(text = "送出", fontSize = 17.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(14.dp))
            // Terms of Service
            Text(
                text = "加入即代表同意LumaLang的服務條款與隱私權",
                color = Color(0xFFB8B8B8),
                fontSize = 13.sp,
            )
        }
    }
}