package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BorderGray = Color(0xFFE5E5E5)

@Composable
fun TabSearchBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Search...",
            color = Color(0xFFAAAAAA),
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "搜尋",
            tint = Color(0xFF888888),
            modifier = Modifier.size(24.dp),
        )
    }
}