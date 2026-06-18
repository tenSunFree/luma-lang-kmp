package com.sun.kmpstartertemplaterefined.core.ui.screens.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TextDark = Color(0xFF4A4A4A)

@Composable
fun MainTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "LumaLang",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextDark,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.GridView,
            contentDescription = "課表",
            tint = Color(0xFF555555),
            modifier = Modifier.size(26.dp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFFFA2B9)),
            contentAlignment = Alignment.Center,
        ) {
            Text("🤖", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(20.dp))
        Box(
            modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFF1E9BEF)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Headphones,
                contentDescription = "耳機",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}