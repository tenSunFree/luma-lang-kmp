package com.sun.kmpstartertemplaterefined.core.ui.screens.main.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Pink = Color(0xFFFF3F68)
private val TextGray = Color(0xFF777777)
private val BorderGray = Color(0xFFE5E5E5)

private val funTvOptions = listOf(
    "News", "Music", "Video", "Trend",
    "Living", "Office", "Column", "Story",
)

@Composable
fun FunTvTab() {
    val selected = remember { mutableStateListOf("News", "Music", "Video") }
    var rememberPrefs by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp, vertical = 24.dp),
    ) {
        funTvOptions.forEach { option ->
            FunTvOptionRow(
                title = option,
                isSelected = selected.contains(option),
                onClick = {
                    if (selected.contains(option)) selected.remove(option)
                    else selected.add(option)
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Checkbox(
                checked = rememberPrefs,
                onCheckedChange = { rememberPrefs = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Pink,
                    checkmarkColor = Color.White,
                ),
            )
            Text(text = "Remember preferences", color = TextGray, fontSize = 17.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Pink,
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Play", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FunTvOptionRow(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, BorderGray, RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, color = TextGray, fontSize = 18.sp, modifier = Modifier.weight(1f))
        if (isSelected) {
            Box(
                modifier = Modifier.size(26.dp).clip(CircleShape).background(Pink),
                contentAlignment = Alignment.Center,
            ) {
                Text("✓", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color(0xFFCCCCCC), CircleShape),
            )
        }
    }
}